package com.panda.sport.order.export;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.panda.sport.backup.mapper.BackupTUserMapper;
import com.panda.sport.merchant.common.dto.UserAllowListReq;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.po.merchant.UserOrderAllPO;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import com.panda.sport.order.service.UserAllowListService;
import com.panda.sport.order.service.expot.AbstractOrderFileExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author :  istio
 * --------  ---------  --------------------------
 */
@Service("betUserAllowListReportExportServiceImpl")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BetUserAllowListReportExportServiceImpl extends AbstractOrderFileExportService {

    private final UserAllowListService userAllowListService;
    private final BackupTUserMapper backupUserMapper;


    @Override
    @Async()
    public void export(MerchantFile merchantFile) {
        long rate = 10;
        InputStream inputStream = null;
        try {
            log.info("开始投注用户白名单导出大文件开始导出 param = {}", merchantFile.getExportParam());
            if (super.checkTask(merchantFile.getId())) {
                log.info("当前任务被删除！");
                return;
            }
            super.updateRate(merchantFile.getId(), rate);

            UserAllowListReq vo = JSON.parseObject(merchantFile.getExportParam(), UserAllowListReq.class);
            int total = backupUserMapper.countUserAllowList(vo);
            long startTL = System.currentTimeMillis();
            List<UserOrderAllPO> userOrderList = new ArrayList<>();
            if (total > 1000000) {
                throw new RuntimeException("导出数据大于1000000条！");
            }
            if (20000 < total && 1000000 > total) {
                int thrednum = (total % 20000 == 0) ? (total / 20000) : (total / 20000 + 1);
                ExecutorService service = Executors.newFixedThreadPool(thrednum);
                log.info("export  thrednum=" + thrednum);
                try {
                    BlockingQueue<Future<List<UserOrderAllPO>>> queue = new LinkedBlockingQueue<>();
                    for (int i = 0; i < thrednum; i++) {
                        Integer endLoopSize = 0;
                        if (i == thrednum - 1) {
                            endLoopSize = total - i * 20000;
                        }
                        Future<List<UserOrderAllPO>> future = service.submit(read2List(i, vo, merchantFile.getOperatName()));
                        queue.add(future);
                    }
                    int queueSize = queue.size();
                    log.info("export  queueSize=" + queueSize);
                    for (int i = 0; i < queueSize; i++) {
                        List<UserOrderAllPO> list = queue.take().get();
                        log.info("export  list=" + (list == null ? 0 : list.size()));
                        userOrderList.addAll(list);
                    }
                    log.info("投注用户白名单导出export  userOrderListList=" + userOrderList.size());
                } finally {
                    service.shutdown();
                }
                super.updateRate(merchantFile.getId(), 80L);
                log.info(":查询结束,花费时间" + (System.currentTimeMillis() - startTL));
                if (CollectionUtils.isEmpty(userOrderList)) {
                    throw new Exception("未查询到数据");
                }
            } else {
                PageInfo<?> pageInfo = userAllowListService.listAll(merchantFile.getOperatName(), vo, vo.getLanguage());
                if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
                    userOrderList.addAll((List<UserOrderAllPO>) pageInfo.getList());
                }
            }
            UserOrderVO vo1 = new UserOrderVO();
            vo1.setLanguage(vo.getLanguage());
            inputStream = new ByteArrayInputStream(betAllowListUserReportExportToCsvToCsv(userOrderList, vo1));
            super.uploadFile(merchantFile, inputStream);
            log.info("导出结束,花费时间: {}", (System.currentTimeMillis() - startTL));
            super.updateFileStatusEnd(merchantFile.getId());
        } catch (Exception e) {
            super.exportFail(merchantFile.getId(), e.getMessage());
            log.error("开始投注用户白名单导出异常!", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.error("流关闭异常！", e);
                }
            }
        }
    }

    private Callable<List<UserOrderAllPO>> read2List(final int i, final UserAllowListReq vo, String merchantName) {
        long starTime = System.currentTimeMillis();
        Callable<List<UserOrderAllPO>> callable = new Callable<List<UserOrderAllPO>>() {
            @Override
            public List<UserOrderAllPO> call() throws Exception {
                List<UserOrderAllPO> list = new ArrayList<>();
                int startIndex = i * 20000;
                for (int j = 0; j < 10; j++) {
                    UserOrderVO parms = new UserOrderVO();
                    BeanUtils.copyProperties(vo, parms);
                    parms.setStart(startIndex + (j * 2000));
                    parms.setPageSize(2000);
                    try {
                        PageInfo<?> pageInfo = userAllowListService.listAll(merchantName, vo, vo.getLanguage());
                        if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
                            list.addAll((List<UserOrderAllPO>) pageInfo.getList());
                        }

                    } catch (Exception e) {
                        log.error("投注用户白名单导出异常！开始补偿", e);
                        int i = 10;
                        while (i > 0) {
                            i--;
                            try {
                                PageInfo<?> pageInfo = userAllowListService.listAll(merchantName, vo, vo.getLanguage());
                                if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
                                    list.addAll((List<UserOrderAllPO>) pageInfo.getList());
                                }
                                log.info("用户白名单 read2List list=" + list.size());
                                i = 0;
                            } catch (Exception e1) {
                                log.error("投注用户白名单导出异常！,补偿错误！", e1);
                            }
                        }
                    }
                }
                return list;
            }
        };
        long time = System.currentTimeMillis() - starTime;
        if (time > 300000) {
            log.info("数据库查询订单耗时 {}", time / 1000);
        }
        return callable;
    }
}
