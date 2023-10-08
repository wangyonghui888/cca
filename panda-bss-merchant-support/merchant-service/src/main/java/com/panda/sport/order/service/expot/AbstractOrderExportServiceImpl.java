package com.panda.sport.order.service.expot;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.panda.sport.backup.mapper.BackupOrderMixMapper;
import com.panda.sport.backup.mapper.MergeOrderMixMapper;
import com.panda.sport.backup.mapper.OrderMixExportMapper;
import com.panda.sport.merchant.common.constant.CommonDefaultValue;
import com.panda.sport.merchant.common.constant.Constant;
import com.panda.sport.merchant.common.enums.CurrencyTypeEnum;
import com.panda.sport.merchant.common.enums.LanguageEnum;
import com.panda.sport.merchant.common.po.merchant.MerchantFile;
import com.panda.sport.merchant.common.utils.CsvUtil;
import com.panda.sport.merchant.common.vo.BetOrderVO;
import com.panda.sport.merchant.common.vo.merchant.OrderSettle;
import com.panda.sport.merchant.manage.service.impl.LocalCacheService;
import com.panda.sport.order.service.MerchantFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.panda.sport.merchant.common.constant.Constant.*;

/**
 * @author :  duwan
 * @Project Name :  panda-merchant
 * @Package Name :  com.panda.sports.order.file
 * @Description :  注单导出实现类
 * @Date: 2020-12-11 13:46:00
 * @ModificationHistory Who    When    What
 * --------  ---------  --------------------------
 */
@Slf4j
@Service("abstractOrderExportService")
public abstract class AbstractOrderExportServiceImpl extends AbstractOrderFileExportService {

	@Autowired
	public OrderMixExportMapper orderMixExportMapper;
	@Autowired
	public BackupOrderMixMapper backupOrderMixMapper;
	@Autowired
	public MergeOrderMixMapper mergeOrderMixMapper;

	private static final int threadSize = 20000;

	private static final int pageSize = 2000;

	public void execute(MerchantFile merchantFile) {
		long rate = 10;
		try {
			log.info("大文件开始导出注单 param = {}", merchantFile.getExportParam());
			if (super.checkTask(merchantFile.getId())) {
				log.info("当前任务被删除！");
				return;
			}
			super.updateRate(merchantFile.getId(), rate);
			BetOrderVO betOrderVO = JSON.parseObject(merchantFile.getExportParam(), BetOrderVO.class);
			long startTL = System.currentTimeMillis();
			String filter = StringUtils.isEmpty(betOrderVO.getFilter()) ? "1" : betOrderVO.getFilter();
			log.info("注单导出查询入参:" + betOrderVO);
			Integer count = merchantFile.getDataSize();
			if (count == null) {
				count = countOrder(filter, betOrderVO, betOrderVO.getDatabaseSwitch());
			}
			if (count > 1000000) {
				throw new RuntimeException("导出数据大于1000000条！");
			}
			log.info("组装 导出注单betOrderVO: {} , 总数 = {}", betOrderVO.getStartTimeL(), count);
			List<OrderSettle> orderSettleList;
			if (count < MerchantFileService.SPLIT_FILE_DATE_SIZE) {
				InputStream inputStream = null;
				try {
					if (count > pageSize) {
						orderSettleList = new ArrayList<>();
						super.updateFileStart(merchantFile.getId());
						int thrednum = (count % threadSize == 0) ? (count / threadSize) : (count / threadSize + 1);
						ExecutorService service = Executors.newFixedThreadPool(thrednum);
						log.info("export  thrednum=" + thrednum);
						try {
							BlockingQueue<Future<List<OrderSettle>>> queue = new LinkedBlockingQueue<Future<List<OrderSettle>>>();
							for (int i = 0; i < thrednum; i++) {
								Future<List<OrderSettle>> future = service.submit(read2List(i, filter, betOrderVO));
								queue.add(future);
							}
							int queueSize = queue.size();
							log.info("export  queueSize=" + queueSize);
							for (int i = 0; i < queueSize; i++) {
								List<OrderSettle> list = queue.take().get();
								log.info("export  list=" + (list == null ? 0 : list.size()));
								orderSettleList.addAll(list);
							}
							log.info("export  orderSettleList=" + orderSettleList.size());
						} finally {
							service.shutdown();
						}
					} else {
						betOrderVO.setStart(0);
						betOrderVO.setSize(pageSize);
						orderSettleList = processQuery(filter, betOrderVO);
					}
					super.updateRate(merchantFile.getId(), 80L);
					log.info(":查询结束,花费时间" + (System.currentTimeMillis() - startTL));
					if (CollectionUtils.isEmpty(orderSettleList)) {
						throw new Exception("未查询到数据");
					}
					inputStream = new ByteArrayInputStream(exportOrderToCsv(orderSettleList, betOrderVO.getLanguage(),merchantFile.getMerchantCode()));
					super.uploadFile(merchantFile, inputStream);
					log.info("导出结束,花费时间: {}", (System.currentTimeMillis() - startTL));
					super.updateFileStatusEnd(merchantFile.getId());
				} finally {
					if (inputStream != null) {
						try {
							inputStream.close();
						} catch (Exception e) {
							log.error("流关闭异常3！", e);
						}
					}
				}
			} else {
				//大于30W走文件分割
				String[] names = merchantFile.getFtpFileName().split(",");
				int k = 0;
				for (int j = names.length; j > 0; j--) {
					InputStream inputStream = null;
					try {
						orderSettleList = new ArrayList<>();
						super.updateFileStart(merchantFile.getId());
						int thrednum = 5;
						ExecutorService service = Executors.newFixedThreadPool(thrednum);
						log.info("export  thrednum=" + thrednum);
						try {
							BlockingQueue<Future<List<OrderSettle>>> queue = new LinkedBlockingQueue<Future<List<OrderSettle>>>();
							for (int i = 0; i < thrednum; i++) {
								Future<List<OrderSettle>> future = service.submit(read2List(k, i, filter, betOrderVO));
								queue.add(future);
							}
							int queueSize = queue.size();
							log.info("export  queueSize=" + queueSize);
							for (int i = 0; i < queueSize; i++) {
								List<OrderSettle> list = queue.take().get();
								log.info("export  list=" + (list == null ? 0 : list.size()));
								orderSettleList.addAll(list);
							}
							log.info("export  orderSettleList=" + orderSettleList.size());
						} finally {
							service.shutdown();
						}
						super.updateRate(merchantFile.getId(), 80L / j);
						log.info("查询结束,花费时间:" + (System.currentTimeMillis() - startTL));
						if (CollectionUtils.isEmpty(orderSettleList)) {
							throw new Exception("未查询到数据");
						}
						inputStream = new ByteArrayInputStream(exportOrderToCsv(orderSettleList, betOrderVO.getLanguage(),merchantFile.getMerchantCode()));
						super.uploadFileMax(merchantFile, inputStream, names[k]);
						log.info("导出结束,花费时间: {}", (System.currentTimeMillis() - startTL));
					} finally {
						if (inputStream != null) {
							try {
								inputStream.close();
							} catch (Exception e) {
								log.error("流关闭异常3！", e);
							}
						}
					}
					k++;
				}
				super.updateFileStatusEnd(merchantFile.getId());
			}
		} catch (Exception e) {
			log.error("订单导出异常！id ={}", merchantFile.getId(), e);
			super.exportFail(merchantFile.getId(), e.getMessage());
		}
	}

