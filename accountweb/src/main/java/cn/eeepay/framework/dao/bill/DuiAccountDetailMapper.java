package cn.eeepay.framework.dao.bill;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.*;
import cn.eeepay.framework.model.nposp.TransInfo;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 对账明细表
 * @author Administrator
 *
 */
public interface DuiAccountDetailMapper {
	/**
	 * 新增对账明细
	 * @return
	 */
	@Insert("insert into check_account_detail(acq_trans_serial_no,acq_merchant_no,acq_merchant_name,acq_terminal_no,access_org_no,access_org_name,acq_batch_no,acq_serial_no,acq_account_no,acq_card_sequence_no,"
			+ "acq_trans_time,acq_reference_no,acq_settle_date,acq_trans_code,acq_trans_status,acq_trans_amount,acq_refund_amount,acq_check_date,acq_ori_trans_serial_no,acq_enname,"
			+ "plate_trans_id,plate_acq_merchant_no,plate_acq_terminal_no,plate_merchant_no,plate_terminal_no,plate_acq_batch_no,plate_acq_serial_no,plate_batch_no,plate_serial_no,plate_account_no,"
			+ "plate_trans_amount,plate_acq_merchant_fee,plate_merchant_fee,plate_agent_fee,plate_acq_merchant_rate,plate_merchant_rate,plate_acq_reference_no,plate_acq_trans_time,plate_merchant_settle_date,plate_trans_type,"
			+ "plate_trans_status,check_account_status,check_batch_no,mend_batch_no,description,create_time,plate_agent_no,plate_trans_source,my_settle,bag_settle,pos_type,error_handle_status,error_handle_creator,plate_agent_share_amount,"
			+ "record_status,settlement_method,settle_status,account,acq_merchant_order_no,"
			+ "acq_trans_order_no,acq_trans_type,acq_order_no,order_reference_no,acq_auth_no,plate_card_no,freeze_status,is_add_bill,out_bill_status,settle_type,plate_order_no)"
			+"values(#{dui.acqTransSerialNo},#{dui.acqMerchantNo},#{dui.acqMerchantName},#{dui.acqTerminalNo},#{dui.accessOrgNo},#{dui.accessOrgName},#{dui.acqBatchNo},#{dui.acqSerialNo},#{dui.acqAccountNo},#{dui.acqCardSequenceNo},"
			+ "#{dui.acqTransTime},#{dui.acqReferenceNo},#{dui.acqSettleDate},#{dui.acqTransCode},#{dui.acqTransStatus},#{dui.acqTransAmount},#{dui.acqRefundAmount},#{dui.acqCheckDate},#{dui.acqOriTransSerialNo},#{dui.acqEnname},"
			+ "#{dui.plateTransId},#{dui.plateAcqMerchantNo},#{dui.plateAcqTerminalNo},#{dui.plateMerchantNo},#{dui.plateTerminalNo},#{dui.plateAcqBatchNo},#{dui.plateAcqSerialNo},#{dui.plateBatchNo},#{dui.plateSerialNo},#{dui.plateAccountNo},"
			+ "#{dui.plateTransAmount},#{dui.plateAcqMerchantFee},#{dui.plateMerchantFee},#{dui.plateAgentFee},#{dui.plateAcqMerchantRate},#{dui.plateMerchantRate},#{dui.plateAcqReferenceNo},#{dui.plateAcqTransTime},#{dui.plateMerchantSettleDate},#{dui.plateTransType},"
			+ "#{dui.plateTransStatus},#{dui.checkAccountStatus},#{dui.checkBatchNo},#{dui.mendBatchNo},#{dui.description},#{dui.createTime},#{dui.plateAgentNo},#{dui.plateTransSource},#{dui.mySettle},#{dui.bagSettle},#{dui.posType},"
			+ "#{dui.errorHandleStatus},#{dui.errorHandleCreator},#{dui.plateAgentShareAmount},#{dui.recordStatus},#{dui.settlementMethod},#{dui.settleStatus},#{dui.account},#{dui.acqMerchantOrderNo},"
			+ "#{dui.acqTransOrderNo},#{dui.acqTransType},#{dui.acqOrderNo},#{dui.orderReferenceNo},#{dui.acqAuthNo},#{dui.plateCardNo},#{dui.freezeStatus},#{dui.isAddBill},#{dui.outBillStatus},#{dui.settleType},#{dui.plateOrderNo})"
			)
	int insertDuiAccountDetail(@Param("dui")DuiAccountDetail dui);
	
	
	@UpdateProvider(type = SqlProvider.class, method = "updateDuiAccountDetail")
	int updateDuiAccountDetail(@Param("dui")DuiAccountDetail dui);

	@UpdateProvider(type = SqlProvider.class, method = "updatePlateDuiAccountDetail")
	int updatePlateAcqDuiAccountDetail(@Param("dui")DuiAccountDetail dui);

	
	@Update("<script>"
			+ " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"\" "
			+ "         close=\"\" separator=\";\"> "
			+ "         update check_account_detail  "
			+ "         set acq_trans_serial_no=#{item.acqTransSerialNo}, "
			+ " 	    acq_merchant_no=#{item.acqMerchantNo}, "
			+ " 	    acq_merchant_name=#{item.acqMerchantName}, "
			+ " 	    acq_terminal_no=#{item.acqTerminalNo}, "
			+ " 		access_org_no=#{item.accessOrgNo}, "
			+ " 		access_org_name=#{item.accessOrgName}, "
			+ " 		acq_batch_no=#{item.acqBatchNo}, "
			+ " 		acq_serial_no=#{item.acqSerialNo}, "
			+ " 		acq_account_no=#{item.acqAccountNo}, "
			+ " 		acq_card_sequence_no=#{item.acqCardSequenceNo}, "
			+ " 		acq_trans_time=#{item.acqTransTime}, "
			+ " 		acq_reference_no=#{item.acqReferenceNo}, "
			+ " 		acq_settle_date=#{item.acqSettleDate}, "
			+ " 		acq_trans_code=#{item.acqTransCode}, "
			+ " 		acq_trans_status=#{item.acqTransStatus}, "
			+ " 		acq_trans_amount=#{item.acqTransAmount}, "
			+ " 		acq_refund_amount=#{item.acqRefundAmount}, "
			+ " 		acq_check_date=#{item.acqCheckDate}, "
			+ " 		acq_ori_trans_serial_no=#{item.acqOriTransSerialNo}, "
			+ " 		acq_enname=#{item.acqEnname}, "
			+ " 		plate_trans_id=#{item.plateTransId}, "
			+ " 		plate_acq_merchant_no=#{item.plateAcqMerchantNo}, "
			+ " 		plate_acq_terminal_no=#{item.plateAcqTerminalNo}, "
			+ " 		plate_merchant_no=#{item.plateMerchantNo}, "
			+ " 		plate_terminal_no=#{item.plateTerminalNo}, "
			+ " 		plate_acq_batch_no=#{item.plateAcqBatchNo}, "
			+ " 		plate_acq_serial_no=#{item.plateAcqSerialNo}, "
			+ " 		plate_batch_no=#{item.plateBatchNo}, "
			+ " 		plate_serial_no=#{item.plateSerialNo}, "
			+ " 		plate_account_no=#{item.plateAccountNo}, "
			+ " 		plate_trans_amount=#{item.plateTransAmount}, "
			+ " 		plate_acq_merchant_fee=#{item.plateAcqMerchantFee}, "
			+ " 		plate_merchant_fee=#{item.plateMerchantFee}, "
			+ " 		plate_acq_merchant_fee=#{item.plateAcqMerchantFee}, "
			+ " 		plate_agent_fee=#{item.plateAgentFee}, "
			+ " 		plate_acq_merchant_rate=#{item.plateAcqMerchantRate}, "
			+ " 		plate_merchant_rate=#{item.plateMerchantRate}, "
			+ " 		plate_acq_reference_no=#{item.plateAcqReferenceNo}, "
			+ " 		plate_acq_trans_time=#{item.plateAcqTransTime}, "
			+ " 		plate_merchant_settle_date=#{item.plateMerchantSettleDate}, "
			+ " 		plate_trans_type=#{item.plateTransType}, "
			+ " 		plate_trans_status=#{item.plateTransStatus}, "
			+ " 		check_account_status=#{item.checkAccountStatus}, "
			+ " 		check_batch_no=#{item.checkBatchNo}, "
			+ " 		mend_batch_no=#{item.mendBatchNo}, "
			+ " 		description=#{item.description}, "
			+ " 		create_time=#{item.createTime}, "
			+ " 		plate_agent_no=#{item.plateAgentNo}, "
			+ " 		plate_trans_source=#{item.plateTransSource}, "
			+ " 		my_settle=#{item.mySettle}, "
			+ " 		bag_settle=#{item.bagSettle}, "
			+ " 		pos_type=#{item.posType}, "
			+ " 		error_handle_status=#{item.errorHandleStatus}, "
			+ " 		error_msg=#{item.errorMsg}, "
			+ " 		plate_agent_share_amount=#{item.plateAgentShareAmount}, "
			+ " 		record_status=#{item.recordStatus}, "
			+ " 		remark=#{item.remark}, "
			+ " 		settlement_method=#{item.settlementMethod}, "
			+ " 		settle_status=#{item.settleStatus}, "
			+ " 		account=#{item.account}, "
			+ " 		error_handle_creator=#{item.errorHandleCreator}, "
			+ " 		acq_merchant_order_no=#{item.acqMerchantOrderNo}, "
			+ " 		acq_order_no=#{item.acqOrderNo}, "
			+ " 		acq_trans_type=#{item.acqTransType}, "
			+ " 		acq_trans_order_no=#{item.acqTransOrderNo}, "
			+ " 		plate_order_time=#{item.plateOrderTime}, "
			+ " 		plate_card_no=#{item.plateCardNo}, "
			+ " 		acq_auth_no=#{item.acqAuthNo}, "
			+ " 		out_account_bill_method=#{item.outAccountBillMethod}, "
			+ " 		is_add_bill=#{item.isAddBill}, "
			+ " 		freeze_status=#{item.freezeStatus}, "
			+ " 		out_bill_status=#{item.outBillStatus}, "
			+ " 		settle_type=#{item.settleType} "
			+ " 		where plate_order_no=#{item.plateOrderNo} "
			+ "     </foreach> "
            + " </script>")
	int updateDuiAccountDetailBatchByPlateOrderNo(@Param("list")List<DuiAccountDetail> list);
	
