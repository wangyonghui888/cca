package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.common.vo.MerchantVideoManageVo;
import com.panda.sport.merchant.common.vo.Response;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IMerchantVideoManageService {

    int saveMerchantVideoManage(MerchantVideoManageVo videoManageVo);

    MerchantVideoManageVo getMerchantVideoManage(String merchantCode);

    Response getVideoManageList(MerchantVideoManageVo videoManageVo);

    int batchUpdateMerchantVideoManage(MerchantVideoManageVo videoManageVo);

    Response getMerchantVideoManageList();

    int updateMerchantVideoManage(HttpServletRequest request,MerchantVideoManageVo videoManageVo);
}
