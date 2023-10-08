package com.panda.multiterminalinteractivecenter.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.panda.multiterminalinteractivecenter.base.APIResponse;
import com.panda.multiterminalinteractivecenter.entity.Permissions;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogPageEnum;
import com.panda.multiterminalinteractivecenter.enums.MerchantLogTypeEnum;
import com.panda.multiterminalinteractivecenter.mapper.PermissionsMapper;
import com.panda.multiterminalinteractivecenter.service.MerchantLogService;
import com.panda.multiterminalinteractivecenter.utils.FieldCompareUtil;
import com.panda.multiterminalinteractivecenter.vo.MerchantLogFiledVO;
import com.panda.multiterminalinteractivecenter.vo.PermissionsVo;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
public class MenuServiceImpl extends ServiceImpl<PermissionsMapper, Permissions> implements IService<Permissions> {

    @Autowired
    private PermissionsMapper permissionsMapper;

    @Autowired
    private MerchantLogService merchantLogService;

    public APIResponse queryMenuTreeList(){
        List<Permissions> permissionsList = permissionsMapper.selectAll();
        List<PermissionsVo> list = new ArrayList<>();
        if (CollectionUtil.isEmpty(permissionsList)){
            return  APIResponse.returnSuccess(list);
        }
        list = getMenuTree(permissionsList);
        return  APIResponse.returnSuccess(list);
    }

    public APIResponse queryRoleMenuTreeList(Long roleId){
        List<Permissions> permissionsList = permissionsMapper.selectAll();
        List<PermissionsVo> list = new ArrayList<>();
        if (CollectionUtil.isEmpty(permissionsList)){
            return  APIResponse.returnSuccess(list);
        }
        List<Permissions> permissionsList1 = permissionsMapper.findPermissionsByRoleId(roleId);
        if (CollectionUtil.isNotEmpty(permissionsList1)){
            for (Permissions p : permissionsList1){
                for (Permissions p1 : permissionsList){
                    if (p.getId().equals(p1.getId())){
                        p1.setStatus(1);
                        break;
                    }
                }
            }
        }
        list = getMenuTree(permissionsList);
        return  APIResponse.returnSuccess(list);
    }

    public APIResponse queryMenuList(PermissionsVo permissionsVo){
        LambdaQueryWrapper<Permissions> queryWrapper = new QueryWrapper<Permissions>().lambda();
        queryWrapper.eq(StringUtils.isNotBlank(permissionsVo.getName()),Permissions::getName,permissionsVo.getName());
        queryWrapper.orderByDesc(Permissions::getSort);
        Integer page = permissionsVo.getPage();
        if (Objects.isNull(page)) {
            page = 1;
        }
        Integer size = permissionsVo.getSize();
        if (Objects.isNull(size)) {
            size = 20;
        }
        PageHelper.startPage(page, size);
        List<Permissions> list = baseMapper.selectList(queryWrapper);
        PageInfo<Permissions> poList = new PageInfo<>(list);
        return  APIResponse.returnSuccess(poList);
    }

    public void addPermissions(Permissions permissions, HttpServletRequest request){
        permissions.setId(permissionsMapper.maxId() + 1);
        permissionsMapper.insert(permissions);
        MerchantLogFiledVO filedVO = FieldCompareUtil.compareObject(new Permissions(), permissions);
        merchantLogService.saveLog(MerchantLogPageEnum.MENU_MANAGE, MerchantLogTypeEnum.SAVE, filedVO,  null, String.valueOf(permissions.getId()), request);
    }

    public void updatePermissions(Permissions permissions, HttpServletRequest request){
        Permissions old = baseMapper.selectById(permissions.getId());
        baseMapper.updateById(permissions);
        MerchantLogFiledVO filedVO = FieldCompareUtil.compareObject(old, permissions);
        filedVO.setFieldName(filedVO.getFieldName().stream().map(f -> MerchantFieldUtil.FIELD_MAPPING.get("url").equals(f) ? "菜单标识" : f).collect(Collectors.toList()));
        merchantLogService.saveLog(MerchantLogPageEnum.MENU_MANAGE, MerchantLogTypeEnum.EDIT, filedVO,  null, String.valueOf(permissions.getId()), request);
    }

    public void deletePermissions(Permissions permissions, HttpServletRequest request){
        Permissions old = baseMapper.selectById(permissions.getId());
        permissionsMapper.deleteById(permissions.getId());
        MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
        filedVO.getFieldName().addAll(Arrays.asList("ID",MerchantFieldUtil.FIELD_MAPPING.get("menuName"),MerchantFieldUtil.FIELD_MAPPING.get("menuSign"),
                MerchantFieldUtil.FIELD_MAPPING.get("pid"),MerchantFieldUtil.FIELD_MAPPING.get("sort"),MerchantFieldUtil.FIELD_MAPPING.get("status")));
        filedVO.getBeforeValues().addAll(Arrays.asList(String.valueOf(old.getId()), old.getName(),
                old.getUrl() ,String.valueOf(old.getPid()), String.valueOf(old.getSort()), old.getStatus() == 1 ? "启用" : " 禁用"));
        filedVO.getAfterValues().add("-");
        merchantLogService.saveLog(MerchantLogPageEnum.MENU_MANAGE, MerchantLogTypeEnum.DEL, filedVO,  null, String.valueOf(permissions.getId()), request);
    }

    public List<PermissionsVo> getMenuTree(List<Permissions> permissionsList){
        if (CollectionUtil.isEmpty(permissionsList)){
            return null;
        }
        List<PermissionsVo> list = new ArrayList<>();
        for (Permissions permissions : permissionsList){
            if (permissions.getPid() == null){
                PermissionsVo permissionsVo = new PermissionsVo();
                BeanUtil.copyProperties(permissions,permissionsVo);
                permissionsVo = buildChildren(permissionsVo, permissionsList);
                list.add(permissionsVo);
            }
        }
        return list;
    }


    public PermissionsVo buildChildren(PermissionsVo rootNode,List<Permissions> permissionsList) {
        List<PermissionsVo> childrenTree = new ArrayList<>();
        for (Permissions permissions : permissionsList) {
            if (permissions.getPid() != null && permissions.getPid().equals(rootNode.getId())) {
                PermissionsVo permissionsVo = new PermissionsVo();
                BeanUtil.copyProperties(permissions,permissionsVo);
                childrenTree.add(buildChildren(permissionsVo,permissionsList));
            }
        }
        rootNode.setList(childrenTree);
        return rootNode;
    }

}
