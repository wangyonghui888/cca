package com.panda.center.service;



import com.github.pagehelper.PageInfo;
import com.panda.center.vo.ActivityBetStatDTO;
import com.panda.center.vo.ActivityBetStatVO;

import java.util.List;

/**
 * @author :  toney
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.center.service
 * @Description :  TODO
 * @Date: 2021-12-25 10:17
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
public interface BonusService {

	void executeDailyTask(Long startL, Long endL, Long nowL);

	void executeSumTask();

	// void processTickets(List<TicketMessageVO> allMessages, Long nowL);

	void clearDailyTask();
}
