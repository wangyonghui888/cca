/**
 *
 */
package com.panda.center.service.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import com.panda.center.entity.MerchantLog;
import com.panda.center.entity.SOlympicLuckyboxDictPo;
import com.panda.center.enums.BoxTypeEnum;
import com.panda.center.enums.MerchantLogTypeEnum;
import com.panda.center.mapper.activity.AcTaskPOMapper;
import com.panda.center.mapper.activity.OlympicLuckyboxDictMapper;
import com.panda.center.mapper.activity.TActivityConfigMapper;
import com.panda.center.result.BoxListForm;
import com.panda.center.result.SDailyLuckyBoxNumberPO;
import com.panda.center.result.SDailyLuckyBoxNumberVo;
import com.panda.center.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;

/**
 * @ClassName: AcLuckyBoxService
 * @Description: TODO
 * @Author: Star
 * @Date: 2021-12-24 20:11:49
 * @version V1.0
 */
@Service
public class AcLuckyBoxService {

    @Resource
    private AcTaskPOMapper acTaskPOMapper;
    @Resource
    private OlympicLuckyboxDictMapper olympicLuckyboxDictMapper;

    @Autowired
    TActivityConfigMapper tActivityConfigMapper;

    @Autowired
   	MerchantLogServiceImpl merchantLogServiceImpl;

    /**
     * 获取幸运盲盒
     *
     * @return
     */
    public List<SOlympicLuckyboxDictPo> getLuckyBoxDict(BoxListForm boxListForm) {

        //List<SOlympicLuckyboxDictVo> sOlympicLuckyboxDictVos = acTaskPOMapper.getLuckyBoxDict();

        QueryWrapper<SOlympicLuckyboxDictPo> queryWrapper = new QueryWrapper<>();

        List<Integer> isUps = Lists.newArrayList(0,1);
        if(boxListForm.getIsUp() != 2) {
        	isUps.clear();
        	isUps.add(boxListForm.getIsUp());
        }
        queryWrapper.in("is_up", isUps);
        queryWrapper.orderByAsc("order_num");
        //Page<SOlympicLuckyboxDictPo> page = PageHelper.startPage(boxListForm.getPg(), boxListForm.getSize(), true);
        //Page<SOlympicLuckyboxDictPo> page = new Page<SOlympicLuckyboxDictPo>(boxListForm.getPageNum(), boxListForm.getPageSize());
        PageHelper.startPage(boxListForm.getPageNum(), boxListForm.getPageSize(), true);
        //IPage<SOlympicLuckyboxDictPo> selectPage = olympicLuckyboxDictMapper.selectPage(page, queryWrapper);
        List<SOlympicLuckyboxDictPo> selectList = olympicLuckyboxDictMapper.selectList(queryWrapper);
        if(!selectList.isEmpty()) {
    		int orderNum = selectList.get(selectList.size() - 1).getOrderNum();
    		if(orderNum == 0) {
    			synchronized (this) {
    				List<SOlympicLuckyboxDictPo> selectList2 = olympicLuckyboxDictMapper.selectList(null);
    				int orderNum2 = selectList2.get(selectList.size() - 1).getOrderNum();
    				if(orderNum2 == 0)
    				//修改排序值
        			for (int i = 1; i <= selectList2.size(); i++) {
        				SOlympicLuckyboxDictPo sOlympicLuckyboxDictPo = selectList2.get(i - 1);
        				sOlympicLuckyboxDictPo.setOrderNum(i);
        				olympicLuckyboxDictMapper.updateById(sOlympicLuckyboxDictPo);
        			}

    			}

    		}
        }
        return selectList;
    }
    public boolean gameTime() {
        //取本地缓存
    	TActivityConfigMapper.TActivityConfig config = tActivityConfigMapper.selectById(10009);
        if(config == null) {
        	return false;
        }
        Integer status = config.getStatus();
        if(status == 0) return false;

        Long startTime = config.getInStartTime();
        Long endTime = config.getInEndTime();

        if(startTime == null || endTime == null) {
        	return false;
        }

        if(startTime == 0 && endTime == 0) return true;

        long now = System.currentTimeMillis();

        if(startTime == 0 && now <= endTime) return true;

        if(endTime == 0 && now >= startTime) return true;

		return now >= startTime && now <= endTime;
	}

