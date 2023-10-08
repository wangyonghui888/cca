package com.panda.sport.bss.mapper;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.sport.merchant.common.dto.DomainChangeDTO;
import com.panda.sport.merchant.common.po.bss.*;
import com.panda.sport.merchant.common.po.trader.Merchant;
import com.panda.sport.merchant.common.vo.AccountChangeHistoryFindVO;
import com.panda.sport.merchant.common.vo.CheckApiDomainVo;
import com.panda.sport.merchant.common.vo.MerchantInfoVo;
import com.panda.sport.merchant.common.vo.MerchantTree;
import com.panda.sport.merchant.common.vo.merchant.*;
import com.panda.sport.merchant.common.vo.user.MerchantComboVO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface MerchantMapper extends BaseMapper<MerchantPO> {

    MerchantPO getMerchant(@Param(value = "merchantCode") String merchantCode);
    MerchantPO getMerchantAPI(@Param(value = "merchantCode") String merchantCode);

    MerchantPO getMerchantNotStatus(@Param(value = "merchantCode") String merchantCode);

    MerchantPO getMerchantByKanaCode(@Param(value = "merchantCode") String merchantCode);

    List<MerchantPO> getMerchantAllList();

    List<MerchantComboVO> selectMerChantList(@Param(value = "merchantCode") String merchantCode);

    @Select("select id, agent_level from t_merchant where merchant_code=#{merchantCode}")
    Map<String,String> findLevelByMerchantCode(@Param(value = "merchantCode") String merchantCode);

    @Select("select merchant_code as merchantCode,merchant_name as merchantName, agent_level as agentLevel from t_merchant where 1=1 and merchant_group_id=#{id}")
    List<Map<String, String>> queryOldMerchantListByParam(@Param(value = "id") String id);

    MerchantPO getMerchantInfo(@Param(value = "merchantCode") String merchantCode);

    List<MerchantPO> selectList(MerchantPO merchantPO);

    List<MerchantPO> selectRiskList(MerchantPO merchantPO);

    @Select("select id,language_name languageName, status,msg from t_merchant_language")
    List<MerchantLanguage> getMerchantLanguageList();

    List<MerchantPO> selectMonthList(MerchantVO merchantVO);

    MerchantPO selectById(@Param(value = "id") String id);

    int countByParam(@Param(value = "merchantName") String merchantName, @Param(value = "merchantCode") String merchantCode
            , @Param(value = "merchantAdmin") String merchantAdmin);

    int updateMerchant(MerchantPO merchantPO);

    int insertMerchant(MerchantPO merchantPO);

    List<TMerchantKey> queryKeyList(MerchantVO merchantVo);

    List<MerchantPO> queryMerchantKeyList(@Param("type") String type, @Param("startTime") String startTime, @Param("endTime") String endTime);

//    @Select("select `key` from t_merchant where merchant_code=#{merchantCode}")
    String getKey(@Param(value = "merchantCode") String merchantCode, @Param(value = "keyLabel") String keyLabel);


    void updateMerchantStatus(@Param(value = "merchantCode") String merchantCode, @Param(value = "status") Integer status, @Param(value = "remark") String remark);

    void updateMerchantBackendStatus(@Param(value = "merchantCode") String merchantCode, @Param(value = "status") Integer status);

    @Update("update t_merchant set `key`=#{key},history_key=#{old} ,update_time=now() where merchant_code=#{merchantCode}")
    void updateHistoryKey(@Param(value = "merchantCode") String merchantCode, @Param(value = "key") String key, @Param(value = "old") String old);

    @Update("update t_merchant_key set second_merchant_key=#{key},second_history_key=#{old} ,update_time=unix_timestamp(now()) * 1000 where merchant_code=#{merchantCode}")
    void updateSecondHistoryKey(@Param(value = "merchantCode") String merchantCode, @Param(value = "key") String key, @Param(value = "old") String old);


    @Update("update t_merchant set `admin_password`=#{password},update_time=now() where merchant_code=#{merchantCode}")
    void updateMerchantAdminPassword(@Param(value = "merchantCode") String merchantCode, @Param(value = "password") String password);


//    @Select("select `key`,start_time startTime,end_time endTime from t_merchant where merchant_code=#{merchantCode}")
    TMerchantKey getMerchantKeyPo(@Param(value = "merchantCode") String merchantCode);

    MerchantPO getMerchantByMerchantCode(@Param(value = "merchantCode") String merchantCode);

    List<MerchantPO> queryMerchantList(Map<String, Object> param);


    List<MerchantPO> queryMerchantListByGroup(Map<String, Object> param);

    List<Map<String, String>> queryMerchantListByParam(Map<String, Object> param);

    List<MerchantAdminVO> getAdminList();

    List<Map<String, Object>> queryAgentCount();

    @Update("update t_merchant k set k.start_time=#{start,jdbcType=DATE},k.end_time=#{end,jdbcType=DATE},`status`=#{status},`key`=#{key},updated_by=#{modifier,jdbcType=VARCHAR} " +
            "where k.end_time > #{end} and  k.merchant_code in(SELECT code from (select m.merchant_code as code from t_merchant m where m.parent_id  =#{parentId}) as a)")
    void updateChildrenKey(@Param(value = "parentId") String parentId, @Param(value = "key") String key, @Param(value = "start") String start,
                           @Param(value = "end") String end, @Param(value = "status") Integer status, @Param(value = "modifier") String modifier);

    void updateChildrenSecondKey(@Param(value = "key") String key, @Param(value = "secondHistoryKey") String secondHistoryKey, @Param(value = "modifier") String modifier, @Param(value = "codeList") List<String> codeList);

    @Update("update t_merchant m set m.start_time=#{start}, m.end_time = #{end} where m.parent_id = #{parentId} and m.end_time > #{end}")
    void updateChildMerchant(@Param(value = "parentId") String parentId, @Param(value = "start") String startTime,
                             @Param(value = "end") String endTime);

    @Update("update t_merchant m set m.computing_standard=#{computingStandard},m.technique_amount=#{techniqueAmount},m.payment_cycle=#{paymentCycle}," +
            "m.vip_amount=#{vipAmount},m.vip_payment_cycle=#{vipPaymentCycle},m.terrace_rate=#{terraceRate},m.technique_payment_cycle=#{techniquePaymentCycle}  where m.parent_id = #{parentId}")
    void updateChildMerchantAmount(@Param(value = "parentId") String parentId, @Param(value = "computingStandard") String computingStandard,
                                   @Param(value = "techniqueAmount") Long techniqueAmount, @Param(value = "paymentCycle") Integer paymentCycle,
                                   @Param(value = "vipAmount") Long vipAmount, @Param(value = "vipPaymentCycle") Integer vipPaymentCycle,
                                   @Param(value = "terraceRate") Double terraceRate, @Param(value = "techniquePaymentCycle") Integer techniquePaymentCycle);

    @Select({"<script>",
            "SELECT id,merchant_code,merchant_name,level,agent_level FROM t_merchant WHERE merchant_code IN ",
            "<foreach collection='merchantCode' index='index' item='item' open='(' separator=',' close=')'>",
            "  #{item} ",
            "</foreach>",
            "</script>"})
    List<MerchantPO> getMerchantInMerchantCode(@Param(value = "merchantCode") Set<String> merchantCode);

    List<MerchantPO> getMerchantInMerchantId(@Param(value = "merchantId") Set<String> merchantId);

    List<TMerchantKey> getKeyInMerchantCode(@Param(value = "merchantCode") Set<String> merchantCode);

    @Update("update t_merchant m set m.transfer_mode=#{transferMode}, m.url = #{url}, m.callback_url = #{callbackUrl}  where m.parent_id = #{parentId}")
    void updateChildTransferMode(@Param(value = "parentId") String parentId, @Param(value = "transferMode") Integer transferMode,
                                 @Param(value = "url") String url, @Param(value = "callbackUrl") String callbackUrl);

    @Update("update t_merchant m set m.url = #{url}, m.callback_url = #{callbackUrl},m.balance_url=#{balanceUrl}  where m.parent_id = #{parentId}")
    void updateChildUrl(@Param(value = "parentId") String parentId, @Param(value = "url") String url, @Param(value = "callbackUrl") String callbackUrl, @Param(value = "balanceUrl") String balanceUrl);

    @Update("update t_merchant k set k.start_time=#{start,jdbcType=DATE},k.end_time=#{end,jdbcType=DATE},k.`status`=#{status},k.`key`=#{key} " +
            "where  k.merchant_code in(SELECT merchant_code FROM (select m.merchant_code from t_merchant m where m.parent_id =#{parentId} )t1)")
    void forceUpdateChildrenKey(@Param(value = "parentId") String parentId, @Param(value = "key") String key, @Param(value = "start") String start,
                                @Param(value = "end") String end, @Param(value = "status") Integer status);

    @Update("UPDATE t_merchant SET admin_password = #{pwd},update_time = #{updateTime} WHERE  id = #{id} AND merchant_admin = #{merchantAdmin}")
    void updateMerchantPwdByParam(@Param("id") String merchantId, @Param("merchantAdmin") String merchantAdmin, @Param("pwd") String pwd, @Param("updateTime") String updateTime);

    @Select("select merchant_code from t_merchant where parent_id = #{merchantId} ")
    List<String> queryChildList(@Param("merchantId") String merchantId);

    @Select("select * from t_merchant where parent_id = #{merchantId} ")
    List<MerchantPO> queryChildPoList(@Param("merchantId") String merchantId);

    @Select("select merchant_code from t_merchant where parent_id = (select id from t_merchant where merchant_code=#{merchantCode})")
    List<String> queryChildren(@Param("merchantCode") String merchantCode);

    @Select("select merchant_code from t_merchant where agent_level in(0,2) and status=1")
    List<String> queryAllPlatformCode();

    List<MerchantInfoVo> findMerchantInfo(@Param(value = "list") List<String> merchantCodes);
    @Update("update t_merchant m set m.white_ip=#{whiteIp} where m.parent_id = #{parentId}")
    void updateChildrenIp(@Param(value = "parentId") String parentId, @Param(value = "whiteIp") String whiteIp);


    void updateChildrenIpAndDomain(@Param("mvo") MerchantVO merchantVO);

    List<MerchantPO> queryMerchantTree();

    List<MerchantPO> queryMerchantTreeByMerchantCode(@Param("merchantCode") String merchantCode);

    List<Map<String, String>> queryFTPUrlList();

    @Update("update t_merchant m set m.ftp_user =#{ftpUser},m.ftp_password =#{ftpPassword} where m.merchant_code=#{merchantCode}")
    void updateMerchantFTPInfo(@Param("merchantCode") String merchantCode, @Param("ftpUser") String ftpUser, @Param("ftpPassword") String ftpPassword);

    @Update("update t_merchant m set m.parent_id = null,m.old_parent =#{parentId} where m.merchant_code=#{merchantCode}")
    void deleteSubAgent(@Param("merchantCode") String merchantCode, @Param("parentId") String parentId);

    void batchUpdateMerchantParentId(MerchantAgentVO merchantAgentVO);

    List<MerchantTree> getParentMerchant();

    List<MerchantTree> getMerchantListTree(@Param("id") String id);

    List<MerchantPO> selectListByAgent(MerchantPO merchantPO);

    List<Map<String, String>> listParentCodeByMerchantCode(@Param(value = "allChildMerchantCode") List<String> allChildMerchantCode);

    @Select("select id from t_merchant where merchant_code = #{merchantCode}")
    Long getMerchantId(@Param("merchantCode") String merchantCode);

    @Select("select merchant_name from t_merchant where merchant_code = #{merchantCode}")
    String getMerchantName(@Param("merchantCode") String merchantCode);

    @Insert("insert into t_merchant_agent(`merchant_code`,`agent_id`,`agent_name`) value(#{merchantCode},#{agentId},#{agentName})")
    int insetMerchantAgent(@Param("merchantCode") String merchantCode, @Param("agentId") String agentId, @Param("agentName") String agentName);

    @Update("update t_merchant_agent m set m.agent_name = #{agentName} where m.merchant_code=#{merchantCode} and m.agent_id =#{agentId}")
    int updateMerchantAgent(@Param("merchantCode") String merchantCode, @Param("agentId") String agentId, @Param("agentName") String agentName);

    @Select("select agent_id from t_merchant_agent where merchant_code=#{merchantCode} and agent_id = #{agentId}")
    String getAgentId(@Param("merchantCode") String merchantCode, @Param("agentId") String agentId);

    @Select("select agent_id agentId,merchant_code merchantCode,agent_name agentName from t_merchant_agent where merchant_code=#{merchantCode} ")
    List<AgentVO> getAgentList(@Param("merchantCode") String merchantCode);

    @Select("select agent_id agentId,merchant_code merchantCode,agent_name agentName from t_merchant_agent where merchant_code=#{merchantCode} and agent_id =#{agentId}")
    AgentVO getAgent(@Param("merchantCode") String merchantCode, @Param("agentId") String agentId);

    List<MerchantTree> getMerchantByTag(@Param("tag") Integer tag, @Param(value = "merchantName") String merchantName);

    List<MerchantTree> getMerchantByIds(@Param("ids") List<String> ids);

    List<MerchantPO> merchantDomainList(@Param("merchantTag") Integer merchantTag, @Param(value = "containsType") Integer containsType, @Param(value = "containsStr") String containsStr, @Param(value = "parentCode") String parentCode);

    List<MerchantPO> merchantAppDomainList(@Param("merchantTag") Integer merchantTag, @Param(value = "containsType") Integer containsType, @Param(value = "containsStr") String containsStr, @Param(value = "parentCode") String parentCode, @Param(value = "merchantCode") String merchantCode);

    int updateDomainList(@Param("merchantTag") Integer merchantTag, @Param(value = "containsType") Integer containsType
            , @Param(value = "containsStr") String containsStr, @Param(value = "newDomainStr") String newDomainStr, @Param(value = "merchantCode") String merchantCode, @Param(value = "parentCode") String parentCode, @Param(value = "domainType") String domainType);

    int updateKanaCode(@Param("kanaCode") String kanaCode, @Param(value = "kanaCodeTime") Date kanaCodeTime, @Param(value = "merchantCode") String merchantCode);

    int updateKanaCodeByParentId(@Param("kanaCode") String kanaCode, @Param(value = "kanaCodeTime") Date kanaCodeTime, @Param(value = "parentId") String parentId);

    List<String> getMerhcantCodeList(@Param(value = "parentId") String parentId);

    int updateSerialNumber(@Param("serialNumber") Integer serialNumber, @Param(value = "merchantCode") String merchantCode);

    int checkSerialNumber(@Param("serialNumber") Integer serialNumber, @Param("parentId") String parentId);

    int updateAppDomainList(@Param("merchantTag") Integer merchantTag, @Param(value = "containsType") Integer containsType
            , @Param(value = "containsStr") String containsStr, @Param(value = "newDomainStr") String newDomainStr, @Param(value = "merchantCode") String merchantCode, @Param(value = "parentCode") String parentCode);

    @Select("select merchant_code from t_merchant_config where merchant_tag = 1")
    List<String> getCreditMerchant();

    @Select("select merchant_code from t_merchant_config where merchant_tag = 0 or merchant_tag is null")
    List<String> getCashMerchant();

    @Select("select id,merchant_code merchantCode from t_merchant where status=1")
    List<Map<String, Object>> getMerchantIdList();


    List<String> getAllApiDomain(@Param(value = "merchantCode") String merchantCode);

    List<CheckApiDomainVo> getCheckApiDomain();

    @Update("update t_merchant set app_domain =#{newDomain} where app_domain=#{oldDomain} ")
    void updateApiDomain(@Param(value = "oldDomain") String oldDomain, @Param(value = "newDomain") String newDomain);

    @Update("update t_domain set domain_name =#{newDomain},update_time=NOW() where domain_name=#{oldDomain}")
    void updateDomainPool(@Param(value = "oldDomain") String oldDomain, @Param(value = "newDomain") String newDomain);

    @Update("update t_merchant set app_domain =#{newDomain} where merchant_code=#{merchantCode}")
    int updateApiDomainByMerchantCode(@Param(value = "newDomain") String newDomain, @Param(value = "merchantCode") String merchantCode);

    int updateMerchantGroupId(@Param(value = "merchantGroupId") String merchantGroupId, @Param(value = "domainGroupCode") String domainGroupCode, @Param("merchantCodeList") List<String> merchantCodeList);

    int updateDomainGroupCode(@Param(value = "groupCode") String groupCode, @Param("merchantCodeList") List<String> merchantCodeList);

    @Update("update t_merchant set merchant_group_id =0 where merchant_group_id=#{merchantGroupId}")
    int updateMerchantGroupIdDefult(@Param(value = "merchantGroupId") String merchantGroupId);

    @Update("update t_merchant set domain_group_code = #{defaultCode} where domain_group_code=#{groupCode}")
    int updateDomainGroupCodeDefult(@Param(value = "defaultCode") String defaultCode, @Param(value = "groupCode") String groupCode);

    @Select("SELECT count(*) FROM `t_merchant` WHERE app_domain=#{domain}")
    int checkMerchantDomainExist(@Param(value = "domain") String domain);

    /**
     * 修改超级管理员用户名
     *
     * @param merchantPO
     * @return boolean
     */
    boolean updateMerchantAdminById(@Param(value = "merchantPO") MerchantPO merchantPO);

    List<Map<String, String>> getGroupMerchantList();

    /**
     * 获取商户code和商户钱包类型对应列表
     *
     * @return
     */
    @Select("select merchant_code AS merchantCode, transfer_mode AS transferMode from t_merchant")
    List<MerchantPO> queryMerchantTransferModeList();

    List<String> getDifferentGroupMerchantList();

    @Select("select a.id,a.uid,a.amount from t_account a LEFT join t_user u on a.uid=u.uid where u.username=#{userName} and u.merchant_code=#{merchantCode}")
    AccountPO checkBalance(@Param("userName") String username, @Param("merchantCode") String merchantCode);

    @Update("update t_account a set a.amount =#{amount} where a.uid =#{uid}")
    void updateAccount(@Param("uid") Long uid, @Param("amount") BigDecimal amount);

    void insertAccountChange(AccountChangeHistoryFindVO vo);

    List<MerchantDomainPO> selectMerchantDomainList(@Param("merchantIds") List<String> merchantIds);


    List<MerchantPO> listByDomainName(@Param("domainType") Integer domainType, @Param("domainName") String domainName);

    void updateNewDomain(@Param("oldDomain") String oldDomain,@Param("domainType")  Integer domainType,@Param("newDomain") String newDomain);

    void updateNewVideoDomain(@Param("oldDomain") String oldDomain,@Param("domainType")  Integer domainType,@Param("newDomain") String newDomain);

    void replaceH5(@Param("source") String source,@Param("target")  String target);

    void replacePC(@Param("source") String source,@Param("target")  String target);

    void replaceAPP(@Param("source") String source,@Param("target")  String target);

    int updateMerchantDomainByMerchantCode(@Param(value = "domainType")Integer domainType,@Param(value = "newDomain") String newDomain,@Param(value = "oldDomain") String oldDomain,  @Param(value = "merchantCode") String merchantCode);

    List<MerchantPO> queryChildrenMerchant(@Param(value = "parentId") String parentId);

    void updateChildrenMerchantStatus(@Param("merchantCodeList") List<String> merchantCodeList, @Param(value = "status") Integer status);
    void updateChildrenMerchantBackendStatus(@Param("merchantCodeList") List<String> merchantCodeList, @Param(value = "status") Integer status);

    List<String> getOldH5PcDomain();

    int checkMerchantDomainExistByDomainType(@Param(value = "domainName") String domainName,@Param(value = "domainType") Integer domainType);


    @Update("update t_merchant set is_open_bill = #{openBill} where id=#{merchantId}")
    int updateOpenBill(@Param(value = "merchantId") String merchantId, @Param(value = "openBill") Integer openBill);

    void clearOrderData(@Param(value = "merchantCode") String merchantCode,@Param(value = "indexNum") Integer indexNum, @Param(value = "num") Integer num);

    void clearOrderDetailData(@Param(value = "merchantCode") String merchantCode, @Param(value = "indexNum") Integer indexNum, @Param(value = "num") Integer num);

    void clearSettleData(@Param(value = "merchantCode") String merchantCode, @Param(value = "indexNum") Integer indexNum, @Param(value = "num") Integer num);

    void clearAccountChangeHistoryData(@Param(value = "merchantCode") String merchantCode, @Param(value = "indexNum") Integer indexNum, @Param(value = "num") Integer num);

    int queryMerchantCountByGroup(Map<String, Object> param);

    void updateApiDomainByMerchantCodes(@Param("newDomain") String domain, @Param("merchantCodes") List<String> collect);


    int updateMerchantDomainByMerchantCodes(@Param("domainType") Integer domainType, @Param("newDomain") String domainName,
                                            @Param("oldDomain") String oldDomain, @Param("merchantCodeList") List<String> merchantCodeList,
                                            @Param("noMerchantCodeList") List<String> noMerchantCodeList);

    int updateMerchantVideoDomainByMerchantCodes(@Param("domainType") Integer domainType, @Param("newDomain") String domainName,
                                            @Param("oldDomain") String oldDomain, @Param("merchantCodeList") List<String> merchantCodeList,
                                            @Param("noMerchantCodeList") List<String> noMerchantCodeList);


    @Update("update t_merchant set language_list = #{languageList} where id = #{id}")
    int updateLanguage(@Param(value = "id")String id,@Param(value = "languageList") String languageList);

    @Select("SELECT language_list FROM `t_merchant` WHERE  id = #{id}")
    String getLanguageById(@Param(value = "id")String id);

    List<JSONObject> getMerchantByApiDomains(@Param("domainList") List<String> domainList, @Param("merchantGroupType") Integer merchantGroupType);

    List<MerchantPO> getMerchantByDomains(@Param("domain") String domain);

    @Select("SELECT merchant_code FROM t_merchant WHERE domain_group_code = #{code} AND agent_level in (0,2) AND status = 1")
    List<String> queryMerchantByGroupCode(@Param(value = "code") String groupCode);

    List<JSONObject> selectMerchantCountByCode(@Param("list") List<String> merchantCodeList);

    List<MerchantPO> queryMerchantCurrency(@Param("agentLevel") Integer agentLevel);

    int updateMerchantCurrency(@Param("currencyCode") String currencyCode, @Param("merchantCode") String merchantCode);

    @Select("SELECT id as id,merchant_code as merchantCode,merchant_name as merchantName FROM t_merchant mt WHERE mt.agent_level = 1")
    List<MerchantPO> queryChannelMerchant();

    List<MerchantPO> querySecondMerchantCurrency(@Param("id") String id);

    @Select("select sport_list as currencyCode,merchant_code as merchantCode from t_merchant t where t.merchant_code = #{merchantCode}")
    MerchantPO selectMerchantCurrency(@Param("merchantCode") String merchantCode);


    @Update("update t_merchant m set m.merchant_name= #{merchantName} where m.merchant_code = #{merchantCode} ")
    int updateMerchantName(@Param("merchantCode") String merchantCode,@Param("merchantName") String merchantName);

    List<FrontendMerchantDomain> queryMerchantDomain(FrontendMerchantDomain merchantDomain);

    List<MerchantDomainPO> queryMerchantDomainByKey(MerchantDomainPO domainPo);

    int changeMerchantDomain(DomainChangeDTO domainChangeDto);

    @Select("select merchant_code as merchantCode from t_frontend_merchant_relation t where t.frontend_merchant_group_id = #{groupId}")
    List<String> selectMerchantCode (@Param("groupId") Integer groupId);

    int updateMerchantDomain(@Param("newMerchantDomain") String newMerchantDomain, @Param("oldMerchantDomain") String oldMerchantDomain, @Param("domainType") String domainType, @Param("merchantCodeList") List<String> merchantCodeList);

    int createFrontendMerchantGroup(FrontendMerchantGroup merchantGroup);

    int updateFrontendMerchantGroup(FrontendMerchantGroup merchantGroup);

    int selectFrontendMerchantGroup(FrontendMerchantGroup merchantGroup);

    int batchSaveFrontendMerchantRelation(@Param("relationList") List<FrontendMerchantRelation> relationList);

    int delFrontendMerchantRelation(@Param("merchantGroupId") Long merchantGroupId);

    int selectFrontendDomain(FrontendMerchantDomain merchantDomain);

    int createFrontendDomain(FrontendMerchantDomain merchantDomain);

    int delFrontendDomain(FrontendMerchantDomain merchantDomain);

    List<FrontendMerchantGroupDomainPO> queryFrontendMerchantGroup(FrontendMerchantGroupDomainPO groupDomainPo);

    FrontendMerchantGroup selectMerchantGroupById(FrontendMerchantGroup merchantGroup);

    int updateMerchantEvent(@Param("enevtJson") String enevtJson,@Param("merchantCode") String merchantCode);

    String selectMerchantEventByCode(@Param("merchantCode") String merchantCode);

    List<String> getMerchantByName(String merchantName);

    int queryMerchant(@Param(value = "domainList") List<String> domainList);

    List<MerchantSimpleVO> queryMerchantSimpleListByParams(JSONObject param);

    int queryMerchantVideoDomain(@Param(value = "domainList") List<String> domainList);

    List<MerchantPO> queryMerchantKey(@Param("codeList") List<String> codeList);

    List<MerchantKeyVO> selectAllMerchantKey();

    int addMerchantKey(MerchantKeyVO merchantKeyVO);

    MerchantKeyVO selectMerchantKey(MerchantKeyVO merchantKeyVO);

    MerchantKeyVO queryMerchantKeyInfo(MerchantKeyVO merchantKeyVO);

    int updateMerchantKey (MerchantKeyVO merchantKeyVO);

    int enableMerchantKey(MerchantKeyVO merchantKeyVO);
}