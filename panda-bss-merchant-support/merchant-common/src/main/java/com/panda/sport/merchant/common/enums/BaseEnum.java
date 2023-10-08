package com.panda.sport.merchant.common.enums;

import java.io.Serializable;

public interface BaseEnum extends Serializable {

    boolean isSuccess(); 
       
    String  getId();

    String getCode();

    String getLabel();
}
