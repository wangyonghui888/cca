package com.panda.sport.bc.controller;

import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * @ClassName MerchantController
 * @auth YK
 * @Description
 * @Date 2020-09-11 17:33
 * @Version
 */
@RestController
@RequestMapping("/bc/merchant")
@Slf4j
public class MerchantController {

    /**
    * @description:图片上传
    * @Param: [multipartFile]
    * @return: com.panda.sport.merchant.common.vo.Response<java.lang.Object>
    * @author: YK
    * @date: 2020/9/11 17:34
    */
    @PostMapping("/img/upload")
    public Response<Object> uploadImg(@RequestParam("imageFile") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty() || StringUtils.isBlank(multipartFile.getOriginalFilename())) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        String contentType = multipartFile.getContentType();
        if (!contentType.contains("")) {
            return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
        }
        String root_fileName = multipartFile.getOriginalFilename();
        BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        String res = encoder.encodeBuffer(multipartFile.getBytes()).trim();
        log.info("上传图片:name={},type={}", root_fileName, contentType);
        return Response.returnSuccess(res);
    }
}