	public List<OrderSettle> processQuery(String filter, BetOrderVO betOrderVO) {
		List<OrderSettle> betOrderPOList;
		List<OrderSettle> userMerchantPOList = new ArrayList<>();
		String databaseSwitch = betOrderVO.getDatabaseSwitch();
		if (StringUtils.isEmpty(databaseSwitch)){
			databaseSwitch = "0";
		}
		Long beginTime = System.currentTimeMillis();
		if ("1".equals(filter)) {
			String orderBy = StringUtils.isEmpty(betOrderVO.getSqlOrder()) || Constant.enabledSortColumnMap.get(betOrderVO.getSqlOrder()) == null ?
					Constant.ID : Constant.enabledSortColumnMap.get(betOrderVO.getSqlOrder());
			String sort = StringUtils.isEmpty(betOrderVO.getSortby()) ? Constant.DESC : betOrderVO.getSortby();
			betOrderVO.setSqlOrder(orderBy);
			betOrderVO.setSortby(sort);
			//betOrderPOList = orderMixExportMapper.queryBetOrderList(betOrderVO);
			betOrderPOList = databaseSwitch.equals(REALTIME_TABLE) ? backupOrderMixMapper.queryBetOrderList(betOrderVO) :
					mergeOrderMixMapper.queryBetOrderList(betOrderVO);
			if(databaseSwitch.equals(REALTIME_TABLE) && CollectionUtils.isNotEmpty(betOrderPOList)) {
				Set<Long> uidList =betOrderPOList.stream().map(e->Long.parseLong(e.getUid())).collect(Collectors.toSet());
				log.info("导出用户数===={}"+uidList.size());
				userMerchantPOList = databaseSwitch.equals(REALTIME_TABLE) ? backupOrderMixMapper.queryUserMerchantPOList(uidList) :
						mergeOrderMixMapper.queryUserMerchantPOList(uidList);
			}
		} else if ("3".equals(filter)) {
			String orderBy = StringUtils.isEmpty(betOrderVO.getSqlOrder()) || Constant.enabledSortColumnMap1.get(betOrderVO.getSqlOrder()) == null ?
					"s1.id" : Constant.enabledSortColumnMap1.get(betOrderVO.getSqlOrder());
			String sort = StringUtils.isEmpty(betOrderVO.getSortby()) ? Constant.DESC : betOrderVO.getSortby();
			betOrderVO.setSqlOrder(orderBy);
			betOrderVO.setSortby(sort);
			//betOrderPOList = orderMixExportMapper.querySettledOrderList(betOrderVO);
			betOrderPOList = databaseSwitch.equals(REALTIME_TABLE) ? backupOrderMixMapper.querySettledOrderList(betOrderVO) :
					mergeOrderMixMapper.queryBetOrderList(betOrderVO);
			if(databaseSwitch.equals(REALTIME_TABLE) && CollectionUtils.isNotEmpty(betOrderPOList)) {
				Set<Long> uidList = betOrderPOList.stream().map(e -> Long.parseLong(e.getUid())).collect(Collectors.toSet());
				log.info("导出用户数===={}"+uidList.size());
				userMerchantPOList = (databaseSwitch.equals(REALTIME_TABLE) ? backupOrderMixMapper.queryUserMerchantPOList(uidList) :
						mergeOrderMixMapper.queryUserMerchantPOList(uidList));
			}
		} else {
			String orderBy = StringUtils.isEmpty(betOrderVO.getSqlOrder()) || Constant.enabledSortColumnMap1.get(betOrderVO.getSqlOrder()) == null ?
					Constant.ID : Constant.enabledSortColumnMap1.get(betOrderVO.getSqlOrder());
			String sort = StringUtils.isEmpty(betOrderVO.getSortby()) ? Constant.DESC : betOrderVO.getSortby();
			betOrderVO.setSqlOrder(orderBy);
			betOrderVO.setSortby(sort);
			//betOrderPOList = orderMixExportMapper.queryLiveOrderList(betOrderVO);
			betOrderPOList = databaseSwitch.equals(REALTIME_TABLE) ? backupOrderMixMapper.queryLiveOrderList(betOrderVO) : mergeOrderMixMapper.queryBetOrderList(betOrderVO);
			if(databaseSwitch.equals(REALTIME_TABLE) && CollectionUtils.isNotEmpty(betOrderPOList)) {
				Set<Long> uidList = betOrderPOList.stream().map(e -> Long.parseLong(e.getUid())).collect(Collectors.toSet());
				log.info("导出用户数===={}"+uidList.size());
				userMerchantPOList = (databaseSwitch.equals(REALTIME_TABLE) ? backupOrderMixMapper.queryUserMerchantPOList(uidList) :
						mergeOrderMixMapper.queryUserMerchantPOList(uidList));
			}
		}
		Long emdTimed= System.currentTimeMillis();
		log.info("导出查询sql耗时：{}" + (emdTimed-beginTime));
		//组装betOrderPOList
		if(CollectionUtils.isNotEmpty(userMerchantPOList)){
			betOrderPOList= assembleBetOrderList(betOrderPOList,userMerchantPOList);
		}
		return betOrderPOList;
	}
	private  List<OrderSettle> assembleBetOrderList(List<OrderSettle> orderList,List<OrderSettle>userMerchantPOList){
		for(OrderSettle orderSettle : orderList){
			A: for(OrderSettle order:userMerchantPOList) {
				if (orderSettle.getUid().equals(order.getUid())) {
					orderSettle.setUserName(order.getUserName());
					orderSettle.setFakeName(order.getFakeName());
					orderSettle.setUserLevel(order.getUserLevel());
					orderSettle.setCurrencyCode(order.getCurrencyCode());
					orderSettle.setMerchantName(order.getMerchantName());
					orderSettle.setMerchantCode(order.getMerchantCode());
					orderSettle.setTransferMode(order.getTransferMode());
					break A;
				}
			}
		}
		return orderList;
	}
	protected int countOrder(String filter, BetOrderVO betOrderVO,String databaseSwitch) {
		log.info("countOrder:" + betOrderVO.getStartTimeL() + "," + betOrderVO.getEndTimeL() + "," + betOrderVO.getFilter() +
				",outcomeList=" + betOrderVO.getOutComeList() + ",matchType=" + betOrderVO.getMatchType());
		if (BET_TIME_FILTER.equals(filter)) {
			return REALTIME_TABLE.equals(databaseSwitch) ? backupOrderMixMapper.countBetOrderList(betOrderVO) : getCountFromCache(betOrderVO);
			//return backupOrderMixMapper.countBetOrderList(betOrderVO);
		} else if (SETTLE_TIME_FILTER.equals(filter)) {
			return REALTIME_TABLE.equals(databaseSwitch) ? backupOrderMixMapper.countSettledOrderList(betOrderVO) :
					getCountFromCache(betOrderVO);
			//return backupOrderMixMapper.countSettledOrderList(betOrderVO);
		} else {
			return getCountFromCache(betOrderVO);
			//return REALTIME_TABLE.equals(databaseSwitch) ? backupOrderMixMapper.countLiveOrderList(betOrderVO) : getCountFromCache(betOrderVO);
			//return backupOrderMixMapper.countLiveOrderList(betOrderVO);
		}
	}

