package com.panda.sport.merchant.manage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.bss.mapper.MerchantMapper;
import com.panda.sport.bss.mapper.SMatchInfoMapper;
import com.panda.sport.bss.mapper.TOrderDetailMapper;
import com.panda.sport.match.mapper.TMatchNoticeMapper;
import com.panda.sport.merchant.common.constant.CommonDefaultValue;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.*;
import com.panda.sport.merchant.common.po.bss.MatchInfoPO;
import com.panda.sport.merchant.common.po.bss.OrderDetailPO;
import com.panda.sport.merchant.common.po.bss.SLanguagePO;
import com.panda.sport.merchant.common.po.match.TMatchNotice;
import com.panda.sport.merchant.common.po.merchant.MerchantNotice;
import com.panda.sport.merchant.common.po.merchant.NoticeLangContextPo;
import com.panda.sport.merchant.common.po.merchant.mq.MarketOptionsResultPO;
import com.panda.sport.merchant.common.po.merchant.mq.MarketResultMessagePO;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.utils.RegexUtils;
import com.panda.sport.merchant.common.utils.SportToESportUtil;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.manage.entity.form.MerchantNoticeForm;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.service.MerchantNoticeService;
import com.panda.sport.merchant.manage.util.CommonUtil;
import com.panda.sport.merchant.mapper.MerchantNoticeMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author YK
 * @Description:我的公告
 * @date 2020/3/12 17:07
 */
@Slf4j
@Service
@RefreshScope
public class MerchantNoticeServiceImpl extends ServiceImpl<MerchantNoticeMapper, MerchantNotice> implements MerchantNoticeService {

    @Autowired
    private SMatchInfoMapper sMatchInfoMapper;

    @Autowired
    private TMatchNoticeMapper tMatchNoticeMapper;

    @Autowired
    private TOrderDetailMapper tOrderDetailMapper;

    @Autowired
    private SLanguageServiceImpl sLanguageService;

    @Autowired
    private MerchantNoticeMapper merchantNoticeMapper;

    @Autowired
    private MerchantLogService merchantLogService;

    @Autowired
    private LoginUserService loginUserService;

    @Autowired
    private MerchantMapper merchantMapper;

    //"http://www.phiqui.com/"
    @Value("${merchant.sport_notice_to_esport.url}")
    private String sportNoticeToESportUrl;

    //5a49502d69594196622c5b3a981b5208
    @Value("${merchant.sport_notice_to_esport.key}")
    private String sportNoticeToESportKey;

    //31433517168705439
    @Value("${merchant.sport_notice_to_esport.merchant_id}")
    private String sportNoticeToESportMerchantId;


    // 电竞方语言类型
    private static final int zs = 1;
    private static final int zh = 2;
    private static final int en = 3;
    private static final int vi = 4;
    private static final int th = 5;
    private static final int ma = 6;
    private static final int ko = 7;
    private static final int pt = 8;
    private static final int es = 9;
    private static final int mya = 10;

    private static List<Integer> eSportNoticeTypeList = new ArrayList<>(10);

    static {
        eSportNoticeTypeList.add(NoticeTypeEnum.LOL.getCode());
        eSportNoticeTypeList.add(NoticeTypeEnum.DOTA2.getCode());
        eSportNoticeTypeList.add(NoticeTypeEnum.KoG.getCode());
        eSportNoticeTypeList.add(NoticeTypeEnum.CS_GO.getCode());
    }

