package com.panda.sport.merchant.common.vo.merchant;/**
 * @author Administrator
 * @date 2021/5/14
 * @TIME 17:59
 */

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 *@ClassName AgentVO
 *@Description TODO
 *@Author Administrator
 *@Date 2021/5/14 17:59
 */
@Setter
@Getter
public class AgentVO implements Serializable {
    private String agentId;
    private String agentName;
    private String merchantCode;
}
