package com.panda.sport.merchant.manage.service.impl;

import cn.hutool.core.date.DateUtil;
import com.panda.sport.match.mapper.TMatchNoticeMapper;
import com.panda.sport.merchant.common.constant.CommonDefaultValue;
import com.panda.sport.merchant.common.dto.MaintenanceRecordDTO;
import com.panda.sport.merchant.common.po.match.TMatchNotice;
import com.panda.sport.merchant.common.po.merchant.MerchantNotice;
import com.panda.sport.merchant.manage.service.MaintenancePlatformNoticeService;
import com.panda.sport.merchant.manage.util.CommonUtil;
import com.panda.sport.merchant.mapper.MerchantNoticeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author :  ives
 * @Description :  给体育发信息处理类
 * @Date: 2022-04-10 10:51
 */
@Service
@Slf4j
public class MaintenancePlatformNoticeServiceImpl implements MaintenancePlatformNoticeService {

    @Resource
    private TMatchNoticeMapper tMatchNoticeMapper;

    @Resource
    private MerchantNoticeMapper merchantNoticeMapper;

    private static final String title = "维护公告";
    private static final String enTitle = "Maintenance Announcement";
    private static final String zhTitle = "維護公告";
    private static final String viTitle = "Thông báo bảo trì";

    private static final String format = "yyyy/MM/dd HH:mm:ss";


    /**
     * 维护平台发送公告给体育
     * @param maintenanceRecordDTO
     */
    @Override
    public void sendStartMaintainRemindAndNoticeToSport(MaintenanceRecordDTO maintenanceRecordDTO){
        Long nowTime = System.currentTimeMillis();
        Date startTime = DateUtil.date(maintenanceRecordDTO.getMaintenanceStartTime());
        Date endTime = DateUtil.date(maintenanceRecordDTO.getMaintenanceEndTime());
        String startTimeStr = DateUtil.format(startTime, format);
        String endTimeStr = DateUtil.format(endTime, format);
        Long timeDifference = TimeUnit.MILLISECONDS.toMinutes(maintenanceRecordDTO.getMaintenanceStartTime() - System.currentTimeMillis());
        String tempTitle = "维护平台体育开始维护时发送公告至体育";
        Integer maintainNoticeType = 3;
        Integer tMerchantNoticeId = CommonUtil.getMatchNoticeType(maintainNoticeType);

        String publishedBy = "Auto";
        String content = "【维护公告】为了给您带来更好的体验，我们将在" + timeDifference + "分钟之后即" + startTimeStr + "-"+ endTimeStr + "进行系统维护，期间将暂时无法使用登录系统，给您带来不便深表歉意！";
        String enContent = "[Maintenance Announcement] In order to bring you a better experience，the system will be maintained after " + timeDifference + " minutes from "+ startTimeStr + "-"+ endTimeStr + ", during which the login system will be temporarily unavailable. Sorry for the inconvenience!";
        String zhContent = "【維護公告】為了給您帶來更好的體驗，我們將在" + timeDifference + "分鐘之後即" + startTimeStr + "-"+ endTimeStr + "進行系統維護，期間將暫時無法使用登錄系統，給您帶來不便深表歉意！";
        String viContent = "[Thông báo bảo trì] Để mang đến cho bạn trải nghiệm tốt hơn, chúng tôi sẽ tiến hành bảo trì hệ thống trong "+ timeDifference + " phút từ "+ startTimeStr + "-"+ endTimeStr + ", trong thời gian này hệ thống đăng nhập sẽ tạm thời không hoạt động. Xin lỗi vì sự bất tiện!";

        TMatchNotice tMatchNotice = new TMatchNotice();

        tMatchNotice.setTitle(title);
        tMatchNotice.setContext(content);
        tMatchNotice.setEnTitle(enTitle);
        tMatchNotice.setEnContext(enContent);
        tMatchNotice.setZhTitle(zhTitle);
        tMatchNotice.setZhContext(zhContent);
        tMatchNotice.setViTitle(viTitle);
        tMatchNotice.setViContext(viContent);

        // 发布范围,1直营商户，2渠道商户, 3二级商户,5内部用户,6所有商户投注用户,8信用网
        tMatchNotice.setReleaseRange("1,2,3,5,6,8");
        tMatchNotice.setStatus(1);
        tMatchNotice.setNoticeType(tMerchantNoticeId);

        tMatchNotice.setIsPop(CommonDefaultValue.DifferentiateStatus.NO);
        tMatchNotice.setIsTag(CommonDefaultValue.DifferentiateStatus.NO);
        tMatchNotice.setIsTop(CommonDefaultValue.DifferentiateStatus.YES);
        tMatchNotice.setIsShuf(CommonDefaultValue.DifferentiateStatus.NO);

        tMatchNotice.setCreatedBy(publishedBy);
        tMatchNotice.setUpdatedBy(publishedBy);
        tMatchNotice.setBeginTime(nowTime);
        tMatchNotice.setEndTime(maintenanceRecordDTO.getMaintenanceStartTime());

        tMatchNotice.setSendTime(nowTime);
        tMatchNotice.setCreateTime(nowTime);
        tMatchNotice.setModifyTime(nowTime);
        int result = tMatchNoticeMapper.insert(tMatchNotice);
        if (result != CommonDefaultValue.ONE){
            log.error("{}异常,原因：保存异常！", tempTitle);
            return;
        }

        MerchantNotice merchantNotice = new MerchantNotice();
        BeanUtils.copyProperties(tMatchNotice, merchantNotice);
        merchantNotice.setTMatchNoticeId(tMatchNotice.getId());
        merchantNotice.setNoticeTypeId(maintainNoticeType);
        merchantNotice.setId(null);
        result = merchantNoticeMapper.insert(merchantNotice);
        if (result != CommonDefaultValue.ONE){
            log.error("{}异常,原因：保存异常！", tempTitle);
            return;
        }
        log.info("{}成功！", tempTitle);
    }
}
