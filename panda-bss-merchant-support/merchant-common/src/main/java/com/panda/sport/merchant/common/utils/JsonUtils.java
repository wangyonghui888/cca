package com.panda.sport.merchant.common.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author :  dorich
 * @project Name :  first
 * @package Name :  com.work.first.utils
 * @description :  TODO
 * @create Date:  2019-07-13 10:40
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
public class JsonUtils {
    /**
     * ObjectMapper是JSON操作的核心，Jackson的所有JSON操作都是在ObjectMapper中实现。
     * ObjectMapper有多个JSON序列化的方法，可以把JSON字符串保存File、OutputStream等不同的介质中。
     * writeValue(File arg0, Object arg1) 把arg1转成json序列，并保存到arg0文件中。
     * writeValue(OutputStream arg0, Object arg1) 把arg1转成json序列，并保存到arg0输出流中。
     * writeValueAsBytes(Object arg0)把arg0转成json序列，并把结果输出成字节数组。
     * writeValueAsString(Object arg0)把arg0转成json序列，并把结果输出成字符串
     **/
    public static ObjectMapper objectMapper = new ObjectMapper();

    static {
        /**  反序列化配置 **/
        /**  浮点数转换为 BigDecimal **/
        objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        objectMapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true);
        objectMapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true);

        /**  序列化配置 **/
        /** 把日期转为时间戳的功能暂时禁用，等上线时再调整 **/
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);

    }
    /**
     * @return T
     * @Description 把json字符串转换为Object对象
     * @Param json：需要转换的json字符串
     * type：待转换的对象类型
     * @Author dorich
     * @Date 2019/7/13
     **/
    public static <T> T jsonToObject(String json, TypeReference<T> type) {
        if (json != null && !json.trim().isEmpty()) {
            try {
                return objectMapper.readValue(json, type);
            } catch (JsonParseException e) {
                log.error("JsonParseException:" + json);
            } catch (Exception e) {
                log.error("data:" + json + ";;" + e.getMessage());
            }
        }
        return null;
    }

    /**
     * @Description 把object 转为string
     * @Param [o]
     * @Author dorich
     * @Date 2019/7/13
     * @return java.lang.String
     **/
    public static String objectToJson(Object o) {
        if (null != o && !(o instanceof Collection)) {
            try {
                return objectMapper.writeValueAsString(o);
            } catch (JsonProcessingException e) {
                log.info(o.toString());
            }
        }
        return null;
    }

    /**
     * @return T
     * @Description 把json字符串转换为Object对象
     * @Param json：需要转换的json字符串
     * type：待转换的对象类型
     * @Author dorich
     * @Date 2019/7/13
     **/
    public static <T> T jsonToObject(String json, Class<T> type) {
        if (json != null && !json.trim().isEmpty()) {
            try {
                return objectMapper.readValue(json, type);
            } catch (JsonParseException e) {
                log.error("JsonParseException:" + json);
            } catch (Exception e) {
                log.error("data:" + json + ";;" + e.getMessage());
            }
        }
        return null;
    }
    /**
     * @Description   把list 转换为 json 字符串
     * @Param [list]
     * @Author  dorich
     * @Date   2019/7/13
     * @return java.lang.String
     **/
    public static String listToJson(List list) {
        if (null != list && list.size() > 0) {
            try {
                return objectMapper.writeValueAsString(list);
            }  catch (Exception e) {
                log.error("data:" + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @return T
     * @Description 把json数据转换为 list的方法，
     * @Param json: 需要转为list的json数据
     * type: list包含的对象类型
     * @Author dorich
     * @Date 2019/7/13
     **/
    public static <T> List<T> jsonToObjectList(String json, Class<T> type) {
        if (json != null && !json.trim().isEmpty()) {
            try {
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, type);
                return (List<T>)objectMapper.readValue(json, javaType);
            } catch (JsonParseException e) {
                log.error("JsonParseException:" + json);
                e.printStackTrace();
            } catch (Exception e) {
                log.error("data:" + json + ";;" + e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @return java.util.Map
     * @Description 把json字符串转换为map对象
     * @Param json：待转换的json字符串
     * @Author dorich
     * @Date 2019/7/13
     **/
    public static Map jsonToObjectList(String json) {
        if (json != null && !json.trim().isEmpty()) {
            try {
                return objectMapper.readValue(json, Map.class);
            } catch (JsonParseException e) {
                log.error("JsonParseException:" + json);
                e.printStackTrace();
            } catch (JsonMappingException e) {
                log.error("JsonMappingException:" + json);
                e.printStackTrace();
            } catch (Exception e) {
                log.error("data:" + json + ";;" + e.getMessage());
            }
        }
        return null;
    }

    /**
     * @return java.util.Map
     * @Description 把json字符串转换为map对象
     * @Param json：待转换的json字符串
     * @Author dorich
     * @Date 2019/7/13
     **/
    public static Map jsonToMap(String json) {
        if (!StringUtil.isBlankOrNull(json)) {
            try {
                return objectMapper.readValue(json, Map.class);
            } catch (Exception e) {
                log.error("data:" + json + ";;" + e.getMessage());
            }
        }
        return null;
    }
}