package com.panda.sport.merchant.manage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.dto.MaintenanceRecordDTO;
import com.panda.sport.merchant.common.enums.NoticeLanguageEnum;
import com.panda.sport.merchant.common.enums.NoticeTypeEnum;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.merchant.MerchantNotice;
import com.panda.sport.merchant.common.po.merchant.MerchantNoticeType;
import com.panda.sport.merchant.common.po.merchant.NoticeLangContextPo;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.RegexUtils;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.BackendNoticeVo;
import com.panda.sport.merchant.common.vo.merchant.NoticeLangVo;
import com.panda.sport.merchant.manage.entity.form.MerchantNoticeForm;
import com.panda.sport.merchant.manage.entity.form.MerchantNoticeTypeForm;
import com.panda.sport.merchant.manage.entity.form.MerchantTreeForm;
import com.panda.sport.merchant.manage.service.MaintenancePlatformNoticeService;
import com.panda.sport.merchant.manage.service.impl.MerchantNoticeServiceImpl;
import com.panda.sport.merchant.manage.service.impl.MerchantNoticeTypeServiceImpl;
import com.panda.sport.merchant.manage.util.CommonUtil;
import com.panda.sports.auth.permission.AuthRequiredPermission;
import com.panda.sport.merchant.manage.service.impl.MerchantNoticeTypeServiceImpl;
import com.panda.sports.auth.util.SsoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author YK
 * @Description: 公告栏
 * @date 2020/3/12 15:02
 */
@RestController
@RequestMapping(value = "/manage/notice")
@Slf4j
public class MerchantNoticeController {

    @Autowired
    private MerchantNoticeServiceImpl merchantNoticeService;


    @Autowired
    private MerchantNoticeTypeServiceImpl merchantNoticeTypeService;

    @Resource
    private MaintenancePlatformNoticeService maintenancePlatformNoticeService;


    @PostMapping(value = "/getPop")
    public Response getPop() {

        QueryWrapper<MerchantNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.eq("is_pop", 1);
        queryWrapper.orderByDesc("id");
        List<MerchantNotice> merchantNoticeList = merchantNoticeService.list(queryWrapper);
        Map<String, Object> map = new HashMap<>();
        if (merchantNoticeList.size() > 0) {
            MerchantNotice merchantNotice = merchantNoticeList.get(0);
            map.put("id", merchantNotice.getId());
        } else {
            map.put("id", "");
        }
        return Response.returnSuccess(map);
    }


    @PostMapping(value = "/list")
    @AuthRequiredPermission("Merchant:Manage:notice:list")
    public Response list(
            @RequestParam(name = "title", defaultValue = "") String title,
            @RequestParam(name = "mid", defaultValue = "0") Long mid,
            @RequestParam(name = "createdBy",defaultValue = "") String createdBy,
            @RequestParam(name = "nid", defaultValue = "0") Integer nid,
            @RequestParam(name = "status", defaultValue = "-1") Integer status,
            @RequestParam(name = "pgNum", defaultValue = "1") Integer pgNum,
            @RequestParam(name = "pgSize", defaultValue = "20") Integer pgSize
    ) {

        PageHelper.startPage(pgNum, pgSize, true);
        QueryWrapper<MerchantNotice> queryWrapper = new QueryWrapper<>();
        if (mid > 0) {
            queryWrapper.eq("standard_match_id", mid);
        }
        if (nid > 0) {
            queryWrapper.eq("notice_type_id", nid);
        }
        if (!StringUtils.isEmpty(createdBy)) {
            queryWrapper.like("created_by",createdBy);
        }

        //0:草稿，1:已发布，2:取消发布
        if (status > -1) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.like("title", title);
        queryWrapper.orderByAsc("status");
        queryWrapper.orderByDesc("send_time");

        List<MerchantNotice> merchantNoticePoList = merchantNoticeService.list(queryWrapper);

        for (MerchantNotice merchantNotice : merchantNoticePoList) {
            merchantNotice.setContext(RegexUtils.delImgTag(merchantNotice.getContext()));
        }

        PageInfo<MerchantNotice> pageInfo = new PageInfo<>(merchantNoticePoList);
        return Response.returnSuccess(pageInfo);
    }


