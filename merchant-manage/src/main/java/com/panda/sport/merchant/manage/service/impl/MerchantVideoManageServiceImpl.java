package com.panda.sport.merchant.manage.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.sport.bss.mapper.MerchantVideoManageMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.MerchantVideoManageVo;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.IMerchantVideoManageService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
@RefreshScope
public class MerchantVideoManageServiceImpl implements IMerchantVideoManageService {

    @Autowired
    private MerchantVideoManageMapper videoManageMapper;

    @Autowired
    private MerchantLogService merchantLogService;

    /**
     * 视频设置(0默认,1自定义)
     */
    private final static Integer VIDEO_SETTINGS = 0;

    /**
     * 默认视频观看时长设置(15分钟)
     */
    private final static Integer VIEWING_TIME = 15;

    /**
     * 视频流量管控开关值
     */
    private final static Integer SWITCH_VALUE = 0;

    @Override
    public int saveMerchantVideoManage(MerchantVideoManageVo videoManageVo) {
        if(videoManageVo.getVideoSettings() != null && videoManageVo.getVideoSettings().equals(VIDEO_SETTINGS)){
            videoManageVo.setViewingTime(VIEWING_TIME);
        }
        videoManageMapper.updateMerchantVideoManage(videoManageVo);
        return 0;
    }

    @Override
    public MerchantVideoManageVo getMerchantVideoManage(String merchantCode) {
        if(StringUtils.isEmpty(merchantCode)){
            return null;
        }
        MerchantVideoManageVo manageVo = videoManageMapper.queryMerchantVideoManage(merchantCode);
        return manageVo;
    }

    @Override
    public Response getVideoManageList(MerchantVideoManageVo videoManageVo) {
        Integer pageIndex = videoManageVo.getPageIndex();
        Integer pageSize = videoManageVo.getPageSize();
        String sort = videoManageVo.getSort();
        String orderBy = videoManageVo.getOrderBy();
        pageSize = (pageSize == null || pageSize == 0) ? 100 : pageSize;
        pageIndex = pageIndex == null ? 1 : pageIndex;
        sort = StringUtils.isEmpty(sort) ? Constant.DESC : sort;
        orderBy = StringUtils.isEmpty(orderBy) ? "t.update_time" : orderBy;
        String orderStr = orderBy + " " + sort;
        if ("".equals(orderBy)) {
            orderStr = "t.update_time desc";
        }
        PageHelper.startPage(pageIndex, pageSize, orderStr);
        PageInfo<MerchantVideoManageVo> pageInfo = new PageInfo<>(videoManageMapper.getList(videoManageVo));
        return Response.returnSuccess(pageInfo);
    }

    @Override
    public int batchUpdateMerchantVideoManage(MerchantVideoManageVo videoManageVo) {
        int num = 0;
        if(videoManageVo != null){
            if(videoManageVo.getVideoSettings() != null && videoManageVo.getVideoSettings().equals(VIDEO_SETTINGS)){
                videoManageVo.setViewingTime(VIEWING_TIME);
            }
            num = videoManageMapper.batchUpdateMerchantVideoManage(videoManageVo);
        }
        return num;
    }

    @Override
    public Response getMerchantVideoManageList() {
        List<MerchantVideoManageVo> list = Lists.newArrayList();
        try {
            list = videoManageMapper.getVideoManageList();
        }catch (Exception e){
            log.error("查询视频流量管控配置失败!");
        }
        return Response.returnSuccess(list);
    }

    @Override
    public int updateMerchantVideoManage(HttpServletRequest request,MerchantVideoManageVo videoManageVo) {
        if(videoManageVo.getVideoSettings() != null && videoManageVo.getVideoSettings().equals(VIDEO_SETTINGS)){
            videoManageVo.setViewingTime(VIEWING_TIME);
        }
        //长时间未操作暂停视频开关/不可背景播放开关不可同时关闭
        if(videoManageVo.getClosedWithoutOperation().equals(SWITCH_VALUE) && videoManageVo.getNoBackgroundPlay().equals(SWITCH_VALUE)){
            throw new RuntimeException("长时间未操作暂停视频开关和不可背景播放开关不可同时关闭!");
        }
        MerchantVideoManageVo merchantVideoManageVo = videoManageMapper.queryMerchantVideoManage(videoManageVo.getMerchantCode());
        int num = videoManageMapper.updateMerchantVideoManage(videoManageVo);

        /**
         *  添加系统日志
         * */
        String userId = request.getHeader("user-id");
        String username = request.getHeader("merchantName");
        String ip = IPUtils.getIpAddr(request);
        String language = request.getHeader("language");
        MerchantLogFiledVO vo = new MerchantLogFiledVO();
        List<String>  filedNames  = new ArrayList<>();
        filedNames.add("长时间未操作暂停视频");
        filedNames.add("默认/自定义");
        filedNames.add("观看时间设置");
        filedNames.add("不可背景播放");
        List<String>  bdf  = new ArrayList<>();
        bdf.add(0 == merchantVideoManageVo.getClosedWithoutOperation() ?"关":"开" );
        bdf.add(0 == merchantVideoManageVo.getVideoSettings() ?"默认":"自定义" );
        bdf.add(merchantVideoManageVo.getCustomViewTime().toString());
        bdf.add(0 == merchantVideoManageVo.getNoBackgroundPlay()  ?"关":"开");
        List<String>  aft  = new ArrayList<>();
        aft.add(0 == videoManageVo.getClosedWithoutOperation() ?"关":"开" );
        aft.add(0 == videoManageVo.getVideoSettings() ?"默认":"自定义" );
        aft.add(videoManageVo.getCustomViewTime().toString());
        aft.add(0 == videoManageVo.getNoBackgroundPlay()  ?"关":"开");
        vo.setBeforeValues(bdf);
        vo.setAfterValues(aft);
        vo.setFieldName(filedNames);
        merchantLogService.saveLog(MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_0, MerchantLogTypeEnum.EDIT_INFO, vo,
                MerchantLogConstants.MERCHANT_IN, userId, username, null, username, userId, language, ip);
        return num;
    }
}
