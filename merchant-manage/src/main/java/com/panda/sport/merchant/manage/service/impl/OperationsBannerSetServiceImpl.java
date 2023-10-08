package com.panda.sport.merchant.manage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.panda.sport.bss.mapper.OperationsBannerRelationMapper;
import com.panda.sport.bss.mapper.OperationsBannerSetMapper;
import com.panda.sport.merchant.common.enums.MerchantLogPageEnum;
import com.panda.sport.merchant.common.enums.MerchantLogTypeEnum;
import com.panda.sport.merchant.common.po.bss.OperationsBannerRelation;
import com.panda.sport.merchant.common.po.bss.OperationsBannerSet;
import com.panda.sport.merchant.common.utils.IPUtils;
import com.panda.sport.merchant.common.utils.MerchantUtil;
import com.panda.sport.merchant.common.vo.*;
import com.panda.sport.merchant.manage.service.IOperationsBannerRelationService;
import com.panda.sport.merchant.manage.service.IOperationsBannerSetService;
import com.panda.sport.merchant.manage.service.MerchantLogService;
import com.panda.sport.merchant.manage.util.RedisTemp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author baylee
 * @version 1.0
 * @date 2021/8/20 12:09:56
 */
@Service
@Slf4j
public class OperationsBannerSetServiceImpl extends ServiceImpl<OperationsBannerSetMapper, OperationsBannerSet> implements IOperationsBannerSetService {

    @Resource
    private OperationsBannerSetMapper bannerSetMapper;

    @Resource
    private MerchantLogService merchantLogService;

    @Resource
    private OperationsBannerRelationMapper relationMapper;

    @Resource
    private IOperationsBannerRelationService operationsBannerRelationService;

    private static final String BANNER_SET_USER_NUM = "BA_NUM_";

    private static final String BANNER_SET_USER_NUM_DAY = "BA_DAY_";


    @Override
    public List<MerchantTreeVO> getMerchantList(MerchantTreeVO paramVO) {
        List<MerchantTreeVO> dataList = bannerSetMapper.getMerchantList(null);
        Map<Long, List<MerchantTreeVO>> rootMap = dataList.stream().filter(var -> var.getParentId() == null)
                .collect(Collectors.groupingBy(MerchantTreeVO::getId));
        List<MerchantTreeVO> returnData = new ArrayList<>(rootMap.size());
        for (MerchantTreeVO subVO : dataList) {
            if (subVO.getParentId() == null) {
                continue;
            }
            List<MerchantTreeVO> rootList = rootMap.get(subVO.getParentId());
            if (CollUtil.isEmpty(rootList)) {
                continue;
            }
            MerchantTreeVO rootVo = rootList.get(0);
            if (rootVo.getSubList() == null) {
                List<MerchantTreeVO> temp = Lists.newArrayList();
                temp.add(subVO);
                rootVo.setSubList(temp);
                returnData.add(rootVo);
                continue;
            }
            rootVo.getSubList().add(subVO);
        }
        return returnData;
    }

    @Override
    public PageInfo<OperationsBannerVO> getBannerList(OperationsVO paramVO) {
        Integer start = paramVO.getStart();
        Integer size = paramVO.getSize();
        paramVO.setStart(start == null ? 0 : start);
        paramVO.setSize(size == null ? 20 : size);
        if (null != paramVO.getMerchantCode()) {
            List<OperationsBannerRelation> relationList = relationMapper.selectList(new QueryWrapper<OperationsBannerRelation>().eq("merchant_code", paramVO.getMerchantCode()));
            List<Long> ids = Lists.newArrayList();
            if (relationList.size() > 0) {
                relationList.forEach(e -> {
                    ids.add(e.getBannerId());
                });
            }
            if (ids.size() > 0) {
                paramVO.setIds(ids);
            }
        }
        PageHelper.startPage(paramVO.getStart(), paramVO.getSize());
        PageInfo<OperationsBannerVO> pageInfo = new PageInfo<>(bannerSetMapper.getList(paramVO));
        List<OperationsBannerVO> list = pageInfo.getList();
        list.forEach(e -> {
            Long bannerId = Long.valueOf(e.getId());
            List<OperationsBannerRelation> relations = relationMapper.selectList(new QueryWrapper<OperationsBannerRelation>().eq("banner_id", bannerId));
            if (relations.size() > 0) {
                List<String> merchantCode = Lists.newArrayList();
                relations.forEach(rel -> merchantCode.add(rel.getMerchantCode()));
                List<String> merchantName = bannerSetMapper.getMerchantName(merchantCode);
                e.setMerchant(StringUtils.join(merchantName.toArray(), ","));
            }
            if (null != e.getMerchantList() && e.getMerchantList().equalsIgnoreCase("all")) {
                e.setMerchant("全部");
            }
        });
        pageInfo.setList(list);
        return pageInfo;
    }

