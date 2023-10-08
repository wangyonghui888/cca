package com.panda.sport.admin.feign;

import com.panda.sport.admin.vo.RateVo;
import com.panda.sport.merchant.common.vo.OrderOperationVO;
import com.panda.sport.merchant.common.vo.user.UserRetryTransferVO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sport.admin.feign
 * @Description :  TODO
 * @Date: 2021-07-06 17:46:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@Component
public class BssRemoteHystrix implements FallbackFactory<BssBackendClient> {


    @Override
    public BssBackendClient create(Throwable cause) {
        log.error("BssRemoteHystrix error",cause);
        return new BssBackendFallBack() {

/*            @Override
            public Object retryTransfer(UserRetryTransferVO vo) {
                log.error("retryTransfer 调用异常");
                return null;
            }*/

            @Override
            public Object orderOperation(OrderOperationVO orderOperation) {
                log.error("orderOperation 调用异常");
                return null;
            }

            @Override
            public Object queryRateList(RateVo rateVo) {
                log.error("queryRateList 调用异常");
                return null;
            }

            @Override
            public Object queryRateLogList(RateVo rateVo) {
                log.error("queryRateList 调用异常");
                return null;
            }
        };
    }
}
