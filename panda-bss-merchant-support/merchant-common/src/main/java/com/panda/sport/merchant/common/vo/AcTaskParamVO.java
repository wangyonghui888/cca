package com.panda.sport.merchant.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/8/25 11:57:27
 */
@Data
public class AcTaskParamVO implements Serializable, Cloneable {

    private static final long serialVersionUID = 5858357210923120818L;

    private Integer symbol;

    private Integer conditionId;

    private Integer beforeValue;

    private Integer afterValue;

    private String startTime;

    private String endTime;

    private String playInfoList;

    private String matchInfoList;
    private List<Integer> playInfoLists;

    private List<Long> matchInfoLists;

    public Long beforeTime;

    public Long afterTime;

    public String beforeDate;

    public String afterDate;

    public List<Long> haveUserList;

    public List<Long> userList;


}