	private Integer getCountFromCache(BetOrderVO betOrderVO) {
		StringBuilder key = new StringBuilder("orderNo=" + betOrderVO.getOrderNo() + "betNo=" + betOrderVO.getBetNo() + "startTimeL=" + betOrderVO.getStartTimeL() + "endTimeL=" + betOrderVO.getEndTimeL() + "userName=" +
				betOrderVO.getUserName() + "userId=" + betOrderVO.getUserId() + "filter=" + betOrderVO.getFilter() + "currency=" + betOrderVO.getCurrency() + "merchantCode=" + betOrderVO.getMerchantCode() +
				"marketType=" + betOrderVO.getMarketType() + "language=" +
				betOrderVO.getLanguage() + "vip=" + betOrderVO.getUserVip() + "sportId=" + betOrderVO.getSportId() + "playId=" + betOrderVO.getPlayId() + "settleTimes=" + betOrderVO.getSettleTimes() +
				"orderStatus=" + betOrderVO.getOrderStatus() + "seriesType=" +
				betOrderVO.getSeriesType() + betOrderVO.getSettleType() + betOrderVO.getMaxOdds() + betOrderVO.getMinOdds() + "managerCode=" + betOrderVO.getManagerCode() + "betAmount=" + betOrderVO.getMaxBetAmount() + betOrderVO.getMinBetAmount() +
				"matchId=" + betOrderVO.getManagerCode() + "matchType=" + betOrderVO.getMatchType() + "profit=" + betOrderVO.getMaxProfit() + betOrderVO.getMinProfit()
				+ "settleStatus=" + betOrderVO.getSettleStatus() + "settleCancel=" + betOrderVO.getSettleCancle() + "cashout=" + betOrderVO.getEnablePreSettle() + "accountTag=" + betOrderVO.getAccountTag() + "tournamentId=" + betOrderVO.getTournamentId());
		List<String> merchantCodeList = betOrderVO.getMerchantCodeList();
		if (CollectionUtils.isNotEmpty(merchantCodeList)) {
			key.append("merchantCodeList=");
			for (String code : merchantCodeList) {
				key.append(code);
			}
		}
		List<Integer> playList = betOrderVO.getPlayIdList();
		if (CollectionUtils.isNotEmpty(playList)) {
			key.append("playList=");
			for (Integer code : playList) {
				key.append(code);
			}
		}
		List<Integer> orderStatusList = betOrderVO.getOrderStatusList();
		if (CollectionUtils.isNotEmpty(orderStatusList)) {
			key.append("orderStatusList=");
			for (Integer code : orderStatusList) {
				key.append(code);
			}
		}
		List<Integer> outComeList = betOrderVO.getOutComeList();
		if (CollectionUtils.isNotEmpty(outComeList)) {
			key.append("outComeList=");
			for (Integer code : outComeList) {
				key.append(code);
			}
		}
		Integer total = LocalCacheService.ticketSearchMap.getIfPresent(key.toString());
		log.info("缓存获取getCountFromCache:" + total);
		if (total == null) {
			total = mergeOrderMixMapper.countBetOrderList(betOrderVO);
			LocalCacheService.ticketSearchMap.put(key.toString(), total);
		}
		return total;
	}

