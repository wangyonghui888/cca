package com.panda.multiterminalinteractivecenter.base;

import java.io.Serializable;

public interface BaseEnum extends Serializable {

    boolean isSuccess(); 
       
    String  getId();

    String getCode();

    String getLabel();
}
