package com.panda.sport.admin.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.panda.sport.admin.feign.PandaRcsCreditClient;
import com.panda.sport.admin.service.OutMerchantService;
import com.panda.sport.admin.utils.DistributedLockerUtil;
import com.panda.sport.admin.utils.EncryptUtils;
import com.panda.sport.admin.utils.JwtTokenUtil;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.dto.CreditConfigDto;
import com.panda.sport.merchant.common.dto.CreditConfigHttpQueryDto;
import com.panda.sport.merchant.common.dto.CreditConfigSaveDto;
import com.panda.sport.merchant.common.dto.Request;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.enums.api.ApiResponseEnum;
import com.panda.sport.merchant.common.po.bss.TMerchantKey;
import com.panda.sport.merchant.common.utils.CreateAdminPassword;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.UUIDUtils;
import com.panda.sport.merchant.common.vo.MerchantOrderOperationVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.api.APIResponse;
import com.panda.sport.merchant.common.vo.merchant.AdminUserNameUpdateReqVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/admin/merchant")
@Slf4j
public class MerchantController {

    @Resource
    private OutMerchantService outMerchantService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private static final String decoty = "/opt/merchant/upload/";

    @Autowired
    private PandaRcsCreditClient pandaRcsCreditClient;

    @Autowired
    private DistributedLockerUtil distributedLockerUtil;

    /**
     * 用户中心缓存family key
     */
    public static final String Cencal_order_KEY = "cancelOrderkey:";

    /**
     * 商户列表查询
     *
     * @param request
     * @param merchantName
     * @param status
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GetMapping(value = "/merchantList")
    @PreAuthorize("hasAnyRole('secondary')")
    public Response merchantList(HttpServletRequest request, @RequestParam(value = "merchantName", required = false) String merchantName,
                                 @RequestParam(value = "status", required = false) Integer status,
                                 @RequestParam(value = "merchantTag", required = false) Integer merchantTag,
                                 @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                 @RequestParam(value = "pageNum", required = false) Integer pageNum) {
        log.info("admin/merchant/merchantList,merchantName:" + merchantName + ",status:" + status +
                ",pageSize:" + pageSize + ",pageIndex:" + pageNum);
        return outMerchantService.selectList(request, merchantName, status, merchantTag, pageNum, pageSize);
    }

    /**
     * 商户详情
     *
     * @param request
     * @param id
     * @return
     */
    @GetMapping(value = "/getDetail")
    @PreAuthorize("hasAnyRole('basic')")
    public Response getDetail(HttpServletRequest request, @RequestParam(value = "id", required = false) String id) {
        log.info("admin/merchant/getDetail" + id);
        return outMerchantService.getMerchantDetail(id);
    }

    /**
     * 新建商户
     *
     * @param request
     * @param merchantVO
     * @return
     */
    @PostMapping("/create")
    public Response create(HttpServletRequest request, @RequestBody @Validated MerchantVO merchantVO) {
        log.info("admin/merchant/create" + merchantVO);
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return outMerchantService.createMerchant(merchantVO, language, IPUtils.getIpAddr(request));
    }

    /**
     * 修改商户
     *
     * @param request
     * @param merchantVO
     * @return
     */
    @PostMapping("/update")
    public Response update(HttpServletRequest request, @RequestBody @Validated MerchantVO merchantVO) {
        log.info("admin/merchant/update" + merchantVO);
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return outMerchantService.updateMerchant(merchantVO, language, IPUtils.getIpAddr(request));
    }

    @GetMapping("/deleteSubAgent")
    public Response<Object> deleteSubAgent(HttpServletRequest request, @RequestParam(value = "merchantCode") String merchantCode,
                                           @RequestParam(value = "parentId") String parentId) {
        log.info("admin/merchant/deleteSubAgent:" + parentId + "," + merchantCode);
        return StringUtils.isAnyEmpty(merchantCode, parentId) ? Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                outMerchantService.deleteSubAgent(merchantCode, parentId);
    }

