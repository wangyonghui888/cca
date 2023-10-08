package com.panda.sport.merchant.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserAllowListReq {

    private Integer pageNum;

    private Integer pageSize;

    private String userId;

    private String userName;

    /**id,name混合查询*/
    private String userInfo;

    private List<String> userIdList;

    private List<String> merchantCodeList;

    /**
     * @see  com.panda.sport.merchant.common.enums.UserAllowListSourceEnum
     */
    private Integer disabled;

    /**同disabled*/
    private Integer allowListSource;

    private String language;

}
