package com.panda.sport.admin.utils;

import com.panda.sport.merchant.common.utils.CsvUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author baylee
 * @version 1.0
 * @date 02/22/22 18:22:45
 */
@Slf4j
public class ExportUtils {

    public static void browserDownload(String fileName, LinkedHashMap<String, String> headers,
                                       List<LinkedHashMap<String, Object>> exportData, HttpServletResponse response) {
        ServletOutputStream outputStream = null;
        try {
            byte[] exportCSV = CsvUtil.exportCSV(headers, exportData);
            if (exportCSV == null) {
                log.error("export data is empty,file name:{}", fileName);
                return;
            }
            outputStream = response.getOutputStream();
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);
            outputStream.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            FileCopyUtils.copy(exportCSV, outputStream);
        } catch (IOException e) {
            log.error("export error,file name:{},cause:{}", fileName, e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }
}
