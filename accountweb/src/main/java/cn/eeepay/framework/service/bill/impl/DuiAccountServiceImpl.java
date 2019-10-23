package cn.eeepay.framework.service.bill.impl;

import cn.eeepay.framework.enums.DuiAccountStatus;
import cn.eeepay.framework.enums.TransStatus;
import cn.eeepay.framework.model.bill.DuiAccountBatch;
import cn.eeepay.framework.model.bill.DuiAccountDetail;
import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.model.nposp.CollectiveTransOrder;
import cn.eeepay.framework.model.nposp.TransInfo;
import cn.eeepay.framework.service.bill.DuiAccountBatchService;
import cn.eeepay.framework.service.bill.DuiAccountDetailService;
import cn.eeepay.framework.service.bill.DuiAccountService;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.service.nposp.CollectionTransOrderService;
import cn.eeepay.framework.service.nposp.TransInfoService;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service("duiAccountService")
public class DuiAccountServiceImpl implements DuiAccountService{
	@Resource
	public SysDictService sysDictService;
	@Resource
	public DuiAccountDetailService duiAccountDetailService;
	@Resource
	public DuiAccountBatchService duiAccountBatchService;
	@Autowired
	public CollectionTransOrderService collectionTransOrderService;
	
	@Autowired
	public TransInfoService transInfoService;
	
	private static final Logger log = LoggerFactory.getLogger(DuiAccountServiceImpl.class);
	
