package com.panda.sport.merchant.api.controller;


import com.alibaba.fastjson.JSON;
import com.panda.sport.merchant.api.service.UserOrderUpdateService;
import com.panda.sport.merchant.common.dto.UserOrderTimePO;
import com.panda.sport.merchant.common.po.merchant.UserOrderDayPO;
import com.panda.sport.merchant.common.vo.user.UserVipVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/order")
@Slf4j
public class UserOrderController {


    @Autowired
    private UserOrderUpdateService userOrderUpdateService;

    @PostMapping(value = "/updateUserOrderTime")
    public int updateUserOrderTime(@RequestBody List<UserOrderTimePO> list, @RequestParam("merchantCode") String merchantCode) {
        return userOrderUpdateService.updateUserOrderTime(list);
    }

    @PostMapping(value = "/updateUserSevenOrder")
    public int updateUserSevenOrder(@RequestBody List<UserOrderTimePO> list, @RequestParam("merchantCode") String merchantCode) {
        return userOrderUpdateService.updateUserSevenOrder(list);
    }

    @PostMapping(value = "/updateUserAllStatistic")
    public int updateUserAllStatistic(@RequestBody List<UserOrderDayPO> list, @RequestParam("merchantCode")String merchantCode){
        return userOrderUpdateService.updateUserAllStatistic(list);
    }


    @PostMapping(value = "/updateUserAllLifeData")
    public int updateUserAllLifeData(@RequestBody List<UserOrderTimePO> list, @RequestParam("merchantCode") String merchantCode) {
        return userOrderUpdateService.updateUserAllLifeData(list);
    }

    @PostMapping(value = "/upsertUserVip")
    public int upsertUserVip(@RequestParam(value = "merchantCode") String merchantCode, @RequestBody List<UserVipVO> vipList) {
        log.info(merchantCode + "/user/order/upsertUserVip:" + merchantCode + ":" + vipList.size());
        return userOrderUpdateService.upsertUserVip(merchantCode, vipList);
    }

    @PostMapping(value = "/upsertUserDisabled")
    public int upsertUserDisabled(@RequestParam(value = "merchantCode") String merchantCode,@RequestBody List<UserVipVO> vipList) {
        log.info(merchantCode + "/user/order/upsertUserVip:" + merchantCode + ":" + vipList.size());
        return userOrderUpdateService.upsertUserDisabled(merchantCode, vipList);
    }

    @PostMapping(value = "/upsertUserDisabled2Allow")
    public int upsertUserDisabled2Allow(@RequestParam(value = "merchantCode") String merchantCode,@RequestParam(value = "disabled") Integer disabled,   @RequestBody List<UserVipVO> vipList) {
        log.info(merchantCode + "/user/order/upsertUserDisabled2Allow:" + merchantCode + ":" + vipList.size());
        return userOrderUpdateService.upsertUserDisabled2Allow(merchantCode, disabled, vipList);
    }

    @PostMapping(value = "/updateDisabled")
    public int updateDisabled(@RequestParam(value = "merchantCode") String merchantCode, @RequestBody UserVipVO userVipVO) {
        log.info(merchantCode + "/user/order/updateDisabled:" + merchantCode + ":" + JSON.toJSONString(userVipVO));
        return userOrderUpdateService.updateDisabled(merchantCode, userVipVO);
    }

    @GetMapping(value = "/updateIsVipDomain")
    public void updateIsVipDomain(@RequestParam(value = "merchantCode") String merchantCode, @RequestParam(value = "userId") String userId,
                                  @RequestParam(value = "isVipDomain") Integer isVipDomain) {
        log.info(merchantCode + "/user/order/updateIsVipDomain:" + merchantCode + ",user=" + userId + ",isVipDomain=" + isVipDomain);
        userOrderUpdateService.updateIsVipDomain(merchantCode, userId, isVipDomain);
    }

    @GetMapping(value = "/upsertUserToDisabled")
    public void upsertUserToDisabled(@RequestParam(value = "merchantCode") String merchantCode, @RequestParam(value = "uidList") List<Long> uidList,
                                     @RequestParam(value = "disabled") Integer disabled) {
        log.info(merchantCode + "/user/order/upsertUserToDisabled:" + merchantCode + ",uidList=" + uidList + ",disabled=" + disabled);
        userOrderUpdateService.upsertUserToDisabled(merchantCode, uidList, disabled);
    }
}
