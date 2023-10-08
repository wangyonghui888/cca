package com.panda.sport.merchant.manage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.panda.sport.backup.mapper.BackupTUserMapper;
import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.vo.CheckToolsEditReqVO;
import com.panda.sport.merchant.common.vo.CheckToolsEditRespVO;
import com.panda.sport.merchant.common.vo.CheckToolsQueryReqVO;
import com.panda.sport.merchant.common.vo.CheckToolsQueryRespVO;
import com.panda.sport.merchant.manage.feign.MerchantReportClient;
import com.panda.sport.merchant.manage.service.CheckToolsService;
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
    private BackupTUserMapper tUserMapper;

    @Resource
    private MerchantReportClient reportClient;

    @Override
    public CheckToolsQueryRespVO checkFinance(CheckToolsQueryReqVO queryReqVO) {
        if (ObjectUtil.isNotNull(queryReqVO.getAgentLevel())){
            List<Integer> list = new ArrayList<>(4);
            list.add(queryReqVO.getAgentLevel());
            queryReqVO.setAgentLevelList(list);
        }
        return this.reportClient.checkFinance(queryReqVO);
    }


    @Override
    public CheckToolsEditRespVO editFinance(CheckToolsEditReqVO editReqVO) {
        return this.reportClient.editFinance(editReqVO);
    }

    @Override
    public CheckToolsQueryRespVO checkUserFinance(CheckToolsQueryReqVO queryReqVO) throws Exception {


        List<UserPO> userPOList  =  tUserMapper.getUserPO(null,null, queryReqVO.getUserId(), queryReqVO.getUserName());
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
