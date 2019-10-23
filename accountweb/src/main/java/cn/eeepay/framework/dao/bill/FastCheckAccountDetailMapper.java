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
import cn.eeepay.framework.model.bill.FastCheckAccDetail;
import cn.eeepay.framework.model.nposp.CollectiveTransOrder;

/**
 * 对账明细表
 * @author yt
 *
 */
public interface FastCheckAccountDetailMapper {
	/**
	 * 新增对账明细
	 * @param subject
	 * @return
	 */
	@Insert("insert into fast_check_account_detail(check_batch_no,acq_order_no,acq_trans_type,acq_trans_order_no,acq_order_time,acq_order_status,acq_trans_amount,acq_refund_amount,acq_check_date,acq_enname,"
			+ "plate_order_no,plate_agent_no,plate_merchant_no,plate_trans_amount,plate_acq_merchant_fee,plate_merchant_fee,plate_agent_fee,plate_order_time,plate_trans_type,plate_trans_status,"
			+ "plate_trans_id,check_account_status,create_time,error_handle_status,error_handle_creator,record_status,settlement_method,settle_status,account)"
			+"values(#{dui.checkBatchNo},#{dui.acqOrderNo},#{dui.acqTransType},#{dui.acqTransOrderNo},#{dui.acqOrderTime},#{dui.acqOrderStatus},#{dui.acqTransAmount},#{dui.acqRefundAmount},#{dui.acqCheckDate},#{dui.acqEnname},"
			+ "#{dui.plateOrderNo},#{dui.plateAgentNo},#{dui.plateMerchantNo},#{dui.plateTransAmount},#{dui.plateAcqMerchantFee},#{dui.plateMerchantFee},#{dui.plateAgentFee},#{dui.plateOrderTime},"
			+ "#{dui.plateTransType},#{dui.plateTransStatus},#{dui.plateTransId},#{dui.checkAccountStatus},now(),#{dui.errorHandleStatus},#{dui.errorHandleCreator},#{dui.recordStatus},#{dui.settlementMethod},#{dui.settleStatus},#{dui.account})"
			)
	int saveFastCheckAccountDetail(@Param("dui")FastCheckAccDetail dui);
	
	@Select("select * from fast_check_account_detail where check_batch_no = #{checkBatchNo} and error_handle_status != #{status}")
	@ResultMap("cn.eeepay.framework.dao.bill.FastCheckAccountDetailMapper.BaseResultMap")
	List<FastCheckAccDetail> findByCheckBatchNoAndErrorHandleSatus(@Param("checkBatchNo")String checkBatchNo, @Param("status")String status);
	
	@Delete("delete from fast_check_account_detail where check_batch_no = #{checkBatchNo} ")
	int deleteByCheckBatchNo(@Param("checkBatchNo")String checkBatchNo);
	
	@Select("select * from fast_check_account_detail where plate_order_no = #{transInfo.orderNo} and check_account_status = 'SUCCESS'")
	@ResultMap("cn.eeepay.framework.dao.bill.FastCheckAccountDetailMapper.BaseResultMap")
	FastCheckAccDetail findDuiAccountDetailByTransInfo(@Param("transInfo")CollectiveTransOrder transInfo);
	
	@Select("select * from fast_check_account_detail where check_batch_no = #{checkBatchNo} and check_account_status != 'SUCCESS' and record_status in (0,2) and (error_handle_status='pendingTreatment' or error_handle_status='accountFailed')")
	@ResultMap("cn.eeepay.framework.dao.bill.FastCheckAccountDetailMapper.BaseResultMap")
	List<FastCheckAccDetail> findErrorDuiAccountDetailList2(@Param("checkBatchNo")String checkBatchNo);
	
	@Update("update fast_check_account_detail set error_handle_status=#{dui.errorHandleStatus},error_msg=#{dui.errorMsg}, record_status=#{dui.recordStatus} where id=#{dui.id}")
	int updateDuiAccount(@Param("dui")FastCheckAccDetail dui);
	
	//===========================对账差错处理的六个按钮具体功能的实现开始====================
	//查询对账详细信息 通过  id
	@Select("SELECT * FROM fast_check_account_detail WHERE id = #{id}")
	@ResultMap("cn.eeepay.framework.dao.bill.FastCheckAccountDetailMapper.BaseResultMap")
	FastCheckAccDetail findDuiAccountDetailById(@Param("id")String id) ;
	
	/**
	 * 查询对账详细信息列表
	 * @param duiAccountDetail
	 * @param sort
	 * @param page
	 * @return
	 */
	@SelectProvider( type=SqlProvider.class,method="findQueryDuiAccountDetailList")
	@ResultType(FastCheckAccDetail.class)
	List<FastCheckAccDetail> findQueryDuiAccountDetailList(@Param("createTimeStart")String createTimeStart,@Param("createTimeEnd")String createTimeEnd,
			@Param("duiAccountDetail")FastCheckAccDetail duiAccountDetail, @Param("sort")Sort sort, Page<FastCheckAccDetail> page);
	
