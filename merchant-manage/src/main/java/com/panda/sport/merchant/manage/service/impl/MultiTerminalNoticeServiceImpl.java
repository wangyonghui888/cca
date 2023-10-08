package com.panda.sport.merchant.manage.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panda.sport.match.mapper.TMatchNoticeMapper;
import com.panda.sport.merchant.common.constant.CommonDefaultValue;
import com.panda.sport.merchant.common.constant.DatabaseCommonColumnToStr;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.AggregateFestivalEnum;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.po.match.TMatchNotice;
import com.panda.sport.merchant.common.po.merchant.MerchantNotice;
import com.panda.sport.merchant.common.po.merchant.MultiTerminalNotice;
import com.panda.sport.merchant.common.po.merchant.mq.CancelOrderNoticePO;
import com.panda.sport.merchant.common.vo.ESportsNoticeDelReqVO;
import com.panda.sport.merchant.common.vo.ESportsNoticeAddReqVO;
import com.panda.sport.merchant.common.vo.ESportsNoticeEditReqVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.service.MultiTerminalNoticeService;
import com.panda.sport.merchant.manage.util.CommonUtil;
import com.panda.sport.merchant.mapper.MerchantNoticeMapper;
import com.panda.sport.merchant.mapper.MultiTerminalNoticeMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author :  ives
 * @Description :  多端公告处理服务实现类
 * @Date: 2022-03-09 11:32
 */
@Service
@Slf4j
@RefreshScope
public class MultiTerminalNoticeServiceImpl extends ServiceImpl<MultiTerminalNoticeMapper, MultiTerminalNotice> implements MultiTerminalNoticeService {


    @Resource
    private TMatchNoticeMapper tMatchNoticeMapper;

    @Resource
    private MerchantNoticeMapper merchantNoticeMapper;

    @Resource
    private MerchantLogService merchantLogService;


    public static final int sourceESports = 1;
    private static final String NOTICE_PREFIX = "$P$M$";

    @Value("${OSMC.NOTICE.isFix:true}")
    private Boolean isFix4Nacos;

