
package com.oubao.mq;

import com.alibaba.fastjson.JSON;
import com.oubao.mapper.TAccountMapper;
import com.oubao.mapper.TOrderMapper;
import com.oubao.mapper.TUserMapper;
import com.oubao.po.AccountPO;
import com.oubao.po.OrderPO;
import com.oubao.po.TAccountChangeHistoryPO;
import com.oubao.service.UserService;
import com.oubao.vo.MerchantBetOrderVO;
import com.panda.sport.mqsdk.annotation.RocketMQMessageListener;
import com.panda.sport.mqsdk.mq.base.BasePandaRocketMQListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RocketMQMessageListener
public class OrderConsumer extends BasePandaRocketMQListener {
    @Autowired
    private TOrderMapper orderMapper;

    @Autowired
    private TUserMapper userMapper;
    public final static String TAGS_BET = "bet";
    public final static String TAGS_SETTLE = "settle";
    public final static String TAGS_CANCEL_BET = "cancel";
    public final static String CONFIRM_TAG = "confirm";
    public final static String TAGS_REFUSE = "refuse";
    public final static String TAGS_SETTLE_ROLLBACK = "settleRollBack";
    public final static String TAGS_CANCEL_ROLLBACK = "cancelRollBack";

    @Value("${transfer_mode}")
    private String transferMode;

    @Autowired
    private TAccountMapper tAccountMapper;
    @Autowired
    private UserService userService;

    @Autowired
    private OubaoDistributedLockerUtil oubaoDistributedLockerUtil;

    @Override
    public void consumerExecute(List<MessageExt> msgs) throws Exception {
        log.info("此次开始处理:" + msgs.size());
        List<MerchantBetOrderVO> allMessages = new ArrayList<>();
        for (MessageExt msg : msgs) {
            String tags = msg.getTags();
            String body = new String(msg.getBody(), StandardCharsets.UTF_8);
            log.info(tags + "  consumerExecute body:" + body);
            List<MerchantBetOrderVO> messages;
            try {
                messages = JSON.parseArray(new String(msg.getBody()), MerchantBetOrderVO.class);
            } catch (Exception e) {
                messages = new ArrayList<>();
                MerchantBetOrderVO message = JSON.parseObject(new String(msg.getBody()), MerchantBetOrderVO.class);
                messages.add(message);
            }
            log.info("info:" + messages);
            if (TAGS_BET.equals(tags) || TAGS_CANCEL_ROLLBACK.equals(tags) || TAGS_SETTLE_ROLLBACK.equals(tags)) {
                for (MerchantBetOrderVO orderVO : messages) {
                    orderVO.setTag(tags);
                    // insertOrder(tags, orderVO);
                }
            } else if (TAGS_SETTLE.equals(tags)) {
                for (MerchantBetOrderVO settleVO : messages) {
                    log.info(tags + "结算OrderConsumer, tags:" + tags + ";orderNo  is " + settleVO.getOrderNo());
                    settleVO.setTag(tags);
                    // updateSettleOrder(tags, settleVO);
                }
            } else if (TAGS_CANCEL_BET.equals(tags) || TAGS_REFUSE.equals(tags)) {
                log.info(tags + "此次消费撤单数据共:" + messages.size());
                for (MerchantBetOrderVO orderVO : messages) {
                    log.info("撤单OrderConsumer,tags:" + tags + ";orderNo  is " + orderVO.getOrderNo());
                    orderVO.setTag(tags);
                    // cancelBet(tags, orderVO);
                }
            } else if (CONFIRM_TAG.equals(tags)) {
                log.info(tags + "此次消费注单确认数据共:" + messages.size());
/*                for (MerchantBetOrderVO message : messages) {
                    message.setTag(tags);
                    OrderPO orderPO = new OrderPO();
                    orderPO.setOrderNo(message.getOrderNo());
                    Long uid = userMapper.getUserId(message.getUserName());
                    if (uid == null) {
                        log.error("查询用户异常!user=" + message.getUserName());
                        throw new Exception("查询用户异常!");
                    }
                    orderPO.setUid(uid);
                    orderPO.setTag(tags);
                    orderPO.setOrderStatus(message.getOrderStatus());
                   // int num = orderMapper.updateOrder(orderPO);
                }*/
            }
            allMessages.addAll(messages);
        }
        //saveMessages(allMessages);
    }

