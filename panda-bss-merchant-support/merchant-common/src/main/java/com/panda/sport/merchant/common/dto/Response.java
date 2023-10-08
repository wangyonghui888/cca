
package com.panda.sport.merchant.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> implements Serializable {
    private int code = 200;
    private String msg = "";
    private T data = null;
    private Boolean success = null;
}
