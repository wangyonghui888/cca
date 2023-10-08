package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.po.merchant.MerchantLogPO;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.vo.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.merchant.manage.service
 * @Description :  商户日志接口服务类
 * @Date: 2020-09-01 15:30
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public interface MerchantLogService {

    /**
     * 保存日志
     *
     * @param pageCode
     * @param operatType
     * @param merchantName
     * @param dataId
     * @return
     */
    void saveLog(Integer pageCode, Integer operatType, String merchantName, String dataId);

    /**
     * 保存完整日志
     *
     * @param pageEnum  页面名称
     * @param typeEnum  操作类型
     * @param filedVO    操作数据 操作前数据 和操作后数据
     * @param tag  操作端 LogTagEnum枚举
     * @param userId  用户ID
     * @param username  用户名
     * @param merchantCode 商户编码
     * @param merchantName  商户名称
     * @param dataId  主数据ID
     * @param language   语言
     * @return
     */

    void saveLog(MerchantLogPageEnum pageEnum, MerchantLogTypeEnum typeEnum, MerchantLogFiledVO filedVO,
                 Integer tag, String userId, String username, String merchantCode, String merchantName,
                 String dataId, String language,String ip);


    void saveLog(MerchantLogPageEnum pageEnum, MerchantLogTypeEnum typeEnum, MerchantLogFiledVO filedVO,
                 Integer tag, String userId, String username, String merchantCode, String merchantName,
                 String dataId, String language,String ip,
                 String domainSelfResult,String domainThirdResult,Integer domainType);

    void saveLogNew(String head, MerchantLogTypeEnum typeEnum, MerchantLogFiledVO filedVO,
                 Integer tag, String userId, String username, String merchantCode, String merchantName,
                 String dataId, String language,String ip);
    /**
     * 保存日志
     *
     * @param merchantLog
     * @return
     */
    void saveLog(MerchantLogPO merchantLog);


    /**
     * 查询日志
     */
    PageVO<MerchantLogPO> queryLog(MerchantLogFindVO findVO);

    /**
     * 加载商户端日志页面
     *
     * @return
     */
    List<MerchantPagesTree> loadLogPages(Integer tag, String language);

    /**
     * 加载商户端日志页面
     *
     * @return
     */
    List<MerchantPagesTree> loadLogOutPages(Integer tag, Integer agentLevel, String language);

    /**
     * 加载操作类型
     */
    Map<String, List<MerchantLogTypeVo>> loadLogType();

    void saveOperationLog(Integer operationType, String typeName, String pageName, String pageCode,
                          String merchantCode, String merchantName, String dataId, String operationField,
                          List<String> beforeValue, List<String> afterValue, HttpServletRequest request);

    void saveOperationRoomLog(Integer operationType, String typeName, String pageName, String pageCode,
                          String merchantCode, String merchantName, String dataId, List<String> operationField,
                          List<String> beforeValue, List<String> afterValue, String userName,String userId,String ip);
    /**
     *手动加扣款操作日志
     */
    void savePlusDeductionLog(MerchantLogPageEnum pageEnum, AccountChangeHistoryFindVO vo, Integer logTag, String language,String ip,String userId,String userName);


}
