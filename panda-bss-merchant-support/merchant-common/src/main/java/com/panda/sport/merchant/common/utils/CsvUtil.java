package com.panda.sport.merchant.common.utils;


import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Slf4j
public class CsvUtil {


    /**
     * 导出csv文件
     *
     * @param headers    内容标题
     *                   注意：headers类型是LinkedHashMap，保证遍历输出顺序和添加顺序一致。
     *                   而HashMap的话不保证添加数据的顺序和遍历出来的数据顺序一致，这样就出现
     *                   数据的标题不搭的情况的
     * @param exportData 要导出的数据集合
     * @return
     */
    public static byte[] exportCSV(LinkedHashMap<String, String> headers, List<LinkedHashMap<String, Object>> exportData) {
        ByteArrayOutputStream baos = null;
        BufferedWriter buffCvsWriter = null;
        try {
            // 编码gb2312，处理excel打开csv的时候会出现的标题中文乱码
            baos = new ByteArrayOutputStream();
            buffCvsWriter = new BufferedWriter(new OutputStreamWriter(baos, "UTF-8"));
            buffCvsWriter.write(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));
            // 写入cvs文件的头部
            Map.Entry propertyEntry;
            for (Iterator<Map.Entry<String, String>> propertyIterator = headers.entrySet().iterator(); propertyIterator.hasNext(); ) {
                propertyEntry = propertyIterator.next();
                buffCvsWriter.write(propertyEntry.getValue().toString());
                if (propertyIterator.hasNext()) {
                    buffCvsWriter.write(",");
                }
            }
            buffCvsWriter.newLine();
            // 写入文件内容
            LinkedHashMap row;
            for (Iterator<LinkedHashMap<String, Object>> iterator = exportData.iterator(); iterator.hasNext(); ) {
                row = iterator.next();
                for (Iterator<Map.Entry> propertyIterator = row.entrySet().iterator(); propertyIterator.hasNext(); ) {
                    propertyEntry = propertyIterator.next();
                    if (propertyEntry != null && propertyEntry.getValue() != null) {
                        buffCvsWriter.write("\"" + propertyEntry.getValue().toString() + "\"");
                    } else {
                        buffCvsWriter.write("\"\"");
                    }
                    if (propertyIterator.hasNext()) {
                        buffCvsWriter.write(",");
                    }
                }
                if (iterator.hasNext()) {
                    buffCvsWriter.newLine();
                }
            }
            // 记得刷新缓冲区，不然数可能会不全的，当然close的话也会flush的，不加也没问题
            buffCvsWriter.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("导出异常!", e);
            return null;
        } finally {
            // 释放资源
            if (buffCvsWriter != null) {
                try {
                    buffCvsWriter.close();
                } catch (IOException e) {
                    log.error("流关闭异常1!!", e);
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    log.error("流关闭异常2!!", e);
                }
            }
        }
    }

    /**
     * 导出csv文件
     *
     * @param headers    内容标题
     *                   注意：headers类型是LinkedHashMap，保证遍历输出顺序和添加顺序一致。
     *                   而HashMap的话不保证添加数据的顺序和遍历出来的数据顺序一致，这样就出现
     *                   数据的标题不搭的情况的
     * @param exportData 要导出的数据集合
     * @return
     */
    public static byte[] exportStringCSV(LinkedHashMap<String, String> headers, List<LinkedHashMap<String, String>> exportData) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedWriter buffCvsWriter = null;

        try {
            // 编码gb2312，处理excel打开csv的时候会出现的标题中文乱码
            buffCvsWriter = new BufferedWriter(new OutputStreamWriter(baos, "UTF-8"));
            buffCvsWriter.write(new String(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}));
            // 写入cvs文件的头部
            Map.Entry propertyEntry = null;
            for (Iterator<Map.Entry<String, String>> propertyIterator = headers.entrySet().iterator(); propertyIterator.hasNext(); ) {
                propertyEntry = propertyIterator.next();
                String str = "";
                if (propertyEntry != null && propertyEntry.getValue() != null) {
                    str = propertyEntry.getValue().toString();
                    str = str.replaceAll("\"", "\"\"");
                    if (str.indexOf(",") >= 0) {
                        str = "\"" + str + "\"";
                    }
                }
                buffCvsWriter.write("\"" + str + "\"");
                if (propertyIterator.hasNext()) {
                    buffCvsWriter.write(",");
                }
            }
            buffCvsWriter.newLine();
            // 写入文件内容
            LinkedHashMap row = null;
            for (Iterator<LinkedHashMap<String, String>> iterator = exportData.iterator(); iterator.hasNext(); ) {
                row = iterator.next();
                for (Iterator<Map.Entry> propertyIterator = row.entrySet().iterator(); propertyIterator.hasNext(); ) {
                    propertyEntry = propertyIterator.next();
                    buffCvsWriter.write("\"" + propertyEntry.getValue() + "\"");
                    if (propertyIterator.hasNext()) {
                        buffCvsWriter.write(",");
                    }
                }
                if (iterator.hasNext()) {
                    buffCvsWriter.newLine();
                }
            }
            // 记得刷新缓冲区，不然数可能会不全的，当然close的话也会flush的，不加也没问题
            buffCvsWriter.flush();
        } catch (IOException e) {

        } finally {
            // 释放资源
            if (buffCvsWriter != null) {
                try {
                    buffCvsWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return baos.toByteArray();
    }

    /**
     * 解析csv文件，VIP用户数据
     *
     * @return
     */
    public static List<Long> getCsvToList(MultipartFile vipCsv) {
        ArrayList<Long> result = Lists.newArrayList();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(vipCsv.getInputStream(), "utf-8"));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String[] split = line.split(",");
                result.add(Long.parseLong(split[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 释放资源
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 解析excel文件，VIP用户数据
     *
     * @return
     */
    public static List<Long> getExcelToList(MultipartFile vipCsv) {
        ArrayList<Long> result = Lists.newArrayList();
        try {
            ExcelReader reader = ExcelUtil.getReader(vipCsv.getInputStream(),0,true);
            List<List<Object>> cellls = reader.read();
            for(List<Object> ls : cellls){
                for(Object cell : ls){
                    result.add((Long)cell);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
