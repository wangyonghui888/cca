package com.panda.sport.merchant.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.panda.sport.merchant.common.enums.TaskConditionEnum;
import com.panda.sport.merchant.common.vo.AcTaskParamVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j
public class JosnToUtil {
    private static final TreeMap<Integer,Integer>  dMap = new TreeMap<>();
    private static final TreeMap<Integer,Integer>  dsMap = new TreeMap<>();
    static{
        //key是条件，value是顺序
        dMap.put(2,1);
        dMap.put(1,2);
        dMap.put(10,3);
        dMap.put(4,4);
        dMap.put(3,5);
        dMap.put(5,6);
        dMap.put(8,7);
        dMap.put(9,8);
        dMap.put(7,9);
        dMap.put(6,10);
        TreeMap<Integer,Integer> dd1 = new TreeMap<>();
        //key是顺序，value是条件
        dsMap.put(1,2);
        dsMap.put(2,1);
        dsMap.put(3,10);
        dsMap.put(4,4);
        dsMap.put(5,3);
        dsMap.put(6,5);
        dsMap.put(7,8);
        dsMap.put(8,9);
        dsMap.put(9,7);
        dsMap.put(10,6);
    }



    public static Map<String,Object> get (String taskCondition, Long nowL, Long startL, Long endL) {
     StringBuffer buff = new StringBuffer();
        JSONObject obj = JSON.parseObject(taskCondition);
            List<Integer> playList = null;
            List<Long> sportList = null;
            List<String> listIds = new ArrayList();
            List<AcTaskParamVO> acTaskParamList = new ArrayList<AcTaskParamVO>();
            for(String str1: obj.keySet()) {
                JSONObject jsonObject1 = ((JSONObject) obj.get(str1));
                jsonObject1.put("conditionId", str1);
                if (null != jsonObject1.getJSONObject("playInfoList")) {
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("playInfoList");
                    if (jsonObject2 != null) {
                        playList = new ArrayList();
                        for (String str : jsonObject2.keySet()) {
                            List<Integer> playListTemp = (List<Integer>) jsonObject2.get(str);
                            playList.addAll(playListTemp);
                        }
                    }
                }
                if (null != jsonObject1.get("matchInfoList")) {
                    String sports = (String) jsonObject1.get("matchInfoList");
                    if (StringUtils.isNotEmpty(sports)) {
                        sportList = Arrays.asList(sports.split(",")).stream().map(e -> Long.valueOf(e)).collect(Collectors.toList());
                    }
                }
                buff.append(getRemarkValue(str1)).append("【").append(getSymbol((Integer) jsonObject1.get("symbol")));
                if (StringUtils.isNotEmpty(null != jsonObject1.get("beforeValue") ? jsonObject1.get("beforeValue").toString() : null)){
                    buff.append(jsonObject1.get("beforeValue").toString());
                    getEndConcat(buff, false);
                }
                if (StringUtils.isNotEmpty(null != jsonObject1.get("afterValue") ? jsonObject1.get("afterValue").toString() : null)){
                    buff.append("同").append(jsonObject1.get("afterValue").toString()).append("之间");
                  getEndConcat(buff, false);
                 }
                if(StringUtils.isNotEmpty(null!=jsonObject1.get("startTime")? jsonObject1.get("startTime").toString():null)){
                    buff.append("时间区间开始时间:").append(jsonObject1.get("startTime").toString());
                    getEndConcat(buff,false);
                }
                if(StringUtils.isNotEmpty(null!=jsonObject1.get("endTime")? jsonObject1.get("endTime").toString():null)){
                    buff.append("时间区间结束时间:").append(jsonObject1.get("endTime").toString());
                    getEndConcat(buff,false);
                }
                if(StringUtils.isNotEmpty(null!=jsonObject1.get("playInfoList")? jsonObject1.get("playInfoList").toString():null)){
                    buff.append("玩法选择:").append(jsonObject1.get("playInfoList").toString());
                    getEndConcat(buff,false);
                }
                if(StringUtils.isNotEmpty(null!=jsonObject1.get("matchInfoList")? jsonObject1.get("matchInfoList").toString():null)){
                    buff.append("虚拟球种:").append(jsonObject1.get("matchInfoList").toString());
                    getEndConcat(buff,false);
                }
                getEndConcat(buff,true);
                AcTaskParamVO acTaskParamVO = JSONObject.parseObject(jsonObject1.toJSONString(), new TypeReference<AcTaskParamVO>() {});
                acTaskParamVO.setPlayInfoLists(playList);
                acTaskParamVO.setMatchInfoLists(sportList);
                //处理结束时间大于开始时间30天，就只查35天的数据
                try {
                    startEndQueryTime(acTaskParamVO,nowL, startL, endL);
                }catch (Exception e){
                   throw new RuntimeException(e);
                }
                acTaskParamList.add(acTaskParamVO);
                listIds.add(str1);
        }

        Map<String,Object> mapInfo = new HashMap<>();
        mapInfo.put("remark",buff.toString());
        mapInfo.put("condition",obj);
        mapInfo.put("acTaskParamList",acTaskParamList);
        mapInfo.put("conditionIdList",listIds);
        mapInfo.put("playInfoList",playList);
        mapInfo.put("matchInfoList",sportList);

        return mapInfo;
    }