	/**
	 * 查询差错对账详细信息列表
	 * @param duiAccountDetail
	 * @param sort
	 * @param page
	 * @return
	 */
	@SelectProvider( type=SqlProvider.class,method="findErrorDuiAccountDetailList")
	@ResultMap("cn.eeepay.framework.dao.bill.FastCheckAccountDetailMapper.BaseResultMap")
	List<FastCheckAccDetail> findErrorDuiAccountDetailList(@Param("createTimeStart")String createTimeStart,@Param("createTimeEnd")String createTimeEnd,
			@Param("duiAccountDetail")FastCheckAccDetail duiAccountDetail, @Param("sort")Sort sort, Page<FastCheckAccDetail> page);
	
	/**
	 * 查询对账详细信息（用来导出）
	 * @param duiAccountDetail
	 * @return
	 */
	@SelectProvider( type=SqlProvider.class,method="findExportDuiAccountDetailList")
	@ResultMap("cn.eeepay.framework.dao.bill.FastCheckAccountDetailMapper.BaseResultMap")
	List<FastCheckAccDetail> findExportDuiAccountDetailList(@Param("createTimeStart")String createTimeStart,@Param("createTimeEnd")String createTimeEnd,
			@Param("duiAccountDetail")FastCheckAccDetail duiAccountDetail);
	
	@SelectProvider( type=SqlProvider.class,method="findErrorExportDuiAccountDetailList")
	@ResultMap("cn.eeepay.framework.dao.bill.FastCheckAccountDetailMapper.BaseResultMap")
	List<FastCheckAccDetail> findErrorExportDuiAccountDetailList(@Param("createTimeStart")String createTimeStart,@Param("createTimeEnd")String createTimeEnd,
			@Param("duiAccountDetail")FastCheckAccDetail duiAccountDetail);
	
	@Update("UPDATE fast_check_account_detail set remark=#{detail.remark} where id=#{detail.id}")
	int updateRemark(@Param("detail")FastCheckAccDetail detail);
	
	public class SqlProvider{
		
