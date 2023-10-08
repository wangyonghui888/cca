package com.panda.sport.merchant.common.dto;

import com.panda.sport.merchant.common.po.bss.BaseVO;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.panda.sport.merchant.common.constant.Constant.LANGUAGE_CHINESE_SIMPLIFIED;

/**
 * @author :  toney
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.common.vo.activity
 * @Description :  活动投注统计
 * @Date: 2021-08-21 11:16
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Data
public class ActivityBetStatDTO extends BaseVO {
    private static final long serialVersionUID = 1L;


    /**
     * 用户id
     */
    private List<String> uidList;


    /**
     * 商户名称
     */
    private List<String> merchantCodeList;


    /**
     * 搜索开始时间
     */
    private String startTime;


    /**
     * 搜索结束时间
     */
    private String endTime;


    /**
     * 分页相关
     */
    private Integer pageNum;

    private Integer pageSize;


    private String language;

    private String operUsername;


    public String getLanguage() {
        if(StringUtils.isEmpty(language))
        {
            return LANGUAGE_CHINESE_SIMPLIFIED;
        }
        return language;
    }
}
