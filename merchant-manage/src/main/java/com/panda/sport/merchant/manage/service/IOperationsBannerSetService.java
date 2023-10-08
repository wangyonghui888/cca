package com.panda.sport.merchant.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.panda.sport.merchant.common.po.bss.OperationsBannerSet;
import com.panda.sport.merchant.common.vo.MerchantTreeVO;
import com.panda.sport.merchant.common.vo.OperationsBannerVO;
import com.panda.sport.merchant.common.vo.OperationsVO;
import com.panda.sport.merchant.common.vo.Response;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/8/20 12:05:24
 */
public interface IOperationsBannerSetService extends IService<OperationsBannerSet> {
    /**
     * 管理商户树形接口查询
     */
    List<MerchantTreeVO> getMerchantList(MerchantTreeVO paramVO);

    /**
     * 返回banner page
     */
    PageInfo<OperationsBannerVO> getBannerList(OperationsVO parmVO);

    Response<?> save(HttpServletRequest request,OperationsBannerVO paramVO);

    Response<?> update(HttpServletRequest request,OperationsBannerVO paramVO);

    Response<?> delete(OperationsVO paramVO);

    Response<?> deleteKey(String key);
}