   	public void changeOrder1(long id, int sort) {
    	SOlympicLuckyboxDictPo selectById = olympicLuckyboxDictMapper.selectById(id);

    	int orderNum = selectById.getOrderNum();

         if (orderNum > sort) {
             //上移
             List<SOlympicLuckyboxDictPo> sortList = olympicLuckyboxDictMapper.selectList(new QueryWrapper<SOlympicLuckyboxDictPo>()
                     //大于等于
            		 .ge("order_num", sort)
                     //小于
            		 .lt("order_num", orderNum)
                     .orderByAsc("order_num"));
             //依次+1
             sortList.forEach(v ->{
            	 v.setOrderNum(v.getOrderNum() + 1);
            	 olympicLuckyboxDictMapper.updateById(v);
             });
         }
         if (orderNum < sort) {
             //下移
             List<SOlympicLuckyboxDictPo> sortList = olympicLuckyboxDictMapper.selectList(new QueryWrapper<SOlympicLuckyboxDictPo>()
                     .gt("order_num", orderNum)
                     .le("order_num", sort)
                     .orderByAsc("order_num"));
             sortList.forEach(v ->{
            	 v.setOrderNum(v.getOrderNum() - 1);
            	 olympicLuckyboxDictMapper.updateById(v);
             });
         }
         selectById.setOrderNum(sort);
     	olympicLuckyboxDictMapper.updateById(selectById);
   	}

    /**
     * 更新幸运盲盒
     *
     * @param sOlympicLuckyboxDict
     * @return
     */

    public Integer boxUpdate(SOlympicLuckyboxDictPo sOlympicLuckyboxDict,String userId,String userName) {

    	SOlympicLuckyboxDictPo beforeValue =  acTaskPOMapper.findBox(sOlympicLuckyboxDict.getId());

    	if(sOlympicLuckyboxDict.getDelete() == 1) {

    		//上架的不能删除
    		int isUp = beforeValue.getIsUp();
    		if(isUp ==1) throw new RuntimeException("该奖品已经上架，不能被删除");

    		//活动开启不能删除
    		 if(gameTime()) throw new RuntimeException("活动已经上线，不能被删除");

    		 olympicLuckyboxDictMapper.deleteById(sOlympicLuckyboxDict.getId());

    	}else {
    		if(sOlympicLuckyboxDict.getId() == null) {

    			sOlympicLuckyboxDict.setCreateTime(System.currentTimeMillis());

    			Integer selectMinOrder = olympicLuckyboxDictMapper.selectMinOrder();

    			if(selectMinOrder == null) selectMinOrder = 0;
    			
    			else selectMinOrder = selectMinOrder - 1;
    			
    			sOlympicLuckyboxDict.setOrderNum(selectMinOrder);

    			olympicLuckyboxDictMapper.insert(sOlympicLuckyboxDict);
    		}else {
    			//acTaskPOMapper.boxUpdate(sOlympicLuckyboxDict);
    			olympicLuckyboxDictMapper.updateById(sOlympicLuckyboxDict);

    		}
    	}

    	SOlympicLuckyboxDictPo afterValue =  acTaskPOMapper.findBox(sOlympicLuckyboxDict.getId());
    	MerchantLog logPO = new MerchantLog();
		logPO.setUserId(Long.valueOf(userId)).setUserName(userName)
		        .setOperatType(MerchantLogTypeEnum.SET_BOX_NUMBER.getCode())
		        .setTypeName("盲盒奖品编辑").setPageName("运营管理—盲盒奖品设置").setPageCode("")
		        .setMerchantCode("").setMerchantName("编辑盲盒奖品").setDataId("1100002")
		        .setOperatField(JsonUtils.listToJson(Collections.singletonList("编辑盲盒奖品")))
		        .setBeforeValues(JsonUtils.listToJson(Collections.singletonList(beforeValue)))
		        .setAfterValues(JsonUtils.listToJson(Collections.singletonList(afterValue)))
		        .setLogTag(1).setOperatTime(System.currentTimeMillis());

		// 插入日志
		merchantLogServiceImpl.save(logPO);
        return 1;
    }


