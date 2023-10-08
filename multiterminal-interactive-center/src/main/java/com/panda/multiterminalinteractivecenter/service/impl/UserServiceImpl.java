package com.panda.multiterminalinteractivecenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.entity.Permissions;
import com.panda.multiterminalinteractivecenter.entity.User;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogPageEnum;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogTypeEnum;
import com.panda.multiterminalinteractivecenter.mapper.UserMapper;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.utils.FieldCompareUtil;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import com.panda.multiterminalinteractivecenter.vo.UserVo;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.service.impl
 * @Description :  TODO
 * @Date: 2022-03-11 15:10:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IService<User> {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MerchantLogService merchantLogService;

    public User getUser(String name,String pwd){
        log.info("输入参数 {} {}", name , pwd);
        return userMapper.queryUserByNameAndPwd(name, pwd);
    }

    public User findUserByName(String username){
        return userMapper.queryUserByName(username);
    }

    public void updateUser(User user){
        userMapper.updateById(user);
    }

    public void restKey(Long id, HttpServletRequest request){
        User oldUser = userMapper.selectById(id);
        userMapper.restKey(id);
        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("secret"), oldUser.getSecret(), null);
        merchantLogService.saveLog(MerchantLogPageEnum.USER_MANAGE, MerchantLogTypeEnum.REST_KEY, filedVO,  null, String.valueOf(id), request);
    }

    public APIResponse queryList(UserVo userVo){
        LambdaQueryWrapper<User> queryWrapper = new QueryWrapper<User>().lambda();
        queryWrapper.eq(StringUtils.isNotBlank(userVo.getUsername()),User::getName,userVo.getUsername());
        queryWrapper.orderByDesc(User::getCreatTime);
        Integer page = userVo.getPage();
        if (Objects.isNull(page)) {
            page = 1;
        }
        Integer size = userVo.getSize();
        if (Objects.isNull(size)) {
            size = 20;
        }
        PageHelper.startPage(page, size);
        List<User> list = baseMapper.selectList(queryWrapper);
        PageInfo<User> poList = new PageInfo<>(list);
        return  APIResponse.returnSuccess(poList);
    }

    public void addUser(User user, HttpServletRequest request){
        user.setId(userMapper.maxId()+1);
        baseMapper.insert(user);
        MerchantLogFiledVO filedVO = merchantLogService.createFiledVO(MerchantFieldUtil.FIELD_MAPPING.get("userName"), "-", user.getName());
        merchantLogService.saveLog(MerchantLogPageEnum.USER_MANAGE, MerchantLogTypeEnum.SAVE, filedVO,  null, String.valueOf(user.getId()), request);
    }
}
