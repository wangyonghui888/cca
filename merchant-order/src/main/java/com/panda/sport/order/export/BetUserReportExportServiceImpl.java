package com.panda.sport.order.export;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.panda.sport.merchant.common.po.merchant.UserOrderAllPO;
import com.panda.sport.merchant.common.vo.BetOrderVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import com.panda.sport.merchant.common.vo.merchant.OrderSettle;
import com.panda.sport.order.feign.MerchantReportClient;
import com.panda.sport.order.service.UserOrderService;
import com.panda.sport.order.service.expot.AbstractOrderFileExportService;
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
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author :  javier
 * --------  ---------  --------------------------
 */
@Service("betUserReportExportServiceImpl")
@Slf4j
public class BetUserReportExportServiceImpl extends AbstractOrderFileExportService {

    @Autowired
    private UserOrderService userOrderService;

    @Override
    @Async()
    public void export(MerchantFile merchantFile) {
        long rate = 10;
        InputStream inputStream = null;
        try {
            log.info("开始投注用户管理导出大文件开始导出注单 param = {}", merchantFile.getExportParam());
            if (super.checkTask(merchantFile.getId())) {
                log.info("当前任务被删除！");
                return;
            }
            super.updateRate(merchantFile.getId(), rate);

            UserOrderVO vo = JSON.parseObject(merchantFile.getExportParam(), UserOrderVO.class);
            long startTL = System.currentTimeMillis();
            List<UserOrderAllPO> userOrderList = new ArrayList<>();
            if (vo.getPageSize() > 1000000) {
                throw new RuntimeException("导出数据大于1000000条！");
            }
            if(20000 < vo.getPageSize() && 1000000 > vo.getPageSize() ){
                   int thrednum = (vo.getPageSize() % 20000 == 0) ? (vo.getPageSize() / 20000) : (vo.getPageSize() / 20000 + 1);
                    ExecutorService service = Executors.newFixedThreadPool(thrednum);
                    log.info("export  thrednum=" + thrednum);
                    try {
                        BlockingQueue<Future<List<UserOrderAllPO>>> queue = new LinkedBlockingQueue<>();
                        for (int i = 0; i < thrednum; i++) {
                            Integer endLoopSize = 0;
                            if(i==thrednum-1){
                                endLoopSize = vo.getPageSize()-i*20000;
                            }
                            Future<List<UserOrderAllPO>> future = service.submit(read2List(i, vo));
                            queue.add(future);
                        }
                        int queueSize = queue.size();
                        log.info("export  queueSize=" + queueSize);
                        for (int i = 0; i < queueSize; i++) {
                            List<UserOrderAllPO> list = queue.take().get();
                            log.info("export  list=" + (list == null ? 0 : list.size()));
                            userOrderList.addAll(list);
                        }
                        log.info("投注用户管理导出export  userOrderListList=" + userOrderList.size());
                    } finally {
                        service.shutdown();
                    }
                	super.updateRate(merchantFile.getId(), 80L);
					log.info(":查询结束,花费时间" + (System.currentTimeMillis() - startTL));
					if (CollectionUtils.isEmpty(userOrderList)) {
						throw new Exception("未查询到数据");
					}
                 }else{
                       vo.setStart(0);
                        Response res = userOrderService.queryUserBetList(vo, vo.getLanguage(), 1);
                        PageInfo pageInfo = new PageInfo<>();
                        if (null != res.getData()) {
                            pageInfo = (PageInfo) res.getData();
                        }
                        if(CollectionUtils.isNotEmpty(pageInfo.getList())){
                            userOrderList.addAll(pageInfo.getList());
                        }
                  }
					inputStream = new ByteArrayInputStream(betUserReportExportToCsvToCsv(userOrderList,vo));
					super.uploadFile(merchantFile, inputStream);
					log.info("导出结束,花费时间: {}", (System.currentTimeMillis() - startTL));
					super.updateFileStatusEnd(merchantFile.getId());
        } catch (Exception e) {
            super.exportFail(merchantFile.getId(), e.getMessage());
            log.error("开始投注用户管理导出异常!", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    log.error("流关闭异常！");
                }
            }
        }
    }

    private Callable<List<UserOrderAllPO>> read2List(final int i,  final UserOrderVO vo) {
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
                        Response res = userOrderService.queryUserBetList(parms, parms.getLanguage(), 1);
                        PageInfo pageInfo = new PageInfo<>();
                        if (null != res.getData()) {
                            pageInfo = (PageInfo) res.getData();
                        }
                        if(CollectionUtils.isNotEmpty(pageInfo.getList())){
                            list.addAll(pageInfo.getList());
                        }

                    } catch (Exception e) {
                        log.error("投注用户管理导出异常！开始补偿", e);
                        int i = 10;
                        while (i > 0) {
                            i--;
                            try {
                                Response res = userOrderService.queryUserBetList(parms, parms.getLanguage(), 1);
                                PageInfo pageInfo = new PageInfo<>();
                                if (null != res.getData()) {
                                    pageInfo = (PageInfo) res.getData();
                                }
                                log.info("投注用户管理read2List list=" + list.size());
                                if(CollectionUtils.isNotEmpty(pageInfo.getList())){
                                    list.addAll(pageInfo.getList());
                                }
                                i = 0;
                            } catch (Exception e1) {
                                log.error("投注用户管理导出异常！,补偿错误！", e1);
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
