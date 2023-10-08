package com.panda.multiterminalinteractivecenter.service;

import com.panda.multiterminalinteractivecenter.enums.MerchantLogPageEnum;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogTypeEnum;
import com.panda.multiterminalinteractivecenter.po.MerchantLogPO;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogQueryVO;
import com.panda.sport.merchant.common.vo.MerchantLogTypeVo;
import com.panda.sport.merchant.common.vo.MerchantPagesTree;
import com.panda.sport.merchant.common.vo.Response;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author : ifan
 * @Description :  商户日志接口服务类
 * @Date: 2022-07-11
 */
public interface MerchantLogService {

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
     * @param domainSelfResult   自检结果
     * @param domainThirdResult   三方检测结果
     * @param domainType   域名类型
     * @return
     */

    void saveLog(MerchantLogPageEnum pageEnum, MerchantLogTypeEnum typeEnum, MerchantLogFiledVO filedVO, Integer tag,
                 String userId, String username, String merchantCode, String merchantName, String dataId,
                 String language, String ip,String domainSelfResult,String domainThirdResult,Integer domainType);


    /**
     * 保存操作日志
     * @param pageEnum 页面名称
     * @param typeEnum 操作类型
     * @param filedVO 操作数据 操作前数据 和操作后数据
     * @param userId 用户id
     * @param dataId 数据id
     * @param request request
     */
    void saveLog(MerchantLogPageEnum pageEnum, MerchantLogTypeEnum typeEnum, MerchantLogFiledVO filedVO,
                 String userId, String dataId, HttpServletRequest request);

    /**
     * create MerchantLogFiledVO
     * @param fieldName 操作的字段名
     * @param beforeValues 操作之前的值
     * @param afterValues 操作之后的值
     * @return MerchantLogFiledVO
     */
     MerchantLogFiledVO createFiledVO(String fieldName, String beforeValues, String afterValues);


    /**
     * 查询日志
     */
    Response queryLog(MerchantLogQueryVO queryVO);

    /**
     * 加载操作类型
     */
    Map<String, List<MerchantLogTypeVo>> loadLogType();

    List<MerchantPagesTree> loadLogPages();
}
