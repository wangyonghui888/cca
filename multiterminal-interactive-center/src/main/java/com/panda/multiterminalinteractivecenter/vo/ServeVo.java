package com.panda.multiterminalinteractivecenter.vo;

import com.panda.multiterminalinteractivecenter.entity.MaintenancePlatform;
import lombok.Data;

import java.util.List;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.vo
 * @Description :  TODO
 * @Date: 2022-03-19 13:12:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class ServeVo {

    private String dataCode;

    private List<MaintenancePlatform> serves;

}
