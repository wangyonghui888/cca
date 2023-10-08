package com.panda.sport.merchant.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.panda.sport.merchant.common.vo.DomainVo;
import com.panda.sport.merchant.manage.ManageApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ManageApplication.class)
@ComponentScan(basePackages = {"com.panda.*"}, excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.panda.sport.merchant.manage.*"))
@AutoConfigureMockMvc
public class ManageControllerTest {
    private final HttpHeaders httpHeaders = new HttpHeaders();
    @Autowired
    Gson gson;
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void init() {
        httpHeaders.add("user-id", "437");
        httpHeaders.add("app-id", "10008");
    }

    /**
     * 商户管理/商户查询
     *
     * @throws Exception
     */
    @Test
    public void getMerchantList() throws Exception {
        String url = "/manage/merchant/list?agentLevel=0&pageNum=1&pageSize=20&merchantTag=0&transferMode=1&isApp=0&rnd_str_st=1691824993705";
        String responseString = mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("".getBytes(StandardCharsets.UTF_8))
                        .headers(this.httpHeaders).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
        log.info("result={}", responseString);
    }

    /**
     * 彩票获取分组域名
     *
     * @throws Exception
     */
    @Test
    public void getDJMerchantGroup() throws Exception {
        String url = "/manage/merchantDomainGroup/dj/getMerchantGroup?rnd_str_st=1691824993705";
        new JSONObject() {{
            put("", "");
        }};
        DomainVo domainVo = new DomainVo();
        domainVo.setMerchantGroupCode(1);
        String requestBody = gson.toJson(domainVo);
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.getBytes(StandardCharsets.UTF_8))
                        .headers(this.httpHeaders).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
        log.info("result={}", responseString);
    }

    /**
     * 电竞获取分组域名
     *
     * @throws Exception
     */
    @Test
    public void getCPMerchantGroup() throws Exception {
        String url = "/manage/merchantDomainGroup/cp/getMerchantGroup?rnd_str_st=1691824993705";
        DomainVo domainVo = new DomainVo();
        domainVo.setMerchantGroupCode(2);
        String requestBody = gson.toJson(domainVo);
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody.getBytes(StandardCharsets.UTF_8))
                        .headers(this.httpHeaders).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().encoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn()
                .getResponse()
                .getContentAsString();
        log.info("result={}", responseString);
    }
}
