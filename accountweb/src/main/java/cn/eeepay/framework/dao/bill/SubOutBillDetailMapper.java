package cn.eeepay.framework.dao.bill;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.OutBillDetail;
import cn.eeepay.framework.model.bill.SubOutBillDetail;
/**
 * 
 * @author Administrator
 *
 */
public interface SubOutBillDetailMapper {
	
	@Insert("insert into sub_out_bill_detail("
			+ "id,"
			+ "out_bill_detail_id,"
			+ "out_bill_id,"
			+ "trans_time,"
			+ "trans_amount,"
			+ "order_reference_no,"
			+ "out_account_note,"
			+ "record_status,"
			+ "out_bill_status,"
			+ "verify_flag,"
			+ "verify_msg,"
			+ "acq_enname,"
			+ "acq_org_no,"
			+ "merchant_no,"
			+ "merchant_balance,"
			+ "out_account_task_amount,"
			+ "is_add_bill,"
			+ "plate_merchant_entry_no,"
			+ "acq_merchant_no,"
			+ "create_time"
			+ ")"
			+" values("
			+ "#{subOutBillDetail.id},"
			+ "#{subOutBillDetail.outBillDetailId},"
			+ "#{subOutBillDetail.outBillId},"
			+ "#{subOutBillDetail.transTime},"
			+ "#{subOutBillDetail.transAmount},"
			+ "#{subOutBillDetail.orderReferenceNo},"
			+ "#{subOutBillDetail.outAccountNote},"
			+ "#{subOutBillDetail.recordStatus},"
			+ "#{subOutBillDetail.outBillStatus},"
			+ "#{subOutBillDetail.verifyFlag},"
			+ "#{subOutBillDetail.verifyMsg},"
			+ "#{subOutBillDetail.acqEnname},"
			+ "#{subOutBillDetail.acqOrgNo},"
			+ "#{subOutBillDetail.merchantNo},"
			+ "#{subOutBillDetail.merchantBalance},"
			+ "#{subOutBillDetail.outAccountTaskAmount},"
			+ "#{subOutBillDetail.isAddBill},"
			+ "#{subOutBillDetail.plateMerchantEntryNo},"
			+ "#{subOutBillDetail.acqMerchantNo},"
			+ "now())"
			)
	int insertSubOutBillDetail(@Param("subOutBillDetail")SubOutBillDetail subOutBillDetail);

	
	
	@SelectProvider( type=SqlProvider.class,method="findSubOutBillDetailList")
	@ResultMap("cn.eeepay.framework.dao.bill.SubOutBillDetailMapper.BaseResultMap")
	List<SubOutBillDetail> findSubOutBillDetailList(@Param("subOutBillDetail")SubOutBillDetail subOutBillDetail,
			@Param("merchantNo")String merchantNo,
			@Param("acqOrgNo")String acqOrgNo,
			@Param("merchantBalance1")String merchantBalance1,
			@Param("merchantBalance2")String merchantBalance2,
			@Param("outAccountTaskAmount1")String outAccountTaskAmount1,
			@Param("outAccountTaskAmount2")String outAccountTaskAmount2,
			@Param("isChangeRemark")String isChangeRemark,
			@Param("timeStart")String timeStart, 
			@Param("timeEnd")String timeEnd,
			@Param("sort")Sort sort,
			Page<SubOutBillDetail> page);
	

	@Update("update sub_out_bill_detail set "
			+ "change_remark=#{subOutBillDetail.changeRemark},"
			+ "change_operator_id=#{subOutBillDetail.changeOperatorId},"
			+ "change_operator_name=#{subOutBillDetail.changeOperatorName} where id=#{subOutBillDetail.id}")
	int updateOutBillDetailChangeRemark(@Param("subOutBillDetail")SubOutBillDetail subOutBillDetail);

	@Update("update  sub_out_bill_detail set out_bill_id = #{subOutBillDetail.outBillId} , out_bill_detail_id = #{subOutBillDetail.outBillDetailId}, is_add_bill = '0'  where id=#{subOutBillDetail.id}")
	int updateOutBillDetailById(@Param("subOutBillDetail")SubOutBillDetail subOutBillDetail);

	@Delete("delete from sub_out_bill_detail where id=#{subOutBillDetail.id}")
	int deleteSubOutBillDetailById(@Param("subOutBillDetail")SubOutBillDetail subOutBillDetail);
	