    /**
     * 添加公告
     *
     * @param merchantNoticeForm
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response addNotice(MerchantNoticeForm merchantNoticeForm, Integer userId ,String language,String ip) {

        Long time = System.currentTimeMillis();
        String merchantCodes = merchantNoticeForm.getMids() != null ?  merchantNoticeForm.getMids() : "";
        Integer isFull = merchantNoticeForm.getIsFull() == null ? 0 :  merchantNoticeForm.getIsFull() ? 1 : 0;
        Integer isTop = merchantNoticeForm.getIsTop() == null ? 0 : merchantNoticeForm.getIsTop();
        Integer isShuf = merchantNoticeForm.getIsShuf() == null ? 0 : merchantNoticeForm.getIsShuf();

        // 赋值给添加公告
        MerchantNotice merchantNoticePo = new MerchantNotice();
        BeanUtils.copyProperties(merchantNoticeForm, merchantNoticePo);

        merchantNoticePo.setMerchantCodes(merchantCodes);
        merchantNoticePo.setIsFull(isFull);
        merchantNoticePo.setUpdatedBy(merchantNoticeForm.getCreatedBy());
        merchantNoticePo.setSendTime(time);
        merchantNoticePo.setModifyTime(time);
        merchantNoticePo.setCreateTime(time);
        merchantNoticePo.setBeginTime(merchantNoticeForm.getBeginTime());
        merchantNoticePo.setEndTime(merchantNoticeForm.getEndTime());
        merchantNoticePo.setIsTop(isTop);
        merchantNoticePo.setIsShuf(isShuf);
        if (merchantNoticeForm.getIsPublish()) {
            merchantNoticePo.setSendTime(System.currentTimeMillis());
            merchantNoticePo.setStatus(1);
        }

        // 商户公告
        merchantNoticePo = getMatchNotice(merchantNoticePo,merchantNoticeForm);

//        if (!eSportNoticeTypeList.contains(merchantNoticeForm.getNoticeTypeId()) && !StringUtils.isEmpty(merchantNoticeForm.getStandardMatchId())) {
//            MatchInfoPO matchInfo = sMatchInfoMapper.getMatchInfoByMid(Long.valueOf(merchantNoticeForm.getStandardMatchId()));
//            if (StringUtils.isEmpty(matchInfo)) {
//                return Response.returnFail("赛事ID填写错误");
//            }
//        }

        try {
            // 如果包含投注用户
            Long tMatchNoticeId = 0L;
            String releaseRange = merchantNoticePo.getReleaseRange();
            if ( releaseRange.contains(String.valueOf(NoticeReleaseEnum.BET_USER.getCode()))
                    || releaseRange.contains(String.valueOf(NoticeReleaseEnum.ALL_MERCHANT_USER.getCode()))
                    || releaseRange.contains(String.valueOf(NoticeReleaseEnum.SOME_MERCHANT_USER.getCode()))
            ) {
                int noticeType = CommonUtil.getMatchNoticeType(merchantNoticeForm.getNoticeTypeId());
                // 如果有公告类型就插入到赛事表的公告中
                if (noticeType > 0) {
                    // 发布范围
                    String bssReleaseRange;
                    if (releaseRange.contains(String.valueOf(NoticeReleaseEnum.SOME_MERCHANT_USER.getCode())) ) {
                        bssReleaseRange = String.valueOf(NoticeReleaseEnum.SOME_MERCHANT_USER.getCode());
                    } else {
                        bssReleaseRange = String.valueOf(NoticeReleaseEnum.ALL_MERCHANT_USER.getCode());
                    }

                    TMatchNotice tMatchNotice = new TMatchNotice();
                    tMatchNotice.setNoticeType(noticeType).setReleaseRange(bssReleaseRange)
                            .setMerchantCodes(merchantCodes).setIsFull(isFull)
                            .setTitle(merchantNoticePo.getTitle()).setContext(RegexUtils.delHtmlTag(merchantNoticePo.getContext()))
                            .setEnTitle(merchantNoticePo.getEnTitle()).setEnContext(RegexUtils.delHtmlTag(merchantNoticePo.getEnContext()))
                            .setZhTitle(merchantNoticePo.getZhTitle()).setZhContext(RegexUtils.delHtmlTag(merchantNoticePo.getZhContext()))
                            .setViTitle(merchantNoticePo.getViTitle()).setViContext(RegexUtils.delHtmlTag(merchantNoticePo.getViContext()))
                            .setJpTitle(merchantNoticePo.getJpTitle()).setJpContext(RegexUtils.delHtmlTag(merchantNoticePo.getJpContext()))
                            .setThTitle(merchantNoticePo.getThTitle()).setThContext(RegexUtils.delHtmlTag(merchantNoticePo.getThContext()))
                            .setMaTitle(merchantNoticePo.getMaTitle()).setMaContext(RegexUtils.delHtmlTag(merchantNoticePo.getMaContext()))
                            .setBiTitle(merchantNoticePo.getEnTitle()).setBiContext(RegexUtils.delHtmlTag(merchantNoticePo.getEnContext()))
                            .setKoTitle(merchantNoticePo.getKoTitle()).setKoContext(RegexUtils.delHtmlTag(merchantNoticePo.getKoContext()))
                            .setPtTitle(merchantNoticePo.getPtTitle()).setPtContext(RegexUtils.delHtmlTag(merchantNoticePo.getPtContext()))
                            .setEsTitle(merchantNoticePo.getEsTitle()).setEsContext(RegexUtils.delHtmlTag(merchantNoticePo.getEsContext()))
                            .setMyaTitle(merchantNoticePo.getMyaTitle()).setMyaContext(RegexUtils.delHtmlTag(merchantNoticePo.getMyaContext()))
                            .setCreateTime(time).setSendTime(time).setModifyTime(time).setCreatedBy(merchantNoticePo.getCreatedBy())
                            .setBeginTime(merchantNoticeForm.getBeginTime())
                            .setEndTime(merchantNoticeForm.getEndTime())
                            .setIsTop(isTop).setIsShuf(isShuf);

                    if (merchantNoticeForm.getIsPublish()) {
                        // 如果发布
                        tMatchNotice.setSendTime(System.currentTimeMillis());
                        tMatchNotice.setStatus(1);
                    }
                    tMatchNoticeMapper.insert(tMatchNotice);
                    tMatchNoticeId = tMatchNotice.getId();
                }
            }
            // 插入商户后台的公告总表
            if (tMatchNoticeId > 0) {
                merchantNoticePo.setTMatchNoticeId(tMatchNoticeId);
            }
            save(merchantNoticePo);

            // 推送特定类型的公告给电竞(电竞只要已发布的) -- 添加
            boolean writeFlag = Objects.equals(CommonDefaultValue.ONE,merchantNoticePo.getStatus()) && (Objects.equals(NoticeTypeEnum.NBA2K.getCode(), CommonUtil.getMatchNoticeType(merchantNoticePo.getNoticeTypeId())) ||
                    Objects.equals(NoticeTypeEnum.FIFA.getCode(), CommonUtil.getMatchNoticeType(merchantNoticePo.getNoticeTypeId())));
            if (writeFlag){
                String apiName = "v1/notice/insert";
                String flag = "添加";
                divideAndSendSportNoticeToESport(merchantNoticePo, apiName, flag);
            }

            //添加日志
            String username = loginUserService.getLoginUser(userId);
            TMatchNoticeVO vo = new TMatchNoticeVO();
            BeanUtils.copyProperties(merchantNoticePo, vo);
            vo.setNoticeEndTime(merchantNoticePo.getEndTime());
            MerchantFieldUtil fieldUtil = new MerchantFieldUtil<TMatchNoticeVO>();
            MerchantLogFiledVO filedVO = fieldUtil.compareObject(new TMatchNoticeVO(), vo);
            merchantLogService.saveLog(MerchantLogPageEnum.MES_IN_CENT, MerchantLogTypeEnum.CENT_ADD, filedVO, MerchantLogConstants.MERCHANT_IN, userId.toString(),
                    username, null, merchantNoticePo.getTitle(), tMatchNoticeId.toString(),language, ip);
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("增加公告异常!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 设置公告的语言类型
     * @param merchantNoticePo
     * @param merchantNoticeForm
     * @return
     */
    private MerchantNotice getMatchNotice(MerchantNotice merchantNoticePo,MerchantNoticeForm merchantNoticeForm) {

        for (NoticeLangContextPo langContextPo : merchantNoticeForm.getList()) {

            if (NoticeLanguageEnum.EN.getKey().equals(langContextPo.getLangType())) {
                merchantNoticePo.setEnTitle(langContextPo.getTitle());
                merchantNoticePo.setEnContext(langContextPo.getContext());
                merchantNoticePo.setBiContext(langContextPo.getContext());
                merchantNoticePo.setBiTitle(langContextPo.getTitle());
            } else if (NoticeLanguageEnum.ZS.getKey().equals(langContextPo.getLangType())) {
                merchantNoticePo.setTitle(langContextPo.getTitle());
                merchantNoticePo.setContext(langContextPo.getContext());
            } else if (NoticeLanguageEnum.ZH.getKey().equals(langContextPo.getLangType())) {
                merchantNoticePo.setZhTitle(langContextPo.getTitle());
                merchantNoticePo.setZhContext(langContextPo.getContext());
            } else if (NoticeLanguageEnum.VI.getKey().equals(langContextPo.getLangType())) {
                merchantNoticePo.setViTitle(langContextPo.getTitle());
                merchantNoticePo.setViContext(langContextPo.getContext());
            }  else if (NoticeLanguageEnum.JP.getKey().equals(langContextPo.getLangType())) {
                merchantNoticePo.setJpContext(langContextPo.getContext());
                merchantNoticePo.setJpTitle(langContextPo.getTitle());
            }  else if (NoticeLanguageEnum.TH.getKey().equals(langContextPo.getLangType())) {
                merchantNoticePo.setThContext(langContextPo.getContext());
                merchantNoticePo.setThTitle(langContextPo.getTitle());
            }  else if (NoticeLanguageEnum.MA.getKey().equals(langContextPo.getLangType())) {
                merchantNoticePo.setMaContext(langContextPo.getContext());
                merchantNoticePo.setMaTitle(langContextPo.getTitle());
            }/*else if (NoticeLanguageEnum.BI.getKey().equals(langContextPo.getLangType())) {
                merchantNoticePo.setBiContext(langContextPo.getContext());
                merchantNoticePo.setBiTitle(langContextPo.getTitle());
            }*/else if (NoticeLanguageEnum.KO.getKey().equals(langContextPo.getLangType())) {
                merchantNoticePo.setKoContext(langContextPo.getContext());
                merchantNoticePo.setKoTitle(langContextPo.getTitle());
            }else if (NoticeLanguageEnum.PT.getKey().equals(langContextPo.getLangType())) {
                merchantNoticePo.setPtContext(langContextPo.getContext());
                merchantNoticePo.setPtTitle(langContextPo.getTitle());
            }else if (NoticeLanguageEnum.ES.getKey().equals(langContextPo.getLangType())) {
                merchantNoticePo.setEsContext(langContextPo.getContext());
                merchantNoticePo.setEsTitle(langContextPo.getTitle());
            }else if (NoticeLanguageEnum.MYA.getKey().equals(langContextPo.getLangType())) {
                merchantNoticePo.setMyaContext(langContextPo.getContext());
                merchantNoticePo.setMyaTitle(langContextPo.getTitle());
            }
        }
        return merchantNoticePo;
    }




