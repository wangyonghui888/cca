package com.panda.sport.merchant.common.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpMethod;

/**
 * @author :  hooli
 * @Project Name :
 * @Package Name :
 * @Description : 发送http请求辅助类（支持几乎所有请求的发送）
 * @Date: 2019-08-29 下午2:14:34
 */
public class HttpUtils {
    public static final String JSON_CONTENT_TYPE = "application/json";
    public static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static Pattern PATTERN = Pattern.compile("(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?");

    /**
     * get请求
     *
     * @param scc 自定义的cookie名，若不网站不要带cookie，传null即可
     * @param uri URL
     * @return
     */
    public static String sendWebRequestByGet(String scc, String uri) {
        return sendWebRequestByGet(scc, uri, null);
    }

    /**
     * get请求
     *
     * @param scc               自定义的cookie名，若不网站不要带cookie，传null即可
     * @param uri               URL
     * @param forceRespEncoding 返回值编码 默认UTF-8
     * @return
     */
    public static String sendWebRequestByGet(String scc, String uri, String forceRespEncoding) {
        BasicCookieStore bcs = getBasicCookieStore(scc);
        return sendWebRequest(uri, HttpMethod.GET, null, null, forceRespEncoding, null, bcs);
    }

    /**
     * 发送JSON请求
     *
     * @param scc     自定义的cookie名，若不网站不要带cookie，传null即可
     * @param uri     URL
     * @param jsonStr json字符串
     * @return
     */
    public static String sendWebRequestByPostJsonStr(String scc, String uri, String jsonStr) {
        return sendWebRequestByPost(scc, uri, jsonStr, null, JSON_CONTENT_TYPE);
    }

    /**
     * 发送JSON请求
     *
     * @param scc     自定义的cookie名，若不网站不要带cookie，传null即可
     * @param uri     URL
     * @param jsonStr json字符串
     * @param timeout 超时时间(ms)
     * @return
     */
    public static String sendWebRequestByPostJsonStr(String scc, String uri, String jsonStr, Integer timeout) {
        BasicCookieStore bcs = getBasicCookieStore(scc);
        return sendWebRequest(uri, HttpMethod.POST, null, jsonStr, null, null, timeout, bcs);
    }

    public static String sendWebRequestByPost(String scc, String uri, String contents, String reqEncoding, String contentType) {
        BasicCookieStore bcs = getBasicCookieStore(scc);
        Map<String, String> headers = new HashMap<>();
        if (contentType != null) {
            headers.put("Content-Type", contentType);
        }
        return sendWebRequest(uri, HttpMethod.POST, headers, contents, reqEncoding, "UTF-8", null, bcs);
    }

    public static String sendWebRequestByForm(String scc, String uri, Map<String, String> params) {
        return sendWebRequestByForm(scc, uri, null, params, null);
    }

    public static String sendWebRequestByForm(String scc, String uri, Map<String, String> headers, Map<String, String> params, String reqEncoding) {
        BasicCookieStore bcs = getBasicCookieStore(scc);
        boolean hasContentType = false;
        Map<String, String> kvpHeaders = new HashMap<>();
        if (headers != null) {
            for (String key : headers.keySet()) {
                if ("Content-Type".equalsIgnoreCase(key)) {
                    hasContentType = true;
                }
                kvpHeaders.put(key, headers.get(key));
            }
        }
        if (!hasContentType) {
            kvpHeaders.put("Content-Type", DEFAULT_CONTENT_TYPE);
        }
        return sendWebRequest(uri, HttpMethod.POST, kvpHeaders, formToUrlEncodedString(params, reqEncoding), reqEncoding, null, null, bcs);

    }

    public static String sendWebRequest(String uri, HttpMethod httpMethod, Map<String, String> headers, String contents, String reqEncoding, String forceRespEncoding, Integer timeout, BasicCookieStore cookieStore) {
        if (contents == null) {
            contents = "";
        }
        if (isBlankOrEmpty(reqEncoding)) {
            reqEncoding = "UTF-8";
        }
        return sendWebRequest(uri, httpMethod, headers, contents.getBytes(Charset.forName(reqEncoding)), forceRespEncoding, timeout, cookieStore);
    }

    public static String sendWebRequest(String uri, HttpMethod httpMethod, Map<String, String> headers, byte[] contents, String forceRespEncoding, Integer timeout, BasicCookieStore cookieStore) {
        return sendWebRequest(uri, httpMethod, headers, contents, forceRespEncoding, timeout, cookieStore, null, null);
    }

