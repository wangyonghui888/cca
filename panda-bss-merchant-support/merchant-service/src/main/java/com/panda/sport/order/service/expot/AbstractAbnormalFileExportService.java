package com.panda.sport.order.service.expot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.AbnormalLimitEnum;
import com.panda.sport.merchant.common.enums.UserLimitEnum;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.utils.FtpUtil;
import com.panda.sport.merchant.common.utils.ZipFilesUtils;
import com.panda.sport.merchant.common.vo.merchant.AbnormalInfoDto;
import com.panda.sport.merchant.common.vo.merchant.AbnormalUserInfoDto;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import com.panda.sport.order.service.MerchantFileService;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

@Slf4j
public abstract class AbstractAbnormalFileExportService implements OrderFileExportService {

    @Autowired
    private MerchantFileService merchantFileService;

    @Autowired
    private LocalCacheService localCacheService;

    @Autowired
    protected FtpProperties ftpProperties;


    public void updateRate(Long fileId, Long rate) {
        merchantFileService.updateFileRate(fileId, rate);
    }

    public void updateFileStart(Long fileId) {
        merchantFileService.updateFileStatus(fileId);
    }

    public void updateFileStatus(Long fileId, Integer size) {
        merchantFileService.updateFileSizeAndStatus(fileId, size);
    }

    public boolean checkTask(Long id) {
        if (merchantFileService.loadById(id) == null) {
            return true;
        }
        return false;
    }

    public void updateFileStatusEnd(Long fileId) {
        merchantFileService.updateFileEnd(fileId);
    }

    public void exportFail(Long fileId, String remark) {
        merchantFileService.updateFileFail(fileId, remark);
    }