	@Select("select sum(out_account_task_amount) as out_account_task_amount,merchant_no,acq_org_no from sub_out_bill_detail where out_bill_id=#{id} and out_account_task_amount>0 and acq_org_no=#{acqEnname} "
			+ " and out_bill_status in(0,2) GROUP BY merchant_no")
	@ResultType(SubOutBillDetail.class)
	List<SubOutBillDetail> findPartBySubOutBillIdAndAcq(@Param("id") Integer id, @Param("acqEnname") String acqEnname);

	@Select("select out_account_task_amount as out_account_task_amount,merchant_no,acq_org_no,trans_time,order_reference_no from sub_out_bill_detail where out_bill_id=#{id} and out_account_task_amount>0 and acq_org_no=#{acqEnname} "
			+ " and out_bill_status in(0,2)")
	@ResultType(SubOutBillDetail.class)
	List<SubOutBillDetail> findPartBySubOutBillIdAndAcqNotGroup(@Param("id") Integer id, @Param("acqEnname") String acqEnname);

	@Select("select sum(out_account_task_amount) as out_account_task_amount,merchant_no,acq_org_no,plate_merchant_entry_no,acq_merchant_no from sub_out_bill_detail where out_bill_id=#{id} and out_account_task_amount>0 and acq_org_no=#{acqEnname} "
			+ " and out_bill_status in(0,2) GROUP BY plate_merchant_entry_no")
	@ResultType(SubOutBillDetail.class)
	List<SubOutBillDetail> findPartBySubOutBillIdAndAcqInZFZQ(@Param("id") Integer id, @Param("acqEnname") String acqEnname);

	@Update("update sub_out_bill_detail set "
			+ " out_bill_detail_id=#{updateSubOutBillDetail.outBillDetailId}"
			+ " where id=#{updateSubOutBillDetail.id}")
	int updateSubIdByOutBillIdAndMerchantNo(@Param("updateSubOutBillDetail")SubOutBillDetail updateSubOutBillDetail);

	@Select("select id from sub_out_bill_detail where out_bill_id=#{selectSubOutBillDetail.outBillId} and merchant_no=#{selectSubOutBillDetail.merchantNo}")
	@ResultType(SubOutBillDetail.class)
	List<SubOutBillDetail> querySubIdByOutBillIdAndMerchantNo(@Param("selectSubOutBillDetail")SubOutBillDetail selectSubOutBillDetail);

	@Select("select id from sub_out_bill_detail where out_bill_id=#{selectSubOutBillDetail.outBillId} and merchant_no=#{selectSubOutBillDetail.merchantNo} and plate_merchant_entry_no=#{selectSubOutBillDetail.plateMerchantEntryNo} ")
	@ResultType(SubOutBillDetail.class)
	List<SubOutBillDetail> querySubIdByOutBillIdAndMerchantNoAndEntryNo(@Param("selectSubOutBillDetail")SubOutBillDetail selectSubOutBillDetail);
	
	@SelectProvider(type = SqlProvider.class, method = "findSubMerChuAccountList")
	@ResultMap("cn.eeepay.framework.dao.bill.SubOutBillDetailMapper.BaseResultMap")
	List<SubOutBillDetail> findSubMerChuAccountList(@Param("subOutBillDetail")SubOutBillDetail subOutBillDetail, @Param("sort")Sort sort, Page<SubOutBillDetail> page);

	@Select("select * from sub_out_bill_detail where id = #{subOutBillDetail.id} and out_bill_status != '1' ")
	@ResultMap("cn.eeepay.framework.dao.bill.SubOutBillDetailMapper.BaseResultMap")
	SubOutBillDetail queryOutBillDetailById(@Param("subOutBillDetail")SubOutBillDetail subOutBillDetail);
	
	@Update("update sub_out_bill_detail set "
			+ "is_add_bill = #{isAddBill} ,"
			+ "out_bill_id = #{subOutBillDetail.outBillId} "
			+ "where id = #{subOutBillDetail.id}")
	int updateOutBillDetailByOrderReNum(@Param("subOutBillDetail")SubOutBillDetail subOutBillDetail, @Param("isAddBill")String isAddBill);
	
	
	@Update("update sub_out_bill_detail set "
			+ "out_bill_status = #{subOutBillDetail.outBillStatus} ,"
			+ "is_add_bill = #{subOutBillDetail.isAddBill} ,"
			+ "settle_time = #{subOutBillDetail.settleTime} ,"
			+ "acq_enname = #{subOutBillDetail.acqEnname} "
			+ "where id = #{subOutBillDetail.id}")
	int updateOutBillStatusBySubOutBillDetailId(@Param("subOutBillDetail")SubOutBillDetail subOutBillDetail);
	
