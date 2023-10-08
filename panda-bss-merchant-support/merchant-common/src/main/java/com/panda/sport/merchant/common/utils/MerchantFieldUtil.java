package com.panda.sport.merchant.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.panda.sport.merchant.common.enums.AggregateFestivalEnum;
import com.panda.sport.merchant.common.enums.NoticeReleaseEnum;
import com.panda.sport.merchant.common.enums.NoticeTypeEnum;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author :  duwan
 * @Project Name :  sp
 * @Package Name :  com.panda.sport.merchant.common.utils
 * @Description :  商户对象字段处理类
 * @Date: 2020-09-01 16:43
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
public class MerchantFieldUtil<T> {

    /**
     * 商户管理字段映射关系对照表
     */
    public static Map<String,String> FIELD_MAPPING = new HashMap<>();

    /**
     * 过滤字段
     */
    public static List<String> filterFieldNames = new ArrayList<>();

    /**
     * 币种映射
     */
    public static Map<String,String> currencyMap = new HashMap<>();

    /**
     * 缴纳周期映射
     */
    public static Map<String,String> techniquePaymentCycleMap = new HashMap<>();

    /**
     * VIP缴纳周期映射
     */
    public static Map<String,String> vipPaymentCycle = new HashMap<>();

    /**
     * VIP缴纳周期映射
     */
    public static Map<String,String> paymentCycle = new HashMap<>();

    /**
     * //商户钱包
     */
    public static Map<String,String> transferModeMap = new HashMap<>();

    /**
     * 计算标准
     */
    public static Map<String,String> computingStandardMap = new HashMap<>();


    public String noInitObject = "{}";

