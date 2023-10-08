package com.panda.center.service;



import com.github.pagehelper.PageInfo;
import com.panda.center.vo.ActivityBetStatDTO;
import com.panda.center.vo.ActivityBetStatVO;

import java.util.List;

/**
 * @author :  toney
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.center.service
 * @Description :  TODO
 * @Date: 2021-12-25 10:17
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public interface ActivityService {

	PageInfo<ActivityBetStatVO> getActivityBetStatList(ActivityBetStatDTO vo) ;

	/**
	 * 导出excel
	 * @param vo
	 * @return
	 */
	List<ActivityBetStatVO> exportExcel(ActivityBetStatDTO vo);

}