    private void saveMessages(List<MerchantBetOrderVO> voList) {
        try {
            List<OrderPO> orderPOList = new ArrayList<>();
            for (MerchantBetOrderVO orderVO : voList) {
                OrderPO orderPO = new OrderPO();
                BeanUtils.copyProperties(orderVO, orderPO);
                orderPOList.add(orderPO);
                orderPO.setAmountTotal(orderVO.getOrderAmount());
                orderPO.setMerchantCode("oubao");
            }
            orderMapper.batchInsert(orderPOList);
        } catch (Exception e) {
            log.error("保存商户消息队列订单数据异常!", e);
        }
    }

    private void insertOrder(String tags, MerchantBetOrderVO message) throws Exception {
        if (message != null) {
            if (tags != null && tags.equals(TAGS_BET)) {
                OrderPO orderPO = new OrderPO();
                orderPO.setOrderNo(message.getOrderNo());
                orderPO.setBetAmount(message.getOrderAmount());
                Long uid = userMapper.getUserId(message.getUserName());
                if (uid == null) {
                    log.error("查询用户异常!user=" + message.getUserName());
                    throw new Exception("查询用户异常!");
                }
                orderPO.setUid(uid);
                orderPO.setOrderStatus(orderPO.getOrderStatus());
                orderPO.setSeriesType(message.getSeriesType());
                orderPO.setSeriesValue(message.getSeriesValue());
                orderPO.setCreateTime(message.getCreateTime());
                orderPO.setTag(tags);
                orderPO.setAmountTotal(message.getOrderAmount());
                Integer num = orderMapper.countOrder(orderPO.getOrderNo());
                if (num < 1) {
                    orderMapper.insertOrder(orderPO);
                }
            } else if (tags != null && (tags.equals(TAGS_CANCEL_ROLLBACK) || tags.equals(TAGS_SETTLE_ROLLBACK))) {
                OrderPO orderPO = new OrderPO();
                orderPO.setOrderNo(message.getOrderNo());
                Long uid = userMapper.getUserId(message.getUserName());
                if (uid == null) {
                    log.error("查询用户异常!user=" + message.getUserName());
                    throw new Exception("查询用户异常!");
                }
                orderPO.setUid(uid);
                orderPO.setOrderStatus(orderPO.getOrderStatus());
                orderPO.setProfitAmount(BigDecimal.ZERO);
                orderPO.setSettleAmount(BigDecimal.ZERO);
                orderPO.setTag(tags);
                int num = orderMapper.updateOrder(orderPO);
                if (!"2".equals(transferMode)) {
                    try {
                        oubaoDistributedLockerUtil.lock("oubao-emulate:" + message.getUserName());
                        if (tags.equals(TAGS_SETTLE_ROLLBACK)) {
                            userService.addAccount(message.getSettleAmount(), message.getUserName(), 4L, "oubao");
                        } else {
                            userService.addAccount(message.getOrderAmount(), message.getUserName(), 4L, "oubao");
                        }
                        AccountPO accountPO = tAccountMapper.checkBalance(message.getUserName(), "oubao");
                        TAccountChangeHistoryPO po = new TAccountChangeHistoryPO();
                        po.setChangeAmount(message.getOrderAmount());
                        po.setAccountId(accountPO.getId());
                        po.setUid(uid);
                        po.setChangeType(1);
                        po.setBizType(4L);
                        po.setCreateUser(message.getUserName());
                        po.setModifyUser(message.getUserName());
                        po.setCurrentBalance(accountPO.getAmount());
                        po.setMerchantCode("oubao");
                        po.setCreateTime(System.currentTimeMillis());
                        po.setModifyTime(System.currentTimeMillis());
                        po.setRemark(tags);
                        tAccountMapper.insertAccountChange(po);
                    } finally {
                        oubaoDistributedLockerUtil.unLock("oubao-emulate:" + message.getUserName());
                    }
                }
            }
        }
    }

