package com.panda.sport.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.panda.sport.admin.security.JwtUser;
import com.panda.sport.admin.service.UserCheckLogService;
import com.panda.sport.admin.utils.SecurityUtils;
import com.panda.sport.backup.mapper.BackupTUserMapper;
import com.panda.sport.config.mapper.BackupUserCheckMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.po.bss.UserCheckLogPO;
import com.panda.sport.merchant.common.po.merchant.UserOrderAllPO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.UserOrderVO;
import com.panda.sport.merchant.common.vo.user.UserCheckLogVO;
import com.panda.sport.merchant.common.vo.user.UserVO;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service("userCheckLogService")
public class UserCheckLogServiceImpl implements UserCheckLogService {

    @Autowired
    private BackupUserCheckMapper userCheckMapper;

    @Autowired
    private BackupTUserMapper tUserMapper;

    @Autowired
    private LocalCacheService localCacheService;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public Response checkUserList(List<UserCheckLogVO> checkLogVOS) {
        JwtUser user = SecurityUtils.getUser();
        String merchantCode = user.getMerchantCode();
        String merchantId = user.getMerchantId();
        Integer agentLevel = user.getAgentLevel();

        UserOrderVO userOrderVO = new UserOrderVO();
        if (agentLevel == 1 || agentLevel == 10) {
            List<String> merchantCodeList = localCacheService.getMerchantCodeList(merchantId, agentLevel);
            userOrderVO.setMerchantCodeList(merchantCodeList);
        } else {
            userOrderVO.setMerchantCode(merchantCode);
        }

        List<UserCheckLogVO> userCheckLogVOS = Lists.newArrayList();

        for(UserCheckLogVO vo:checkLogVOS){
            userOrderVO.setUserId(vo.getUid());
            UserOrderAllPO userOrderAllPO = tUserMapper.getUserDetail(userOrderVO);
            UserCheckLogVO userCheckLogVO = new UserCheckLogVO();

            if(userOrderAllPO==null){
                // 未在我馆投注
                userCheckLogVO.setCheckType("1");
                userCheckLogVOS.add(userCheckLogVO);
                continue;
            }else {
                userCheckLogVO.setUid(userOrderAllPO.getUserId());
                userCheckLogVO.setUserName(userOrderAllPO.getUserName());
                userCheckLogVO.setMerchantCode(userOrderAllPO.getMerchantCode());
            }

            String key = "CHECKUSERLIST_"+vo.getUid() + "_" + user.getUsername() + "_" + vo.getCheckReason();

            if (redisTemplate.hasKey(key)) {
                log.info("checkUserList 重复提交了 uid ："+vo.getUid());
                userCheckLogVO.setCheckType("4");
                userCheckLogVOS.add(userCheckLogVO);
                continue;
            }

            // 是否距离上次查询不足3天
            int num = userCheckMapper.countCheckUserByDays(vo.getUid());
            if(num>0){
                userCheckLogVO.setCheckType("4");
                userCheckLogVOS.add(userCheckLogVO);
                continue;
            }

            // 插入数据
            try {
                UserCheckLogPO userCheckLogPO =  new UserCheckLogPO();
                userCheckLogPO.setUserName(userOrderAllPO.getUserName());
                userCheckLogPO.setCreateUser(user.getUsername());
                userCheckLogPO.setSubmitUser(user.getUsername());
                userCheckLogPO.setModifyUser(user.getUsername());
                userCheckLogPO.setCheckReason(vo.getCheckReason());
                userCheckLogPO.setCheckExplain(vo.getCheckExplain());
                userCheckLogPO.setCreateTime(System.currentTimeMillis());
                userCheckLogPO.setModifyTime(System.currentTimeMillis());
                userCheckLogPO.setStatus(0);
                userCheckLogPO.setMerchantCode(userOrderAllPO.getMerchantCode());
                userCheckLogPO.setUid(vo.getUid());
                userCheckMapper.insertUserCheckLog(userCheckLogPO);
                redisTemplate.opsForValue().set(key,1, 1, TimeUnit.MINUTES);
            }catch (Exception e){
                log.error("checkUserList error uid:"+vo.getUid(),e);
            }
        }
        return Response.returnSuccess(userCheckLogVOS);
    }

    @Override
    public Response getCheckLogList(UserCheckLogVO userCheckLogVO) {
        JwtUser user = SecurityUtils.getUser();
        String merchantCode = user.getMerchantCode();
        String merchantId = user.getMerchantId();
        Integer agentLevel = user.getAgentLevel();

        if (agentLevel == 1 || agentLevel == 10) {
            List<String> merchantCodeList = localCacheService.getMerchantCodeList(merchantId, agentLevel);
            userCheckLogVO.setMerchantCodeList(merchantCodeList);
        } else {
            userCheckLogVO.setMerchantCode(merchantCode);
        }
        Integer pageIndex = userCheckLogVO.getPageIndex();
        Integer pageSize = userCheckLogVO.getPageSize();
        String sort = userCheckLogVO.getSort();
        String orderBy = userCheckLogVO.getOrderBy();
        pageSize = (pageSize == null || pageSize == 0) ? 100 : pageSize;
        pageIndex = pageIndex == null ? 1 : pageIndex;
        sort = StringUtils.isEmpty(sort) ? Constant.DESC : sort;
        orderBy = StringUtils.isEmpty(orderBy) ? "s.create_time" : orderBy;
        String orderStr = orderBy + " " + sort;
        if ("".equals(orderBy)) {
            orderStr = "s.create_time desc";
        }
        PageHelper.startPage(pageIndex, pageSize, orderStr);
        PageInfo<UserCheckLogPO> pageInfo = new PageInfo<>(userCheckMapper.selectList(userCheckLogVO));
        return Response.returnSuccess(pageInfo);
    }

    @Override
    public Response<Object> getUserByUserName(String userName, String language) {

        JwtUser user = SecurityUtils.getUser();
        String merchantCode = user.getMerchantCode();
        Integer agentLevel = user.getAgentLevel();
        String merchantId = user.getMerchantId();

        String key = userName+","+merchantCode ;

        List<UserVO> userPOList = LocalCacheService.userPOListMap.getIfPresent(key);

        if (CollectionUtils.isEmpty(userPOList)){
            if (agentLevel == 1 || agentLevel == 10) {
                List<String> merchantCodeList = localCacheService.getMerchantCodeList(merchantId, agentLevel);
                userPOList  =  tUserMapper.getUserByUserNameOther(null,merchantCodeList,userName);
            }else{
                userPOList  =  tUserMapper.getUserByUserNameOther(merchantCode,null,userName);
            }
            LocalCacheService.userPOListMap.put(key,CollectionUtils.isEmpty(userPOList) ? new ArrayList<>() : userPOList);
        }

        if (CollectionUtils.isEmpty(userPOList)){
            if (language.equals(Constant.LANGUAGE_CHINESE_SIMPLIFIED) ){
                return    Response.returnFail("请检查并输入正确的用户名");
            }else{
                return   Response.returnFail("please input correct userName");
            }
        }

        return Response.returnSuccess(userPOList);
    }
}
