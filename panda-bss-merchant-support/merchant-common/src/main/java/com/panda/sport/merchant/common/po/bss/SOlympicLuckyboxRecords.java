package com.panda.sport.merchant.common.po.bss;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


@Getter @Setter @ToString
@Accessors(chain = true)
public class SOlympicLuckyboxRecords extends SLuckyboxRecords {
	private static final long serialVersionUID = 1L;
	private Long boxId;
	private String remark;
	
}