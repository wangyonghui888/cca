package com.panda.sport.merchant.manage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.panda.sport.backup.mapper.BackupTUserMapper;
import com.panda.sport.bss.mapper.MerchantLevelMapper;
import com.panda.sport.match.mapper.CurrencyRateMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.constant.MerchantLogConstants;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.enums.ResponseEnum;
import com.panda.sport.merchant.common.po.bss.CurrencyRatePO;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.po.merchant.*;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.MerchantFieldUtil;
import com.panda.sport.merchant.common.utils.MerchantUtil;
import com.panda.sport.merchant.common.vo.MerchantLogFiledVO;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.merchant.AdminMenuVo;
import com.panda.sport.merchant.common.vo.merchant.AdminPermissionVo;
import com.panda.sport.merchant.common.vo.merchant.CurrencyRateVO;
import com.panda.sport.merchant.common.vo.merchant.MerchantLevelVO;
import com.panda.sport.merchant.manage.service.MerchantLevelService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.mapper.AdminMenuMapper;
import com.panda.sport.merchant.mapper.AdminPermissionMapper;
import com.panda.sport.merchant.mapper.AdminRolesMenusMapper;
import com.panda.sport.merchant.mapper.AdminRolesPermissionsMapper;
import com.panda.sports.auth.util.SsoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : Jeffrey
 * @Date: 2020-01-24 12:30
 * @Description :
 */
@Service("merchantLevelService")
public class MerchantLevelServiceImpl implements MerchantLevelService {

    @Autowired
    private MerchantLevelMapper levelMapper;

    @Autowired
    private BackupTUserMapper tUserMapper;

    @Autowired
    private MerchantLogService merchantLogService;

    @Autowired
    private LoginUserService loginUserService;

    @Autowired
    private CurrencyRateMapper currencyRateMapper;

    @Autowired
    private AdminPermissionMapper adminPermissionMapper;

    @Autowired
    private AdminMenuMapper adminMenuMapper;

    @Autowired
    private AdminRolesPermissionsMapper adminRolesPermissionsMapper;

    @Autowired
    private AdminRolesMenusMapper adminRolesMenusMapper;

    private List<MerchantLevelVO> convertPObyList(List<MerchantLevelPO> list) {
        List<MerchantLevelVO> returnList = new ArrayList<>();
        list.forEach(val -> {
            MerchantLevelVO tempVo = new MerchantLevelVO();
            BeanUtils.copyProperties(val, tempVo);
            returnList.add(tempVo);
        });
        return returnList;
    }

    /**
     * 查询商户等级列表
     *
     * @param merchantLevelVO
     * @return
     */
    @Override
    public List<MerchantLevelVO> queryList(MerchantLevelVO merchantLevelVO) {
        return convertPObyList(levelMapper.selectByLevelVO(merchantLevelVO));
    }

    /**
     * 商户等级详情
     *
     * @param id
     * @return
     */
    @Override
    public Response detail(String id) {
        return Response.returnSuccess(levelMapper.selectById(id));
    }


