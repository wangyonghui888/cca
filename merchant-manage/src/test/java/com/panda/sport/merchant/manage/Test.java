package com.panda.sport.merchant.manage;

import com.panda.sport.merchant.manage.schedule.MerchantCurrencyTask;
import com.panda.sport.merchant.manage.schedule.MerchantKeyExportTask;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author :  toney
 * @Project Name :  panda-merchant
 * @Package Name :  PACKAGE_NAME
 * @Description :  vip用户注单统计测试
 * @Date: 2021-12-14 19:14
 * @ModificationHistory amos    20220518    优化本地测试
 * --------  ---------  --------------------------
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ManageApplication.class)
@Slf4j
public class Test {

	@Autowired
	private MerchantCurrencyTask merchantCurrencyTask;

	@Autowired
	private MerchantKeyExportTask merchantKeyExportTask;

	@org.junit.Test
	public void merchantCurrencyTask() throws Exception{
		merchantCurrencyTask.execute();
		// 因为内部方法有异步线程，当前线程main线程提前断开，所以等异步线程结束后才可停止当前线程。
		Thread.sleep(Integer.MAX_VALUE);
	}

	@org.junit.Test
	public void MerchantKeyExportTask() throws Exception{
		String str = "1";
		merchantKeyExportTask.execute(str);
		Thread.sleep(Integer.MAX_VALUE);
	}

}
