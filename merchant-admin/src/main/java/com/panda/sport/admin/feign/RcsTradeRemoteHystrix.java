package com.panda.sport.admin.feign;
/**
 * @author Administrator
 * @date 2021/5/9
 * @TIME 12:02
 */

import com.panda.sport.merchant.common.dto.*;
import com.panda.sport.merchant.common.vo.RiskMerchantImportUpdateVo;
import com.panda.sport.merchant.common.vo.UserRiskControlQueryAllReqVO;
import com.panda.sport.merchant.common.vo.merchant.AbnormalUserVo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 *@ClassName RcsRemoteHystrix
 *@Description TODO
 *@Author Administrator
 *@Date 2021/5/9 12:02
 */
@Slf4j
@Component
public class RcsTradeRemoteHystrix implements FallbackFactory<PandaRcsTradeClient> {


    @Override
    public PandaRcsTradeClient create(Throwable cause) {
        log.error("PandaRcsTradeClient error",cause);
        return new PandRcsTradeFallBack() {
            @Override
            public Response<Boolean> updateUserSpecialLimit(Request<UserSpecialLimitDto> var1) {
                return null;
            }

            @Override
            public Response<RcsTradeRestrictMerchantSettingDto> getUserTradeRestrict(Request<UserTradeRestrictDto> var1) {
                return null;
            }
            @Override
            public Map<String, Object>  queryAbnormalUserList(AbnormalUserVo request){
                return null;
            }

            @Override
            public Map queryPageUserRiskControlList(UserRiskControlPageQueryDTO userRiskControlPageQueryDTO) {
                return null;
            }

            @Override
            public Map updateStatus(UserRiskControlStatusEditDTO userRiskControlStatusEditDTO) {
                return null;
            }

            @Override
            public Map exportUserRiskControlList(UserRiskControlQueryAllReqVO userRiskControlQueryAllReqVO) {
                return null;
            }

            @Override
            public Map importUserRiskControlList(List<RiskMerchantImportUpdateVo> riskMerchantImportUpdateVoList) {
                return null;
            }

            @Override
            public Map pendingCount(UserRiskControlPendingCountQueryDTO queryDTO) {
                return null;
            }
        };
    }
}
