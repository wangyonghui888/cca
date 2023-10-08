package com.panda.sport.admin;

import com.panda.sport.admin.utils.JwtTokenUtil;
import com.panda.sport.admin.utils.RedisUtil;
import com.panda.sport.merchant.common.po.bss.MerchantPO;
import com.panda.sport.merchant.common.vo.merchant.MerchantFileMsgVo;
import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @auth: YK
 * @Description:TODO
 * @Date:2020/5/10 11:44
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestApplication {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    public void ee() {

        RedisUtil.set("ee","777777777");
        System.out.println("eeee");

        List<MerchantPO> merchantPOList = new ArrayList<>();
        MerchantPO merchantPO = new MerchantPO();
        merchantPO.setId("1250710787228569600");
        merchantPO.setMerchantCode("225226");

        MerchantPO merchantPO1 = new MerchantPO();
        merchantPO1.setId("1250710905600217088");
        merchantPO1.setMerchantCode("606982");
        merchantPO1.setParentId("1250710787228569600");

        merchantPOList.add(merchantPO);
        merchantPOList.add(merchantPO1);

        //List<String> parentIdList = merchantPOList.stream().filter(m->m.getParentId() != null).map(MerchantPO::getParentId).collect(Collectors.toList());

        List<String> parentIdList = merchantPOList.stream().filter(m->m.getParentId() != null).map(m->m.getParentId()).collect(Collectors.toList());

        System.out.println(parentIdList);
    }

    @Test
    public void eee() {


        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTU5MDYwMjM3NCwiaWF0IjoxNTkwNTk1MTc0fQ.f22KXQQQ1U_Zk9YWgFPNA0jd_3kOXj4TURf2xnnIvSe5dCq0u6_SRDZNEPPqpUA82dx8jS2-di8VQdyMv1YtdQ";
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
        System.out.println(claims);
    }

    @Test public void testMQ(){
        String channel1 = "MERCHANT_FILE_TOPIC";
        MerchantFileMsgVo user = new MerchantFileMsgVo();
        user.setId(45645654L);
        redisTemplate.convertAndSend(channel1,user);
    }
}
