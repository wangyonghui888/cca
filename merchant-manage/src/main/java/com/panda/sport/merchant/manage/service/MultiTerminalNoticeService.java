package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.common.po.merchant.mq.CancelOrderNoticePO;
import com.panda.sport.merchant.common.vo.ESportsNoticeAddReqVO;
import com.panda.sport.merchant.common.vo.ESportsNoticeDelReqVO;
import com.panda.sport.merchant.common.vo.ESportsNoticeEditReqVO;
import com.panda.sport.merchant.common.vo.Response;

/**
 * @author :  ives
 * @Description :  多端公告处理服务接口
 * @Date: 2022-03-09 11:32
 */
public interface MultiTerminalNoticeService {

    /**
     * 供电竞部门调用，添加到本地公告信息
     * @param addReqVO
     * @return Response
     */
    Response addESportsNotice(ESportsNoticeAddReqVO addReqVO ,String ip);

    /**
     * 供电竞部门调用，删除电竞写入的本地公告信息
     * @param delReqVO
     * @return Response
     */
    Response delESportsNotice(ESportsNoticeDelReqVO delReqVO);

    /**
     * 供电竞部门调用，修改录入本地的公告信息
     * @param editReqVO
     * @return Response
     */
    Response editESportsNotice(ESportsNoticeEditReqVO editReqVO ,String ip);

    /**
     * 保存二次结算&取消注单自动公告
     * @param cancelOrderNoticePO
     */
    void saveCancelOrderNotice(CancelOrderNoticePO cancelOrderNoticePO);

}
