package com.panda.sport.merchant.common.bo.sport;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.panda.sport.merchant.common.po.bss.SportFilterVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shuan
 * @version v1.0
 * @date 9/3/23 11:01 am
 * @description
 */
@ConfigurationProperties(prefix = "sport")
@RefreshScope
@Component
@Data
public class SportFilterBO {

    /**
     * -1    C01足球
     * -2    O01足球
     * -3    B03足球
     * -4    B03篮球
     */
    @NacosValue(value = "${sport.sportIds:-1,-2,-3,-4}", autoRefreshed = true)
    private String strSportIds;

    /**
     * 新增球种code
     */
    @NacosValue(value = "${sport.sportCodes:C01-1,O01-2,B03-3,B03-4}", autoRefreshed = true)
    private String strSportCodes;

    /**
     * 新增球种name
     */
    @NacosValue(value = "${sport.sportNames:C01-足球,O01-足球,B03-足球,B03-篮球}", autoRefreshed = true)
    private String strSportNames;
    /**
     * 球种
     */

    private List<SportFilterVo> sportFilterList = new ArrayList<>();

    @EventListener(ContextRefreshedEvent.class)
    protected void refresh() {
        this.loadData();
    }

    @PostConstruct
    public void loadData() {
        String[] sportIds = strSportIds.split(",");
        String[] sportCodes = strSportCodes.split(",");
        String[] sportNames = strSportNames.split(",");
        int size = sportCodes.length > sportNames.length ? sportNames.length : sportCodes.length;
        size = size > sportIds.length ? sportIds.length : size;
        sportFilterList.clear();
        for (int i = 0; i < size; i++) {
            String sportCode = sportCodes[i];
            String sportName = sportNames[i];
            String sportId = sportIds[i];
            sportFilterList.add(new SportFilterVo() {{
                setId(Long.valueOf(sportId));
                setNameCode(sportCode);
                setName(sportName);
            }});
        }
    }
}