    private void updateSettleOrder(String tags, MerchantBetOrderVO message) throws Exception {
        log.info("updateOrder message:" + message.toString());
        OrderPO orderPO = new OrderPO();
        orderPO.setOrderNo(message.getOrderNo());
        Long uid = userMapper.getUserId(message.getUserName());
        if (uid == null) {
            log.error("查询用户异常!user=" + message.getUserName());
            throw new Exception("查询用户异常!");
        }
        orderPO.setUid(uid);
        orderPO.setTag(tags);
        orderPO.setOrderStatus(message.getOrderStatus());
        BigDecimal settleAmount = message.getSettleAmount();
        log.info("updateOrder profit:" + settleAmount);
        orderPO.setProfitAmount(message.getProfitAmount());
        int num = orderMapper.updateOrder(orderPO);
        if (!"2".equals(transferMode)) {
            try {
                oubaoDistributedLockerUtil.lock("oubao-emulate:" + message.getUserName());
                boolean result = userService.addAccount(settleAmount, message.getUserName(), 2L, "oubao");

                AccountPO accountPO = tAccountMapper.checkBalance(message.getUserName(), "oubao");

                TAccountChangeHistoryPO po = new TAccountChangeHistoryPO();
                po.setChangeAmount(message.getSettleAmount());
                po.setAccountId(accountPO.getId());
                po.setUid(uid);
                po.setChangeType(1);
                po.setBizType(2L);
                po.setCreateUser(message.getUserName());
                po.setModifyUser(message.getUserName());
                po.setCurrentBalance(accountPO.getAmount());
                po.setMerchantCode("oubao");
                po.setCreateTime(System.currentTimeMillis());
                po.setModifyTime(System.currentTimeMillis());
                po.setRemark(tags);
                tAccountMapper.insertAccountChange(po);
            } finally {
                oubaoDistributedLockerUtil.unLock("oubao-emulate:" + message.getUserName());
            }
        }

    }

    private void cancelBet(String tags, MerchantBetOrderVO message) throws Exception {
        log.info("cancelBet message:" + message.toString());
        OrderPO orderPO = new OrderPO();
        orderPO.setOrderNo(message.getOrderNo());
        Long uid = userMapper.getUserId(message.getUserName());
        if (uid == null) {
            log.error("查询用户异常!user=" + message.getUserName());
            throw new Exception("查询用户异常!");
        }
        orderPO.setUid(uid);
        orderPO.setTag(tags);

        orderPO.setOrderStatus(message.getOrderStatus());//撤单
        int num = orderMapper.updateOrder(orderPO);

        log.info("updateOrder num:" + num);
        if (!"2".equals(transferMode)) {
            boolean result = userService.addAccount(message.getOrderAmount(), message.getUserName(), 3L, "oubao");
            AccountPO accountPO = tAccountMapper.checkBalance(message.getUserName(), "oubao");
            TAccountChangeHistoryPO po = new TAccountChangeHistoryPO();
            po.setChangeAmount(message.getOrderAmount());
            po.setAccountId(accountPO.getId());
            po.setUid(uid);
            po.setChangeType(1);
            po.setBizType(3L);
            po.setCreateUser(message.getUserName());
            po.setModifyUser(message.getUserName());
            po.setCurrentBalance(accountPO.getAmount());
            po.setMerchantCode("oubao");
            po.setCreateTime(System.currentTimeMillis());
            po.setModifyTime(System.currentTimeMillis());
            po.setRemark(tags);
            tAccountMapper.insertAccountChange(po);
        }

    }


}
