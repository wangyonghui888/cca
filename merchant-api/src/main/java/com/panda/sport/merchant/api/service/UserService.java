package com.panda.sport.merchant.api.service;


import com.panda.sport.merchant.common.po.bss.UserLevelRelationVO;
import com.panda.sport.merchant.common.po.bss.UserPO;
import com.panda.sport.merchant.common.po.bss.WindUserPO;
import com.panda.sport.merchant.common.vo.RcsUserSpecialBetLimitConfigVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import com.panda.sport.merchant.common.vo.user.UserVipVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {

    APIResponse create(HttpServletRequest request, String username, String nickname, String merchantCode, Long timestamp, String currency, String agentId, String signature) throws Exception;

    APIResponse<Object> login(HttpServletRequest request, String username, String terminal, String merchantCode, String currency, String callbackUrl,
                              Long timestamp, String signature, String jumpsupport, String jumpfrom, String ip, String stoken, String agentId, Boolean imitate, String language) throws Exception;


    APIResponse<Object> checkUserOnline(HttpServletRequest request, String username, String merchantCode, Long timestamp, String signature);


    APIResponse<Object> kickOutUser(HttpServletRequest request, String userName, String merchantCode, Long timestamp, String signature);

    APIResponse kickoutUserInternal(Long uid);

    void updateUserCache(Long uid, String merchantCode);

    APIResponse<Object> modifyUserMarketLevel(HttpServletRequest request, String marketLevel, String userName, String uid, String merchantCode, Long timestamp, String signature);

    APIResponse<Object> getUserMarketLevel(HttpServletRequest request, String userName, String uid, String merchantCode, Long timestamp, String signature);

    APIResponse kickoutMerchantUser(String merchantCode);

    APIResponse kickoutDisableUser(String merchantCode, List<UserVipVO> list);

    APIResponse updateMerchantUserCache(String merchantCode);

    APIResponse<Object> preLogin(String userName, String terminal, String merchantCode, String currency, Long timestamp) throws Exception;

    APIResponse updateMaintainCache(Long maintainTime);

    APIResponse getMaintainCache();

    APIResponse updateUserSpecialBettingLimit(UserPO userInfo);


    APIResponse updateUserSpecialBettingLimitLog(List<RcsUserSpecialBetLimitConfigVO> rcsUserSpecialBetLimitConfigDataVoList, String merchantCode, Long userId, Integer specialBettingLimit);

    Response<?> queryRedisCache(Long uid, String merchantCode);

    boolean batchUpdateUserCache(List<Long> uidList, String merchantCode);

    boolean batchUpdateUserAndDomainCache(List<Long> uidList, String merchantCode, Integer isVipDomain);


    void updateUserAndDomainCache(Long valueOf, String merchantCode, Integer isVipDomain);

    APIResponse<Object> refreshBalance(HttpServletRequest request, String userName, String merchantCode, Long timestamp, String signature);

    Long queryUidByUserName(String userName);

    void windControlUpdateUserCache(Long uid,String merchantCod, WindUserPO userPO);
}
