package com.panda.sport.order.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.panda.sport.merchant.common.report.MatchBetInfoReq;
import com.panda.sport.merchant.common.utils.HttpConnectionPool;
import com.panda.sport.merchant.common.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 赛事数据查询大数据接口
 * 接口文档地址  http://172.18.178.39:8088/panda/swagger-ui.html#/%E8%B5%9B%E4%BA%8B%E6%8A%95%E6%B3%A8%E7%BB%9F%E8%AE%A1
 * 因前端跨域 所以后端调用转发，需header 加上appid 参数
 */
@RestController
@RequestMapping("/order/reportMatch")
@Slf4j
public class MatchReportController {

    @Value("${report.match.appid:f907e4ca9d1a11ec92e2000c29816e91}")
    private String appid;

    @Value("${report.match.url:http://test-panda-bigdata-report-web.sportxxxkd1.com/api/panda-report}")
    private String url;

    /**
     * 赛事投注统计报表
     * @param request1
     * @param vo
     * @return
     */
    @PostMapping(value = "/reportDateList")
    public Object reportDateList(HttpServletRequest request1, @RequestBody MatchBetInfoReq vo) {
        log.info("/report/match/reportDateList:,VO=" + vo);
        vo.setMerchantIdsScope(1);
        JSONObject jsonObject = (JSONObject) JSON.toJSON(vo);
        log.info("/report/match/reportDateList:,VO=" + vo);
        try {
            HttpEntity request = new HttpEntity<>(jsonObject, getHeader());
            JSONObject response =  HttpConnectionPool.restTemplate.postForObject(url.concat("/matchBetReport/reportDateList"), request, JSONObject.class);
            return response;
        }catch (Exception e){
            log.error("reportDateList error:",e);

        }
        return Response.returnFail("system error");
    }


    /**
     * 玩法明细信息
     * @param request1
     * @param vo   赛事ID  联赛ID  开赛时间 不能为空
     * @return
     */
    @PostMapping(value = "/marketBetDetailList")
    public Object marketBetDetailList(HttpServletRequest request1, @RequestBody MatchBetInfoReq vo) {
        log.info("/report/match/marketBetDetailList:,VO=" + vo);
        vo.setMerchantIdsScope(1);
        JSONObject jsonObject = (JSONObject) JSON.toJSON(vo);
        try {
            HttpEntity request = new HttpEntity<>(jsonObject, getHeader());
            JSONObject response =  HttpConnectionPool.restTemplate.postForObject(url.concat("/matchBetReport/marketBetDetailList"), request, JSONObject.class);
            return response;
        }catch (Exception e){
            log.error("marketBetDetailList error:",e);

        }
        return Response.returnFail("system error");
    }


    /**
     * 多币种查询
     * @param request1
     * @param vo
     * @return
     */
    @PostMapping(value = "/currencyDataList")
    public Object currencyDataList(HttpServletRequest request1, @RequestBody MatchBetInfoReq vo) {
        log.info("/report/match/currencyDataList:,VO=" + vo);
        vo.setMerchantIdsScope(1);
        JSONObject jsonObject = (JSONObject) JSON.toJSON(vo);
        try {
            HttpEntity request = new HttpEntity<>(jsonObject, getHeader());
            JSONObject response =  HttpConnectionPool.restTemplate.postForObject(url.concat("/matchBetReport/currencyDataList"), request, JSONObject.class);
            return response;
        }catch (Exception e){
            log.error("currencyDataList error:",e);

        }
        return Response.returnFail("system error");
    }



    /**
     * 多币种汇总
     * @param request1
     * @param vo
     * @return
     */
    @PostMapping(value = "/currencySum")
    public Object currencySum(HttpServletRequest request1, @RequestBody MatchBetInfoReq vo) {
        log.info("/report/match/currencySum:,VO=" + vo);
        vo.setMerchantIdsScope(1);
        JSONObject jsonObject = (JSONObject) JSON.toJSON(vo);
        try {
            HttpEntity request = new HttpEntity<>(jsonObject, getHeader());
            JSONObject response =  HttpConnectionPool.restTemplate.postForObject(url.concat("/matchBetReport/currencySum"), request, JSONObject.class);
            return response;
        }catch (Exception e){
            log.error("currencySum error:",e);

        }
        return Response.returnFail("system error");
    }


