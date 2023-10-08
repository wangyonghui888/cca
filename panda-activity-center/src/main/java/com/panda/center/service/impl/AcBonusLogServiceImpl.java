package com.panda.center.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.panda.center.entity.AcBonusLog;
import com.panda.center.mapper.activity.AcBonusLogMapper;
import com.panda.center.result.Response;
import com.panda.center.service.IAcBonusLogService;
import com.panda.center.utils.CsvUtil;
import com.panda.center.param.AcBonusLogParam;
import com.panda.center.vo.AcBonusLogVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 * 优惠券领取日志表 服务实现类
 * </p>
 *
 * @author Auto Generator
 * @since 2021-12-24
 */
@Service
@Slf4j
public class AcBonusLogServiceImpl extends ServiceImpl<AcBonusLogMapper, AcBonusLog> implements IAcBonusLogService {
    @Resource
    private AcBonusLogMapper acBonusLogMapper;

    @Override
    public Response<?> pageList(AcBonusLogParam param) {
        Integer taskId = param.getTaskId();
        String merchantSer = param.getMerchantSer();
        String userSer = param.getUserSer();

        if (ObjectUtil.isNull(taskId)) {
            return Response.returnFail("taskId can not be empty");
        }

        QueryWrapper<AcBonusLog> wrapper = new QueryWrapper<>();
        wrapper.select("merchant_code", "merchant_account", "user_name", "uid", "ticket_num", "receive_time")
                .eq("task_id", taskId)
                .orderByDesc("receive_time");

        if (StringUtils.isNotBlank(merchantSer)) {
            wrapper.like("merchant_code", merchantSer);
        }
        if (StringUtils.isNotBlank(userSer)) {
            wrapper.and(i -> i.like("user_name", userSer)
                    .or().like("uid", userSer));
        }
        Long strTime = param.getStrTime();
        Long endTime = param.getEndTime();
        if (ObjectUtil.isNotNull(strTime) && ObjectUtil.isNotNull(endTime)) {
            wrapper.ge("receive_time", strTime)
                    .le("receive_time", endTime);
        }
        Integer page = param.getPage();
        if (ObjectUtil.isNull(page)) {
            page = 1;
        }
        Integer size = param.getSize();
        if (ObjectUtil.isNull(size)) {
            size = 20;
        }
        PageHelper.startPage(page, size);
        List<AcBonusLog> list = acBonusLogMapper.selectList(wrapper);
        PageInfo<AcBonusLog> poList = new PageInfo<>(list);
        Page<AcBonusLogVO> voPage = new Page<>(poList.getPageNum(), poList.getPageSize());
        voPage.setTotal(poList.getTotal());
        for (AcBonusLog acBonusLog : list) {
            AcBonusLogVO bonusLogVO = AcBonusLogVO.getInstance();
            BeanUtils.copyProperties(acBonusLog, bonusLogVO);
            voPage.add(bonusLogVO);
        }
        return Response.returnSuccess(new PageInfo<>(voPage));
    }

    @Override
    public Response<?> export(AcBonusLogParam param, HttpServletResponse response) {
        Integer taskId = param.getTaskId();
        String merchantSer = param.getMerchantSer();
        String userSer = param.getUserSer();
        Long strTime = param.getStrTime();
        Long endTime = param.getEndTime();
        if (ObjectUtil.isNull(taskId)) {
            return Response.returnFail("taskId can not be empty");
        }
        if (ObjectUtil.isNull(strTime) || ObjectUtil.isNull(endTime)) {
            return Response.returnFail("strTime or endTime can not be null");
        }
        QueryWrapper<AcBonusLog> wrapper = new QueryWrapper<>();
        wrapper.select("merchant_code", "merchant_account", "user_name", "uid", "ticket_num", "receive_time")
                .eq("task_id", taskId)
                .ge("receive_time", strTime)
                .le("receive_time", endTime)
                .orderByDesc("receive_time");
        if (StringUtils.isNotBlank(merchantSer)) {
            wrapper.like("merchant_code", merchantSer);
        }
        if (StringUtils.isNotBlank(userSer)) {
            wrapper.and(i -> i.like("user_name", userSer)
                    .or().like("uid", userSer));
        }
        List<AcBonusLog> list = acBonusLogMapper.selectList(wrapper);
        LinkedHashMap<String, String> headers = Maps.newLinkedHashMap();
        headers.put("1", "商户编号");
        headers.put("2", "用户名");
        headers.put("3", "用户ID");
        headers.put("4", "领取状态");
        headers.put("5", "奖券数");
        headers.put("6", "领取时间");
        List<LinkedHashMap<String, Object>> mapList = Lists.newArrayList();
        for (AcBonusLog acBonusLog : list) {
            LinkedHashMap<String, Object> datas = Maps.newLinkedHashMap();
            datas.put("1", acBonusLog.getMerchantCode().concat("\t"));
            datas.put("2", acBonusLog.getUserName());
            datas.put("3", acBonusLog.getUid().concat("\t"));
            datas.put("4", "已领取");
            datas.put("5", acBonusLog.getTicketNum());
            datas.put("6", DateUtil.formatDateTime(new Date(acBonusLog.getReceiveTime())));
            mapList.add(datas);
        }
        OutputStream outputStream = null;
        try {
            byte[] exportCSV = CsvUtil.exportCSV(headers, mapList);
            if (ObjectUtil.isNull(exportCSV)) {
                return Response.returnFail("taskId can not be empty");
            }
            String fileName = DateUtil.formatDate(new Date(strTime)) + "_" +
                    DateUtil.formatDate(new Date(endTime)) + "_领取记录" + ".csv";
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
            outputStream = response.getOutputStream();
            outputStream.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            FileCopyUtils.copy(exportCSV, outputStream);
        } catch (IOException e) {
            log.error("export error", e);
        } finally {
            try {
                if (ObjectUtil.isNotNull(outputStream)) {
                    outputStream.close();
                }
            } catch (IOException e) {
                log.error("outputStream close error", e);
            }
        }
        return Response.returnSuccess();
    }
}
