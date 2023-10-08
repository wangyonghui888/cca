package com.panda.sport.merchant.manage.entity.form;

import lombok.Data;

import java.io.Serializable;

@Data
public class DomainCheckPost implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;
    private String domain;
    private String domaintype;

}
