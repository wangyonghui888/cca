package com.panda.multiterminalinteractivecenter.base;

/**
 * @author lary
 * @version 1.0.0
 * @ClassName Constant.java
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.multiterminalinteractivecenter.base
 * @Description TODO
 * @createTime 2022/03/19
 */
public class Constant {
    /**
     * 英文
     */
    public static String LANGUAGE_ENGLISH = "en";
    public static String LANGUAGE_CHINESE = "zs";

    /**
     * 操作类型(1,设置维护,2,开始维护,3,延长30分钟维护,4,结束维护，5，发送维护公告，6，弹出用户提醒,7，踢用户)
     */
    public enum OperationType {
        /**
         * 设置维护
         */
        SETMAINTENANCE(1),
        /**
         * 开始维护
         */
        STARTMAINTENANCE(2),
        /**
         * 延长30分钟维护
         */
        ADD30MINUTE(3),
        /**
         * 结束维护
         */
        ENDMAINTENANCE(4),
        /**
         * 发送维护公告
         */
        SENDNOTICE(5),
        /**
         * 弹出用户提醒
         */
        ISREMIND(6),
        /**
         * 踢用户
         */
        KICKUSER(7);

        private int value;

        OperationType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 维护状态(默认无维护0，未开始1，维护中2，已结束3)
     */
    public enum MaintenanceStatus {
        /**
         * 无维护
         */
        NOMAINTENANCE(0),
        /**
         * 未开始
         */
        NOSTARTING(1),
        /**
         * 维护中
         */
        STARTING(2),
        /**
         * 已结束3
         */
        THEEND(3);

        private int value;

        MaintenanceStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum DataCodeType {
        /**
         * 体育系统
         */
        TJ("ty","体育系统"),
        /**
         * 彩票系统
         */
        CP("cp","彩票系统"),
        /**
         * 电竞系统
         */
        DJ("dj","电竞系统");

        private String key;
        private String value;

        DataCodeType(String key,String value) {
            this.key = key;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String getKey() {
            return key;
        }
        //根据key获取value的值
        public static String getValueByKey(String key) {
            for (DataCodeType s : DataCodeType.values()) {
                if (s.getKey().equals(key)) {
                    return s.getValue();
                }
            }
            return "";
        }
    }
}
