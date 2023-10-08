package com.panda.sport.order.feign;/**
 * @author Administrator
 * @date 2021/5/9
 * @TIME 12:02
 */

import com.panda.sport.merchant.common.dto.*;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *@ClassName RcsRemoteHystrix
 *@Description TODO
 *@Author Administrator
 *@Date 2021/5/9 12:02
 */
@Slf4j
@Component
public class RcsUserLimitHystrix implements FallbackFactory<PandaRcsUserLimitClient> {


    @Override
    public PandaRcsUserLimitClient create(Throwable cause) {
        log.error("RcsRemoteHystrix error",cause);
        return null;
    }
}