    /**
     * 获取类型公告类型
     *
     * @return
     */
    @PostMapping(value = "/noticeType")
    public Response noticeType(HttpServletRequest request) {

        List<MerchantNoticeType> merchantNoticePoList = merchantNoticeTypeService.list();
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        if (language.equalsIgnoreCase(Constant.LANGUAGE_ENGLISH)) {
            for (MerchantNoticeType type : merchantNoticePoList) {
                type.setTypeName(type.getTypeEn());
            }
        }
        return Response.returnSuccess(merchantNoticePoList);
    }


    /**
     * 语言类型
     * @return
     */
    @PostMapping(value = "/langType")
    public Response langType() {

        List<NoticeLangVo> noticeLangVos = new ArrayList<>();
        for (NoticeLanguageEnum noticeLanguageEnum : NoticeLanguageEnum.values() ) {
            NoticeLangVo noticeLangVo = new NoticeLangVo();
            noticeLangVo.setId(noticeLanguageEnum.getKey());
            noticeLangVo.setType(noticeLanguageEnum.getValue());
            noticeLangVo.setEnType(noticeLanguageEnum.getEnValue());
            noticeLangVos.add(noticeLangVo);
        }
        return  Response.returnSuccess(noticeLangVos);
    }



    @PostMapping(value = "/add")
    @AuthRequiredPermission("Merchant:Manage:notice:add")
    public Response add(HttpServletRequest request, @RequestBody @Valid MerchantNoticeForm merchantNoticeForm, BindingResult bindingResult) {
        log.info("/manage/notice/add : {}", merchantNoticeForm);
        if (StringUtils.isEmpty(merchantNoticeForm)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if (bindingResult.hasErrors()) {
            return Response.returnFail(bindingResult.getFieldError().getDefaultMessage());
        }
        // 如果置顶和轮播
        if (merchantNoticeForm.getIsTop() != null && merchantNoticeForm.getIsShuf() != null) {
            if (merchantNoticeForm.getIsTop() == merchantNoticeForm.getIsShuf()) {
                return Response.returnFail("请勿同时启用置顶和轮播功能");
            }
        }
        //2418 需求
        if(!NoticeTypeEnum.ABNORMAL_EVENT_USER.getCode().equals(merchantNoticeForm.getNoticeTypeId())) {
            if(merchantNoticeForm.getIsPop() == null) {
                return Response.returnFail("登录弹出不能为空");
            }
            if(merchantNoticeForm.getBeginTime() == null) {
                return Response.returnFail("生效开始时间不能为空");
            }
            if(merchantNoticeForm.getEndTime() == null) {
                return Response.returnFail("生效结束时间不能为空");
            }
        }else {
            merchantNoticeService.setNoticeDefaultValue(merchantNoticeForm);
        }
        if (!StringUtils.isEmpty(merchantNoticeForm.getStandardMatchId())) {
            if (!CommonUtil.isInteger(merchantNoticeForm.getStandardMatchId())) {
                log.error("赛事ID填写错误,ID:【{}】",merchantNoticeForm.getStandardMatchId());
                return Response.returnFail("赛事ID填写错误");
            }
        }
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        return merchantNoticeService.addNotice(merchantNoticeForm, SsoUtil.getUserId(request), language,IPUtils.getIpAddr(request)
        );
    }


    @PostMapping(value = "/editPost")
    @AuthRequiredPermission("Merchant:Manage:notice:update")
    public Response editPost(HttpServletRequest request,@RequestBody @Valid MerchantNoticeForm merchantNoticeForm, BindingResult bindingResult) {

        if (StringUtils.isEmpty(merchantNoticeForm)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if (StringUtils.isEmpty(merchantNoticeForm.getId())) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if (bindingResult.hasErrors()) {
            return Response.returnFail(bindingResult.getFieldError().getDefaultMessage());
        }
        //2418 需求
        if(!NoticeTypeEnum.ABNORMAL_EVENT_USER.getCode().equals(merchantNoticeForm.getNoticeTypeId())) {
            if(merchantNoticeForm.getIsPop() == null) {
                return Response.returnFail("登录弹出不能为空");
            }
            if(merchantNoticeForm.getBeginTime() == null) {
                return Response.returnFail("生效开始时间不能为空");
            }
            if(merchantNoticeForm.getEndTime() == null) {
                return Response.returnFail("生效结束时间不能为空");
            }
        }else {
            merchantNoticeService.setNoticeDefaultValue(merchantNoticeForm);
        }

        // 如果置顶和轮播
        if (merchantNoticeForm.getIsTop() != null && merchantNoticeForm.getIsShuf() != null) {
            if (merchantNoticeForm.getIsTop() == merchantNoticeForm.getIsShuf()) {
                return Response.returnFail("请勿同时启用置顶和轮播功能");
            }
        }

        if (!StringUtils.isEmpty(merchantNoticeForm.getStandardMatchId())) {
            if (!CommonUtil.isInteger(merchantNoticeForm.getStandardMatchId())) {
                return Response.returnFail("赛事ID填写错误");
            }
        }

        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        return merchantNoticeService.editNotice(merchantNoticeForm, SsoUtil.getUserId(request), language,IPUtils.getIpAddr(request));
    }


    @PostMapping(value = "/delete")
    @AuthRequiredPermission("Merchant:Manage:notice:delete")
    public Response delete(HttpServletRequest request, Integer id) {

        if (StringUtils.isEmpty(id)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        return merchantNoticeService.deleteNotice(id, SsoUtil.getUserId(request), language,IPUtils.getIpAddr(request));
    }

    /**
     * 取消公告
     *
     * @param id
     * @return
     */
    @PostMapping(value = "/cacelNotice")
    @AuthRequiredPermission("Merchant:Manage:notice:cacelNotice")
    public Response cacelNotice(HttpServletRequest request, Integer id) {

        if (StringUtils.isEmpty(id)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        return merchantNoticeService.cacelNotice(id, SsoUtil.getUserId(request), language, IPUtils.getIpAddr(request));
    }

    /**
     * 恢复公告
     *
     * @param id
     * @return
     */
    @PostMapping("/backNotice")
    @AuthRequiredPermission("Merchant:Manage:notice:backNotice")
    public Response backNotice(HttpServletRequest request, Integer id) {

        if (StringUtils.isEmpty(id)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        return merchantNoticeService.backNotice(id, SsoUtil.getUserId(request), language,IPUtils.getIpAddr(request));
    }


    /**
     * 编辑
     *
     * @return
     */
    @PostMapping(value = "/findById")
    @AuthRequiredPermission("Merchant:Manage:notice:edit")
    public Response findById(Integer id) {

        if (StringUtils.isEmpty(id)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        MerchantNotice merchantNoticePo = merchantNoticeService.getById(id);
        BackendNoticeVo backendNoticeVo = new BackendNoticeVo();

        BeanUtils.copyProperties(merchantNoticePo, backendNoticeVo);
        if (!StringUtils.isEmpty(merchantNoticePo.getMerchantCodes())) {
            backendNoticeVo.setMerchantCodes(merchantNoticePo.getMerchantCodes().split(","));
        }

        backendNoticeVo = getNoticeList(backendNoticeVo,merchantNoticePo);

        return Response.returnSuccess(backendNoticeVo);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @PostMapping("/detail")
    @AuthRequiredPermission("Merchant:Manage:notice:detail")
    public Response detail(Integer id) {

        if (StringUtils.isEmpty(id)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        MerchantNotice merchantNoticePo = merchantNoticeService.getById(id);
        if (StringUtils.isEmpty(merchantNoticePo)) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        MerchantNotice merchantNoticePoUpdate = new MerchantNotice();
        merchantNoticePoUpdate.setId(merchantNoticePo.getId());
        merchantNoticePoUpdate.setVisitNumber(merchantNoticePo.getVisitNumber() + 1);
        merchantNoticePoUpdate.setModifyTime(System.currentTimeMillis());

        // 输出
        BackendNoticeVo backendNoticeVo = new BackendNoticeVo();
        BeanUtils.copyProperties(merchantNoticePo, backendNoticeVo);
        if (!StringUtils.isEmpty(merchantNoticePo.getMerchantCodes())) {
            backendNoticeVo.setMerchantCodes(merchantNoticePo.getMerchantCodes().split(","));
        }
        backendNoticeVo = getNoticeList(backendNoticeVo,merchantNoticePo);
        try {
            merchantNoticeService.updateById(merchantNoticePoUpdate);
            return Response.returnSuccess(backendNoticeVo);
        } catch (Exception e) {
            return Response.returnFail(e.getMessage());
        }
    }


    public BackendNoticeVo getNoticeList(BackendNoticeVo backendNoticeVo,MerchantNotice merchantNotice) {

        List<NoticeLangContextPo> list = new ArrayList<>();
        for (NoticeLanguageEnum noticeLanguageEnum : NoticeLanguageEnum.values()) {
            NoticeLangContextPo noticeLangContextPo = new NoticeLangContextPo();
            if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.ZS.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.ZS.getKey());
                noticeLangContextPo.setTitle(merchantNotice.getTitle());
                noticeLangContextPo.setContext(merchantNotice.getContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.EN.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.EN.getKey());
                noticeLangContextPo.setTitle(merchantNotice.getEnTitle());
                noticeLangContextPo.setContext(merchantNotice.getEnContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.ZH.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.ZH.getKey());
                noticeLangContextPo.setTitle(merchantNotice.getZhTitle());
                noticeLangContextPo.setContext(merchantNotice.getZhContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.VI.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.VI.getKey());
                noticeLangContextPo.setTitle(merchantNotice.getViTitle());
                noticeLangContextPo.setContext(merchantNotice.getViContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.JP.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.JP.getKey());
                noticeLangContextPo.setTitle(StringUtils.isEmpty(merchantNotice.getJpTitle())?merchantNotice.getEnTitle():merchantNotice.getJpTitle());
                noticeLangContextPo.setContext(StringUtils.isEmpty(merchantNotice.getJpContext())?merchantNotice.getEnContext():merchantNotice.getJpContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.TH.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.TH.getKey());
                noticeLangContextPo.setTitle(StringUtils.isEmpty(merchantNotice.getThTitle())?merchantNotice.getEnTitle():merchantNotice.getThTitle());
                noticeLangContextPo.setContext(StringUtils.isEmpty(merchantNotice.getThContext())?merchantNotice.getEnContext():merchantNotice.getThContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.MA.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.MA.getKey());
                noticeLangContextPo.setTitle(StringUtils.isEmpty(merchantNotice.getMaTitle())?merchantNotice.getEnTitle():merchantNotice.getMaTitle());
                noticeLangContextPo.setContext(StringUtils.isEmpty(merchantNotice.getMaContext())?merchantNotice.getEnContext():merchantNotice.getMaContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.BI.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.BI.getKey());
                noticeLangContextPo.setTitle(merchantNotice.getEnTitle());
                noticeLangContextPo.setContext(merchantNotice.getEnContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.KO.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.KO.getKey());
                noticeLangContextPo.setTitle(StringUtils.isEmpty(merchantNotice.getKoTitle())?merchantNotice.getEnTitle():merchantNotice.getKoTitle());
                noticeLangContextPo.setContext(StringUtils.isEmpty(merchantNotice.getKoContext())?merchantNotice.getEnContext():merchantNotice.getKoContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.PT.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.PT.getKey());
                noticeLangContextPo.setTitle(StringUtils.isEmpty(merchantNotice.getPtTitle())?merchantNotice.getEnTitle():merchantNotice.getPtTitle());
                noticeLangContextPo.setContext(StringUtils.isEmpty(merchantNotice.getPtContext())?merchantNotice.getEnContext():merchantNotice.getPtContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.ES.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.ES.getKey());
                noticeLangContextPo.setTitle(StringUtils.isEmpty(merchantNotice.getEsTitle())?merchantNotice.getEnTitle():merchantNotice.getEsTitle());
                noticeLangContextPo.setContext(StringUtils.isEmpty(merchantNotice.getEsContext())?merchantNotice.getEnContext():merchantNotice.getEsContext());
            } else if (noticeLanguageEnum.getKey().equals(NoticeLanguageEnum.MYA.getKey())) {
                noticeLangContextPo.setLangType(NoticeLanguageEnum.MYA.getKey());
                noticeLangContextPo.setTitle(StringUtils.isEmpty(merchantNotice.getMyaTitle())?merchantNotice.getEnTitle():merchantNotice.getMyaTitle());
                noticeLangContextPo.setContext(StringUtils.isEmpty(merchantNotice.getMyaContext())?merchantNotice.getEnContext():merchantNotice.getMyaContext());
            } else{
                // 未兼容语言 统统展示英语
                noticeLangContextPo.setLangType(noticeLanguageEnum.getKey());
                noticeLangContextPo.setTitle(merchantNotice.getEnTitle());
                noticeLangContextPo.setContext(merchantNotice.getEnContext());
            }
            list.add(noticeLangContextPo);
        }
        backendNoticeVo.setList(list);

        // 返回
        return backendNoticeVo;
    }

    /**
     * 公告类型
     *
     * @param typeName
     * @param pgNum
     * @param pgSize
     * @return
     */
    @PostMapping("/typeList")
    public Response typeList(
            @RequestParam(name = "typeName", defaultValue = "") String typeName,
            @RequestParam(name = "pgNum", defaultValue = "1") Integer pgNum,
            @RequestParam(name = "pgSize", defaultValue = "20") Integer pgSize
    ) {

        PageHelper.startPage(pgNum, pgSize, true);
        QueryWrapper<MerchantNoticeType> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(typeName)) {
            queryWrapper.like("type_name", typeName);
        }
        List<MerchantNoticeType> merchantNoticePoList = merchantNoticeTypeService.list(queryWrapper);
        PageInfo<MerchantNoticeType> pageInfo = new PageInfo<>(merchantNoticePoList);
        return Response.returnSuccess(pageInfo);
    }

    /**
     * 公告类型删除
     *
     * @param id
     * @return
     */
    @PostMapping("/typeDelete")
    public Response typeDelete(Integer id) {

        if (StringUtils.isEmpty(id)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        boolean isDelete = merchantNoticeTypeService.removeById(id);
        if (isDelete) {
            return Response.returnSuccess();
        }
        return Response.returnFail("删除失败");
    }


    /**
     * 公告类型的添加
     *
     * @param merchantNoticeTypeForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/typeAdd")
    public Response typeAdd(@Valid MerchantNoticeTypeForm merchantNoticeTypeForm, BindingResult bindingResult) {

        if (StringUtils.isEmpty(merchantNoticeTypeForm)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if (bindingResult.hasErrors()) {
            return Response.returnFail(bindingResult.getFieldError().getDefaultMessage());
        }
        MerchantNoticeType merchantNoticeType = new MerchantNoticeType();
        BeanUtils.copyProperties(merchantNoticeTypeForm, merchantNoticeType);
        Boolean isInsert = merchantNoticeTypeService.save(merchantNoticeType);
        if (isInsert) {
            return Response.returnSuccess();
        }
        return Response.returnFail("添加失败");
    }


    /**
     * 编辑
     *
     * @param merchantNoticeTypeForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/typeEdit")
    public Response typeEdit(@Valid MerchantNoticeTypeForm merchantNoticeTypeForm, BindingResult bindingResult) {

        if (StringUtils.isEmpty(merchantNoticeTypeForm)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if (StringUtils.isEmpty(merchantNoticeTypeForm.getId())) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if (bindingResult.hasErrors()) {
            return Response.returnFail(bindingResult.getFieldError().getDefaultMessage());
        }
        MerchantNoticeType merchantNoticeType = new MerchantNoticeType();
        BeanUtils.copyProperties(merchantNoticeTypeForm, merchantNoticeType);
        try {
            merchantNoticeTypeService.updateById(merchantNoticeType);
            return Response.returnSuccess();
        } catch (Exception e) {
            return Response.returnFail(e.getMessage());
        }
    }


    /**
     * 获取商户的树
     *
     * @return
     */
    @PostMapping("/getMerchantTree")
    public Response getMerchantTree(@RequestBody @Valid MerchantTreeForm merchantTreeForm, BindingResult bindingResult) {

        if (StringUtils.isEmpty(merchantTreeForm)) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if (StringUtils.isEmpty(merchantTreeForm.getTag())) {
            merchantTreeForm.setTag(0);
            //return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        if (StringUtils.isEmpty(merchantTreeForm.getName())) {
            //return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        return merchantNoticeService.getMerchantTree(merchantTreeForm.getTag(), merchantTreeForm.getName());
    }

    @PostMapping("/sendStartMaintainRemindAndNoticeToSport")
    public void sendStartMaintainRemindAndNoticeToSport(@RequestBody @Valid MaintenanceRecordDTO maintenanceRecordDTO) {
       maintenancePlatformNoticeService.sendStartMaintainRemindAndNoticeToSport(maintenanceRecordDTO);
    }
}