    /**
     * 赛事投注统计报表导出
     * @param request1
     * @param vo
     * @return
     */
    @PostMapping(value = "/matchBetReportDataExport")
    public void matchBetReportDataExport(HttpServletResponse response, HttpServletRequest request1, @RequestBody MatchBetInfoReq vo) {
        log.info("/report/match/matchBetReportDataExport:,VO=" + vo);
        vo.setMerchantIdsScope(1);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse =null;
        JSONObject jsonObject = (JSONObject) JSON.toJSON(vo);
        try {
            Map<String,String> requestMap = jsonObject.toJavaObject(Map.class);
            String urls =url.concat("/matchBetReport/matchBetReportDataExport");
            URIBuilder uriBuilder = new URIBuilder(urls);
            List<NameValuePair> list = new LinkedList<>();
            for (Map.Entry<String, String> map : requestMap.entrySet()) {
                if(map.getKey()!=null && null!=map.getValue()) {
                    BasicNameValuePair pair = new BasicNameValuePair(map.getKey(), String.valueOf(map.getValue()));
                    list.add(pair);
                }
            }
            uriBuilder.setParameters(list);
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.addHeader("appid",appid);
            httpResponse =  httpClient.execute(httpGet);
            // 设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
            Header[] httpHead =httpResponse.getHeaders("Content-Disposition");
            response.addHeader("Content-Disposition", String.valueOf(httpHead[0]));
            InputStream is = httpResponse.getEntity().getContent();
            OutputStream outputStream = response.getOutputStream();
            IOUtils.copy(is,outputStream);
            outputStream.flush();
            is.close();
            outputStream.close();
        } catch (URISyntaxException e) {
            log.error("URISyntaxException",e);
        } catch (ClientProtocolException e) {
            log.error("ClientProtocolException",e);
        } catch (Exception e) {
            log.error("Exception",e);
        } finally {
            if(response!=null){
                try {
                    httpResponse.close();
                    httpClient.close();
                } catch (IOException e) {
                    log.error("IOException",e);
                }

            }
        }
    }


    /**
     * 玩法投注统计报表导出
     * @param request1
     * @param vo
     * @return
     */
    @PostMapping(value = "/marketBetReportDataExport")
    public void marketBetReportDataExport(HttpServletResponse response, HttpServletRequest request1, @RequestBody MatchBetInfoReq vo) {
        log.info("/report/match/marketBetReportDataExport:,VO=" + vo);
        vo.setMerchantIdsScope(1);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse =null;
        JSONObject jsonObject = (JSONObject) JSON.toJSON(vo);
        try {
            Map<String,String> requestMap = jsonObject.toJavaObject(Map.class);
            String urls =url.concat("/matchBetReport/marketBetReportDataExport");
            URIBuilder uriBuilder = new URIBuilder(urls);
            List<NameValuePair> list = new LinkedList<>();
            for (Map.Entry<String, String> map : requestMap.entrySet()) {
                if(map.getKey()!=null && null!=map.getValue()) {
                    BasicNameValuePair pair = new BasicNameValuePair(map.getKey(), String.valueOf(map.getValue()));
                    list.add(pair);
                }
            }
            uriBuilder.setParameters(list);
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.addHeader("appid",appid);
            httpResponse =  httpClient.execute(httpGet);
            // 设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
            Header[] httpHead =httpResponse.getHeaders("Content-Disposition");
            response.addHeader("Content-Disposition", String.valueOf(httpHead[0]));
            InputStream is = httpResponse.getEntity().getContent();
            OutputStream outputStream = response.getOutputStream();
            IOUtils.copy(is,outputStream);
            outputStream.flush();
            is.close();
            outputStream.close();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
           log.error("Exception",e);
        } finally {
            if(response!=null){
                try {
                    httpResponse.close();
                    httpClient.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }

    /**
     * 赛事投注统计联赛名称列表
     * @param request1
     * @param vo
     * @return
     */
    @PostMapping(value = "/tournamentDataList")
    public Object tournamentDataList(HttpServletRequest request1, @RequestBody MatchBetInfoReq vo) {
        log.info("/report/match/tournamentDataList:,VO=" + vo);
        vo.setMerchantIdsScope(1);
        JSONObject jsonObject = (JSONObject) JSON.toJSON(vo);
        try {
            HttpEntity request = new HttpEntity<>(jsonObject, getHeader());
            JSONObject response =  HttpConnectionPool.restTemplate.postForObject(url.concat("/matchBetReport/tournamentDataList"), request, JSONObject.class);
            return response;
        }catch (Exception e){
            log.error("tournamentDataList error:",e);

        }
        return Response.returnFail("system error");
    }


    private  HttpHeaders getHeader() {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add("appid", appid);
        return headers;
    }
}