    /**
     * 编辑公告
     *
     * @param merchantNoticeForm
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response editNotice(MerchantNoticeForm merchantNoticeForm, Integer userId,String language,String ip) {

        try {

            MerchantNotice merchantNoticePoResult = getById(merchantNoticeForm.getId());
            if (StringUtils.isEmpty(merchantNoticePoResult)) {
                return Response.returnFail("结果不存在");
            }
            Long currentTime = System.currentTimeMillis();
            String merchantCodes = merchantNoticeForm.getMids() != null ?  merchantNoticeForm.getMids() : "";
            Integer isFull = merchantNoticeForm.getIsFull() == null ? 0 :  merchantNoticeForm.getIsFull() ? 1 : 0;
            Integer isTop = merchantNoticeForm.getIsTop() == null ? 0 : merchantNoticeForm.getIsTop();
            Integer isShuf = merchantNoticeForm.getIsShuf() == null ? 0 : merchantNoticeForm.getIsShuf();

            //merchant的公告 赋值
            MerchantNotice merchantNoticePo = new MerchantNotice();
            BeanUtils.copyProperties(merchantNoticeForm, merchantNoticePo);
            merchantNoticePo.setUpdatedBy(merchantNoticeForm.getCreatedBy());
            merchantNoticePo.setModifyTime(currentTime);

            merchantNoticePo.setMerchantCodes(merchantCodes);
            merchantNoticePo.setIsFull(isFull);
            merchantNoticePo.setBeginTime(merchantNoticeForm.getBeginTime());
            merchantNoticePo.setEndTime(merchantNoticeForm.getEndTime());
            merchantNoticePo.setIsTop(isTop);
            merchantNoticePo.setIsShuf(isShuf);
            merchantNoticePo = getMatchNotice(merchantNoticePo,merchantNoticeForm);

            if (merchantNoticeForm.getIsPublish()) {
                merchantNoticePo.setStatus(1);
                merchantNoticePo.setSendTime(currentTime);
            } else {
                merchantNoticePo.setStatus(0);
            }
//            if (!eSportNoticeTypeList.contains(merchantNoticeForm.getNoticeTypeId()) && !StringUtils.isEmpty(merchantNoticeForm.getStandardMatchId())) {
//                MatchInfoPO matchInfo = sMatchInfoMapper.getMatchInfoByMid(Long.valueOf(merchantNoticeForm.getStandardMatchId()));
//                if (StringUtils.isEmpty(matchInfo)) {
//                    return Response.returnFail("赛事ID填写错误");
//                }
//            }
            Long tMatchNoticeId = null;

            String releaseRange = merchantNoticePo.getReleaseRange();

            // 如果提交的包含
            if ( releaseRange.contains(String.valueOf(NoticeReleaseEnum.BET_USER.getCode()))
                    || releaseRange.contains(String.valueOf(NoticeReleaseEnum.ALL_MERCHANT_USER.getCode()))
                    || releaseRange.contains(String.valueOf(NoticeReleaseEnum.SOME_MERCHANT_USER.getCode()))
            )
             {
                 //tybss_new的公告
                TMatchNotice tMatchNotice = new TMatchNotice();
                tMatchNotice.setModifyTime(currentTime);
                if (merchantNoticeForm.getIsPublish()) {
                    tMatchNotice.setStatus(1);
                    tMatchNotice.setSendTime(currentTime);
                } else {
                    tMatchNotice.setStatus(0);
                }

                //设置公告
                tMatchNotice.setTitle(merchantNoticePo.getTitle()).setContext(RegexUtils.delHtmlTag(merchantNoticePo.getContext()))
                             .setEnTitle(merchantNoticePo.getEnTitle()).setEnContext(RegexUtils.delHtmlTag(merchantNoticePo.getEnContext()))
                             .setZhTitle(merchantNoticePo.getZhTitle()).setZhContext(RegexUtils.delHtmlTag(merchantNoticePo.getZhContext()))
                             .setViTitle(merchantNoticePo.getViTitle()).setViContext(RegexUtils.delHtmlTag(merchantNoticePo.getViContext()))
                             .setJpTitle(merchantNoticePo.getJpTitle()).setJpContext(RegexUtils.delHtmlTag(merchantNoticePo.getJpContext()))
                             .setThTitle(merchantNoticePo.getThTitle()).setThContext(RegexUtils.delHtmlTag(merchantNoticePo.getThContext()))
                             .setMaTitle(merchantNoticePo.getMaTitle()).setMaContext(RegexUtils.delHtmlTag(merchantNoticePo.getMaContext()))
                             .setBiTitle(merchantNoticePo.getEnTitle()).setBiContext(RegexUtils.delHtmlTag(merchantNoticePo.getEnContext()))
                             .setKoTitle(merchantNoticePo.getKoTitle()).setKoContext(RegexUtils.delHtmlTag(merchantNoticePo.getKoContext()))
                             .setPtTitle(merchantNoticePo.getPtTitle()).setPtContext(RegexUtils.delHtmlTag(merchantNoticePo.getPtContext()))
                             .setEsTitle(merchantNoticePo.getEsTitle()).setEsContext(RegexUtils.delHtmlTag(merchantNoticePo.getEsContext()))
                             .setMyaTitle(merchantNoticePo.getMyaTitle()).setMyaContext(RegexUtils.delHtmlTag(merchantNoticePo.getMyaContext()))
                             .setSendTime(currentTime).setCreatedBy(merchantNoticePo.getCreatedBy())
                             .setBeginTime(merchantNoticeForm.getBeginTime())
                             .setEndTime(merchantNoticeForm.getEndTime())
                             .setIsTop(isTop).setIsShuf(isShuf);

                // 获取公告类型
                Integer noticeType = CommonUtil.getMatchNoticeType(merchantNoticeForm.getNoticeTypeId());

                if (noticeType > 0) {
                    // 公告类型
                    tMatchNotice.setNoticeType(noticeType);
                    tMatchNotice.setMerchantCodes(merchantCodes);
                    tMatchNotice.setIsFull(isFull);
                    // 发布范围
                    String bssReleaseRange;
                    if (releaseRange.contains(String.valueOf(NoticeReleaseEnum.SOME_MERCHANT_USER.getCode())) ) {
                        bssReleaseRange = String.valueOf(NoticeReleaseEnum.SOME_MERCHANT_USER.getCode());
                    } else {
                        bssReleaseRange = String.valueOf(NoticeReleaseEnum.ALL_MERCHANT_USER.getCode());
                    }

                    // 如果大于0
                    if (merchantNoticePoResult.getTMatchNoticeId() > 0) {
                        tMatchNoticeId = merchantNoticePoResult.getTMatchNoticeId();
                        tMatchNotice.setId(merchantNoticePoResult.getTMatchNoticeId());
                        // 更新
                        tMatchNoticeMapper.updateById(tMatchNotice);
                    } else {
                        tMatchNotice.setCreateTime(currentTime);
                        tMatchNotice.setSendTime(currentTime);
                        tMatchNoticeMapper.insert(tMatchNotice);
                        tMatchNoticeId = tMatchNotice.getId();
                    }
                }
            } else {

                // 如果以前包含 而 现在不包含
                if (merchantNoticePoResult.getReleaseRange().contains(String.valueOf(NoticeReleaseEnum.BET_USER.getCode()))
                        && merchantNoticePoResult.getTMatchNoticeId() > 0
                ) {
                    tMatchNoticeMapper.deleteById(merchantNoticePoResult.getTMatchNoticeId());
                    tMatchNoticeId = 0L;
                }
            }

            // 设置tMatchNoticeId的值
            if (tMatchNoticeId != null && tMatchNoticeId >= 0) {
                merchantNoticePo.setTMatchNoticeId(tMatchNoticeId);
            }
            // 更新 merchant_notice
            updateById(merchantNoticePo);
            TMatchNoticeVO vo1 = new TMatchNoticeVO();
            BeanUtils.copyProperties(merchantNoticePo, vo1);
            TMatchNoticeVO vo2 = new TMatchNoticeVO();
            BeanUtils.copyProperties(merchantNoticePoResult, vo2);

            // 推送特定类型的公告给电竞(电竞只要已发布的) -- 修改
            boolean writeFlag = Objects.equals(CommonDefaultValue.ONE,merchantNoticePo.getStatus()) && (Objects.equals(NoticeTypeEnum.NBA2K.getCode(), CommonUtil.getMatchNoticeType(merchantNoticePo.getNoticeTypeId())) ||
                    Objects.equals(NoticeTypeEnum.FIFA.getCode(), CommonUtil.getMatchNoticeType(merchantNoticePo.getNoticeTypeId())));
            if (writeFlag){
                SportNoticeToESportSendReqVO sendReqVO = new SportNoticeToESportSendReqVO();
                sendReqVO.setSid(merchantNoticePo.getId());
                String apiName = "v1/notice/delete";
                String flag = "修改-删除";
                handleSportNoticeToESport(sendReqVO, apiName, flag);

                apiName = "v1/notice/insert";
                flag = "修改-添加";

                divideAndSendSportNoticeToESport(merchantNoticePo, apiName, flag);
            }

            //添加日志
            String username = loginUserService.getLoginUser(userId);
            MerchantFieldUtil fieldUtil = new MerchantFieldUtil<TMatchNoticeVO>();
            MerchantLogFiledVO filedVO = fieldUtil.compareObject(vo2, vo1);
            merchantLogService.saveLog(MerchantLogPageEnum.MES_IN_CENT, MerchantLogTypeEnum.CENT_EDIT, filedVO, MerchantLogConstants.MERCHANT_IN, userId.toString(),
                    username, null, merchantNoticePo.getTitle(), merchantNoticePo.getId().toString(),language , ip);
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("编辑公告异常!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }

    }

    /**
     * 删除公告
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response deleteNotice(Integer id, Integer userId,String language,String ip) {
        try {
            MerchantNotice merchantNoticePoResult = getById(id);
            if (!StringUtils.isEmpty(merchantNoticePoResult)) {
                if (!StringUtils.isEmpty(merchantNoticePoResult.getTMatchNoticeId()) && merchantNoticePoResult.getTMatchNoticeId() > 0) {
                    tMatchNoticeMapper.deleteById(merchantNoticePoResult.getTMatchNoticeId());
                }
            }
            removeById(id);
            //添加日志
            MerchantLogTypeEnum logType = MerchantLogTypeEnum.CENT_DELETE;
            String username = loginUserService.getLoginUser(userId);
            if (merchantNoticePoResult.getStatus() == 1) {
                logType = MerchantLogTypeEnum.CENT_PUSH_DELETE;
            }

            // 推送特定类型的公告给电竞(电竞只要已发布的) -- 删除
            boolean writeFlag = Objects.equals(NoticeTypeEnum.NBA2K.getCode(), CommonUtil.getMatchNoticeType(merchantNoticePoResult.getNoticeTypeId())) ||
                    Objects.equals(NoticeTypeEnum.FIFA.getCode(), CommonUtil.getMatchNoticeType(merchantNoticePoResult.getNoticeTypeId()));
            if (writeFlag){
                SportNoticeToESportSendReqVO sendReqVO = new SportNoticeToESportSendReqVO();
                sendReqVO.setSid(merchantNoticePoResult.getId());

                String apiName = "v1/notice/delete";
                String flag = "删除";
                handleSportNoticeToESport(sendReqVO, apiName, flag);
            }
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            vo.getFieldName().add("删除公告");
            vo.getBeforeValues().add(id+"-"+merchantNoticePoResult.getTitle());
            vo.getAfterValues().add("-");
            merchantLogService.saveLog(MerchantLogPageEnum.MES_IN_CENT_DELETE, logType, vo, MerchantLogConstants.MERCHANT_IN, userId.toString(),
                    username, null,null , id.toString(),language,ip);
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("删除公告异常!", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 取消发布公告
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response cacelNotice(Integer id, Integer userId,String language,String ip) {
        try {
            MerchantNotice merchantNoticePoResult = getById(id);
            if (StringUtils.isEmpty(merchantNoticePoResult)) {
                return Response.returnFail("结果不存在");
            }
            Long currentTime = System.currentTimeMillis();

            //取消状态
            MerchantNotice merchantNotice = new MerchantNotice();
            merchantNotice.setId(Long.valueOf(id));
            merchantNotice.setStatus(2);
            merchantNotice.setModifyTime(currentTime);
            updateById(merchantNotice);

            if (merchantNoticePoResult.getTMatchNoticeId() > 0) {
                TMatchNotice tMatchNotice = new TMatchNotice();
                tMatchNotice.setId(merchantNoticePoResult.getTMatchNoticeId());
                tMatchNotice.setStatus(2);
                tMatchNotice.setModifyTime(currentTime);
                tMatchNoticeMapper.updateById(tMatchNotice);
            }

            // 推送特定类型的公告给电竞(电竞只要已发布的) -- 取消发布
            boolean writeFlag = Objects.equals(CommonDefaultValue.ONE,merchantNoticePoResult.getStatus()) && (Objects.equals(NoticeTypeEnum.NBA2K.getCode(), CommonUtil.getMatchNoticeType(merchantNoticePoResult.getNoticeTypeId())) ||
                    Objects.equals(NoticeTypeEnum.FIFA.getCode(), CommonUtil.getMatchNoticeType(merchantNoticePoResult.getNoticeTypeId())));
            if (writeFlag){
                SportNoticeToESportSendReqVO sendReqVO = new SportNoticeToESportSendReqVO();
                sendReqVO.setSid(merchantNoticePoResult.getId());

                String apiName = "v1/notice/recall";
                String flag = "撤回";
                handleSportNoticeToESport(sendReqVO, apiName, flag);
            }

            //添加日志
            MerchantLogFiledVO vo = new MerchantLogFiledVO();
            vo.getFieldName().add("取消发布");
            vo.getBeforeValues().add(id+"-"+merchantNoticePoResult.getTitle());
            vo.getAfterValues().add("-");
            String username = loginUserService.getLoginUser(userId);
            merchantLogService.saveLog(MerchantLogPageEnum.MES_IN_CENT_RELEASE, MerchantLogTypeEnum.CENT_CANCEL, vo, MerchantLogConstants.MERCHANT_IN, userId.toString(),
                    username, null, null, id.toString(),language,ip);
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("cacelNotice", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    /**
     * 恢复发布
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response backNotice(Integer id,Integer userId,String language ,String ip) {

        try {
            MerchantNotice merchantNoticePoResult = getById(id);
            if (StringUtils.isEmpty(merchantNoticePoResult)) {
                return Response.returnFail("结果不存在");
            }
            Long currentTime = System.currentTimeMillis();

            //取消状态
            MerchantNotice merchantNotice = new MerchantNotice();
            merchantNotice.setId(Long.valueOf(id));
            merchantNotice.setStatus(1);
            merchantNotice.setSendTime(currentTime);
            merchantNotice.setModifyTime(currentTime);
            updateById(merchantNotice);

            if (merchantNoticePoResult.getTMatchNoticeId() > 0) {
                TMatchNotice tMatchNotice = new TMatchNotice();
                tMatchNotice.setId(merchantNoticePoResult.getTMatchNoticeId());
                tMatchNotice.setStatus(1);
                tMatchNotice.setSendTime(currentTime);
                tMatchNotice.setModifyTime(currentTime);
                tMatchNoticeMapper.updateById(tMatchNotice);
            }

            // 推送特定类型的公告给电竞(电竞只要已发布的) -- 恢复发布
            boolean writeFlag = Objects.equals(NoticeTypeEnum.NBA2K.getCode(), CommonUtil.getMatchNoticeType(merchantNoticePoResult.getNoticeTypeId())) ||
                    Objects.equals(NoticeTypeEnum.FIFA.getCode(), CommonUtil.getMatchNoticeType(merchantNoticePoResult.getNoticeTypeId()));
            if (writeFlag){
                SportNoticeToESportSendReqVO sendReqVO = new SportNoticeToESportSendReqVO();
                sendReqVO.setSid(merchantNoticePoResult.getId());

                String apiName = "v1/notice/delete";
                String flag = "恢复发布-删除";
                if (handleSportNoticeToESport(sendReqVO, apiName, flag)){
                    apiName = "v1/notice/insert";
                    flag = "恢复发布-添加";

                    divideAndSendSportNoticeToESport(merchantNoticePoResult, apiName, flag);
                }
            }

            //添加日志
            String username = loginUserService.getLoginUser(userId);
            MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
            filedVO.getFieldName().add(MerchantFieldUtil.FIELD_MAPPING.get("status"));
            filedVO.getAfterValues().add(merchantNoticePoResult.getStatus().toString());
            filedVO.getBeforeValues().add("1");
            merchantLogService.saveLog(MerchantLogPageEnum.MES_IN_CENT, MerchantLogTypeEnum.CENT_PUSH, filedVO, MerchantLogConstants.MERCHANT_IN, userId.toString(),
                    username, null, merchantNoticePoResult.getTitle(), id.toString(),language,ip);
            return Response.returnSuccess();
        } catch (Exception e) {
            log.error("backNotice", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * mq推送的
     *
     * @param marketResultMessagePO
     */
    @Override
    public void addOsmcMqNotice(MarketResultMessagePO marketResultMessagePO) {
        Long sendTime = System.currentTimeMillis();
        Long tMatchNoticeId;

        Long playId = 0L;

        Long merchantNoticeId = 0L;
        Long merchantTMatchNoticeId = 0L;

        Long matchId = marketResultMessagePO.getMatchId();
        MatchInfoPO matchInfo = sMatchInfoMapper.getMatchInfoByMid(matchId);
        if (StringUtils.isEmpty(matchInfo)) {
            return;
        }
        // 是否取消注单
        boolean isCacel = false;

        // 二次结算
        boolean isTwo = false;

        // 二次结算会有一次赛事同个玩法会取消多个盘口
        // 要不要更新
        boolean isUpdate = false;

        for (MarketOptionsResultPO marketOptionsResultPO : marketResultMessagePO.getMarketOptionsResults()) {
            // 如果取消
            if (marketOptionsResultPO.getResult() != null && marketOptionsResultPO.getResult() >= 7) {
                isCacel = true;
                break;
            }
        }
        // 存在二次结算
        if (marketResultMessagePO.getSettlementTimes() > 1) {
            isTwo = true;
        }
        // 没有取消的注单
        if (!isCacel && !isTwo) {
            return;
        }
        log.info("取消注单,赛事ID:{}", marketResultMessagePO.getMatchId());
        //MAP封装
        Map<Long, SLanguagePO> languageMap = new HashMap<>();
        List<SLanguagePO> sLanguageList = sLanguageService.getLangList(Arrays.asList(matchInfo.getHomeNameCode(), matchInfo.getAwayNameCode(), matchInfo.getTournamentNameCode()));
        languageMap = sLanguageList.stream().collect(Collectors.toMap(SLanguagePO::getNameCode, sLanguage -> sLanguage));

        // 设置标题和内容
        String title, content,enTitle,enContext,zhTitle,zhContext;
        if (isCacel) {

            // 取消注单
            title = SportEnum.getSportEnum(matchInfo.getSportId()).getValue() + "赛事: " +
                    DateUtil.format(DateUtil.date(matchInfo.getBeginTime()), "yyyy/MM/dd HH:mm") + " " + languageMap.get(matchInfo.getTournamentNameCode()).getZs()
                    + "( " + languageMap.get(matchInfo.getHomeNameCode()).getZs() + " VS " + languageMap.get(matchInfo.getAwayNameCode()).getZs() + " ) 注单取消公告";

            enTitle = LanguageSportEnum.getSportEnum(matchInfo.getSportId()).getEnValue() + " match: " +
                    DateUtil.format(DateUtil.date(matchInfo.getBeginTime()), "yyyy/MM/dd HH:mm") + " " + languageMap.get(matchInfo.getTournamentNameCode()).getEn()
                    + "( " + languageMap.get(matchInfo.getHomeNameCode()).getEn() + " VS " + languageMap.get(matchInfo.getAwayNameCode()).getEn() + " ) bet cancellation announcement";

            zhTitle = LanguageSportEnum.getSportEnum(matchInfo.getSportId()).getZhValue() + " match: " +
                    DateUtil.format(DateUtil.date(matchInfo.getBeginTime()), "yyyy/MM/dd HH:mm") + " " + languageMap.get(matchInfo.getTournamentNameCode()).getZh()
                    + "( " + languageMap.get(matchInfo.getHomeNameCode()).getZh() + " VS " + languageMap.get(matchInfo.getAwayNameCode()).getZh() + " ) 注單取消公告";


            content = SportEnum.getSportEnum(matchInfo.getSportId()).getValue() + "赛事: " +
                    DateUtil.format(DateUtil.date(matchInfo.getBeginTime()), "yyyy/MM/dd HH:mm") + " " + languageMap.get(matchInfo.getTournamentNameCode()).getZs()
                    + "( " + languageMap.get(matchInfo.getHomeNameCode()).getZs() + " VS " + languageMap.get(matchInfo.getAwayNameCode()).getZs() + " ) 的注单一律取消,过关以 (1) 结算";

            enContext = LanguageSportEnum.getSportEnum(matchInfo.getSportId()).getEnValue() + " match: " +
                    DateUtil.format(DateUtil.date(matchInfo.getBeginTime()), "yyyy/MM/dd HH:mm") + " " + languageMap.get(matchInfo.getTournamentNameCode()).getEn()
                    + "( " + languageMap.get(matchInfo.getHomeNameCode()).getEn() + " VS " + languageMap.get(matchInfo.getAwayNameCode()).getEn() + " ) cancellation of the betting rules, and settlement with (1)";

            zhContext = LanguageSportEnum.getSportEnum(matchInfo.getSportId()).getZhValue() + " 賽事: " +
                    DateUtil.format(DateUtil.date(matchInfo.getBeginTime()), "yyyy/MM/dd HH:mm") + " " + languageMap.get(matchInfo.getTournamentNameCode()).getZh()
                    + "( " + languageMap.get(matchInfo.getHomeNameCode()).getZh() + " VS " + languageMap.get(matchInfo.getAwayNameCode()).getZh() + " ) 的注單一律取消,過關以 (1) 結算";
        } else {

            String playName = "";

            //二次结算
            OrderDetailPO orderDetailPO = tOrderDetailMapper.getPlayAndMarket(marketResultMessagePO.getMarketId());
            if (StringUtils.isEmpty(orderDetailPO)) {
                return;
            }
            log.info("该盘口包含投注订单,赛事ID:{},盘口ID:{}", marketResultMessagePO.getMatchId(), marketResultMessagePO.getMarketId());
            //玩法名称
            playName = orderDetailPO.getPlayName().trim();
            playId = orderDetailPO.getPlayId();
            List<MerchantNotice> merchantNoticeList = merchantNoticeMapper.getNoticeByParam(String.valueOf(marketResultMessagePO.getMatchId()), playId);

            if (merchantNoticeList.size() > 0) {
                isUpdate = true;
                merchantNoticeId = merchantNoticeList.get(0).getId();
                merchantTMatchNoticeId = merchantNoticeList.get(0).getTMatchNoticeId();
            }

            title = SportEnum.getSportEnum(matchInfo.getSportId()).getValue() + "赛事: " +
                    DateUtil.format(DateUtil.date(matchInfo.getBeginTime()), "yyyy/MM/dd HH:mm") + " " + languageMap.get(matchInfo.getTournamentNameCode()).getZs()
                    + "( " + languageMap.get(matchInfo.getHomeNameCode()).getZs() + " VS " + languageMap.get(matchInfo.getAwayNameCode()).getZs() + " ) 相关注单二次结算公告";

            enTitle = LanguageSportEnum.getSportEnum(matchInfo.getSportId()).getEnValue() + " match: " +
                    DateUtil.format(DateUtil.date(matchInfo.getBeginTime()), "yyyy/MM/dd HH:mm") + " " + languageMap.get(matchInfo.getTournamentNameCode()).getEn()
                    + "( " + languageMap.get(matchInfo.getHomeNameCode()).getEn() + " VS " + languageMap.get(matchInfo.getAwayNameCode()).getEn() + " ) relevant bet secondary settlement announcement";

            zhTitle = LanguageSportEnum.getSportEnum(matchInfo.getSportId()).getZhValue() + "賽事: " +
                    DateUtil.format(DateUtil.date(matchInfo.getBeginTime()), "yyyy/MM/dd HH:mm") + " " + languageMap.get(matchInfo.getTournamentNameCode()).getZh()
                    + "( " + languageMap.get(matchInfo.getHomeNameCode()).getZh() + " VS " + languageMap.get(matchInfo.getAwayNameCode()).getZh() + " ) 相關注單二次結算公告";

            content = SportEnum.getSportEnum(matchInfo.getSportId()).getValue() + "赛事: " +
                    DateUtil.format(DateUtil.date(matchInfo.getBeginTime()), "yyyy/MM/dd HH:mm") + " " + languageMap.get(matchInfo.getTournamentNameCode()).getZs()
                    + "( " + languageMap.get(matchInfo.getHomeNameCode()).getZs() + " VS " + languageMap.get(matchInfo.getAwayNameCode()).getZs() + " ) 因赛果修正，" + (StringUtils.isEmpty(playName) ? "相关注单已重新结算" : "( " + playName + " ) 相关注单已重新结算");

            enContext = LanguageSportEnum.getSportEnum(matchInfo.getSportId()).getEnValue() + " match: " +
                    DateUtil.format(DateUtil.date(matchInfo.getBeginTime()), "yyyy/MM/dd HH:mm") + " " + languageMap.get(matchInfo.getTournamentNameCode()).getEn()
                    + "( " + languageMap.get(matchInfo.getHomeNameCode()).getEn() + " VS " + languageMap.get(matchInfo.getAwayNameCode()).getEn() + " )  , because result correction , the related bet has been re-settled";

            zhContext = LanguageSportEnum.getSportEnum(matchInfo.getSportId()).getZhValue() + " 賽事: " +
                    DateUtil.format(DateUtil.date(matchInfo.getBeginTime()), "yyyy/MM/dd HH:mm") + " " + languageMap.get(matchInfo.getTournamentNameCode()).getZh()
                    + "( " + languageMap.get(matchInfo.getHomeNameCode()).getZh() + " VS " + languageMap.get(matchInfo.getAwayNameCode()).getZh() + " )  , 因賽果修正，" + (StringUtils.isEmpty(playName) ?"相關注單已重新結算" : "( " + playName + " ) 相關注單已重新結算");
        }


        // 如果是更新
        if (isUpdate) {

            if (merchantTMatchNoticeId > 0 && !StringUtils.isEmpty(title) && !StringUtils.isEmpty(content)) {
                TMatchNotice tMatchNotice = new TMatchNotice();
                tMatchNotice.setId(merchantTMatchNoticeId).setTitle(title).setEnTitle(enTitle).setZhTitle(zhTitle).setContext(content).setEnContext(enContext).setZhContext(zhContext).setCreateTime(sendTime).setSendTime(sendTime).setModifyTime(sendTime);

                tMatchNoticeMapper.updateById(tMatchNotice);
            }

            if (merchantNoticeId > 0 && !StringUtils.isEmpty(title) && !StringUtils.isEmpty(content)) {
                MerchantNotice merchantNotice = new MerchantNotice();
                merchantNotice.setId(merchantNoticeId).setTitle(title).setContext(content).setSendTime(sendTime).setCreateTime(sendTime).setModifyTime(sendTime);
                updateById(merchantNotice);
            }

        } else {

            // 如果标题和内容不能为空
            if (!StringUtils.isEmpty(title) && !StringUtils.isEmpty(content)) {

                TMatchNotice tMatchNotice = new TMatchNotice();
                tMatchNotice.setNoticeType(CommonUtil.getMatchNoticeTypeBySportId(String.valueOf(matchInfo.getSportId())))
                        .setTitle(title).setEnTitle(enTitle).setZhTitle(zhTitle).setStatus(0).setContext(content).setEnContext(enContext).setZhContext(zhContext)
                        .setCreateTime(sendTime).setSendTime(sendTime).setModifyTime(sendTime);
                tMatchNoticeMapper.insert(tMatchNotice);


                //商户库
                tMatchNoticeId = tMatchNotice.getId();

                MerchantNotice merchantNotice = new MerchantNotice();
                merchantNotice.setTitle(title).setEnTitle(enTitle).setZhTitle(zhTitle).setContext(content).setEnContext(enContext).setZhContext(zhContext)
                        .setNoticeTypeId(CommonUtil.getMerchantMatchNoticeTypeBySportId(String.valueOf(matchInfo.getSportId()))).setStatus(0)
                        .setReleaseRange("1,2,3,4").setStandardMatchId(String.valueOf(marketResultMessagePO.getMatchId()))
                        .setSendTime(sendTime).setCreateTime(sendTime).setModifyTime(sendTime);

                if (tMatchNoticeId > 0) {
                    merchantNotice.setTMatchNoticeId(tMatchNoticeId);
                }

                if (playId > 0) {
                    merchantNotice.setPlayId(playId);
                }
                save(merchantNotice);
            }
        }
    }