	protected byte[] exportOrderToCsv(List<OrderSettle> resultList, String language,String merchantCode) throws Exception {
		List<LinkedHashMap<String, Object>> exportData =
				new ArrayList<>(CollectionUtils.isEmpty(resultList) ? 0 : resultList.size());
		Integer i = 0;

		for (OrderSettle order : resultList) {
			try {
				if(order.getOrderDetailList() == null ||order.getOrderDetailList().size() ==0 || StringUtils.isEmpty(order.getOrderDetailList().get(0).getBetNo()))
				{
					continue;
				}

				LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
				i = i + 1;

				rowData.put("1", i);
				rowData.put("2", order.getFakeName()  + "\t");
				rowData.put("4", "123");
				rowData.put("5", order.getMerchantCode());
				rowData.put("6", language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ?
						CurrencyTypeEnum.optGetDescription(order.getCurrencyCode()) : CurrencyTypeEnum.optGetCurrency(order.getCurrencyCode()));
				rowData.put("7", getObj(order.getProfitAmount()));
				rowData.put("8", getObj(order.getOrderAmountTotal()));
				rowData.put("9", getOrderStatus(order.getOrderStatus(), language));
				rowData.put("10", getObj(order.getCreateTimeStr()));
				rowData.put("11", getObj(order.getSettleTimeStr()));
				rowData.put("12", order.getOrderNo() + "\t");
				rowData.put("13", getSeriesValue(order.getSeriesType(), language));
				rowData.put("14", language.equalsIgnoreCase(LanguageEnum.ZS.getCode()) ?
						(order.getOutcome() == null ? "未结算" : settleStatusMap.get(order.getOutcome())) :
						(order.getOutcome() == null ? "unsettled" : settleStatusEnMap.get(order.getOutcome())));
				rowData.put("15", getObj(order.getUid()) + "\t");
				rowData.put("16", getObj(order.getLocalProfitAmount()));
				rowData.put("17", getObj(order.getLocalBetAmount()));
				rowData.put("19", getDeviceType(order.getDeviceType(),language));

				BigDecimal orderValidBetMoney = new BigDecimal(0);

				if (order.getOutcome() != null && (order.getOutcome() == 4 || order.getOutcome() == 5)) {
					if (order.getProfitAmount() != null) {
						if (order.getLocalBetAmount().compareTo(order.getProfitAmount()) < 0) {
							orderValidBetMoney = order.getLocalBetAmount().abs();
						} else {
							orderValidBetMoney = order.getProfitAmount().abs();
						}
					}
				} else {
					if (order.getProfitAmount() != null) {
						orderValidBetMoney = order.getProfitAmount().abs();
					}
				}

				// 填充有效投注笔数
				order.setSumValidBetNo(CommonDefaultValue.ZERO);
				if (order.getOutcome() != null && (order.getOutcome() == 3 || order.getOutcome() == 4 || order.getOutcome() == 5 || order.getOutcome() == 6)) {
					order.setSumValidBetNo(CommonDefaultValue.ONE);
				}
				rowData.put("20", getObj(orderValidBetMoney));
				if (order.getOrderDetailList() != null) {
					if (StringUtils.isEmpty(merchantCode)) {
						for (int j = 0; j < order.getOrderDetailList().size(); j++) {
							LinkedHashMap<String, Object> rowDataCopy = new LinkedHashMap<>(rowData);
							Integer matchType = order.getOrderDetailList().get(j).getMatchType();
							Integer betResult = order.getOrderDetailList().get(j).getBetResult();

							rowDataCopy.put("21", Objects.equals(order.getSeriesType(), 1) ?
									(language.equals(LanguageEnum.ZS.getCode()) ?
											(matchType == 1 ? "赛前" : (matchType == 2 ? "滚球盘" : "冠军盘")) :
											(matchType == 1 ? "Prematch" : (matchType == 2 ? "Live" : "Outright"))) :
									(language.equals(LanguageEnum.ZS.getCode()) ? "串关" :"Cross connection" ));
							rowDataCopy.put("22", order.getOrderDetailList().get(j).getSportName());
							rowDataCopy.put("23", getObj(order.getOrderDetailList().get(j).getTournamentName()));
							rowDataCopy.put("24", order.getOrderDetailList().get(j).getMatchInfo());
							rowDataCopy.put("25", getObj(order.getOrderDetailList().get(j).getMatchId()) );
							rowDataCopy.put("26", getObj(order.getOrderDetailList().get(j).getBeginTimeStr()));
							rowDataCopy.put("27", order.getOrderDetailList().get(j).getPlayName());
							rowDataCopy.put("28", order.getOrderDetailList().get(j).getPlayOptionName() + "\t" );
							rowDataCopy.put("29",  getObj(order.getOrderDetailList().get(j).getMarketType()));
							rowDataCopy.put("30", getObj(order.getOrderDetailList().get(j).getMarketValue()));
							rowDataCopy.put("31",getObj(order.getOrderDetailList().get(j).getOddFinally()));
							rowDataCopy.put("32", getObj(order.getIp()));
							rowDataCopy.put("33", order.getDeviceImei());
							int series =  order.getOrderDetailList().size();
							rowDataCopy.put("36", order.getMerchantCode());
							rowDataCopy.put("37", order.getMerchantName());
							rowDataCopy.put("38", ObjectUtil.equal(order.getUserLevel(),1) ? (language.equals(LanguageEnum.ZS.getCode()) ? "Vip用户":"VIP user"): (language.equals(LanguageEnum.ZS.getCode()) ? "普通用户":"Ordinary users"));
							rowDataCopy.put("39", order.getLocalBetAmount() == null ? "":order.getLocalBetAmount().divide(BigDecimal.valueOf(series),4,BigDecimal.ROUND_HALF_UP));
							rowDataCopy.put("40", order.getLocalProfitAmount() == null ? "":order.getLocalProfitAmount().divide(BigDecimal.valueOf(series),4,BigDecimal.ROUND_HALF_UP));
							rowDataCopy.put("41", getObj(order.getPreBetAmount()));
							rowDataCopy.put("42", order.getVipUpdateTimeStr());
							rowDataCopy.put("43", order.getSumValidBetNo());
							rowDataCopy.put("44", language.equalsIgnoreCase(LanguageEnum.ZS.getCode()) ?
									(betResult == null || betResult == 0 ? "未结算" : settleStatusMap.get(betResult)):
									(betResult == null || betResult == 0 ? "unsettled" : settleStatusEnMap.get(betResult)));
							exportData.add(rowDataCopy);
						}
					}else {
						if("trader".equals(merchantCode)){
							Integer matchType = order.getOrderDetailList().get(0).getMatchType();
							rowData.put("21", Objects.equals(order.getSeriesType(), 1) ?
									(language.equals(LanguageEnum.ZS.getCode()) ?
											(matchType == 1 ? "赛前" : (matchType == 2 ? "滚球盘" : "冠军盘")) :
											(matchType == 1 ? "Prematch" : (matchType == 2 ? "Live" : "Outright"))) :
									(language.equals(LanguageEnum.ZS.getCode()) ? "串关" :"Cross connection" ));
							rowData.put("22", order.getOrderDetailList().get(0).getSportName());
							if ("1".equals(String.valueOf(order.getSeriesType()))){
								rowData.put("23", getObj(order.getOrderDetailList().get(0).getTournamentName()));
								rowData.put("24", order.getOrderDetailList().get(0).getMatchInfo());
								rowData.put("25", getObj(order.getOrderDetailList().get(0).getMatchId()) );
								rowData.put("26", getObj(order.getOrderDetailList().get(0).getBeginTimeStr()));
								rowData.put("27", order.getOrderDetailList().get(0).getPlayName());
								rowData.put("28", order.getOrderDetailList().get(0).getPlayOptionName() + "\t" );
								rowData.put("29",  getObj(order.getOrderDetailList().get(0).getMarketType()));
								rowData.put("30", getObj(order.getOrderDetailList().get(0).getMarketValue()));
								rowData.put("31",getObj(order.getOrderDetailList().get(0).getOddFinally()));
							}else {
								rowData.put("23", " ");
								rowData.put("24", " ");
								rowData.put("25", " ");
								rowData.put("26", " ");
								rowData.put("27", " ");
								rowData.put("28", " ");
								rowData.put("29", " ");
								rowData.put("30", " ");
								rowData.put("31", " ");
							}
							rowData.put("32", getObj(order.getIp()));
							rowData.put("33", order.getDeviceImei());
							int series =  order.getOrderDetailList().size();
							rowData.put("36", order.getMerchantCode());
							rowData.put("37", order.getMerchantName());
							rowData.put("38", ObjectUtil.equal(order.getUserLevel(),1) ? (language.equals(LanguageEnum.ZS.getCode()) ? "Vip用户":"VIP user"):(language.equals(LanguageEnum.ZS.getCode()) ? "普通用户":"Ordinary users"));
							//rowData.put("39", order.getOrderAmountTotal().divide(BigDecimal.valueOf(series),4,BigDecimal.ROUND_HALF_UP));
							//rowData.put("40",order.getProfitAmount() == null ? "" : order.getProfitAmount().divide(BigDecimal.valueOf(series),4,BigDecimal.ROUND_HALF_UP));
							rowData.put("39", order.getLocalBetAmount() == null ? "": order.getLocalBetAmount().divide(BigDecimal.valueOf(series),4,BigDecimal.ROUND_HALF_UP));
							rowData.put("40",order.getLocalProfitAmount() == null ? "" : order.getLocalProfitAmount().divide(BigDecimal.valueOf(series),4,BigDecimal.ROUND_HALF_UP));
							rowData.put("41", getObj(order.getPreBetAmount()));
							rowData.put("42", order.getVipUpdateTimeStr());
							rowData.put("43", order.getSumValidBetNo());
							rowData.put("44", "");
						}else {
							Integer matchType = order.getOrderDetailList().get(0).getMatchType();
							rowData.put("21", Objects.equals(order.getSeriesType(), 1) ?
									(language.equals(LanguageEnum.ZS.getCode()) ?
											(matchType == 1 ? "赛前" : (matchType == 2 ? "滚球盘" : "冠军盘")) :
											(matchType == 1 ? "Prematch" : (matchType == 2 ? "Live" : "Outright"))) :
									(language.equals(LanguageEnum.ZS.getCode()) ? "串关" :"Cross connection" ));
							rowData.put("22", order.getOrderDetailList().get(0).getSportName());
							rowData.put("23", getObj(order.getOrderDetailList().get(0).getTournamentName()));
							rowData.put("24", order.getOrderDetailList().get(0).getMatchInfo());
							rowData.put("25", getObj(order.getOrderDetailList().get(0).getMatchId()));
							rowData.put("26", getObj(order.getOrderDetailList().get(0).getBeginTimeStr()));
							rowData.put("27", order.getOrderDetailList().get(0).getPlayName());
							rowData.put("28", order.getOrderDetailList().get(0).getPlayOptionName() + "\t");
							rowData.put("29", getObj(order.getOrderDetailList().get(0).getMarketType()));
							rowData.put("30", getObj(order.getOrderDetailList().get(0).getMarketValue()));
							rowData.put("31", getObj(order.getOrderDetailList().get(0).getOddFinally()));
							rowData.put("32", getObj(order.getIp()));
							rowData.put("33", order.getDeviceImei());
							rowData.put("34", order.getVipUpdateTimeStr());
							rowData.put("35", order.getSumValidBetNo());
							rowData.put("41", getObj(order.getPreBetAmount()));
						}
						exportData.add(rowData);
					}
				} else {
					rowData.put("21", " ");
					rowData.put("22", " ");
					rowData.put("23", " ");
					rowData.put("24", " ");
					rowData.put("25", " ");
					rowData.put("26", " ");
					rowData.put("27", " ");
					rowData.put("28", " ");
					rowData.put("29", " ");
					rowData.put("30", " ");
					rowData.put("31", " ");
					rowData.put("32", getObj(order.getIp()));
					rowData.put("33", order.getDeviceImei());
					rowData.put("34", order.getVipUpdateTimeStr());
					rowData.put("35", order.getSumValidBetNo());
					rowData.put("41", getObj(order.getPreBetAmount()));
					exportData.add(rowData);
				}

				//添加提前结算列表
	/*			if(order.getPreSettleDetailList()!= null && order.getPreSettleDetailList().size() > 0) {
					if(order.getPreSettleDetailList().get(0).getOrderNo() != null) {
						exportData.addAll(getPreSettleSubDataRow(order, i, language));
					}
				}*/

			} catch (Exception e) {
				log.error("数据异常！data = {}", JSON.toJSONString(order), e);
				//throw new Exception("数据异常！" + JSON.toJSONString(order));
			}
		}
		if (StringUtils.isEmpty(merchantCode) || "trader".equals(merchantCode)){
			return CsvUtil.exportCSV(getHeaderManage(language), exportData);
		}else {
			return CsvUtil.exportCSV(getHeader(language), exportData);
		}
	}
//1:H5，2：PC,3:Android,4:IOS

