package com.panda.sport.admin.controller;

import com.panda.sport.admin.feign.MerchantApiClient;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.LogTagEnum;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.vo.MerchantConfigVo;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.MerchantConfigEditReqVO;
import com.panda.sport.merchant.manage.service.impl.MerchantConfigServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.merchant.manage.controller
 * @Description :  TODO
 * @Date: 2021-02-04 13:43:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@RestController
@RequestMapping("/admin/config")
@Slf4j
public class MerchantConfigController {

    @Autowired
    private MerchantConfigServiceImpl merchantConfigService;

    @Resource
    private MerchantApiClient merchantApiClient;

    @GetMapping("/getConfig")
    @ApiOperation(value = "/admin/config/getConfig", notes = "账户中心/商户信息管理/C端综合设置-获取设置信息")
    public Response queryByMerchantCode(@Param("merchantCode") String merchantCode){
        return Response.returnSuccess(merchantConfigService.findByMerchantCode(merchantCode));
    }

    @PostMapping("/uploadImgUrl")
    @ApiOperation(value = "/admin/config/uploadImgUrl", notes = "账户中心/商户信息管理/上传图片")
    public Response uploadImgUrl(@RequestBody MerchantConfigVo merchantConfigVo){
        String logoUrl = merchantConfigVo.getUrl();
        // 获取文件的后缀名
        String suffixName = StringUtils.isNotEmpty(logoUrl)?logoUrl.substring(logoUrl.lastIndexOf(".")):"";
        log.info("文件的后缀名为：" + suffixName);
        //2056需求，只接收图片（png、jpeg、tif、gif、bmp、jpg等）类型的文件上传
        Set<String> typeSet = new HashSet<>();
        typeSet.add(".png");
        typeSet.add(".jpeg");
        typeSet.add(".tif");
        typeSet.add(".gif");
        typeSet.add(".bmp");
        typeSet.add(".jpg");
        if (!typeSet.contains(suffixName.toLowerCase())) {
            //表⽰这个文件类型不是图⽚格式附件，徐重新上传。
            log.error("上传的文件格式为非图片格式："+ suffixName);
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        merchantConfigService.updateLogoUrl(merchantConfigVo.getMerchantCode(),merchantConfigVo.getTag(),logoUrl);
        return Response.returnSuccess();
    }

    @PostMapping("/updateConfig")
    @ApiOperation(value = "/admin/config/updateConfig", notes = "账户中心/商户信息管理/C端综合设置-修改设置信息")
    public Response updateConfig(HttpServletRequest request, @RequestBody @Valid MerchantConfigEditReqVO merchantConfig ) {
        // 获取文件的后缀名
        String darkLogoUrl = merchantConfig.getDarkLogoUrl();
        String whiteLogoUrl = merchantConfig.getWhiteLogoUrl();
        String pcLogoUrl = merchantConfig.getPcLogoUrl();
        String compatLogoUrl = merchantConfig.getCompatLogoUrl();
        String videoLogoUrl = merchantConfig.getVideoLogoUrl();
        darkLogoUrl = StringUtils.isNotEmpty(darkLogoUrl) ? darkLogoUrl.substring(darkLogoUrl.lastIndexOf(".")) : "";
        whiteLogoUrl = StringUtils.isNotEmpty(whiteLogoUrl) ? whiteLogoUrl.substring(whiteLogoUrl.lastIndexOf(".")) : "";
        pcLogoUrl = StringUtils.isNotEmpty(pcLogoUrl) ? pcLogoUrl.substring(pcLogoUrl.lastIndexOf(".")) : "";
        compatLogoUrl = StringUtils.isNotEmpty(compatLogoUrl) ? compatLogoUrl.substring(compatLogoUrl.lastIndexOf(".")) : "";
        videoLogoUrl = StringUtils.isNotEmpty(videoLogoUrl) ? videoLogoUrl.substring(videoLogoUrl.lastIndexOf(".")) : "";

        log.info("dark的后缀名为：" + darkLogoUrl + "==" + whiteLogoUrl + "==" + pcLogoUrl + "==" + compatLogoUrl + "==" + videoLogoUrl);
        //2056需求，只接收图片（png、jpeg、tif、gif、bmp、jpg等）类型的文件上传
        Set<String> typeSet = new HashSet<>();
        Set<String> typeUrl = new HashSet<String>();
        typeSet.add(".png");
        typeSet.add(".jpeg");
        typeSet.add(".tif");
        typeSet.add(".gif");
        typeSet.add(".bmp");
        typeSet.add(".jpg");
        if (StringUtils.isNotEmpty(darkLogoUrl)) {
            typeUrl.add(darkLogoUrl);
        }
        if (StringUtils.isNotEmpty(whiteLogoUrl)) {
            typeUrl.add(whiteLogoUrl);
        }
        if (StringUtils.isNotEmpty(pcLogoUrl)) {
            typeUrl.add(pcLogoUrl);
        }
        if (StringUtils.isNotEmpty(compatLogoUrl)) {
            typeUrl.add(compatLogoUrl);
        }
        if (StringUtils.isNotEmpty(videoLogoUrl)) {
            typeUrl.add(videoLogoUrl);

        }
        typeUrl.removeAll(typeSet);
        if (!typeUrl.isEmpty()) {
            //表⽰这个文件类型不是图⽚格式附件，徐重新上传。
            log.error("上传的文件格式为非图片格式：" + darkLogoUrl);
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);

        }
            try {
                log.info("url:manage/admin/config/updateConfig;param:{}", merchantConfig.toString());

                JwtUser user = SecurityUtils.getUser();
                // 获取系统语言
                String language = request.getHeader("language");
                if (org.apache.commons.lang3.StringUtils.isEmpty(language))
                    language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;

                // 设置日志标识
                merchantConfig.setLogTag(LogTagEnum.MERCHANT_END.getCode());

                merchantConfigService.updateConfig(merchantConfig, user.getId(), user.getUsername(), language, IPUtils.getIpAddr(request));
                merchantApiClient.updateMerchantUserCache(merchantConfig.getMerchantCode());
                return Response.returnSuccess();
            } catch (Exception e) {
                return Response.returnFail(e.getMessage());
            }
    }

}
