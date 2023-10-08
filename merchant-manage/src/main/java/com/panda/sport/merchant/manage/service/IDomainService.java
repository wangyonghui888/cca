package com.panda.sport.merchant.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.sport.merchant.common.dto.ApiMerchantGroupVO;
import com.panda.sport.merchant.common.po.bss.FrontendMerchantGroupDomainPO;
import com.panda.sport.merchant.common.po.bss.TDomain;
import com.panda.sport.merchant.common.po.bss.VideoMerchantGroupDomainPO;
import com.panda.sport.merchant.common.vo.DomainVo;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * oss域名表 服务类
 * </p>
 *
 * @author Baylee
 * @since 2021-05-28
 */
public interface IDomainService extends IService<TDomain> {

    /**
     * 删除
     *
     * @param userId
     * @param id
     */
    void deleteDomain(Integer userId, Long id,String ip, HttpServletRequest request);

    /**
     * 恢复使用
     *
     * @param userId
     * @param id
     */
    void resetDomain(Integer userId, Long id);

    /**
     * 删除
     *
     * @param userId
     * @param domainVo
     */
    void deleteDomainAll(Integer userId, DomainVo domainVo,String ip, HttpServletRequest request);

    /**
     * 列表
     *
     * @param domainVo
     * @return
     */
    Response queryList(DomainVo domainVo);

    /**
     * 导入
     *
     * @param file
     * @return
     */
    Response importDomain(Integer userId, MultipartFile file);

    /**
     * 保存接口
     *
     * @param domainVo
     * @return
     */
    Response saveDomain(Integer userId, DomainVo domainVo,HttpServletRequest request);


    /**
     * 查询
     *
     * @return
     */
    Response queryAnimation();

    /**
     * 更新
     *
     * @param url
     * @return
     */
    Response updateAnimation(HttpServletRequest request,String url);

    /**
     * 清除缓存
     *
     * @return
     */
    Response deleteAniCache(HttpServletRequest request);

    /**
     * 强刷商户组缓存
     *
     * @param domain
     * @param groupId
     */
    Response updateMerchantDomain(Integer userId, String oldDomain, String domain, Long groupId,String ip,Integer domaintType);


    /**
     * 获取api域名
     *
     * @return
     */
    List<ApiMerchantGroupVO> apiMerchantGroupList();

    /**
     * 添加商户api分组
     *
     * @param apiMerchantGroupVO
     * @return
     */
    Response addApiMerchantGroup(ApiMerchantGroupVO apiMerchantGroupVO);

    /**
     * 更新商户api分组
     *
     * @param apiMerchantGroupVO
     * @return
     */
    Response updateApiMerchantGroup(ApiMerchantGroupVO apiMerchantGroupVO);

    /**
     * 删除api分组
     *
     * @param apiMerchantGroupVO
     * @return
     */
    Response deleteApiMerchantGroup(ApiMerchantGroupVO apiMerchantGroupVO);

     boolean checkDomainByGroup(String api,String groupCode);

    APIResponse<Object> queryFrontendMerchantDomain(FrontendMerchantGroupDomainPO frontendMerchantGroup);

    APIResponse<Object> queryVideoMerchantDomain(VideoMerchantGroupDomainPO frontendMerchantGroup);
}