    public void uploadFile(MerchantFile merchantFile, InputStream inputStream) throws IOException {
        InputStream zip = null;
        try {
            zip = ZipFilesUtils.zip(inputStream, merchantFile.getFileName());
            updateFileStatus(merchantFile.getId(), zip.available());
            // ftp上传
            FtpUtil.uploadFile(ftpProperties.getHost(), Integer.parseInt(ftpProperties.getPort()), ftpProperties.getUsername(), ftpProperties.getPassword(), "", merchantFile.getFilePath(), merchantFile.getFtpFileName(), zip);

            updateFileStatusEnd(merchantFile.getId());
        } catch (Exception e) {
            log.error("导出上传异常！", e);
            throw e;
        } finally {
            if (zip != null) {
                zip.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }


    public void uploadFile(MerchantFile merchantFile, InputStream inputStream, int j) throws IOException {
        InputStream zip = null;
        try {
            String[] fileNames = merchantFile.getFileName().split(",");
            zip = ZipFilesUtils.zip(inputStream, fileNames[j]);
            updateFileStatus(merchantFile.getId(), zip.available() * (j + 1));
            // ftp上传
            String[] ftpFileNames = merchantFile.getFtpFileName().split(",");
            FtpUtil.uploadFile(ftpProperties.getHost(), Integer.parseInt(ftpProperties.getPort()), ftpProperties.getUsername(), ftpProperties.getPassword(), "", merchantFile.getFilePath(), ftpFileNames[j], zip);

            updateFileStatusEnd(merchantFile.getId());
        } catch (Exception e) {
            log.error("导出上传异常！", e);
            throw e;
        } finally {
            if (zip != null) {
                zip.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 异常会员名单导出报表格式
     *
     * @param resultList
     * @param language
     * @return
     */
    protected byte[] exportMatchToCsv(List<?> resultList, String language,Integer type) {
        if (StringUtils.isEmpty(language)){
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        List<LinkedHashMap<String, Object>> exportData =
                new ArrayList<>(CollectionUtils.isEmpty(resultList) ? 0 : resultList.size());
        ObjectMapper mapper = new ObjectMapper();
        List<AbnormalInfoDto> filterList = mapper.convertValue(resultList, new TypeReference<List<AbnormalInfoDto>>() {
        });
        int i = 0;
        for (AbnormalInfoDto order : filterList) {
            i = i + 1;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", i);
            rowData.put("2", order.getUserId()+"\t");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            rowData.put("3", order.getUserName());
            if(0==type) {//对内
                rowData.put("4",order.getMerchantCode()+"\t");
                rowData.put("5", sdf.format(order.getOperateTime()));
                rowData.put("6", order.getRiskType());
                rowData.put("7", StringUtils.isNotEmpty(order.getRemark())? order.getRemark():"");
            }else{
                rowData.put("4", sdf.format(order.getOperateTime()));
                rowData.put("5", order.getRiskType());
                rowData.put("6", StringUtils.isNotEmpty(order.getRemark())? order.getRemark():"");
            }

            exportData.add(rowData);
        }
        return CsvUtil.exportCSV(getheader(language,type), exportData);
    }


    /**
     * 异常用户导出报表格式
     *
     * @param resultList
     * @param language
     * @return
     */
    protected byte[] exportAbnormalUserMatchToCsv(List<?> resultList, String language,Integer type) {
        if (StringUtils.isEmpty(language)){
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        List<LinkedHashMap<String, Object>> exportData =
                new ArrayList<>(CollectionUtils.isEmpty(resultList) ? 0 : resultList.size());
        ObjectMapper mapper = new ObjectMapper();
        List<AbnormalUserInfoDto> filterList = mapper.convertValue(resultList, new TypeReference<List<AbnormalUserInfoDto>>() {
        });
        Map<Integer, String> sportMap = localCacheService.getSportMap(language);
        int i = 0;
        for (AbnormalUserInfoDto order : filterList) {
            i = i + 1;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            //序列
            rowData.put("1", i);
            //用户id
            rowData.put("2", order.getUid()+"\t");
            //用户名
            rowData.put("3", order.getUserName());
            //所属商户
            rowData.put("4",order.getMerchantCode()+"\t");
            //日期
            rowData.put("5", order.getCrtTime() +"\t");
            String typeString = order.getType();
            StringBuffer bff = new StringBuffer();
            String content = order.getUpdateContent();
           if(StringUtil.isNotEmpty(content)){
               JSONArray jsonArray = JSONArray.parseArray(content);
               if(null!=jsonArray && jsonArray.size() > 0){
                   getStringInfo(jsonArray.getJSONObject(0),bff,sportMap,typeString,language);
                  }
           }

            //风控措施
            rowData.put("6", bff.toString());
            exportData.add(rowData);
        }
        return CsvUtil.exportCSV(getUserheader(language,type), exportData);
    }

    private LinkedHashMap<String, String> getheader(String language,Integer type) {
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        if (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
            header.put("1", "序号");
            header.put("2", "用户ID");
            header.put("3", "用户名");
            if (0 == type) {//对内
                header.put("4", "商户编码");
                header.put("5", "操作时间");
                header.put("6", "异常类型");
                header.put("7", "备注");
            } else {
                header.put("4", "操作时间");
                header.put("5", "异常类型");
                header.put("6", "备注");
            }
        } else {
            header.put("1", "NO");
            header.put("2", "UserId");
            header.put("3", "UserName");
            if (0 == type) {//对内
                header.put("4", "merchantCode");
                header.put("5", "OperatingTime");
                header.put("6", "UserType");
                header.put("7", "Remark");
            } else {
                header.put("4", "OperatingTime");
                header.put("5", "UserType");
                header.put("6", "Remark");
            }
        }
        return header;
    }
    private StringBuffer getStringInfo(JSONObject jsonObject,StringBuffer bff, Map<Integer, String> sportMap,String typeString,String language){
        String oldData=null;
        String data = null;
        String name = jsonObject.getString("name");
        if(StringUtils.isNotEmpty(typeString) && (typeString.equals("2") || typeString.equals("11"))) {//特殊延时  11:特殊延时时间
            if(typeString.equals("11")){
                oldData = jsonObject.getString("oldData") + "s";
                data = jsonObject.getString("data") + "s";
            } else{ // 限额百分比
                oldData = StringUtils.isNotEmpty(jsonObject.getString("oldData")) ? jsonObject.getString("oldData"):"";
                data = StringUtils.isNotEmpty(jsonObject.getString("data")) ? jsonObject.getString("data"):"";
            }

        }else if(StringUtils.isNotEmpty(typeString) && (typeString.equals("3") || typeString.equals("20") ||  typeString.equals("21") ||  typeString.equals("22")
                ||  typeString.equals("14") ||  typeString.equals("17") ||  typeString.equals("18"))){//特殊限额
            if(typeString.equals("17")){
                oldData= dataInfo(jsonObject,"oldData",language);
                data = dataInfo(jsonObject,"data",language);

            }else if(typeString.equals("14")) {
                oldData= StringUtils.isNotEmpty(jsonObject.getString("oldData")) ? jsonObject.getString("oldData") + "%":"";
                data = StringUtils.isNotEmpty(jsonObject.getString("data")) ? jsonObject.getString("data") + "%": "";
            }else{
                oldData = StringUtils.isNotEmpty(jsonObject.getString("oldData")) ? jsonObject.getString("oldData"):"";
                data = StringUtils.isNotEmpty(jsonObject.getString("data")) ? jsonObject.getString("data"):"";
            }
            //处理类型对应name值
            name = nameInfo(jsonObject,"name",language);
        }else if(StringUtils.isNotEmpty(typeString) && (typeString.contains("12"))){//特殊延时赛种
            String object = StringUtils.isNotEmpty(jsonObject.getString("oldData")) ?jsonObject.getString("oldData"):"";
            if(object.contains("[")){
                JSONArray array = JSONArray.parseArray(jsonObject.getString("oldData"));
                if(null !=array && array.size()>0){
                    for(int i =0;i<array.size();i++){
                        JSONObject obj =JSONObject.parseObject(array.getString(i));
                        if(StringUtils.isNotEmpty(oldData)) {
                            oldData = oldData + " " + transformationValue(obj.getString("sportId"), sportMap);
                        }else{
                            oldData = transformationValue(obj.getString("sportId"), sportMap);
                        }
                    }
                }
            }else{
                oldData="关";
            }
            String object1 = StringUtils.isNotEmpty(jsonObject.getString("data")) ?jsonObject.getString("data"):"";

            if(object1.contains("[")) {
                JSONArray array1 = JSONArray.parseArray("data");
                if (null != array1 && array1.size() > 0) {
                    for (int i = 0; i < array1.size(); i++) {
                        JSONObject obj = JSONObject.parseObject(array1.getString(i));
                        if (StringUtils.isNotEmpty(data)) {
                            data = data + " " + transformationValue(obj.getString("sportId"), sportMap);
                        } else {
                            data = transformationValue(obj.getString("sportId"), sportMap);
                        }
                    }
                }
            }else{
                data="关";
            }
        }else if(StringUtils.isNotEmpty(typeString) && typeString.equals("15")){//赔率分组
           if( StringUtils.isNotEmpty(jsonObject.toJSONString("oldData"))){
               oldData =  Integer.valueOf(jsonObject.toJSONString("oldData"))-10 >0 ? (Integer.valueOf(jsonObject.toJSONString("oldData"))-10)+"": "0";
           }
            if( StringUtils.isNotEmpty(jsonObject.toJSONString("data"))){
                data =  Integer.valueOf(jsonObject.toJSONString("data"))-10 >0 ? (Integer.valueOf(jsonObject.toJSONString("data"))-10)+"": "0";
            }
        } else if(StringUtils.isNotEmpty(typeString) && typeString.equals("15")){//赛种货量百分比
            JSONArray ary = JSONArray.parseArray("oldData");
            if(null !=ary && ary.size()>0){
                for(int i =0;i<ary.size();i++){
                    JSONObject obj =JSONObject.parseObject(ary.getString(i));
                    if(StringUtils.isNotEmpty(oldData)) {
                        oldData =oldData + "   " + transformationValue(obj.getString("sportId"), sportMap) + " " + obj.getString("betRate");
                    }else{
                        oldData = transformationValue(obj.getString("sportId"), sportMap) + " " + obj.getString("betRate");
                    }
                }
            }
            JSONArray array1 = JSONArray.parseArray("data");
            if(null !=array1 && array1.size()>0){
                for(int i =0;i<array1.size();i++){
                    JSONObject obj =JSONObject.parseObject(ary.getString(i));
                    if(StringUtils.isNotEmpty(data)){
                        data =data+"   " + transformationValue(obj.getString("sportId"),sportMap) +" " +obj.getString("betRate");
                    }else{
                        data =transformationValue(obj.getString("sportId"),sportMap) +" " +obj.getString("betRate");
                    }
                }
            }

        }else if(StringUtils.isNotEmpty(typeString) && typeString.equals("13")){//提前结算
            oldData = StringUtil.isNotEmpty(jsonObject.toJSONString("oldData")) && "1".equals(jsonObject.toJSONString("oldData"))
            ? "是" : "否";
            data = StringUtil.isNotEmpty(jsonObject.toJSONString("data")) && "1".equals(jsonObject.toJSONString("oldData"))
                    ? "是" : "否";
        }else  if(StringUtils.isNotEmpty(typeString) && (typeString.equals("23") || typeString.equals("24"))) {//动态风控
            //动态风控
            oldData = StringUtils.isNotEmpty(jsonObject.getString("oldData")) ?jsonObject.getString("oldData"):"";
            data = StringUtils.isNotEmpty(jsonObject.getString("data")) ?jsonObject.getString("data"):"";
        } else if(StringUtils.isNotEmpty(typeString) && (typeString.equals("25"))){
            //派彩限额
            oldData = StringUtils.isNotEmpty(jsonObject.getString("oldData")) ?jsonObject.getString("oldData"):"";
            data = StringUtils.isNotEmpty(jsonObject.getString("data")) ?jsonObject.getString("data"):"";
        }
        if(StringUtils.isNotEmpty(name)){
            bff.append(name).append(": ");
        }
        if(StringUtils.isNotEmpty(jsonObject.getString("oldData"))){
            bff.append(oldData);
        }
        if(StringUtils.isNotEmpty(jsonObject.getString("data"))){
            bff.append("---->").append(data);
        }
        return bff;
    }

    private String transformationValue(String sportId,Map<Integer, String> sportVOS){
        String sportNamed = "";
        for(Map.Entry entry: sportVOS.entrySet()){
            if(sportId.equals(entry.getKey())) {
                sportNamed = entry.getValue().toString();
                break;
            }
        }
        return sportNamed;
    }

    private String dataInfo(JSONObject jsonObject,String newData,String language) {
        String neData ="";
        if(StringUtils.isNotEmpty(jsonObject.getString(newData))){

              switch (jsonObject.getString(newData)){
                  case "1":
                      neData=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?UserLimitEnum.LIMIT_TYPE_1.getRemark():UserLimitEnum.LIMIT_TYPE_1.getEnRemark();
                      break;
                  case "2":
                      neData=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?UserLimitEnum.LIMIT_TYPE_2.getRemark():UserLimitEnum.LIMIT_TYPE_2.getEnRemark();
                      break;
                  case "3":
                      neData=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?UserLimitEnum.LIMIT_TYPE_3.getRemark():UserLimitEnum.LIMIT_TYPE_3.getEnRemark();
                      break;
                  case "4":
                      neData=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?UserLimitEnum.LIMIT_TYPE_4.getRemark():UserLimitEnum.LIMIT_TYPE_4.getEnRemark();
                      break;
                  case "5":
                      neData=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?UserLimitEnum.LIMIT_TYPE_5.getRemark():UserLimitEnum.LIMIT_TYPE_5.getEnRemark();
                      break;
             }
        }


        return neData;
    }

    private String nameInfo(JSONObject jsonObject,String name,String language) {
        String endName ="";
        if(StringUtils.isNotEmpty(jsonObject.getString("type"))){

            switch (jsonObject.getString("type")){
                case "2":
                    endName=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ? AbnormalLimitEnum.LIMIT_TYPE_2.getRemark():AbnormalLimitEnum.LIMIT_TYPE_2.getEnRemark();
                    break;
                case "3":
                    endName=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?AbnormalLimitEnum.LIMIT_TYPE_3.getRemark():AbnormalLimitEnum.LIMIT_TYPE_3.getEnRemark();
                    break;
                case "4":
                    endName=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?AbnormalLimitEnum.LIMIT_TYPE_4.getRemark():AbnormalLimitEnum.LIMIT_TYPE_4.getEnRemark();
                    break;
                case "5":
                    endName=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?AbnormalLimitEnum.LIMIT_TYPE_5.getRemark():AbnormalLimitEnum.LIMIT_TYPE_5.getEnRemark();
                    break;
                case "8":
                    endName=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?AbnormalLimitEnum.LIMIT_TYPE_8.getRemark():AbnormalLimitEnum.LIMIT_TYPE_8.getEnRemark();
                    break;
                case "120":
                    endName=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?AbnormalLimitEnum.LIMIT_TYPE_120.getRemark():AbnormalLimitEnum.LIMIT_TYPE_120.getEnRemark();
                    break;
                case "121":
                    endName=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?AbnormalLimitEnum.LIMIT_TYPE_121.getRemark():AbnormalLimitEnum.LIMIT_TYPE_121.getEnRemark();
                    break;
                case "122":
                    endName=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?AbnormalLimitEnum.LIMIT_TYPE_122.getRemark():AbnormalLimitEnum.LIMIT_TYPE_122.getEnRemark();
                    break;
                case "123":
                    endName=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?AbnormalLimitEnum.LIMIT_TYPE_123.getRemark():AbnormalLimitEnum.LIMIT_TYPE_123.getEnRemark();
                    break;
                case "130":
                    endName=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?AbnormalLimitEnum.LIMIT_TYPE_130.getRemark():AbnormalLimitEnum.LIMIT_TYPE_130.getEnRemark();
                    break;
                case "131":
                    endName=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?AbnormalLimitEnum.LIMIT_TYPE_131.getRemark():AbnormalLimitEnum.LIMIT_TYPE_131.getEnRemark();
                    break;
                case "132":
                    endName=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?AbnormalLimitEnum.LIMIT_TYPE_132.getRemark():AbnormalLimitEnum.LIMIT_TYPE_132.getEnRemark();
                    break;
                case "133":
                    endName=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?AbnormalLimitEnum.LIMIT_TYPE_133.getRemark():AbnormalLimitEnum.LIMIT_TYPE_133.getEnRemark();
                    break;
                case "210":
                    endName=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?AbnormalLimitEnum.LIMIT_TYPE_210.getRemark():AbnormalLimitEnum.LIMIT_TYPE_210.getEnRemark();
                    break;
                case "220":
                    endName=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?AbnormalLimitEnum.LIMIT_TYPE_220.getRemark():AbnormalLimitEnum.LIMIT_TYPE_220.getEnRemark();
                    break;
                case "230":
                    endName=LANGUAGE_CHINESE_SIMPLIFIED.equals(language) ?AbnormalLimitEnum.LIMIT_TYPE_230.getRemark():AbnormalLimitEnum.LIMIT_TYPE_230.getEnRemark();
                    break;
                default:
                    endName=jsonObject.getString(name);
            }
        }


        return endName;
    }

    private LinkedHashMap<String, String> getUserheader(String language,Integer type) {
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        if (language.equalsIgnoreCase(Constant.LANGUAGE_CHINESE_SIMPLIFIED)) {
            header.put("1", "序号");
            header.put("2", "用户ID");
            header.put("3", "用户名");
            header.put("4", "所属商户");
            header.put("5", "日期");
            header.put("6", "风控措施");

        } else {
            header.put("1", "NO");
            header.put("2", "UserId");
            header.put("3", "UserName");
            header.put("4", "MerchantCode");
            header.put("5", "Date");
            header.put("6", "RiskMeasures");
        }
        return header;
    }
}