    /**
     * 新增商户等级
     *
     * @param merchantLevelVO
     * @param language
     * @param ip
     * @return
     */
    @Override
    public Response add(MerchantLevelVO merchantLevelVO, Integer userId, String language, String ip) {
        if (merchantLevelVO.getLevel() == null || levelMapper.countMerchantLevel(merchantLevelVO.getLevel()) > 0) {
            return Response.returnFail(ResponseEnum.MERCHANT_LEVEL_IS_EXIST);
        }
        if (merchantLevelVO.getRateId() == null) {
            return Response.returnFail(ResponseEnum.MERCHANT_LEVEL_IS_EXIST);
        }
        MerchantLevelPO tempPo = new MerchantLevelPO();
        convertVOByPO(merchantLevelVO, tempPo);
        tempPo.setLevelName(merchantLevelVO.getLevel() + "级商户");
        levelMapper.insertSelective(tempPo);
        //记录日志
        String username = loginUserService.getLoginUser(userId);
        merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_LEVEL, MerchantLogTypeEnum.NEW_MERCHANT_LEVEL, null, MerchantLogConstants.MERCHANT_IN, userId.toString(), username
                , null, null, merchantLevelVO.getLevelName(), language,ip);
        return Response.returnSuccess();
    }

    /**
     * 修改商户等级
     *
     * @param merchantLevelVO
     * @param language
     * @return
     */
    @Override
    public boolean update(MerchantLevelVO merchantLevelVO, Integer userId, String language,String ip) {
        MerchantLevelPO tempPo = new MerchantLevelPO();
        convertVOByPO(merchantLevelVO, tempPo);
        MerchantLevelPO merchantLevelPO = levelMapper.selectById(merchantLevelVO.getId().toString());
        int row = levelMapper.updateByPrimaryKeySelective(tempPo);
        if (row > 0) {
            //记录日志
            String username = loginUserService.getLoginUser(userId);
            MerchantFieldUtil fieldUtil = new MerchantFieldUtil<MerchantLevelPO>();
            MerchantLogFiledVO filedVO = fieldUtil.compareObject(merchantLevelPO, tempPo);
            merchantLogService.saveLog(MerchantLogPageEnum.SET_MERCHANT_LEVEL, MerchantLogTypeEnum.EDIT_MERCHANT_LEVEL, filedVO, MerchantLogConstants.MERCHANT_IN, userId.toString(), username
                    , null, null, merchantLevelVO.getId().toString(), language,ip);
            return true;
        }
        return false;
    }

    /**
     * 商户等级详情
     *
     * @param level
     * @return
     */
    @Override
    public MerchantLevelPO getMerchantLevel(Integer level) {

        return levelMapper.getMerchantLevel(level);
    }

    /**
     * 所有商户等级列表
     *
     * @return
     */
    @Override
    public List<MerchantLevelPO> queryLevelList() {

        return levelMapper.queryLevelList();
    }

    /**
     * 用户汇率
     * @return
     */
    @Override
    public List<CurrencyRateVO> currencyRateList() {
        List<CurrencyRatePO> currencyRatePOList = currencyRateMapper.queryCurrencyRateList();
        List<CurrencyRateVO> currencyRateVOList = new ArrayList<>();
        for( CurrencyRatePO currencyRatePO : currencyRatePOList) {

            CurrencyRateVO currencyRateVO = new CurrencyRateVO();
            currencyRateVO.setCountryCn(currencyRatePO.getCountryCn());
            currencyRateVO.setCountryZh(currencyRatePO.getCountryZh());
            currencyRateVO.setCurrencyCode(Integer.valueOf(currencyRatePO.getCurrencyCode()));
            currencyRateVOList.add(currencyRateVO);
        }
        return currencyRateVOList;
    }


    /**
     * 菜单和权限的所有
     * @param code
     * @return
     */
    @Override
    public Response menuPersionAll(String code) {

        List<AdminRole> adminRoles = adminMenuMapper.getRoleAdmin(code,1);
        if (CollectionUtil.isEmpty(adminRoles)) {
            return Response.returnFail("请先添加商户管理员账号");
        }

        // 获取所对应的账号
        Long roleId = adminRoles.get(0).getId();

        // 权限
        List<AdminPermission> adminPermissionList = adminPermissionMapper.getPermission();
        List<AdminPermissionVo> adminPermissionVoList = new ArrayList<>();
        for (AdminPermission adminPermission : adminPermissionList) {
            AdminPermissionVo adminPermissionVo = new AdminPermissionVo();
            BeanUtils.copyProperties(adminPermission,adminPermissionVo);
            adminPermissionVoList.add(adminPermissionVo);
        }

        // 菜单
        List<AdminMenu> adminMenuList = adminMenuMapper.getMenu();
        List<AdminMenuVo> adminMenuVoList = new ArrayList<>();
        for (AdminMenu adminMenu :adminMenuList) {
            AdminMenuVo adminMenuVo = new AdminMenuVo();
            BeanUtils.copyProperties(adminMenu,adminMenuVo);
            adminMenuVoList.add(adminMenuVo);
        }

        List<AdminMenuVo> rootMenu = new ArrayList<>();
        for (AdminMenuVo adminMenuVo : adminMenuVoList) {
            if (adminMenuVo.getPid() ==0 ) {
                adminMenuVo.setParentName("");
                rootMenu.add(adminMenuVo);
            }
        }

        //获取菜单的子集
        for (AdminMenuVo amv : rootMenu) {
            List<AdminMenuVo> childList = getChild(amv,adminMenuVoList);
            amv.setChildren(childList);
        }

        //获取这个角色所对应的菜单
        List<AdminRolesMenus> adminRolesMenusList = adminMenuMapper.getRoleMenu(roleId);
        List<String> menuId = adminRolesMenusList.stream().map(e -> String.valueOf(e.getMenuId())).collect(Collectors.toList());
        String menuIdString = String.join(",", menuId);

        List<AdminRolesPermissions> adminRolesPermissionsList = adminMenuMapper.getRolesPermissions(roleId);
        List<String> permissionId = adminRolesPermissionsList.stream().map(e -> String.valueOf(e.getPermissionId())).collect(Collectors.toList());
        String permissionIdString = String.join(",", permissionId);

        //输出
        Map<String,Object> outMap = new HashMap<String, Object>(2){{
            put("permission", adminPermissionVoList);
            put("menu",rootMenu);
            put("menuIds", menuIdString);
            put("permissionIds", permissionIdString);

        }};

        return Response.returnSuccess(outMap);
    }

    // 获取菜单子集
    private List<AdminMenuVo> getChild(AdminMenuVo parent, List<AdminMenuVo> adminMenuList) {

        List<AdminMenuVo> childList = new ArrayList<>();
        for (AdminMenuVo adminMenuVo : adminMenuList) {
            if (adminMenuVo.getPid().equals(parent.getId())) {
                adminMenuVo.setParentName(parent.getPath());
                childList.add(adminMenuVo);
            }
        }
        for (AdminMenuVo nav : childList) {
            nav.setChildren(getChild(nav, adminMenuList));
        }
        if (childList.size() == 0) {
            return Collections.emptyList();
        }
        return childList;
    }


    /**
     * 根据菜单获取权限
     * @param menuIds
     * @return
     */
    @Override
    public List<AdminPermissionVo> getPermissionInMenuIds(List<String> menuIds) {

        List<AdminPermission> adminPermissionList = adminPermissionMapper.getPermissionInMenuIds(menuIds);
        List<AdminPermissionVo> adminPermissionVoList = new ArrayList<>();
        for (AdminPermission adminPermission : adminPermissionList) {
            AdminPermissionVo adminPermissionVo = new AdminPermissionVo();
            BeanUtils.copyProperties(adminPermission,adminPermissionVo);
            adminPermissionVoList.add(adminPermissionVo);
        }
        return adminPermissionVoList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response addRoleMenu(String code, List<String> menuIdList, List<String> PermissionIdList,String agentLevel,HttpServletRequest request) {

        List<AdminRole> adminRoles = adminMenuMapper.getRoleAdmin(code,1);
        if (CollectionUtil.isEmpty(adminRoles)) {
            return Response.returnFail("请先添加商户管理员账号");
        }

        // 获取所对应的账号
        Long roleId = adminRoles.get(0).getId();

        List<AdminRolesMenus> adminRolesMenusList = new ArrayList<>();
        menuIdList.forEach(e -> {
            AdminRolesMenus adminRolesMenus = new AdminRolesMenus();
            adminRolesMenus.setRoleId(roleId);
            adminRolesMenus.setMenuId(Long.valueOf(e));
            adminRolesMenusList.add(adminRolesMenus);
        });

        Integer userId = SsoUtil.getUserId(request);
        String username = loginUserService.getLoginUser(userId);
        String ip = IPUtils.getIpAddr(request);


        List<AdminRolesPermissions> adminRolesPermissionsList = new ArrayList<>();
        PermissionIdList.forEach(p -> {
            AdminRolesPermissions adminRolesPermissions = new AdminRolesPermissions();
            adminRolesPermissions.setRoleId(roleId);
            adminRolesPermissions.setPermissionId(Long.valueOf(p));

            adminRolesPermissionsList.add(adminRolesPermissions);
        });
        // 查询旧数据

        MerchantLogFiledVO filedVO = new MerchantLogFiledVO();
        getMerchantLogFiledVO(filedVO,roleId,menuIdList,PermissionIdList);
        // 插入
        adminRolesMenusMapper.delete(new QueryWrapper<AdminRolesMenus>().eq("role_id", roleId));
        adminMenuMapper.insertBatchRolesMenus(adminRolesMenusList);
        adminRolesPermissionsMapper.delete(new QueryWrapper<AdminRolesPermissions>().eq("role_id", roleId));
        adminMenuMapper.insertBatchRolesPermissions(adminRolesPermissionsList);

        //查询商户信息
        MerchantPO mvo  =tUserMapper.getUserMerchantUserLog(userId.toString());
        merchantLogService.saveOperationRoomLog(MerchantLogTypeEnum.CHAT_MANAGER.getCode(),  MerchantLogTypeEnum.EDIT_INFO.getRemark()
                , MerchantUtil.getModeInfo(agentLevel,Constant.LANGUAGE_CHINESE_SIMPLIFIED )+"-"+MerchantLogTypeEnum.CHAT_MANAGER.getRemark(),
                MerchantLogTypeEnum.CHAT_MANAGER.getPageCode().get(0),null==mvo? null: mvo.getMerchantCode(), null==mvo? null :mvo.getMerchantName(), null==mvo? userId.toString() :mvo.getId(),
                filedVO.getFieldName(), filedVO.getBeforeValues(), filedVO.getAfterValues(),username,userId.toString(),ip);

        return Response.returnSuccess();
    }

    private MerchantLogFiledVO getMerchantLogFiledVO(MerchantLogFiledVO filedVO,Long roleId,List<String> menuIdList, List<String> PermissionIdList){

        // 菜单
        List<AdminMenu> adminMenuList = adminMenuMapper.getMenu();
        List<String> names = new ArrayList<>();
        List<String> befores = new ArrayList<>();
        List<String> afters = new ArrayList<>();
        List<AdminMenuVo> adminMenuVoList = new ArrayList<>();
        for (AdminMenu adminMenu :adminMenuList) {
            AdminMenuVo adminMenuVo = new AdminMenuVo();
            BeanUtils.copyProperties(adminMenu,adminMenuVo);
            adminMenuVoList.add(adminMenuVo);
        }
        List<AdminMenuVo> oneMenuVoList = new ArrayList<>();
        List<AdminMenuVo>  twoMenuVoList = new ArrayList<>();
        //得到分组目录
        Map<Long, List<AdminMenuVo>> appleMap = adminMenuVoList.stream().collect(Collectors.groupingBy(AdminMenuVo::getPid));
        for(Map.Entry entry : appleMap.entrySet()){
            if((long)entry.getKey()==0){
                oneMenuVoList.addAll((List<AdminMenuVo>)entry.getValue());
            }else{//下级的
                twoMenuVoList.addAll((List<AdminMenuVo>)entry.getValue());
            }
        }


        // 权限
        List<AdminPermission> adminPermissionList = adminPermissionMapper.getPermission();
        List<AdminPermissionVo> adminPermissionVoList = new ArrayList<>();
        for (AdminPermission adminPermission : adminPermissionList) {
            AdminPermissionVo adminPermissionVo = new AdminPermissionVo();
            BeanUtils.copyProperties(adminPermission,adminPermissionVo);
            adminPermissionVoList.add(adminPermissionVo);
        }


        List<AdminMenuVo> rootMenu = new ArrayList<>();
        for (AdminMenuVo adminMenuVo : adminMenuVoList) {
            if (adminMenuVo.getPid() ==0 ) {
                adminMenuVo.setParentName("");
                rootMenu.add(adminMenuVo);
            }
        }



        //获取这个角色所对应的菜单
        List<AdminRolesMenus> adminRolesMenusList = adminMenuMapper.getRoleMenu(roleId);
        List<String> menuId = adminRolesMenusList.stream().map(e -> String.valueOf(e.getMenuId())).collect(Collectors.toList());


        List<AdminRolesPermissions> adminRolesPermissionsList = adminMenuMapper.getRolesPermissions(roleId);
        List<String> permissionId = adminRolesPermissionsList.stream().map(e -> String.valueOf(e.getPermissionId())).collect(Collectors.toList());


        for(AdminMenuVo vo : oneMenuVoList){
            names.add("资源列表-" +vo.getName());
             Boolean flag =  getOkOrNo(vo,menuId);
             Boolean flag2 =  getOkOrNo(vo,menuIdList);
             if(flag){
                 befores.add("勾选");
             }else{
                 befores.add("不勾选");
             }

            if(flag2){
                afters.add("勾选");
            }else{
                afters.add("不勾选");
            }
            for (AdminMenuVo vo2 : twoMenuVoList){
                if(vo.getId()==vo2.getPid()){
                    names.add(vo2.getName());
                    Boolean flag1 =  getOkOrNo(vo2,menuId);
                    Boolean flag3 =  getOkOrNo(vo2,menuIdList);
                    if(flag1){
                        befores.add("勾选");
                    }else{
                        befores.add("不勾选");
                    }

                    if(flag3){
                        afters.add("勾选");
                    }else{
                        afters.add("不勾选");
                    }
                }
            }
        }

        for(AdminPermissionVo vo : adminPermissionVoList){
            names.add("权限列表-" +vo.getAlias());
            Boolean flag =  getOkOrNoP(vo,permissionId);
            Boolean flag1 =  getOkOrNoP(vo,PermissionIdList);
            if(flag){
                befores.add("勾选");
            }else{
                befores.add("不勾选");
            }

            if(flag1){
                afters.add("勾选");
            }else{
                afters.add("不勾选");
            }
        }
        filedVO.setFieldName(names);
        filedVO.setBeforeValues(befores);
        filedVO.setAfterValues(afters);

        return filedVO;
    }

    private Boolean getOkOrNo(AdminMenuVo vo,List<String> menuId){
          if(menuId.contains(vo.getId().toString())){
              return true;
          }else{
              return false;
          }
    }

    private Boolean getOkOrNoP(AdminPermissionVo vo,List<String> menuId){
        if(menuId.contains(vo.getId().toString())){
            return true;
        }else{
            return false;
        }
    }

}
