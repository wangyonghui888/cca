package com.panda.sport.merchant.common.algrithom;

import java.math.BigDecimal;

/**
 * 盘口赔率计算工具类
 * @author sklee
 *
 */
public class OddsCalculateUtil {

	public static BigDecimal odds_times = new BigDecimal(100000);

	/**
	 * 统一用欧洲盘库赔率计算金额
	 * @param ueOdds  欧洲盘口赔率
	 * @param orderAmount  下注金额
	 * @return
	 */
	public static BigDecimal amountByUEOddsAndOrderAmount(final float ueOdds,final BigDecimal orderAmount) {
		//使用欧洲赔率计算方式 最后截取2位小数
		return sciCalKeepTwo(orderAmount.multiply(new BigDecimal(ueOdds)).doubleValue());
	}



	/**
	 * @param value 需要科学计算的数据
	 * @return
	 * 功能：四舍六入五成双计算法 保留2位小数
	 */
	public static BigDecimal sciCalKeepTwo(double value){
		return sciCal(value,2);
	}

	/**
	 * @param value 需要科学计算的数据
	 * @param digit 保留的小数位
	 * @return
	 * 功能：四舍六入五成双计算法
	 */
	public static BigDecimal sciCal(double value, int digit){
		BigDecimal result;
		try {
			double ratio = Math.pow(10, digit);
			double _num = value * ratio;
			double mod = _num % 1;
			double integer = Math.floor(_num);
			double returnNum;
			if(mod > 0.5){
				returnNum=(integer + 1) / ratio;
			}else if(mod < 0.5){
				returnNum=integer / ratio;
			}else{
				returnNum=(integer % 2 == 0 ? integer : integer + 1) / ratio;
			}
			BigDecimal bg = new BigDecimal(returnNum);
			result = bg.setScale((int)digit, BigDecimal.ROUND_HALF_UP);
		} catch (RuntimeException e) {
			throw e;
		}
		return result;
	}


	public static BigDecimal halfWin(BigDecimal odds) {
		return odds.subtract(new BigDecimal(1)).divide(new BigDecimal(2)).add(new BigDecimal(1));
	}

	/**
	 * @Author: butr
	 * @Description: 计算赔率:走水
	 * @return 走水：返回赔率1
	 */
	public static BigDecimal draw() {
		return odds_times;
	}

	/**
	 * @Author: butr
	 * @Description: 计算赔率: 输
	 * @param odds  盘口赔率
	 * @return  输：赔率乘零
	 */
	public static  BigDecimal allLose(BigDecimal odds) {
		return odds.multiply(new BigDecimal(0));
	}

	/**
	 * @Author: butr
	 * @Description: 计算赔率: 赢
	 * @param odds  盘口赔率
	 * @return 赢：返回当前赔率
	 */
	public static BigDecimal allWin(BigDecimal odds) {
		return odds;
	}

	/**
	 * @Author: butr
	 * @Description: 计算赔率:输半
	 * @return 输半：赔率除2
	 */
	public static BigDecimal halfLose() {
		return  new BigDecimal(0.5);
	}

	/**
	 * @Author: butr
	 * @Description: 计算最后金额
	 * @param odds
	 * @param amount
	 * @return
	 */
	public static BigDecimal settleAmount(BigDecimal odds, BigDecimal amount) {
		return sciCalKeepTwo(odds.multiply(amount).doubleValue());
	}
}
