package com.panda.sport.order.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.panda.sport.backup.mapper.BackupOrderMapper;
import com.panda.sport.merchant.common.enums.NoticeLanguageEnum;
import com.panda.sport.merchant.common.enums.NoticeTypeEnum;
import com.panda.sport.merchant.common.vo.Response;
import com.panda.sport.merchant.common.vo.api.MatchVO;
import com.panda.sport.order.OrderApplication;
import com.panda.sport.order.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class)
@Slf4j
class MatchControllerTest {


    @Value("${abnormal.event.user.zs.context:您好，经风控部门进一步检查，此赛事以下会员为异常下注会员，为避免此类会员对贵司造成不必要的损失，已做风控处理。同时我方建议关闭此类会员的账户或者限制此类会员的投注额度。}")
    private String zsContext;
    @Value("${abnormal.event.user.en.context:Hello, After further inspection by the risk control department, the following members of this race are abnormal betting members. In order to avoid unnecessary losses to your company caused by such members, risk control has been carried out. At the same time, we propose to close the accounts of such members or limit the betting limit of such members.}")
    private String enContext;

    @Autowired
    private BackupOrderMapper backupOrderMapper;

    @Autowired
    private MatchService sportService;
    @Test
    void getMatchInfoByMatchId() {
        //String noticeTypeId = NoticeTypeEnum.LOL.getCode().toString();
        String noticeTypeId = NoticeTypeEnum.ABNORMAL_EVENT_USER.getCode().toString();
        String matchId = "3086242";
        Response<?> response = sportService.getMatchInfoByMatchId(noticeTypeId, matchId);
        if(StringUtils.isNoneBlank(String.valueOf(response.getData()))) {
            Map<Integer, Map<String, Object>> result = (Map<Integer, Map<String, Object>>)  response.getData();
            log.info("result ==> " + result);
        }
    }

    @Test
    void getMatchInfo() {

        String zscontext = zsContext,
                encontext = enContext,
                matchId= "30867418";

        MatchVO result = backupOrderMapper.selectMatchInfo(Long.valueOf(matchId));
        if(result == null) {
           log.info(String.format("matchId 【%s】没有找到对应的赛事信息！", matchId));
        } else  {
            if(result.getMatchId() == null) {
                log.info("该赛事没有对应的投注记录！");
            }

            zscontext = zscontext + "\nMatch ID：" + matchId + "\n" + result.getMatchInfoZs() + "\n" +
                    result.getSportNameZs() + StringPool.SLASH + result.getTournamentNameZs() + "\n" + result.getBeginTimeZs();
            encontext = encontext + "\nMatch ID：" + matchId + "\n" + result.getMatchInfoEn() + "\n" +
                    result.getSportNameEn() + StringPool.SLASH + result.getTournamentNameEn() + "\n" + result.getBeginTimeEn();


            Map<Integer, Map<String, Object>> resultMap = new HashMap<>(2);
            Map<String, Object> zsmap = new HashMap<>(2);
            zsmap.put("langType", NoticeLanguageEnum.ZS.getKey());
            zsmap.put("context", zscontext);
            resultMap.put(0, zsmap);
            Map<String, Object> enmap = new HashMap<>(2);
            enmap.put("langType", NoticeLanguageEnum.EN.getKey());
            enmap.put("context", encontext);
            resultMap.put(1, enmap);
            log.info( "response：" + resultMap);
        }
    }

    @Test
    void sportList() {
        try {
            Response<Object> sportList = sportService.getSportList();
            log.info(String.valueOf(sportList.getData()));
        }catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}