package com.panda.sport.merchant.common.utils;

import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.AggregateFestivalEnum;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.enums.SportEnum;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.formula.functions.T;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;



@Slf4j
public class MerchantUtil<T> {

    public static Map<String,String> FIELD_MAPPING = new HashMap<>();


    /**
     * 活动运营活动设置
     */
    public static Map<String,String> FIELD_ACTIVE_MAPPING = new HashMap<>();


    /**
     * 运营位
     */
    public static Map<String,String> FIELD_OPERATION_MAPPING = new HashMap<>();


    /**
     * 文章管理
     */
    public static Map<String,String> FIELD_ARTICLE_MAPPING = new HashMap<>();

    /**
     * 新建渠道字段
     */
    public static Map<String,String> FIELD_CHANNEL_MAPPING = new HashMap<>();

    /**
     * 新建渠道字段
     */
    public static Map<String,String> FIELD_MERCHNTLEVEL_MAPPING = new HashMap<>();

    public static Map<String,String> FIELD_MERCHNTRATE_MAPPING = new HashMap<>();

    public static Map<String,String> FIELD_ACTIVITY_MAPPING = new HashMap<>();

    /**
     * 新建渠道过滤字段
     */
    public static List<String> filterFieldAddNames = new ArrayList<>();

    /**
     * 过滤字段
     */
    public static List<String> filterFieldNames = new ArrayList<>();

    public static List<String> filterField = new ArrayList<>();

    public static List<String> filterTrateField = new ArrayList<>();

    public static List<String> filterActivityField = new ArrayList<>();

    public static List<String> filterActiveFieldAddNames = new ArrayList<>();

    public static List<String> filterOpertationAddNames = new ArrayList<>();

    public static List<String> filterArticelAddNames = new ArrayList<>();



