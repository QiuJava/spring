package cn.eeepay.framework.dao.bill;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;


import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.SubOutBillDetail;
import cn.eeepay.framework.model.bill.SubOutBillDetailLogs;
/**
 * 
 * @author Administrator
 *
 */
public interface SubOutBillDetailLogsMapper {
	


	@Insert("insert into sub_out_bill_detail_logs("
			+ "sub_out_bill_detail_id,"
			+ "out_bill_detail_id,"
			+ "out_bill_id,"
			+ "trans_time,"
			+ "settle_time,"
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
			+ "create_time"
			+ ")"
			+" values("
			+ "#{subOutBillDetail.subOutBillDetailId},"
			+ "#{subOutBillDetail.outBillDetailId},"
			+ "#{subOutBillDetail.outBillId},"
			+ "#{subOutBillDetail.transTime},"
			+ "#{subOutBillDetail.settleTime},"
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
			+ "now())"
			)
	int insertOutBillDetailLogs(@Param("subOutBillDetail")SubOutBillDetail subOutBillDetail);


	@SelectProvider( type=SqlProvider.class,method="findOutBillDetailLogsList")
	@ResultMap("cn.eeepay.framework.dao.bill.SubOutBillDetailLogsMapper.BaseResultMap")
	List<SubOutBillDetailLogs> findOutBillDetailLogsList(@Param("subOutBillDetailLogs")SubOutBillDetailLogs subOutBillDetailLogs, @Param("sort")Sort sort,
			Page<SubOutBillDetailLogs> page);

