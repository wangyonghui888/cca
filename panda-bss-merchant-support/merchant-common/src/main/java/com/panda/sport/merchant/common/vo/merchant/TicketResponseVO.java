package com.panda.sport.merchant.common.vo.merchant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Project Name :panda-bss
 * @Package Name :com.panda.sports.bss.spi.usercenter.vo
 * @Description : 用户vo
 * @Date: 2019-10-10 21:29
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
@Data
public class TicketResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<TicketOrderSettle> list ;
    private Set<String> matchIdList ;
    private Map<String,Object> statistics  ;
    private Integer pageNum ;
    private Integer pageSize;
    private Long total;
    private String searchAfterId;
    private Long  timeId;
}