	private String getDeviceType(String deviceType,String language) {
		switch (String.valueOf(deviceType)) {
			case "1":
				return "H5";
			case "2":
				return "PC";
			case "3":
				return "Android";
			case "4":
				return "IOS";
			default:
				return (language.equals(LanguageEnum.ZS.getCode()) ? "未知" :"unknown" );
		}
	}

	private String getSeriesValue(Integer seriesType, String language) {
		return !language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED) ? series_en.get(seriesType + "") : series_zs.get(seriesType + "");
	}

	private LinkedHashMap<String, String> getHeader(String language) {
		LinkedHashMap<String, String> header = new LinkedHashMap<>();
		if (language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED)) {
			header.put("1", "序号");
			header.put("2", "用户名称");
			header.put("4", "平台名称");
			header.put("5", "商户名称");
			header.put("6", "用户币种");
			header.put("7", "盈亏");
			header.put("8", "下注金额");
			header.put("9", "注单状态");
			header.put("10", "投注时间");
			header.put("11", "结算时间");
			header.put("12", "注单号");
			header.put("13", "串关值");
			header.put("14", "结算状态");
			header.put("15", "用户ID");
			header.put("16", "盈亏(RMB)");
			header.put("17", "下注金额(RMB)");
			header.put("19", "设备信息");
			header.put("20", "有效注额");
			header.put("21", "注单类型");
			header.put("22", "赛种");
			header.put("23", "联赛名称");
			header.put("24", "比赛对阵");
			header.put("25", "赛事ID");
			header.put("26", "开赛时间");
			header.put("27", "玩法名称");
			header.put("28", "投注项名称");
			header.put("29", "盘口类型");
			header.put("30", "盘口值");
			header.put("31", "赔率");
			header.put("32", "ip");
			header.put("33", "设备号");
			header.put("34", "vip升级时间");
			header.put("35", "有效投注笔数");
			header.put("41", "提前结算金额");

		} else {
			header.put("1", "NO");
			header.put("2", "User");
			header.put("4", "Merchant");
			header.put("5", "MerchantName");
			header.put("6", "Currency");
			header.put("7", "Sport");
			header.put("8", "BetAmount");
			header.put("9", "MarketType");
			header.put("10", "BetTime");
			header.put("11", "MatchBegin");
			header.put("12", "BetNo");
			header.put("13", "SeriesValue");
			header.put("14", "Outcome");
			header.put("15", "MatchId");
			header.put("16", "Profit(RMB)");
			header.put("17", "BetAmount(RMB)");
			header.put("19", "device");
			header.put("20", "orderValidBetMoney");
			header.put("21", "BetType");
			header.put("22", "sport");
			header.put("23", "Tournament");
			header.put("24", "MatchInfo");
			header.put("25", "matchId");
			header.put("26", "starTime");
			header.put("27", "PlayName");
			header.put("28", "PlayOption");
			header.put("29", "MarketValue");
			header.put("30", "Odds");
			header.put("31", "BetStatus");
			header.put("32", "ip");
			header.put("33", "IMEI");
			header.put("34", "vipTime");
			header.put("35", "sumValidBetNo");
			header.put("41", "PreBetAmount(RMB)");

		}
		return header;
	}

	private LinkedHashMap<String, String> getHeaderManage(String language) {
		LinkedHashMap<String, String> header = new LinkedHashMap<>();
		if (language.equalsIgnoreCase(LANGUAGE_CHINESE_SIMPLIFIED)) {
			header.put("1", "序号");
			header.put("2", "用户名称");
			header.put("4", "平台名称");
			header.put("5", "商户名称");
			header.put("6", "用户币种");
			header.put("7", "盈亏");
			header.put("8", "下注金额");
			header.put("9", "注单状态");
			header.put("10", "投注时间");
			header.put("11", "结算时间");
			header.put("12", "注单号");
			header.put("13", "串关值");
			header.put("14", "结算状态");
			header.put("15", "用户ID");
			header.put("16", "盈亏(RMB)");
			header.put("17", "下注金额(RMB)");

			header.put("19", "设备信息");
			header.put("20", "有效注额");
			header.put("21", "注单类型");
			header.put("22", "赛种");
			header.put("23", "联赛名称");
			header.put("24", "比赛对阵");
			header.put("25", "赛事ID");
			header.put("26", "开赛时间");
			header.put("27", "玩法名称");
			header.put("28", "投注项名称");
			header.put("29", "盘口类型");
			header.put("30", "盘口值");
			header.put("31", "赔率");
			header.put("32", "ip");
			header.put("33", "设备号");

			header.put("36", "商户代号");
			header.put("37", "商户名称");
			header.put("38", "团体式");
			header.put("39", "串关投注金额");
			header.put("40", "串关盈利");
			header.put("41", "提前结算金额");
			header.put("42", "vip升级时间");
			header.put("43", "有效投注笔数");
			header.put("44", "投注项状态");
		} else {
			header.put("1", "NO");
			header.put("2", "User");
			header.put("4", "Merchant");
			header.put("5", "MerchantName");
			header.put("6", "Currency");
			header.put("7", "Sport");
			header.put("8", "BetAmount");
			header.put("9", "MarketType");
			header.put("10", "BetTime");
			header.put("11", "MatchBegin");
			header.put("12", "BetNo");
			header.put("13", "SeriesValue");
			header.put("14", "Outcome");
			header.put("15", "MatchId");
			header.put("16", "Profit(RMB)");
			header.put("17", "BetAmount(RMB)");

			header.put("19", "device");
			header.put("20", "orderValidBetMoney");
			header.put("21", "BetType");
			header.put("22", "sport");
			header.put("23", "Tournament");
			header.put("24", "MatchInfo");
			header.put("25", "matchId");
			header.put("26", "starTime");
			header.put("27", "PlayName");
			header.put("28", "PlayOption");
			header.put("29", "MarketValue");
			header.put("30", "Odds");
			header.put("31", "BetStatus");
			header.put("32", "ip");
			header.put("33", "IMEI");

			header.put("36", "merchantCode");
			header.put("37", "merchantName");
			header.put("38", "userLevel");
			header.put("39", "seriesAmount");
			header.put("40", "seriesProfit");
			header.put("41", "PreBetAmount(RMB)");
			header.put("42", "vipTime");
			header.put("43", "sumValidBetNo");
			header.put("44", "BetResult");

		}
		return header;
	}

	private Callable<List<OrderSettle>> read2List(final int i, final String filter, final BetOrderVO betOrderVO) {
		long starTime = System.currentTimeMillis();
		Callable<List<OrderSettle>> callable = new Callable<List<OrderSettle>>() {
			@Override
			public List<OrderSettle> call() throws Exception {
				List<OrderSettle> list = new ArrayList<>();
				int startIndex = i * threadSize;
				for (int j = 0; j < 10; j++) {
					BetOrderVO parms = new BetOrderVO();
					BeanUtils.copyProperties(betOrderVO, parms);
					parms.setStart(startIndex + (j * pageSize));
					parms.setSize(pageSize);
					try {
						List<OrderSettle> listResult = processQuery(filter, parms);
						list.addAll(listResult);
					} catch (Exception e) {
						log.error("注单查询导出异常！开始补偿", e);
						int i = 10;
						while (i > 0) {
							i--;
							try {
								List<OrderSettle> listResult = processQuery(filter, parms);
								log.info("read2List list=" + list.size());
								list.addAll(listResult);
								i = 0;
							} catch (Exception e1) {
								log.error("注单查询导出异常！,补偿错误！", e1);
							}
						}
					}
				}
				return list;
			}
		};
		long time = System.currentTimeMillis() - starTime;
		if (time > 300000) {
			log.info("数据库查询订单耗时 {}", time / 1000);
		}
		return callable;
	}

	private Callable<List<OrderSettle>> read2List(final int j, final int i, final String filter, final BetOrderVO betOrderVO) {
		long starTime = System.currentTimeMillis();
		Callable<List<OrderSettle>> callable = new Callable<List<OrderSettle>>() {
			@Override
			public List<OrderSettle> call() throws Exception {
				List<OrderSettle> list = new ArrayList<>();
				int startIndex = i * threadSize + (j * MerchantFileService.SPLIT_FILE_DATE_SIZE);
				for (int k = 0; k < 10; k++) {
					BetOrderVO parms = new BetOrderVO();
					BeanUtils.copyProperties(betOrderVO, parms);
					parms.setStart(startIndex + (k * pageSize));
					parms.setSize(pageSize);
					try {
						List<OrderSettle> listResult = processQuery(filter, parms);
						list.addAll(listResult);
					} catch (Exception e) {
						log.error("注单查询导出异常！开始补偿", e);
						int i = 10;
						while (i > 0) {
							i--;
							try {
								List<OrderSettle> listResult = processQuery(filter, parms);
								log.info("read2List list=" + list.size());
								list.addAll(listResult);
								i = 0;
							} catch (Exception e1) {
								log.error("注单查询导出异常！,补偿错误！", e1);
							}
						}
					}
				}
				return list;
			}
		};
		long time = System.currentTimeMillis() - starTime;
		if (time > 300000) {
			log.info("数据库查询订单耗时 {}", time / 1000);
		}
		return callable;
	}

	protected static Object getObj(Object o) {
		if (o == null) {
			return "";
		}
		return o;
	}

	protected static String getOrderStatus(Integer orderStatus, String language) {
		return language.equals(LanguageEnum.ZS.getCode()) ? orderStatusMap.get(orderStatus) : orderStatusEnMap.get(orderStatus);
	}

	protected static String getPreSettleStatus(Integer orderStatus, String language) {
		return language.equals(LanguageEnum.ZS.getCode()) ? preSettleStatusMap.get(orderStatus) : preSettleStatusEnMap.get(orderStatus);
	}

}