    @GetMapping(value = "/getMerchantLanguageList")
    public Response getMerchantLanguageList(HttpServletRequest request) {
        try {
            return outMerchantService.getMerchantLanguageList();
        } catch (Exception e) {
            log.error("MerchantController.getMerchantLanguageList,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 查询商户证书
     *
     * @param request
     * @param merchantName
     * @param parentId
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GetMapping(value = "/queryKeyList", produces = "application/json;charset=utf-8")
    public Response<PageInfo<TMerchantKey>> queryKeyList(HttpServletRequest request,
                                                         @RequestParam(value = "merchantName", required = false) String merchantName,
                                                         @RequestParam(value = "parentId", required = false) String parentId,
                                                         @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                         @RequestParam(value = "pageNum", required = false) Integer pageNum) {
        try {
            String language = request.getHeader("language");
            if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
            return outMerchantService.queryKeyList(merchantName, parentId, pageSize, pageNum, language);
        } catch (Exception e) {
            log.error("MerchantController.queryKeyList,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 修改商户状态
     *
     * @param request
     * @param merchantCode
     * @param status
     * @return
     */
    @GetMapping("/updateMerchantStatus")
    public Response<Object> updateMerchantStatus(HttpServletRequest request, @RequestParam(value = "merchantCode") String merchantCode,
                                                 @RequestParam(value = "status") String status) {
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return StringUtils.isAnyEmpty(merchantCode, status) || (!"0".equals(status) && !"1".equals(status)) ?
                Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                outMerchantService.updateMerchantStatus(merchantCode, status, language, IPUtils.getIpAddr(request));
    }

    /**
     * 修改商户状态
     *
     * @param request
     * @param merchantCode
     * @param status
     * @return
     */
    @GetMapping("/updateMerchantBackendStatus")
    public Response<Object> updateMerchantBackendStatus(HttpServletRequest request,
                                                        @RequestParam(value = "merchantCode") String merchantCode,
                                                        @RequestParam(value = "status") String status) {
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return StringUtils.isAnyEmpty(merchantCode, status) || (!"0".equals(status) && !"1".equals(status)) ?
                Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                outMerchantService.updateMerchantBackendStatus(merchantCode, status, language, IPUtils.getIpAddr(request));
    }

    /**
     * 获取KEY
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/getKey")
    @PreAuthorize("hasAnyRole('mykey')")
    public Response<TMerchantKey> getKey(HttpServletRequest request) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return outMerchantService.getKey(language);
    }


    /**
     * 生成KEY
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/generateKey")
    public Response<Object> generateKey(HttpServletRequest request) {
        return outMerchantService.generateKey();
    }

    /**
     * 修改KEY
     *
     * @param request
     * @param key
     * @param startTime
     * @param endTime
     * @return

    @GetMapping(value = "/updateKey")
    public Response<Object> updateKey(HttpServletRequest request, @RequestParam(value = "key") String key,
                                      @RequestParam(value = "startTime") String startTime,
                                      @RequestParam(value = "endTime") String endTime) {
        log.info("admin/merchant/updateKey: key :" + key + ",startTime:" + startTime + ",endTime:" + endTime);
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return StringUtils.isAnyEmpty(key, startTime, endTime) ? Response.returnFail(ResponseEnum.PARAMETER_INVALID) :
                outMerchantService.updateKey(key, startTime, endTime, language, IPUtils.getIpAddr(request));
    }
     */

    /**
     * 创建商户管理员
     *
     * @param request
     * @param id
     * @param adminName
     * @return
     */
    @GetMapping("/createAdmin")
    public Response<?> createAdmin(HttpServletRequest request, @RequestParam(value = "id") String id,
                                   @RequestParam(value = "adminName") String adminName) {
        log.info("admin/merchant/createAdmin:" + id + "," + adminName);
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return StringUtils.isAnyEmpty(id, adminName) ? Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY) :
                outMerchantService.createAdmin(id, adminName, null, language, IPUtils.getIpAddr(request));
    }

    /**
     * 修改管理员密码
     *
     * @param request
     * @param id
     * @return
     */
    @GetMapping("/updateAdminPassword")
    public Response<?> updateAdminPassword(HttpServletRequest request, @RequestParam(value = "id") String id) {
        log.info("admin/merchant/updateAdminPassword:" + id);
        String language = request.getHeader("language");
        if (org.apache.commons.lang3.StringUtils.isEmpty(language)) language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        return StringUtils.isAnyEmpty(id) ? Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY) :
                outMerchantService.updateAdminPassword(id, null, language, IPUtils.getIpAddr(request));
    }

    /**
     * 获取管理员密码
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/getPassword")
    public Response getPassword(HttpServletRequest request) {
        try {
            return Response.returnSuccess(CreateAdminPassword.getPassword());
        } catch (Exception e) {
            log.error("MerchantController.getPassword,exception:", e);
            return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 二级商户本月投注统计
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/queryCurrentMonthMerchantList")
    public Response queryCurrentMonthMerchantList(HttpServletRequest request) {
        return outMerchantService.queryCurrentMonthMerchantList();
    }

    /**
     * 图片上传
     *
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @PostMapping("/img/upload")
    public Response<Object> uploadImg(@RequestParam("imageFile") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty() || StringUtils.isBlank(multipartFile.getOriginalFilename())) {
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
        String contentType = multipartFile.getContentType();
        String root_fileName = multipartFile.getOriginalFilename();
        BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        String res = encoder.encodeBuffer(multipartFile.getBytes()).trim();
        log.info("上传图片:name={},type={}", root_fileName, contentType);
        return Response.returnSuccess(res);
    }

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/file/upload")
    public Response<Object> upload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
            }
            Map<String, String> map = Maps.newHashMap();
            // 获取文件名
            String fileName = file.getOriginalFilename();
            log.info("上传的文件名为：" + fileName);
            map.put("fileName", fileName);
            String firstName = fileName.substring(0, fileName.lastIndexOf("."));
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
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
                log.error("上传的文件格式为非图片格式：" + suffixName);
                return Response.returnFail(ResponseEnum.PARAMETER_INVALID);
            }

            // 设置文件存储路径
            String path = decoty + firstName + EncryptUtils.getStr() + suffixName;
            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            map.put("filePath", path);
            return Response.returnSuccess(map);
        } catch (Exception e) {
            log.error("上传的文件异常：", e);
            return Response.returnFail(ResponseEnum.PARAMETER_CANT_BE_EMPTY);
        }
    }

    /**
     * 文件下载
     *
     * @param request
     * @param response
     * @param fileName
     * @param filePath
     * @return
     */
    @GetMapping("/file/download")
    public File downloadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "fileName") String fileName,
                             @RequestParam(value = "filePath") String filePath, @RequestParam(value = "token") String token) throws Exception {
        if (StringUtils.isEmpty(token)) {
            String ex = JSON.toJSONString(APIResponse.returnFail(ResponseEnum.TOKEN_IS_EXPIRED.getId(), "token is need"));
            response.getWriter().write(ex);
            return null;
        }
        if (!jwtTokenUtil.validateToken(token)) {
            String ex = JSON.toJSONString(APIResponse.returnFail(ResponseEnum.TOKEN_IS_EXPIRED.getId(), "token is expired"));
            response.getWriter().write(ex);
            return null;
        }
        if (fileName != null) {
            //设置文件路径
            File file = new File(filePath);
            if (file.exists()) {
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    inputStream = new FileInputStream(file);
                    outputStream = response.getOutputStream();
                    response.setContentType("application/x-download");
                    String name = fileName.replace(decoty, "");
                    log.info("/file/download fileName=" + name);
                    name = new String(name.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
                    response.addHeader("Content-Disposition", "attachment;filename=" + name);
                    IOUtils.copy(inputStream, outputStream);
                    outputStream.flush();
                } catch (Exception e) {
                    log.error("下载文件异常：", e);
                } finally {
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            log.error("下载文件异常：", e);
                        }
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            log.error("下载文件异常：", e);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 商户联想查询列表
     */
    @GetMapping(value = "/getMerchantListTree")
    public Response getMerchantListTree(HttpServletRequest request) {
        log.info("/admin/merchant/getMerchantListTree");
        return outMerchantService.getMerchantListTree();
    }


    /**
     * 利率
     *
     * @return
     */
    @PostMapping("/currencyRate")
    public Response currencyRate() {
        try {
            return Response.returnSuccess(outMerchantService.currencyRateList());
        } catch (Exception e) {
            log.error("MerchantLevelController.currencyRate: error!", e);
        }
        return Response.returnFail(ResponseEnum.INTERNAL_ERROR);
    }


    @PostMapping("/queryCreditLimitConfig")
    public Response queryCreditLimitConfig(HttpServletRequest request, @RequestParam(value = "merchantId", required = false) Long merchantId,
                                           @RequestParam(value = "creditId", required = false) String creditId) {
        String globalId = UUIDUtils.createId();
        log.info("/admin/merchant/queryCreditLimitConfig  merchantId:" + merchantId + ",creditId:" + creditId + ",globalId:" + globalId);
        try {
            CreditConfigDto reqData = new CreditConfigDto();
            reqData.setCreditId(creditId);
            reqData.setMerchantId(merchantId);
            Request req = new Request();
            req.setGlobalId(globalId);
            req.setData(reqData);
            return Response.returnSuccess(pandaRcsCreditClient.queryCreditLimitConfig(req).getData());
        } catch (Exception e) {
            log.error("MerchantController.queryCreditLimitConfig,exception:", e);
            return Response.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }

    }

    @PostMapping("/saveOrUpdateCreditLimitConfig")
    public Response saveOrUpdateCreditLimitConfig(HttpServletRequest request, @RequestBody CreditConfigSaveDto reqData) {
        String globalId = UUIDUtils.createId();
        log.info("api/fund/saveOrUpdateCreditLimitConfig  reqData:" + reqData.toString() + ",globalId:" + globalId);
        try {
            Request req = new Request();
            req.setData(reqData);
            req.setGlobalId(globalId);
            Boolean bool = (Boolean) pandaRcsCreditClient.saveOrUpdateCreditLimitConfig(req).getData();
            log.info("api/fund/saveOrUpdateCreditLimitConfig  bool:" + bool);
            return Response.returnSuccess(bool);
        } catch (Exception e) {
            log.error("FundController.saveOrUpdateCreditLimitConfig,exception:", e);
            return Response.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @PostMapping("/queryMechantAgentList")
    public Response queryMechantAgentList(HttpServletRequest request, @RequestParam(value = "merchantCode") String merchantCode) {
        log.info("/admin/merchant/queryMechantAgentList  merchantCode:" + merchantCode);
        try {
            return outMerchantService.queryMechantAgentList(merchantCode);
        } catch (Exception e) {
            log.error("MerchantController.queryCreditLimitConfig,exception:", e);
            return Response.returnFail(ApiResponseEnum.INTERNAL_ERROR);
        }
    }

    @RequestMapping(value = "/getMerchantMatchCreditConfig", method = RequestMethod.POST)
    public Response<CreditConfigHttpQueryDto> getMerchantMatchCreditConfig(@RequestBody CreditConfigHttpQueryDto configDto) {
        log.info("获取获取信用限额配置查询参数查询参数： {}", configDto);
        return Response.returnSuccess(pandaRcsCreditClient.getMerchantMatchCreditConfig(configDto).getData());
    }

    @RequestMapping(value = "/saveCreditLimitTemplate", method = RequestMethod.POST)
    public Response<String> saveCreditLimitTemplate(@RequestBody CreditConfigHttpQueryDto configDto) {
        log.info("保存获取信用限额配置查询参数数据： {}", configDto);
        try {
            return Response.returnSuccess(pandaRcsCreditClient.saveCreditLimitTemplate(configDto).getData());
        } catch (Exception e) {
            return Response.returnFail("商户信用额度配置保存失败！");
        }
    }

    @RequestMapping(value = "/orderOperation", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('cancel_order')")
    public Response orderOperation(HttpServletRequest request, @RequestBody @Validated MerchantOrderOperationVO orderOperation) {
        log.info("取消注单： {}", orderOperation);
        try {
            distributedLockerUtil.lock(Cencal_order_KEY + orderOperation.getOrderNos());
            return outMerchantService.orderOperation(orderOperation, IPUtils.getIpAddr(request));
        } catch (Exception e) {
            log.error("商户注单取消失败：", e);
            return Response.returnFail("商户注单取消失败！");
        } finally {
            distributedLockerUtil.unLock(Cencal_order_KEY + orderOperation.getOrderNos());
        }
    }

    @PostMapping("/updateAdminUserName")
    @ApiOperation(value = "/admin/merchant/updateAdminUserName", notes = "对外商户后台-账户中心-二级商户管理-修改超级管理员名称")
    public Response updateAdminUserName(HttpServletRequest request, @RequestBody @Valid AdminUserNameUpdateReqVO adminUserNameUpdateReqVO) {
        String language = request.getHeader("language");
        if (StringUtils.isEmpty(language)) {
            language = Constant.LANGUAGE_CHINESE_SIMPLIFIED;
        }
        String ipAddr = IPUtils.getIpAddr(request);
        return outMerchantService.updateAdminUserName(adminUserNameUpdateReqVO, language, ipAddr);
    }
}
