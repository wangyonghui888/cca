package com.panda.center.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.panda.center.entity.SOlympicLuckyboxDictPo;
import com.panda.center.enums.BoxTypeEnum;
import com.panda.center.result.BoxListForm;
import com.panda.center.result.Response;
import com.panda.center.result.SDailyLuckyBoxNumberPO;
import com.panda.center.result.SDailyLuckyBoxNumberVo;
import com.panda.center.service.impl.AcLuckyBoxService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;


/**
 * 后台盲盒设置
 */
@RestController
@RequestMapping("/manage/dj/luckybox")
public class AcLuckyboxController {


    @Autowired
    private AcLuckyBoxService acTaskService;

    /**
     * 列表
     * @param boxListForm
     * @return
     */
    @PostMapping(value = "/list")
    public Response<?> list(@RequestBody BoxListForm boxListForm) {

        List<SOlympicLuckyboxDictPo> sOlympicLuckyboxDictVoList = acTaskService.getLuckyBoxDict(boxListForm);
        PageInfo<SOlympicLuckyboxDictPo> pageInfo = new PageInfo<>(sOlympicLuckyboxDictVoList);
        return Response.returnSuccess(pageInfo);
    }

    /**
     * 发现盲盒的
     * @param boxListForm
     * @return
     */
    @PostMapping(value = "/findBox")
    public Response<?>findBox(@RequestBody BoxListForm boxListForm) {

        if (StringUtils.isEmpty(boxListForm.getId())){
            return Response.returnFail("ID不能为空");
        }

        SOlympicLuckyboxDictPo sOlympicLuckyboxDictPo = acTaskService.findBox(boxListForm.getId());
        if (StringUtils.isEmpty(sOlympicLuckyboxDictPo)) {
            return Response.returnFail("查询结果不存在");
        }
        return Response.returnSuccess(sOlympicLuckyboxDictPo);
    }

    /**
     * 更新盲盒
     * @param boxUpdateForm
     * @return
     */
    @RequestMapping(value = "/changeOrder")
    public Response<?>changeOrder(long id,int orderNum) {

        try {
            // 快速更新
            acTaskService.changeOrder1(id,orderNum);
            return Response.returnSuccess();
        } catch (Exception e) {
            String errorLog = String.format("执行出现异常，%s", e.getMessage());
            return Response.returnFail(errorLog);
        }
    }
    /**
     * 更新盲盒
     * @param boxUpdateForm
     * @return
     */
    @PostMapping(value = "/updateBox")
    public Response<?>updateBox(@RequestBody SOlympicLuckyboxDictPo boxUpdateForm, HttpServletRequest request) {


    	try {
    		/*if (boxUpdateForm.getId() == null) {
			    return Response.returnFail("ID不能为空");
			}*/
    		if (boxUpdateForm.getAward() == 0) {
    			return Response.returnFail("奖金不能为空");
    		}

    		//保存日志
    		String userId = request.getHeader("user-id");
    		String userName = request.getHeader("merchantName");

    		SOlympicLuckyboxDictPo sOlympicLuckyboxDict = new SOlympicLuckyboxDictPo();
    		sOlympicLuckyboxDict.setId(boxUpdateForm.getId());
    		BeanUtils.copyProperties(boxUpdateForm,sOlympicLuckyboxDict);

    		sOlympicLuckyboxDict.setModifyTime(System.currentTimeMillis());

    		// 快速更新
    		acTaskService.boxUpdate(sOlympicLuckyboxDict,userId,userName);
    		return Response.returnSuccess();
    	} catch (Exception e) {
    		String errorLog = String.format("执行出现异常，%s", e.getMessage());
    		return Response.returnFail(errorLog);
    	}
    }


    /**
     * 查询每日的盲盒的设置
     * @return
     */
    @PostMapping(value = "/findDaily")
    public Response<?>findDaily() {

        SDailyLuckyBoxNumberVo sDailyLuckyBoxNumberVo = acTaskService.findDaily();
        return Response.returnSuccess(sDailyLuckyBoxNumberVo);
    }


    /**
     * 更新每日的配置
     * @param boxUpdateForm
     * @return
     */
    @PostMapping(value = "/updateDaily")
    public Response<?>updateDaily(@RequestBody SDailyLuckyBoxNumberVo boxUpdateForm,HttpServletRequest request) {

        try {

            //保存日志
            String userId = request.getHeader("user-id");
            String userName = request.getHeader("merchantName");
            SDailyLuckyBoxNumberVo beforeValue = acTaskService.findDaily();

            // 银每日更新
            SDailyLuckyBoxNumberPO silver = new SDailyLuckyBoxNumberPO();
            Long time = System.currentTimeMillis();
            silver.setBoxType(BoxTypeEnum.SILVER.getCode());
            silver.setDailyNumber( StringUtils.isEmpty(boxUpdateForm.getSilverDailyNumber()) ? null : boxUpdateForm.getSilverDailyNumber());
            silver.setShowNumber(StringUtils.isEmpty(boxUpdateForm.getSilverShowNumber()) ? null : boxUpdateForm.getSilverShowNumber());
            silver.setShowRate(StringUtils.isEmpty(boxUpdateForm.getShowRate()) ? null : boxUpdateForm.getShowRate());
            silver.setModifyTime(time);
            silver.setToken(boxUpdateForm.getSilverToken());
            acTaskService.dailyUpdate(silver);

            // 黄金每日更新
            SDailyLuckyBoxNumberPO gold = new SDailyLuckyBoxNumberPO();
            gold.setBoxType(BoxTypeEnum.GOLD.getCode());
            gold.setDailyNumber( StringUtils.isEmpty(boxUpdateForm.getGoldDailyNumber()) ? null : boxUpdateForm.getGoldDailyNumber());
            gold.setShowNumber(StringUtils.isEmpty(boxUpdateForm.getGoldShowNumber()) ? null : boxUpdateForm.getGoldShowNumber());
            gold.setShowRate(StringUtils.isEmpty(boxUpdateForm.getShowRate()) ? null : boxUpdateForm.getShowRate());
            gold.setModifyTime(time);
            gold.setToken(boxUpdateForm.getGoldToken());
            acTaskService.dailyUpdate(gold);

            // 砖石每日更新
            SDailyLuckyBoxNumberPO diamond = new SDailyLuckyBoxNumberPO();
            diamond.setBoxType(BoxTypeEnum.DIAMOND.getCode());
            diamond.setDailyNumber( StringUtils.isEmpty(boxUpdateForm.getDiamondDailyNumber()) ? null : boxUpdateForm.getDiamondDailyNumber());
            diamond.setShowNumber(StringUtils.isEmpty(boxUpdateForm.getDiamondShowNumber()) ? null : boxUpdateForm.getDiamondShowNumber());
            diamond.setShowRate(StringUtils.isEmpty(boxUpdateForm.getShowRate()) ? null : boxUpdateForm.getShowRate());
            diamond.setModifyTime(time);
            diamond.setToken(boxUpdateForm.getDiamondToken());

            acTaskService.dailyUpdate(diamond);
            // 操作
            acTaskService.dailyLog(beforeValue,boxUpdateForm,userId,userName);
            return Response.returnSuccess();

        } catch (Exception e) {
            String errorLog = String.format("执行出现异常，%s", e.getMessage());
            return Response.returnFail(errorLog);
        }


    }
}