    //初始化
    static {
        computingStandardMap.put("1","盈利金额");
        computingStandardMap.put("2","投注金额");

        transferModeMap.put("1","免转钱包");
        transferModeMap.put("2","转账钱包");

        paymentCycle.put("1","每月");
        paymentCycle.put("2","每季度");
        paymentCycle.put("3","每半年");
        paymentCycle.put("4","每年");

        vipPaymentCycle.put("1","每月");
        vipPaymentCycle.put("2","每季度");
        vipPaymentCycle.put("3","每半年");
        vipPaymentCycle.put("4","每年");

        techniquePaymentCycleMap.put("1","每月");
        techniquePaymentCycleMap.put("2","每季度");
        techniquePaymentCycleMap.put("3","每半年");
        techniquePaymentCycleMap.put("4","每年");

        currencyMap.put("1","人民币");
        currencyMap.put("2","美元");
        currencyMap.put("3","港币");
        currencyMap.put("4","越南盾");
        currencyMap.put("5","新加坡币");
        currencyMap.put("6","英镑");
        currencyMap.put("7","欧元");
        currencyMap.put("8","比特币");

        FIELD_MAPPING.put("uploadChangeMerchantName","上传脱敏文件");
        FIELD_MAPPING.put("marketLevel","赔率分组");
        FIELD_MAPPING.put("betExtraDelay","投注延时");
        FIELD_MAPPING.put("percentage","特殊限额");
        FIELD_MAPPING.put("cancelBet","注单取消");
        FIELD_MAPPING.put("url", "加扣款接口");
        FIELD_MAPPING.put("callbackUrl", "加扣款回调url");
        FIELD_MAPPING.put("maxBet", "商户下最大投注额封锁值");
        FIELD_MAPPING.put("transferMode", "转账类型");
        FIELD_MAPPING.put("status", "状态");
        FIELD_MAPPING.put("client", "客户端");
        FIELD_MAPPING.put("merchantBackground", "商户后台");
        FIELD_MAPPING.put("whiteIp", "IP白名单设置");
        FIELD_MAPPING.put("balanceUrl", "查询余额URL");
        FIELD_MAPPING.put("logo", "商户LOGO");
        FIELD_MAPPING.put("childMaxAmount", "下级商户最大数量");
        FIELD_MAPPING.put("key", "商户密钥");
        FIELD_MAPPING.put("email", "电子邮件");
        FIELD_MAPPING.put("contact", "联系人");
        FIELD_MAPPING.put("phone", "联系电话");
        FIELD_MAPPING.put("merchant_admin", "商户管理员");
        FIELD_MAPPING.put("admin_password", "密码");
        FIELD_MAPPING.put("province", "省市地区");
        FIELD_MAPPING.put("country", "国家");
        FIELD_MAPPING.put("order", "排序");
        FIELD_MAPPING.put("address", "详细地址");
        FIELD_MAPPING.put("parentId","添加/删除");
        FIELD_MAPPING.put("delete","删除");
        FIELD_MAPPING.put("merchantCode", "商户编码");
        FIELD_MAPPING.put("merchantName", "商户名称");
        FIELD_MAPPING.put("statusDescription", "状态");
        FIELD_MAPPING.put("username", "用户名");
        FIELD_MAPPING.put("password", "密码");
        FIELD_MAPPING.put("avatar", "用户头像");
        FIELD_MAPPING.put("roleId", "角色权限");
        FIELD_MAPPING.put("enabled", "启用状态");
        FIELD_MAPPING.put("adjustCause", "调整原因");
        FIELD_MAPPING.put("synchronizationBill", "对账单数据同步");
        FIELD_MAPPING.put("synchronizationOrder", "用户投注统计同步");
        FIELD_MAPPING.put("synchronizationMatch", "赛事投注统计同步");
        FIELD_MAPPING.put("adjustAmount", "调整金额");
        FIELD_MAPPING.put("pcDomain", "Panda PC域名");
        FIELD_MAPPING.put("h5Domain", "Panda H5域名");
        FIELD_MAPPING.put("appDomain", "Panda App域名");
        FIELD_MAPPING.put("currency", "币种");
        FIELD_MAPPING.put("commerce", "panda商务");
        FIELD_MAPPING.put("endTime", "商户有效期");
        FIELD_MAPPING.put("domains", "商户根域名*");
        FIELD_MAPPING.put("domainUrl", "数据商动画域名");
        FIELD_MAPPING.put("serverSwitch", "17ce 开关");
        FIELD_MAPPING.put("importAbnormalIP", "导入异常ip");
        FIELD_MAPPING.put("deleteAllIp", "异常ip");
        FIELD_MAPPING.put("domain", "商户根域名更新");
        FIELD_MAPPING.put("logoEnable","预约投注");
        FIELD_MAPPING.put("maxSeriesNum","最大串关数");
        FIELD_MAPPING.put("minSeriesNum","最小串关数");
        FIELD_MAPPING.put("profesTag","专业版UI");
        FIELD_MAPPING.put("ballSwitch","球类开关");
        FIELD_MAPPING.put("matchSwitch","联赛开关");
        FIELD_MAPPING.put("defaultLanguage","默认语言");
        FIELD_MAPPING.put("languageList","C端默认语言");
        FIELD_MAPPING.put("bookBetSwitch","预约投注开关");
        FIELD_MAPPING.put("changeNum","频率设定");
        FIELD_MAPPING.put("openEsport","电子竞技");
        FIELD_MAPPING.put("reminderSet","提醒设定");
        FIELD_MAPPING.put("closeAndOpen","禁用/启用");
        FIELD_MAPPING.put("postArticle","发布文章");
        FIELD_MAPPING.put("level", "等级");
        FIELD_MAPPING.put("paymentCycle", "缴纳周期");
        FIELD_MAPPING.put("rangeAmountBegin", "金额范围开始");
        FIELD_MAPPING.put("rangeAmountEnd", "金额范围结束");
        FIELD_MAPPING.put("rateId", "费率ID");
        FIELD_MAPPING.put("techniqueAmount", "技术服务费 (万元)");
        FIELD_MAPPING.put("techniquePaymentCycle", "技术服务费缴纳周期");
        FIELD_MAPPING.put("terraceRate", "平台费率");
        FIELD_MAPPING.put("vipAmount", "VIP费用");
        FIELD_MAPPING.put("vipPaymentCycle", "VIP费用缴纳周期");

        FIELD_MAPPING.put("levelName", "等级名称");
        FIELD_MAPPING.put("computingStandard", "计算模式");

        FIELD_MAPPING.put("openVrSport", "VR体育");
        FIELD_MAPPING.put("openVideos", "视频开关");
        FIELD_MAPPING.put("openVideoExternal", "对外视频地址开关 0关闭 1打开");
        FIELD_MAPPING.put("settleSwitchAdvance", "提前结算");
        FIELD_MAPPING.put("closeOpen", "禁用/启用");


        FIELD_MAPPING.put("title", "标题");
        FIELD_MAPPING.put("context", "正文");
        FIELD_MAPPING.put("releaseRange", "发布范围");
        FIELD_MAPPING.put("isPop", "是否弹出");
        FIELD_MAPPING.put("noticeTypeId", "公告类型");
        FIELD_MAPPING.put("isFrom", "公告来源");
        FIELD_MAPPING.put("isTag", "标记");
        FIELD_MAPPING.put("standardMatchId", "赛事ID");

        FIELD_MAPPING.put("remarks", "备注");
        FIELD_MAPPING.put("remark", "备注");
        FIELD_MAPPING.put("name", "名称");
        FIELD_MAPPING.put("createTime", "创建时间");
        FIELD_MAPPING.put("modifyTime", "更新时间");
        FIELD_MAPPING.put("createUser", "创建用户");
        FIELD_MAPPING.put("modifyUser", "更新用户");
        FIELD_MAPPING.put("sendTime", "发布时间");
        FIELD_MAPPING.put("kanaCode", "公司代码");
        FIELD_MAPPING.put("isTest", "测试商户状态");
        FIELD_MAPPING.put("isExternal", "内外部商户状态");
        FIELD_MAPPING.put("serialNumber", "流水等级");
        FIELD_MAPPING.put("isApp", "APP商户状态");
        FIELD_MAPPING.put("videoSwitch","视频流量管控开关");
        FIELD_MAPPING.put("chatRoomSwitch","聊天室状态");
        FIELD_MAPPING.put("backendSwitch","商户后台开关");
        FIELD_MAPPING.put("merchantStatus","商户状态");
        FIELD_MAPPING.put("detailMerchant","详细商户");
        FIELD_MAPPING.put("setWack","提醒设定");
        FIELD_MAPPING.put("defaultVideoDomain","商户默认视频域名");
        FIELD_MAPPING.put("updateFrontendMerchantGroup","编辑商户前端域名分组");
        FIELD_MAPPING.put("abnormalUserIds","异常用户id");
        FIELD_MAPPING.put("createdBy","发版人");
        FIELD_MAPPING.put("updatedBy","最后修改人");
        FIELD_MAPPING.put("upload","上传");
        FIELD_MAPPING.put("isShuf","是否轮播");
        FIELD_MAPPING.put("isTop","是否置顶");
        FIELD_MAPPING.put("beginTime","生效时间(24小时制)");
        FIELD_MAPPING.put("uploadName","附件名称");
        //FIELD_MAPPING.put("endTime","结束时间");
        //过滤字段
        filterFieldNames.add("createTime");
        filterFieldNames.add("modifyTime");
        filterFieldNames.add("serialVersionUID");
        filterFieldNames.add("statusDescription");
        filterFieldNames.add("logo");
        filterFieldNames.add("lastPasswordResetTime");
        filterFieldNames.add("filePath");
        filterFieldNames.add("fileName");
        filterFieldNames.add("updatedBy");
        filterFieldNames.add("noticeEndTime");
        filterFieldNames.add("updateTime");
        filterFieldNames.add("lastUpdated");
        filterFieldNames.add("id");

        //三端
        FIELD_MAPPING.put("lineCarrierName", "线路商名称");
        FIELD_MAPPING.put("areaName", "区域名称");
        FIELD_MAPPING.put("selfTestSwitch", "自检开关");
        FIELD_MAPPING.put("enableSwitch", "启用开关");
        FIELD_MAPPING.put("replaceDomain", "域名选择");
        FIELD_MAPPING.put("clearCache", "清除缓存");
        FIELD_MAPPING.put("cleanMerchantCache", "清除商户缓存");
        FIELD_MAPPING.put("userName", "用户名");
        FIELD_MAPPING.put("password", "密码");
        FIELD_MAPPING.put("secret", "密钥");
        FIELD_MAPPING.put("domainImport", "域名导入");
        FIELD_MAPPING.put("17ce", "17ce开关");
        FIELD_MAPPING.put("addRoles", "添加角色");
        FIELD_MAPPING.put("menuPermissionsSet", "菜单权限设置");
        FIELD_MAPPING.put("domainType", "域名类型");
        FIELD_MAPPING.put("groupType", "分组类型");
        FIELD_MAPPING.put("lineCarrierId", "线路商ID");
        FIELD_MAPPING.put("domainName", "域名");
        FIELD_MAPPING.put("operator", "操作人");
        FIELD_MAPPING.put("selfTestTag", "自检开关");
        FIELD_MAPPING.put("programId", "方案组id");
        FIELD_MAPPING.put("merchantCodes", "详细商户");
        FIELD_MAPPING.put("sort", "排序");
        FIELD_MAPPING.put("menuName", "菜单名称");
        FIELD_MAPPING.put("menuSign", "菜单标识");
        FIELD_MAPPING.put("pid", "父级菜单");
        FIELD_MAPPING.put("h5Threshold;", "h5阈值");
        FIELD_MAPPING.put("pcThreshold;", "pc阈值");
        FIELD_MAPPING.put("apiThreshold", "API阈值");
        FIELD_MAPPING.put("imgThreshold", "图片阈值");
        FIELD_MAPPING.put("programName", "方案名称");
        FIELD_MAPPING.put("merchantGroupId", "商户组名称");
        FIELD_MAPPING.put("merchantGroupName", "商户组名称");
        FIELD_MAPPING.put("groupName", "商户组");
        FIELD_MAPPING.put("soldOut", "下架");
    }


