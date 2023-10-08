//package com.panda.sport.merchant.manage;
//
//import com.alibaba.fastjson.JSONObject;
//import com.panda.sport.merchant.common.po.bss.SportDutyTraderPO;
//import com.panda.sport.merchant.common.po.merchant.mq.MarketResultMessagePO;
//import com.panda.sport.merchant.common.utils.DateUtils;
//import com.panda.sport.merchant.common.vo.MerchantAmountAlertVO;
//import com.panda.sport.merchant.manage.service.MerchantNewsService;
//import com.panda.sport.merchant.manage.service.SportDutyTraderService;
//import com.panda.sport.merchant.manage.service.impl.MerchantNoticeServiceImpl;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.util.StringUtils;
//
//import java.text.DateFormat;
//import java.util.Date;
//import java.util.List;
//
///**
// * @auth: YK
// * @Description:TODO
// * @Date:2020/6/29 21:27
// */
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class TestApplication {
//
//    @Autowired
//    private MerchantNoticeServiceImpl merchantNoticeService;
//
//    @Autowired
//    private SportDutyTraderService sportDutyTraderService;
//
//    @Autowired
//    private MerchantNewsService merchantNewsService;
//
//    @Test
//    public void t() {
//
//        //String message = "{\"score\":null,\"periodId\":null,\"sportId\":0,\"matchId\":839033,\"marketId\":1240264279014678530,\"settlementTimes\":2,\"totalSettlementTimes\":1,\"marketOptionsResults\":[{\"optionsId\":1276866777507217410,\"result\":3},{\"optionsId\":1276866777532383234,\"result\":4},{\"optionsId\":1276866777553354755,\"result\":3}]}";
//
//       // String message = "{\"score\":\"0:0\",\"periodId\":7,\"sportId\":1,\"matchId\":839033,\"marketId\":145537259292115071,\"settlementTimes\":2,\"totalSettlementTimes\":2,\"marketOptionsResults\":[{\"optionsId\":143401780369013311,\"result\":3},{\"optionsId\":1278580455168057346,\"result\":2}]}";
//
//        String message = "{\n" +
//                "    \"marketId\": 141380450930030200,\n" +
//                "    \"marketOptionsResults\": [\n" +
//                "        {\n" +
//                "            \"optionsId\": 143401780369013311,\n" +
//                "            \"result\": 3\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"optionsId\": 1278580455168057346,\n" +
//                "            \"result\": 2\n" +
//                "        }\n" +
//                "    ],\n" +
//                "    \"matchId\": 1691719,\n" +
//                "    \"periodId\": 7,\n" +
//                "    \"score\": \"0:0\",\n" +
//                "    \"settlementTimes\": 2,\n" +
//                "    \"sportId\": 1,\n" +
//                "    \"totalSettlementTimes\": 2\n" +
//                "}";
//
//        MarketResultMessagePO marketResultMessagePO = JSONObject.parseObject(message, MarketResultMessagePO.class);
//
//        if (StringUtils.isEmpty(marketResultMessagePO)) {
//            return;
//        }
//
//        merchantNoticeService.addOsmcMqNotice(marketResultMessagePO);
//        System.out.println("执行车工");
//
//    }
//
//    @Test
//    public void test(){
//        String test="[" +
//                "    {" +
//                "      \"userCode\": \"admin\"," +
//                "      \"userId\": \"1\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"alva\"," +
//                "      \"userId\": \"10015\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"azan\"," +
//                "      \"userId\": \"10014\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"benz\"," +
//                "      \"userId\": \"10040\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"bobby\"," +
//                "      \"userId\": \"10035\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_1_test\"," +
//                "      \"userId\": \"206864\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_10_test\"," +
//                "      \"userId\": \"100004\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_11_test\"," +
//                "      \"userId\": \"100005\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_12_test\"," +
//                "      \"userId\": \"100006\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_13_test\"," +
//                "      \"userId\": \"100007\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_14_test\"," +
//                "      \"userId\": \"100008\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_16_test\"," +
//                "      \"userId\": \"100010\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_17_test\"," +
//                "      \"userId\": \"100011\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_18_test\"," +
//                "      \"userId\": \"100012\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_19_test\"," +
//                "      \"userId\": \"100013\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_2_test\"," +
//                "      \"userId\": \"210598\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_21_test\"," +
//                "      \"userId\": \"100015\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_22_test\"," +
//                "      \"userId\": \"100016\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_23_test\"," +
//                "      \"userId\": \"100017\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_24_test\"," +
//                "      \"userId\": \"100018\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_25_test\"," +
//                "      \"userId\": \"100019\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_3_test\"," +
//                "      \"userId\": \"651333\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_30_test\"," +
//                "      \"userId\": \"100024\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_4_test\"," +
//                "      \"userId\": \"218825\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_6_test\"," +
//                "      \"userId\": \"252178\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"BW_7_test\"," +
//                "      \"userId\": \"100001\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"cable\"," +
//                "      \"userId\": \"651337\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"carvin\"," +
//                "      \"userId\": \"10034\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"cptest\"," +
//                "      \"userId\": \"10018\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"david\"," +
//                "      \"userId\": \"651340\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"logan\"," +
//                "      \"userId\": \"651335\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"marshall\"," +
//                "      \"userId\": \"10031\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"melo\"," +
//                "      \"userId\": \"651339\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"nice\"," +
//                "      \"userId\": \"10024\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"ninja\"," +
//                "      \"userId\": \"10025\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"ordertest\"," +
//                "      \"userId\": \"8\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"pi\"," +
//                "      \"userId\": \"10011\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"ray\"," +
//                "      \"userId\": \"10028\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"saga\"," +
//                "      \"userId\": \"10008\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"sctest\"," +
//                "      \"userId\": \"10012\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"sctest003\"," +
//                "      \"userId\": \"10020\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"shtest\"," +
//                "      \"userId\": \"10019\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"spring\"," +
//                "      \"userId\": \"10026\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"starTest\"," +
//                "      \"userId\": \"651342\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"test\"," +
//                "      \"userId\": \"10038\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"tt\"," +
//                "      \"userId\": \"651338\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"veigar\"," +
//                "      \"userId\": \"10010\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"vito\"," +
//                "      \"userId\": \"10030\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"wisk\"," +
//                "      \"userId\": \"10029\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"zdadmin\"," +
//                "      \"userId\": \"10021\"" +
//                "    }," +
//                "    {" +
//                "      \"userCode\": \"zdtest001\"," +
//                "      \"userId\": \"10022\"" +
//                "    }," +
//                "    {" +
//                "      \"shift\": \"2\"," +
//                "      \"sportId\": 2," +
//                "      \"userCode\": \"eisha\"," +
//                "      \"userId\": \"10013\"" +
//                "    }," +
//                "    {" +
//                "      \"shift\": \"3\"," +
//                "      \"sportId\": 3," +
//                "      \"userCode\": \"enzo\"," +
//                "      \"userId\": \"651344\"" +
//                "    }," +
//                "    {" +
//                "      \"shift\": \"2\"," +
//                "      \"sportId\": 1," +
//                "      \"userCode\": \"fktest\"," +
//                "      \"userId\": \"10017\"" +
//                "    }," +
//                "    {" +
//                "      \"shift\": \"4\"," +
//                "      \"sportId\": 2," +
//                "      \"userCode\": \"gunner\"," +
//                "      \"userId\": \"10037\"" +
//                "    }," +
//                "    {" +
//                "      \"shift\": \"2\"," +
//                "      \"sportId\": 1," +
//                "      \"userCode\": \"jinnian\"," +
//                "      \"userId\": \"10036\"" +
//                "    }," +
//                "    {" +
//                "      \"shift\": \"3\"," +
//                "      \"sportId\": 1," +
//                "      \"userCode\": \"joken\"," +
//                "      \"userId\": \"10039\"" +
//                "    }" +
//                "  ]";
//
//        List<SportDutyTraderPO> list = JSONObject.parseArray(test, SportDutyTraderPO.class);
//        sportDutyTraderService.syncTrader(list);
//    }
//
//    @Test
//    public void testMerchantAlert(){
//        String test = "{\"usedAmountPercent\":1.0000,\"merchantId\":2,\"usedAmount\":100.00,\"dateExpect\":\"2021-03-06\",\"timestamp\":1615011949725}";
//        MerchantAmountAlertVO merchantAmountAlertVO = JSONObject.parseObject(test, MerchantAmountAlertVO.class);
//        merchantNewsService.addMerchantAlertFromUsedAmount(merchantAmountAlertVO);
//    }
//
//    @Test
//    public void testTime(){
//        Date date = DateUtils.dateStrToDate("2021-03-05 16:57:48");
//        System.out.println(date.getTime());
//    }
//}
