package com.panda.sport.api.entity;

import java.io.Serializable;

public interface BaseEnum extends Serializable {

    boolean isSuccess();

    String getId();

    String getCode();

    String getLabel();
}
