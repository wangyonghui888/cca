package com.panda.multiterminalinteractivecenter.vo;

import com.panda.multiterminalinteractivecenter.entity.MaintenanceRecord;
import lombok.Data;

import java.util.List;

/**
 * @author lary
 * @version 1.0.0
 * @ClassName HomeVo.java
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.vo
 * @Description TODO
 * @createTime 2022/03/21
 */
@Data
public class HomeVo {

    private String dataCode;
    private List<MaintenanceRecord> records;
    /**赛事信息*/
    private List<MatchInfo> matchInfo;

}
