package com.panda.sport.order.feign;


import com.panda.sport.merchant.common.po.bss.AcBonusPO;
import com.panda.sport.merchant.common.vo.AccountChangeHistoryFindVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.user.UserRetryTransferVO;
import com.panda.sport.merchant.common.vo.user.UserVipVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
@FeignClient(value = "merchant-api", fallback = RemoteHystrix.class)
public interface MerchantApiClient {
    @GetMapping(value = "/api/user/preLogin")
    Object preLogin(@RequestParam(value = "userName") String userName,
                    @RequestParam(value = "terminal") String terminal,
                    @RequestParam(value = "merchantCode") String merchantCode,
                    @RequestParam(value = "currency", required = false) String currency,
                    @RequestParam(value = "timestamp") Long timestamp);

    @PostMapping(value = "/api/transfer/retryTransfer")
    Object retryTransfer(@RequestBody UserRetryTransferVO vo, @RequestParam(value = "merchantCode") String merchantCode);

    @PostMapping(value = "/api/avtivity/upsertUserBonus")
    void upsertUserBonus(@RequestParam(value = "merchantCode") String merchantCode, @RequestBody List<AcBonusPO> bonusList);

    @GetMapping(value = "/api/avtivity/clearTicketsOfTask")
    void clearTicketsOfTask(@RequestParam(value = "merchantCode") String merchantCode);

    @GetMapping(value = "/api/avtivity/clearTicketsOfMardigraTask")
    void clearTicketsOfMardigraTask(@RequestParam(value = "merchantCode") String merchantCode, @RequestParam(value = "conditionId") Integer conditionId);


    @GetMapping(value = "/api/merchant/kickoutMerchant")
    Object kickoutMerchant(@RequestParam(value = "merchantCode") String merchantCode);
    /**
     * 增加扣款，加款操作
     */
    @PostMapping(value = "/api/transfer/addChangeRecordHistory")
    void addChangeRecordHistory(@RequestBody AccountChangeHistoryFindVO accountChangeHistoryFindVO, @RequestParam(value = "merchantCode") String merchantCode);

    /**
     * 导入Vip用户
     */
    @PostMapping(value = "/user/order/upsertUserVip")
    int upsertUserVip(@RequestParam(value = "merchantCode") String merchantCode, @RequestBody List<UserVipVO> vipList);

    /**
     * 导入问题用户
     */
    @PostMapping(value = "/user/order/upsertUserDisabled")
    int upsertUserDisabled(@RequestParam(value = "merchantCode") String merchantCode, @RequestBody List<UserVipVO> vipList);
     /**
     * 导入白名单用户
     */
    @PostMapping(value = "/user/order/upsertUserDisabled2Allow")
    int upsertUserDisabled2Allow(@RequestParam(value = "merchantCode") String merchantCode,@RequestParam(value = "disabled") Integer disabled, @RequestBody List<UserVipVO> vipList);


    @PostMapping(value = "/user/order/updateDisabled")
    int updateDisabled(@RequestParam(value = "merchantCode") String merchantCode, @RequestBody UserVipVO userVipVO);

    @GetMapping(value = "/user/order/updateIsVipDomain")
    void updateIsVipDomain(@RequestParam(value = "merchantCode") String merchantCode, @RequestParam(value = "userId") String userId,
                           @RequestParam(value = "isVipDomain") Integer isVipDomain);


    @GetMapping(value = "/api/user/userAccountClearDataTask")
    void userAccountClearDataTask(@RequestParam(value = "merchantsCode") String merchantCode, @RequestParam(value = "num") Integer num);

    @GetMapping(value = "/api/user/transferRecordClearDataTask")
    void transferRecordClearDataTask(@RequestParam(value = "merchantsCode") String merchantCode, @RequestParam(value = "num") Integer num);
/*

    @GetMapping("/api/merchant/clearStressTestData")
    void clearStressTestData(@RequestParam(value = "merchantsCode") String merchantCode, @RequestParam(value = "num") Integer num);
*/

    @GetMapping("/api/avtivity/executeSumTask")
    void executeSumTask(@RequestParam(value = "merchantCode") String merchantCode);


    @GetMapping("/api/avtivity/executeDailyTask")
    void executeDailyTask(@RequestParam(value = "merchantCode") String merchantCode, @RequestParam(value = "dateStr") String dateStr);

    @GetMapping(value = "/user/order/upsertUserToDisabled")
    void upsertUserToDisabled(@RequestParam(value = "merchantCode") String merchantCode, @RequestParam(value = "uidList") List<Long> uidList,
                              @RequestParam(value = "disabled") Integer disabled);
}
