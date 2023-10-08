package com.panda.sport.merchant.common.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/8/24 18:30:09
 */
@Getter
@Setter
@ToString
@Slf4j
public class SportAndPlayTreeVO implements Serializable, Cloneable {

    private static final long serialVersionUID = -8524804649552178283L;

    private static SportAndPlayTreeVO sportAndPlayTreeVO = new SportAndPlayTreeVO();

    private SportAndPlayTreeVO() {
        super();
    }

    /**
     * 树 title
     */
    private String title;

    /**
     * 树 key
     */
    private Integer key;

    /**
     * 父级 id
     */
    private Integer parentId;

    /**
     * 子节点
     */
    private List<SportAndPlayTreeVO> children;

    public static SportAndPlayTreeVO getInstance() {
        try {
            return (SportAndPlayTreeVO) sportAndPlayTreeVO.clone();
        } catch (CloneNotSupportedException e) {
            log.error("SportAndPlayTreeVO clone error", e);
        }
        return new SportAndPlayTreeVO();
    }
}