	@Delete("delete from sub_out_bill_detail where out_bill_id = #{outBillId}")
	int deleteSubOutBillDetailByOutBillId(@Param("outBillId")Integer id);
	
	@SelectProvider(type = SqlProvider.class, method = "exportOutBillDetailList")
	@ResultMap("cn.eeepay.framework.dao.bill.SubOutBillDetailMapper.BaseResultMap")
	List<SubOutBillDetail> exportOutBillDetailList(@Param("subOutBillDetail")SubOutBillDetail subOutBillDetail);
	
	@Select("select * from sub_out_bill_detail where out_bill_detail_id = #{outBillDetailId}")
	@ResultMap("cn.eeepay.framework.dao.bill.SubOutBillDetailMapper.BaseResultMap")
	List<SubOutBillDetail> querySubOutBillListByOutBillDetailId(@Param("outBillDetailId")String outBillDetailId);
	
	@Select("select * from sub_out_bill_detail where acq_org_no=#{acqEnname} AND out_bill_id=#{outBillId}")
	@ResultMap("cn.eeepay.framework.dao.bill.SubOutBillDetailMapper.BaseResultMap")
	List<SubOutBillDetail> findAllsubOutBill(@Param("outBillId")Integer outBillId, @Param("acqEnname")String acqEnname);

	@Select("select * from sub_out_bill_detail where order_reference_no = #{subOutBillDetail.orderReferenceNo}")
	@ResultMap("cn.eeepay.framework.dao.bill.SubOutBillDetailMapper.BaseResultMap")
	SubOutBillDetail queryOutBillDetailByOrderRefNum(@Param("subOutBillDetail")SubOutBillDetail subOutBillDetail);

	@Select("select count(id) from sub_out_bill_detail where out_bill_id=#{id} and verify_flag=2")
	int findVerifyFlag(@Param("id") Integer id);


	@SelectProvider( type=SqlProvider.class,method="exportSubOutBillDetailList")
	@ResultMap("cn.eeepay.framework.dao.bill.SubOutBillDetailMapper.BaseResultMap")
	List<SubOutBillDetail> exportSubOutBillDetailList(@Param("subOutBillDetail")SubOutBillDetail subOutBillDetail,
			@Param("merchantNo")String merchantNo,
			@Param("acqOrgNo")String acqOrgNo,
			@Param("merchantBalance1")String merchantBalance1,
			@Param("merchantBalance2")String merchantBalance2,
			@Param("outAccountTaskAmount1")String outAccountTaskAmount1,
			@Param("outAccountTaskAmount2")String outAccountTaskAmount2,
			@Param("isChangeRemark")String isChangeRemark,
			@Param("timeStart")String timeStart,
			@Param("timeEnd")String timeEnd);