	@SelectProvider( type=SqlProvider.class,method="exportOutBillDetailLogsList")
	@ResultMap("cn.eeepay.framework.dao.bill.SubOutBillDetailLogsMapper.BaseResultMap")
	List<SubOutBillDetailLogs> exportOutBillDetailLogsList(@Param("subOutBillDetailLogs")SubOutBillDetailLogs subOutBillDetailLogs);
	
	
	public class SqlProvider{
		
		
		public String findOutBillDetailLogsList(final Map<String, Object> parameter) {
			final SubOutBillDetailLogs subOutBillDetailLogs = (SubOutBillDetailLogs) parameter.get("subOutBillDetailLogs");
			final String acqOrgNo = (String) subOutBillDetailLogs.getAcqOrgNo();
			final String acqEnname = (String) subOutBillDetailLogs.getAcqEnname();
			final String outAmount1 = (String) subOutBillDetailLogs.getOutAmount1();
			final String outAmount2 = (String) subOutBillDetailLogs.getOutAmount2();
			final String startTime = (String) subOutBillDetailLogs.getStartTime();
			final String endTime = (String) subOutBillDetailLogs.getEndTime();
			final String transTimeStart = (String) subOutBillDetailLogs.getTransTimeStart();
			final String transTimesEnd = (String) subOutBillDetailLogs.getTransTimesEnd();
			final String merNos = (String) subOutBillDetailLogs.getMerNos();
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL() {{
				SELECT("*");
				FROM(" sub_out_bill_detail_logs ");
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
					WHERE(" acq_org_no = #{subOutBillDetailLogs.acqOrgNo} ");
				}
				if (StringUtils.isNotBlank(acqEnname)) {
					WHERE(" acq_enname = #{subOutBillDetailLogs.acqEnname} ");
				}
				if (StringUtils.isNotBlank(subOutBillDetailLogs.getOrderReferenceNo())) {
					WHERE(" order_reference_no like  \"%\"#{subOutBillDetailLogs.orderReferenceNo}\"%\" ");
				}
				if (StringUtils.isNotBlank(subOutBillDetailLogs.getOutBillDetailId())) {
					WHERE(" out_bill_detail_id like  \"%\"#{subOutBillDetailLogs.outBillDetailId}\"%\" ");
				}
				if (subOutBillDetailLogs.getOutBillId() != null) {
					WHERE(" out_bill_id = #{subOutBillDetailLogs.outBillId}");
				}
				if (subOutBillDetailLogs.getId() != null) {
					WHERE(" id like  \"%\"#{subOutBillDetailLogs.id}\"%\"");
				}
				if (StringUtils.isNotBlank(outAmount1)) {
					WHERE(" out_account_task_amount >= #{subOutBillDetailLogs.outAmount1} ");
				}
				if (StringUtils.isNotBlank(outAmount2)) {
					WHERE(" out_account_task_amount <= #{subOutBillDetailLogs.outAmount2} ");
				}
				if (StringUtils.isNotBlank(startTime)) {
					WHERE(" create_time >= #{subOutBillDetailLogs.startTime} ");
				}
				if (StringUtils.isNotBlank(endTime)) {
					WHERE(" create_time <= #{subOutBillDetailLogs.endTime} ");
				}
				if (StringUtils.isNotBlank(transTimeStart)) {
					WHERE(" trans_time >= #{subOutBillDetailLogs.transTimeStart} ");
				}
				if (StringUtils.isNotBlank(transTimesEnd)) {
					WHERE(" trans_time <= #{subOutBillDetailLogs.transTimesEnd} ");
				}
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
		}
		

		public String exportOutBillDetailLogsList(final Map<String, Object> parameter) {
			final SubOutBillDetailLogs subOutBillDetailLogs = (SubOutBillDetailLogs) parameter.get("subOutBillDetailLogs");
			final String acqOrgNo = (String) subOutBillDetailLogs.getAcqOrgNo();
			final String outAmount1 = (String) subOutBillDetailLogs.getOutAmount1();
			final String outAmount2 = (String) subOutBillDetailLogs.getOutAmount2();
			final String startTime = (String) subOutBillDetailLogs.getStartTime();
			final String endTime = (String) subOutBillDetailLogs.getEndTime();
			final String transTimeStart = (String) subOutBillDetailLogs.getTransTimeStart();
			final String transTimesEnd = (String) subOutBillDetailLogs.getTransTimesEnd();
			final String merNos = (String) subOutBillDetailLogs.getMerNos();
			return new SQL() {{
				SELECT("*");
				FROM(" sub_out_bill_detail_logs ");
				if (StringUtils.isNotBlank(merNos)) {
					if (!"-1".equals(merNos)) {
						StringBuilder sb = new StringBuilder();
						sb.append(" merchant_no in(");
						sb.append(merNos);
						sb.append(") ");
						WHERE(sb.toString());
					}
				}
				if (StringUtils.isNotBlank(acqOrgNo)) {
					WHERE(" acq_org_no = #{subOutBillDetailLogs.acqOrgNo} ");
				}
				if (StringUtils.isNotBlank(acqOrgNo)) {
					WHERE(" acq_enname = #{subOutBillDetailLogs.acqEnname} ");
				}
				if (StringUtils.isNotBlank(subOutBillDetailLogs.getOrderReferenceNo())) {
					WHERE(" order_reference_no like  \"%\"#{subOutBillDetailLogs.orderReferenceNo}\"%\" ");
				}
				if (StringUtils.isNotBlank(subOutBillDetailLogs.getOutBillDetailId())) {
					WHERE(" out_bill_detail_id like  \"%\"#{subOutBillDetailLogs.outBillDetailId}\"%\" ");
				}
				if (subOutBillDetailLogs.getOutBillId() != null) {
					WHERE(" out_bill_id = #{subOutBillDetailLogs.outBillId}");
				}
				if (subOutBillDetailLogs.getId() != null) {
					WHERE(" id like  \"%\"#{subOutBillDetailLogs.id}\"%\"");
				}
				if (StringUtils.isNotBlank(outAmount1)) {
					WHERE(" out_account_task_amount >= #{subOutBillDetailLogs.outAmount1} ");
				}
				if (StringUtils.isNotBlank(outAmount2)) {
					WHERE(" out_account_task_amount <= #{subOutBillDetailLogs.outAmount2} ");
				}
				if (StringUtils.isNotBlank(startTime)) {
					WHERE(" create_time >= #{subOutBillDetailLogs.startTime} ");
				}
				if (StringUtils.isNotBlank(endTime)) {
					WHERE(" create_time <= #{subOutBillDetailLogs.endTime} ");
				}
				if (StringUtils.isNotBlank(transTimeStart)) {
					WHERE(" trans_time >= #{subOutBillDetailLogs.transTimeStart} ");
				}
				if (StringUtils.isNotBlank(transTimesEnd)) {
					WHERE(" trans_time <= #{subOutBillDetailLogs.transTimesEnd} ");
				}
				
			}}.toString();
		}
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","outBillDetailId","outBillId","settleTime","transTime","transAmount","orderReferenceNo","outAccountNote","recordStatus","outBillStatus","verifyFlag","verifyMsg","acqEnname","acqOrgNo","merchantNo","merchantBalance","outAccountTaskAmount","changeRemark","createTime"};
		    final String[] columns={"id","out_bill_detail_id","out_bill_id","settle_time","trans_time","trans_amount","order_reference_no","out_account_note","record_status","out_bill_status","verify_flag","verify_msg","acq_enname","acq_org_no","merchant_no","merchant_balance","out_account_task_amount","change_remark","create_time"};
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
