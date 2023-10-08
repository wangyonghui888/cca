package com.panda.center.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.panda.center.entity.OlympicLuckyboxRecords;
import com.panda.center.mapper.activity.OlympicLuckyboxRecordsMapper;
import com.panda.center.param.LuckyBoxParam;
import com.panda.center.result.Response;
import com.panda.center.service.IOlympicLuckyboxRecordsService;
import com.panda.center.utils.CsvUtil;
import com.panda.center.vo.LuckyBoxRecordsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 * 奥运拆盒历史记录 服务实现类
 * </p>
 *
 * @author Auto Generator
 * @since 2021-12-26
 */
@Service
@Slf4j
public class OlympicLuckyboxRecordsServiceImpl extends ServiceImpl<OlympicLuckyboxRecordsMapper,
        OlympicLuckyboxRecords> implements IOlympicLuckyboxRecordsService {

    @Resource
    private OlympicLuckyboxRecordsMapper luckyboxRecordsMapper;

    @Override
    public Response<?> queryLuckyBoxHistory(LuckyBoxParam luckyBoxParam) {
        QueryWrapper<OlympicLuckyboxRecords> queryWrapper = getWrapper(luckyBoxParam);
        Page<LuckyBoxRecordsVO> page = luckyBoxParam.getPage();
        PageHelper.startPage(Long.valueOf(page.getCurrent()).intValue(), Long.valueOf(page.getSize()).intValue());
        List<OlympicLuckyboxRecords> list = luckyboxRecordsMapper.selectList(queryWrapper);
        PageInfo<OlympicLuckyboxRecords> poList = new PageInfo<>(list);
        page.setTotal(poList.getTotal());
        List<LuckyBoxRecordsVO> voList = Lists.newArrayList();
        for (OlympicLuckyboxRecords records : list) {
            LuckyBoxRecordsVO recordsVO = new LuckyBoxRecordsVO();
            records.setAward(records.getAward() / 100);
            BeanUtils.copyProperties(records, recordsVO);
            recordsVO.setUid(String.valueOf(records.getUid()));
            recordsVO.setBoxName(convertLuckyBox(records.getBoxType()));
            recordsVO.setStatus("已领取");
            recordsVO.setCreateTime(DateUtil.formatDateTime(new Date(records.getCreateTime())));
            voList.add(recordsVO);
        }
        page.setRecords(voList);
        return Response.returnSuccess(page);
    }

    @Override
    public Response<?> queryLuckyBoxHistoryExcel(LuckyBoxParam luckyBoxParam, HttpServletResponse response) {
        if (luckyBoxParam.getStartTime() == null) {
            return Response.returnFail("start time can not be null");
        }
        if (luckyBoxParam.getEndTime() == null) {
            return Response.returnFail("end time can not be null");
        }
        QueryWrapper<OlympicLuckyboxRecords> queryWrapper = getWrapper(luckyBoxParam);
        List<OlympicLuckyboxRecords> records = luckyboxRecordsMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(records)) {
            return Response.returnSuccess();
        }
        LinkedHashMap<String, String> headers = Maps.newLinkedHashMap();
        headers.put("1", "商户ID");
        headers.put("2", "商户账号");
        headers.put("3", "用户名");
        headers.put("4", "用户ID");
        headers.put("5", "盲盒类型");
        headers.put("6", "金额");
        headers.put("7", "消耗奖卷数");
        headers.put("8", "领取状态");
        headers.put("9", "派发时间");
        List<LinkedHashMap<String, Object>> mapList = Lists.newArrayList();
        for (OlympicLuckyboxRecords luckyboxRecords : records) {
            LinkedHashMap<String, Object> datas = Maps.newLinkedHashMap();
            datas.put("1", luckyboxRecords.getMerchantId().concat("\t"));
            datas.put("2", luckyboxRecords.getMerchantAccount());
            datas.put("3", luckyboxRecords.getUserName());
            datas.put("4", String.valueOf(luckyboxRecords.getUid()).concat("\t"));
            datas.put("5", convertLuckyBox(luckyboxRecords.getBoxType()));
            datas.put("6", luckyboxRecords.getAward() / 100);
            datas.put("7", luckyboxRecords.getUseToken());
            datas.put("8", "已领取");
            datas.put("9", DateUtil.formatDateTime(new Date(luckyboxRecords.getCreateTime())));
            mapList.add(datas);
        }
        OutputStream outputStream = null;
        try {
            byte[] exportCSV = CsvUtil.exportCSV(headers, mapList);
            if (ObjectUtil.isNull(exportCSV)) {
                return Response.returnSuccess();
            }
            String fileName = DateUtil.formatDate(new Date(luckyBoxParam.getStartTime())) + "_" +
                    DateUtil.formatDate(new Date(luckyBoxParam.getEndTime())) + "_领取记录" + ".csv";
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
            outputStream = response.getOutputStream();
            outputStream.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            FileCopyUtils.copy(exportCSV, outputStream);
        } catch (IOException e) {
            log.error("export error", e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
        return Response.returnSuccess();
    }

    private QueryWrapper<OlympicLuckyboxRecords> getWrapper(LuckyBoxParam luckyBoxParam) {
        QueryWrapper<OlympicLuckyboxRecords> queryWrapper = new QueryWrapper<>();
        String userSer = luckyBoxParam.getUserName();
        if (StringUtils.isNotBlank(userSer)) {
            queryWrapper.and(var -> var.like("uid", userSer).or().like("user_name", userSer));
        }
        Long staTime = luckyBoxParam.getStartTime();
        if (staTime != null) {
            queryWrapper.ge("create_time", staTime);
        }
        Long endTime = luckyBoxParam.getEndTime();
        if (endTime != null) {
            queryWrapper.le("create_time", endTime);
        }
        String merchantId = luckyBoxParam.getMerchantId();
        if (StringUtils.isNotBlank(merchantId)) {
            List<String> merIds = Arrays.asList(merchantId.split(","));
            queryWrapper.in("merchant_id", merIds);
        }
        return queryWrapper;
    }

    private String convertLuckyBox(Integer boxType) {
        String boxName = "";
        switch (boxType) {
            case 1:
                boxName = "白银盲盒";
                break;
            case 2:
                boxName = "黄金盲盒";
                break;
            case 3:
                boxName = "钻石盲盒";
                break;
        }
        return boxName;
    }
}