		public String findErrorDuiAccountDetailList(final Map<String, Object> parameter) {
			final FastCheckAccDetail duiAccountDetail = (FastCheckAccDetail) parameter.get("duiAccountDetail");
			final Sort sord=(Sort)parameter.get("sort");
			//日期格式的处理，查询时去掉createTime 的时间部分
			final String createTimeStart = (String)parameter.get("createTimeStart") ;
			final String createTimeEnd = (String)parameter.get("createTimeEnd") ;
			return new SQL(){{
				SELECT(" d.*");
				FROM(" fast_check_account_detail d");
				WHERE(" d.check_account_status !='SUCCESS' ");
				if (!StringUtils.isBlank(duiAccountDetail.getCheckBatchNo()) )
					WHERE(" d.check_batch_no like  \"%\"#{duiAccountDetail.checkBatchNo}\"%\" ");
				if (StringUtils.isNotBlank(createTimeStart) )
					WHERE(" d.create_time >=  #{createTimeStart} ");
				if (StringUtils.isNotBlank(createTimeEnd) )
					WHERE(" d.create_time <=  #{createTimeEnd} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqTransSerialNo()))
					WHERE(" d.acq_trans_serial_no like  \"%\"#{duiAccountDetail.acqTransSerialNo}\"%\" ");
				if (StringUtils.isNotBlank(duiAccountDetail.getPlateTransId()))
					WHERE(" d.plate_trans_id =  #{duiAccountDetail.plateTransId}");
				if (StringUtils.isNotBlank(duiAccountDetail.getAcqOrderNo()))
					WHERE(" d.acq_order_no like \"%\"#{duiAccountDetail.acqOrderNo}\"%\" ");
				if (StringUtils.isNotBlank(duiAccountDetail.getAcqTransOrderNo()))
					WHERE(" d.acq_trans_order_no like \"%\"#{duiAccountDetail.acqTransOrderNo}\"%\" ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateMerchantNo()))
					WHERE(" d.plate_merchant_no like  \"%\"#{duiAccountDetail.plateMerchantNo}\"%\" ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateTransType()) && !"ALL".equals(duiAccountDetail.getPlateTransType()))
					WHERE(" d.plate_trans_type like  \"%\"#{duiAccountDetail.plateTransType}\"%\" ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateTransStatus()) && !"ALL".equals(duiAccountDetail.getPlateTransStatus()))
					WHERE(" d.plate_trans_status like  \"%\"#{duiAccountDetail.plateTransStatus}\"%\" ");
				if (!StringUtils.isBlank(duiAccountDetail.getErrorHandleStatus()) && !"ALL".equals(duiAccountDetail.getErrorHandleStatus()))
					WHERE(" d.error_handle_status like  \"%\"#{duiAccountDetail.errorHandleStatus}\"%\" ");
				if (!StringUtils.isBlank(duiAccountDetail.getCheckAccountStatus()) && !"ALL".equals(duiAccountDetail.getCheckAccountStatus()))
					WHERE(" d.check_account_status = #{duiAccountDetail.checkAccountStatus} ");
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
		}
		
		public String findQueryDuiAccountDetailList(final Map<String, Object> parameter) {
			final FastCheckAccDetail duiAccountDetail = (FastCheckAccDetail) parameter.get("duiAccountDetail");
			final Sort sord=(Sort)parameter.get("sort");
			//日期格式的处理，查询时去掉createTime 的时间部分
			final String createTimeStart = (String)parameter.get("createTimeStart") ;
			final String createTimeEnd = (String)parameter.get("createTimeEnd") ;
			return new SQL(){{
				SELECT(" d.*,(IFNULL(d.plate_trans_amount,0)-IFNULL(d.plate_merchant_fee,0))as taskAmount");
				FROM(" fast_check_account_detail d");
				if (!StringUtils.isBlank(duiAccountDetail.getCheckBatchNo()) )
					WHERE(" d.check_batch_no like  \"%\"#{duiAccountDetail.checkBatchNo}\"%\" ");
				if (StringUtils.isNotBlank(createTimeStart) )
					WHERE(" d.create_time >=  #{createTimeStart} ");
				if (StringUtils.isNotBlank(createTimeEnd) )
					WHERE(" d.create_time <=  #{createTimeEnd} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqTransSerialNo()))
					WHERE(" d.acq_trans_serial_no like  \"%\"#{duiAccountDetail.acqTransSerialNo}\"%\" ");
				if (StringUtils.isNotBlank(duiAccountDetail.getPlateTransId()))
					WHERE(" d.plate_trans_id =  #{duiAccountDetail.plateTransId}");
				if (StringUtils.isNotBlank(duiAccountDetail.getAcqOrderNo()))
					WHERE(" d.acq_order_no like \"%\"#{duiAccountDetail.acqOrderNo}\"%\" ");
				if (StringUtils.isNotBlank(duiAccountDetail.getAcqTransOrderNo()))
					WHERE(" d.acq_trans_order_no like \"%\"#{duiAccountDetail.acqTransOrderNo}\"%\" ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateMerchantNo()))
					WHERE(" d.plate_merchant_no like  \"%\"#{duiAccountDetail.plateMerchantNo}\"%\" ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateTransType()) && !"ALL".equals(duiAccountDetail.getPlateTransType()))
					WHERE(" d.plate_trans_type like  \"%\"#{duiAccountDetail.plateTransType}\"%\" ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateTransStatus()) && !"ALL".equals(duiAccountDetail.getPlateTransStatus()))
					WHERE(" d.plate_trans_status like  \"%\"#{duiAccountDetail.plateTransStatus}\"%\" ");
				if (!StringUtils.isBlank(duiAccountDetail.getErrorHandleStatus()) && !"ALL".equals(duiAccountDetail.getErrorHandleStatus()))
					WHERE(" d.error_handle_status like  \"%\"#{duiAccountDetail.errorHandleStatus}\"%\" ");
				if (!StringUtils.isBlank(duiAccountDetail.getCheckAccountStatus()) && !"ALL".equals(duiAccountDetail.getCheckAccountStatus()))
					WHERE(" d.check_account_status like  \"%\"#{duiAccountDetail.checkAccountStatus}\"%\" ");
				if ( duiAccountDetail.getRecordStatus() != null && duiAccountDetail.getRecordStatus() != -1)
					WHERE(" d.record_status = #{duiAccountDetail.recordStatus} ");
				if (StringUtils.isNotBlank(duiAccountDetail.getSettlementMethod()) && !"ALL".equals(duiAccountDetail.getSettlementMethod()))
					WHERE(" d.settlement_method = #{duiAccountDetail.settlementMethod}");
				if (duiAccountDetail.getSettleStatus() != null && !duiAccountDetail.getSettleStatus().equals(-1))
					WHERE(" d.settle_status = #{duiAccountDetail.settleStatus}");
				if (duiAccountDetail.getAccount() != null && !duiAccountDetail.getAccount().equals(-1))
					WHERE(" d.account = #{duiAccountDetail.account}");
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
		}
		
		public String findExportDuiAccountDetailList(final Map<String, Object> parameter) {
			final FastCheckAccDetail duiAccountDetail = (FastCheckAccDetail) parameter.get("duiAccountDetail");
			//日期格式的处理，查询时去掉createTime 的时间部分
			final String createTimeStart = (String)parameter.get("createTimeStart") ;
			final String createTimeEnd = (String)parameter.get("createTimeEnd") ;
			return new SQL(){{
				SELECT("*");
				FROM(" fast_check_account_detail ");
				if (!StringUtils.isBlank(duiAccountDetail.getCheckBatchNo()) )
					WHERE(" check_batch_no like  \"%\"#{duiAccountDetail.checkBatchNo}\"%\" ");
				if (StringUtils.isNotBlank(createTimeStart) )
					WHERE(" create_time >=  #{createTimeStart} ");
				if (StringUtils.isNotBlank(createTimeEnd) )
					WHERE(" create_time <=  #{createTimeEnd} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqTransSerialNo()))
					WHERE(" acq_trans_serial_no like  \"%\"#{duiAccountDetail.acqTransSerialNo}\"%\" ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateMerchantNo()))
					WHERE(" plate_merchant_no like  \"%\"#{duiAccountDetail.plateMerchantNo}\"%\" ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateTransType()) && !"ALL".equals(duiAccountDetail.getPlateTransType()))
					WHERE(" plate_trans_type like  \"%\"#{duiAccountDetail.plateTransType}\"%\" ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateTransStatus()) && !"ALL".equals(duiAccountDetail.getPlateTransStatus()))
					WHERE(" plate_trans_status like  \"%\"#{duiAccountDetail.plateTransStatus}\"%\" ");
				if (!StringUtils.isBlank(duiAccountDetail.getCheckAccountStatus()) && !"ALL".equals(duiAccountDetail.getCheckAccountStatus()))
					WHERE(" check_account_status =#{duiAccountDetail.checkAccountStatus} ");
				if (StringUtils.isNotBlank(duiAccountDetail.getSettlementMethod()) && !"ALL".equals(duiAccountDetail.getSettlementMethod()))
					WHERE(" settlement_method = #{duiAccountDetail.settlementMethod}");
				if (duiAccountDetail.getSettleStatus() != null && !duiAccountDetail.getSettleStatus().equals(-1))
					WHERE(" settle_status = #{duiAccountDetail.settleStatus}");
				if (duiAccountDetail.getAccount() != null && !duiAccountDetail.getAccount().equals(-1))
					WHERE(" account = #{duiAccountDetail.account}");
				if (duiAccountDetail.getRecordStatus() != null && duiAccountDetail.getRecordStatus() != -1)
					WHERE(" record_status = #{duiAccountDetail.recordStatus} ");
			}}.toString();
		}
		
		public String findErrorExportDuiAccountDetailList(final Map<String, Object> parameter) {
			final FastCheckAccDetail duiAccountDetail = (FastCheckAccDetail) parameter.get("duiAccountDetail");
			//日期格式的处理，查询时去掉createTime 的时间部分
			final String createTimeStart = (String)parameter.get("createTimeStart") ;
			final String createTimeEnd = (String)parameter.get("createTimeEnd") ;
			return new SQL(){{
				SELECT("*");
				FROM(" fast_check_account_detail ");
				WHERE(" check_account_status !='SUCCESS' ");
				if (!StringUtils.isBlank(duiAccountDetail.getCheckBatchNo()) )
					WHERE(" check_batch_no like  \"%\"#{duiAccountDetail.checkBatchNo}\"%\" ");
				if (StringUtils.isNotBlank(createTimeStart) )
					WHERE(" create_time >=  #{createTimeStart} ");
				if (StringUtils.isNotBlank(createTimeEnd) )
					WHERE(" create_time <=  #{createTimeEnd} ");
				if (!StringUtils.isBlank(duiAccountDetail.getAcqTransSerialNo()))
					WHERE(" acq_trans_serial_no like  \"%\"#{duiAccountDetail.acqTransSerialNo}\"%\" ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateMerchantNo()))
					WHERE(" plate_merchant_no like  \"%\"#{duiAccountDetail.plateMerchantNo}\"%\" ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateTransType()) && !"ALL".equals(duiAccountDetail.getPlateTransType()))
					WHERE(" plate_trans_type like  \"%\"#{duiAccountDetail.plateTransType}\"%\" ");
				if (!StringUtils.isBlank(duiAccountDetail.getPlateTransStatus()) && !"ALL".equals(duiAccountDetail.getPlateTransStatus()))
					WHERE(" plate_trans_status like  \"%\"#{duiAccountDetail.plateTransStatus}\"%\" ");
				if (!StringUtils.isBlank(duiAccountDetail.getCheckAccountStatus()) && !"ALL".equals(duiAccountDetail.getCheckAccountStatus())) {
					WHERE(" check_account_status =#{duiAccountDetail.checkAccountStatus} ");
				}
			}}.toString();
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
	}
}