    private static void getEndConcat(StringBuffer buff, Boolean isEnd) {
        if(isEnd) {
            buff.append("】");
        }else{
            buff.append(";");
        }
    }

    private static void
    startEndQueryTime(AcTaskParamVO acTaskParamVO,Long nowL, Long startL, Long endL) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        if(StringUtils.isNotEmpty(acTaskParamVO.getStartTime()) && StringUtils.isNotEmpty(acTaskParamVO.getEndTime())){
            Date startDate =DateUtils.parseDate(acTaskParamVO.getStartTime(), "yyyy-MM-dd"),
                    endDate = DateUtils.parseDate(acTaskParamVO.getEndTime(), "yyyy-MM-dd");
            long pEndTimeL = endDate.getTime();
            long pStartTimeL = startDate.getTime();
            long dateDiff = 1000 * 60 * 60 * 24 * 35l;
           /* if (pEndTimeL - pStartTimeL >= dateDiff) {
                log.error("时间区间已经超过35天：startDate={},endDate={}",startDate,endDate);
                throw new RuntimeException("时间区间已经超过35天：startDate="+startDate + "endDate=" +endDate);
            }*/
        }
        if(6 == acTaskParamVO.getConditionId() || 7 == acTaskParamVO.getConditionId()){
            Integer beDays = acTaskParamVO.getBeforeValue();
            Integer enDays = acTaskParamVO.getAfterValue();
            ParseDateReSult(acTaskParamVO, now, sdf, beDays,enDays,nowL,startL,endL);
        }
    }

    private static void ParseDateReSult(AcTaskParamVO acTaskParamVO, Date now, SimpleDateFormat sdf, Integer beDays,Integer enDays,Long nowL, Long startL, Long endL) throws ParseException {
        if(null != beDays ) {
            Date okBeDate = DateUtils.addDays(now, -beDays);
            String irDate=sdf.format(okBeDate);
            String stringDate = irDate + " 00:00:00";
            String stringEndTime = irDate + " 23:59:59";
            Date parseDate = DateUtils.parseDate(stringDate, "yyyy-MM-dd HH:mm:ss");
            Date parseEndTime = DateUtils.parseDate(stringEndTime, "yyyy-MM-dd HH:mm:ss");
            long beforeTime = parseDate.getTime();
            long afterTime;
            long time = 999l;
            afterTime = parseEndTime.getTime() + time;
            switch (acTaskParamVO.getSymbol()) {
                case 1:
                case 2:
                    if(6 == acTaskParamVO.getConditionId()){
                        acTaskParamVO.setBeforeTime(beforeTime);
                     }else{
                        acTaskParamVO.setBeforeDate(irDate);
                    }
                    break;
                case 3:
                case 4:
                    if(6 == acTaskParamVO.getConditionId()) {
                        afterTime = parseEndTime.getTime() + time;
                        acTaskParamVO.setAfterTime(afterTime);
                    }else{
                        acTaskParamVO.setBeforeDate(irDate);
                    }
                    break;
                case 5:
                    if(6 == acTaskParamVO.getConditionId()) {
                        acTaskParamVO.setBeforeTime(beforeTime);
                        acTaskParamVO.setAfterTime(afterTime);
                    }else{
                        acTaskParamVO.setBeforeDate(irDate);
                    }
                    break;
                case 6:
                    if(6 == acTaskParamVO.getConditionId()) {
                        Date okAfterBeDate = DateUtils.addDays(now, -enDays);
                        String afterEndTime = sdf.format(okAfterBeDate) + " 23:59:59";
                        Date parseAfterEndTime = DateUtils.parseDate(afterEndTime, "yyyy-MM-dd HH:mm:ss");
                        afterTime = parseAfterEndTime.getTime() + time;
                        acTaskParamVO.setBeforeTime(beforeTime);
                        acTaskParamVO.setAfterTime(afterTime);
                    }else{
                        Date okAfterDate = DateUtils.addDays(now, -enDays);
                        String afterEndDate = sdf.format(okAfterDate);
                        acTaskParamVO.setBeforeDate(irDate);
                        acTaskParamVO.setAfterDate(afterEndDate);
                    }
                    break;
            }
        }
    }


    public static String getRemarkValue(String str1){
        String remark="";
        switch (str1){
            case "1":
                remark = TaskConditionEnum.TASK_CONDITION1.getLabel();
                break;
            case "2" :
                remark = TaskConditionEnum.TASK_CONDITION2.getLabel();
                break;
            case "3" :
                remark = TaskConditionEnum.TASK_CONDITION3.getLabel();
                break;
            case "4" :
                remark = TaskConditionEnum.TASK_CONDITION4.getLabel();
                break;
            case "5" :
                remark = TaskConditionEnum.TASK_CONDITION5.getLabel();
                break;
            case "6" :
                remark = TaskConditionEnum.TASK_CONDITION6.getLabel();
                break;
            case "7" :
                remark = TaskConditionEnum.TASK_CONDITION7.getLabel();
                break;
            case "8" :
                remark = TaskConditionEnum.TASK_CONDITION8.getLabel();
                break;
            case "9" :
                remark = TaskConditionEnum.TASK_CONDITION9.getLabel();
                break;
        }
        return remark;
    }


    public static String getSymbol(Integer str1){
        String symbol="";
        switch (str1){
            case 1:
                symbol = TaskConditionEnum.TASK_SYMBOL1.getLabel();
                break;
            case 2 :
                symbol = TaskConditionEnum.TASK_SYMBOL2.getLabel();
                break;
            case 3 :
                symbol = TaskConditionEnum.TASK_SYMBOL3.getLabel();
                break;
            case 4 :
                symbol = TaskConditionEnum.TASK_SYMBOL4.getLabel();
                break;
            case 5 :
                symbol = TaskConditionEnum.TASK_SYMBOL5.getLabel();
                break;
            case 6 :
                symbol = TaskConditionEnum.TASK_SYMBOL6.getLabel();
                break;
        }
        return symbol;
    }

    public static boolean isJSON2(String str) {
        boolean result = false;
        try {
            Object obj=JSONObject.parseObject(str);
            result = true;
        } catch (Exception e) {
            result=false;
        }
        return result;
    }
    public static List<AcTaskParamVO>  getSortInfo(List<AcTaskParamVO> pvos,List<String> listIds){
        List<AcTaskParamVO> sortVos = new ArrayList<>();
        List<Integer> newList= new ArrayList<>();
        for(String ids : listIds) {
            newList.add(dMap.get(Integer.valueOf(ids)));
        }
        Collections.sort(newList);
        for(Integer e :newList){
           int sortId = dsMap.get(e);
           A:for(AcTaskParamVO vo: pvos){
               if(sortId==vo.getConditionId()){
                   sortVos.add(vo);
                   break A;
               }
            }
        }
        return sortVos;
    }

}
