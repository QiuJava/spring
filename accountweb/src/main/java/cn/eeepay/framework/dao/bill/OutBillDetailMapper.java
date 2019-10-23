package cn.eeepay.framework.dao.bill;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.OutBillDetail;

/**
 * 
 * @author zrj
 * 2016年6月14日11:10:51
 *
 */
public interface OutBillDetailMapper {

	@Insert("insert into out_bill_detail(id,out_bill_id,merchant_no,merchant_balance,out_account_task_amount,out_amount,acq_org_no,today_balance,create_time"
			+ ",change_remark,change_operator_id,change_operator_name,out_bill_result,export_status,export_file_serial,export_date,verify_flag,verify_msg"
			+ ",record_status,out_bill_status,plate_merchant_entry_no,acq_merchant_no,trans_time,order_reference_no)"
			+"values(#{outBillDetail.id},#{outBillDetail.outBillId},#{outBillDetail.merchantNo},#{outBillDetail.merchantBalance},#{outBillDetail.outAccountTaskAmount}"
			+ ",#{outBillDetail.outAmount},#{outBillDetail.acqOrgNo},#{outBillDetail.todayBalance},#{outBillDetail.createTime},#{outBillDetail.changeRemark}"
			+ ",#{outBillDetail.changeOperatorId},#{outBillDetail.changeOperatorName},#{outBillDetail.outBillResult},#{outBillDetail.exportStatus},#{outBillDetail.exportFileSerial}"
			+ ",#{outBillDetail.exportDate},#{outBillDetail.verifyFlag},#{outBillDetail.verifyMsg},#{outBillDetail.recordStatus},#{outBillDetail.outBillStatus}" +
			",#{outBillDetail.plateMerchantEntryNo},#{outBillDetail.acqMerchantNo},#{outBillDetail.transTime},#{outBillDetail.orderReferenceNo})"
			)
	int insertOutBillDetail(@Param("outBillDetail")OutBillDetail outBillDetail);
	
	@Update("update out_bill_detail set out_bill_result=#{outBillResult},out_bill_status=#{outBillStatus} where out_bill_id=#{outBillId}")
	int updateOutBillResultByBillId(@Param("outBillResult")String outBillResult, @Param("outBillStatus")Integer outBillStatus, @Param("outBillId")Integer outBillId);
	
	@Update("update out_bill_detail set out_bill_result=#{outBillResult},out_bill_status=#{outBillStatus} where id=#{id}")
	int updateOutBillResultById(@Param("outBillResult")String outBillResult, @Param("outBillStatus")Integer outBillStatus, @Param("id")String id);
	
	@Update("update out_bill_detail set record_status=#{recordStatus} where id=#{id}")
	int updateRecordStatusById(@Param("recordStatus")String recordStatus, @Param("id")Integer id);
	
	@Update("update out_bill_detail set out_bill_id=#{outBillDetail.outBillId}"
			+", merchant_no=#{outBillDetail.merchantNo},merchant_balance=#{outBillDetail.merchantBalance},out_account_task_amount=#{outBillDetail.outAccountTaskAmount}"
			+", out_amount=#{outBillDetail.outAmount},acq_org_no=#{outBillDetail.acqOrgNo},today_balance=#{outBillDetail.todayBalance},create_time=#{outBillDetail.createTime}"
			+", change_remark=#{outBillDetail.changeRemark},change_operator_id=#{outBillDetail.changeOperatorId},change_operator_name=#{outBillDetail.changeOperatorName}"
			+ ",out_bill_result=#{outBillDetail.outBillResult},export_status=#{outBillDetail.exportStatus},export_file_serial=#{outBillDetail.exportFileSerial}"
			+ ",export_date=#{outBillDetail.exportDate},verify_flag=#{outBillDetail.verifyFlag},verify_msg=#{outBillDetail.verifyMsg},record_status=#{outBillDetail.recordStatus}"
			+ ",out_bill_status=#{outBillDetail.outBillStatus}"
			+ " where id=#{outBillDetail.id}")
	int updateOutBillDetailById(@Param("outBillDetail")OutBillDetail outBillDetail);
	
