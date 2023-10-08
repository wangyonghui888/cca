package com.panda.sport.merchant.manage.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.sport.merchant.common.po.bss.OssDomain;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.manage.entity.form.WSResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * oss域名表 服务类
 * </p>
 *
 * @author Baylee
 * @since 2021-05-28
 */
public interface IOssDomainService extends IService<OssDomain> {

    /**
     * 入库并更新oss
     *
     * @param ossDomain ossDomain
     */
    boolean saveAndUploadOss(List<OssDomain> ossDomain);

    boolean ossUpload(JSONObject obj);

    void processWSMessage(WSResponse wsResponse);

    void processWSMessage2(WSResponse wsResponse);

    String switchDomain(Integer i, String domain, String selfTest,String code,Long groupIdNew);

    void getDomainConfig(HttpServletResponse response);
}
