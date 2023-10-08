package com.panda.sport.order.service.impl;

import com.alibaba.fastjson.JSON;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.AgentLevelEnum;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.po.merchant.MerchantOrderDayPO;

import com.panda.sport.merchant.common.utils.AESUtils;
import com.panda.sport.merchant.common.utils.CreateSecretKey;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.MerchantOrderVO;
import com.panda.sport.merchant.manage.mq.MerchantProduct;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import com.panda.sport.order.feign.MerchantReportClient;
import com.panda.sport.order.service.AbstractOrderService;
import com.panda.sport.order.service.MerchantFileService;
import com.panda.sport.order.service.MerchantOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

@Slf4j
@Service
@RefreshScope
public class MerchantOrderServiceImpl implements MerchantOrderService {

    @Autowired
    private MerchantReportClient rpcClient;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private LocalCacheService localCacheService;

    @Autowired
    protected MerchantProduct merchantProduct;

    @Value("${file.path:/opt/oss/}")
    private String filePath;

    @Override
    public Response<Object> userOrderMonth(String merchantCode) {
        return null;
    }

    /**
     * 获取商户报表
     *
     * @Param: []
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:07
     */
    @Override
    public Response<?> queryMerchantReport(MerchantOrderVO vo) {
        try {
            if (vo != null && vo.getCurrency() != null &&
                    !vo.getCurrency().equals(Constant.DEFAULT_CURRENCY_ID)) {
                return new Response();
            }
            return rpcClient.merchantList(vo);
        } catch (Exception e) {
            log.error("MerchantController.merchantList error!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    @Override
    public Response<?> queryMechantAgentList(String merchantCode) {
        return Response.returnSuccess(merchantMapper.getAgentList(merchantCode));
    }

    @Override
    public List<String> queryMerchantByGroupCode(String groupCode){
        return merchantMapper.queryMerchantByGroupCode(groupCode);
    }

    /**
     * 获取商户报表 -javier
     */
    @Override
    public Response<?> listGroupByMerchant(MerchantOrderVO vo) {
        try {
            Response<Map<String, Object>> response = rpcClient.listGroupByMerchant(vo);
            Map<String, Object> map = response.getData();
            if (null != map) {
                List<?> resultList = (List<?>) map.get("list");
                ObjectMapper mapper = new ObjectMapper();
                List<MerchantOrderDayPO> filterList = mapper.convertValue(resultList, new TypeReference<List<MerchantOrderDayPO>>() {
                });
                if (CollectionUtils.isNotEmpty(filterList)) {
                    List<MerchantOrderDayPO> result = resetRepeatUser(filterList, vo);
                    reorderSubMerchant(result);
                    removeMerchantName(result);
                    map.put("list", result);
                }
            }
            return response;
        } catch (Exception e) {
            log.error("MerchantController.merchantList error!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 对内商户名称不展示
     */
    private void removeMerchantName(List<MerchantOrderDayPO> result) {
        for (MerchantOrderDayPO temp : result) {
            temp.setMerchantName("");
        }
    }

    private void reorderSubMerchant(List<MerchantOrderDayPO> result) {
        Map<String, Integer> orderMap = AbstractOrderService.setParentTimeForOrder(result);
        List<String> allChildMerchantCode = AbstractOrderService.getAllChildMerchantCode(result);
        if (CollectionUtils.isNotEmpty(allChildMerchantCode)) {
            List<Map<String, String>> codeMapList = merchantMapper.listParentCodeByMerchantCode(allChildMerchantCode);
            if (CollectionUtils.isNotEmpty(codeMapList)) {
                Map<String, String> codeMap = codeMapList.stream().collect(Collectors.toMap(tempMap -> tempMap.get("child_code"), tempMap -> tempMap.get("parent_code"), (a, b) -> b));
                for (MerchantOrderDayPO temp : result) {
                    if (AgentLevelEnum.AGENT_LEVEL_2.getCode().equals(temp.getAgentLevel())) {
                        String parentMerchantCode = codeMap.get(temp.getMerchantCode());
                        Integer order = orderMap.get(parentMerchantCode);
                        if (null != order) {
                            temp.setTime(order + 1);
                        }
                    }
                }
                Collections.sort(result, Comparator.comparingInt(MerchantOrderDayPO::getTime));
            }
        }
    }


    /**
     * 过滤重复用户统计
     *
     * @param filterList 需要过滤的数据
     * @param vo         统计时的参数
     * @return 过滤后的结果
     */
    private List<MerchantOrderDayPO> resetRepeatUser(List<MerchantOrderDayPO> filterList, MerchantOrderVO vo) {
        Set<String> parentCodes = AbstractOrderService.getAllParentMerchantCodeSet(filterList);
        if (CollectionUtils.isNotEmpty(parentCodes)) {
            List<MerchantPO> merchantList = merchantMapper.getMerchantInMerchantCode(parentCodes);
            //根据代理商或渠道商获得所有的下级商户code,用于查询
            List<String> allChildMerchantCode = new ArrayList<>();
            Map<String, List<String>> parentWithChildMerchantCodeMap = new HashMap<>();
            for (MerchantPO temp : merchantList) {
                List<String> merchantCodeList = localCacheService.getMerchantCodeList(temp.getId(), temp.getAgentLevel());
                if (CollectionUtils.isNotEmpty(merchantCodeList)) {
                    allChildMerchantCode.addAll(merchantCodeList);
                    parentWithChildMerchantCodeMap.put(temp.getMerchantCode(), merchantCodeList);
                }
            }
            vo.setMerchantCodeList(allChildMerchantCode);
            Response<Map<String, Object>> mapResponse = rpcClient.listGroupByMerchantRepeatUser(vo);
            if (null != mapResponse.getData()) {
                final Map<String, Object> tempData = mapResponse.getData();
                AbstractOrderService.resetSettleUsers(filterList, tempData, parentWithChildMerchantCodeMap);
            }
        }
        return filterList;
    }

    @Autowired
    private MerchantFileService merchantFileService;

    /**
     * 导出商户报表
     *
     * @Param: []
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:07
     */
    @Override
    public Map reportDownload(String username, MerchantOrderVO requestVO) {
        String filter = StringUtils.isEmpty(requestVO.getFilter()) ? "1" : requestVO.getFilter();
        requestVO.setFilter(filter);
        requestVO.setPageNum(1);
        requestVO.setPageSize(100000);
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String language = requestVO.getLanguage();
            merchantFileService.saveFileTask(language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "商户注单统计_" : "MerchantReport_", null, username, JSON.toJSONString(requestVO),
                    language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? "数据中心-商户注单统计" : "Report Center-Merchant Report", "groupByMerchantReportExportServiceImpl");
            resultMap.put("code", "0000");
            resultMap.put("msg", "交易记录导出任务创建成功,请在文件列表等候下载！");
        } catch (Exception e) {
            resultMap.put("code", "0002");
            resultMap.put("msg", e.getMessage());
            log.error("exportMerchantReport导出商户报表异常!", e);
        }
        return resultMap;
    }

    /**
     * 查询 当月商户前10
     *
     * @Param: []
     * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
     * @date: 2020/8/23 15:07
     */
    @Override
    public Response queryMerchantTop10(MerchantOrderVO vo) {
        try {
            return rpcClient.queryMerchantTop10(vo);
        } catch (Exception e) {
            log.error("HomeController.merchantOrderTop10,exception:", e);
            return Response.returnFail("查询TOP10异常!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<Object> updateKey(String merchantCode, String key, String startTime, String endTime, Integer userId, String language, String ip) throws Exception {
        MerchantPO merchant = merchantMapper.getMerchantInfo(merchantCode);
        String keys = CreateSecretKey.keyCreate();
        keys = AESUtils.aesEncode(keys);
        boolean result = this.abstractUpdateKey(merchantCode, keys, startTime, endTime, "");
        return result ? Response.returnSuccess(true)
                : Response.returnFail(ResponseEnum.INTERNAL_ERROR);
    }

    protected boolean abstractUpdateKey(String merchantCode, String key, String startTime, String endTime, String modifier)
            throws Exception {
        MerchantPO po = merchantMapper.getMerchantByMerchantCode(merchantCode);
        String endT = po.getEndTime();
        Date endD = DateUtils.parseDate(endT, "yyyy-MM-dd");

        Date start = DateUtils.parseDate(startTime, "yyyy-MM-dd");
        Date end = DateUtils.parseDate(endTime, "yyyy-MM-dd");
        if (start.after(end)) {
            log.warn("开始时间位于结束之后!" + start + "," + end);
            return false;
        }
        if (end.after(endD)) {
            log.warn("结束时间位于商户结束之后!" + end + "," + endD);
            return false;
        }
        String old = merchantMapper.getKey(merchantCode, Constant.REALTIME_TABLE);
        int status = 1;
        Date now = getStartTime(new Date());
        String nowStr = DateFormatUtils.format(now, "yyyyMMddHHmmss");
        long nowL = Long.parseLong(nowStr);
        Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd");
        String endStr = DateFormatUtils.format(endDate, "yyyyMMddHHmmss");
        long endL = Long.parseLong(endStr);
        if (endL < nowL) {
            status = 0;
        }
        if (old != null && old.equals(key)) {
            //merchantMapper.updateKey(key,modifier);
        } else {
            merchantMapper.updateHistoryKey(merchantCode, key, old);
        }
        log.info("更新下级商户失效时间start!" + po.toString());
        if (po.getAgentLevel() != null && po.getAgentLevel() == 1 && po.getChildConnectMode() != null) {
            merchantMapper.updateChildrenKey(po.getId(), key, startTime, endTime, status, modifier);
            //   merchantMapper.updateUnchangeChildrenKey(po.getId(), startTime, endTime, status, modifier);
        }
        //修改商户信息后，发送全量信息到MQ  给风控
        MerchantPO merchantInfo = merchantMapper.getMerchantByMerchantCode(merchantCode);
        merchantProduct.sendMessage(merchantInfo);
        return true;
    }

    private Date getStartTime(Date date) {
        Calendar dateStart = Calendar.getInstance();
        dateStart.setTime(date);
        dateStart.set(Calendar.HOUR_OF_DAY, 0);
        dateStart.set(Calendar.MINUTE, 0);
        dateStart.set(Calendar.SECOND, 0);
        return dateStart.getTime();
    }

    @Override
    public void exportMerchantKey(List<String> codeList) {
        //批量重新生成商户密钥
        String startTime = "1970-01-05";
        String endTime = "1970-01-15";
        for (String item : codeList){
            try {
                this.updateKey(item, null, startTime, endTime, null, "zs", null);
            }catch (Exception e){
                log.error("重新生成商户密钥失败!");
            }
        }
        List<MerchantPO> merchantPOList = merchantMapper.queryMerchantKey(codeList);
        List<MerchantPO> exportList = Lists.newArrayList();
        for (MerchantPO po : merchantPOList){
            MerchantPO merchantPO = new MerchantPO();
            BeanUtils.copyProperties(po, merchantPO);
            merchantPO.setMerchantKey(AESUtils.aesDecode(po.getMerchantKey()));
            exportList.add(merchantPO);
        }
        this.writeIntoCSVFile(exportList);
    }

    private void writeIntoCSVFile(List<MerchantPO> exportList){
        Date date = new Date();
        String str = DateFormatUtils.format(date, "yyMMddHHmm");
        String csvName = str + ".csv";
        try {
            byte[] bytes = exportTicketToFtp(exportList);
            File tempFile = new File(filePath + csvName);
            FileOutputStream fos = new FileOutputStream(tempFile);
            try {
                fos.write(bytes);
            } finally {
                try {
                    fos.close();
                } catch (IOException var8) {
                    log.error("CSV文件写入异常!", var8);
                }
            }
        }catch (Exception e){
            log.error("文件写入异常!", e);
        }
    }

    private byte[] exportTicketToFtp(List<MerchantPO> exportList){
        List<LinkedHashMap<String, Object>> exportData = new ArrayList<>(org.apache.commons.collections.CollectionUtils.isEmpty(exportList) ? 0 : exportList.size());
        int i = 0;
        for (MerchantPO po : exportList) {
            i = i + 1;
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            rowData.put("1", i);
            rowData.put("2", po.getMerchantName());
            rowData.put("3", po.getMerchantCode());
            rowData.put("4", po.getMerchantKey());
            exportData.add(rowData);
        }
        LinkedHashMap<String, String> header = new LinkedHashMap<>();
        header.put("1", "序号");
        header.put("2", "商户简称");
        header.put("3", "商户编码");
        header.put("4", "商户密钥");
        return CsvUtil.exportCSV(header, exportData);
    }
}