	@Update("update out_bill_detail set out_bill_result=-1 where out_bill_id=#{outBillId} and merchant_no in(#{merchantNos})")
	int updateByOutBillIdAndMerchantNo(@Param("outBillId")String outBillId, @Param("merchantNos")String merchantNos);
	
	@Delete("delete from out_bill_detail where id = #{id}")
	int deleteOutBillDetailById(@Param("id")Integer id);
	
	@Delete("delete from out_bill_detail where out_bill_id = #{outBillId}")
	int deleteOutBillDetailByOutBillId(@Param("outBillId")Integer outBillId);
	
	
	@Select("select * from out_bill_detail where out_bill_id=#{id}")
	@ResultType(OutBillDetail.class)
	List<OutBillDetail> findByOutBillId(@Param("id") Integer id);
	
	@Select("select * from out_bill_detail where out_bill_id=#{id} and out_bill_status=#{status}")
	@ResultType(OutBillDetail.class)
	List<OutBillDetail> findByOutBillIdAndStatus(@Param("id") Integer id, @Param("status")Integer status);
	
	@Select("select * from out_bill_detail where out_bill_id=#{id} and out_account_task_amount>0 and export_status in(2,3)")
	@ResultType(OutBillDetail.class)
	List<OutBillDetail> findPartByOutBillId(@Param("id") Integer id);
	
	@Select("select sum(out_account_task_amount) from out_bill_detail where out_bill_id=#{outBillId} and out_account_task_amount>0")
	@ResultType(BigDecimal.class)
	BigDecimal countOutAccountTaskAmount(@Param("outBillId")Integer outBillId);
	
	@Select("select * from out_bill_detail where out_bill_id=#{id} and out_account_task_amount>0 and acq_org_no=#{acqEnname} and out_bill_status in(0,2)")
	@ResultType(OutBillDetail.class)
	List<OutBillDetail> findPartByOutBillIdAndAcq(@Param("id") Integer id, @Param("acqEnname") String acqEnname);
	
	@Select("select acq_org_no from out_bill_detail where out_bill_id=#{id} and out_account_task_amount>0 group by acq_org_no")
	@ResultType(OutBillDetail.class)
	List<OutBillDetail> findAcqOrgByOutBillId(@Param("id") Integer id);
	
	@Select("select * from out_bill_detail where out_bill_id=#{outBillId} and record_status='SUCCESS' and acq_org_no=#{bank} and export_status=0 and out_account_task_amount>0")
	@ResultType(OutBillDetail.class)
	List<OutBillDetail> findByOutBillIdAndBank(@Param("outBillId")Integer outBillId, @Param("bank")String bank);

	@Select("select export_file_serial,export_date from out_bill_detail where out_bill_id=#{outBillId} and acq_org_no=#{bank} and export_status=#{status} and export_date=#{date} group by export_file_serial,export_date")
	@ResultType(OutBillDetail.class)
	List<OutBillDetail> findByOutBillIdAndBank2(@Param("outBillId")Integer outBillId, @Param("bank")String bank, @Param("status")Integer status, @Param("date")String date);
	
	@Select("select export_file_serial,export_date from out_bill_detail where out_bill_id=#{outBillId} and acq_org_no=#{acqEnname} group by export_file_serial,export_date")
	@ResultType(OutBillDetail.class)
	List<OutBillDetail> findTransactionFileByParam(@Param("outBillId")Integer outBillId, @Param("acqEnname")String acqEnname);
	
