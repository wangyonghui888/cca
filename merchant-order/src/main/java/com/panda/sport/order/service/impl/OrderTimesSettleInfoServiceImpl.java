package com.panda.sport.order.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.panda.sport.backup.mapper.BackUpOrderTimesSettleInfoMapper;
import com.panda.sport.bss.mapper.TAccountMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.dto.InfomationOf2TimesOrderDTO;
import com.panda.sport.merchant.common.po.bss.AccountPO;
import com.panda.sport.merchant.common.po.bss.OrderTimesSettleInfoPO;
import com.panda.sport.merchant.common.utils.DateUtils;
import com.panda.sport.merchant.common.vo.OrderTimesSettleInfoReqVO;
import com.panda.sport.merchant.common.vo.OrderTimesSettleInfoRespVO;
import com.panda.sport.merchant.common.vo.PageVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.order.service.MerchantFileService;
import com.panda.sport.order.service.OrderTimesSettleInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 *
 * 订单多次结算账变 服务实现类
 *
 * @author amos
 * @since 2022-05-22
 */
@Service
public class OrderTimesSettleInfoServiceImpl extends ServiceImpl<BackUpOrderTimesSettleInfoMapper, OrderTimesSettleInfoPO> implements OrderTimesSettleInfoService {

    @Resource
    private MerchantFileService merchantFileService;

    @Autowired
    private TAccountMapper tAccountMapper;

    @Override
    public Response pageList(OrderTimesSettleInfoReqVO pageReqVO) {

        // 统计负余额总额  不赔偿总额 待处理总额
        Map<String,Object> statisticsMap =  baseMapper.statistic(pageReqVO);
//        statisticsMap.put("minusAmountTotal",Double.parseDouble(statisticsMap.get("minusAmountTotal").toString())/1000);
//        statisticsMap.put("noDamageAmountTotal",Double.parseDouble(statisticsMap.get("noDamageAmountTotal").toString())/1000);
//        statisticsMap.put("prepareAmountTotal",Double.parseDouble(statisticsMap.get("prepareAmountTotal").toString())/1000);

        Page<Object> page = PageHelper.startPage(pageReqVO.getPageNum(), pageReqVO.getPageSize(), true);

        List<OrderTimesSettleInfoPO> poList = baseMapper.pageList(pageReqVO);

        if (CollectionUtils.isEmpty(poList)){
            return  Response.returnSuccess(statisticsMap);
        }
        // 数据转换
        List<OrderTimesSettleInfoRespVO>  voList = poList.stream().map(po -> {
            OrderTimesSettleInfoRespVO vo = new OrderTimesSettleInfoRespVO();
            BeanUtils.copyProperties(po, vo);
            String remark = vo.getRemark();

            if (remark.contains("原因")) {
                vo.setChangeReason("官方");
                vo .setIsDamageStr("不赔偿");
            }else{
                vo.setChangeReason("待判定");
                vo.setIsDamageStr("待处理");
            }

            // 分转元
            vo      .setUid(po.getUid().toString())
                    .setAmount(BigDecimal.valueOf(po.getAmount().doubleValue()/100))
                    .setFirstChangeTime(DateUtils.transferLongToDateStrings(po.getFirstChangeTime()))
                    .setLastChangeTime(DateUtils.transferLongToDateStrings(po.getLastChangeTime()))
                    .setBeginTime(DateUtils.transferLongToDateStrings(po.getBeginTime()))
                    .setNegativeAmount( BigDecimal.valueOf(po.getNegativeAmount().doubleValue()/100))
                    .setFirstChangeAmount(BigDecimal.valueOf(po.getFirstChangeAmount().doubleValue()/100))
                    .setLastChangeAmount(BigDecimal.valueOf(po.getLastChangeAmount().doubleValue()/100))
                    .setLastAfterChangeAmount(BigDecimal.valueOf(po.getLastAfterChangeAmount().doubleValue()/100))
                    .setFirstChangeBeforeAmount(BigDecimal.valueOf(po.getFirstChangeBeforeAmount().doubleValue()/100));
            return vo;
        }).collect(Collectors.toList());

         statisticsMap.put("pageInfo",new PageVO(page,voList));

        return   Response.returnSuccess(statisticsMap);

    }

