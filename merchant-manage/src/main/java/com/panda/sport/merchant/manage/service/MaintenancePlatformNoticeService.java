package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.common.dto.MaintenanceRecordDTO;

/**
 * @author :  ives
 * @Description :  TODO
 * @Date: 2022-04-10 11:56
 */
public interface MaintenancePlatformNoticeService {
    /**
     * 维护平台发送公告给体育
     * @param maintenanceRecordDTO
     * @return boolean
     */
    void sendStartMaintainRemindAndNoticeToSport(MaintenanceRecordDTO maintenanceRecordDTO);
}
