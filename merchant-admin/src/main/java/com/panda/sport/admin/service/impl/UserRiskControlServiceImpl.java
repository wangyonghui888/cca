package com.panda.sport.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.panda.sport.admin.feign.PandaRcsTradeClient;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.UserRiskControlService;
import com.panda.sport.admin.utils.ExportUtils;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.merchant.common.dto.UserRiskControlPageQueryDTO;
import com.panda.sport.merchant.common.dto.UserRiskControlPendingCountQueryDTO;
import com.panda.sport.merchant.common.dto.UserRiskControlStatusEditDTO;
import com.panda.sport.merchant.common.enums.activity.SlotChangeType;
import com.panda.sport.merchant.common.po.bss.SSlotsTokenChangeHistory;
import com.panda.sport.merchant.common.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author :  ives
 * @Description :  对外商户 账户中心/平台用户风控 实现类
 * @Date: 2022-04-09 18:44
 */
@Service
@Slf4j
public class UserRiskControlServiceImpl implements UserRiskControlService {

    @Resource
    private PandaRcsTradeClient userLimitApiService;

    @Override
    public Response<?> queryPageUserRiskControlList(UserRiskControlPageQueryReqVO queryReqVO) {
        UserRiskControlPageQueryDTO userRiskControlPageQueryDTO = new UserRiskControlPageQueryDTO();
        BeanUtils.copyProperties(queryReqVO,userRiskControlPageQueryDTO);
        userRiskControlPageQueryDTO.setCurrentPage(queryReqVO.getPageNum());

        JwtUser user = SecurityUtils.getUser();
        userRiskControlPageQueryDTO.setMerchantCode(user.getMerchantCode());

        Map response = userLimitApiService.queryPageUserRiskControlList(userRiskControlPageQueryDTO);
        if (ObjectUtil.isNull(response)){
            Response.returnFail("风控接口/riskMerchantManager/pageList返回为空！");
        }
        Integer successStatus = 200;
        String successCode = "code";
        if (!Objects.equals(successStatus,response.get(successCode))){
            Response.returnFail(response.get("msg").toString());
        }
        return Response.returnSuccess(response.get("data"));
    }

    @Override
    public Response<?> updateUserRiskControlStatus(UserRiskControlStatusEditReqVO editReqVO) {
        UserRiskControlStatusEditDTO userRiskControlStatusEditDTO = new UserRiskControlStatusEditDTO();
        BeanUtils.copyProperties(editReqVO,userRiskControlStatusEditDTO);

        JwtUser user = SecurityUtils.getUser();
        userRiskControlStatusEditDTO.setMerchantOperator(user.getUsername());

        Map response = userLimitApiService.updateStatus(userRiskControlStatusEditDTO);
        if (ObjectUtil.isNull(response)){
            Response.returnFail("风控接口/riskMerchantManager/pageList返回为空！");
        }
        Integer successStatus = 200;
        String successCode = "code";
        if (!Objects.equals(successStatus,response.get(successCode))){
            Response.returnFail(response.get("msg").toString());
        }
        return Response.returnSuccess();
    }

