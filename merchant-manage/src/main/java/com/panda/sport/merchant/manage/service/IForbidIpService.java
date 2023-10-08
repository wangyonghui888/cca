package com.panda.sport.merchant.manage.service;

import com.panda.sport.merchant.common.vo.DomainVo;
import com.panda.sport.merchant.common.vo.Response;

import javax.servlet.http.HttpServletRequest;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.manage.service
 * @Description :  TODO
 * @Date: 2021-09-08 11:48:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public interface IForbidIpService {

    public Response queryList(DomainVo domainVo);

    void deleteIp(HttpServletRequest request,Long id);

    void deleteIpByName(HttpServletRequest request,String ipName);

    Response saveIp(HttpServletRequest request,DomainVo domainVo, String username);
}