    /**
     * 发现幸运盲盒
     *
     * @param id
     * @return
     */

    public SOlympicLuckyboxDictPo findBox(Long id) {
        return acTaskPOMapper.findBox(id);
    }


    /**
     * 发现每日的盲盒的设置
     *
     * @return
     */

    public List<SDailyLuckyBoxNumberPO> getDailyBox() {
        return acTaskPOMapper.getDailyBox();
    }


    public SDailyLuckyBoxNumberVo findDaily() {

        List<SDailyLuckyBoxNumberPO> dailyLuckyBoxNumberPOList = acTaskPOMapper.getDailyBox();
        // 赋值输出
        SDailyLuckyBoxNumberVo sDailyLuckyBoxNumberVo = new SDailyLuckyBoxNumberVo();
        for (SDailyLuckyBoxNumberPO sDailyLuckyBoxNumberPO : dailyLuckyBoxNumberPOList) {
            if (BoxTypeEnum.SILVER.getCode().equals(sDailyLuckyBoxNumberPO.getBoxType())) {
                sDailyLuckyBoxNumberVo.setSilverShowNumber(sDailyLuckyBoxNumberPO.getShowNumber()).setSilverDailyNumber(sDailyLuckyBoxNumberPO.getDailyNumber());
                sDailyLuckyBoxNumberVo.setSilverToken(sDailyLuckyBoxNumberPO.getToken());
            }
            if (BoxTypeEnum.GOLD.getCode().equals(sDailyLuckyBoxNumberPO.getBoxType())) {
                sDailyLuckyBoxNumberVo.setGoldShowNumber(sDailyLuckyBoxNumberPO.getShowNumber()).setGoldDailyNumber(sDailyLuckyBoxNumberPO.getDailyNumber());
                sDailyLuckyBoxNumberVo.setGoldToken(sDailyLuckyBoxNumberPO.getToken());
            }
            if (BoxTypeEnum.DIAMOND.getCode().equals(sDailyLuckyBoxNumberPO.getBoxType())) {
                sDailyLuckyBoxNumberVo.setDiamondShowNumber(sDailyLuckyBoxNumberPO.getShowNumber()).setDiamondDailyNumber(sDailyLuckyBoxNumberPO.getDailyNumber()).setShowRate(sDailyLuckyBoxNumberPO.getShowRate());
                sDailyLuckyBoxNumberVo.setDiamondToken(sDailyLuckyBoxNumberPO.getToken());
            }
        }
        return sDailyLuckyBoxNumberVo;

    }

    /**
     * 更新每日
     *
     * @param sOlympicLuckyboxDictPo
     * @return
     */

    public Integer dailyUpdate(SDailyLuckyBoxNumberPO sOlympicLuckyboxDictPo) {

        return acTaskPOMapper.dailyUpdate(sOlympicLuckyboxDictPo);
    }

    /**
     * 插入
     * @param beforeValue
     * @param afterValue
     * @param userId
     * @param userName
     * @return
     */

    public Integer dailyLog(SDailyLuckyBoxNumberVo beforeValue, SDailyLuckyBoxNumberVo afterValue, String userId, String userName) {

    	MerchantLog logPO = new MerchantLog();
		logPO.setUserId(Long.valueOf(userId)).setUserName(userName)
		        .setOperatType(MerchantLogTypeEnum.SET_BOX_NUMBER.getCode())
		        .setTypeName("盲盒个数设置").setPageName("运营管理—盲盒奖品设置").setPageCode("")
		        .setMerchantCode("").setMerchantName("盲盒个数设置").setDataId("1100002")
		        .setOperatField(JsonUtils.listToJson(Collections.singletonList("盲盒个数设置")))
		        .setBeforeValues(JsonUtils.listToJson(Collections.singletonList(beforeValue)))
		        .setAfterValues(JsonUtils.listToJson(Collections.singletonList(afterValue)))
		        .setLogTag(1).setOperatTime(System.currentTimeMillis());

		// 插入日志
		merchantLogServiceImpl.save(logPO);
        return null;
    }





    public Integer getCurMaxNo(Integer actId) {
        return acTaskPOMapper.getCurMaxNo(actId);
    }



}