	@SelectProvider( type=SqlProvider.class,method="findOutBillDetailList")
	@ResultMap("cn.eeepay.framework.dao.bill.OutBillDetailMapper.BaseResultMap")
	List<OutBillDetail> findOutBillDetailList(@Param("outBillDetail")OutBillDetail outBillDetail,
			@Param("merchantNo")String merchantNo,
			@Param("acqOrgNo")String acqOrgNo,
			@Param("merchantBalance1")String merchantBalance1,
			@Param("merchantBalance2")String merchantBalance2,
			@Param("outAccountTaskAmount1")String outAccountTaskAmount1,
			@Param("outAccountTaskAmount2")String outAccountTaskAmount2,
			@Param("isChangeRemark")String isChangeRemark,
			@Param("sort")Sort sort,
			Page<OutBillDetail> page);
	
	@SelectProvider( type=SqlProvider.class,method="findExportOutBillDetailList")
	@ResultMap("cn.eeepay.framework.dao.bill.OutBillDetailMapper.BaseResultMap")
	List<OutBillDetail> findExportOutBillDetailList(@Param("params")Map<String, String> params, @Param("outBillDetail")OutBillDetail outBillDetail);
	
	@SelectProvider(type = SqlProvider.class, method = "findByParams")
	@ResultType(OutBillDetail.class)
	List<OutBillDetail> findByParams(@Param("outBillDetail")OutBillDetail outBillDetail, @Param("sort")Sort sort, Page<OutBillDetail> page);
	
	@Select("select * from out_bill_detail where id = #{id}")
	@ResultMap("cn.eeepay.framework.dao.bill.OutBillDetailMapper.BaseResultMap")
	OutBillDetail findOutBillDetailById(@Param("id")String id);
	
	@UpdateProvider(type = SqlProvider.class, method = "updateExportStatusAndSerial")
	int updateExportStatusAndSerial(@Param("status")Integer status, @Param("serial")String serial, @Param("detailIds")String detailIds);
	
	@UpdateProvider(type = SqlProvider.class, method = "updateByOutBillDetailIds")
	int updateByOutBillDetailIds(@Param("status")Integer status, @Param("detailIds")String detailIds);
	
	//@InsertProvider(type = SqlProvider.class, method = "inserBatch")
	@Insert("<script>"
//	        + " <selectKey resultType =\"java.lang.String\" keyProperty= \"id\" order= \"BEFORE\">"
//	        + " select nextval('out_bill_detail',8) as id"
//	        + " </selectKey >"
            + " insert into out_bill_detail(id,out_bill_id,merchant_no,merchant_balance, out_account_task_amount,out_amount,acq_org_no"
            + ",today_balance,create_time,verify_flag,verify_msg,record_status,out_bill_status) "
            + " values "
            + " <foreach collection =\"list\" item=\"item\" index= \"index\" separator =\",\"> "
            + " (nextval('out_bill_detail',8),#{item.outBillId},#{item.merchantNo},#{item.merchantBalance},#{item.outAccountTaskAmount},#{item.outAmount},#{item.acqOrgNo}"
            + ",#{item.todayBalance},#{item.createTime},#{item.verifyFlag},#{item.verifyMsg},#{item.recordStatus},#{item.outBillStatus})"
            + " </foreach > "
            + " </script>")
//	@SelectKey(statement=" SELECT nextval('out_bill_detail',8) AS id", keyProperty="id", before=true, resultType=String.class)  
	int insertTestBatch(@Param("list")List<OutBillDetail> list);
	
	
	@InsertProvider(type = SqlProvider.class, method = "inserBatch2")
	int insertTestBatch2(@Param("list")List<OutBillDetail> list);
	
	
	//这种批量插入无法返回序列ID
	//@InsertProvider(type = SqlProvider.class, method = "inserBatch")
	@Insert("<script>"
//	        + " <selectKey resultType =\"java.lang.String\" keyProperty= \"list.id\" order= \"BEFORE\">"
//	        + " select currval('out_bill_detail')"
//	        + " </selectKey >"
            + " insert into out_bill_detail(id,out_bill_id,merchant_no,merchant_balance, out_account_task_amount,out_amount,acq_org_no"
            + ",today_balance,create_time,verify_flag,verify_msg,record_status,out_bill_status) "
            + " values "
            + " <foreach collection =\"list\" item=\"item\" index= \"index\" separator =\",\"> "
            + " (nextval('out_bill_detail',8),#{item.outBillId},#{item.merchantNo},#{item.merchantBalance},#{item.outAccountTaskAmount},#{item.outAmount},#{item.acqOrgNo}"
            + ",#{item.todayBalance},#{item.createTime},#{item.verifyFlag},#{item.verifyMsg},#{item.recordStatus},#{item.outBillStatus})"
            + " </foreach > "
            + " </script>")
//	@SelectKey(statement=" SELECT nextval('out_bill_detail',8) AS id", keyProperty="id", before=true, resultType=String.class)  
	@Options(useGeneratedKeys = true, keyProperty = "id")
	int insertTestBatch3(@Param("list")List<OutBillDetail> list);
	