    /**
     * 获取商户的树形结构
     * @param tag
     * @param name
     * @return
     */
    @Override
    public Response getMerchantTree(Integer tag, String name) {

        try {
            final List<MerchantTree> merchantTrees = merchantMapper.getMerchantByTag(tag,name);
            // 拿到顶级用户
            List<MerchantTree> rootMerchant =
                    merchantTrees.stream()
                            .filter(merchantTreeVo->merchantTreeVo.getParentId() == null)
                            .collect(Collectors.toList());
            // 拿到所有二级商户id
            List<String> merchantIds =
                    merchantTrees.stream()
                            .filter(merchantTreeVo->merchantTreeVo.getAgentLevel() == 2 && merchantTreeVo.getParentId() != null)
                            .map(MerchantTree::getParentId)
                            .collect(Collectors.toList());

            // 渠道商户的父级
            if (merchantIds.size()>0) {
                List<MerchantTree> merchantPOListOther = merchantMapper.getMerchantByIds(merchantIds);
                rootMerchant.addAll(merchantPOListOther);
            }

            for (MerchantTree amv : rootMerchant) {
                List<MerchantTree> childList = getChild(amv,merchantTrees);
                amv.setTrees(childList);
            }

            return Response.returnSuccess(rootMerchant);
        } catch (Exception e) {
            log.error("getMerchantTree", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }


    /**
     * 获取子集
     * @param parent
     * @param merchantTreeVoList
     * @return
     */
    private List<MerchantTree> getChild(MerchantTree parent,List<MerchantTree> merchantTreeVoList) {

        List<MerchantTree> childList = new ArrayList<>();
        for (MerchantTree merchantTreeVo : merchantTreeVoList) {
            if (parent.getId().equals(merchantTreeVo.getParentId())) {
                childList.add(merchantTreeVo);
            }
        }
        for (MerchantTree nav : childList) {
            nav.setTrees(getChild(nav,merchantTreeVoList));
        }
        if (childList.size()==0) {
            return Collections.emptyList();
        }
        return childList;
    }

    /**
     * 按照电竞提供的语言类型按类型发送公告给电竞
     * @param merchantNoticePo
     * @param apiName
     * @param flag
     */
    private void divideAndSendSportNoticeToESport(MerchantNotice merchantNoticePo, String apiName, String flag) {
        SportNoticeToESportSendReqVO sendReqVO = new SportNoticeToESportSendReqVO();
        sendReqVO.setSid(merchantNoticePo.getId());
        sendReqVO.setTop(merchantNoticePo.getIsTop());
        sendReqVO.setGame_name(Objects.equals(NoticeTypeEnum.NBA2K.getCode(), CommonUtil.getMatchNoticeType(merchantNoticePo.getNoticeTypeId())) ? "NBA2K" : "FIFA");

        if (ObjectUtil.isNotNull(merchantNoticePo.getTitle()) && ObjectUtil.isNotNull(merchantNoticePo.getContext())){
            sendReqVO.setTitle(merchantNoticePo.getTitle());
            sendReqVO.setContent(merchantNoticePo.getContext());
            sendReqVO.setLang(zs);
            handleSportNoticeToESport(sendReqVO, apiName, flag);
        }

        if (ObjectUtil.isNotNull(merchantNoticePo.getZhTitle()) && ObjectUtil.isNotNull(merchantNoticePo.getZhContext())){
            sendReqVO.setTitle(merchantNoticePo.getZhTitle());
            sendReqVO.setContent(merchantNoticePo.getZhContext());
            sendReqVO.setLang(zh);
            handleSportNoticeToESport(sendReqVO, apiName, flag);
        }

        if (ObjectUtil.isNotNull(merchantNoticePo.getEnTitle()) && ObjectUtil.isNotNull(merchantNoticePo.getEnContext())){
            sendReqVO.setTitle(merchantNoticePo.getEnTitle());
            sendReqVO.setContent(merchantNoticePo.getEnContext());
            sendReqVO.setLang(en);
            handleSportNoticeToESport(sendReqVO, apiName, flag);
        }

        if (ObjectUtil.isNotNull(merchantNoticePo.getViTitle()) && ObjectUtil.isNotNull(merchantNoticePo.getViContext())){
            sendReqVO.setTitle(merchantNoticePo.getViTitle());
            sendReqVO.setContent(merchantNoticePo.getViContext());
            sendReqVO.setLang(vi);
            handleSportNoticeToESport(sendReqVO, apiName, flag);
        }

        if (ObjectUtil.isNotNull(merchantNoticePo.getThTitle()) && ObjectUtil.isNotNull(merchantNoticePo.getThContext())){
            sendReqVO.setTitle(merchantNoticePo.getThTitle());
            sendReqVO.setContent(merchantNoticePo.getThContext());
            sendReqVO.setLang(th);
            handleSportNoticeToESport(sendReqVO, apiName, flag);
        }

        if (ObjectUtil.isNotNull(merchantNoticePo.getMaTitle()) && ObjectUtil.isNotNull(merchantNoticePo.getMaContext())){
            sendReqVO.setTitle(merchantNoticePo.getMaTitle());
            sendReqVO.setContent(merchantNoticePo.getMaContext());
            sendReqVO.setLang(ma);
            handleSportNoticeToESport(sendReqVO, apiName, flag);
        }
    }

    private boolean handleSportNoticeToESport(SportNoticeToESportSendReqVO sendReqVO, String apiName, String flag) {
        String url = sportNoticeToESportUrl + apiName;
        String key = sportNoticeToESportKey;
        Long merchantId = Long.parseLong(sportNoticeToESportMerchantId);
        sendReqVO.setMerchant(merchantId);

        Map<String,Object> sourceMap = BeanUtil.beanToMap(sendReqVO, false, true);
        String sign = SportToESportUtil.eSportNoticeEncrypt(sourceMap,key);
        NameValuePair[] data = new NameValuePair[sourceMap.size() + 1];
        try {
            PostMethod postMethod = new PostMethod(url) ;
            postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8") ;
            int index = 0;
            for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
                data[index++] = new NameValuePair(entry.getKey(),entry.getValue().toString());
            }
            data[index] = new NameValuePair("sign",sign);
            postMethod.setRequestBody(data);
            HttpClient httpClient = new HttpClient();
            int response = httpClient.executeMethod(postMethod);

            //转一下，避免中文乱码
            String result = new String(postMethod.getResponseBodyAsString().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            if (response != 200 || ObjectUtil.isNull(result)) {
                log.error("{}特定类型的公告至电竞失败,response={},标题：{},result:{}",flag,response,sendReqVO.getTitle(),result);
                return false;
            }
            JSONObject jsonObject = JSON.parseObject(result);
            String successStatus = "true";
            String successCode = "200";
            if (successStatus.equals(jsonObject.get("status")) && successCode.equals(jsonObject.get("code"))){
                log.info("{}特定类型的公告至电竞成功,标题：{},result:{}",flag,sendReqVO.getTitle(),result);
                return true;
            } else {
                log.error("{}特定类型的公告至电竞失败,标题：{},result={}",flag,sendReqVO.getTitle(),result);
            }

        } catch (Exception e) {
            log.error("{}特定类型的公告至电竞请求异常,标题：{},param={},异常信息：{}",flag,sendReqVO.getTitle(),data,e);
        }
        return false;
    }

    public void setNoticeDefaultValue(MerchantNoticeForm merchantNoticeForm) {
        //默认弹出
        merchantNoticeForm.setIsPop(1);
        //默认生效时间
        if(merchantNoticeForm.getBeginTime() == null || merchantNoticeForm.getEndTime() == null) {
            long currentTimestamp = System.currentTimeMillis();
            long threeMonthsLaterTimestamp = currentTimestamp + 3 * 30 * 24 * 60 * 60 * 1000L;
            merchantNoticeForm.setBeginTime(merchantNoticeForm.getBeginTime() == null ? currentTimestamp : merchantNoticeForm.getBeginTime());
            merchantNoticeForm.setEndTime(merchantNoticeForm.getEndTime() == null ? threeMonthsLaterTimestamp : merchantNoticeForm.getEndTime());
        }
    }

}
