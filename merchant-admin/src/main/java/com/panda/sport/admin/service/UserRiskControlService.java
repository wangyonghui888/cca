package com.panda.sport.admin.service;

import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.UserRiskControlPageQueryReqVO;
import com.panda.sport.merchant.common.vo.UserRiskControlQueryAllReqVO;
import com.panda.sport.merchant.common.vo.UserRiskControlStatusEditReqVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author :  ives
 * @Description :  对外商户 账户中心/平台用户风控 实现接口
 * @Date: 2022-04-09 18:43
 */
public interface UserRiskControlService {


    /**
     * 查询用户风控分页列表
     * @param queryReqVO
     * @return Response<?>
     */
    Response<?> queryPageUserRiskControlList(UserRiskControlPageQueryReqVO queryReqVO);

    /**
     * 修改用户风控状态
     * @param editReqVO
     * @return boolean
     */
    Response<?> updateUserRiskControlStatus(UserRiskControlStatusEditReqVO editReqVO);

    /**
     * 商户管控记录表导出
     * @param reqVO
     * @param httpServletResponse
     * @return Response
     */
    void exportUserRiskControlList(UserRiskControlQueryAllReqVO reqVO, HttpServletResponse httpServletResponse);

    /**
     * 商户管控记录表批量导入
     * @param request
     * @param multipartFile
     * @return Response
     */
    Response<?> importUserRiskControlList(HttpServletRequest request, MultipartFile multipartFile);

    /**
     * 获取待处理状态的风控消息数量
     * @return Response
     */
    Response<?> pendingCount();
}