    //报表标题自动配置类
    static {
        FIELD_MAPPING.put("logoEnable","Logo状态");
        FIELD_MAPPING.put("profesTag","PC端默认版式");
        FIELD_MAPPING.put("userProfixDigits","用户名前缀位数");
        FIELD_MAPPING.put("maxSeriesNum","最大串关数");
        FIELD_MAPPING.put("minSeriesNum", "最小串关数");
        FIELD_MAPPING.put("title", "中文简体  PC  浏览器  title,英文 PC  浏览器  title,中文繁体  PC  浏览器  title,越南语  PC  浏览器  title,泰语 PC  浏览器  title");
        FIELD_MAPPING.put("darkLogoUrl", "PC logo");
        FIELD_MAPPING.put("pcLogoUrl", "PC   浏览器  logo");
        FIELD_MAPPING.put("compatLogoUrl", "页面兼容性提示logo:（ 页面兼容性提示）");
        FIELD_MAPPING.put("loadLogoUrl", "PC   列表页  loading ");
        FIELD_MAPPING.put("leagueLogoUrl", "无联赛logo");
        FIELD_MAPPING.put("videoLogoUrl", "PC视频异常");
        FIELD_MAPPING.put("bannerUrl", "新版H5 默认banner图片");
        FIELD_MAPPING.put("marketDefault", "默认赔率类型  香港盘");
        FIELD_MAPPING.put("h5Default", "H5端默认版式 白色版");
        FIELD_MAPPING.put("appDefault", "APP端默认版式 白色版");

        FIELD_CHANNEL_MAPPING.put("1", "基本信息");
        FIELD_CHANNEL_MAPPING.put("merchantName", "商户名称");
        FIELD_CHANNEL_MAPPING.put("logo", "logo");
        FIELD_CHANNEL_MAPPING.put("email", "邮箱地址");
        FIELD_CHANNEL_MAPPING.put("contact", "商务联系人");
        FIELD_CHANNEL_MAPPING.put("phone", "联系电话");
        FIELD_CHANNEL_MAPPING.put("country", "国家");
        FIELD_CHANNEL_MAPPING.put("province", "省市地区");
        FIELD_CHANNEL_MAPPING.put("address", "详细地址");
        FIELD_CHANNEL_MAPPING.put("2", "商务信息");
        FIELD_CHANNEL_MAPPING.put("level", "商户等级");
        FIELD_CHANNEL_MAPPING.put("merchantTag", "商户类型");
        FIELD_CHANNEL_MAPPING.put("terraceRate", "平台费率（％）");
        FIELD_CHANNEL_MAPPING.put("computingStandard", "计算标准");
        FIELD_CHANNEL_MAPPING.put("paymentCycle", "缴纳周期");
        FIELD_CHANNEL_MAPPING.put("currency", "结算币种");
        FIELD_CHANNEL_MAPPING.put("vipAmount", "会员费(CNY)");
        FIELD_CHANNEL_MAPPING.put("commerce", "PA商务");
        FIELD_CHANNEL_MAPPING.put("startTime&endTime", "商户有效期");
        FIELD_CHANNEL_MAPPING.put("3", "技术参数");
        FIELD_CHANNEL_MAPPING.put("childConnectMode", "商户钱包类型");
        FIELD_CHANNEL_MAPPING.put("whiteIp", "ip白名单");
        FIELD_CHANNEL_MAPPING.put("pcDomain", "PA PC 域名");
        FIELD_CHANNEL_MAPPING.put("h5Domain", "PA H5 域名");
        FIELD_CHANNEL_MAPPING.put("appDomain", "PA App 域名");
        FIELD_CHANNEL_MAPPING.put("videoDomain", "PA 视频域名");

        FIELD_MERCHNTLEVEL_MAPPING.put("code","商户等级");
        FIELD_MERCHNTLEVEL_MAPPING.put("startValue","月货量（万） 最少值");
        FIELD_MERCHNTLEVEL_MAPPING.put("endValue","月货量（万） 最大值");
        FIELD_MERCHNTLEVEL_MAPPING.put("remark","备注");

        FIELD_MERCHNTRATE_MAPPING.put("computingStandard","计算标准");
        FIELD_MERCHNTRATE_MAPPING.put("rangeAmountBegin&rangeAmountEnd","金额范围");
        FIELD_MERCHNTRATE_MAPPING.put("terraceRate","平台费率");
        FIELD_MERCHNTRATE_MAPPING.put("paymentCycle","缴纳周期");
        FIELD_MERCHNTRATE_MAPPING.put("remarks","备注");

        FIELD_ACTIVITY_MAPPING.put("content","维护公告内容");
        FIELD_ACTIVITY_MAPPING.put("title","维护公告标题");
        FIELD_ACTIVITY_MAPPING.put("maintainStatus","维护开关");
        FIELD_ACTIVITY_MAPPING.put("h5MaintainUrl","H5图片地址");
        FIELD_ACTIVITY_MAPPING.put("pcMaintainUrl"," pc图片地址");

        FIELD_ACTIVE_MAPPING.put("h5Url","H5 活动入口图片");
        FIELD_ACTIVE_MAPPING.put("pcUrl","PC 活动入口图片");
        FIELD_ACTIVE_MAPPING.put("activityMerchants","活动参与商户");
        FIELD_ACTIVE_MAPPING.put("activityType","活动类型配置");


        FIELD_OPERATION_MAPPING.put("tType"," 类型*");
        FIELD_OPERATION_MAPPING.put("merchantList","发布范围*：");
        FIELD_OPERATION_MAPPING.put("merchantList","全部商户/ 部分商户");
        FIELD_OPERATION_MAPPING.put("beginTime&endTime","生效时间：(24小时制)*");
        FIELD_OPERATION_MAPPING.put("bannerName","文案名称 *");
        FIELD_OPERATION_MAPPING.put("hostUrl","域名地址");
        FIELD_OPERATION_MAPPING.put("orderNum","排列顺序");
        FIELD_OPERATION_MAPPING.put("testUser","预览账号");
        FIELD_OPERATION_MAPPING.put("status","状态 *");
        FIELD_OPERATION_MAPPING.put("langType","语言类型 *");
        FIELD_OPERATION_MAPPING.put("imgName","图片名称 *");

        FIELD_ARTICLE_MAPPING.put("sportId","比赛类型");
        FIELD_ARTICLE_MAPPING.put("leagueName","关联赛事");
        FIELD_ARTICLE_MAPPING.put("categoryName","栏目");
        FIELD_ARTICLE_MAPPING.put("authorName","作者");
        FIELD_ARTICLE_MAPPING.put("thumbnails","封面图");
        FIELD_ARTICLE_MAPPING.put("articleTittle","标题");
        FIELD_ARTICLE_MAPPING.put("summary","摘要");
        FIELD_ARTICLE_MAPPING.put("isShow","显示状态");
        FIELD_ARTICLE_MAPPING.put("isTop","是否置顶");
        FIELD_ARTICLE_MAPPING.put("topStartTime","置顶上线时间");
        FIELD_ARTICLE_MAPPING.put("topEndTime","置顶过期时间");
        FIELD_ARTICLE_MAPPING.put("onlineTime","上线时间");
        FIELD_ARTICLE_MAPPING.put("factor","权重");
        FIELD_ARTICLE_MAPPING.put("permanentTop","永久置顶 是/否");
        FIELD_ARTICLE_MAPPING.put("tagName","标签");
        FIELD_ARTICLE_MAPPING.put("articleContent","正文");


        filterOpertationAddNames.add("tType");
        filterOpertationAddNames.add("merchantList");
        filterOpertationAddNames.add("merchantList");
        filterOpertationAddNames.add("beginTime&endTime");
        filterOpertationAddNames.add("bannerName");
        filterOpertationAddNames.add("hostUrl");
        filterOpertationAddNames.add("orderNum");
        filterOpertationAddNames.add("testUser");
        filterOpertationAddNames.add("status");
        filterOpertationAddNames.add("langType");
        filterOpertationAddNames.add("imgName");

        filterActiveFieldAddNames.add("h5Url");
        filterActiveFieldAddNames.add("pcUrl");
        filterActiveFieldAddNames.add("activityMerchants");
        filterActiveFieldAddNames.add("activityType");


        filterFieldAddNames.add("1");
        filterFieldAddNames.add("merchantName");
        filterFieldAddNames.add("logo");
        filterFieldAddNames.add("email");
        filterFieldAddNames.add("contact");
        filterFieldAddNames.add("phone");
        filterFieldAddNames.add("country");
        filterFieldAddNames.add("province");
        filterFieldAddNames.add("address");
        filterFieldAddNames.add("2");
        filterFieldAddNames.add("level");
        filterFieldAddNames.add("merchantTag");
        filterFieldAddNames.add("terraceRate");
        filterFieldAddNames.add("computingStandard");
        filterFieldAddNames.add("paymentCycle");
        filterFieldAddNames.add("currency");
        filterFieldAddNames.add("vipAmount");
        filterFieldAddNames.add("commerce");
        filterFieldAddNames.add("startTime&endTime");
        filterFieldAddNames.add("3");
        filterFieldAddNames.add("childConnectMode");
        filterFieldAddNames.add("whiteIp");
        filterFieldAddNames.add("pcDomain");
        filterFieldAddNames.add("h5Domain");
        filterFieldAddNames.add("appDomain");
        filterFieldAddNames.add("videoDomain");

        filterFieldNames.add("logoEnable");
        filterFieldNames.add("profesTag");
        filterFieldNames.add("userProfixDigits");
        filterFieldNames.add("maxSeriesNum");
        filterFieldNames.add("minSeriesNum");
        filterFieldNames.add("title");
        filterFieldNames.add("darkLogoUrl");
        filterFieldNames.add("pcLogoUrl");
        filterFieldNames.add("compatLogoUrl");
        filterFieldNames.add("loadLogoUrl");
        filterFieldNames.add("leagueLogoUrl");
        filterFieldNames.add("videoLogoUrl");
        filterFieldNames.add("pcLogoUrl");
        filterFieldNames.add("bannerUrl");
        filterFieldNames.add("marketDefault");
        filterFieldNames.add("h5Default");
        filterFieldNames.add("appDefault");

        filterField.add("code");
        filterField.add("startValue");
        filterField.add("endValue");
        filterField.add("remark");

        filterTrateField.add("computingStandard");
        filterTrateField.add("rangeAmountBegin&rangeAmountEnd");
        filterTrateField.add("terraceRate");
        filterTrateField.add("paymentCycle");
        filterTrateField.add("remarks");

        filterActivityField.add("content");
        filterActivityField.add("title");
        filterActivityField.add("maintainStatus");
        filterActivityField.add("h5MaintainUrl");
        filterActivityField.add("pcMaintainUrl");

        filterArticelAddNames.add("sportId");
        filterArticelAddNames.add("leagueName");
        filterArticelAddNames.add("categoryName");
        filterArticelAddNames.add("authorName");
        filterArticelAddNames.add("thumbnails");
        filterArticelAddNames.add("articleTittle");
        filterArticelAddNames.add("summary");
        filterArticelAddNames.add("isShow");
        filterArticelAddNames.add("isTop");
        filterArticelAddNames.add("topStartTime");
        filterArticelAddNames.add("topEndTime");
        filterArticelAddNames.add("onlineTime");
        filterArticelAddNames.add("factor");
        filterArticelAddNames.add("permanentTop");
        filterArticelAddNames.add("tagName");
        filterArticelAddNames.add("articleContent");

    }
    /************************获取字段类信息***********************************************/
    /**
     * @dercription 获取一个类的成员属性名称列表
     * @return List<String>
     * */
    public static List<String> getClassFieldList(Class<?> object,Boolean ignoreSerialVersionUID) {

        //设置忽略序列化Id默认值
        if(null==ignoreSerialVersionUID){
            ignoreSerialVersionUID=true;
        }
        //推断类型
        List<String> objectFiledList = new ArrayList<>();

        //获取类信息
        Class<?> classInfo;
        try {
            //加载类信息
            classInfo = Class.forName(object.getName());
            //使用反射获取该类的成员属性
            Field[] fieldList = classInfo.getDeclaredFields();

            for (int i = 0; i < fieldList.length; i++) {
                //忽略获取序列化ID
                if(ignoreSerialVersionUID){
                    //如果字段是序列化字段，则忽略
                    if (!fieldList[i].getName().equals("serialVersionUID")) {
                        objectFiledList.add(fieldList[i].getName());
                    }
                }else{
                    //序列化ID不忽略作为类的属性返回到结果中
                    objectFiledList.add(fieldList[i].getName());
                }
            }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return objectFiledList;
    }

    public static  String getModeInfo(String agentLevel,String language){
        String modeInfo="";
        if(!StringUtils.isEmpty(agentLevel)) {
            switch (agentLevel) {
                case "0":
                    modeInfo = Objects.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED, language) ? MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_19.getRemark() : MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_19.getEn();
                    break;
                case "1":
                    modeInfo = Objects.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED, language) ? MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_17.getRemark() : MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_17.getEn();
                    break;
                case "2":
                    modeInfo = Objects.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED, language) ? MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_18.getRemark() : MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_18.getEn();
                    break;
                case "10":
                    modeInfo = Objects.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED, language) ? MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_20.getRemark() : MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_20.getEn();
                    break;
            }
        }
        return modeInfo;
    }

    public static  String getModeEndInfo(String agentLevel,String language){
        String modeEndInfo="";
        switch (agentLevel) {
            case "0" :
                /*新建代理商户*/
                modeEndInfo= Objects.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED, language)? MerchantLogTypeEnum.NEW_CREATE_MERCHANT.getRemark(): MerchantLogTypeEnum.NEW_CREATE_MERCHANT.getRemarkEn();
                break;
            case "1" :
                modeEndInfo= Objects.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED, language)? MerchantLogTypeEnum.NEW_CREATE_CHANNEL_MERCHANT.getRemark(): MerchantLogTypeEnum.NEW_CREATE_CHANNEL_MERCHANT.getRemarkEn();
                break;
            case "2" :
                modeEndInfo= Objects.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED, language)?MerchantLogTypeEnum.NEW_LEVEL_TOW_MERCHANT.getRemark(): MerchantLogTypeEnum.NEW_LEVEL_TOW_MERCHANT.getRemarkEn();
                break;
            case "10" :
                modeEndInfo= Objects.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED, language)?MerchantLogTypeEnum.NEW_AGENT_TOW_MERCHANT.getRemark(): MerchantLogTypeEnum.NEW_AGENT_TOW_MERCHANT.getRemarkEn();
                break;
        }
        return modeEndInfo;
    }


    public static  String getTypeName(String tType){
        String modeEndInfo="";
        switch (tType) {
            case "1" :
                /*新建代理商户*/
                modeEndInfo="H5端首页banner";
                break;
            case "2" :
                modeEndInfo= "H5端列表banner";
                break;
            case "3" :
                modeEndInfo= "H5端首页弹窗";
                break;
            case "4" :
                modeEndInfo="H5端浮层标";
                break;
            case "5" :
                modeEndInfo="PC首页弹窗";
                break;
            case "6" :
                modeEndInfo="PC首页banner";
                break;
            case "7" :
                modeEndInfo="PC专题页banner";
                break;
            case "8" :
                modeEndInfo="H5专题页banner";
                break;
        }
        return modeEndInfo;
    }


    public static  String getSportName(String tType){
        String sportName="";
        switch (tType) {
            case "1" :
                sportName= SportEnum.SPORT_FOOTBALL.getValue();
                break;
            case "2" :
                sportName= SportEnum.SPORT_BASKETBALL.getValue();
                break;
            case "3" :
                sportName= SportEnum.SPORT_BASEBALL.getValue();
                break;
            case "4" :
                sportName=SportEnum.SPORT_ICEHOCKEY.getValue();
                break;
            case "5" :
                sportName=SportEnum.SPORT_TENNIS.getValue();
                break;
            case "6" :
                sportName=SportEnum.SPORT_USEFOOTBALL.getValue();
                break;
            case "7" :
                sportName=SportEnum.SPORT_SNOOKER.getValue();
                break;
            case "8" :
                sportName=SportEnum.SPORT_PINGPONG.getValue();
                break;
            case "9" :
                sportName=SportEnum.SPORT_VOLLEYBALL.getValue();
                break;
            case "10" :
                sportName=SportEnum.SPORT_BADMINTON.getValue();
                break;
            case "11" :
                sportName=SportEnum.SPORT_HANDBALL.getValue();
                break;
            case "12" :
                sportName=SportEnum.SPORT_BOXING.getValue();
                break;
            case "13" :
                sportName=SportEnum.SPORT_BEACH_VOLLEYBALL.getValue();
                break;
            case "14" :
                sportName=SportEnum.SPORT_ENGLAND_RUGBY_BALL.getValue();
                break;
            case "15" :
                sportName=SportEnum.SPORT_HOCKEY_BALL.getValue();
                break;
            case "16" :
                sportName=SportEnum.SPORT_WATER_POLO.getValue();
                break;
            case "1001" :
                sportName=SportEnum.VIRTUAL_SPORT_FOOTBALL.getValue();
                break;
            case "1004" :
                sportName=SportEnum.VIRTUAL_SPORT_BASKETBALL.getValue();
                break;
            case "101" :
                sportName=SportEnum.SPORT_DOTA2.getValue();
                break;
        }
        return sportName;
    }

    public MerchantLogFiledVO compareObject(Object oldBean, Object newBean,List<String> filterFields,Map<String,String> fieldMap) {
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        T pojo2 = (T) newBean;
        try {
            List<String> beforeValues = new ArrayList<>();
            List<String> afterValues = new ArrayList<>();
            List<String> fieldNames = new ArrayList<>();

            Class clazz1 = pojo2.getClass();
            Field[] fields = pojo2.getClass().getDeclaredFields();
            if(null==oldBean){//新增
                for (int i=0 ;i< filterFields.size();i++) {
                    if("1".equals(filterFields.get(i))||"2".equals(filterFields.get(i))||"3".equals(filterFields.get(i))){
                        fieldNames.add(fieldMap.get(filterFields.get(i)));
                        beforeValues.add("");
                        afterValues.add("");
                    }else{
                        StringBuffer bffer = new StringBuffer();
                        if(filterFields.get(i).contains("&")){
                            List<String> timeAble = Arrays.asList(filterFields.get(i).split("&"));
                            for(int j =0 ;j< timeAble.size(); j++){
                                PropertyDescriptor pd1 = new PropertyDescriptor(timeAble.get(j), clazz1);
                                Method getMethod1 = pd1.getReadMethod();
                                Object o2 = getMethod1.invoke(pojo2);
                                if(j== timeAble.size()-1){
                                    bffer.append(ObjectUtils.isEmpty(o2)?"-":o2.toString());
                                }else{
                                    bffer.append(ObjectUtils.isEmpty(o2)?"-":o2.toString()).append("->");
                                }
                            }
                            fieldNames.add(fieldMap.get(filterFields.get(i)));
                            beforeValues.add("-");
                            afterValues.add(bffer.toString());
                        }else{
                            PropertyDescriptor pd1 = new PropertyDescriptor(filterFields.get(i), clazz1);
                            Method getMethod1 = pd1.getReadMethod();
                            Object o2 = getMethod1.invoke(pojo2);
                            fieldNames.add(fieldMap.get(filterFields.get(i)));
                            beforeValues.add("-");
                            if("tType".equals(fields[i].getName())){
                                afterValues.add(getTypeName((ObjectUtils.isEmpty(o2)? "-":o2.toString())));
                            }else{
                                afterValues.add(ObjectUtils.isEmpty(o2)?"-":o2.toString());
                            }
                        }
                    }
                }
            }else{//编辑
                T pojo1 = (T) oldBean;
                Class clazz = pojo1.getClass();
                for (int i=0 ;i< fields.length;i++) {
                    if (filterFields.contains(fields[i].getName())) {
                        PropertyDescriptor pd = new PropertyDescriptor(fields[i].getName(), clazz);
                        PropertyDescriptor pd1 = new PropertyDescriptor(fields[i].getName(), clazz1);
                        Method getMethod = pd.getReadMethod();
                        Method getMethod1 = pd1.getReadMethod();
                        Object o1 = getMethod.invoke(pojo1);
                        Object o2 = getMethod1.invoke(pojo2);
                        String old = o1==null?"-":o1.toString();
                        String newValue = o2==null?"-":o2.toString();
                        if(!old.equals(newValue)){
                            fieldNames.add(fieldMap.get(fields[i].getName()));
                            if("sportId".equals(fields[i].getName())) {
                                beforeValues.add(getSportName(old));
                                afterValues.add(getSportName(ObjectUtils.isEmpty(o2)? "-":o2.toString()));
                            }else if("tType".equals(fields[i].getName())){
                                beforeValues.add(getTypeName(old));
                                afterValues.add(getTypeName((ObjectUtils.isEmpty(o2)? "-":o2.toString())));
                            }else {
                                beforeValues.add(old);
                                afterValues.add(newValue);
                            }
                        }
                    }
                }
            }

            vo.setFieldName(fieldNames);
            vo.setBeforeValues(beforeValues);
            vo.setAfterValues(afterValues);

        } catch (Exception e) {
            log.error("字段对比异常！", e);
        }
        log.info("最后结果 ： "+ JSON.toJSONString(vo));
        return vo;
    }

}
