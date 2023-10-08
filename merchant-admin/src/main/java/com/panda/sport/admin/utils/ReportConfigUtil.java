package com.panda.sport.admin.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YK
 * @Description: 导出工具类
 * @date 2020/2/29 18:01
 */
public class ReportConfigUtil {


    public static Map<String, String> REPORT_TITILES = new HashMap<>();

    //报表标题自动配置类
    static {
        REPORT_TITILES.put("merchant-report", "序号,商户名称,商户类型,币种,投注金额,投注笔数,派彩金额,盈利金额,盈利率,投注用户数,新增用户数,注册用户数,投注率");
    }
}
