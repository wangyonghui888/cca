package com.panda.sport.merchant.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @Author: Joken
 * @Project Name: panda-bss
 * @Package Name: com.panda.sports.bss.common.utils
 * @Description: 算数帮助类
 * @Date: 2019/10/21 10:50
 * @Version: 1.0
 */
@Slf4j
public class ArithmeticUtils {

    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 2;

    /**
     * @Author: Joken
     * @Description: 随机获取范围之内的最大最小值
     * @Date: 2019/10/23 11:56
     * @Param: [maxNumber, minNumber]
     * @Return:
     */
    public static Integer randomNumber(Integer minNumber, Integer maxNumber) {
        if (maxNumber == null || minNumber == null) {
            new ArithmeticException("随机获取最大最小数不能为空！");
        }
        if (maxNumber < 1 || minNumber < 1) {
            new ArithmeticException("随机获取最大最小数不能小于1！");
        }
        if (maxNumber < minNumber) {
            new ArithmeticException("随机获取最大值应该大于最小值！");
        }
        return (int) (Math.random() * (maxNumber - minNumber) + 1) + minNumber;
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * @param value 需要科学计算的数据
     * @param digit 保留的小数位
     * @return 功能：四舍六入五成双计算法
     */
    public static BigDecimal sciCal(double value, int digit) {
        BigDecimal result;
        try {
            double ratio = Math.pow(10, digit);
            double _num = value * ratio;
            double mod = _num % 1;
            double integer = Math.floor(_num);
            double returnNum;
            if (mod > 0.5) {
                returnNum = (integer + 1) / ratio;
            } else if (mod < 0.5) {
                returnNum = integer / ratio;
            } else {
                returnNum = (integer % 2 == 0 ? integer : integer + 1) / ratio;
            }
            BigDecimal bg = new BigDecimal(returnNum);
            result = bg.setScale(digit, BigDecimal.ROUND_HALF_UP);
        } catch (RuntimeException e) {
            throw e;
        }
        return result;
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        //把结果保留0位小数并返回
        BigDecimal result = b1.multiply(b2);
        //BigDecimal scaleResult = result.setScale(15, BigDecimal.ROUND_HALF_UP);
        return result.doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2, int digit) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        //把结果保留0位小数并返回
        BigDecimal result = b1.multiply(b2).setScale(digit, BigDecimal.ROUND_HALF_UP);
        return result.doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div1(double v1, double v2, int scale) {
        return div(v1, v2, scale).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时
     * 只保留两位小数
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double divRe2(double v1, double v2) {
        String result = new BigDecimal(v1).divide(new BigDecimal(v2), 4, BigDecimal.ROUND_DOWN).toString();
        if (result.length() - result.indexOf(".") > 3) {
            return Double.valueOf(result.substring(0, result.indexOf(".") + 3));
        } else {
            return Double.valueOf(result);
        }
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static BigDecimal div(Object v1, Object v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1, b2;
        if (v1 instanceof Double || v1 instanceof String) {
            b1 = new BigDecimal(v1 + "");
        } else if (v1 instanceof BigDecimal) {
            b1 = (BigDecimal) v1;
        } else {
            log.error("除数的原始类型：{}", v1.getClass().toString());
            b1 = new BigDecimal(1);
        }
        if (v2 instanceof Double || v2 instanceof String) {
            b2 = new BigDecimal(v2 + "");
        } else if (v2 instanceof BigDecimal) {
            b2 = (BigDecimal) v2;
        } else {
            log.error("被除数的原始类型：{}", v2.getClass().toString());
            b2 = new BigDecimal(1);
        }
        if (b1.equals(new BigDecimal(0))) {
            b1 = new BigDecimal(1);
        }
        if (b2.equals(new BigDecimal(0))) {
            b2 = new BigDecimal(1);
        }
        return b1.divide(b2, scale, BigDecimal.ROUND_FLOOR);
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v        需要四舍五入的数字
     * @param scale    小数点后保留几位
     * @param isHalfUp 是否四舍五入
     * @return 四舍五入后的结果
     */
    /*public static double isRoundHalfUp(double v, int scale, boolean isHalfUp) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal bg = new BigDecimal(v);
        if (isHalfUp) {
            return bg.setScale(scale, RoundingMode.UP).doubleValue();
        } else {
            String r1 = String.valueOf(v);
            if (scale > r1.length()) {
                return v;
            }
            if (r1.contains(".")) {
                int index1 = r1.indexOf(".") + 1 + scale;
                if (index1 < r1.length()) {
                    String r2 = r1.substring(0, index1);
                    return Double.valueOf(r2);
                } else {
                    return v;
                }
            } else {
                return v;
            }
        }
    }*/

    /**
     * @param result           赛果
     * @param orderAmountTotal 实际扣款金额
     * @param maxWinAmount     最高可赢
     * @return
     * @Author: butr
     * @Description: 单关结算（根据投注结果，进行金额结算  0-无结果  2-走水  3-输 4-赢 5-赢一半 6-输一半，7赛事取消，
     * 8赛事延期，11比赛延迟，12比赛中断，13未知，15比赛放弃，16异常盘口，17未知赛事状态，18比赛取消，19比赛延期，20SR-其他，
     * 21SR-无进球球员，22SR-正确比分丢失，23SR-无法确认的赛果，24SR-格式变更，25SR-进球球员丢失，26SR-主动弃赛
     * 27SR-并列获胜，28SR-中途弃赛，29SR-赔率错误，30SR-统计错误，31SR-投手变更）
     */
    public static Double singleAlgorithmsAmount(int result, Double maxWinAmount, Double orderAmountTotal) {
        Double settleAmount = 0d;
        switch (result) {
            case 2:
            case 7:
            case 8:
            case 11:
            case 12:
            case 13:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
                settleAmount = orderAmountTotal;
                break;
            case 4:
                settleAmount = maxWinAmount + orderAmountTotal;
                break;
            case 5:
                settleAmount = orderAmountTotal + maxWinAmount / 2;
                break;
            case 6:
                settleAmount = orderAmountTotal / 2;
                break;
            case 3:
            default:
                return settleAmount;
        }
        //结算金额统一处理，四舍六入五成双的问题
        return ArithmeticUtils.sciCal(new BigDecimal(settleAmount).divide(new BigDecimal(100)).doubleValue(), 2).multiply(new BigDecimal(100)).doubleValue();
    }

    /**
     * @param result 投注结果
     * @return odds   欧赔赔率
     * @Author: butr
     * @Description: 单关结算（根据投注结果，进行金额结算  0-无结果  2-走水  3-输 4-赢 5-赢一半 6-输一半，7赛事取消，
     * 8赛事延期，11比赛延迟，12比赛中断，13未知，15比赛放弃，16异常盘口，17未知赛事状态，18比赛取消，19比赛延期，20SR-其他，
     * 21SR-无进球球员，22SR-正确比分丢失，23SR-无法确认的赛果，24SR-格式变更，25SR-进球球员丢失，26SR-主动弃赛
     * 27SR-并列获胜，28SR-中途弃赛，29SR-赔率错误，30SR-统计错误，31SR-投手变更）
     */
    public static BigDecimal getOddsByResult(int result, BigDecimal odds) {
        switch (result) {
            case 2:
            case 7:
            case 8:
            case 11:
            case 12:
            case 13:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
                return new BigDecimal(1);
            case 3:
                return odds.multiply(new BigDecimal(0));
            case 4:
                return odds;
            case 5:
                return odds.subtract(new BigDecimal(1)).divide(new BigDecimal(2)).add(new BigDecimal(1));
            case 6:
                return new BigDecimal(0.5);
            default:
                return new BigDecimal(0);
        }
    }


    /**
     * @param v1
     * @param v2
     * @Author: Joken
     * @Description: 两个对象对比大小
     * @Date: 2020/5/15 14:31
     * @Return: boolean
     */
    public static boolean compareMaxMin(double v1, double v2) {
        if (v1 >= v2) {
            return true;
        }
        return false;
    }

    /**
     * @param v1
     * @param v2
     * @Author: Joken
     * @Description: 两个对象对比大小
     * @Date: 2020/5/15 14:31
     * @Return: boolean
     */
    public static double getMinValue(double v1, double v2) {
        if (compareMaxMin(v1, v2)) {
            return v2;
        }
        return v1;
    }

    /**
     * @param v1
     * @param v2
     * @Author: Joken
     * @Description: 两个对象对比大小
     * @Date: 2020/5/15 14:31
     * @Return: boolean
     */
    public static double getMaxValue(double v1, double v2) {
        if (compareMaxMin(v1, v2)) {
            return v1;
        }
        return v2;
    }
}
