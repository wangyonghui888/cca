package com.panda.sport.merchant.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
    private Long uid;
    private Long lastLogin;
}
