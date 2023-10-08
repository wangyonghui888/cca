import com.panda.sport.order.OrderApplication;
import com.panda.sport.order.schedule.InfomationOf2TimesOrderTask;
import com.panda.sport.order.schedule.MerchantKeyMigrateTask;
import com.panda.sport.order.schedule.OrderBySToRedisTask;
import com.panda.sport.order.schedule.OrderSettleTask;
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
@SpringBootTest(classes = OrderApplication.class)
@Slf4j
public class Test {
/*
	@Autowired
	private VipOrderSettleTask vipOrderSettleTask;
*/

	@Autowired
	private  OrderSettleTask  orderSettleTask;

	@Autowired
	private OrderBySToRedisTask orderBySToRedisTask;

	@Autowired
	private MerchantKeyMigrateTask merchantKeyMigrateTask;


	@Autowired
	private InfomationOf2TimesOrderTask infomationOf2TimesOrderTask;


/*

	@org.junit.Test
	public void vipOrderSettleTask() throws Exception{
		vipOrderSettleTask.execute("202205,111111");
		// 因为内部方法有异步线程，当前线程main线程提前断开，所以等异步线程结束后才可停止当前线程。
		Thread.sleep(Integer.MAX_VALUE);
	}

*/

	@org.junit.Test
	public void orderSettleTask() throws Exception{
			orderSettleTask.execute("20220602,111111");
			orderSettleTask.execute(null);
	//		orderSettleTask.execute("20220602");
		Thread.sleep(Integer.MAX_VALUE);
	}

	@org.junit.Test
	public void orderBySToRedisTask() throws Exception{
		orderBySToRedisTask.execute(null);
		Thread.sleep(Integer.MAX_VALUE);
	}

	@org.junit.Test
	public void merchantKeyMigrateTask() throws Exception{
		merchantKeyMigrateTask.execute(null);
		Thread.sleep(Integer.MAX_VALUE);
	}



}
