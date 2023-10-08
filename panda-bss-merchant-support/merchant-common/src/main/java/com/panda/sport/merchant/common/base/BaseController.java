
package com.panda.sport.merchant.common.base;

import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.vo.api.APIResponse;

/**
 * @author: Spring
 * @version V1.0.0
 * @Project Name : panda-bss
 * @Package Name : com.panda.sports.bss.common.base
 * @Description: 前端控制器公共类
 * @date: 2019年9月24日 下午1:20:52
 */
public class BaseController<T> {
	
	public APIResponse<T> apiResponse(boolean isOk) {

		if (isOk) {
			return APIResponse.returnSuccess(ApiResponseEnum.SUCCESS);
		}
		return APIResponse.returnFail(ApiResponseEnum.INTERNAL_ERROR);
	}
}
