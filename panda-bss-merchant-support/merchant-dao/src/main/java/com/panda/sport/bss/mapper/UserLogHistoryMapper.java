package com.panda.sport.bss.mapper;

import com.panda.sport.merchant.common.po.merchant.UserLogHistoryPO;
import com.panda.sport.merchant.common.vo.user.UserLogHistoryVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 用户日志查询DB
 * @Author: Jeffrey
 * @Date: 2020/1/29
 */
@Repository
public interface UserLogHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserLogHistoryPO record);

    int insertSelective(UserLogHistoryPO record);

    UserLogHistoryPO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserLogHistoryPO record);

    int updateByPrimaryKey(UserLogHistoryPO record);

    /**
     * 查询近7天用户登录
     *
     * @param record
     * @return List<UserLogHistoryPO>
     */
    List<UserLogHistoryPO> selectAllByPage(UserLogHistoryPO record);

    List<UserLogHistoryPO> queryRecentUserList(@Param(value = "day") Integer day);

    List<Long> queryLoginedUserList();
}