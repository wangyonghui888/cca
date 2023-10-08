package com.panda.sport.backup.mapper;

import com.panda.sport.merchant.common.bo.UserInfoBO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 用户日志查询DB
 * @Author: Jeffrey
 * @Date: 2020/1/29
 */
@Repository
public interface BackupUserLogHistoryMapper {

    void batchInsertOrUpdate(List<UserInfoBO> uList);
}