	@UpdateProvider(type = SqlProvider.class, method = "updateBatch")
	int updateBatch(@Param("list")List<Map<String, String>> list);
	
	@Update("update out_bill_detail set out_account_task_amount=merchant_balance where out_bill_id=#{outBillId} and out_account_task_amount>merchant_balance")
	int updateBatchAgain(@Param("outBillId")Integer outBillId);
	
	@Select("select * from out_bill_detail where out_bill_status=#{outBillStatus} and acq_org_no=#{acqEnname}")
	@ResultMap("cn.eeepay.framework.dao.bill.OutBillDetailMapper.BaseResultMap")
	List<OutBillDetail> findByOutBillStatusAndAcqEnname(@Param("outBillStatus")Integer outBillStatus, @Param("acqEnname")String acqEnname);
	
	@Select("select * from out_bill_detail where (out_bill_status=3 or (out_bill_status=2 and record_status='SUCCESS')) and acq_org_no=#{acqEnname}")
	@ResultMap("cn.eeepay.framework.dao.bill.OutBillDetailMapper.BaseResultMap")
	List<OutBillDetail> findT1AllStatusByAcqEnname(@Param("acqEnname")String acqEnname);
	
	@Select("select acq_org_no from out_bill_detail where out_bill_status=2 and out_account_task_amount>0 group by acq_org_no")
	@ResultMap("cn.eeepay.framework.dao.bill.OutBillDetailMapper.BaseResultMap")
	List<OutBillDetail> findFailedAcqOrg();
	
	@Select("select sum(out_amount) from out_bill_detail where acq_org_no=#{acqEnname} and date(create_time)=curdate() and out_bill_status in(0,2)")
	@ResultType(BigDecimal.class)
	BigDecimal countOutBillAmount(@Param("acqEnname")String acqEnname);
	
	@SelectProvider(type = SqlProvider.class, method = "findNotInDetailIds")
	@ResultMap("cn.eeepay.framework.dao.bill.OutBillDetailMapper.BaseResultMap")
	List<OutBillDetail> findNotInDetailIds(@Param("outBillId")Integer outBillid, @Param("detailIds")String detailIds);
	
public class SqlProvider{
	public String findNotInDetailIds(final Map<String, Object> parameter) {
		final String detailIds = (String) parameter.get("detailIds");
		return new SQL() {{
			SELECT("*");
			FROM("out_bill_detail");
			WHERE(" out_bill_id=#{outBillId} ");
			StringBuilder sb = new StringBuilder();
			sb.append(" id not in(");
			sb.append(detailIds);
			sb.append(") ");
			WHERE(sb.toString());
			WHERE(" record_status='SUCCESS'");
		}}.toString();
	}
	