    /**
     * 对象比较器
     * @param oldBean
     * @param newBean
     * @return
     */
    public MerchantLogFiledVO compareObject(Object oldBean, Object newBean) {
        String jsonString = JSONObject.toJSONString(newBean);
        boolean newB = noInitObject.equals(JSONObject.toJSONString(newBean));
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        T pojo1 = (T) oldBean;
        T pojo2 = (T) newBean;
        try {
            Class clazz = pojo1.getClass();
            Field[] fields = pojo1.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (filterFieldNames.contains(field.getName())) {
                    continue;
                }
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                Method getMethod = pd.getReadMethod();
                Object o1 = getMethod.invoke(pojo1);
                Object o2 = getMethod.invoke(pojo2);

                if ((!newB && o2 == null) || String.valueOf(o1).equals(String.valueOf(o2))) {
                    continue;
                }
                try {
                    if ("sendTime".equals(field.getName()) ){
                        if (o1 != null) {
                            o1 = DateUtils.transferLongToDateStrings(Long.valueOf(o1.toString()));
                        }
                        if (o2 != null) {
                            o2 = DateUtils.transferLongToDateStrings(Long.valueOf(o2.toString()));
                        }
                    }
                }catch (Exception e){
                    log.error("时间处理异常！",e);
                }
                if (o1 == null || o2 == null || !o1.toString().equals(o2.toString())) {
                    log.info("字段名称" + field.getName() + ",旧值:" + o1 + ",新值:" + o2);
                    String value = FIELD_MAPPING.get(field.getName());
                    log.info("value,{}",value);
                    log.info("filedName,{}", field.getName());

                    if (StringUtil.isNotBlank(value) && value.contains("密码")){
                        vo.getFieldName().add(value);
                        vo.getBeforeValues().add("*");
                        vo.getAfterValues().add("*");
                    }else if (StringUtil.isNotBlank(value) && "enabled".equals(field.getName())){
                        vo.getFieldName().add(value);
                        vo.getBeforeValues().add("1".equals(String.valueOf(o1))?"启用":"禁用");
                        vo.getAfterValues().add("1".equals(String.valueOf(o2))?"启用":"禁用");

                    // 过滤默认语言
                    }else if (StringUtil.isNotBlank(value) && "defaultLanguage".equals(field.getName())){

                        vo.getFieldName().add(value);
                        vo.getBeforeValues().add(AggregateFestivalEnum.getInstance(String.valueOf(o1)).getValue());
                        vo.getAfterValues().add(AggregateFestivalEnum.getInstance(String.valueOf(o2)).getValue());

                    // 过滤语言列表
                    }else if (StringUtil.isNotBlank(value) && "languageList".equals(field.getName())){
                        log.info("进入过滤语言列表");
                        vo.getFieldName().add(value);
                        // 解析字符串
                        String languageO1 = Arrays.stream(String.valueOf(o1).split(","))
                                .map(lan -> AggregateFestivalEnum.getInstance(lan).getValue())
                                .collect(Collectors.joining(","));

                        String languageO2 = Arrays.stream(String.valueOf(o2).split(","))
                                .map(lan -> AggregateFestivalEnum.getInstance(lan).getValue())
                                .collect(Collectors.joining(","));

                        vo.getBeforeValues().add(languageO1);
                        vo.getAfterValues().add(languageO2);
                    }else if (StringUtil.isNotBlank(value) && "isTag".equals(field.getName())){
                        vo.getFieldName().add(value);
                        vo.getBeforeValues().add("1".equals(String.valueOf(o1))?"勾选":"不勾选");
                        vo.getAfterValues().add("1".equals(String.valueOf(o2))?"勾选":"不勾选");
                    }else if (StringUtil.isNotBlank(value) && "noticeTypeId".equals(field.getName())){
                        vo.getFieldName().add(value);
                        vo.getBeforeValues().add(o1 != null ? NoticeTypeEnum.getNoticeType((int) o1) : null);
                        vo.getAfterValues().add(NoticeTypeEnum.getNoticeType((int) o2));
                    }else if (StringUtil.isNotBlank(value) && "releaseRange".equals(field.getName())){
                        vo.getFieldName().add(value);
                        vo.getBeforeValues().add(o1 != null ? NoticeReleaseEnum.getReleaseDescribe(String.valueOf(o1)) : null);
                        vo.getAfterValues().add(NoticeReleaseEnum.getReleaseDescribe(String.valueOf(o2)));
                    }else if (StringUtil.isNotBlank(value) && ("isPop".equals(field.getName()) || "isShuf".equals(field.getName())|| "isTop".equals(field.getName()))){
                        vo.getFieldName().add(value);
                        vo.getBeforeValues().add("1".equals(String.valueOf(o1)) ?"是":"否");
                        vo.getAfterValues().add("1".equals(String.valueOf(o2))?"是":"否");
                    }else if (StringUtil.isNotBlank(value) && "beginTime".equals(field.getName())){
                        List<Field> list = Arrays.stream(fields).filter(f -> "noticeEndTime".equals(f.getName())).collect(Collectors.toList());
                        if(list.size() > 0) {
                            vo.getFieldName().add(value);
                            Method method = new PropertyDescriptor("noticeEndTime", clazz).getReadMethod();
                            Object oldEndTime = method.invoke(pojo1);
                            Object newEndTime = method.invoke(pojo2);
                            String beforeValue = null, afterValue = null;
                            if (o1 != null && oldEndTime != null) {
                                beforeValue = DateUtils.transferLongToDateStrings(Long.valueOf(o1.toString())) + "至" + DateUtils.transferLongToDateStrings(Long.valueOf(oldEndTime.toString()));
                            }
                            if(o2 != null && newEndTime != null) {
                                afterValue = DateUtils.transferLongToDateStrings(Long.valueOf(o2.toString())) + "至" + DateUtils.transferLongToDateStrings(Long.valueOf(newEndTime.toString()));
                            }
                            vo.getBeforeValues().add(beforeValue);
                            vo.getAfterValues().add(afterValue);
                        }
                    } else{
                        vo.getFieldName().add(value == null ? field.getName() : value);
                        vo.getBeforeValues().add(convertValue(value == null ? field.getName() : value, o1 == null? null : String.valueOf(o1), null));
                        vo.getAfterValues().add(convertValue(value == null ? field.getName() : value,null, o2 == null? null : String.valueOf(o2)));
                    }
                }
            }
        } catch (Exception e) {
            log.error("字段对比异常！", e);
        }
        log.info("最后结果 ： "+JSON.toJSONString(vo));
        return vo;
    }

    private String convertValue(String fieldName, String beforeValue, String afterValue) {
        if (fieldName.contains("状态") || fieldName.contains("开关")) {
            if(StringUtil.isNotBlank(beforeValue)) {
                return "1".equals(beforeValue)?"启用":"禁用";
            }else {
                return "1".equals(beforeValue)?"禁用":"启用";
            }
        }
        return StringUtil.isNotBlank(beforeValue) ? beforeValue : afterValue;
    }
}
