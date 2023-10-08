package com.panda.sport.merchant.manage.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.bss.mapper.TUserMapper;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.po.merchant.MerchantNews;
import com.panda.sport.merchant.common.utils.DateUtils;
import com.panda.sport.merchant.common.vo.MerchantAmountAlertVO;
import com.panda.sport.merchant.common.vo.UserLimitNewsVO;
import com.panda.sport.merchant.common.vo.merchant.AgentVO;
import com.panda.sport.merchant.manage.service.MerchantNewsService;
import com.panda.sport.merchant.mapper.MerchantNewsMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;

/**
 * @author YK
 * @Description: 我的消息
 * @date 2020/3/12 17:03
 */
@Service
public class MerchantNewsServiceImpl extends ServiceImpl<MerchantNewsMapper, MerchantNews> implements MerchantNewsService {
    @Resource
    private TUserMapper userMapper;
    @Resource
    private MerchantNewsMapper merchantNewsMapper;
    @Resource
    private MerchantMapper merchantMapper;

    @Override
    public void addUserLimitNews(UserLimitNewsVO userLimitNewsVO) {
        UserPO userInfo = userMapper.getUserInfo(Long.parseLong(userLimitNewsVO.getUserId()));
        Assert.notNull(userInfo, "用户信息未查找到{}", userLimitNewsVO.getUserId());
        MerchantPO merchant = merchantMapper.getMerchantByMerchantCode(userInfo.getMerchantCode());
        Assert.notNull(merchant, "未查找到商户信息{}", userInfo.getMerchantCode());

        //封装数据保存
        String title = MessageFormat.format("平台对用户{0}设置了特殊限额", userInfo.getUsername());
        String content = MessageFormat.format("平台于{0}对用户{1}设置了特殊限额，点击<a href='update'>此处</a>查看详情。",
                userLimitNewsVO.getLastUpdateTime(), userInfo.getUsername());
        Date updateDate = DateUtils.dateStrToDate(userLimitNewsVO.getLastUpdateTime());

        merchantNewsMapper.insert(new MerchantNews()
                .setTitle(title)
                .setContext(content)
                .setIsMerchant(0)
                .setMerchantCode(userInfo.getMerchantCode())
                .setMerchantName(merchant.getMerchantName())
                .setIsRead("0")
                .setSelfIsRead("0")
                .setCreateTime(System.currentTimeMillis())
                .setSendTime(updateDate.getTime())
                .setVisitNumber(0L)
                .setUid(0L)
                .setType(2));
    }

    @Override
    public void addMerchantAlertFromUsedAmount(MerchantAmountAlertVO merchantAmountAlert) {
        MerchantPO merchant = merchantMapper.selectById(merchantAmountAlert.getMerchantId());
        Assert.notNull(merchant, "未查找到商户信息{}", merchantAmountAlert.getMerchantId());

        String msg = "";
        if (StringUtils.isNotBlank(merchantAmountAlert.getCreditId())) {
            AgentVO agentVO = merchantMapper.getAgent(merchant.getMerchantCode(), merchantAmountAlert.getCreditId());
            Assert.notNull(agentVO, "未查找到商户代理信息{0},{1}", merchantAmountAlert.getMerchantId(), merchantAmountAlert.getCreditId());
            msg = "下级代理" + agentVO.getAgentName();
        }
        //封装数据保存
        String title = "平台单日赔付预警提醒";
        String content = MessageFormat.format("【重要】贵平台{2}当前账务日已赔付给用户{0}元，达到了预设金额的{1}%！",
                merchantAmountAlert.getUsedAmount(), merchantAmountAlert.getUsedAmountPercent().multiply(new BigDecimal(100)), msg);

        //判断是否存在
        String uploadUnique = merchant.getMerchantCode() + merchantAmountAlert.getDateExpect() + merchantAmountAlert.getUsedAmountPercent();
        MerchantNews isExist = merchantNewsMapper.selectByUpload(uploadUnique);
        if (null == isExist) {
            merchantNewsMapper.insert(new MerchantNews()
                    .setTitle(title)
                    .setContext(content)
                    .setIsMerchant(0)
                    .setMerchantCode(merchant.getMerchantCode())
                    .setMerchantName(merchant.getMerchantName())
                    .setIsRead("0")
                    .setSelfIsRead("0")
                    .setUpload(uploadUnique)
                    .setCreateTime(System.currentTimeMillis())
                    .setSendTime(merchantAmountAlert.getTimestamp())
                    .setVisitNumber(0L)
                    .setUid(0L)
                    .setType(3));
        }
    }
}