    /**
     * 供电竞部门调用，添加电竞公告到本地公告信息
     *
     * @param addReqVO
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response addESportsNotice(ESportsNoticeAddReqVO addReqVO, String ip) {
        QueryWrapper<MultiTerminalNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseCommonColumnToStr.MULTI_TERMINAL_NOTICE_ID, addReqVO.getMultiTerminalNoticeId());

        MultiTerminalNotice multiTerminalNotice = this.getOne(queryWrapper);

        try {
            if (ObjectUtil.isNull(multiTerminalNotice)) {

                MultiTerminalNotice addMultiTerminalNotice = new MultiTerminalNotice();
                BeanUtils.copyProperties(addReqVO, addMultiTerminalNotice);
                Long nowTime = System.currentTimeMillis();
                checkNullValue(addReqVO, addMultiTerminalNotice);

                addMultiTerminalNotice.setSource(sourceESports);
                addMultiTerminalNotice.setCreateTime(nowTime);
                addMultiTerminalNotice.setUpdateTime(nowTime);

                boolean flag = this.save(addMultiTerminalNotice);
                if (!flag) {
                    log.info("标题为：{},电竞公告写入失败1,原因：保存异常！", addReqVO.getTitle());
                    return Response.returnFail("标题为：" + addReqVO.getTitle() + ",电竞公告写入失败,原因：保存异常！");
                }
                TMatchNotice tMatchNotice = new TMatchNotice();

                BeanUtils.copyProperties(addMultiTerminalNotice, tMatchNotice);
                tMatchNotice.setId(null);
                // 发布范围,1直营商户，2渠道商户, 3二级商户,5内部用户,6所有商户投注用户,8信用网
                tMatchNotice.setReleaseRange("1,2,3,5,6,8");

                tMatchNotice.setIsPop(addMultiTerminalNotice.getIsLoginPopup());
                tMatchNotice.setIsTag(addMultiTerminalNotice.getIsImportant());
                tMatchNotice.setIsTop(addMultiTerminalNotice.getIsTop());
                tMatchNotice.setIsShuf(addMultiTerminalNotice.getIsRotation());

                tMatchNotice.setNoticeType(CommonUtil.getMatchNoticeType(addMultiTerminalNotice.getNoticeType()));
                tMatchNotice.setCreatedBy(addMultiTerminalNotice.getPublishedBy());
                tMatchNotice.setUpdatedBy(addMultiTerminalNotice.getPublishedBy());
                tMatchNotice.setBeginTime(addMultiTerminalNotice.getEffectiveTime());
                tMatchNotice.setEndTime(addMultiTerminalNotice.getEffectiveEndTime());

                if (ObjectUtil.isNull(addReqVO.getStatus())) {
                    tMatchNotice.setStatus(1);
                } else {
                    tMatchNotice.setStatus(addReqVO.getStatus());
                }
                tMatchNotice.setSendTime(nowTime);
                tMatchNotice.setCreateTime(nowTime);
                tMatchNotice.setModifyTime(nowTime);
                int result = tMatchNoticeMapper.insert(tMatchNotice);
                if (result != CommonDefaultValue.ONE) {
                    log.info("标题为：{},电竞公告写入失败2,原因：保存异常！", addReqVO.getTitle());
                    return Response.returnFail("标题为：" + addReqVO.getTitle() + ",电竞公告写入失败,原因：保存异常！");
                }

                MerchantNotice merchantNotice = new MerchantNotice();
                BeanUtils.copyProperties(tMatchNotice, merchantNotice);
                merchantNotice.setTMatchNoticeId(tMatchNotice.getId());
                merchantNotice.setNoticeTypeId(tMatchNotice.getNoticeType());
                merchantNotice.setMultiTerminalNoticeId(addReqVO.getMultiTerminalNoticeId());
                merchantNotice.setStandardMatchId(addReqVO.getMultiTerminalNoticeId().toString());
                merchantNotice.setId(null);
                result = merchantNoticeMapper.insert(merchantNotice);
                if (result != CommonDefaultValue.ONE) {
                    log.info("标题为：{},电竞公告写入失败3,原因：保存异常！", addReqVO.getTitle());
                    return Response.returnFail("标题为：" + addReqVO.getTitle() + ",电竞公告写入失败,原因：保存异常！");
                }
                merchantLogService.saveLog(MerchantLogPageEnum.MES_IN_CENT, MerchantLogTypeEnum.CENT_ADD, null, MerchantLogConstants.MERCHANT_IN, null,
                        addReqVO.getPublishedBy(), null, addReqVO.getTitle(), tMatchNotice.getId().toString(), "中文简体", ip);
            } else {
                MultiTerminalNotice updMultiTerminalNotice = new MultiTerminalNotice();
                BeanUtils.copyProperties(addReqVO, updMultiTerminalNotice);
                updMultiTerminalNotice.setUpdateTime(System.currentTimeMillis());
                updMultiTerminalNotice.setId(multiTerminalNotice.getId());

                boolean flag = this.updateById(updMultiTerminalNotice);
                if (!flag) {
                    log.info("标题为：{},电竞公告写入失败1,原因：保存异常！", multiTerminalNotice.getTitle());
                    return Response.returnFail("标题为：" + multiTerminalNotice.getTitle() + ",电竞公告写入失败,原因：保存异常！");
                }
                QueryWrapper<MerchantNotice> merchantNoticeQueryWrapper = new QueryWrapper<>();
                merchantNoticeQueryWrapper.eq(DatabaseCommonColumnToStr.MULTI_TERMINAL_NOTICE_ID, addReqVO.getMultiTerminalNoticeId());
                MerchantNotice merchantNotice = merchantNoticeMapper.selectOne(merchantNoticeQueryWrapper);

                if (ObjectUtil.isNull(merchantNotice)) {
                    log.info("标题为：{},电竞公告修改失败2,原因：查询电竞公告表对应的ID主键无果！", multiTerminalNotice.getTitle());
                    return Response.returnFail("标题为：" + multiTerminalNotice.getTitle() + ",电竞公告写入失败,原因：查询电竞公告表对应的ID主键无果！");
                }

                TMatchNotice tMatchNotice = new TMatchNotice();

                BeanUtils.copyProperties(updMultiTerminalNotice, tMatchNotice);
                checkNotNullValue(addReqVO, tMatchNotice);
                tMatchNotice.setModifyTime(System.currentTimeMillis());
                tMatchNotice.setId(merchantNotice.getTMatchNoticeId());
                int result = tMatchNoticeMapper.updateById(tMatchNotice);
                if (result != CommonDefaultValue.ONE) {
                    log.info("标题为：{},电竞公告写入失败2,原因：保存异常！", multiTerminalNotice.getTitle());
                    return Response.returnFail("标题为：" + multiTerminalNotice.getTitle() + ",电竞公告写入失败,原因：保存异常！");
                }

                MerchantNotice updMerchantNotice = new MerchantNotice();
                BeanUtils.copyProperties(tMatchNotice, updMerchantNotice);
                updMerchantNotice.setStandardMatchId(updMultiTerminalNotice.getMultiTerminalNoticeId().toString());
                updMerchantNotice.setId(merchantNotice.getId());
                if (ObjectUtil.isNotNull(tMatchNotice.getNoticeType())) {
                    updMerchantNotice.setNoticeTypeId(tMatchNotice.getNoticeType());
                }
                result = merchantNoticeMapper.updateById(updMerchantNotice);
                if (result != CommonDefaultValue.ONE) {
                    log.info("标题为：{},电竞公告写入失败3,原因：保存异常！", multiTerminalNotice.getTitle());
                    return Response.returnFail("标题为：" + multiTerminalNotice.getTitle() + ",电竞公告写入失败,原因：保存异常！");
                }
            }


        } catch (BeansException e) {
            log.info("电竞公告写入失败标题：{},原因：{}", addReqVO.getTitle(), e);
            return Response.returnFail("标题为：" + addReqVO.getTitle() + ",电竞公告写入失败,原因：" + e.getMessage());
        }
        return Response.returnSuccess();
    }

    /**
     * 供电竞部门调用，编辑电竞公告到本地公告信息
     *
     * @param editReqVO
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response editESportsNotice(ESportsNoticeEditReqVO editReqVO, String ip) {
        QueryWrapper<MultiTerminalNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseCommonColumnToStr.MULTI_TERMINAL_NOTICE_ID, editReqVO.getMultiTerminalNoticeId());

        MultiTerminalNotice multiTerminalNotice = this.getOne(queryWrapper);

        if (ObjectUtil.isNull(multiTerminalNotice)) {
            log.info("标题为：{},电竞公告修改失败,原因：查询电竞公告表对应的ID主键无果！", editReqVO.getTitle());
            return Response.returnFail("标题为：" + editReqVO.getTitle() + ",电竞公告修改失败,原因：查询电竞公告表对应的ID主键无果！");
        }

        MultiTerminalNotice updMultiTerminalNotice = new MultiTerminalNotice();
        BeanUtils.copyProperties(editReqVO, updMultiTerminalNotice);
        updMultiTerminalNotice.setUpdateTime(System.currentTimeMillis());
        updMultiTerminalNotice.setId(multiTerminalNotice.getId());
        try {
            boolean flag = this.updateById(updMultiTerminalNotice);
            if (!flag) {
                log.info("标题为：{},电竞公告修改失败1,原因：修改异常！", multiTerminalNotice.getTitle());
                return Response.returnFail("标题为：" + multiTerminalNotice.getTitle() + ",电竞公告修改失败,原因：修改异常！");
            }
            QueryWrapper<MerchantNotice> merchantNoticeQueryWrapper = new QueryWrapper<>();
            merchantNoticeQueryWrapper.eq(DatabaseCommonColumnToStr.MULTI_TERMINAL_NOTICE_ID, editReqVO.getMultiTerminalNoticeId());
            MerchantNotice merchantNotice = merchantNoticeMapper.selectOne(merchantNoticeQueryWrapper);

            if (ObjectUtil.isNull(merchantNotice)) {
                log.info("标题为：{},电竞公告修改失败2,原因：查询电竞公告表对应的ID主键无果！", multiTerminalNotice.getTitle());
                return Response.returnFail("标题为：" + multiTerminalNotice.getTitle() + ",电竞公告修改失败,原因：查询电竞公告表对应的ID主键无果！");
            }

            TMatchNotice tMatchNotice = new TMatchNotice();

            BeanUtils.copyProperties(updMultiTerminalNotice, tMatchNotice);
            checkNotNullValue(editReqVO, tMatchNotice);
            tMatchNotice.setModifyTime(System.currentTimeMillis());
            tMatchNotice.setId(merchantNotice.getTMatchNoticeId());
            int result = tMatchNoticeMapper.updateById(tMatchNotice);
            if (result != CommonDefaultValue.ONE) {
                log.info("标题为：{},电竞公告修改失败2,原因：修改异常！", multiTerminalNotice.getTitle());
                return Response.returnFail("标题为：" + multiTerminalNotice.getTitle() + ",电竞公告修改失败,原因：修改异常！");
            }

            MerchantNotice updMerchantNotice = new MerchantNotice();
            BeanUtils.copyProperties(tMatchNotice, updMerchantNotice);
            updMerchantNotice.setId(merchantNotice.getId());
            updMerchantNotice.setStandardMatchId(editReqVO.getMultiTerminalNoticeId().toString());
            if (ObjectUtil.isNotNull(tMatchNotice.getNoticeType())) {
                updMerchantNotice.setNoticeTypeId(tMatchNotice.getNoticeType());
            }
            result = merchantNoticeMapper.updateById(updMerchantNotice);
            if (result != CommonDefaultValue.ONE) {
                log.info("标题为：{},电竞公告修改失败3,原因：修改异常！", multiTerminalNotice.getTitle());
                return Response.returnFail("标题为：" + multiTerminalNotice.getTitle() + ",电竞公告修改失败,原因：修改异常！");
            }
            merchantLogService.saveLog(MerchantLogPageEnum.MES_IN_CENT, MerchantLogTypeEnum.CENT_EDIT, null, MerchantLogConstants.MERCHANT_IN, null,
                    multiTerminalNotice.getPublishedBy(), null, null, tMatchNotice.getId().toString(), "中文简体", ip);
        } catch (BeansException e) {
            log.info("电竞公告修改失败标题：{},原因：{}", multiTerminalNotice.getTitle(), e);
            return Response.returnFail("标题为：" + multiTerminalNotice.getTitle() + ",电竞公告修改失败,原因：" + e.getMessage());
        }
        return Response.returnSuccess();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response delESportsNotice(ESportsNoticeDelReqVO delReqVO) {
        QueryWrapper<MultiTerminalNotice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseCommonColumnToStr.MULTI_TERMINAL_NOTICE_ID, delReqVO.getMultiTerminalNoticeId());

        MultiTerminalNotice multiTerminalNotice = this.getOne(queryWrapper);
        if (ObjectUtil.isNull(multiTerminalNotice)) {
            // 该条记录对应的其他语言可能之前已做过删除操作，此处若查询为空，则直接返回成功即可。
            return Response.returnSuccess();
        }
        this.removeById(multiTerminalNotice);
        QueryWrapper<MerchantNotice> merchantNoticeQueryWrapper = new QueryWrapper<>();
        merchantNoticeQueryWrapper.eq(DatabaseCommonColumnToStr.MULTI_TERMINAL_NOTICE_ID, multiTerminalNotice.getMultiTerminalNoticeId());
        MerchantNotice merchantNotice = merchantNoticeMapper.selectOne(merchantNoticeQueryWrapper);
        if (ObjectUtil.isNull(merchantNotice)) {
            log.info("电竞公告删除失败2,原因：查询电竞公告表对应的ID主键无果！");
            return Response.returnFail("电竞公告删除失败,原因：查询电竞公告表对应的ID主键无果！");
        }

        int result = merchantNoticeMapper.deleteById(merchantNotice);
        if (result != CommonDefaultValue.ONE) {
            log.info("标题为：{},电竞公告删除失败,原因：删除异常！", multiTerminalNotice.getTitle());
            return Response.returnFail("标题为：" + multiTerminalNotice.getTitle() + ",电竞公告删除失败,原因：删除异常！");
        }

        result = tMatchNoticeMapper.deleteById(merchantNotice.getTMatchNoticeId());
        if (result != CommonDefaultValue.ONE) {
            log.info("标题为：{},电竞公告删除失败2,原因：删除异常！", multiTerminalNotice.getTitle());
            return Response.returnFail("标题为：" + multiTerminalNotice.getTitle() + ",电竞公告删除失败,原因：删除异常！");
        }
        return Response.returnSuccess();
    }

    @Override
    public void saveCancelOrderNotice(CancelOrderNoticePO cancelOrderNoticePO) {
        TMatchNotice tMatchNotice = new TMatchNotice();
        String title = "二次结算&取消注单自动公告";
        Long nowTime = System.currentTimeMillis();
        Integer isFix = cancelOrderNoticePO.getIsFix();
        long fixStartTime = nowTime - 12 * 60 * 60 * 1000;
        // 商户公告类型
        Integer merchantNoticeId = CommonUtil.getCancelOrderNoticeType(cancelOrderNoticePO.getSportId());
        // 赛事赛程的公告类型
        Integer tMerchantNoticeId = CommonUtil.getMatchNoticeType(merchantNoticeId);

        MerchantNotice oldMerchantNotice = merchantNoticeMapper
                .getNoticeIn12Hours(merchantNoticeId, cancelOrderNoticePO.getMatchId(),
                        cancelOrderNoticePO.getNoticeResultType(), cancelOrderNoticePO.getSportId(), fixStartTime);

        if (isFix4Nacos && oldMerchantNotice != null && isFix != null && isFix == 1) {
            log.info("赛事ID：【{}】的公告查询到，进行修改旧公告操作",cancelOrderNoticePO.getMatchId());
            tMatchNotice.setId(oldMerchantNotice.getTMatchNoticeId());
            // 修改旧公告
            if (ObjectUtil.isNotNull(cancelOrderNoticePO.getZs())) {
                JSONObject zsJson = JSON.parseObject(cancelOrderNoticePO.getZs());
                oldMerchantNotice.setContext(transferAddNotice(zsJson, oldMerchantNotice.getContext(), AggregateFestivalEnum.zh));
            }

            if (ObjectUtil.isNotNull(cancelOrderNoticePO.getZh())) {
                JSONObject zhJson = JSON.parseObject(cancelOrderNoticePO.getZh());
                oldMerchantNotice.setZhContext(transferAddNotice(zhJson, oldMerchantNotice.getZhContext(), AggregateFestivalEnum.tw));
            }

            if (ObjectUtil.isNotNull(cancelOrderNoticePO.getEn())) {
                JSONObject enJson = JSON.parseObject(cancelOrderNoticePO.getEn());
                oldMerchantNotice.setEnContext(transferAddNotice(enJson, oldMerchantNotice.getEnContext(), AggregateFestivalEnum.en));
            }

            if (ObjectUtil.isNotNull(cancelOrderNoticePO.getYn())) {
                JSONObject viJson = JSON.parseObject(cancelOrderNoticePO.getYn());
                oldMerchantNotice.setViContext(transferAddNotice(viJson, oldMerchantNotice.getViContext(), AggregateFestivalEnum.vi));
            }
            if (ObjectUtil.isNotNull(cancelOrderNoticePO.getTh())) {
                JSONObject thJson = JSON.parseObject(cancelOrderNoticePO.getTh());
                oldMerchantNotice.setThContext(transferAddNotice(thJson, oldMerchantNotice.getThContext(), AggregateFestivalEnum.th));
            }
            if (ObjectUtil.isNotNull(cancelOrderNoticePO.getBm())) {
                JSONObject maJson = JSON.parseObject(cancelOrderNoticePO.getBm());
                oldMerchantNotice.setMaContext(transferAddNotice(maJson, oldMerchantNotice.getMaContext(), AggregateFestivalEnum.ms));
            }
            if (ObjectUtil.isNotNull(cancelOrderNoticePO.getBi())) {
                JSONObject biJson = JSON.parseObject(cancelOrderNoticePO.getEn());
                oldMerchantNotice.setBiContext(transferAddNotice(biJson, oldMerchantNotice.getEnContext(), AggregateFestivalEnum.ad));
            }

            // 二次确认 将PREFIX替换
            tMatchNotice.transferNoticePerfix();

            oldMerchantNotice.setBeginTime(nowTime);
            oldMerchantNotice.setEndTime(nowTime + TimeUnit.DAYS.toMillis(3));
            oldMerchantNotice.setSendTime(nowTime);
            oldMerchantNotice.setModifyTime(nowTime);

            int result = merchantNoticeMapper.updateById(oldMerchantNotice);
            if (result != CommonDefaultValue.ONE) {
                log.info("标题为：{},{}修改merchant_notice失败,原因：保存异常！", tMatchNotice.getTitle(), title);
            }

            tMatchNotice = tMatchNoticeMapper.selectById(oldMerchantNotice.getTMatchNoticeId());

            if(tMatchNotice==null){
                log.error("标题为：{},查询不到赛事",title);
            }else{
                tMatchNotice.setBeginTime(nowTime);
                tMatchNotice.setEndTime(nowTime + TimeUnit.DAYS.toMillis(3));
                tMatchNotice.setSendTime(nowTime);
                tMatchNotice.setModifyTime(nowTime);
                tMatchNotice.setContext(oldMerchantNotice.getContext());
                tMatchNotice.setEnContext(oldMerchantNotice.getEnContext());
                tMatchNotice.setZhContext(oldMerchantNotice.getZhContext());
                tMatchNotice.setViContext(oldMerchantNotice.getViContext());

                result = tMatchNoticeMapper.updateById(tMatchNotice);
                if (result != CommonDefaultValue.ONE) {
                    log.info("标题为：{},{}修改t_match_notice失败,原因：保存异常！", tMatchNotice.getTitle(), title);
                }
            }

        } else {
            // 无需追加，直接新增

            if (ObjectUtil.isNotNull(cancelOrderNoticePO.getZs())) {
                JSONObject zsJson = JSON.parseObject(cancelOrderNoticePO.getZs());
                tMatchNotice.setTitle(zsJson.getString("title"));
                tMatchNotice.setContext(zsJson.getString("content"));
            }

            if (ObjectUtil.isNotNull(cancelOrderNoticePO.getZh())) {
                JSONObject zhJson = JSON.parseObject(cancelOrderNoticePO.getZh());
                tMatchNotice.setZhTitle(zhJson.getString("title"));
                tMatchNotice.setZhContext(zhJson.getString("content"));
            }

            if (ObjectUtil.isNotNull(cancelOrderNoticePO.getEn())) {
                JSONObject enJson = JSON.parseObject(cancelOrderNoticePO.getEn());
                tMatchNotice.setEnTitle(enJson.getString("title"));
                tMatchNotice.setEnContext(enJson.getString("content"));
            }

            if (ObjectUtil.isNotNull(cancelOrderNoticePO.getYn())) {
                JSONObject viJson = JSON.parseObject(cancelOrderNoticePO.getYn());
                tMatchNotice.setViTitle(viJson.getString("title"));
                tMatchNotice.setViContext(viJson.getString("content"));
            }

            if (ObjectUtil.isNotNull(cancelOrderNoticePO.getTh())) {
                JSONObject thJson = JSON.parseObject(cancelOrderNoticePO.getTh());
                tMatchNotice.setThTitle(thJson.getString("title"));
                tMatchNotice.setThContext(thJson.getString("content"));
            }

            if (ObjectUtil.isNotNull(cancelOrderNoticePO.getBm())) {
                JSONObject bmJson = JSON.parseObject(cancelOrderNoticePO.getBm());
                tMatchNotice.setMaTitle(bmJson.getString("title"));
                tMatchNotice.setMaContext(bmJson.getString("content"));
            }

            if (ObjectUtil.isNotNull(cancelOrderNoticePO.getBi())) {
                JSONObject biJson = JSON.parseObject(cancelOrderNoticePO.getEn());
                tMatchNotice.setBiTitle(biJson.getString("title"));
                tMatchNotice.setBiContext(biJson.getString("content"));
            }

            // 二次确认 将PREFIX替换
            tMatchNotice.transferNoticePerfix();

            tMatchNotice.setIsTag(CommonDefaultValue.DifferentiateStatus.NO);
            tMatchNotice.setIsPop(CommonDefaultValue.DifferentiateStatus.NO);
            tMatchNotice.setIsTop(CommonDefaultValue.DifferentiateStatus.NO);
            tMatchNotice.setIsShuf(CommonDefaultValue.DifferentiateStatus.YES);
            // 发布范围,1直营商户，2渠道商户, 3二级商户,5内部用户,6所有商户投注用户,8信用网
            tMatchNotice.setReleaseRange("1,2,3,5,6,8");
            tMatchNotice.setStatus(1);
            tMatchNotice.setNoticeType(tMerchantNoticeId);

            tMatchNotice.setCreatedBy(cancelOrderNoticePO.getOperatorName());
            tMatchNotice.setUpdatedBy(cancelOrderNoticePO.getOperatorName());

            tMatchNotice.setBeginTime(nowTime);
            tMatchNotice.setEndTime(nowTime + TimeUnit.DAYS.toMillis(3));
            tMatchNotice.setSendTime(nowTime);
            tMatchNotice.setCreateTime(nowTime);
            tMatchNotice.setModifyTime(nowTime);
            int result = tMatchNoticeMapper.insert(tMatchNotice);
            if (result != CommonDefaultValue.ONE) {
                log.info("标题为：{},{}写入失败,原因：保存异常！", tMatchNotice.getTitle(), title);
            }

            MerchantNotice merchantNotice = new MerchantNotice();
            BeanUtils.copyProperties(tMatchNotice, merchantNotice);
            merchantNotice.setTMatchNoticeId(tMatchNotice.getId());
            merchantNotice.setNoticeTypeId(merchantNoticeId);
            merchantNotice.setStandardMatchId(cancelOrderNoticePO.getMatchId() + "");
            merchantNotice.setSportId(Long.parseLong(cancelOrderNoticePO.getSportId() + ""));
            merchantNotice.setId(null);
            merchantNotice.setNoticeResultType(cancelOrderNoticePO.getNoticeResultType());
            result = merchantNoticeMapper.insert(merchantNotice);
            if (result != CommonDefaultValue.ONE) {
                log.info("标题为：{},{}写入失败2,原因：保存异常！", tMatchNotice.getTitle(), title);
            }
        }
    }

    private void checkNotNullValue(ESportsNoticeEditReqVO editReqVO, TMatchNotice tMatchNotice) {
        if (ObjectUtil.isNotNull(editReqVO.getIsLoginPopup())) {
            tMatchNotice.setIsPop(editReqVO.getIsLoginPopup());
        }
        if (ObjectUtil.isNotNull(editReqVO.getIsImportant())) {
            tMatchNotice.setIsTag(editReqVO.getIsImportant());
        }
        if (ObjectUtil.isNotNull(editReqVO.getIsTop())) {
            tMatchNotice.setIsTop(editReqVO.getIsTop());
        }
        if (ObjectUtil.isNotNull(editReqVO.getIsRotation())) {
            tMatchNotice.setIsShuf(editReqVO.getIsRotation());
        }
        if (ObjectUtil.isNotNull(editReqVO.getNoticeType())) {
            tMatchNotice.setNoticeType(CommonUtil.getMatchNoticeType(editReqVO.getNoticeType()));
        }
        if (ObjectUtil.isNotNull(editReqVO.getPublishedBy())) {
            tMatchNotice.setUpdatedBy(editReqVO.getPublishedBy());
        }
        if (ObjectUtil.isNotNull(editReqVO.getEffectiveTime())) {
            tMatchNotice.setBeginTime(editReqVO.getEffectiveTime());
        }
        if (ObjectUtil.isNotNull(editReqVO.getEffectiveEndTime())) {
            tMatchNotice.setEndTime(editReqVO.getEffectiveEndTime());
        }
        if (ObjectUtil.isNotNull(editReqVO.getStatus())) {
            tMatchNotice.setStatus(editReqVO.getStatus());
        }
    }

    private void checkNotNullValue(ESportsNoticeAddReqVO addReqVO, TMatchNotice tMatchNotice) {
        if (ObjectUtil.isNotNull(addReqVO.getIsLoginPopup())) {
            tMatchNotice.setIsPop(addReqVO.getIsLoginPopup());
        }
        if (ObjectUtil.isNotNull(addReqVO.getIsImportant())) {
            tMatchNotice.setIsTag(addReqVO.getIsImportant());
        }
        if (ObjectUtil.isNotNull(addReqVO.getIsTop())) {
            tMatchNotice.setIsTop(addReqVO.getIsTop());
        }
        if (ObjectUtil.isNotNull(addReqVO.getIsRotation())) {
            tMatchNotice.setIsShuf(addReqVO.getIsRotation());
        }
        if (ObjectUtil.isNotNull(addReqVO.getNoticeType())) {
            tMatchNotice.setNoticeType(CommonUtil.getMatchNoticeType(addReqVO.getNoticeType()));
        }
        if (ObjectUtil.isNotNull(addReqVO.getPublishedBy())) {
            tMatchNotice.setUpdatedBy(addReqVO.getPublishedBy());
        }
        if (ObjectUtil.isNotNull(addReqVO.getEffectiveTime())) {
            tMatchNotice.setBeginTime(addReqVO.getEffectiveTime());
        }
        if (ObjectUtil.isNotNull(addReqVO.getEffectiveEndTime())) {
            tMatchNotice.setEndTime(addReqVO.getEffectiveEndTime());
        }
        if (ObjectUtil.isNotNull(addReqVO.getStatus())) {
            tMatchNotice.setStatus(addReqVO.getStatus());
        }
    }

    /**
     * 设置默认值
     *
     * @param addReqVO
     * @param multiTerminalNotice
     */
    private void checkNullValue(ESportsNoticeAddReqVO addReqVO, MultiTerminalNotice multiTerminalNotice) {
        // 判断空值赋值默认值
        if (ObjectUtil.isNull(addReqVO.getIsImportant())) {
            multiTerminalNotice.setIsImportant(CommonDefaultValue.DifferentiateStatus.NO);
        }
        if (ObjectUtil.isNull(addReqVO.getIsLoginPopup())) {
            multiTerminalNotice.setIsLoginPopup(CommonDefaultValue.DifferentiateStatus.NO);
        }
        if (ObjectUtil.isNull(addReqVO.getIsRotation())) {
            multiTerminalNotice.setIsRotation(CommonDefaultValue.DifferentiateStatus.NO);
        }

        Long nowTime = System.currentTimeMillis();
        if (ObjectUtil.isNull(addReqVO.getEffectiveTime())) {
            multiTerminalNotice.setEffectiveTime(nowTime);
        }
        if (ObjectUtil.isNull(addReqVO.getEffectiveEndTime())) {
            multiTerminalNotice.setEffectiveEndTime(TimeUnit.DAYS.toMillis(3) + nowTime);
        }
        if (ObjectUtil.isNull(addReqVO.getPublishedBy())) {
            multiTerminalNotice.setPublishedBy("Auto");
        }
    }

    /**
     * 公告内容追加
     *
     * @param jsonObject        公告json
     * @param content           原公告内容
     * @param languageEnum      语言类型
     */
    private String transferAddNotice(JSONObject jsonObject, String content, AggregateFestivalEnum languageEnum) {
        String market = jsonObject.getString("market");
        String newContent = jsonObject.getString("content");
        if (StringUtils.isBlank(content)) {
            return "";
        }
        if (StringUtils.isBlank(market)) {
            return content;
        }

        int fixLength = newContent.length() - newContent.indexOf(NOTICE_PREFIX) - NOTICE_PREFIX.length();
        String subContent = content.substring(0, content.length() - fixLength);
        String endContent = content.substring(content.length() - fixLength);
        String splitPrefix;
        if(languageEnum.getCode().equals(AggregateFestivalEnum.zh.getCode()) || languageEnum.getCode().equals(AggregateFestivalEnum.tw.getCode())){
            splitPrefix = "，";
        }else{
            splitPrefix = ",";
        }
        return String.format("%s%s%s%s", subContent, market,splitPrefix, endContent);
    }
}