    @Override
    public void exportUserRiskControlList(UserRiskControlQueryAllReqVO reqVO, HttpServletResponse httpServletResponse) {

        JwtUser user = SecurityUtils.getUser();
        reqVO.setMerchantCode(user.getMerchantCode());
        Map response = userLimitApiService.exportUserRiskControlList(reqVO);

        if (ObjectUtil.isNull(response)){
            log.error("风控接口/riskMerchantManager/exportOutPutList返回为空！");
        }
        Integer successStatus = 200;
        String successCode = "code";
        if (!Objects.equals(successStatus,response.get(successCode))){
            log.error("风控接口/riskMerchantManager/exportOutPutList返回" + response.get("msg").toString());
        }

        List<Map> queryAllRespVOList = (List<Map>) response.get("data");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<UserRiskControlQueryAllRespVO> respVOList = new ArrayList<>(10);
        for (Map vo : queryAllRespVOList) {
            UserRiskControlQueryAllRespVO queryAllRespVO = new UserRiskControlQueryAllRespVO();
            queryAllRespVO.setUserId(vo.get("userId").toString().concat("\t"));
            queryAllRespVO.setUserName(ObjectUtil.isNotNull(vo.get("userName")) ? vo.get("userName").toString().concat("\t") : null);
            queryAllRespVO.setType(ObjectUtil.isNotNull(vo.get("type")) ? Integer.parseInt(vo.get("type").toString()) : null);
            queryAllRespVO.setMerchantShowValue(ObjectUtil.isNotNull(vo.get("merchantShowValue")) ? vo.get("merchantShowValue").toString().concat("\t")  : null);
            queryAllRespVO.setSupplementExplain(ObjectUtil.isNotNull(vo.get("supplementExplain")) ? vo.get("supplementExplain").toString().concat("\t")  : null);
            queryAllRespVO.setRecommendTime(ObjectUtil.isNotNull(vo.get("recommendTime")) ? Long.parseLong(vo.get("recommendTime").toString())  : null);
            queryAllRespVO.setStatus(ObjectUtil.isNotNull(vo.get("status")) ? Integer.parseInt(vo.get("status").toString())  : null);
            queryAllRespVO.setMerchantOperator(ObjectUtil.isNotNull(vo.get("merchantOperator")) ? vo.get("merchantOperator").toString().concat("\t") : null);
            queryAllRespVO.setProcessTime(ObjectUtil.isNotNull(vo.get("processTime")) ? Long.parseLong(vo.get("processTime").toString()) : null);
            queryAllRespVO.setMerchantRemark(ObjectUtil.isNotNull(vo.get("merchantRemark")) ? vo.get("merchantRemark").toString().concat("\t") : null);
            setTypeStr(queryAllRespVO);
            setStatusStr(queryAllRespVO);
            if (ObjectUtil.isNotNull(queryAllRespVO.getProcessTime())){
                queryAllRespVO.setProcessTimeStr(simpleDateFormat.format(new Date(queryAllRespVO.getProcessTime())).concat("\t"));
            }
            if (ObjectUtil.isNotNull(queryAllRespVO.getRecommendTime())){
                queryAllRespVO.setRecommendTimeStr(simpleDateFormat.format(new Date(queryAllRespVO.getRecommendTime())).concat("\t"));
            }
            respVOList.add(queryAllRespVO);
        }

//        List<UserRiskControlQueryAllRespVO> respVOList = new ArrayList<>(10);
//        UserRiskControlQueryAllRespVO queryAllRespVO = new UserRiskControlQueryAllRespVO();
//        queryAllRespVO.setUserId(495498490785900037L + "");
//        queryAllRespVO.setType(1);
//        queryAllRespVO.setStatus(2);
//        queryAllRespVO.setSupplementExplain("测试一下导入");
//        queryAllRespVO.setMerchantOperator("ives");
//        setTypeStr(queryAllRespVO);
//            setStatusStr(queryAllRespVO);
//        respVOList.add(queryAllRespVO);

        try {

            String fileName = "UserRiskControl_" + System.currentTimeMillis() + ".csv";
            ExcelWriter writer = ExcelUtil.getWriter();

            writer.addHeaderAlias("userId","用户ID");
            writer.addHeaderAlias("userName","用户名");
            writer.addHeaderAlias("typeStr","风控类型");
            writer.addHeaderAlias("merchantShowValue","平台建议设置值");
            writer.addHeaderAlias("supplementExplain","平台风控补充说明");
            writer.addHeaderAlias("recommendTimeStr","平台建议时间");
            writer.addHeaderAlias("statusStr","处理状态");
            writer.addHeaderAlias("merchantOperator","处理人");
            writer.addHeaderAlias("processTimeStr","处理时间");
            writer.addHeaderAlias("merchantRemark","处理说明");

//            writer.addHeaderAlias("userId","用户ID");
//            writer.addHeaderAlias("typeStr","风控类型");
//            writer.addHeaderAlias("statusStr","操作");
//            writer.addHeaderAlias("supplementExplain","补充说明");
//            writer.addHeaderAlias("merchantOperator","商户处理人");

            writer.setColumnWidth(-1,20);
            writer.setColumnWidth(3,40);

            writer.setRowHeight(-1,50);
            writer.setOnlyAlias(true);

            writer.write(respVOList,true);
            httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
            httpServletResponse.setHeader("Content-Disposition",
                    "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
            ServletOutputStream out = httpServletResponse.getOutputStream();
            writer.flush(out,true);
            writer.close();
            IoUtil.close(out);

        } catch (Exception e) {
            log.error("商户管控记录表导出异常", e);
        }
    }

    @Override
    public Response<?> importUserRiskControlList(HttpServletRequest httpServletRequest, MultipartFile multipartFile) {
        if (multipartFile == null){
            return null;
        }
        try {
            ExcelReader reader = ExcelUtil.getReader(multipartFile.getInputStream(),0,true);

            reader.addHeaderAlias("用户ID","userId");
            reader.addHeaderAlias("风控类型","type");
            reader.addHeaderAlias("操作","status");
            reader.addHeaderAlias("补充说明","supplementExplain");
            reader.addHeaderAlias("商户处理人","merchantOperator");
            //读取全部数据
            List<RiskMerchantImportUpdateVo> importExcelReqVOList = reader.readAll(RiskMerchantImportUpdateVo.class);
            Map response = userLimitApiService.importUserRiskControlList(importExcelReqVOList);
            if (ObjectUtil.isNull(response)){
                Response.returnFail("风控接口/riskMerchantManager/batchUpdateStatus返回为空！");
            }
            Integer successStatus = 200;
            String successCode = "code";
            String successFlag = "data";
            if (!Objects.equals(successStatus,response.get(successCode)) || !Objects.equals(true,response.get(successFlag))){
                return Response.returnFail(response.get("msg").toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Response.returnSuccess();
    }

    @Override
    public Response<?> pendingCount() {
        JwtUser user = SecurityUtils.getUser();

        UserRiskControlPendingCountQueryDTO queryDTO = new UserRiskControlPendingCountQueryDTO();
        queryDTO.setMerchantCode(user.getMerchantCode());
        Map response = userLimitApiService.pendingCount(queryDTO);
        if (ObjectUtil.isNull(response)){
            Response.returnFail("风控接口/riskMerchantManager/pendingCount返回为空！");
        }
        Integer successStatus = 200;
        String successCode = "code";
        if (!Objects.equals(successStatus,response.get(successCode))){
            Response.returnFail(response.get("msg").toString());
        }
        return Response.returnSuccess(response.get("data"));
    }

    private void setStatusStr(UserRiskControlQueryAllRespVO queryAllRespVO) {
        //状态:0待处理,1同意,2拒绝,3强制执行
        switch (String.valueOf(queryAllRespVO.getStatus())){
            case "0":
                queryAllRespVO.setStatusStr("待处理");
                break;
            case "1":
                queryAllRespVO.setStatusStr("同意");
                break;
            case "2":
                queryAllRespVO.setStatusStr("拒绝");
                break;
            case "3":
                queryAllRespVO.setStatusStr("强制执行");
                break;
            default:
                queryAllRespVO.setStatusStr("无定义");
                break;
        }
    }

    private void setTypeStr(UserRiskControlQueryAllRespVO queryAllRespVO) {
        //风控类型,1.投注特征标签,2特殊限额,3特殊延时,4提前结算,5赔率分组,6投注特征预警变更标签,7定时任务自动化标签
        switch(String.valueOf(queryAllRespVO.getType())) {
            case "1":
                queryAllRespVO.setTypeStr("投注特征标签");
                break;
            case "2":
                queryAllRespVO.setTypeStr("特殊限额");
                break;
            case "3":
                queryAllRespVO.setTypeStr("特殊延时");
                break;
            case "4":
                queryAllRespVO.setTypeStr("提前结算");
                break;
            case "5":
                queryAllRespVO.setTypeStr("赔率分组");
                break;
            case "6":
                queryAllRespVO.setTypeStr("投注特征预警变更标签");
                break;
            case "7":
                queryAllRespVO.setTypeStr("定时任务自动化标签");
                break;
            default:
                queryAllRespVO.setTypeStr("无定义");
                break;
        }
    }
}
