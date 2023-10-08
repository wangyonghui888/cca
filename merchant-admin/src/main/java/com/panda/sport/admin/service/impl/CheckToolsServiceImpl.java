package com.panda.sport.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.panda.sport.admin.feign.MerchantReportClient;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.CheckToolsService;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.backup.mapper.BackupTUserMapper;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.common.constant.CommonDefaultValue;
import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author :  ives
 * @Description :  财务中心-对账工具 服务实现类
 * @Date: 2022-02-08 20:15
 */
@Service
public class CheckToolsServiceImpl implements CheckToolsService {

    @Resource
    private MerchantReportClient reportClient;

    @Resource
    private MerchantMapper merchantMapper;

    @Resource
    private LocalCacheService  localCacheService ;

    @Resource
    private BackupTUserMapper tUserMapper;

    @Override
    public CheckToolsQueryRespVO checkFinance(CheckToolsQueryReqVO queryReqVO) {
        // 对外商户只查询 当前登录商户(直营，二级) 或者渠道其下级商户 数据
        JwtUser user = SecurityUtils.getUser();
        List<String> codeList = new ArrayList<>(10);

        if (ObjectUtil.isNotNull(user.getAgentLevel())){
            List<Integer> agentLevelList = new ArrayList<>(4);
            switch (user.getAgentLevel()){
                case CommonDefaultValue.AgentLevel.DIRECT_SALES :
                    agentLevelList.add(CommonDefaultValue.AgentLevel.DIRECT_SALES);
                    codeList.add(user.getMerchantCode());
                    break;
                case CommonDefaultValue.AgentLevel.CHANNEL :
                    agentLevelList.add(CommonDefaultValue.AgentLevel.CHANNEL);
                    agentLevelList.add(CommonDefaultValue.AgentLevel.SECONDARY_AGENT);

                    List<String> merchantCodeList = merchantMapper.queryChildList(user.getMerchantId());
                    codeList.addAll(merchantCodeList);
                    break;
                case CommonDefaultValue.AgentLevel.SECONDARY_AGENT :
                    agentLevelList.add(CommonDefaultValue.AgentLevel.SECONDARY_AGENT);
                    codeList.add(user.getMerchantCode());
                    break;
                // 代理商不展示
                case CommonDefaultValue.AgentLevel.AGENCY :
                default:
                    CheckToolsQueryRespVO checkToolsQueryRespVO = new CheckToolsQueryRespVO();
                    checkToolsQueryRespVO.setCheckResult(CommonDefaultValue.ResultStatus.CORRECT);
                    checkToolsQueryRespVO.setCheckList(new ArrayList<>());
                    checkToolsQueryRespVO.setDateType(queryReqVO.getDateType());
                    return checkToolsQueryRespVO;

            }

            queryReqVO.setMerchantCodeList(codeList);
            queryReqVO.setAgentLevelList(agentLevelList);
        } else {
            CheckToolsQueryRespVO checkToolsQueryRespVO = new CheckToolsQueryRespVO();
            checkToolsQueryRespVO.setCheckResult(CommonDefaultValue.ResultStatus.ERROR);
            checkToolsQueryRespVO.setCheckList(new ArrayList<>());
            checkToolsQueryRespVO.setDateType(queryReqVO.getDateType());
            checkToolsQueryRespVO.setErrMessage("无法获取该用户代理等级");
            return checkToolsQueryRespVO;
        }

        return this.reportClient.checkFinance(queryReqVO);
    }


    @Override
    public CheckToolsEditRespVO editFinance(CheckToolsEditReqVO editReqVO) {
        return this.reportClient.editFinance(editReqVO);
    }

    /**
     * 商户对账对外
     * @param queryReqVO
     * @return
     */
    @Override
    public CheckToolsQueryRespVO checkUserFinance(CheckToolsQueryReqVO queryReqVO) throws Exception {

        JwtUser user = SecurityUtils.getUser();
        String merchantCode = user.getMerchantCode();
        Integer agentLevel = user.getAgentLevel();
        String merchantId = user.getMerchantId();

        List<String> merchantCodeList = null;
        List<UserPO> userPOList;
        if (agentLevel == 1 || agentLevel == 10) {
                merchantCodeList = localCacheService.getMerchantCodeList(merchantId, agentLevel);
                merchantCodeList.add(merchantCode);
                userPOList  =  tUserMapper.getUserPO(null,merchantCodeList,queryReqVO.getUserId(),queryReqVO.getUserName());
        }else {
                userPOList  =  tUserMapper.getUserPO(merchantCode,null,queryReqVO.getUserId(),queryReqVO.getUserName());
        }

        if (CollectionUtils.isEmpty(userPOList)){
             throw new Exception("用户不在此商户下");
        }

        List<Long> userIdList = userPOList.stream().map(UserPO::getUserId).collect(Collectors.toList());
        queryReqVO.setUserIdList(userIdList);

        return this.reportClient.checkUserFinance(queryReqVO);
    }


    @Override
    public CheckToolsEditRespVO editUserFinance(CheckToolsEditReqVO editReqVO) {
        return this.reportClient.editUserFinance(editReqVO);
    }

}