	@Select("select * from check_account_detail ")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	List<DuiAccountDetail> findAllDuiAccountDetailList();
	
	
	@Select("select * from check_account_detail where check_batch_no = #{checkBatchNo}")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	List<DuiAccountDetail> findDuiAccountDetailListByCheckBatchNo(@Param("checkBatchNo")String checkBatchNo);
	
	
	@Update("update check_account_detail set "
			+ "is_add_bill = #{isAddBill} "
			+ "where order_reference_no = #{subOutBillDetail.orderReferenceNo}")
	int updateDuiAccountDetailByOrderReNum(@Param("subOutBillDetail")SubOutBillDetail subOutBillDetail , @Param("isAddBill")String isAddBill);

	@Update("update check_account_detail set db_cut_flag = #{dbCutFlag} where id = #{id}")
	int updateDuiAccCut(@Param("id")Integer id,@Param("dbCutFlag")String dbCutFlag);

	@Update("update check_account_detail set treatment_method = #{treatmentMethod},freeze_status=#{freezeStatus} where id = #{id}")
	int updateDuiAccError(@Param("id")Integer id,@Param("treatmentMethod")String treatmentMethod,@Param("freezeStatus")String freezeStatus);

	@Update("update check_account_detail set error_handle_status=#{dui.errorHandleStatus},error_msg=#{dui.errorMsg},record_status=#{dui.recordStatus},error_time=now() where id=#{dui.id}")
	int updateDuiAccount(@Param("dui")DuiAccountDetail dui);
	
	@Delete("delete from check_account_detail where check_batch_no = #{checkBatchNo} and check_account_status = 'ACQ_SINGLE' ")
	int deleteByCheckBatchNo(@Param("checkBatchNo")String checkBatchNo);
	
	@Delete("delete from check_account_detail where id = #{id} ")
	int deleteDuiAccountDetail(@Param("id")Integer id);
	
	@Delete("delete from check_account_detail where acq_merchant_no = #{acqMerchantNo} and acq_terminal_no = #{acqTerminalNo} and acq_batch_no = #{acqBatchNo}"
			+ " and acq_serial_no = #{acqSerialNo} and acq_account_no = #{acqAccountNo} ")
	int deleteDuiAccountDetailByParams(@Param("acqMerchantNo")String acqMerchantNo,@Param("acqTerminalNo")String acqTerminalNo,@Param("acqBatchNo")String acqBatchNo,
			@Param("acqSerialNo")String acqSerialNo,@Param("acqAccountNo")String acqAccountNo);
	
	
	@Select("select * from check_account_detail where plate_acq_trans_time between #{startTime} and #{endTime}")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	List<DuiAccountDetail> findDuiAccountDetailListByStartEndTransTime(@Param("startTime")String startTime,@Param("endTime")String endTime);
	
	
	@Select("select * from check_account_detail where DATE_FORMAT(plate_acq_trans_time, '%Y-%m-%d') = DATE_FORMAT(#{transTime}, '%Y-%m-%d') ")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	List<DuiAccountDetail> findDuiAccountDetailListByTransTime(@Param("transTime")String transTime);
	
//	@Select("select * from check_account_detail where check_account_status='NO_CHECKED' "
//			+ " and DATE_FORMAT(plate_acq_trans_time, '%Y-%m-%d') = DATE_FORMAT(#{transTime}, '%Y-%m-%d') ")
//	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
//	List<DuiAccountDetail> findNoCheckedDuiAccountDetailListByTransTime(@Param("transTime")String transTime);

	@Select("select * from check_account_detail where check_account_status='NO_CHECKED' "
			+ " AND plate_acq_trans_time between #{date1} and #{date2} ")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	List<DuiAccountDetail> findNoCheckedDuiAccountDetailListByTransTime(@Param("date1")String date1,@Param("date2")String date2);
	
	@Select("select * from check_account_detail where plate_acq_merchant_no = #{transInfo.acqMerchantNo} and plate_acq_terminal_no = #{transInfo.acqTerminalNo} "
			+ " and plate_acq_batch_no = #{transInfo.acqBatchNo} and plate_acq_serial_no = #{transInfo.acqSerialNo} "
			+ " and plate_account_no = #{transInfo.accountNo} and check_account_status = 'SUCCESS'")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	DuiAccountDetail findDuiAccountDetailByTransInfo(@Param("transInfo")TransInfo transInfo);
	
	@Select("select * from check_account_detail where check_batch_no = #{checkBatchNo} and check_account_status != 'SUCCESS' and record_status in (0,2) and (error_handle_status='pendingTreatment')")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	List<DuiAccountDetail> findErrorDuiAccountDetailList(@Param("checkBatchNo")String checkBatchNo);

	@Select("select * from check_account_detail where  order_reference_no = #{orderReferenceNo} and acq_enname= #{acqEnname} and check_account_status='ACQ_SINGLE'")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	DuiAccountDetail findError(@Param("orderReferenceNo")String orderReferenceNo,@Param("acqEnname")String acqEnname);

	@SelectProvider(type=SqlProvider.class,method="queryAcqDetail")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	DuiAccountDetail findAcq(@Param("dui")DuiAccountDetail dui);

	@SelectProvider(type=SqlProvider.class,method="queryPlateDetail")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	DuiAccountDetail findPlate(@Param("dui")DuiAccountDetail dui);

	@Select("select * from check_account_detail where check_batch_no = #{checkBatchNo} and error_handle_status != #{status}")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	List<DuiAccountDetail> findByCheckBatchNoAndErrorHandleSatus(@Param("checkBatchNo")String checkBatchNo, @Param("status")String status);

	@Select("select * from check_account_detail where check_batch_no = #{checkBatchNo} and check_account_status != #{checkAccountStatus}")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	List<DuiAccountDetail> findOrderReferenceNo(@Param("checkBatchNo")String checkBatchNo, @Param("checkAccountStatus")String checkAccountStatus);
	/**
	 * 查询对账详细信息列表
	 * @param duiAccountDetail
	 * @param sort
	 * @param page
	 * @return
	 */
	@SelectProvider(type=SqlProvider.class,method="findDuiAccountDetailList")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	List<DuiAccountDetail> findDuiAccountDetailList1(@Param("createTimeStart")String createTimeStart,@Param("createTimeEnd")String createTimeEnd,
			@Param("duiAccountDetail")DuiAccountDetail duiAccountDetail, @Param("sort")Sort sort, Page<DuiAccountDetail> page);
	/**
	 * 查询错误对账详细信息列表
	 * @param duiAccountDetail
	 * @param sort
	 * @param page
	 * @return
	 */
	@SelectProvider( type=SqlProvider.class,method="findErrorDuiAccountDetailList")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	List<DuiAccountDetail> findErrorDuiAccountDetailList1(@Param("createTimeStart")String createTimeStart,@Param("createTimeEnd")String createTimeEnd,
			@Param("duiAccountDetail")DuiAccountDetail duiAccountDetail, @Param("sort")Sort sort, Page<DuiAccountDetail> page);

