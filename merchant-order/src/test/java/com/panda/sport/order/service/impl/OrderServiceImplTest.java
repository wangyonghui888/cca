package com.panda.sport.order.service.impl;

import com.panda.sport.backup83.mapper.Backup83OrderMixMapper;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.LanguageEnum;
import com.panda.sport.merchant.common.vo.BetOrderVO;
import com.panda.sport.order.OrderApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static net.sf.jsqlparser.parser.feature.Feature.orderBy;
import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderApplication.class)
@Slf4j
class OrderServiceImplTest {

    @Test
    void test() {
        String playName = "全场让球比分 1：0";
        try {
            //正则表达式，用于匹配非数字串，+号用于匹配出多个非数字串
            String regEx = "[^0-9]+";
            Pattern pattern = compile(regEx);
            Matcher m = pattern.matcher(playName);
            //将输入的字符串中非数字部分用空格取代并存入一个字符串
            String string = m.replaceAll(" ").trim();
            //以空格为分割符在讲数字存入一个字符串数组中
            String[] str = string.split(" ");
            System.out.println(str[0]);
        } catch (Exception e) {
            log.error("从字符串提取数字异常!", e);
        }
    }



}