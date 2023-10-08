package com.panda.center.mapper.activity;

import java.io.Serializable;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panda.center.mapper.activity.TActivityConfigMapper.TActivityConfig;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 账户表 Mapper 接口
 * </p>
 *
 * @author Auto Generator
 * @since 2020-01-23
 */
public interface TActivityConfigMapper extends BaseMapper<TActivityConfig> {


	@Data
	@Accessors(chain = true)
	public class TActivityConfig implements Serializable {

	    private static final long serialVersionUID = 1L;
	    /**
	     *id
	     * */
	    private Long id;
	    private Long inStartTime;
	    private Long inEndTime;
	    private Integer status;

	}
}