    @Override
    public Response<?> save(HttpServletRequest request,OperationsBannerVO paramVO) {
        OperationsBannerSet entity = new OperationsBannerSet();
        BeanUtil.copyProperties(paramVO, entity);
        log.info("paramVO: {}", paramVO);
        log.info("entity before: {}", entity);
        entity.setLogicDelete(0);
        long createTime = System.currentTimeMillis();
        entity.setCreateTime(createTime);
        entity.setModifyTime(createTime);
        String merchant = paramVO.getMerchantList();
        List<String> merchantList = Lists.newArrayList();
        if (!merchant.contains("all")) {
            if (merchant.contains(",")) {
                if (merchant.contains(",")) {
                    //多个
                    String[] strings = merchant.split(",");
                    merchantList = Arrays.asList(strings);
                }
            } else {
                //一个
                if (!"".equalsIgnoreCase(merchant)) {
                    merchantList.add(merchant);
                }
            }
        }
        bannerSetMapper.insert(entity);
        log.info("entity after: {}", entity);
        List<OperationsBannerRelation> relations = Lists.newArrayList();
        if (merchantList.size() > 0) {
            merchantList.forEach(e -> {
                OperationsBannerRelation relation = new OperationsBannerRelation();
                relation.setMerchantCode(e);
                relation.setBannerId(entity.getId());
                relations.add(relation);
            });
        }
        if (relations.size() > 0) {
            operationsBannerRelationService.saveBatch(relations);
        }
        /**
         *  添加系统日志
         * */
        String userId = request.getHeader("user-id");
        String username = request.getHeader("merchantName");
        String ip = IPUtils.getIpAddr(request);
        MerchantUtil util = new MerchantUtil();
        MerchantLogFiledVO vo1 = util.compareObject(null,paramVO,MerchantUtil.filterOpertationAddNames,MerchantUtil.FIELD_OPERATION_MAPPING);
        merchantLogService.saveOperationRoomLog(MerchantLogTypeEnum.SAVE_INFO.getCode(),  MerchantLogTypeEnum.SAVE_INFO.getRemark()
                , MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_72.getRemark(),
                MerchantLogTypeEnum.SAVE_INFO.getPageCode().get(0), userId, username, userId,
                vo1.getFieldName(), vo1.getBeforeValues(), vo1.getAfterValues(),username,userId,ip);
        return Response.returnSuccess();
    }

    @Override
    public Response<?> update(HttpServletRequest request,OperationsBannerVO paramVO) {
        //查询原来的值
        OperationsBannerVO oldInfo = bannerSetMapper.getOldOneInfo(Long.valueOf(paramVO.getId()));
        OperationsBannerSet entity = new OperationsBannerSet();
        BeanUtil.copyProperties(paramVO, entity);
        entity.setModifyTime(System.currentTimeMillis());
        String merchant = paramVO.getMerchantList();
        List<String> merchantList = Lists.newArrayList();
        if (!merchant.contains("all")) {
            if (merchant.contains(",")) {
                if (merchant.contains(",")) {
                    //多个
                    String[] strings = merchant.split(",");
                    merchantList = Arrays.asList(strings);
                }
            } else {
                //一个
                if (!"".equalsIgnoreCase(merchant)) {
                    merchantList.add(merchant);
                }
            }
        }
        if (entity.getShowNum() != null){
            OperationsBannerSet set = bannerSetMapper.selectById(entity.getId());
            if (!set.getShowNum().equals(entity.getShowNum())){
                try {
                    String bid = String.valueOf(set.getId());
                    RedisTemp.deleteByPattern(BANNER_SET_USER_NUM + bid.substring(bid.length()-5)+ "*");
                    RedisTemp.deleteByPattern(BANNER_SET_USER_NUM_DAY + bid.substring(bid.length()-5)+ "*");
                }catch (Exception e){
                    log.error("活动删除key异常！", e);
                }
            }
        }
        bannerSetMapper.updateById(entity);
        List<OperationsBannerRelation> relations = Lists.newArrayList();
        relationMapper.delete(new QueryWrapper<OperationsBannerRelation>()
                .eq("banner_id", entity.getId()));
        if (merchantList.size() > 0) {
            merchantList.forEach(e -> {
                OperationsBannerRelation relation = new OperationsBannerRelation();
                relation.setMerchantCode(e);
                relation.setBannerId(entity.getId());
                relations.add(relation);
            });
        }
        if (relations.size() > 0) {
            operationsBannerRelationService.saveBatch(relations);
        }
        /**
         *  添加系统日志
         * */
        String userId = request.getHeader("user-id");
        String username = request.getHeader("merchantName");
        String ip = IPUtils.getIpAddr(request);
        MerchantUtil util = new MerchantUtil();
        MerchantLogFiledVO vo1 = util.compareObject(oldInfo,paramVO,MerchantUtil.filterOpertationAddNames,MerchantUtil.FIELD_OPERATION_MAPPING);
        merchantLogService.saveOperationRoomLog(MerchantLogTypeEnum.SAVE_INFO.getCode(),  MerchantLogTypeEnum.SAVE_INFO.getRemark()
                , MerchantLogPageEnum.MERCHANT_INFO_MANAGER_AGENT_72.getRemark(),
                MerchantLogTypeEnum.SAVE_INFO.getPageCode().get(0), userId, username, userId,
                vo1.getFieldName(), vo1.getBeforeValues(), vo1.getAfterValues(),username,userId,ip);
        return Response.returnSuccess();
    }

    @Override
    public Response<?> delete(OperationsVO paramVO) {
        OperationsBannerSet entity = bannerSetMapper.selectById(paramVO.getId());
        entity.setLogicDelete(1);
        bannerSetMapper.updateById(entity);
        relationMapper.delete(new QueryWrapper<OperationsBannerRelation>().eq("banner_id", paramVO.getId()));
        return Response.returnSuccess();
    }

    @Override
    public Response<?> deleteKey(String key) {
        RedisTemp.deleteByPattern(key);
        return Response.returnSuccess();
    }
}