	@SelectProvider(type=SqlProvider.class,method="queryByAcqDbDetails")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	DuiAccountDetail queryByAcqDbDetail(@Param("acqReferenceNo")String acqReferenceNo,@Param("acqMerchantNo")String acqMerchantNo,@Param("acqEnname")String acqEnname,@Param("acqSerNo")String acqSerNo,@Param("acqTerNo")String acqTerNo);
	/**
	 * 查询对账详细信息（用来导出）
	 * @param duiAccountDetail
	 * @return
	 */
	@SelectProvider( type=SqlProvider.class,method="findExportDuiAccountDetailList")
	@ResultType(DuiAccountDetail.class)
	List<DuiAccountDetail> findExportDuiAccountDetailList(@Param("createTimeStart")String createTimeStart,@Param("createTimeEnd")String createTimeEnd,
			@Param("duiAccountDetail")DuiAccountDetail duiAccountDetail);

	/**
	 * 查询对账详细信息（用来导出）
	 * @param duiAccountDetail
	 * @return
	 */
	@SelectProvider( type=SqlProvider.class,method="findErrorExportDuiAccountDetailList")
	@ResultType(DuiAccountDetail.class)
	List<DuiAccountDetail> findErrorExportDuiAccountDetailList(@Param("createTimeStart")String createTimeStart,@Param("createTimeEnd")String createTimeEnd,
														  @Param("duiAccountDetail")DuiAccountDetail duiAccountDetail);
	
	//===========================对账差错处理的六个按钮具体功能的实现开始====================
	//查询对账详细信息 通过  id
	@Select("SELECT * FROM check_account_detail WHERE id = #{id}")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	DuiAccountDetail findDuiAccountDetailById(@Param("id")String id) ;
	
	//查询交易订单号是否存在表  外部账户交易明细
	@Select("SELECT * FROM ext_trans_info WHERE trans_order_no = #{transOrderNo}")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtTransInfoMapper.BaseResultMap")
	List<ExtTransInfo> findExtTransInfoByOrderNo(@Param("transOrderNo")String transOrderNo) ;
	
	//查询冻结解冻记录是否存在   通过    记账流水号
	@Select("SELECT * FROM ext_account_op_record WHERE serial_no = #{serialNo}")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtAccountOpRecordMapper.BaseResultMap")
	ExtAccountOpRecord findOpRecordBySerialNo(@Param("serialNo")String serialNo) ;
	
	//插入一条外部账户冻结解冻记录      <可无>
	@Insert("INSERT INTO ext_account_op_record(account_no,record_date,serial_no,operation_type,freeze_balance,trans_order_no,summary_info) "
			+ "VALUE(#{opRecord.accountNo},#{opRecord.recordDate},#{opRecord.serialNo},#{opRecord.operationType},#{opRecord.freezeBalance},#{opRecord.transOrderNo},#{opRecord.summaryInfo})")
	int insertExtAccountOpRecord(@Param("opRecord")ExtAccountOpRecord opRecord) ;
	
	//查询冻结解冻记录   通过    交易订单号
	@Select("SELECT * FROM ext_account_op_record WHERE trans_order_no = #{transOrderNo}")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtAccountOpRecordMapper.BaseResultMap")
	ExtAccountOpRecord findOpRecordByTransOrderNo(@Param("transOrderNo")String transOrderNo) ;
	
	//更新(解冻)外部账户冻结解冻记录      <可无>
	@Update("UPDATE ext_account_op_record SET operation_type=#{operationType},summary_info=#{summaryInfo} WHERE trans_order_no=#{transOrderNo} ")
	int updateExtAccountOpRecord(@Param("operationType")String operationType ,@Param("summaryInfo")String summaryInfo ,@Param("transOrderNo")String transOrderNo) ;
	
	@Update("UPDATE check_account_detail set remark=#{detail.remark} where id=#{detail.id}")
	int updateRemark(@Param("detail")DuiAccountDetail detail);

	@Update("UPDATE check_account_detail set check_no=#{detail.checkNo},check_status=#{detail.checkStatus},error_check_creator=#{detail.errorCheckCreator},check_remark=#{detail.checkRemark} where id=#{detail.id}")
	int updateErrorCheck(@Param("detail")DuiAccountDetail detail);

	
	@Update("UPDATE check_account_detail set out_bill_status = #{subOutBillDetail.outBillStatus} ,out_account_bill_method = #{outAccountBillMethod} ,"
			+ "is_add_bill = '1' where order_reference_no =#{subOutBillDetail.orderReferenceNo}")
	int updateOutBillStatusByOrderRefNum(@Param("subOutBillDetail")SubOutBillDetail subOutBillDetail ,@Param("outAccountBillMethod")Integer outAccountBillMethod); 
	
	
	public class SqlProvider{

