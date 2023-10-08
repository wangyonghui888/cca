package com.panda.sport.merchant.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.sport.merchant.common.po.merchant.MerchantNotice;
import com.panda.sport.merchant.common.po.merchant.mq.MarketResultMessagePO;
import com.panda.sport.merchant.common.po.merchant.mq.NoticeMessagePo;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.entity.form.MerchantNoticeForm;

/**
 * @author YK
 * @Description: 公告
 * @date 2020/3/12 16:50
 */
public interface MerchantNoticeService extends IService<MerchantNotice> {

    /**
     * 添加公告
     * @param merchantNoticeForm
     * @return
     */
    Response addNotice(MerchantNoticeForm merchantNoticeForm,Integer userId,String language,String ip);

    /**
     * 编辑公告
     * @param merchantNoticeForm
     * @return
     */
    Response editNotice(MerchantNoticeForm merchantNoticeForm,Integer userId,String language,String ip);

    /**
     * 删除公告
     * @param id
     * @return
     */
    Response deleteNotice(Integer id,Integer userId,String language,String ip);

    /**
     * 取消发布
     * @param id
     * @return
     */
    Response cacelNotice(Integer id,Integer userId,String language,String ip);

    /**
     * 恢复发布
     * @param id
     * @return
     */
    Response backNotice(Integer id,Integer userId,String language ,String ip);

    /**
     * 删除
     */

    /**
     * 添加mq的公告的入库
     * @param marketResultMessagePO
     */
    void addOsmcMqNotice(MarketResultMessagePO marketResultMessagePO);


    /**
     *
     * @param tag
     * @param name
     * @return
     */
    Response getMerchantTree(Integer tag,String name);

}