	public String updateByOutBillDetailIds(final Map<String, Object> parameter) {
		final String detailIds = (String) parameter.get("detailIds");
		
		return new SQL() {{
			UPDATE("out_bill_detail");
			SET("export_status=#{status}");
			StringBuilder sb = new StringBuilder();
			sb.append(" id in(");
			sb.append(detailIds);
			sb.append(") ");
			WHERE(sb.toString());
		}}.toString();
	}
	
	public String inserBatch2(Map<String, List<OutBillDetail>> map) {
		List<OutBillDetail> list = map.get("list");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("insert into out_bill_detail(id,out_bill_id,merchant_no,merchant_balance, out_account_task_amount,out_amount,acq_org_no,today_balance,create_time,verify_flag,verify_msg,record_status) values ");
		MessageFormat messageFormat = new MessageFormat("(nextval(\"out_bill_detail\",8),#'{'list[{0}].outBillId},#'{'list[{0}].merchantNo},#'{'list[{0}].merchantBalance},#'{'list[{0}].outAccountTaskAmount},#'{'list[{0}].outAmount},#'{'list[{0}].acqOrgNo},#'{'list[{0}].todayBalance},#'{'list[{0}].createTime},#'{'list[{0}].verifyFlag},#'{'list[{0}].verifyMsg},#'{'list[{0}].recordStatus})");
		for (int i = 0; i < list.size(); i++) {
			stringBuilder.append(messageFormat.format(new Integer[]{i}));
			if (i < list.size() - 1) {  
				stringBuilder.append(",");  
            } 
		}
		return stringBuilder.toString();
	}
	
	public String updateBatch(Map<String, List<Map<String, String>>> map) {
		List<Map<String, String>> list = map.get("list");
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("insert into out_bill_detail(id, out_account_task_amount,acq_org_no,change_remark) values ");
		MessageFormat messageFormat = new MessageFormat("(#'{'list[{0}].id},#'{'list[{0}].outAccountTaskAmount},#'{'list[{0}].acqOrgNo},#'{'list[{0}].changeRemark})");
		for (int i = 0; i < list.size(); i++) {
			stringBuilder.append(messageFormat.format(new Integer[]{i}));
			if (i < list.size() - 1) {  
				stringBuilder.append(",");  
            } 
		}
		stringBuilder.append(" on duplicate key update out_account_task_amount=values(out_account_task_amount),acq_org_no=values(acq_org_no), change_remark=values(change_remark)");
		return stringBuilder.toString();
	}
	
