package com.panda.sport.merchant.common.vo.user;


import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class UserRetryTransferVO implements Serializable {


    private List<String> transferId;

    private List<String> transferIdList;

    private String userName;

    private Long retryCount;
}
