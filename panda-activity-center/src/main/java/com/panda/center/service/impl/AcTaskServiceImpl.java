package com.panda.center.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.panda.center.entity.AcTask;
import com.panda.center.entity.Games;
import com.panda.center.mapper.activity.AcTaskMapper;
import com.panda.center.mapper.trader.GamesMapper;
import com.panda.center.result.Response;
import com.panda.center.service.IAcTaskService;
import com.panda.center.vo.SportAndPlayTreeVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 活动任务表 服务实现类
 * </p>
 *
 * @author Auto Generator
 * @since 2021-12-24
 */
@Service
public class AcTaskServiceImpl extends ServiceImpl<AcTaskMapper, AcTask> implements IAcTaskService {
    @Resource
    private AcTaskMapper acTaskMapper;
    @Resource
    private GamesMapper gamesMapper;

    @Value("${anchorIds:257154660915053,257561197207055}")
    private List<Long> anchorIds;

    @Override
    public List<SportAndPlayTreeVO> getSportAndPlayTree() {
        List<SportAndPlayTreeVO> list = Lists.newArrayList();
        List<Games> gamesList = gamesMapper.selectList(new QueryWrapper<Games>().eq("status", 1));
        for (Games games : gamesList) {
            SportAndPlayTreeVO playTreeVO = SportAndPlayTreeVO.getInstance();
            playTreeVO.setTitle(games.getCnName());
            String key = String.valueOf(games.getId());
            playTreeVO.setKey(key);
            playTreeVO.setParentId("0");
            List<SportAndPlayTreeVO> childList = Lists.newArrayList();
            // if (anchorIds.contains(games.getId())) {
            childList.add(buildChildTree(games.getCnName() + "-非主播盘", "3", key));
            childList.add(buildChildTree(games.getCnName() + "-主播盘", "4", key));
            // }
            playTreeVO.setChildren(childList);
            list.add(playTreeVO);
        }
        return list;
    }

    private SportAndPlayTreeVO buildChildTree(String title, String key, String parentId) {
        SportAndPlayTreeVO detailTreeVO = SportAndPlayTreeVO.getInstance();
        detailTreeVO.setTitle(title);
        detailTreeVO.setKey(key);
        detailTreeVO.setParentId(parentId);
        detailTreeVO.setChildren(Lists.newArrayList());
        return detailTreeVO;
    }

    @Override
    public Response<?> changeOrder(Integer id, Integer sort) {
        //获取当前排序
        AcTask AcTask = acTaskMapper.selectById(id);
        if (ObjectUtil.isNull(AcTask)) {
            return Response.returnFail("data empty");
        }
        Integer curNo = AcTask.getOrderNo();
        if (curNo > sort) {
            //上移
            List<AcTask> sortList = acTaskMapper.selectList(new QueryWrapper<AcTask>()
                    .eq("act_id", AcTask.getActId())
                    .ge("order_no", sort)
                    .lt("order_no", curNo)
                    .orderByAsc("order_no"));
            //依次+1
            sortList.forEach(v -> v.setOrderNo(v.getOrderNo() + 1));
            this.updateBatchById(sortList);
        }
        if (curNo < sort) {
            //下移
            List<AcTask> sortList = acTaskMapper.selectList(new QueryWrapper<AcTask>()
                    .eq("act_id", AcTask.getActId())
                    .gt("order_no", curNo)
                    .le("order_no", sort)
                    .orderByAsc("order_no"));
            //依次+1
            sortList.forEach(v -> v.setOrderNo(v.getOrderNo() - 1));
            this.updateBatchById(sortList);
        }
        //更新当前数据
        AcTask.setOrderNo(sort);
        this.updateById(AcTask);
        return Response.returnSuccess();
    }

    @Override
    public Integer getCurMaxNo(Integer actId) {
        return acTaskMapper.getCurMaxNo(actId);
    }
}