    public static String sendWebRequest(String uri, HttpMethod httpMethod, Map<String, String> headers, byte[] contents, String forceRespEncoding, Integer timeout, BasicCookieStore cookieStore, byte[] certData, String certPassword) {
        if (timeout == null || timeout < 1000) {
            timeout = 10000;
        } else if (timeout > 100000) {
            timeout = 100000;
        }
        if (cookieStore == null) {
            cookieStore = new BasicCookieStore();
        }

        try {
            RequestConfig defaultRequestConfig = RequestConfig.custom()
                    .setSocketTimeout(timeout)
                    .setConnectTimeout(timeout)
                    .setConnectionRequestTimeout(timeout)
                    .build();
            CloseableHttpClient httpclient = null;
            try {
                if (uri.startsWith("https://")) {
                    SSLContextBuilder sslContextBuilder = SSLContexts.custom();
                    if (certData != null && certPassword != null) {
                        KeyStore keyStore = KeyStore.getInstance("PKCS12");
                        try (InputStream inputStream = new ByteArrayInputStream(certData)) {
                            keyStore.load(inputStream, certPassword.toCharArray());
                        }
                        sslContextBuilder.loadKeyMaterial(keyStore, certPassword.toCharArray());
                    }
                    SSLContext ctx = sslContextBuilder.build();

                    SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(ctx, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

                    httpclient = HttpClients.custom().useSystemProperties()
                            .setDefaultCookieStore(cookieStore)
                            .setDefaultRequestConfig(defaultRequestConfig)
                            .setSSLSocketFactory(ssf)
                            .build();
                } else {
                    httpclient = HttpClients.custom().useSystemProperties()
                            .setDefaultCookieStore(cookieStore)
                            .setDefaultRequestConfig(defaultRequestConfig)
                            .build();
                }
                CloseableHttpResponse response = null;
                HttpRequestBase request = null;
                if (httpMethod == HttpMethod.GET) {
                    request = new HttpGet(uri);
                } else {
                    request = new HttpPost(uri);
                    ByteArrayEntity byteEntity = new ByteArrayEntity(contents);
                    ((HttpPost) request).setEntity(byteEntity);
                }
                if (headers != null) {
                    for (String key : headers.keySet()) {
                        request.setHeader(key, headers.get(key));
                    }
                }
                response = httpclient.execute(request);
                try {
                    if (response.getStatusLine().getStatusCode() != 200 && response.getStatusLine().getStatusCode() != 201) {
                        if (response.getStatusLine().getStatusCode() == 302) {
                            return response.getHeaders("Location")[0].getValue();
                        }
                        return null;
                    }
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        if (forceRespEncoding == null) {
                            return EntityUtils.toString(entity);
                        } else {
                            return byteArrayToString(EntityUtils.toByteArray(entity), forceRespEncoding);
                        }
                    }
                } finally {
                    if (response != null) {
                        response.close();
                    }
                }
            } finally {
                if (httpclient != null) {
                    httpclient.close();
                }
            }
        } catch (Exception e) {
            System.out.println("web request exception: " + uri);
            e.printStackTrace();
        }
        return null;
    }

    private static String formToUrlEncodedString(Map<String, String> params) {
        return formToUrlEncodedString(params, "UTF-8");
    }

    private static String formToUrlEncodedString(Map<String, String> params, String charset) {
        StringBuilder sb = new StringBuilder();
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                sb.append(key);
                sb.append('=');
                sb.append(urlEncode(params.get(key), charset));
                sb.append('&');
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    private static Map<String, String> urlEncodedStringToForm(String encoded) {
        return urlEncodedStringToForm(encoded, "UTF-8");
    }

    private static Map<String, String> urlEncodedStringToForm(String encoded, String charset) {
        if (isBlankOrEmpty(encoded)) {
            return null;
        }
        Map<String, String> retval = new HashMap<>();
        String[] params = encoded.split("&");
        for (String param : params) {
            String[] kvp = param.split("=");
            retval.put(kvp[0], kvp.length > 1 ? urlDecode(kvp[1], charset) : "");
        }
        return retval;
    }

    /**
     * URL编码
     *
     * @param origin 要编码的文本
     * @return
     */
    public static String urlEncode(String origin) {
        return urlEncode(origin, "UTF-8");
    }

    /**
     * URL编码
     *
     * @param origin  要编码的文本
     * @param charset 编码格式
     * @return
     */
    public static String urlEncode(String origin, String charset) {
        try {
            if (isBlankOrEmpty(charset)) {
                charset = "UTF-8";
            }
            return URLEncoder.encode(origin, charset);
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    /**
     * URL解码
     *
     * @param encoded 要解码的文本
     * @return
     */
    public static String urlDecode(String encoded) {
        return urlDecode(encoded, "UTF-8");
    }

    /**
     * URL解码
     *
     * @param encoded 要解码的文本
     * @param charset 解码格式
     * @return
     */
    public static String urlDecode(String encoded, String charset) {
        try {
            if (isBlankOrEmpty(charset)) {
                charset = "UTF-8";
            }
            return URLDecoder.decode(encoded, charset);
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    private static String addParamsToUrl(String url, Map<String, String> params, boolean isNeedUrlEncode, String urlEncoding) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(url.trim());
            if (url.contains("?")) {
                if (sb.charAt(sb.length() - 1) != '?' && sb.charAt(sb.length() - 1) != '&') {
                    sb.append('&');
                }
            } else {
                sb.append('?');
            }
            if (params != null && params.size() > 0) {
                for (String key : params.keySet()) {
                    sb.append(key);
                    sb.append('=');
                    if (isNeedUrlEncode) {
                        sb.append(URLEncoder.encode(params.get(key), urlEncoding));
                    } else {
                        sb.append(params.get(key));
                    }
                    sb.append('&');
                }
            }
            sb.deleteCharAt(sb.length() - 1);

            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String addParamsToContent(Map<String, String> params, boolean isNeedUrlEncode, String urlEncoding)
            throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                sb.append(key);
                sb.append('=');
                if (isNeedUrlEncode) {
                    sb.append(URLEncoder.encode(params.get(key), urlEncoding));
                } else {
                    sb.append(params.get(key));
                }
                sb.append('&');
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        return sb.toString();
    }

    private static String byteArrayToString(byte[] bytes, String encoding) {
        return new String(bytes, Charset.forName(encoding));
    }

    private static BasicCookieStore getBasicCookieStore(String scc) {
        BasicCookieStore bcs = null;
        if (scc != null) {
            bcs = CookieManager.getCookieStore(scc);
            if (bcs == null) {
                bcs = new BasicCookieStore();
                CookieManager.setCookieStore(scc, bcs);
            }
        }
        return bcs;
    }

    private static boolean isBlankOrEmpty(String s) {
        return s == null || "".equals(s.trim());
    }

    public static boolean isValidURL(String url) {
        if (isBlankOrEmpty(url)) {
            return false;
        }
        Matcher matcher = PATTERN.matcher(url);
        return matcher.matches();
    }

}
