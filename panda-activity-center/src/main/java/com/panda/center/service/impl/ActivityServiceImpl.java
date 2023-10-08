package com.panda.center.service.impl;

import com.github.pagehelper.PageInfo;
import com.panda.center.service.ActivityService;
import com.panda.center.vo.ActivityBetStatDTO;
import com.panda.center.vo.ActivityBetStatVO;
import com.panda.center.mapper.activity.UserReport4Mapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @author :  toney
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.center.service.impl
 * @Description :  TODO
 * @Date: 2021-12-25 10:18
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@Service
@RefreshScope
public class ActivityServiceImpl implements ActivityService {
	@Autowired
	private UserReport4Mapper userReport4Mapper;

	@Override
	public PageInfo<ActivityBetStatVO> getActivityBetStatList(ActivityBetStatDTO param) {
		try {

			Integer total = userReport4Mapper.getActivityBetStatCount(param);

			if (total == null) {
				total = 0;
			}

			List<ActivityBetStatVO> result = userReport4Mapper.getActivityBetStatList(param);

			PageInfo<ActivityBetStatVO> page = new PageInfo<>(result);
			page.setEndRow(param.getPageSize());
			page.setStartRow((param.getPageNum() - 1) * param.getPageSize());
			page.setTotal(total);
			return page;
		} catch (Exception ex) {
			log.error("ActivityServiceImpl.getActivityBetStatList,exception:" + ex.getMessage(), ex);
			return null;
		}
	}

	@Override
	public List<ActivityBetStatVO> exportExcel(ActivityBetStatDTO param) {
		int size = 2000;


		param.setPageSize(size);
		List<ActivityBetStatVO> resultList = new Vector<>();

		Integer total = userReport4Mapper.getActivityBetStatCount(param);

		if (total == 0) {
			return resultList;
		}

		int threadNum = (int) Math.ceil((float) total / size);

		CountDownLatch countDownLatch = new CountDownLatch(threadNum);
		ExecutorService threadPool = Executors.newFixedThreadPool(threadNum);

		try {
			for (int i = 1; i <= threadNum; i++) {

				final ActivityBetStatDTO vo = new ActivityBetStatDTO();

				BeanUtils.copyProperties(param, vo);
				vo.setPageNum(i);

				threadPool.execute(() -> {
					List<ActivityBetStatVO> activityBetStatList = userReport4Mapper.getActivityBetStatList(vo);
					if (CollectionUtils.isNotEmpty(activityBetStatList)) {
						resultList.addAll(activityBetStatList);
					}
					countDownLatch.countDown();
				});
			}

			countDownLatch.await();
			return resultList;
		} catch (Exception ex) {
			log.error("导出报表数据异常", ex);
		} finally {
			threadPool.shutdown();
		}

		return null;
	}
}
