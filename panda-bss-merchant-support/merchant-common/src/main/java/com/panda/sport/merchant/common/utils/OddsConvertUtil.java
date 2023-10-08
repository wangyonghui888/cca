package com.panda.sport.merchant.common.utils;


import com.google.protobuf.ServiceException;
import java.math.BigDecimal;

/**
 * 盘口赔率转换工具类
 *
 * @author sklee
 */
public class OddsConvertUtil {

    /**
     * 欧洲盘转香港盘
     *
     * @param odds
     * @return
     */
    public static BigDecimal oddsEUConvertHK(final BigDecimal odds) {
        //香港盘=欧洲盘-1
        return odds.subtract(BigDecimal.ONE);
    }

    /**
     * 欧洲盘转马来盘
     *
     * @param odds
     * @return
     */
    public static BigDecimal oddsEUConvertMY(final BigDecimal odds) {
        if (odds.doubleValue() < 2) {
            //若欧洲盘<2：马来盘=欧洲盘-1
            return odds.subtract(BigDecimal.ONE);
        } else {
            //若欧洲盘≥2：马来盘= 1 /（1-欧洲盘）
            return BigDecimal.ONE.divide((BigDecimal.ONE.subtract(odds)),2, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * 欧洲盘转印尼盘
     *
     * @param odds
     * @return
     */
    public static BigDecimal oddsEUConvertID(final BigDecimal odds) {
        if (odds.doubleValue() > 2) {
            //若欧洲盘>2：印尼盘= 欧洲盘-1
            return odds.subtract(BigDecimal.ONE);
        } else {
            //若欧洲盘<=2：印尼盘=-1/(欧洲赔率-1)
            return (BigDecimal.ONE.divide(odds.subtract(BigDecimal.ONE),2, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal(-1));
        }
    }

    /**
     * 欧洲盘转换美式盘
     *
     * @param odds
     * @return
     */
    public static BigDecimal oddsEUConvertUS(final BigDecimal odds) {
        if (odds.doubleValue() < 2) {
            //欧洲盘<2：美式盘= 100 /（1-欧洲盘）
            return new BigDecimal(100).divide(BigDecimal.ONE.subtract(odds),2, BigDecimal.ROUND_HALF_UP);
        } else {
            //欧洲盘≥2：美式盘= 100x（欧洲盘-1）
            return new BigDecimal(100).multiply(odds.subtract(BigDecimal.ONE));
        }
    }

    /**
     * 欧洲盘转换英式盘
     *
     * @param odds
     * @return
     */
    public static BigDecimal oddsEUConvertUk(final BigDecimal odds) {
        //英式盘=欧洲盘-1
        return odds.subtract(BigDecimal.ONE);
    }

    /**
     * 赔率统一转换为欧洲赔率
     *
     * @param odds        当前类型赔率数值
     * @param inParamEnum 输入赔率类型
     * @return
     */
    public static Double oddsConvertEU(final double odds, OddsTypeEnum inParamEnum) throws ServiceException {
        Double convertOdds = null;
        switch (inParamEnum) {
            case HK:
                //欧洲盘=香港盘+1
                convertOdds = odds + 1;
                break;
            case EU:
                convertOdds = odds;
                break;
            case ID:
                if (odds < 0) {
                    //若印尼盘为负数，欧洲盘=1-(1/印尼盘)
                    convertOdds = 1 - (1 / odds);
                } else {
                    //若印尼盘为正数，欧洲盘=印尼盘+1
                    convertOdds = 1 + odds;
                }
                break;
            case MY:
                if (odds < 0) {
                    //若马来盘为负数，欧洲盘=1-(1/马来盘)
                    convertOdds = 1 - (1 / odds);
                } else {
                    //若马来盘为正数，欧洲盘=马来盘+1
                    convertOdds = 1 + odds;
                }
                break;
            case UK:
                //欧洲赔率=英式盘+1
                convertOdds = 1 + odds;
                break;
            case US:
                if (odds < 0) {
                    //若美式盘为负数，欧洲赔率=1-100/美式盘
                    convertOdds = 1 - 100 / odds;
                } else {
                    //若美式盘为正数，欧洲盘=1+美式盘/100
                    convertOdds = 1 + odds / 100;
                }
                break;
            default:
                break;
        }
        if (convertOdds == null) {
            throw new ServiceException("赔率转换异常");
        }
        return convertOdds;
    }


}
