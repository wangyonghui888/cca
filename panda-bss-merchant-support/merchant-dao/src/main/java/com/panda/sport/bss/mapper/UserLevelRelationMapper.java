package com.panda.sport.bss.mapper;

import com.panda.sport.merchant.common.po.bss.UserLevelRelationVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserLevelRelationMapper {

    UserLevelRelationVO getUserLevelRelationByUid(@Param("uid") Long uid);

    int addUserLevelRelation(UserLevelRelationVO userLevelRelationVO);

    int updateUserLevelRelation(UserLevelRelationVO userLevelRelationVO);

}