	public class SqlProvider{
		
		
		public String exportOutBillDetailList(final Map<String, Object> parameter) {
			final SubOutBillDetail subOutBillDetail = (SubOutBillDetail) parameter.get("subOutBillDetail");
			final String acqOrgNo = (String) subOutBillDetail.getAcqOrgNo();
			final String acqEnname = (String) subOutBillDetail.getAcqEnname();
			final Integer outBillStatus = (Integer) subOutBillDetail.getOutBillStatus();
			final String isAddBill = (String) subOutBillDetail.getIsAddBill();
			final String outAmount1 = (String) subOutBillDetail.getOutAmount1();
			final String outAmount2 = (String) subOutBillDetail.getOutAmount2();
			final String startTime = (String) subOutBillDetail.getStartTime();
			final String endTime = (String) subOutBillDetail.getEndTime();
			final String transTimeStart = (String) subOutBillDetail.getTransTimeStart();
			final String transTimesEnd = (String) subOutBillDetail.getTransTimesEnd();
			final String merNos = (String) subOutBillDetail.getMerNos();
			return new SQL() {{
				SELECT("*");
				FROM(" sub_out_bill_detail ");
				if (StringUtils.isNotBlank(merNos)) {
					if (!"-1".equals(merNos)) {
						StringBuilder sb = new StringBuilder();
						sb.append(" merchant_no in(");
						sb.append(merNos);
						sb.append(") ");
						WHERE(sb.toString());
					}
				}
				if (StringUtils.isNotBlank(subOutBillDetail.getPlateMerchantEntryNo()))
					WHERE(" plate_merchant_entry_no  like \"%\"#{subOutBillDetail.plateMerchantEntryNo}\"%\" ");
				if (StringUtils.isNotBlank(subOutBillDetail.getAcqMerchantNo()))
					WHERE(" acq_merchant_no  like \"%\"#{subOutBillDetail.acqMerchantNo}\"%\" ");

				if (StringUtils.isNotBlank(acqOrgNo)) {
					WHERE(" acq_org_no = #{subOutBillDetail.acqOrgNo} ");
				}
				if (StringUtils.isNotBlank(acqEnname)) {
					WHERE(" acq_enname = #{subOutBillDetail.acqEnname} ");
				}
				if (outBillStatus != -1) {
					WHERE(" out_bill_status = #{subOutBillDetail.outBillStatus} ");
				}
				if (!"-1".equals(isAddBill)) {
					WHERE(" is_add_bill = #{subOutBillDetail.isAddBill} ");
				}
				if (StringUtils.isNotBlank(subOutBillDetail.getOrderReferenceNo())) {
					WHERE(" order_reference_no like  \"%\"#{subOutBillDetail.orderReferenceNo}\"%\" ");
				}
				if (StringUtils.isNotBlank(subOutBillDetail.getOutBillDetailId())) {
					WHERE(" out_bill_detail_id like  \"%\"#{subOutBillDetail.outBillDetailId}\"%\" ");
				}
				if (subOutBillDetail.getOutBillId() != null) {
					WHERE(" out_bill_id = #{subOutBillDetail.outBillId}");
				}
				if (subOutBillDetail.getId() != null) {
					WHERE(" id like  \"%\"#{subOutBillDetail.id}\"%\"");
				}
				if (StringUtils.isNotBlank(outAmount1)) {
					WHERE(" out_account_task_amount >= #{subOutBillDetail.outAmount1} ");
				}
				if (StringUtils.isNotBlank(outAmount2)) {
					WHERE(" out_account_task_amount <= #{subOutBillDetail.outAmount2} ");
				}
				if (StringUtils.isNotBlank(startTime)) {
					WHERE(" create_time >= #{subOutBillDetail.startTime} ");
				}
				if (StringUtils.isNotBlank(endTime)) {
					WHERE(" create_time <= #{subOutBillDetail.endTime} ");
				}
				if (StringUtils.isNotBlank(transTimeStart)) {
					WHERE(" trans_time >= #{subOutBillDetail.transTimeStart} ");
				}
				if (StringUtils.isNotBlank(transTimesEnd)) {
					WHERE(" trans_time <= #{subOutBillDetail.transTimesEnd} ");
				}
				
			}}.toString();
		}
		
		
		public String findSubMerChuAccountList(final Map<String, Object> parameter) {
			final SubOutBillDetail subOutBillDetail = (SubOutBillDetail) parameter.get("subOutBillDetail");
			final String acqOrgNo = (String) subOutBillDetail.getAcqOrgNo();
			final String acqEnname = (String) subOutBillDetail.getAcqEnname();
			final String selectIds = (String) subOutBillDetail.getSelectIds();
			final Integer outBillStatus = (Integer) subOutBillDetail.getOutBillStatus(); 
			final String isAddBill = (String) subOutBillDetail.getIsAddBill();
			final String outAmount1 = (String) subOutBillDetail.getOutAmount1();
			final String outAmount2 = (String) subOutBillDetail.getOutAmount2();
			final String startTime = (String) subOutBillDetail.getStartTime();
			final String endTime = (String) subOutBillDetail.getEndTime();
			final String merNos = (String) subOutBillDetail.getMerNos();
			final String transTimeStart = (String) subOutBillDetail.getTransTimeStart();
			final String transTimesEnd = (String) subOutBillDetail.getTransTimesEnd();
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL() {{
				SELECT(" sb.id,sb.out_bill_detail_id,"
						+ "sb.out_bill_id,sb.settle_time,sb.trans_time,"
						+ "sb.trans_amount,sb.order_reference_no,"
						+ "sb.out_account_note,"
						+ "ob.record_status,"
						+ "sb.out_bill_status,sb.verify_flag,sb.verify_msg,"
						+ "sb.acq_enname,sb.merchant_no,sb.merchant_balance,"
						+ "sb.out_account_task_amount,sb.is_add_bill,sb.create_time,"
						+ "sb.acq_org_no,sb.change_remark,sb.change_operator_id,"
						+ "sb.change_operator_name,sb.plate_merchant_entry_no,sb.acq_merchant_no ");
				FROM(" sub_out_bill_detail sb left join out_bill_detail ob on sb.out_bill_detail_id = ob.id");
				if (StringUtils.isNotBlank(merNos)) {
					if (!"-1".equals(merNos)) {
						StringBuilder sb = new StringBuilder();
						sb.append(" sb.merchant_no in(");
						sb.append(merNos);
						sb.append(") ");
						WHERE(sb.toString());
					}
				}
				if (StringUtils.isNotBlank(selectIds)) {
					if (!"-1".equals(selectIds)) {
						StringBuilder sb = new StringBuilder();
						sb.append(" sb.id in(");
						sb.append(selectIds);
						sb.append(") ");
						WHERE(sb.toString());
					}
				}
				if (StringUtils.isNotBlank(subOutBillDetail.getPlateMerchantEntryNo()))
					WHERE(" sb.plate_merchant_entry_no = #{subOutBillDetail.plateMerchantEntryNo} ");

				if (StringUtils.isNotBlank(subOutBillDetail.getAcqMerchantNo())) {
					StringBuilder sb = new StringBuilder();
					sb.append(" sb.acq_merchant_no  in (");
					sb.append(subOutBillDetail.getAcqMerchantNo());
					sb.append(") ");
					WHERE(sb.toString());
				}

				if (StringUtils.isNotBlank(acqOrgNo)) {
					WHERE(" sb.acq_org_no = #{subOutBillDetail.acqOrgNo} ");
				}
				if (StringUtils.isNotBlank(acqEnname)) {
					WHERE(" sb.acq_enname = #{subOutBillDetail.acqEnname} ");
				}
				if(outBillStatus != null){
					if (outBillStatus != -1) {
						WHERE(" sb.out_bill_status = #{subOutBillDetail.outBillStatus} ");
					}
				}
				if(isAddBill != null){
				    if (!"-1".equals(isAddBill)) {
					    WHERE(" sb.is_add_bill = #{subOutBillDetail.isAddBill} ");
				    }
				}
				if (StringUtils.isNotBlank(subOutBillDetail.getOrderReferenceNo())) {
					StringBuilder sb = new StringBuilder();
					sb.append(" sb.order_reference_no in (");
					sb.append(subOutBillDetail.getOrderReferenceNo());
					sb.append(") ");
					WHERE(sb.toString());
				}
				if (StringUtils.isNotBlank(subOutBillDetail.getOutBillDetailId())) {
					WHERE(" sb.out_bill_detail_id = #{subOutBillDetail.outBillDetailId}");
				}
				if (subOutBillDetail.getOutBillId() != null) {
					WHERE(" sb.out_bill_id = #{subOutBillDetail.outBillId}");
				}
				if (StringUtils.isNotBlank(subOutBillDetail.getId())) {
					WHERE(" sb.id = #{subOutBillDetail.id}");
				}
				if (StringUtils.isNotBlank(outAmount1)) {
					WHERE(" sb.out_account_task_amount >= #{subOutBillDetail.outAmount1} ");
				}
				if (StringUtils.isNotBlank(outAmount2)) {
					WHERE(" sb.out_account_task_amount <= #{subOutBillDetail.outAmount2} ");
				}
				if (StringUtils.isNotBlank(startTime)) {
					WHERE(" sb.create_time >= #{subOutBillDetail.startTime} ");
				}
				if (StringUtils.isNotBlank(endTime)) {
					WHERE(" sb.create_time <= #{subOutBillDetail.endTime} ");
				}
				if (StringUtils.isNotBlank(transTimeStart)) {
					WHERE(" sb.trans_time >= #{subOutBillDetail.transTimeStart} ");
				}
				if (StringUtils.isNotBlank(transTimesEnd)) {
					WHERE(" sb.trans_time <= #{subOutBillDetail.transTimesEnd} ");
				}
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
		}
		
		
		public String exportSubOutBillDetailList(final Map<String, Object> parameter) {
			final SubOutBillDetail subOutBillDetail = (SubOutBillDetail) parameter.get("subOutBillDetail");
			final String merchantNo = (String) parameter.get("merchantNo");
			final String acqOrgNo = (String) parameter.get("acqOrgNo");
			final String merchantBalance1 = (String) parameter.get("merchantBalance1");
			final String merchantBalance2 = (String) parameter.get("merchantBalance2");
			final String outAccountTaskAmount1 = (String) parameter.get("outAccountTaskAmount1");
			final String outAccountTaskAmount2 = (String) parameter.get("outAccountTaskAmount2");
			final String timeStart = (String) parameter.get("timeStart");
			final String timeEnd = (String) parameter.get("timeEnd");
			final String isChangeRemark = (String) parameter.get("isChangeRemark");
			String sb =  new SQL(){{
				SELECT(" sb.id,"
						+ "sb.out_bill_detail_id,"
						+ "sb.out_bill_id,"
						+ "sb.settle_time,"
						+ "sb.trans_time,"
						+ "sb.trans_amount,"
						+ "sb.order_reference_no,"
						+ "sb.out_account_note,"
						+ "ob.record_status,"
						+ "sb.out_bill_status,"
						+ "sb.verify_flag,"
						+ "sb.verify_msg,"
						+ "sb.acq_enname,"
						+ "sb.merchant_no,"
						+ "bea.settling_amount as merchant_balance,"
						+ "sb.out_account_task_amount,"
						+ "sb.is_add_bill,"
						+ "sb.create_time,"
						+ "sb.acq_org_no,"
						+ "sb.change_remark,"
						+ "sb.change_operator_id,"
						+ "sb.plate_merchant_entry_no,"
						+ "sb.acq_merchant_no,"
						+ "sb.change_operator_name ");
				FROM(" sub_out_bill_detail sb "
					+ " left join out_bill_detail ob on sb.out_bill_detail_id = ob.id"
					+ " left join ext_account_info  eai on eai.user_id = sb.merchant_no "
					+ " left join bill_ext_account  bea on bea.account_no = eai.account_no ");
				WHERE(" eai.account_type = 'M' and bea.subject_no = '224101001'  and sb.out_bill_id  = #{subOutBillDetail.outBillId} ");
				if (StringUtils.isNotBlank(subOutBillDetail.getId()))
					WHERE(" sb.id = #{subOutBillDetail.id} ");
				if (StringUtils.isNotBlank(subOutBillDetail.getOutBillDetailId()))
					WHERE(" sb.out_bill_detail_id = #{subOutBillDetail.outBillDetailId} ");
				if (StringUtils.isNotBlank(merchantNo))
					WHERE(" sb.merchant_no like  \"%\"#{merchantNo}\"%\" ");
				if (StringUtils.isNotBlank(subOutBillDetail.getPlateMerchantEntryNo()))
					WHERE(" sb.plate_merchant_entry_no  like \"%\"#{subOutBillDetail.plateMerchantEntryNo}\"%\" ");
				if (StringUtils.isNotBlank(subOutBillDetail.getAcqMerchantNo()))
					WHERE(" sb.acq_merchant_no  like \"%\"#{subOutBillDetail.acqMerchantNo}\"%\" ");
				if (StringUtils.isNotBlank(acqOrgNo) && !acqOrgNo.equals("ALL"))
					WHERE(" sb.acq_org_no = #{acqOrgNo} ");
				if (StringUtils.isNotBlank(merchantBalance1))
					WHERE(" sb.merchant_balance >= #{merchantBalance1} ");
				if (StringUtils.isNotBlank(merchantBalance2))
					WHERE(" sb.merchant_balance <= #{merchantBalance2} ");
				if (StringUtils.isNotBlank(outAccountTaskAmount1))
					WHERE(" sb.out_account_task_amount >= #{outAccountTaskAmount1} ");
				if (StringUtils.isNotBlank(outAccountTaskAmount2))
					WHERE(" sb.out_account_task_amount <= #{outAccountTaskAmount2} ");
				if (StringUtils.isNotBlank(isChangeRemark) && !"ALL".equalsIgnoreCase(isChangeRemark)) {
					if ("1".equals(isChangeRemark)) {
						WHERE(" sb.change_remark is not null and sb.change_remark!='' ");
					} else {
						WHERE(" (sb.change_remark is null or sb.change_remark='') ");
					}
				} 
				if (StringUtils.isNotBlank(subOutBillDetail.getOrderReferenceNo()))
					WHERE(" sb.order_reference_no like  \"%\"#{subOutBillDetail.orderReferenceNo}\"%\" ");
				if (StringUtils.isNotBlank(subOutBillDetail.getVerifyFlag()) && !"-1".equalsIgnoreCase(subOutBillDetail.getVerifyFlag())) {
					WHERE(" sb.verify_flag=#{subOutBillDetail.verifyFlag} ");
				} 
				if (StringUtils.isNotBlank(timeStart) )
					WHERE(" sb.trans_time >=  #{timeStart} ");
				if (StringUtils.isNotBlank(timeEnd) )
					WHERE(" sb.trans_time <=  #{timeEnd} ");
				if (subOutBillDetail.getOutBillStatus() != null && !subOutBillDetail.getOutBillStatus().equals(-1)) {
					WHERE(" sb.out_bill_status=#{subOutBillDetail.outBillStatus} ");
				}
				if (StringUtils.isNotBlank(subOutBillDetail.getRecordStatus()) && !subOutBillDetail.getRecordStatus().equals("-1")) {
					WHERE(" ob.record_status=#{subOutBillDetail.recordStatus} ");
				}
			}}.toString();
			return sb;
		}
		
		public String findSubOutBillDetailList(final Map<String, Object> parameter) {
			final SubOutBillDetail subOutBillDetail = (SubOutBillDetail) parameter.get("subOutBillDetail");
			final String merchantNo = (String) parameter.get("merchantNo");
			final String acqOrgNo = (String) parameter.get("acqOrgNo");
			final String merchantBalance1 = (String) parameter.get("merchantBalance1");
			final String merchantBalance2 = (String) parameter.get("merchantBalance2");
			final String outAccountTaskAmount1 = (String) parameter.get("outAccountTaskAmount1");
			final String outAccountTaskAmount2 = (String) parameter.get("outAccountTaskAmount2");
			final String isChangeRemark = (String) parameter.get("isChangeRemark");

			final String timeStart = (String) parameter.get("timeStart");
			final String timeEnd = (String) parameter.get("timeEnd");
			final Sort sord=(Sort)parameter.get("sort");
			String sb =  new SQL(){{
				SELECT(" sb.id,"
						+ "sb.out_bill_detail_id,"
						+ "sb.out_bill_id,"
						+ "sb.settle_time,"
						+ "sb.trans_time,"
						+ "sb.trans_amount,"
						+ "sb.order_reference_no,"
						+ "sb.out_account_note,"
						+ "ob.record_status,"
						+ "sb.out_bill_status,"
						+ "sb.verify_flag,"
						+ "sb.verify_msg,"
						+ "sb.acq_enname,"
						+ "sb.merchant_no,"
						+ "bea.settling_amount as merchant_balance,"
						+ "sb.out_account_task_amount,"
						+ "sb.is_add_bill,"
						+ "sb.create_time,"
						+ "sb.acq_org_no,"
						+ "sb.change_remark,"
						+ "sb.change_operator_id,"
						+ "sb.plate_merchant_entry_no,"
						+ "sb.acq_merchant_no,"
						+ "sb.change_operator_name ");
				FROM(" sub_out_bill_detail sb "
						+ " left join out_bill_detail ob on sb.out_bill_detail_id = ob.id"
						+ " left join ext_account_info  eai on eai.user_id = sb.merchant_no "
						+ " left join bill_ext_account  bea on bea.account_no = eai.account_no ");
				WHERE( " eai.account_type = 'M' and bea.subject_no = '224101001'  and sb.out_bill_id  = #{subOutBillDetail.outBillId} ");
				if (StringUtils.isNotBlank(subOutBillDetail.getId()))
					WHERE(" sb.id = #{subOutBillDetail.id} ");
				if (StringUtils.isNotBlank(subOutBillDetail.getOutBillDetailId()))
					WHERE(" sb.out_bill_detail_id = #{subOutBillDetail.outBillDetailId} ");
				if (StringUtils.isNotBlank(merchantNo))
					WHERE(" sb.merchant_no like  \"%\"#{merchantNo}\"%\" ");
				if (StringUtils.isNotBlank(subOutBillDetail.getPlateMerchantEntryNo()))
					WHERE(" sb.plate_merchant_entry_no  like \"%\"#{subOutBillDetail.plateMerchantEntryNo}\"%\" ");
				if (StringUtils.isNotBlank(subOutBillDetail.getAcqMerchantNo()))
					WHERE(" sb.acq_merchant_no  like \"%\"#{subOutBillDetail.acqMerchantNo}\"%\" ");
				if (StringUtils.isNotBlank(acqOrgNo) && !acqOrgNo.equals("ALL"))
					WHERE(" sb.acq_org_no = #{acqOrgNo} ");
				if (StringUtils.isNotBlank(merchantBalance1))
					WHERE(" sb.merchant_balance >= #{merchantBalance1} ");
				if (StringUtils.isNotBlank(merchantBalance2))
					WHERE(" sb.merchant_balance <= #{merchantBalance2} ");
				if (StringUtils.isNotBlank(outAccountTaskAmount1))
					WHERE(" sb.out_account_task_amount >= #{outAccountTaskAmount1} ");
				if (StringUtils.isNotBlank(outAccountTaskAmount2))
					WHERE(" sb.out_account_task_amount <= #{outAccountTaskAmount2} ");
				if (StringUtils.isNotBlank(isChangeRemark) && !"ALL".equalsIgnoreCase(isChangeRemark)) {
					if ("1".equals(isChangeRemark)) {
						WHERE(" sb.change_remark is not null and sb.change_remark!='' ");
					} else {
						WHERE(" (sb.change_remark is null or sb.change_remark='') ");
					}
				} 
				if (StringUtils.isNotBlank(subOutBillDetail.getOrderReferenceNo()))
					WHERE(" sb.order_reference_no like  \"%\"#{subOutBillDetail.orderReferenceNo}\"%\" ");
				if (StringUtils.isNotBlank(subOutBillDetail.getVerifyFlag()) && !"-1".equalsIgnoreCase(subOutBillDetail.getVerifyFlag())) {
					WHERE(" sb.verify_flag=#{subOutBillDetail.verifyFlag} ");
				} 
				if (subOutBillDetail.getOutBillStatus() != null && !subOutBillDetail.getOutBillStatus().equals(-1)) {
					WHERE(" sb.out_bill_status=#{subOutBillDetail.outBillStatus} ");
				}
				if (StringUtils.isNotBlank(subOutBillDetail.getRecordStatus()) && !subOutBillDetail.getRecordStatus().equals("-1")) {
					WHERE(" ob.record_status=#{subOutBillDetail.recordStatus} ");
				}
				if (StringUtils.isNotBlank(timeStart) )
					WHERE(" sb.trans_time >=  #{timeStart} ");
				if (StringUtils.isNotBlank(timeEnd) )
					WHERE(" sb.trans_time <=  #{timeEnd} ");
					
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
			return sb;
		}
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","outBillDetailId","outBillId","settleTime","transTime","transAmount","orderReferenceNo","outAccountNote","recordStatus","outBillStatus","verifyFlag","verifyMsg","acqEnname","acqOrgNo","merchantNo","merchantBalance","outAccountTaskAmount","isAddBill","changeRemark","createTime"};
		    final String[] columns={"id","out_bill_detail_id","out_bill_id","settle_time","trans_time","trans_amount","order_reference_no","out_account_note","record_status","out_bill_status","verify_flag","verify_msg","acq_enname","acq_org_no","merchant_no","merchant_balance","out_account_task_amount","is_add_bill","change_remark","create_time"};
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
		
	}
	@Update("<script>"
			+ " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"\" "
			+ "         close=\"\" separator=\";\"> "
			+ " update sub_out_bill_detail  "
			+ " set change_remark=#{item.changeRemark},"
			+ " change_operator_id=#{item.changeOperatorId},"
			+ " change_operator_name=#{item.changeOperatorName}"
			+ " where id=#{item.id}"
			+ "     </foreach> "
            + " </script>")
	int updateRemarkBacthOutBillDetailById(@Param("list")List<SubOutBillDetail> list);



	


}
