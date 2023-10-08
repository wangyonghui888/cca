package com.oubao.service;

import com.oubao.po.*;
import com.oubao.vo.APIResponse;
import com.oubao.vo.PageVO;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

public interface UserService {

    /**
     * @param userId；
     * @return UserPO
     * @description 根据uId或者id查询账户
     */
    UserPO findByUserId(Long userId);

    UserApiVo login(String var1, String var2, String var3, String merchantCode) throws Exception;


    /**
     * @param userPO
     * @return accountPo
     * @description 注册用户，返回UserPO
     */
    UserPO register(UserPO userPO) throws Exception;

    /**
     * @param userPo
     * @return accountPo
     * @description 根据是否有userId做更新或新增操作，返回UserPO
     */
    UserPO update(UserPO userPo) throws Exception;

    /**
     * @param username
     * @param phone
     * @param email
     * @return UserPO
     * @description 根据用户号，用户名称，手机号或emial查询对应账户信息是否已经存在
     */
    UserPO getUser(Long userId, String username, String phone, String email, String merchantCode) throws Exception;

    boolean changeCredit(String userName, Integer type, Double credit, String merchantCode);

    PageVO<UserPO> queryUserList(String userPO, String startTime, String endTime, String merchantCode, Integer page, Integer size);

    PageVO<TAccountChangeHistoryPO> queryAccountChangeList(String userName, String merchantCode, String startTime, String endTime, Integer page, Integer size);

    PageVO<OrderPO> queryOrderList(String userName, String startTime, String endTime, Integer page, Integer size, String merchantCode);

    boolean changeOubaoCredit(TransferPO orderPO);

    boolean notifyMerchant(NotifyPO orderPO);
    boolean notifySafetyMerchant(String merchantCode,String transferId, String userName,String amount, String transferType,Long timestamp,String signature);

    boolean addAccount(BigDecimal amount, String userName, Long bizType, String merchantCode);

    BigDecimal checkBalance(String userName, String merchantCode);

/*   APIResponse transfer(String username,Integer transferType,Double amount);

    BigDecimal checkMerchantBalance(String userName);*/

    Object loginPanda(String userName, String terminal, String merchantCode);

    Map<String,Object> tryPlayer(UserPO user, String terminal) throws Exception;

    APIResponse checkOubaoCredit(String merchantCode, String username, String timestamp, String signature);

    APIResponse transferPandaCredit(String username, Integer transferType, BigDecimal amount, String merchantCode) throws Exception;

    Object tryPlay(String merchantCode,String userName,String password, String terminal,String languageName) throws Exception;
}
