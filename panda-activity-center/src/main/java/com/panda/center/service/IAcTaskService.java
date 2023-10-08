package com.panda.center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.center.entity.AcTask;
import com.panda.center.result.Response;
import com.panda.center.vo.SportAndPlayTreeVO;

import java.util.List;

/**
 * <p>
 * 活动任务表 服务类
 * </p>
 *
 * @author Auto Generator
 * @since 2021-12-24
 */
public interface IAcTaskService extends IService<AcTask> {

    Response<?> changeOrder(Integer id, Integer sort);

    Integer getCurMaxNo(Integer actId);

    /**
     * 树节点
     *
     * @return com.panda.center.vo.SportAndPlayTreeVO
     */
    List<SportAndPlayTreeVO> getSportAndPlayTree();
}