	public String updateExportStatusAndSerial(final Map<String, Object> parameter) {
		final String detailIds = (String) parameter.get("detailIds");
		
		return new SQL() {{
			UPDATE("out_bill_detail");
			SET("export_status=#{status}, export_file_serial=#{serial}, export_date=curdate()");
			StringBuilder sb = new StringBuilder();
			sb.append(" id in(");
			sb.append(detailIds);
			sb.append(") ");
			WHERE(sb.toString());
		}}.toString();
	}
	
	
		public String findByParams(final Map<String, Object> parameter) {
			final OutBillDetail outBillDetail = (OutBillDetail) parameter.get("outBillDetail");
			final String acqOrgNo = (String) outBillDetail.getAcqOrgNo();
			final Integer outBillStatus = (Integer) outBillDetail.getOutBillStatus(); 
			final String outAmount1 = (String) outBillDetail.getOutAmount1();
			final String outAmount2 = (String) outBillDetail.getOutAmount2();
			final String startTime = (String) outBillDetail.getStartTime();
			final String endTime = (String) outBillDetail.getEndTime();
			final String merNos = (String) outBillDetail.getMerNos();
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL() {{
				SELECT("*");
				FROM(" out_bill_detail ");
				if (StringUtils.isNotBlank(merNos)) {
					if ("-1".equals(merNos)) {
						//这里用来进行限制不查询任何数据
						WHERE(" merchant_no = '-1' ");
					} else {
						StringBuilder sb = new StringBuilder();
						sb.append(" merchant_no in(");
						sb.append(merNos);
						sb.append(") ");
						WHERE(sb.toString());
					}
				}
				if (StringUtils.isNotBlank(acqOrgNo)) {
					WHERE(" acq_org_no = #{outBillDetail.acqOrgNo} ");
				}
				if (outBillStatus != -1) {
					WHERE(" out_bill_status = #{outBillDetail.outBillStatus} ");
				}
				if (StringUtils.isNotBlank(outAmount1)) {
					WHERE(" out_account_task_amount >= #{outBillDetail.outAmount1} ");
				}
				if (StringUtils.isNotBlank(outAmount2)) {
					WHERE(" out_account_task_amount <= #{outBillDetail.outAmount2} ");
				}
				if (StringUtils.isNotBlank(startTime)) {
					WHERE(" create_time >= #{outBillDetail.startTime} ");
				}
				if (StringUtils.isNotBlank(endTime)) {
					WHERE(" create_time <= #{outBillDetail.endTime} ");
				}
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
		}
	
		public String findExportOutBillDetailList(final Map<String, Object> parameter) {
			final OutBillDetail outBillDetail = (OutBillDetail) parameter.get("outBillDetail");
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			
			final String merchantNo = (String) params.get("merchantNo");
			final String acqOrgNo = (String) params.get("acqOrgNo");
			final String merchantBalance1 = (String) params.get("merchantBalance1");
			final String merchantBalance2 = (String) params.get("merchantBalance2");
			final String outAccountTaskAmount1 = (String) params.get("outAccountTaskAmount1");
			final String outAccountTaskAmount2 = (String) params.get("outAccountTaskAmount2");
			final String isChangeRemark = (String) params.get("isChangeRemark");
			
			return new SQL(){{
				SELECT("*");
				FROM(" out_bill_detail ");
				WHERE(" out_bill_id  = #{outBillDetail.outBillId} ");
				if (StringUtils.isNotBlank(merchantNo))
					WHERE(" merchant_no like  \"%\"#{params.merchantNo}\"%\" ");
				if (StringUtils.isNotBlank(acqOrgNo) && !acqOrgNo.equals("ALL"))
					WHERE(" acq_org_no = #{params.acqOrgNo} ");
				if (StringUtils.isNotBlank(merchantBalance1))
					WHERE(" merchant_balance >= #{params.merchantBalance1} ");
				if (StringUtils.isNotBlank(merchantBalance2))
					WHERE(" merchant_balance <= #{params.merchantBalance2} ");
				if (StringUtils.isNotBlank(outAccountTaskAmount1))
					WHERE(" out_account_task_amount >= #{params.outAccountTaskAmount1} ");
				if (StringUtils.isNotBlank(outAccountTaskAmount2))
					WHERE(" out_account_task_amount <= #{params.outAccountTaskAmount2} ");
				if (StringUtils.isNotBlank(isChangeRemark) && !"ALL".equalsIgnoreCase(isChangeRemark)) {
					if ("1".equals(isChangeRemark)) {
						WHERE(" change_remark is not null and change_remark!='' ");
					} else {
						WHERE(" change_remark is null or change_remark='' ");
					}
				} 
			}}.toString();
		}
		
		public String findOutBillDetailList(final Map<String, Object> parameter) {
			final OutBillDetail outBillDetail = (OutBillDetail) parameter.get("outBillDetail");
			final String merchantNo = (String) parameter.get("merchantNo");
			final String acqOrgNo = (String) parameter.get("acqOrgNo");
			final String merchantBalance1 = (String) parameter.get("merchantBalance1");
			final String merchantBalance2 = (String) parameter.get("merchantBalance2");
			final String outAccountTaskAmount1 = (String) parameter.get("outAccountTaskAmount1");
			final String outAccountTaskAmount2 = (String) parameter.get("outAccountTaskAmount2");
			final String isChangeRemark = (String) parameter.get("isChangeRemark");
			final Sort sord=(Sort)parameter.get("sort");
			String sb =  new SQL(){{
				SELECT("*");
				FROM(" out_bill_detail od");
				WHERE(" od.out_bill_id  = #{outBillDetail.outBillId} ");
				if (StringUtils.isNotBlank(outBillDetail.getId()))
					WHERE(" od.id = #{outBillDetail.id} ");
				if (StringUtils.isNotBlank(merchantNo))
					WHERE(" od.merchant_no like  \"%\"#{merchantNo}\"%\" ");
                if (StringUtils.isNotBlank(outBillDetail.getPlateMerchantEntryNo()))
                    WHERE(" od.plate_merchant_entry_no  like \"%\"#{outBillDetail.plateMerchantEntryNo}\"%\" ");
                if (StringUtils.isNotBlank(outBillDetail.getAcqMerchantNo()))
                    WHERE(" od.acq_merchant_no  like \"%\"#{outBillDetail.acqMerchantNo}\"%\" ");
				if (StringUtils.isNotBlank(acqOrgNo) && !acqOrgNo.equals("ALL"))
					WHERE(" od.acq_org_no = #{acqOrgNo} ");
				if (StringUtils.isNotBlank(merchantBalance1))
					WHERE(" od.merchant_balance >= #{merchantBalance1} ");
				if (StringUtils.isNotBlank(merchantBalance2))
					WHERE(" od.merchant_balance <= #{merchantBalance2} ");
				if (StringUtils.isNotBlank(outAccountTaskAmount1))
					WHERE(" od.out_account_task_amount >= #{outAccountTaskAmount1} ");
				if (StringUtils.isNotBlank(outAccountTaskAmount2))
					WHERE(" od.out_account_task_amount <= #{outAccountTaskAmount2} ");
				if (StringUtils.isNotBlank(isChangeRemark) && !"ALL".equalsIgnoreCase(isChangeRemark)) {
					if ("1".equals(isChangeRemark)) {
						WHERE(" od.change_remark is not null and change_remark!='' ");
					} else {
						WHERE(" (od.change_remark is null or change_remark='') ");
					}
				} 
				if (StringUtils.isNotBlank(outBillDetail.getVerifyFlag()) && !"-1".equalsIgnoreCase(outBillDetail.getVerifyFlag())) {
					WHERE(" od.verify_flag=#{outBillDetail.verifyFlag} ");
				} 
				if (outBillDetail.getOutBillStatus() != null && !outBillDetail.getOutBillStatus().equals(-1)) {
					WHERE(" od.out_bill_status=#{outBillDetail.outBillStatus} ");
				}
				if (StringUtils.isNotBlank(outBillDetail.getRecordStatus()) && !"-1".equals(outBillDetail.getRecordStatus())) {
					WHERE(" od.record_status=#{outBillDetail.recordStatus} ");
				}	
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
			return sb;
		}
		
		public String updateByOutBillIdAndMerchantNo(final Map<String, Object> parameter) {
			final List<String> merchantNoList = (List<String>) parameter.get("merchantNoList");
			final String outBillId = (String) parameter.get("outBillId");
			
			String noStrs = StringUtils.join(merchantNoList.toArray(), ",");
			
			return new SQL() {{
				UPDATE("out_bill_detail");
				SET(" out_bill_result=-1 ");
				WHERE(" out_bill_id=#{outBillId} ");
				WHERE(" merchant_no in(#{noStrs}) ");
				
			}}.toString();
		}
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","outBillId","merchantNo","merchantBalance","acqOrgNo","todayAmount","todayHistoryBalance","todayBalance","outAccountTaskAmount","calcOutAmount","outAmount","createTime","verifyFlag"};
		    final String[] columns={"id","out_bill_id","merchant_no","merchant_balance","acq_org_no","today_amount","today_history_balance","today_balance","out_account_task_amount","calc_out_amount","out_amount","create_time","verify_flag"};
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
	
}
