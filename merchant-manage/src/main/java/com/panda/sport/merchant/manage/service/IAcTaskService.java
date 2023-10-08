package com.panda.sport.merchant.manage.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.panda.sport.merchant.common.po.bss.AcTaskPO;
import com.panda.sport.merchant.common.po.bss.SDailyLuckyBoxNumberPO;
import com.panda.sport.merchant.common.po.bss.SOlympicLuckyboxDictPo;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.SportAndPlayTreeVO;
import com.panda.sport.merchant.common.vo.activity.SDailyLuckyBoxNumberVo;
import com.panda.sport.merchant.manage.entity.form.BoxListForm;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 活动任务表 服务类
 * </p>
 *
 * @author baylee
 * @since 2021-08-24
 */
public interface IAcTaskService extends IService<AcTaskPO> {

    /**
     * 树节点
     *
     * @return com.panda.sport.merchant.common.vo.SportAndPlayTreeVO
     */
    List<SportAndPlayTreeVO> getSportAndPlayTree();

    List<SportAndPlayTreeVO> getVirtualSportTree();

    /**
     * 获取幸运盲盒
     *
     * @return
     */
    List<SOlympicLuckyboxDictPo> getLuckyBoxDict(BoxListForm boxListForm);


    /**
     * 更新盲盒的参数
     *
     * @param sOlympicLuckyboxDict
     * @return
     */
    Integer boxUpdate(SOlympicLuckyboxDictPo sOlympicLuckyboxDict,String userId,String userName,String ip );

    /**
     * 发现盲盒配置
     *
     * @param id
     * @return
     */
    SOlympicLuckyboxDictPo findBox(Long id);


    List<SDailyLuckyBoxNumberPO> getDailyBox();


    /**
     * 更新每日
     *
     * @param sDailyLuckyBoxNumberPO
     * @return
     */
    Integer dailyUpdate(SDailyLuckyBoxNumberPO sDailyLuckyBoxNumberPO);

    SDailyLuckyBoxNumberVo findDaily();

    /**
     * 操作日志
     * @param beforeValue
     * @param userId
     * @param userName
     * @return
     */
    Integer dailyLog(SDailyLuckyBoxNumberVo beforeValue,SDailyLuckyBoxNumberVo afterValue, String userId, String userName,String ip);

    /**
     * 获取当前最大排序字段
     *
     * @return
     */
    Integer getCurMaxNo(Integer actId);

    Response<?> changeOrder(HttpServletRequest request,Integer id, Integer sort);

	/**
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param id
	* @param orderNum
	* @author: Star
	* @Date: 2021-11-17 19:12:34
	*/ 
	void changeOrder1(HttpServletRequest request,long id, int orderNum);
}