	@Override
	public String doDuiAccountForKqzq(String acqEnname, List<DuiAccountDetail> dbDetails, Map<String, Object> map) {
		try {
			  synchronized(this){
				SysDict sysDict = sysDictService.findSysDictByKeyName("acq_channel_my_settle", acqEnname);
				String mySettle =sysDict.getSysValue();
				DuiAccountBatch checkAccountBatch = new DuiAccountBatch();
				List<DuiAccountDetail> checkAccountDetails = (List<DuiAccountDetail>) map.get("checkAccountDetails");
				String fileName = (String)map.get("fileName");

				String checkFileDate = (String)map.get("checkFileDate");

				DuiAccountBatch batch = duiAccountBatchService.findDuiAccountBatchByFileNameAndAcqEnname(fileName,acqEnname);
				if(batch != null){
					return "1";
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				SimpleDateFormat shortSdf = new SimpleDateFormat("yyyyMMdd");
				int random = (int) (Math.random() * 1000);
				String checkBatchNo =  acqEnname+ shortSdf.format(new Date()) + random;
				checkAccountBatch.setCheckBatchNo(checkBatchNo);
				try {
					checkAccountBatch.setCheckFileDate(sdf.parse(checkFileDate));
				} catch (ParseException e) {
					log.error(acqEnname+"添加对账批次日期转换异常:",e);
				}
				checkAccountBatch.setCheckFileName(fileName);
				checkAccountBatch.setCheckTime(new Date());
				checkAccountBatch.setOperator((String)map.get("uname"));
				
				SysDict acqOrg = sysDictService.findSysDictByKeyValue("sys_acq_org", acqEnname);
				checkAccountBatch.setAcqEnname(acqEnname);
				checkAccountBatch.setAcqCnname(acqOrg.getSysName());
				BigDecimal acqTotalAmount = new BigDecimal("0.00");
				BigDecimal totalAmount = new BigDecimal("0.00");
				for(int i=0; i<checkAccountDetails.size();i++){
					acqTotalAmount = acqTotalAmount.add(checkAccountDetails.get(i).getAcqTransAmount());
				}

				checkAccountBatch.setAcqTotalAmount(acqTotalAmount);

				for(int i=0; i<dbDetails.size();i++){
					totalAmount = totalAmount.add(dbDetails.get(i).getPlateTransAmount());
				}
				checkAccountBatch.setTotalAmount(totalAmount);

				checkAccountBatch.setAcqTotalItems(Long.parseLong(String.valueOf(checkAccountDetails.size())));
				checkAccountBatch.setTotalItems(Long.parseLong(String.valueOf(dbDetails.size())));

				int acqTotalSuccessItems = 0;
				int acqTotalFailedItems = 0;
				int totalSuccessItems = 0;
				int totalFailedItems = 0;
				log.info(acqEnname+"：对账逻辑开始:" );
				for(int i=0; i<checkAccountDetails.size(); i++){			
					DuiAccountDetail detail = checkAccountDetails.get(i);
					detail.setAcqEnname(acqEnname);
					detail.setCheckBatchNo(checkBatchNo);	
					detail.setRecordStatus(2);
					detail.setErrorHandleStatus("pendingTreatment");
					detail.setErrorHandleCreator((String)map.get("uname"));
					for(int j=0; j<dbDetails.size(); j++){
						DuiAccountDetail dbDetail = dbDetails.get(j);
						dbDetail.setAcqEnname(acqEnname);			
						dbDetail.setCheckBatchNo(checkBatchNo);
						dbDetail.setRecordStatus(2);
						dbDetail.setErrorHandleStatus("pendingTreatment");
						dbDetail.setErrorHandleCreator((String)map.get("uname"));
						//设置 从字典表获取mysettle的值
						dbDetail.setMySettle(mySettle);
//						log.info("dbDetail=" + ToStringBuilder.reflectionToString(dbDetail, ToStringStyle.MULTI_LINE_STYLE));
//						log.info("detail=" + ToStringBuilder.reflectionToString(detail, ToStringStyle.MULTI_LINE_STYLE));
						boolean flag = false;
						//对账详情表中对应交易表的  参考号，对账文件的是 ---收单机构系统参考号
						if(detail.getAcqReferenceNo().equals(dbDetail.getPlateAcqReferenceNo())){
							flag = true;
						}
//						log.info("银盛对账，flag的值为：" + flag );
						if(flag){
							installKqZqDetailByInfo(dbDetail, detail);
							BigDecimal transAmount = detail.getAcqTransAmount();
							if(dbDetail.getPlateTransAmount().compareTo(transAmount) == 0){
								detail.setCheckAccountStatus(DuiAccountStatus.SUCCESS.toString());
								dbDetail.setCheckAccountStatus(DuiAccountStatus.SUCCESS.toString());
								dbDetail.setRecordStatus(1);  //对账成功，则记账也成功
								checkAccountDetails.remove(i);
								dbDetails.remove(j);
								acqTotalSuccessItems++;
								totalSuccessItems++;
							}else{
								detail.setCheckAccountStatus(DuiAccountStatus.AMOUNT_FAILED.toString());
								dbDetail.setCheckAccountStatus(DuiAccountStatus.AMOUNT_FAILED.toString());
								checkAccountDetails.remove(i);
								dbDetails.remove(j);
								acqTotalFailedItems++;
								totalFailedItems++;
							}
							duiAccountDetailService.updateDuiAccountDetail(dbDetail);
							i--;
							j--;
							break;
						}
					}
					if(detail.getCheckAccountStatus() == null || detail.getCheckAccountStatus().equals("")){
						String acqReferenceNo = detail.getAcqReferenceNo();
						DuiAccountDetail dbDetail = duiAccountDetailService.queryByAcqDbDetailInfo1(acqReferenceNo,acqEnname);
						if(dbDetail != null){
	                        dbDetail.setAcqEnname(acqEnname);
	                        dbDetail.setCheckBatchNo(checkBatchNo);
	                        dbDetail.setRecordStatus(2);
	                        dbDetail.setErrorHandleStatus("pendingTreatment");
	                        dbDetail.setErrorHandleCreator((String)map.get("uname"));
	                        //设置 从字典表获取mysettle的值
	                        dbDetail.setMySettle(mySettle);

							if(dbDetail.getCheckAccountStatus().equals(DuiAccountStatus.NO_CHECKED) && TransStatus.SUCCESS.toString().equals(dbDetail.getPlateTransStatus())){
								if(dbDetail.getPlateTransAmount().compareTo(detail.getAcqTransAmount())!=0){
									detail.setCheckAccountStatus(DuiAccountStatus.AMOUNT_FAILED.toString());
									dbDetail.setCheckAccountStatus(DuiAccountStatus.AMOUNT_FAILED.toString());
									acqTotalFailedItems++;
									totalFailedItems++;
								}else {
									dbDetail.setCheckAccountStatus(DuiAccountStatus.SUCCESS.toString());
									dbDetail.setRecordStatus(1);  //对账成功，则记账也成功
									checkAccountBatch.setTotalItems(checkAccountBatch.getTotalItems() + 1);
									totalSuccessItems++;
									acqTotalSuccessItems++;
								}
								dbDetail.setCheckBatchNo(checkBatchNo);
								checkAccountDetails.remove(i);
								installKqZqDetailByInfo(dbDetail, detail);
								duiAccountDetailService.updateDuiAccountDetail(dbDetail);
								i--;

							}else{
								detail.setSettleStatus(0);
								detail.setCheckAccountStatus(DuiAccountStatus.ACQ_SINGLE.toString());
								detail.setCheckBatchNo(checkBatchNo);
								checkAccountDetails.remove(i);
								try {
									duiAccountDetailService.insertDuiAccountDetail(detail);
								} catch (Exception e) {
									log.info(acqEnname+":异常信息：" + e.getMessage() );
								}
								acqTotalFailedItems++;
								i--;
							}


						}else{
							//如果数据库中对账详情表中找，如果没有找到，那么去boss的交易表里面找，则新增
							TransInfo transInfo1 = new TransInfo();
							try {
								transInfo1 = transInfoService.getTranDataByAcqReferenceNo(acqReferenceNo);
							} catch (Exception e1) {
								log.info(acqEnname+":重抓数据异常信息：" + e1.getMessage() );
								transInfo1 = null;
							}
							
							log.info(acqEnname+" 上游单边处理方式去交易表里重抓数据：订单参考号为 ：" + detail.getAcqReferenceNo()   );
							if(transInfo1 == null){
								detail.setSettleStatus(0);
								detail.setCheckAccountStatus(DuiAccountStatus.ACQ_SINGLE.toString());
								detail.setCheckBatchNo(checkBatchNo);
								checkAccountDetails.remove(i);
								try {
									duiAccountDetailService.insertDuiAccountDetail(detail);
								} catch (Exception e) {
									log.error("",e);
									log.info(acqEnname+":异常信息：" + e.getMessage() );
								}
								acqTotalFailedItems++;
								i--;
							}else{
								//这里的交易是不管是否成功，都是上游单边
								BigDecimal transAmount1 = detail.getAcqTransAmount();
								if(transInfo1.getTransAmount().compareTo(transAmount1) == 0){
									detail.setCheckAccountStatus(DuiAccountStatus.ACQ_SINGLE.toString());//对上了，但是还是上游单边，达娟说的
									acqTotalFailedItems++;
								}else{
									detail.setCheckAccountStatus(DuiAccountStatus.AMOUNT_FAILED.toString());
									acqTotalFailedItems++;
								}
								//如果找到了这笔交易，说明是由于日切造成的，第2天这笔数据会对帐为平台单边，因为订单参考号不唯一，所以这里只记录平台数据，这种情况理论上不会出现
								if(transInfo1.getTransStatus().equals(TransStatus.SUCCESS.toString())){
									detail.setSettleStatus(0);
									duiAccountDetailService.insertDuiAccountDetail(detail);
								}else{
									duiAccountDetailService.installKqzqDetailByInfo(transInfo1,acqReferenceNo,detail,acqEnname);
								}
								checkAccountDetails.remove(i);
								i--;
							}
							
						}
					}

				}

				if(dbDetails.size() >= 0){
					  for(int i=0; i<dbDetails.size(); i++){
						  DuiAccountDetail dbDetail = dbDetails.get(i);
						  dbDetail.setMySettle(mySettle);
						  if(judgeCheckEd(dbDetail,acqEnname)){
							  checkAccountBatch.setTotalItems(checkAccountBatch.getTotalItems()-1);
							  continue;
						  }
						  dbDetail.setAcqEnname(acqEnname);
						  dbDetail.setCheckBatchNo(checkBatchNo);
						  dbDetail.setCheckAccountStatus(DuiAccountStatus.PLATE_SINGLE.toString());
						  dbDetail.setErrorHandleStatus("pendingTreatment");
						  dbDetail.setErrorHandleCreator((String)map.get("uname"));
						  dbDetail.setRecordStatus(2);
						  duiAccountDetailService.updateDuiAccountDetail(dbDetail);
						  totalFailedItems++;

					  }
				}
				checkAccountBatch.setAcqTotalSuccessItems(Long.parseLong(String.valueOf(acqTotalSuccessItems)));
				checkAccountBatch.setAcqTotalFailedItems(Long.parseLong(String.valueOf(acqTotalFailedItems)));
				checkAccountBatch.setTotalSuccessItems(Long.parseLong(String.valueOf(totalSuccessItems)));
				checkAccountBatch.setTotalFailedItems(Long.parseLong(String.valueOf(totalFailedItems)));

				if(acqTotalFailedItems>0 || totalFailedItems>0){
					checkAccountBatch.setCheckResult(DuiAccountStatus.FAILED.toString());
					checkAccountBatch.setRecordStatus(2);
				}else{
					checkAccountBatch.setCheckResult(DuiAccountStatus.SUCCESS.toString());
					checkAccountBatch.setRecordStatus(2);
				}
				checkAccountBatch.setCreateTime(new Date());
				duiAccountBatchService.insertDuiAccountBatch(checkAccountBatch);
			  }
			} catch (Exception e) {
	            log.error("",e);
				throw new RuntimeException(acqEnname+":对账出现异常！回滚！" +e.getMessage());
			}
			return "0";	
	}
	
	/**
	 * 快钱
	 * @param dbDetail
	 * @param detail
	 */
	private void installKqZqDetailByInfo(DuiAccountDetail dbDetail, DuiAccountDetail detail) {
		dbDetail.setAcqTransSerialNo(detail.getAcqTransSerialNo());
		dbDetail.setAcqTerminalNo(detail.getAcqTerminalNo());
		dbDetail.setAcqMerchantNo(detail.getAcqMerchantNo());
		dbDetail.setAcqMerchantName(detail.getAcqMerchantName());
		dbDetail.setAccessOrgNo(detail.getAccessOrgNo());
		dbDetail.setAccessOrgName(detail.getAccessOrgName());
		dbDetail.setAcqBatchNo(detail.getAcqBatchNo());
		dbDetail.setAcqSerialNo(detail.getAcqSerialNo());
		dbDetail.setAcqAccountNo(detail.getAcqAccountNo());
		dbDetail.setAcqCardSequenceNo(detail.getAcqCardSequenceNo());

		dbDetail.setAcqTransTime(detail.getAcqTransTime());
		dbDetail.setAcqReferenceNo(detail.getAcqReferenceNo());
//		dbDetail.setOrderReferenceNo(detail.getOrderReferenceNo());// 订单参考号
		dbDetail.setAcqSettleDate(detail.getAcqSettleDate());
		dbDetail.setAcqTransCode(detail.getAcqTransCode());
		dbDetail.setAcqTransStatus(detail.getAcqTransStatus());
		dbDetail.setAcqTransAmount(detail.getAcqTransAmount());
		dbDetail.setAcqRefundAmount(detail.getAcqRefundAmount());
		dbDetail.setAcqCheckDate(detail.getAcqCheckDate());
		dbDetail.setCreateTime(detail.getCreateTime());

	}


	@Override
	public String doDuiAccountForKqZg(String acqEnname, List<DuiAccountDetail> dbDetails, Map<String, Object> map) {
		try {
			synchronized(this){
				Map<String, DuiAccountDetail> dbDetailsMap = dbDetails.stream().collect(Collectors.toMap((it) -> it.getPlateOrderNo(), a -> a, (k1, k2) -> k1));
				DuiAccountBatch checkAccountBatch = new DuiAccountBatch();
				List<DuiAccountDetail> checkAccountDetails = (List<DuiAccountDetail>) map.get("checkAccountDetails");
				String fileName = (String)map.get("fileName");

				String checkFileDate = (String)map.get("checkFileDate");

				DuiAccountBatch batch = duiAccountBatchService.findDuiAccountBatchByFileNameAndAcqEnname(fileName,acqEnname);
				if(batch != null){
					return "1";
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

				SimpleDateFormat shortSdf = new SimpleDateFormat("yyyyMMdd");
				int random = (int) (Math.random() * 1000);
				String checkBatchNo =  acqEnname+ shortSdf.format(new Date()) + random;
				checkAccountBatch.setCheckBatchNo(checkBatchNo);
				try {
					checkAccountBatch.setCheckFileDate(sdf.parse(checkFileDate));
				} catch (ParseException e) {
					log.error(acqEnname+"添加对账批次日期转换异常:",e);
				}
				checkAccountBatch.setCheckFileName(fileName);
				checkAccountBatch.setCheckTime(new Date());
				checkAccountBatch.setOperator((String)map.get("uname"));
				UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
						.getAuthentication()
						.getPrincipal();
				SysDict acqOrg = sysDictService.findSysDictByKeyValue("sys_acq_org", acqEnname);
				checkAccountBatch.setAcqEnname(acqEnname);
				checkAccountBatch.setAcqCnname(acqOrg.getSysName());
				BigDecimal acqTotalAmount = new BigDecimal("0.00");
				BigDecimal totalAmount = new BigDecimal("0.00");
				for(int i=0; i<checkAccountDetails.size();i++){
					acqTotalAmount = acqTotalAmount.add(checkAccountDetails.get(i).getAcqTransAmount());
				}

				checkAccountBatch.setAcqTotalAmount(acqTotalAmount);

				for(int i=0; i<dbDetails.size();i++){
					totalAmount = totalAmount.add(dbDetails.get(i).getPlateTransAmount());
				}
				checkAccountBatch.setTotalAmount(totalAmount);

				checkAccountBatch.setAcqTotalItems(Long.parseLong(String.valueOf(checkAccountDetails.size())));
				checkAccountBatch.setTotalItems(Long.parseLong(String.valueOf(dbDetails.size())));

				log.info(acqEnname+"：对账逻辑开始:" );
				List<DuiAccountDetail> tempcheckAccountDetails = new ArrayList<>();
				tempcheckAccountDetails.addAll(checkAccountDetails);

				AtomicLong acqTotalSuccessItemsAtomic = new AtomicLong(0);
				AtomicLong totalSuccessItemsAtomic = new AtomicLong(0);
				AtomicLong acqTotalFailedItemsAtomic = new AtomicLong(0);
				AtomicLong totalFailedItemsAtomic = new AtomicLong(0);

				log.info("{}开始遍历对账文件",acqEnname);
				long startTime=System.currentTimeMillis();

				// 每10000条数据开启一条线程
				int threadSize = 10000;
				// 总数据条数
				int dataSize = checkAccountDetails.size();
				// 线程数
				int threadNum = dataSize / threadSize + 1;
				log.info("{}对账分配线程数：{}",acqEnname,threadNum);
				// 定义标记,过滤threadNum为整数
				boolean special = dataSize % threadSize == 0;

				// 创建一个线程池
				ExecutorService exec = Executors.newFixedThreadPool(threadNum);
				// 定义一个任务集合
				List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
				Callable<Integer> task = null;
				List<DuiAccountDetail> cutList = null;

				// 确定每条线程的数据
				for (int i = 0; i < threadNum; i++) {
					if (i == threadNum - 1) {
						if (special) {
							break;
						}
						cutList = checkAccountDetails.subList(threadSize * i, dataSize);
					} else {
						cutList = checkAccountDetails.subList(threadSize * i, threadSize * (i + 1));
					}
					final List<DuiAccountDetail> listStr = cutList;
					task = new Callable<Integer>() {
						@Override
						public Integer call() throws Exception {
							listStr.stream().forEach(it -> {
								DuiAccountDetail detail = it;
								//log.info("文件订单参考号：" + detail.getOrderReferenceNo() );

								detail.setAcqEnname(acqEnname);
								detail.setCheckBatchNo(checkBatchNo);
								detail.setRecordStatus(2);
								detail.setErrorHandleStatus("pendingTreatment");
								detail.setErrorHandleCreator((String)map.get("uname"));
								//JDK8已优化字符串相加，无需使用StringBuffer
								String key = it.getAcqMerchantOrderNo();
								DuiAccountDetail dbDetail = dbDetailsMap.get(key);
								if(dbDetail != null){
									dbDetail.setAcqEnname(acqEnname);
									dbDetail.setCheckBatchNo(checkBatchNo);
									dbDetail.setRecordStatus(2);
									dbDetail.setErrorHandleStatus("pendingTreatment");
									dbDetail.setErrorHandleCreator((String)map.get("uname"));

									installCreditZgZqDetailByInfo(dbDetail, detail);
									BigDecimal transAmount = detail.getAcqTransAmount();

									if(dbDetail.getPlateTransAmount().compareTo(transAmount) == 0){
										detail.setCheckAccountStatus(DuiAccountStatus.SUCCESS.toString());
										dbDetail.setCheckAccountStatus(DuiAccountStatus.SUCCESS.toString());
										dbDetail.setRecordStatus(1);  //对账成功，则记账也成功
										tempcheckAccountDetails.remove(it);
										acqTotalSuccessItemsAtomic.incrementAndGet();		//对账成功笔数
										totalSuccessItemsAtomic.incrementAndGet();
										//checkAccountDetailService.updateAcclStatus(info.getId());//交易表增加对账状态20160217

										//transInfoService.updateTransInfoAccById(dbDetail.getPlateTransId().toString());
									}else{
										detail.setCheckAccountStatus(DuiAccountStatus.AMOUNT_FAILED.toString());
										dbDetail.setCheckAccountStatus(DuiAccountStatus.AMOUNT_FAILED.toString());
										tempcheckAccountDetails.remove(it);
										acqTotalFailedItemsAtomic.incrementAndGet();		//对账失败笔数
										totalFailedItemsAtomic.incrementAndGet();
									}
									try {
										duiAccountDetailService.updateDuiAccountDetail(dbDetail);
									} catch (Exception e) {
										e.printStackTrace();
									}
									dbDetailsMap.remove(key);

								}

								if(detail.getCheckAccountStatus() == null || detail.getCheckAccountStatus().equals("")){
									String orderNo = detail.getAcqOrderNo();
									DuiAccountDetail dbNewDetail = duiAccountDetailService.queryByAcqDbDetailInfoNoChecked(orderNo,acqEnname);
									if(dbNewDetail != null){
										dbNewDetail.setAcqEnname(acqEnname);
										dbNewDetail.setCheckBatchNo(checkBatchNo);
										dbNewDetail.setRecordStatus(2);
										dbNewDetail.setErrorHandleStatus("pendingTreatment");
										dbNewDetail.setErrorHandleCreator((String)map.get("uname"));

										if(dbNewDetail.getCheckAccountStatus().equals(DuiAccountStatus.NO_CHECKED) && TransStatus.SUCCESS.toString().equals(dbNewDetail.getPlateTransStatus())){
											if(dbNewDetail.getPlateTransAmount().compareTo(detail.getAcqTransAmount())!=0){
												detail.setCheckAccountStatus(DuiAccountStatus.AMOUNT_FAILED.toString());
												dbNewDetail.setCheckAccountStatus(DuiAccountStatus.AMOUNT_FAILED.toString());
												acqTotalFailedItemsAtomic.incrementAndGet();
												totalFailedItemsAtomic.incrementAndGet();
											}else {
												dbNewDetail.setCheckAccountStatus(DuiAccountStatus.SUCCESS.toString());
												dbNewDetail.setRecordStatus(1);  //对账成功，则记账也成功
												checkAccountBatch.setTotalItems(checkAccountBatch.getTotalItems() + 1);
												totalSuccessItemsAtomic.incrementAndGet();
												acqTotalSuccessItemsAtomic.incrementAndGet();
												//transInfoService.updateTransInfoAccById(dbDetail.getPlateTransId().toString());
											}
											dbNewDetail.setCheckBatchNo(checkBatchNo);
											tempcheckAccountDetails.remove(it);
											installCreditZgZqDetailByInfo(dbNewDetail, detail);
											try {
												duiAccountDetailService.updateDuiAccountDetail(dbNewDetail);
											} catch (Exception e) {
												e.printStackTrace();
											}
										}else{
											detail.setSettleStatus(0);
											detail.setCheckAccountStatus(DuiAccountStatus.ACQ_SINGLE.toString());
											detail.setCheckBatchNo(checkBatchNo);
											tempcheckAccountDetails.remove(it);
											try {
												duiAccountDetailService.insertDuiAccountDetail(detail);
											} catch (Exception e) {
												log.info(acqEnname+":异常信息：" + e.getMessage() );
											}
											acqTotalFailedItemsAtomic.incrementAndGet();
										}
									}else{			//上游单边
										detail.setSettleStatus(0);
										detail.setCheckAccountStatus(DuiAccountStatus.ACQ_SINGLE.toString());
										detail.setCheckBatchNo(checkBatchNo);
										tempcheckAccountDetails.remove(it);
										try {
											duiAccountDetailService.insertDuiAccountDetail(detail);
										} catch (Exception e) {
											log.error("",e);
											log.info(acqEnname+":异常信息：" + e.getMessage() );
										}
										acqTotalFailedItemsAtomic.incrementAndGet();
									}
								}

							});
							return 1;
						}
					};
					// 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
					tasks.add(task);
				}

				List<Future<Integer>> results = exec.invokeAll(tasks);

				for (Future<Integer> future : results) {
					future.get();		//阻塞，等任务结束
				}

				// 关闭线程池
				exec.shutdown();

				long endTime=System.currentTimeMillis();
				float excTime=(float)(endTime-startTime)/1000;
				log.info("{}遍历对账文件结束,执行时长{}秒",acqEnname,excTime);

				log.info("{}判断是否有平台单边数据，有则开始遍历",acqEnname);
				startTime=System.currentTimeMillis();
				dbDetailsMap.forEach((K,V) -> {			//平台单边
					DuiAccountDetail dbDetail = V;
					if(judgeCheckEd(dbDetail,acqEnname)){
						checkAccountBatch.setTotalItems(checkAccountBatch.getTotalItems()-1);
					}else{
						dbDetail.setAcqEnname(acqEnname);
						dbDetail.setCheckBatchNo(checkBatchNo);
						dbDetail.setCheckAccountStatus(DuiAccountStatus.PLATE_SINGLE.toString());
						dbDetail.setErrorHandleStatus("pendingTreatment");
						dbDetail.setErrorHandleCreator((String)map.get("uname"));
						dbDetail.setRecordStatus(2);
						try {
							duiAccountDetailService.updateDuiAccountDetail(dbDetail);
						} catch (Exception e) {
							e.printStackTrace();
						}
						totalFailedItemsAtomic.incrementAndGet();
					}
				});
				endTime=System.currentTimeMillis();
				excTime=(float)(endTime-startTime)/1000;
				log.info("{}判断是否有平台单边数据结束,执行时长{}秒",acqEnname,excTime);

				checkAccountBatch.setAcqTotalSuccessItems(acqTotalSuccessItemsAtomic.get());
				checkAccountBatch.setAcqTotalFailedItems(acqTotalFailedItemsAtomic.get());
				checkAccountBatch.setTotalSuccessItems(totalSuccessItemsAtomic.get());
				checkAccountBatch.setTotalFailedItems(totalFailedItemsAtomic.get());

				if(acqTotalFailedItemsAtomic.get()>0 || totalFailedItemsAtomic.get()>0){
					checkAccountBatch.setCheckResult(DuiAccountStatus.FAILED.toString());
					checkAccountBatch.setRecordStatus(2);
				}else{
					checkAccountBatch.setCheckResult(DuiAccountStatus.SUCCESS.toString());
					checkAccountBatch.setRecordStatus(2);
				}
				checkAccountBatch.setCreateTime(new Date());
				duiAccountBatchService.insertDuiAccountBatch(checkAccountBatch);

			}
		} catch (Exception e) {
			log.error("",e);
			throw new RuntimeException(acqEnname+":对账出现异常！回滚！" +e.getMessage());
		}
		return "0";
	}


	private void installCreditZgZqDetailByInfo(DuiAccountDetail dbDetail, DuiAccountDetail detail) {
		dbDetail.setAcqMerchantNo(detail.getAcqMerchantNo());
		dbDetail.setAcqMerchantName(detail.getAcqMerchantName());
		dbDetail.setAcqOrderNo(detail.getAcqOrderNo());
		dbDetail.setAcqTransType(detail.getAcqTransType());
		dbDetail.setAcqTransAmount(detail.getAcqTransAmount());
		dbDetail.setAcqTransStatus(detail.getAcqTransStatus());
		dbDetail.setAcqTransTime(detail.getAcqTransTime());
		dbDetail.setAcqRefundAmount(detail.getAcqRefundAmount());
	}



	
	/**
	 * 判断该交易是否已经对账（避免由于时间差异造成的单笔记录）
	 * 
	 * */
	private boolean judgeCheckEd(DuiAccountDetail detail,String acqEnname){
		DuiAccountDetail dbDetail = null;
		try {
			String acqOrderNo = detail.getAcqMerchantOrderNo();
			dbDetail = duiAccountDetailService.queryByAcqDbDetailInfo(acqOrderNo, acqEnname);
		} catch (Exception e) {
			log.error("异常:",e);
		}
		if(dbDetail == null){
			return false;
		}
		return true;
	}
}
