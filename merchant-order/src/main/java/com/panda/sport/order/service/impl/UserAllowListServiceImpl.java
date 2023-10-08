package com.panda.sport.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.panda.sport.backup.mapper.BackupTUserMapper;
import com.panda.sport.bss.mapper.UserOrderUpdateMapper;
import com.panda.sport.merchant.common.dto.UserAllowListReq;
import com.panda.sport.merchant.common.enums.UserAllowListSourceEnum;
import com.panda.sport.merchant.common.po.merchant.UserOrderAllPO;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import com.panda.sport.merchant.common.vo.user.UserVipVO;
import com.panda.sport.order.feign.MerchantApiClient;
import com.panda.sport.order.service.AbstractOrderService;
import com.panda.sport.order.service.MerchantFileService;
import com.panda.sport.order.service.UserAllowListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserAllowListServiceImpl implements UserAllowListService {

    private final MerchantFileService merchantFileService;
    private final UserOrderUpdateMapper tUserMapper;
    private final AbstractOrderService abstractOrderService;
    private final BackupTUserMapper backupUserMapper;
    private final MerchantApiClient merchantApiClient;

    @Override
    public PageInfo<?> listAll(String merchantName, UserAllowListReq req, String language) {

        PageInfo<UserOrderAllPO> pageInfo = new PageInfo();
        int total = backupUserMapper.countUserAllowList(req);
        if(total == 0){
            return pageInfo;
        }

        int pageNum = req.getPageNum() == null ? 1 : req.getPageNum();
        int pageSize = req.getPageSize() == null || req.getPageSize() > 100 ? 20 : req.getPageSize();
        req.setPageNum(pageNum);
        req.setPageSize(pageSize);

        PageHelper.startPage(pageNum, pageSize);
        List<UserOrderAllPO> resultList = backupUserMapper.queryUserAllowList(req);
        resultList= abstractOrderService.execUserInfo(resultList,new UserOrderVO());
        return new PageInfo<>(resultList);
    }

    @Override
    public Integer del(UserAllowListReq req) {
        try {
            UserVipVO userVipVO = backupUserMapper.queryUserInfoById(Long.valueOf(req.getUserId()));
            List<UserVipVO> vipls = Lists.newArrayList();
            if(userVipVO.getDisabled() == 0 || userVipVO.getDisabled() == 1){
                return 0;
            }
            vipls.add(userVipVO);
            return  merchantApiClient.upsertUserDisabled2Allow(userVipVO.getMerchantCode(),UserAllowListSourceEnum.SOURCE_MERCHANT_ENABLE.getCode(),vipls);
        }catch (Exception e){
            log.error("rpc to api update disabled to 1 error");
            return 0;
        }
    }

    @Override
    public Integer delAll() {
        try {

            //return merchantApiClient.upsertUserDisabled2Allow("ALLIN",UserAllowListSourceEnum.SOURCE_MERCHANT_ENABLE.getCode(),Lists.newArrayList());
            int targetDisabled = UserAllowListSourceEnum.SOURCE_MERCHANT_ENABLE.getCode();
            int total = 0;
            // 这里全表扫描t_user 观察是否频繁可优化
            List<UserVipVO> vipls = backupUserMapper.queryUserInfoAllAllow();
            Map<String, List<UserVipVO>> resultMap = vipls.stream().collect(Collectors.groupingBy(UserVipVO::getMerchantCode));

            //按商户code分组调用feign
            for (String key : resultMap.keySet()) {
                int result = merchantApiClient.upsertUserDisabled2Allow(key,targetDisabled,resultMap.get(key));
                total += result;
                log.info("importUser导入白名单用户,feign merchant:{},list:{},result:{}", key, resultMap.get(key), result);
                total = total + result;
            }
            return total;

        }catch (Exception e){
            log.error("rpc to api update disabled to 1 error");
            return 0;
        }
    }

    @Override
    public Integer importUser(UserAllowListReq req) {
        try {
            int targetDisabled = req.getDisabled();
            int total = 0;
            List<UserVipVO> vipls = backupUserMapper.queryUserInfoByIdList(req.getUserIdList().stream().map(Long::valueOf).collect(Collectors.toList()));
            Map<String, List<UserVipVO>> resultMap = vipls.stream().collect(Collectors.groupingBy(UserVipVO::getMerchantCode));

            //按商户code分组调用feign
            for (String key : resultMap.keySet()) {
                int result = merchantApiClient.upsertUserDisabled2Allow(key,targetDisabled,resultMap.get(key));
                total += result;
                log.info("importUser导入白名单用户,feign merchant:{},list:{},result:{}", key, resultMap.get(key), result);
                total = total + result;
            }
            return total;
        }catch (Exception e){
            log.error("rpc to api update disabled to 1 error");
            return 0;
        }
    }

    @Override
    public void exportAllowListUserList(String merchantName, HttpServletRequest request, UserAllowListReq req) {
        String language = req.getLanguage();
        req.setPageNum(1);
        req.setPageSize(1_000_000);
        int total = backupUserMapper.countUserAllowList(req);
        if(total==0){
            throw new RuntimeException("根据当前参数查询无数据");
        }
        merchantFileService.saveFileTask(
                (language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "投注用户白名单导出_" : "Bet User AllowList Report Exporting_"),
                null, merchantName, JSON.toJSONString(req),
                (language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "数据中心-投注用户白名单导出" : "Report Center-Bet-User-Allow-List Report"),
                "betUserAllowListReportExportServiceImpl");

    }
}
