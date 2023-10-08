package com.panda.sport.merchant.manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.common.enums.AgentLevelEnum;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.MerchantAgentPageVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantAgentVO;
import com.panda.sport.merchant.manage.service.MerchantAgentService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author javier
 * @date 2020/12/15
 * @description 代理商设置服务实现
 */
@Service
@Slf4j
public class MerchantAgentServiceImpl implements MerchantAgentService {
    @Resource
    protected MerchantMapper merchantMapper;

    @Resource
    private MerchantLogService merchantLogService;

    @Resource
    private LocalCacheService localCacheService;

    @Override
    public Response<PageInfo<MerchantPO>> listMerchant(MerchantAgentPageVO merchantAgentPageVO) {
        String orderStr = "updateTime desc";
        PageHelper.startPage(merchantAgentPageVO.getPageNum() == null ? 1 : merchantAgentPageVO.getPageNum(),
                merchantAgentPageVO.getPageSize() == null ? 20 : merchantAgentPageVO.getPageSize(), orderStr);
        return Response.returnSuccess(new PageInfo<>(merchantMapper.selectListByAgent(getMerchantPo(merchantAgentPageVO))));
    }

    /**
     * 封装查询渠道商户参数信息
     */
    private MerchantPO getMerchantPo(MerchantAgentPageVO merchantAgentPageVO) {
        MerchantPO merchant = new MerchantPO();
        merchant.setMerchantName(merchantAgentPageVO.getMerchantName());
        merchant.setStatus(merchantAgentPageVO.getStatus());
        if(merchantAgentPageVO.getAgentLevel()==null) {
            List list = Lists.newArrayList();
            list.add(1);
            list.add(0);
            merchant.setAgentLevelList(list);
        }else{
            merchant.setAgentLevel(merchantAgentPageVO.getAgentLevel());
        }
        merchant.setParentId(merchantAgentPageVO.getMerchantId());
        return merchant;
    }

    @Override
    public Response<ResponseEnum> bindMerchantByAgent(MerchantAgentVO merchantAgentVO, String language, String ip) {
        //验证参数
        List<MerchantPO> merchantList = validParameter(merchantAgentVO);
        //数据持久化
        merchantMapper.batchUpdateMerchantParentId(merchantAgentVO);
        //刷新缓存
        localCacheService.invalidateAll();
        //新增操作日志
        addLogForBatchAddMerchant(merchantAgentVO, merchantList, language, ip);
        return Response.returnSuccess();
    }

    /**
     * todo 验证业务参数
     *
     * @return 如果验证成功，则返回需要更新的渠道商户集合，否则异常
     */
    private List<MerchantPO> validParameter(MerchantAgentVO merchantAgentVO) {
        MerchantPO merchant = merchantMapper.selectById(merchantAgentVO.getMerchantAgentId());
        Assert.notNull(merchant, "无代理商记录");
        Assert.isTrue(AgentLevelEnum.AGENT_LEVEL_10.getCode().equals(merchant.getAgentLevel()), merchant.getId() + "身份不是代理商");
        List<MerchantPO> merchantList = merchantMapper.getMerchantInMerchantId(merchantAgentVO.getMerchantIds());
        Assert.notEmpty(merchantList, "无下级商户记录");
        merchantList.forEach(temp -> {
           // Assert.isTrue(AgentLevelEnum.AGENT_LEVEL_1.getCode().equals(temp.getAgentLevel()), "检测到id:" + temp.getId() + "不是渠道商户");
            Assert.isNull(temp.getParentId(), "无需重复绑定，下级商户id:" + temp.getId());
        });
        return merchantList;
    }

    /**
     * todo 保存业务日志
     */
    private void addLogForBatchAddMerchant(MerchantAgentVO merchantAgentVO, List<MerchantPO> merchantList, String language,String ip ) {
        MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
        StringBuilder merchantIds = new StringBuilder();
        StringBuilder merchantNames = new StringBuilder();
        StringBuilder merchantCodes = new StringBuilder();
        List <String> before = new ArrayList<>();
        List <String> afters = new ArrayList<>();
        before.add("序号  商户编码  商户名称 商户等级");
        afters.add("序号  商户编码  商户名称 商户等级");
        before.add(" -      -        -      -   ");

        for(int i =0;i< merchantList.size();i++){
            merchantIds.append(merchantList.get(i).getId()).append(":");
            merchantNames.append(merchantList.get(i).getMerchantName()).append(":");
            merchantCodes.append(merchantList.get(i).getMerchantCode()).append(":");
            afters.add((i+1) + "  " + merchantList.get(i).getId() + "  " + merchantList.get(i).getMerchantCode() +
             "  " +merchantList.get(i).getMerchantName() + "  "  + merchantList.get(i).getAgentLevel());
        }
        String merchantIdsStr = merchantIds.toString();
        merchantIdsStr = merchantIdsStr.substring(0, merchantIdsStr.length() - 1);
        String merchantCodesStr = merchantCodes.toString();
        merchantCodesStr = merchantCodesStr.substring(0, merchantCodesStr.length() - 1);
        String merchantNamesStr = merchantNames.toString();
        merchantNamesStr = merchantNamesStr.substring(0, merchantNamesStr.length() - 1);
        filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("parentId"));
        filedVO.setBeforeValues(before);
        filedVO.setAfterValues(afters);
        merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_21, MerchantLogTypeEnum.Add_SUB, filedVO,
                merchantAgentVO.getMerchantOutIn(), merchantAgentVO.getUserId(), merchantAgentVO.getUpdateUser(),
                merchantCodesStr, merchantNamesStr, merchantIdsStr, language,ip);

     }
}