		/**
		 * 差错帐查询
		 * @param parameter
		 * @return
		 */
		public String findErrorDuiAccountDetailList(final Map<String, Object> parameter) {
			final DuiAccountDetail duiAccountDetail = (DuiAccountDetail) parameter.get("duiAccountDetail");
			final Sort sord=(Sort)parameter.get("sort");
			//日期格式的处理，查询时去掉createTime 的时间部分
			final String createTimeStart = (String)parameter.get("createTimeStart") ;
			final String createTimeEnd = (String)parameter.get("createTimeEnd") ;
			return new SQL(){{
				SELECT(" d.*");
				FROM(" check_account_detail d");
				WHERE(" d.check_account_status !='SUCCESS' ");
				if (!StringUtils.isBlank(duiAccountDetail.getCheckBatchNo()) )
					WHERE(" d.check_batch_no =  #{duiAccountDetail.checkBatchNo} ");

				if (StringUtils.isNotBlank(duiAccountDetail.getPlateAcqTransTime1()) )
					WHERE(" d.plate_acq_trans_time >=  #{duiAccountDetail.plateAcqTransTime1} ");
				if (StringUtils.isNotBlank(duiAccountDetail.getPlateAcqTransTime2()) )
					WHERE(" d.plate_acq_trans_time <=  #{duiAccountDetail.plateAcqTransTime2} ");

				if (StringUtils.isNotBlank(duiAccountDetail.getAcqTransTime1()) )
					WHERE(" d.acq_trans_time >=  #{duiAccountDetail.acqTransTime1} ");
				if (StringUtils.isNotBlank(duiAccountDetail.getAcqTransTime2()) )
					WHERE(" d.acq_trans_time <=  #{duiAccountDetail.acqTransTime2} ");


				if (StringUtils.isNotBlank(createTimeStart) )
					WHERE(" d.create_time >=  #{createTimeStart} ");
				if (StringUtils.isNotBlank(createTimeEnd) )
					WHERE(" d.create_time <=  #{createTimeEnd} ");
				if (StringUtils.isNotBlank(duiAccountDetail.getAcqEnname()) && ! "ALL".equalsIgnoreCase(duiAccountDetail.getAcqEnname()))
					WHERE(" d.acq_enname = #{duiAccountDetail.acqEnname}");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqMerchantName()))
					WHERE(" d.acq_merchant_name = #{duiAccountDetail.acqMerchantName} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqMerchantNo()))
					WHERE(" d.acq_merchant_no = #{duiAccountDetail.acqMerchantNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqTerminalNo()))
					WHERE(" d.acq_terminal_no = #{duiAccountDetail.acqTerminalNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqAccountNo()))
					WHERE(" d.acq_account_no = #{duiAccountDetail.acqAccountNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqTransSerialNo()))
					WHERE(" d.acq_trans_serial_no = #{duiAccountDetail.acqTransSerialNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqReferenceNo()))
					WHERE(" d.acq_reference_no in ("+duiAccountDetail.getAcqReferenceNo()+") ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateAcqReferenceNo()))
					WHERE(" d.plate_acq_reference_no = #{duiAccountDetail.plateAcqReferenceNo} ");
				if (duiAccountDetail.getPlateTransId()!=null)
					WHERE(" d.plate_trans_id =  #{duiAccountDetail.plateTransId}");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateMerchantNo()))
					WHERE(" d.plate_merchant_no = #{duiAccountDetail.plateMerchantNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateTerminalNo()))
					WHERE(" d.plate_terminal_no = #{duiAccountDetail.plateTerminalNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateOrderNo()))
					WHERE(" d.plate_order_no = #{duiAccountDetail.plateOrderNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getOrderReferenceNo()))
					WHERE(" d.order_reference_no = #{duiAccountDetail.orderReferenceNo} ");

				if (!StringUtils.isBlank(duiAccountDetail.getPlateTransType()) && !"ALL".equals(duiAccountDetail.getPlateTransType()))
					WHERE(" d.plate_trans_type = #{duiAccountDetail.plateTransType} ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateTransStatus()) && !"ALL".equals(duiAccountDetail.getPlateTransStatus()))
					WHERE(" d.plate_trans_status = #{duiAccountDetail.plateTransStatus} ");
				if (!StringUtils.isBlank(duiAccountDetail.getCheckAccountStatus()) && !"ALL".equals(duiAccountDetail.getCheckAccountStatus()))
					WHERE(" d.check_account_status = #{duiAccountDetail.checkAccountStatus} ");
				if (!StringUtils.isBlank(duiAccountDetail.getErrorHandleStatus()) && !"ALL".equals(duiAccountDetail.getErrorHandleStatus()))
					WHERE(" d.error_handle_status = #{duiAccountDetail.errorHandleStatus} ");
                if (!StringUtils.isBlank(duiAccountDetail.getCheckStatus()) && !duiAccountDetail.getCheckStatus().equals("-1"))
                    WHERE(" d.check_status = #{duiAccountDetail.checkStatus}");
                if (null != duiAccountDetail.getRecordStatus() && -1 != duiAccountDetail.getRecordStatus())
                    WHERE(" d.record_status = #{duiAccountDetail.recordStatus}");
                if (duiAccountDetail.getAccount() != null && -1 != duiAccountDetail.getAccount())
                    WHERE(" d.account = #{duiAccountDetail.account}");
                if (duiAccountDetail.getSettleStatus() != null && !duiAccountDetail.getSettleStatus().equals(-1))
                    WHERE(" d.settle_status = #{duiAccountDetail.settleStatus}");
                if (duiAccountDetail.getFreezeStatus() != null && !duiAccountDetail.getFreezeStatus().equals("-1"))
                    WHERE(" d.freeze_status = #{duiAccountDetail.freezeStatus}");
                if (!StringUtils.isBlank(duiAccountDetail.getTreatmentMethod()) && !"-1".equals(duiAccountDetail.getTreatmentMethod()))
                    WHERE(" d.treatment_method = #{duiAccountDetail.treatmentMethod} ");
                if (!StringUtils.isBlank(duiAccountDetail.getCheckNo()))
                    WHERE(" d.check_no = #{duiAccountDetail.checkNo} ");
                if (null != duiAccountDetail.getCheckStatus() && !duiAccountDetail.getCheckStatus().equals("-1"))
                    WHERE(" d.check_status = #{duiAccountDetail.checkStatus}");


				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}else{
					ORDER_BY(" d.create_time desc ") ;
				}
			}}.toString();
		}
		
		public String findDuiAccountDetailList(final Map<String, Object> parameter) {
			final DuiAccountDetail duiAccountDetail = (DuiAccountDetail) parameter.get("duiAccountDetail");
			final Sort sord=(Sort)parameter.get("sort");
			//日期格式的处理，查询时去掉createTime 的时间部分
			final String createTimeStart = (String)parameter.get("createTimeStart") ;
			final String createTimeEnd = (String)parameter.get("createTimeEnd") ;
			return new SQL(){{
				SELECT(" d.* ");
				FROM(" check_account_detail d");
				if (!StringUtils.isBlank(duiAccountDetail.getCheckBatchNo()) )
					WHERE(" d.check_batch_no = #{duiAccountDetail.checkBatchNo} ");
				if (StringUtils.isNotBlank(createTimeStart) )
					WHERE(" d.create_time >=  #{createTimeStart} ");
				if (StringUtils.isNotBlank(createTimeEnd) )
					WHERE(" d.create_time <=  #{createTimeEnd} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqMerchantName()))
					WHERE(" d.acq_merchant_name = #{duiAccountDetail.acqMerchantName} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqMerchantNo()))
					WHERE(" d.acq_merchant_no = #{duiAccountDetail.acqMerchantNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqTerminalNo()))
					WHERE(" d.acq_terminal_no = #{duiAccountDetail.acqTerminalNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqAccountNo()))
					WHERE(" d.acq_account_no = #{duiAccountDetail.acqAccountNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqTransSerialNo()))
					WHERE(" d.acq_trans_serial_no = #{duiAccountDetail.acqTransSerialNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqReferenceNo()))
					WHERE(" d.acq_reference_no = #{duiAccountDetail.acqReferenceNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateAcqReferenceNo()))
					WHERE(" d.plate_acq_reference_no = #{duiAccountDetail.plateAcqReferenceNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateMerchantNo()))
					WHERE(" d.plate_merchant_no = #{duiAccountDetail.plateMerchantNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateTerminalNo()))
					WHERE(" d.plate_terminal_no = #{duiAccountDetail.plateTerminalNo} ");
				if (duiAccountDetail.getRecordStatus() != null && duiAccountDetail.getRecordStatus() != -1)
					WHERE(" d.record_status = #{duiAccountDetail.recordStatus} ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateTransType()) && !"ALL".equals(duiAccountDetail.getPlateTransType()))
					WHERE(" d.plate_trans_type = #{duiAccountDetail.plateTransType} ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateTransStatus()) && !"ALL".equals(duiAccountDetail.getPlateTransStatus()))
					WHERE(" d.plate_trans_status = #{duiAccountDetail.plateTransStatus} ");
				if (!StringUtils.isBlank(duiAccountDetail.getCheckAccountStatus()) && !"ALL".equals(duiAccountDetail.getCheckAccountStatus()))
					WHERE(" d.check_account_status = #{duiAccountDetail.checkAccountStatus} ");
				if (duiAccountDetail.getSettleStatus() != null && !duiAccountDetail.getSettleStatus().equals(-1))
					WHERE(" d.settle_status = #{duiAccountDetail.settleStatus}");
				if (duiAccountDetail.getAccount() != null && !duiAccountDetail.getAccount().equals(-1))
					WHERE(" d.account = #{duiAccountDetail.account}");
				if (null != duiAccountDetail.getRecordStatus() && -1 != duiAccountDetail.getRecordStatus())
					WHERE(" d.record_status = #{duiAccountDetail.recordStatus}");
				if (StringUtils.isNotBlank(duiAccountDetail.getAcqEnname()) && ! "ALL".equalsIgnoreCase(duiAccountDetail.getAcqEnname()))
					WHERE(" d.acq_enname = #{duiAccountDetail.acqEnname}");
				if (!StringUtils.isBlank(duiAccountDetail.getOrderReferenceNo()))
					WHERE(" d.order_reference_no = #{duiAccountDetail.orderReferenceNo} ");
				if (StringUtils.isNotBlank(duiAccountDetail.getIsAddBill()) && ! "ALL".equalsIgnoreCase(duiAccountDetail.getIsAddBill()))
					WHERE(" d.is_add_bill = #{duiAccountDetail.isAddBill}");
				if (StringUtils.isNotBlank(duiAccountDetail.getFreezeStatus()) && ! "ALL".equalsIgnoreCase(duiAccountDetail.getFreezeStatus()))
					WHERE(" d.freeze_status = #{duiAccountDetail.freezeStatus}");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateOrderNo()))
					WHERE(" d.plate_order_no = #{duiAccountDetail.plateOrderNo} ");
				if (StringUtils.isNotBlank(duiAccountDetail.getDbCutFlag()) && !"ALL".equals(duiAccountDetail.getDbCutFlag()))
					WHERE(" d.db_cut_flag = #{duiAccountDetail.dbCutFlag}");
                if (duiAccountDetail.getPayMethod() != null && !"ALL".equals(duiAccountDetail.getPayMethod()))
                    WHERE(" d.pay_method = #{duiAccountDetail.payMethod}");
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}else{
					ORDER_BY(" d.create_time desc ") ;
				}
			}}.toString();
		}
		 

		public String findExportDuiAccountDetailList(final Map<String, Object> parameter) {
			final DuiAccountDetail duiAccountDetail = (DuiAccountDetail) parameter.get("duiAccountDetail");
			//日期格式的处理，查询时去掉createTime 的时间部分
			final String createTimeStart = (String)parameter.get("createTimeStart") ;
			final String createTimeEnd = (String)parameter.get("createTimeEnd") ;
			return new SQL(){{
				SELECT(" d.* ");
				FROM(" check_account_detail d");
				if (!StringUtils.isBlank(duiAccountDetail.getCheckBatchNo()) )
					WHERE(" d.check_batch_no = #{duiAccountDetail.checkBatchNo} ");
				if (StringUtils.isNotBlank(createTimeStart) )
					WHERE(" d.create_time >=  #{createTimeStart} ");
				if (StringUtils.isNotBlank(createTimeEnd) )
					WHERE(" d.create_time <=  #{createTimeEnd} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqMerchantName()))
					WHERE(" d.acq_merchant_name = #{duiAccountDetail.acqMerchantName} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqMerchantNo()))
					WHERE(" d.acq_merchant_no = #{duiAccountDetail.acqMerchantNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqTerminalNo()))
					WHERE(" d.acq_terminal_no = #{duiAccountDetail.acqTerminalNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqAccountNo()))
					WHERE(" d.acq_account_no = #{duiAccountDetail.acqAccountNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqTransSerialNo()))
					WHERE(" d.acq_trans_serial_no = #{duiAccountDetail.acqTransSerialNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqReferenceNo()))
					WHERE(" d.acq_reference_no = #{duiAccountDetail.acqReferenceNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateAcqReferenceNo()))
					WHERE(" d.plate_acq_reference_no = #{duiAccountDetail.plateAcqReferenceNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateMerchantNo()))
					WHERE(" d.plate_merchant_no = #{duiAccountDetail.plateMerchantNo} ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateTerminalNo()))
					WHERE(" d.plate_terminal_no = #{duiAccountDetail.plateTerminalNo} ");
				if (duiAccountDetail.getRecordStatus() != null && duiAccountDetail.getRecordStatus() != -1)
					WHERE(" d.record_status = #{duiAccountDetail.recordStatus} ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateTransType()) && !"ALL".equals(duiAccountDetail.getPlateTransType()))
					WHERE(" d.plate_trans_type = #{duiAccountDetail.plateTransType} ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateTransStatus()) && !"ALL".equals(duiAccountDetail.getPlateTransStatus()))
					WHERE(" d.plate_trans_status = #{duiAccountDetail.plateTransStatus} ");
				if (!StringUtils.isBlank(duiAccountDetail.getCheckAccountStatus()) && !"ALL".equals(duiAccountDetail.getCheckAccountStatus()))
					WHERE(" d.check_account_status = #{duiAccountDetail.checkAccountStatus} ");
				if (duiAccountDetail.getSettleStatus() != null && !duiAccountDetail.getSettleStatus().equals(-1))
					WHERE(" d.settle_status = #{duiAccountDetail.settleStatus}");
				if (duiAccountDetail.getAccount() != null && !duiAccountDetail.getAccount().equals(-1))
					WHERE(" d.account = #{duiAccountDetail.account}");
				if (null != duiAccountDetail.getRecordStatus() && -1 != duiAccountDetail.getRecordStatus())
					WHERE(" d.record_status = #{duiAccountDetail.recordStatus}");
				if (StringUtils.isNotBlank(duiAccountDetail.getAcqEnname()) && ! "ALL".equalsIgnoreCase(duiAccountDetail.getAcqEnname()))
					WHERE(" d.acq_enname = #{duiAccountDetail.acqEnname}");
				if (!StringUtils.isBlank(duiAccountDetail.getOrderReferenceNo()))
					WHERE(" d.order_reference_no = #{duiAccountDetail.orderReferenceNo} ");
				if (StringUtils.isNotBlank(duiAccountDetail.getIsAddBill()) && ! "ALL".equalsIgnoreCase(duiAccountDetail.getIsAddBill()))
					WHERE(" d.is_add_bill = #{duiAccountDetail.isAddBill}");
				if (StringUtils.isNotBlank(duiAccountDetail.getFreezeStatus()) && ! "ALL".equalsIgnoreCase(duiAccountDetail.getFreezeStatus()))
					WHERE(" d.freeze_status = #{duiAccountDetail.freezeStatus}");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateOrderNo()))
					WHERE(" d.plate_order_no = #{duiAccountDetail.plateOrderNo} ");
				if (StringUtils.isNotBlank(duiAccountDetail.getDbCutFlag()) && !"ALL".equals(duiAccountDetail.getDbCutFlag()))
					WHERE(" d.db_cut_flag = #{duiAccountDetail.dbCutFlag}");
				if (duiAccountDetail.getPayMethod() != null && !"ALL".equals(duiAccountDetail.getPayMethod()))
					WHERE(" d.pay_method = #{duiAccountDetail.payMethod}");
				ORDER_BY(" d.create_time desc ") ;
			}}.toString();
		}
		public String findErrorExportDuiAccountDetailList(final Map<String, Object> parameter) {
			final DuiAccountDetail duiAccountDetail = (DuiAccountDetail) parameter.get("duiAccountDetail");
			//日期格式的处理，查询时去掉createTime 的时间部分
			final String createTimeStart = (String)parameter.get("createTimeStart") ;
			final String createTimeEnd = (String)parameter.get("createTimeEnd") ;
			return new SQL(){{
				SELECT(" d.* ");
				FROM(" check_account_detail d");
				WHERE(" d.check_account_status!='SUCCESS'");
                if (!StringUtils.isBlank(duiAccountDetail.getCheckBatchNo()) )
                    WHERE(" d.check_batch_no = #{duiAccountDetail.checkBatchNo} ");
                if (StringUtils.isNotBlank(createTimeStart) )
                    WHERE(" d.create_time >=  #{createTimeStart} ");
                if (StringUtils.isNotBlank(createTimeEnd) )
                    WHERE(" d.create_time <=  #{createTimeEnd} ");

				if (StringUtils.isNotBlank(duiAccountDetail.getPlateAcqTransTime1()) )
					WHERE(" d.plate_acq_trans_time >=  #{duiAccountDetail.plateAcqTransTime1} ");
				if (StringUtils.isNotBlank(duiAccountDetail.getPlateAcqTransTime2()) )
					WHERE(" d.plate_acq_trans_time <=  #{duiAccountDetail.plateAcqTransTime2} ");

				if (StringUtils.isNotBlank(duiAccountDetail.getAcqTransTime1()) )
					WHERE(" d.acq_trans_time >=  #{duiAccountDetail.acqTransTime1} ");
				if (StringUtils.isNotBlank(duiAccountDetail.getAcqTransTime2()) )
					WHERE(" d.acq_trans_time <=  #{duiAccountDetail.acqTransTime2} ");


                if (StringUtils.isNotBlank(duiAccountDetail.getAcqEnname()) && ! "ALL".equalsIgnoreCase(duiAccountDetail.getAcqEnname()))
                    WHERE(" d.acq_enname = #{duiAccountDetail.acqEnname}");
                if (!StringUtils.isBlank(duiAccountDetail.getAcqMerchantName()))
                    WHERE(" d.acq_merchant_name = #{duiAccountDetail.acqMerchantName}");
                if (!StringUtils.isBlank(duiAccountDetail.getAcqMerchantNo()))
                    WHERE(" d.acq_merchant_no = #{duiAccountDetail.acqMerchantNo} ");
                if (!StringUtils.isBlank(duiAccountDetail.getAcqTerminalNo()))
                    WHERE(" d.acq_terminal_no = #{duiAccountDetail.acqTerminalNo}");
                if (!StringUtils.isBlank(duiAccountDetail.getAcqAccountNo()))
                    WHERE(" d.acq_account_no = #{duiAccountDetail.acqAccountNo}");
                if (!StringUtils.isBlank(duiAccountDetail.getAcqTransSerialNo()))
                    WHERE(" d.acq_trans_serial_no = #{duiAccountDetail.acqTransSerialNo}");
                if (!StringUtils.isBlank(duiAccountDetail.getAcqReferenceNo()))
                    WHERE(" d.acq_reference_no in ("+duiAccountDetail.getAcqReferenceNo()+")");
                if (!StringUtils.isBlank(duiAccountDetail.getPlateAcqReferenceNo()))
                    WHERE(" d.plate_acq_reference_no = #{duiAccountDetail.plateAcqReferenceNo}");
                if (duiAccountDetail.getPlateTransId()!=null)
                    WHERE(" d.plate_trans_id =  #{duiAccountDetail.plateTransId}");
                if (!StringUtils.isBlank(duiAccountDetail.getPlateMerchantNo()))
                    WHERE(" d.plate_merchant_no = #{duiAccountDetail.plateMerchantNo}");
                if (!StringUtils.isBlank(duiAccountDetail.getPlateTerminalNo()))
                    WHERE(" d.plate_terminal_no = #{duiAccountDetail.plateTerminalNo}");
                if (!StringUtils.isBlank(duiAccountDetail.getPlateOrderNo()))
                    WHERE(" d.plate_order_no = #{duiAccountDetail.plateOrderNo}");
                if (!StringUtils.isBlank(duiAccountDetail.getOrderReferenceNo()))
                    WHERE(" d.order_reference_no = #{duiAccountDetail.orderReferenceNo}");

                if (!StringUtils.isBlank(duiAccountDetail.getPlateTransType()) && !"ALL".equals(duiAccountDetail.getPlateTransType()))
                    WHERE(" d.plate_trans_type = #{duiAccountDetail.plateTransType}");
                if (!StringUtils.isBlank(duiAccountDetail.getPlateTransStatus()) && !"ALL".equals(duiAccountDetail.getPlateTransStatus()))
                    WHERE(" d.plate_trans_status = #{duiAccountDetail.plateTransStatus}");
                if (!StringUtils.isBlank(duiAccountDetail.getCheckAccountStatus()) && !"ALL".equals(duiAccountDetail.getCheckAccountStatus()))
                    WHERE(" d.check_account_status = #{duiAccountDetail.checkAccountStatus}");
                if (!StringUtils.isBlank(duiAccountDetail.getErrorHandleStatus()) && !"ALL".equals(duiAccountDetail.getErrorHandleStatus()))
                    WHERE(" d.error_handle_status = #{duiAccountDetail.errorHandleStatus}");
                if (!StringUtils.isBlank(duiAccountDetail.getCheckStatus()) && !duiAccountDetail.getCheckStatus().equals("-1"))
                    WHERE(" d.check_status = #{duiAccountDetail.checkStatus}");
                if (null != duiAccountDetail.getRecordStatus() && -1 != duiAccountDetail.getRecordStatus())
                    WHERE(" d.record_status = #{duiAccountDetail.recordStatus}");
                if (duiAccountDetail.getAccount() != null && -1 != duiAccountDetail.getAccount())
                    WHERE(" d.account = #{duiAccountDetail.account}");
                if (duiAccountDetail.getSettleStatus() != null && !duiAccountDetail.getSettleStatus().equals(-1))
                    WHERE(" d.settle_status = #{duiAccountDetail.settleStatus}");
                if (duiAccountDetail.getFreezeStatus() != null && !duiAccountDetail.getFreezeStatus().equals("-1"))
                    WHERE(" d.freeze_status = #{duiAccountDetail.freezeStatus}");
                if (!StringUtils.isBlank(duiAccountDetail.getTreatmentMethod()) && !"-1".equals(duiAccountDetail.getTreatmentMethod()))
                    WHERE(" d.treatment_method = #{duiAccountDetail.treatmentMethod}");
                if (!StringUtils.isBlank(duiAccountDetail.getCheckNo()))
                    WHERE(" d.check_no = #{duiAccountDetail.checkNo}");
                if (null != duiAccountDetail.getCheckStatus() && !duiAccountDetail.getCheckStatus().equals("-1"))
                    WHERE(" d.check_status = #{duiAccountDetail.checkStatus}");
				ORDER_BY(" d.create_time desc ") ;
			}}.toString();
		}
		public String updateDuiAccountDetail(final Map<String, DuiAccountDetail> parameter) {
			final DuiAccountDetail dui = parameter.get("dui");
			return new SQL() {
				{
					UPDATE(" check_account_detail  ");
					SET("  acq_trans_serial_no=#{dui.acqTransSerialNo},acq_merchant_no=#{dui.acqMerchantNo},"
							+ " acq_merchant_name=#{dui.acqMerchantName},acq_terminal_no=#{dui.acqTerminalNo},access_org_no=#{dui.accessOrgNo},"
							+ " access_org_name=#{dui.accessOrgName},acq_batch_no=#{dui.acqBatchNo},acq_serial_no=#{dui.acqSerialNo},"
							+ " acq_account_no=#{dui.acqAccountNo},acq_card_sequence_no=#{dui.acqCardSequenceNo},"
							+ " acq_trans_time=#{dui.acqTransTime}, acq_reference_no=#{dui.acqReferenceNo},acq_settle_date=#{dui.acqSettleDate},"
							+ " acq_trans_code=#{dui.acqTransCode},acq_trans_status=#{dui.acqTransStatus},"
							+ " acq_trans_amount=#{dui.acqTransAmount}, acq_refund_amount=#{dui.acqRefundAmount},acq_check_date=#{dui.acqCheckDate},"
							+ " acq_ori_trans_serial_no=#{dui.acqOriTransSerialNo},acq_enname=#{dui.acqEnname},"
							+ " plate_trans_id=#{dui.plateTransId}, plate_acq_merchant_no=#{dui.plateAcqMerchantNo},"
							+ " plate_acq_terminal_no=#{dui.plateAcqTerminalNo},plate_merchant_no=#{dui.plateMerchantNo},plate_terminal_no=#{dui.plateTerminalNo},"
							+ " plate_acq_batch_no=#{dui.plateAcqBatchNo}, plate_acq_serial_no=#{dui.plateAcqSerialNo},plate_batch_no=#{dui.plateBatchNo},"
							+ " plate_serial_no=#{dui.plateSerialNo},plate_account_no=#{dui.plateAccountNo},"
							+ " plate_trans_amount=#{dui.plateTransAmount}, plate_acq_merchant_fee=#{dui.plateAcqMerchantFee},"
							+ " plate_merchant_fee=#{dui.plateMerchantFee},plate_agent_fee=#{dui.plateAgentFee},plate_acq_merchant_rate=#{dui.plateAcqMerchantRate},"
							+ " plate_merchant_rate=#{dui.plateMerchantRate}, plate_acq_reference_no=#{dui.plateAcqReferenceNo},"
							+ " plate_acq_trans_time=#{dui.plateAcqTransTime},plate_merchant_settle_date=#{dui.plateMerchantSettleDate},"
							+ " plate_trans_type=#{dui.plateTransType},record_status=#{dui.recordStatus},"
							+ " plate_trans_status=#{dui.plateTransStatus}, check_account_status=#{dui.checkAccountStatus},"
							+ " check_batch_no=#{dui.checkBatchNo},mend_batch_no=#{dui.mendBatchNo},description=#{dui.description},"
							+ " create_time=now(), plate_agent_no=#{dui.plateAgentNo},plate_trans_source=#{dui.plateTransSource},"
							+ " my_settle=#{dui.mySettle},bag_settle=#{dui.bagSettle},acq_auth_no=#{dui.acqAuthNo},error_handle_creator=#{dui.errorHandleCreator},"
							+ " pos_type=#{dui.posType},error_handle_status=#{dui.errorHandleStatus},plate_agent_share_amount=#{dui.plateAgentShareAmount}, "
							+ " freeze_status=#{dui.freezeStatus},is_add_bill=#{dui.isAddBill},out_bill_status=#{dui.outBillStatus},settle_type=#{dui.settleType},"
                            + " plate_order_no=#{dui.plateOrderNo},order_reference_no=#{dui.orderReferenceNo},settlement_method=#{dui.settlementMethod},account=#{dui.account} ");
					if (dui.getId() != null) {
						WHERE(" id = #{dui.id}");
					} else {
						if ("ZF_ZQ".equals(dui.getAcqEnname()) || "YS_ZQ".equals(dui.getAcqEnname()) || "neweptok".equals(dui.getAcqEnname())) {
							if (StringUtils.isNotBlank(dui.getAcqReferenceNo())) {
								WHERE(" plate_acq_reference_no = #{dui.acqReferenceNo}");
							}
							if (StringUtils.isNotBlank(dui.getAcqSerialNo())) {
								WHERE(" plate_acq_serial_no = #{dui.acqSerialNo}");
							}
							if (StringUtils.isNotBlank(dui.getAcqAccountNo())) {
								WHERE(" plate_account_no = #{dui.acqAccountNo}");
							}
							if (StringUtils.isNotBlank(dui.getAcqTerminalNo())) {
								WHERE(" plate_acq_terminal_no = #{dui.acqTerminalNo}");
							}
							if (StringUtils.isNotBlank(dui.getAcqMerchantNo()) && !"neweptok".equals(dui.getAcqEnname())  && !"YS_ZQ".equals(dui.getAcqEnname()) ) {
								WHERE(" plate_acq_merchant_no = #{dui.acqMerchantNo}");
							}
						} else {
							if (StringUtils.isNotBlank(dui.getOrderReferenceNo())) {
								WHERE(" order_reference_no= #{dui.orderReferenceNo}");
							}
						}
					}
				}
			}.toString();
		}
		public String updatePlateDuiAccountDetail(final Map<String, DuiAccountDetail> parameter) {
			final DuiAccountDetail dui = parameter.get("dui");
			return new SQL() {
				{
					UPDATE(" check_account_detail  ");
					SET( " plate_trans_id=#{dui.plateTransId}, plate_acq_merchant_no=#{dui.plateAcqMerchantNo},"
							+ " plate_acq_terminal_no=#{dui.plateAcqTerminalNo},plate_merchant_no=#{dui.plateMerchantNo},plate_terminal_no=#{dui.plateTerminalNo},"
							+ " plate_acq_batch_no=#{dui.plateAcqBatchNo}, plate_acq_serial_no=#{dui.plateAcqSerialNo},plate_batch_no=#{dui.plateBatchNo},"
							+ " plate_serial_no=#{dui.plateSerialNo},plate_account_no=#{dui.plateAccountNo},"
							+ " plate_trans_amount=#{dui.plateTransAmount}, plate_acq_merchant_fee=#{dui.plateAcqMerchantFee},"
							+ " plate_merchant_fee=#{dui.plateMerchantFee},plate_agent_fee=#{dui.plateAgentFee},plate_acq_merchant_rate=#{dui.plateAcqMerchantRate},"
							+ " plate_merchant_rate=#{dui.plateMerchantRate}, plate_acq_reference_no=#{dui.plateAcqReferenceNo},"
							+ " plate_acq_trans_time=#{dui.plateAcqTransTime},plate_merchant_settle_date=#{dui.plateMerchantSettleDate},"
							+ " plate_trans_type=#{dui.plateTransType},record_status=#{dui.recordStatus},"
							+ " plate_trans_status=#{dui.plateTransStatus}, check_account_status=#{dui.checkAccountStatus},"
							+ " check_batch_no=#{dui.checkBatchNo},description=#{dui.description},"
							+ " create_time=now(), plate_agent_no=#{dui.plateAgentNo},plate_trans_source=#{dui.plateTransSource},"
							+ " my_settle=#{dui.mySettle},bag_settle=#{dui.bagSettle},error_handle_creator=#{dui.errorHandleCreator},"
							+ " pos_type=#{dui.posType},error_handle_status=#{dui.errorHandleStatus},plate_agent_share_amount=#{dui.plateAgentShareAmount}, "
							+ " freeze_status=#{dui.freezeStatus},is_add_bill=#{dui.isAddBill},out_bill_status=#{dui.outBillStatus},settle_type=#{dui.settleType},"
							+ " plate_order_no=#{dui.plateOrderNo},order_reference_no=#{dui.orderReferenceNo},settlement_method=#{dui.settlementMethod},account=#{dui.account},"
							+ " deduction_fee=#{dui.deductionFee}, actual_fee=#{dui.actualFee},coupon_nos=#{dui.couponNos},pay_method=#{dui.payMethod}");
						WHERE(" id = #{dui.id}");
				}
			}.toString();
		}

		public String propertyMapping(String name,int type){
			final String[] propertys={"checkBatchNo","acqTransTime","acqSettleDate","acqTransAmount","acqRefundAmount","acqCheckDate","plateTransAmount",
					"plateAcqMerchantFee","plateMerchantFee","plateAcqMerchantRate","plateMerchantRate","plateMerchantSettleDate","plateAcqTransTime","createTime"};
		    final String[] columns={"check_batch_no","acq_trans_time","acq_settle_date","acq_trans_amount","acq_refund_amount","acq_check_date","plate_trans_amount",
		    		"plate_acq_merchant_fee","plate_merchant_fee","plate_acq_merchant_rate","plate_merchant_rate","plate_merchant_settle_date","plate_acq_trans_time","create_time"};
		    if(StringUtils.isNotBlank(name)){
		    	if(type==0){//属性查出字段名
		    		for(int i=0;i<propertys.length;i++){
		    			if(name.equalsIgnoreCase(propertys[i])){
		    				return columns[i];
		    			}
		    		}
		    	}else if(type==1){//字段名查出属性
		    		for(int i=0;i<propertys.length;i++){
		    			if(name.equalsIgnoreCase(columns[i])){
		    				return propertys[i];
		    			}
		    		}
		    	}
		    }
			return null;
		}
		

		public String getCheckDetailTransInfos(final Map<String, Object> parameter) {
			final String acqEnname = (String) parameter.get("acqEnname");
			final String transTimeBegin = (String) parameter.get("transTimeBegin");
			final String transTimeEnd = (String) parameter.get("transTimeEnd");
			return new SQL(){{
				SELECT(" * ");
				FROM("check_account_detail ");
				WHERE(" plate_trans_status = 'SUCCESS' ");
				if (StringUtils.isNotBlank(acqEnname))
					WHERE(" acq_enname  = #{acqEnname} ");
				if (StringUtils.isNotBlank(transTimeBegin) && StringUtils.isNotBlank(transTimeEnd))
					WHERE(" plate_acq_trans_time >= #{transTimeBegin} and plate_acq_trans_time <= #{transTimeEnd} ");
			}}.toString();
		
	}

		public String findAllTranByAcqNameAndDate(final Map<String, Object> parameter) {
			final String acqEnname = (String) parameter.get("acqEnname");
			final String startTime = (String) parameter.get("startTime");
			final String endTime = (String) parameter.get("endTime");
			String sql = new SQL(){{
				SELECT(" * ");
				FROM("check_account_detail cad ");
				WHERE(" cad.plate_trans_status = 'SUCCESS' ");
				WHERE(" cad.is_add_bill = '0' ");
				WHERE(" cad.account = '1' ");
				WHERE(" cad.freeze_status = '0' ");
				WHERE(" cad.out_bill_status = '0' ");
				WHERE(" cad.settle_status = '4' ");
				if (StringUtils.isNotBlank(acqEnname))
					WHERE(" cad.acq_enname  = #{acqEnname} ");
				if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime))
					WHERE(" cad.plate_acq_trans_time  between  #{startTime} and #{endTime}");
				if (StringUtils.isNotBlank(startTime) && StringUtils.isBlank(endTime))
					WHERE(" cad.plate_acq_trans_time <= #{startTime} ");
				
				ORDER_BY(" cad.plate_acq_trans_time asc ");
			}}.toString();
			return sql;
		}
		public String queryByAcqDbDetails(final Map<String, Object> parameter) {
			final String acqEnname = StringUtil.filterNull(parameter.get("acqEnname"));
			final String acqMerchantNo = StringUtil.filterNull(parameter.get("acqMerchantNo"));
			final String acqSerNo =   StringUtil.filterNull(parameter.get("acqSerNo"));
			final String acqTerNo =   StringUtil.filterNull(parameter.get("acqTerNo"));
			final String acqReferenceNo =  StringUtil.filterNull(parameter.get("acqReferenceNo"));
			String sql = new SQL(){{
				SELECT("  *  " );
				FROM(" check_account_detail ");
				if (StringUtils.isNotBlank(acqEnname))
					WHERE(" acq_enname  = #{acqEnname} ");
				if (StringUtils.isNotBlank(acqSerNo) )
					WHERE(" plate_acq_serial_no = #{acqSerNo}");
				if (StringUtils.isNotBlank(acqTerNo))
					WHERE(" plate_acq_terminal_no = #{acqTerNo} ");
				if (StringUtils.isNotBlank(acqReferenceNo))
					WHERE(" plate_acq_reference_no = #{acqReferenceNo} ");
				if (!"neweptok".equals(acqEnname) && !"YS_ZQ".equals(acqEnname) && StringUtils.isNotBlank(acqMerchantNo))
					WHERE(" plate_acq_merchant_no = #{acqMerchantNo} ");

			}}.toString();
			return sql;
		}

		public String queryPlateDetail(final Map<String, Object> parameter) {
			final DuiAccountDetail duiAccountDetail = (DuiAccountDetail) parameter.get("dui");
			String sql = new SQL(){{
				SELECT("  *  " );
				FROM(" check_account_detail ");
				WHERE(" check_account_status='PLATE_SINGLE' ");
				if (StringUtils.isNotBlank(duiAccountDetail.getAcqEnname()))
					WHERE(" acq_enname  = #{dui.acqEnname} ");
				if (StringUtils.isNotBlank(duiAccountDetail.getAcqSerialNo()) )
					WHERE(" plate_acq_serial_no = #{dui.acqSerialNo}");
				if (StringUtils.isNotBlank(duiAccountDetail.getAcqTerminalNo()))
					WHERE(" plate_acq_terminal_no = #{dui.acqTerminalNo} ");
				if (StringUtils.isNotBlank(duiAccountDetail.getAcqReferenceNo()))
					WHERE(" plate_acq_reference_no = #{dui.acqReferenceNo} ");
//				if (!"neweptok".equals(duiAccountDetail.getAcqEnname())  && !"YS_ZQ".equals(duiAccountDetail.getAcqEnname()) && !"zm_xe".equals(duiAccountDetail.getAcqEnname()) && !"WF_ZQ".equals(duiAccountDetail.getAcqEnname()) && StringUtils.isNotBlank(duiAccountDetail.getAcqMerchantNo()))
//					WHERE(" plate_acq_merchant_no = #{dui.acqMerchantNo} ");
				List<String> temp = new ArrayList<>();
				temp.add("YBEI");
				temp.add("ds_pay");
				temp.add("ZFYL_ZQ");
				temp.add("hljcLd");
				temp.add("HLJC");
				temp.add("HLB_KJ");
				temp.add("zm_xe");
				temp.add("WF_ZQ");
				temp.add("openPay");
				temp.add("openPay");
				temp.add("ZY");
				temp.add("ZYLD");
				if(temp.contains(duiAccountDetail.getAcqEnname())){
					WHERE("plate_order_no = #{dui.acqOrderNo}" );
				}

				if (StringUtils.isNotBlank(duiAccountDetail.getAcqAccountNo()))
					WHERE(" plate_account_no = #{dui.acqAccountNo} ");

			}}.toString();
			return sql;
		}

		public String queryAcqDetail(final Map<String, Object> parameter) {
			final DuiAccountDetail duiAccountDetail = (DuiAccountDetail) parameter.get("dui");
			String sql = new SQL(){{
				SELECT("  *  " );
				FROM(" check_account_detail ");
				WHERE(" check_account_status='ACQ_SINGLE' ");
				if (StringUtils.isNotBlank(duiAccountDetail.getAcqEnname()))
					WHERE(" acq_enname  = #{dui.acqEnname} ");
				if (StringUtils.isNotBlank(duiAccountDetail.getPlateAcqSerialNo()) )
					WHERE(" acq_serial_no = #{dui.plateAcqSerialNo}");
				if (StringUtils.isNotBlank(duiAccountDetail.getPlateAcqTerminalNo()))
					WHERE(" acq_terminal_no = #{dui.plateAcqTerminalNo} ");
				if (StringUtils.isNotBlank(duiAccountDetail.getPlateAcqReferenceNo()))
					WHERE(" acq_reference_no = #{dui.plateAcqReferenceNo} ");
//				if (!"neweptok".equals(duiAccountDetail.getAcqEnname())  && !"YS_ZQ".equals(duiAccountDetail.getAcqEnname()) && !"zm_xe".equals(duiAccountDetail.getAcqEnname()) && !"WF_ZQ".equals(duiAccountDetail.getAcqEnname()) && StringUtils.isNotBlank(duiAccountDetail.getPlateAcqMerchantNo()))
//					WHERE(" acq_merchant_no = #{dui.plateAcqMerchantNo} ");

				List<String> temp = new ArrayList<>();
				temp.add("YBEI");
				temp.add("ds_pay");
				temp.add("ZFYL_ZQ");
				temp.add("hljcLd");
				temp.add("HLJC");
				temp.add("HLB_KJ");
				temp.add("zm_xe");
				temp.add("WF_ZQ");
				temp.add("openPay");
				temp.add("openPay");
				temp.add("ZY");
				temp.add("ZYLD");
				if(temp.contains(duiAccountDetail.getAcqEnname())){
					WHERE("acq_order_no = #{dui.plateOrderNo}" );
				}

				if (StringUtils.isNotBlank(duiAccountDetail.getAcqAccountNo()))
					WHERE(" acq_account_no = #{dui.plateAccountNo} ");

			}}.toString();
			return sql;
		}

	}

	@SelectProvider( type=SqlProvider.class,method="getCheckDetailTransInfos")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	List<DuiAccountDetail> getCheckDetailTransInfos(@Param("acqEnname")String acqEnname,@Param("transType")String transType, @Param("transTimeBegin")String transTimeBegin, @Param("transTimeEnd")String transTimeEnd);
	
	@Select("select * from check_account_detail where order_reference_no = #{orderNo} and acq_enname = #{acqEnname} and check_account_status!='NO_CHECKED'")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	DuiAccountDetail queryByAcqDbDetailInfo(@Param("orderNo")String orderNo,@Param("acqEnname")String acqEnname);


	@Select("select * from check_account_detail where order_reference_no = #{orderNo} and acq_enname = #{acqEnname} and check_account_status='NO_CHECKED'")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	DuiAccountDetail queryByAcqDbDetailInfoNoChecked(@Param("orderNo")String orderNo,@Param("acqEnname")String acqEnname);

	/**
	 * 盛付通专用
	 * @param orderNo
	 * @param acqEnname
	 * @return
	 */
	@Select("select * from check_account_detail where plate_acq_reference_no = #{orderNo} and acq_enname = #{acqEnname} and check_account_status!='NO_CHECKED'")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	DuiAccountDetail queryByAcqDbDetailInfo1(@Param("orderNo")String orderNo,@Param("acqEnname")String acqEnname);

	List<SubOutBillDetailLogs> exportOutBillDetailList(@Param("subOutBillDetail")SubOutBillDetail subOutBillDetail);

	@SelectProvider( type=SqlProvider.class,method="findAllTranByAcqNameAndDate")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	List<DuiAccountDetail> findAllTranByAcqNameAndDate(@Param("acqEnname")String acqEnname, @Param("transTime")Date transTime, @Param("startTime")String startTime, @Param("endTime")String endTime);

	@Select("select * from check_account_detail where order_reference_no = #{subOutBillDetail.orderReferenceNo} "
			+ " and plate_trans_status = 'SUCCESS' and account = '1' and  settle_status = '4' and out_bill_status = '0'")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	DuiAccountDetail queryDuiAccountDetailByOrderRefNum(@Param("subOutBillDetail")SubOutBillDetail subOutBillDetail);
	
	@Select("select check_account_status from check_account_detail where order_reference_no = #{orderReferenceNo} ")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	DuiAccountDetail findDuiAccountDetailByOrderReferenceNo(@Param("orderReferenceNo")String orderReferenceNo);

	@Select("select * from check_account_detail where acq_order_no = #{orderReferenceNo} and (create_time >= #{date1} and create_time <= #{date2}) and check_account_status = 'ACQ_SINGLE'")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	DuiAccountDetail findDuiAccountDetailByAcqOrderNoAndDate(@Param("orderReferenceNo")String orderReferenceNo,@Param("date1")String date1,@Param("date2")String date2);

	@Select("select * from check_account_detail where acq_reference_no = #{orderReferenceNo} and (create_time >= #{date1} and create_time <= #{date2}) and check_account_status = 'ACQ_SINGLE'")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	DuiAccountDetail findDuiAccountDetailByAcqReferenceNoAndDate(@Param("orderReferenceNo")String orderReferenceNo,@Param("date1")String date1,@Param("date2")String date2);

	@Select("select * from check_account_detail where acq_merchant_order_no = #{orderReferenceNo} and (create_time >= #{date1} and create_time <= #{date2}) and check_account_status = 'ACQ_SINGLE'")
	@ResultMap("cn.eeepay.framework.dao.bill.DuiAccountDetailMapper.BaseResultMap")
	DuiAccountDetail findDuiAccountDetailByAcqMerchantOrderNoAndDate(@Param("orderReferenceNo")String orderReferenceNo,@Param("date1")String date1,@Param("date2")String date2);


	@Update("update check_account_detail set acq_merchant_no=#{dui.acqMerchantNo}, order_reference_no=#{dui.orderReferenceNo},plate_acq_merchant_no=#{dui.plateAcqMerchantNo}," +
			"plate_agent_no=#{dui.plateAgentNo},plate_merchant_no=#{dui.plateMerchantNo},plate_terminal_no=#{dui.plateTerminalNo},plate_acq_batch_no=#{dui.plateAcqBatchNo}," +
			"plate_acq_serial_no=#{dui.plateAcqSerialNo},plate_account_no=#{dui.plateAccountNo},plate_batch_no=#{dui.plateBatchNo},plate_serial_no=#{dui.plateSerialNo}," +
			"plate_card_no=#{dui.plateCardNo},plate_trans_amount=#{dui.plateTransAmount},plate_acq_reference_no=#{dui.plateAcqReferenceNo},plate_acq_trans_time=#{dui.plateAcqTransTime}, " +
			"plate_trans_type=#{dui.plateTransType},plate_trans_status=#{dui.plateTransStatus},plate_acq_merchant_fee=#{dui.plateAcqMerchantFee},plate_merchant_fee=#{dui.plateMerchantFee}," +
			"plate_acq_merchant_rate=#{dui.plateAcqMerchantRate},plate_merchant_rate=#{dui.plateMerchantRate},plate_trans_status=#{dui.plateTransStatus}," +
			"bag_settle=#{dui.bagSettle},pos_type=#{dui.posType},plate_order_no=#{dui.plateOrderNo},plate_agent_share_amount=#{dui.plateAgentShareAmount}," +
			"settle_status=#{dui.settleStatus},settlement_method=#{dui.settlementMethod},account=#{dui.account}," +
			"freeze_status=#{dui.freezeStatus},out_bill_status=#{dui.outBillStatus},settle_type=#{dui.settleType}," +
			"plate_merchant_entry_no=#{dui.plateMerchantEntryNo}  where id=#{dui.id}")
	int updateDuiAccountForT1(@Param("dui")DuiAccountDetail dui);


	@Update("update check_account_detail set account=#{dui.account}," +
			"freeze_status=#{dui.freezeStatus} , settle_status=#{dui.settleStatus} , plate_trans_status=#{dui.plateTransStatus} where id=#{dui.id}")
	int updateDuiAccountStatus(@Param("dui")DuiAccountDetail dui);

}