    @Override
    public Map<String, Object> orderTimeSettleExport(String language, OrderTimesSettleInfoReqVO pageReqVO, String userName) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", "0000");
        resultMap.put("msg",  "导出任务创建成功,请在文件列表等候下载！");
        try {
            merchantFileService.saveFileTask( "二次结算统计导出_"
                    , null, userName, JSON.toJSONString(pageReqVO),
                 "二次结算统计下载" , "orderTimesSettleExportServiceImpl", null);
        } catch (RuntimeException e) {
            resultMap.put("code", "0002");
            resultMap.put("msg", e.getMessage());
        }
        return resultMap;
    }

    @Override
    public void exportInfomationOf2TimesOrder(HttpServletResponse response, String ymd) {
        try {
            String time = (ymd == null ? new SimpleDateFormat("yyyy_MM_dd").format(new Date()) : ymd);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long startTime = new SimpleDateFormat("yyyy_MM_dd").parse(ymd).getTime();
            String fileName = URLEncoder.encode(Constant.INFORMATION_OF_2TIMES_ORDER + time + ".xlsx", StandardCharsets.UTF_8.toString());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
            ServletOutputStream outputStream = response.getOutputStream();
            List<OrderTimesSettleInfoPO> tOrderTimesSettleInfoList = baseMapper.selectTOrderTimesSettleInfoList(startTime - 24 * 60 * 60 * 1000, startTime);
            //查询导出时候的余额
            List<Long> uidList = new ArrayList<>();
            tOrderTimesSettleInfoList.forEach(e -> uidList.add(e.getUid()));
            List<AccountPO> tAccountList = tAccountMapper.selectTAccountByUids(uidList);
            for (OrderTimesSettleInfoPO tOrderTimesSettleInfo : tOrderTimesSettleInfoList) {
                for (AccountPO tAccount : tAccountList) {
                    if (tOrderTimesSettleInfo.getUid().equals(tAccount.getUid())) {
                        tOrderTimesSettleInfo.setAmount(tAccount.getAmount());
                    }
                }
            }
            List<InfomationOf2TimesOrderDTO> infomationOf2TimesOrderDTOList = new ArrayList<>();
            for (OrderTimesSettleInfoPO tOrderTimesSettleInfo : tOrderTimesSettleInfoList) {
                InfomationOf2TimesOrderDTO infomationOf2TimesOrderDTO = new InfomationOf2TimesOrderDTO();
                infomationOf2TimesOrderDTO.setUid(tOrderTimesSettleInfo.getUid().toString());
                infomationOf2TimesOrderDTO.setUsername(tOrderTimesSettleInfo.getUsername());
                infomationOf2TimesOrderDTO.setMerchantCode(tOrderTimesSettleInfo.getMerchantCode());
                infomationOf2TimesOrderDTO.setMerchantName(tOrderTimesSettleInfo.getMerchantName());
                infomationOf2TimesOrderDTO.setOrderNo(tOrderTimesSettleInfo.getOrderNo());
                infomationOf2TimesOrderDTO.setDiffNegativeAmount(new DecimalFormat(".00").format(tOrderTimesSettleInfo.getNegativeAmount() * 1.0 / 100));
                infomationOf2TimesOrderDTO.setAmount(new DecimalFormat(".00").format(tOrderTimesSettleInfo.getAmount() * 1.0 / 100));
                infomationOf2TimesOrderDTO.setMatchId(tOrderTimesSettleInfo.getMatchId().toString());
                infomationOf2TimesOrderDTO.setMatchInfo(tOrderTimesSettleInfo.getMatchInfo());
                infomationOf2TimesOrderDTO.setMatchName(tOrderTimesSettleInfo.getMatchName());
                infomationOf2TimesOrderDTO.setPlayName(tOrderTimesSettleInfo.getPlayName());
                infomationOf2TimesOrderDTO.setPlayOptionName(tOrderTimesSettleInfo.getPlayOptionName());
                infomationOf2TimesOrderDTO.setChangeReason(tOrderTimesSettleInfo.getRemark());
                infomationOf2TimesOrderDTO.setBeginTime(simpleDateFormat.format(tOrderTimesSettleInfo.getBeginTime()));
                infomationOf2TimesOrderDTO.setLastChangeAmount(new DecimalFormat(".00").format(tOrderTimesSettleInfo.getLastChangeAmount() * 1.0 / 100));
                infomationOf2TimesOrderDTO.setLastChangeTime(simpleDateFormat.format(tOrderTimesSettleInfo.getLastChangeTime()));
                infomationOf2TimesOrderDTO.setLastAfterChangeAmount(new DecimalFormat(".00").format(tOrderTimesSettleInfo.getLastAfterChangeAmount() / 100));
                infomationOf2TimesOrderDTO.setFirstChangeAmount(new DecimalFormat(".00").format(tOrderTimesSettleInfo.getFirstChangeAmount() * 1.0 / 100));
                infomationOf2TimesOrderDTO.setFirstChangeTime(simpleDateFormat.format(tOrderTimesSettleInfo.getFirstChangeTime()));
                infomationOf2TimesOrderDTO.setFirstChangeBeforeAmount(new DecimalFormat(".00").format(tOrderTimesSettleInfo.getFirstChangeBeforeAmount() * 1.0 / 100));
                infomationOf2TimesOrderDTOList.add(infomationOf2TimesOrderDTO);
            }
            ExcelWriter excelWriter = EasyExcel.write(outputStream, InfomationOf2TimesOrderDTO.class).build();
            try {
                Set<String> columns = new HashSet<>();
                columns.add("uid");
                columns.add("username");
                columns.add("merchantCode");
                columns.add("merchantName");
                columns.add("orderNo");
                columns.add("diffNegativeAmount");
                columns.add("amount");
                columns.add("matchId");
                columns.add("matchInfo");
                columns.add("matchName");
                columns.add("playName");
                columns.add("playOptionName");
                columns.add("changeReason");
                columns.add("beginTime");
                columns.add("lastChangeAmount");
                columns.add("lastChangeTime");
                columns.add("lastAfterChangeAmount");
                columns.add("firstChangeAmount");
                columns.add("firstChangeTime");
                columns.add("firstChangeBeforeAmount");
                EasyExcel.write(outputStream, InfomationOf2TimesOrderDTO.class)
                        .head(InfomationOf2TimesOrderDTO.class)
                        .includeColumnFiledNames(columns)
                        .sheet(Constant.INFORMATION_OF_2TIMES_ORDER)
                        .doWrite(infomationOf2TimesOrderDTOList);
            } catch (Exception e) {
                log.error("运营二次结算数据导出异常", e);
            } finally {
                excelWriter.finish();
            }
        } catch (Exception e) {
            log.error("运营二次结算数据导出异常", e);
        }
    }

}
