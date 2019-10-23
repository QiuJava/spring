package cn.eeepay.framework.dao.nposp;

import cn.eeepay.framework.model.bill.DuiAccountDetail;
import cn.eeepay.framework.model.bill.FastCheckAccDetail;
import cn.eeepay.framework.model.nposp.CollectiveTransOrder;
import cn.eeepay.framework.model.nposp.TransInfo;
import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 交易流水表
 * @author Administrator
 *
 */
public interface TransInfoMapper {
	
	@Update("update trans_info set acc_status=1  where order_no=#{orderReferenceNo}")
	int updateTransInfoAccStatus(@Param("orderReferenceNo")String orderReferenceNo);

	@Update("update trans_info set acc_status=1  where acq_reference_no=#{acqReferenceNo} and acq_serial_no = #{acqSerNo} and acq_merchant_no = #{acqaMerNo} and acq_terminal_no = #{acqTerNo}")
	int updateTransInfoAcc(@Param("acqEnname") String acqEnname,@Param("acqReferenceNo")String acqReferenceNo,@Param("acqMerNo")String acqMerNo,@Param("acqSerNo")String acqSerNo,@Param("acqTerNo")String acqTerNo);

	@Update("update collective_trans_order set trans_status='SUCCESS',settle_status = '4',profits_1 = 0 where order_no=#{ct.orderNo}")
	int updateCollectiveOrder(@Param("ct") CollectiveTransOrder ct);

	@Update("update trans_info set trans_status='SUCCESS',acq_reference_no=#{dui.acqReferenceNo} ,acq_terminal_no=#{dui.acqTerminalNo} where order_no=#{ct.orderNo}")
	int updateTransInfo(@Param("ct") CollectiveTransOrder ct,@Param("dui")DuiAccountDetail dui);

	@Update("update scan_code_trans set trade_state='SUCCESS',result_code=0 where trade_no=#{ct.orderNo}")
	int updateScanCodeTrans(@Param("ct") CollectiveTransOrder ct);

	@Update("update trans_info set acc_status=1  where id = #{id}")
	int updateTransInfoAccById(@Param("id")String id);


	@SelectProvider( type=SqlProvider.class,method="findAcqTransInfoByParams")
	@ResultMap("cn.eeepay.framework.dao.nposp.TransInfoMapper.BaseResultMap")
	TransInfo findAcqTransInfoByParams(@Param("transType")String transType,@Param("acqMerchantNo")String acqMerchantNo, @Param("acqTerminalNo")String acqTerminalNo, @Param("acqBatchNo")String acqBatchNo, 
			@Param("acqSerialNo")String acqSerialNo, @Param("acqAccountNo")String acqAccountNo, @Param("acqAccountNoEnd")String acqAccountNoEnd,@Param("acqEnname")String acqEnname,@Param("acqReferenceNo")String acqReferenceNo);
	
	@SelectProvider( type=SqlProvider.class,method="findAcqTransInfoRyxByParams")
	@ResultMap("cn.eeepay.framework.dao.nposp.TransInfoMapper.BaseResultMap")
	TransInfo findAcqTransInfoRyxByParams(@Param("transType")String transType,@Param("acqReferenceNo")String acqReferenceNo,@Param("acqEnname")String acqEnname,
			@Param("transAmount")BigDecimal transAmount, @Param("acqMerchantNo")String acqMerchantNo,@Param("accountNo")String accountNo);
	
	@SelectProvider( type=SqlProvider.class,method="findRefundByAcqTransInfo")
	@ResultMap("cn.eeepay.framework.dao.nposp.TransInfoMapper.BaseResultMap")
	TransInfo findRefundByAcqTransInfo(@Param("acqMerchantNo")String acqMerchantNo, @Param("acqTerminalNo")String acqTerminalNo, @Param("acqBatchNo")String acqBatchNo, 
			@Param("acqSerialNo")String acqSerialNo, @Param("acqAccountNo")String acqAccountNo,@Param("acqEnname")String acqEnname,@Param("acqReferenceNo")String acqReferenceNo,
			@Param("transType")String transType);
	
	@SelectProvider( type=SqlProvider.class,method="findCheckData")
	@ResultMap("cn.eeepay.framework.dao.nposp.TransInfoMapper.BaseResultMap")
	List<TransInfo> findCheckData(@Param("acqEnname")String acqEnname,@Param("transType")String transType, @Param("transTimeBegin")String transTimeBegin, @Param("transTimeEnd")String transTimeEnd);
	
	
	@Select("SELECT * FROM trans_info WHERE  acq_terminal_no = #{duiAccountDetail.acqTerminalNo} "
			+ "AND acq_batch_no = #{duiAccountDetail.acqBatchNo} AND acq_serial_no = #{duiAccountDetail.acqSerialNo} "
			+ "AND account_no = #{duiAccountDetail.acqAccountNo}  "
			+ "AND acq_enname = #{duiAccountDetail.acqEnname}")
	@ResultMap("cn.eeepay.framework.dao.nposp.TransInfoMapper.BaseResultMap")
	TransInfo findTransInfoByDuiAccountDetail(@Param("duiAccountDetail")DuiAccountDetail duiAccountDetail);
	
	@Select("SELECT ti.*,cto.profits_1 FROM trans_info ti left join collective_trans_order cto on cto.order_no=ti.order_no  WHERE  ti.acq_terminal_no = #{duiAccountDetail.acqTerminalNo} "
			+ "AND ti.acq_batch_no = #{duiAccountDetail.acqBatchNo} AND ti.acq_serial_no = #{duiAccountDetail.acqSerialNo} "
			+ "AND ti.account_no = #{duiAccountDetail.acqAccountNo}  "
			+ "AND ti.acq_enname = #{duiAccountDetail.acqEnname}")
	@ResultMap("cn.eeepay.framework.dao.nposp.TransInfoMapper.BaseResultMap")
	TransInfo findTransInfoByFastDuiAccountDetail(@Param("duiAccountDetail")FastCheckAccDetail duiAccountDetail);
	
	@Select("SELECT ti.*,cto.profits_1 FROM trans_info ti left join collective_trans_order cto on cto.order_no=ti.order_no WHERE ti.id=#{plateTransId} "
			+ "AND ti.acq_enname = #{acqEnname}")
	@ResultMap("cn.eeepay.framework.dao.nposp.TransInfoMapper.BaseResultMap")
	TransInfo findErrorTransInfoByDuiAccountDetail(@Param("plateTransId")String plateTransId, @Param("acqEnname")String acqEnname);
	
	
	@Select("SELECT * FROM trans_info WHERE  acq_terminal_no = #{duiAccountDetail.plateAcqTerminalNo} "
			+ "AND acq_batch_no = #{duiAccountDetail.plateAcqBatchNo} AND acq_serial_no = #{duiAccountDetail.plateAcqSerialNo} "
			+ "AND account_no = #{duiAccountDetail.plateAccountNo}  "
			+ "AND acq_enname = #{duiAccountDetail.acqEnname}")
	@ResultMap("cn.eeepay.framework.dao.nposp.TransInfoMapper.BaseResultMap")
	TransInfo findErrorTransInfoByFastDuiAccountDetail(@Param("duiAccountDetail")FastCheckAccDetail duiAccountDetail);
	
	public class SqlProvider{
		
		public String findCheckData(final Map<String, Object> parameter) {
			final String acqEnname = (String) parameter.get("acqEnname");
			final String transTimeBegin = (String) parameter.get("transTimeBegin");
			final String transTimeEnd = (String) parameter.get("transTimeEnd");
			final String transType = (String) parameter.get("transType");
			return new SQL(){{
				SELECT(" ti.*,cto.account ");
				FROM("trans_info ti left join collective_trans_order cto on ti.order_no=cto.order_no");
				WHERE(" ti.trans_status = 'SUCCESS' ");
				if (StringUtils.isNotBlank(transType))
					WHERE(" ti.trans_type  = #{transType} ");
				if (StringUtils.isNotBlank(acqEnname))
					WHERE(" ti.acq_enname  = #{acqEnname} ");
				if (StringUtils.isNotBlank(transTimeBegin) && StringUtils.isNotBlank(transTimeEnd))
					WHERE(" ti.trans_time between #{transTimeBegin} and #{transTimeEnd} ");
			}}.toString();
		}
		
		public String findRefundByAcqTransInfo(final Map<String, Object> parameter) {
			final String acqMerchantNo = (String) parameter.get("acqMerchantNo");
			final String acqTerminalNo = (String) parameter.get("acqTerminalNo");
			final String acqBatchNo = (String) parameter.get("acqBatchNo");
			final String acqSerialNo = (String) parameter.get("acqSerialNo");
			final String acqAccountNo = (String) parameter.get("acqAccountNo");
			final String acqEnname = (String) parameter.get("acqEnname");
			final String acqReferenceNo = (String) parameter.get("acqReferenceNo");
			final String transType = (String) parameter.get("transType");
			return new SQL(){{
				SELECT(" * ");
				FROM("trans_info ");
				if (StringUtils.isNotBlank(acqReferenceNo))
					WHERE(" acq_reference_no  = #{acqReferenceNo} ");
				if (StringUtils.isNotBlank(acqEnname))
					WHERE(" acq_enname  = #{acqEnname} ");
				if (StringUtils.isNotBlank(transType))
					WHERE(" trans_type  = #{transType} ");
				if (StringUtils.isNotBlank(acqMerchantNo))
					WHERE(" acq_merchant_no  = #{acqMerchantNo} ");
				if (StringUtils.isNotBlank(acqTerminalNo))
					WHERE(" acq_terminal_no  = #{acqTerminalNo} ");
				if (StringUtils.isNotBlank(acqSerialNo))
					WHERE(" acq_serial_no  = #{acqSerialNo} ");
				if (StringUtils.isNotEmpty(acqBatchNo)) {
					WHERE(" and acq_batch_no = #{acqBatchNo} ");
				}
				if(!"bill".equals(acqEnname)){
					WHERE(" and account_no = #{acqAccountNo} ");
				}
			}}.toString();
		}
		
		public String findAcqTransInfoByParams(final Map<String, Object> parameter) {
			final String acqMerchantNo = (String) parameter.get("acqMerchantNo");
			final String acqTerminalNo = (String) parameter.get("acqTerminalNo");
			final String acqBatchNo = (String) parameter.get("acqBatchNo");
			final String acqSerialNo = (String) parameter.get("acqSerialNo");
			final String acqAccountNo = (String) parameter.get("acqAccountNo");
			final String acqEnname = (String) parameter.get("acqEnname");
			final String acqReferenceNo = (String) parameter.get("acqReferenceNo");
			final String transType = (String) parameter.get("transType");
			final String acqAccountNoEnd = (String) parameter.get("acqAccountNoEnd");
			return new SQL(){{
				SELECT(" *");
				FROM("trans_info ");
				WHERE(" acq_enname = #{acqEnname}");
				WHERE(" trans_type = #{transType}");
				WHERE(" acq_terminal_no = #{acqTerminalNo}");
				WHERE(" acq_serial_no = #{acqSerialNo}");
				if (StringUtils.isNotBlank(acqBatchNo))
					WHERE(" acq_batch_no  = #{acqBatchNo} ");
				if("hypay".equals(acqEnname) || "halpay".equals(acqEnname)||"xdjkpay".equals(acqEnname)||"xmcmbc".equals(acqEnname)){
					WHERE(" substring(account_no,length(account_no)-3,length(account_no)) = #{acqAccountNoEnd}");
				}else{
					WHERE(" account_no = #{acqAccountNo} ");
				}
				if(!"zypay".equals(acqEnname) && !"neweptok".equals(acqEnname) && !"YS_ZQ".equals(acqEnname)){
					WHERE(" acq_merchant_no = #{acqMerchantNo} ");
				}
				if("bypay".equals(acqEnname)){
					WHERE(" trans_id  = #{acqReferenceNo} ");
				}else{
					WHERE(" acq_reference_no  = #{acqReferenceNo} ");
				}
			}}.toString();
		}
		
		public String findAcqTransInfoRyxByParams(final Map<String, Object> parameter) {
			final String acqReferenceNo = (String) parameter.get("acqReferenceNo");
			final String acqEnname = (String) parameter.get("acqEnname");
			final String transAmount = (String) parameter.get("transAmount");
			final String acqMerchantNo = (String) parameter.get("acqMerchantNo");
			final String accountNo = (String) parameter.get("accountNo");
			final String transType = (String) parameter.get("transType");
			return new SQL(){{
				SELECT(" *");
				FROM("trans_info ");
				if (StringUtils.isNotBlank(acqReferenceNo))
					WHERE(" acq_reference_no  = #{acqReferenceNo} ");
				if (StringUtils.isNotBlank(acqEnname))
					WHERE(" acq_enname  = #{acqEnname} ");
				if (StringUtils.isNotBlank(transAmount))
					WHERE(" trans_amount  = #{transAmount} ");
				if (StringUtils.isNotBlank(transType))
					WHERE(" trans_type  = #{transType} ");
				if (StringUtils.isNotBlank(accountNo))
					WHERE(" account_no  = #{accountNo} ");
				if (StringUtils.isNotBlank(acqMerchantNo))
					WHERE(" acq_merchant_no  = #{acqMerchantNo} ");
			}}.toString();
		}

		public String getTransData(final Map<String, Object> parameter) {
			final String orderReferenceNo = StringUtil.filterNull(parameter.get("orderReferenceNo"));
			final String acqEnname = StringUtil.filterNull( parameter.get("acqEnname"));
			final String acqSerNo =  StringUtil.filterNull(parameter.get("acqSerNo"));
			final String acqMerchantNo =  StringUtil.filterNull(parameter.get("acqMerchantNo"));
			final String acqTerNo = StringUtil.filterNull(parameter.get("acqTerNo"));
			final String acqAccountNo = StringUtil.filterNull(parameter.get("acqAccountNo"));
			return new SQL(){{
				SELECT(" ti.id,ti.order_no,ti.acq_enname,ti.acq_code,"
						+ " ti.acq_merchant_no,ti.acq_terminal_no, "
						+ " ti.acq_auth_no,ti.acq_reference_no, "
						+ " ti.acq_batch_no,ti.acq_serial_no, "
						+ " ti.acq_response_code,ti.agent_no,ti.merchant_no,"
						+ " ti.terminal_no,ti.batch_no,"
						+ " ti.serial_no,ti.account_no, "
						+ " ti.card_type,ti.currency_type, "
						+ " ti.trans_amount,ti.merchant_fee, "
						+ " ti.merchant_rate,ti.acq_merchant_fee, "
						+ " ti.acq_merchant_rate,ti.trans_type, "
						+ " ti.trans_status,ti.trans_source, "
						+ " ti.ori_acq_batch_no,ti.ori_acq_serial_no,"
						+ " ti.ori_batch_no,ti.ori_serial_no, "
						+ " ti.acq_settle_date,ti.merchant_settle_date, "
						+ " ti.my_settle,ti.review_status, "
						+ " ti.trans_time,ti.last_update_time, "
						+ " ti.create_time,ti.settle_err_code, "
						+ " ti.settle_err_msg,ti.belong_pay, "
						+ " cto.settlement_method,ti.bag_settle, "
						+ " ti.trans_msg,ti.cardholder_phone, "
						+ " ti.trans_id,cto.freeze_status,"
						+ " ti.sign_img,ti.device_sn,ti.sign_check_person,"
						+ " cto.freeze_status,"
						+ " ti.sign_check_time,ti.is_iccard,ti.ic_msg,ti.insurance,ti.expired,ti.pos_type,ti.msg_id,"
						+ " ti.acc_status,ti.issued_status,ti.service_id,ti.single_share_amount,ti.acq_service_id,ti.syn_status,ti.holidays_mark,cto.account,cto.settle_status,cto.settle_type "
						+ " from trans_info ti left join collective_trans_order cto on ti.order_no = cto.order_no");
					//	WHERE(" ti.trans_status = 'SUCCESS' ") ;
						WHERE(" ti.trans_type  = 'PURCHASE' ");
				if (StringUtils.isNotBlank(orderReferenceNo))
					WHERE(" ti.acq_reference_no=#{orderReferenceNo} ");
				if (StringUtils.isNotBlank(acqEnname))
					WHERE(" ti.acq_enname  = #{acqEnname} ");
				if (StringUtils.isNotBlank(acqSerNo))
					WHERE(" ti.acq_serial_no = #{acqSerNo} ");
				if (StringUtils.isNotBlank(acqTerNo))
					WHERE(" ti.acq_terminal_no = #{acqTerNo} ");
				if (StringUtils.isNotBlank(acqAccountNo))
					WHERE(" ti.account_no = #{acqAccountNo} ");
				if (!"neweptok".equals(acqEnname) && !"YS_ZQ".equals(acqEnname) && StringUtils.isNotBlank(acqMerchantNo))
					WHERE(" ti.acq_merchant_no  = #{acqMerchantNo} ");
			}}.toString();
		}

		public String getColletiveTransData(final Map<String, Object> parameter) {
			final String acqEnname = StringUtil.filterNull( parameter.get("acqEnname"));
			final String acqSerNo =  StringUtil.filterNull(parameter.get("acqSerNo"));
			final String acqMerchantNo =  StringUtil.filterNull(parameter.get("acqMerchantNo"));
			final String acqTerNo = StringUtil.filterNull(parameter.get("acqTerNo"));
			final String acqAccountNo = StringUtil.filterNull(parameter.get("acqAccountNo"));
			return new SQL(){{
				SELECT(" cto.*,ti.acq_reference_no "
						+ " from trans_info ti left join collective_trans_order cto on ti.order_no = cto.order_no");
				//	WHERE(" ti.trans_status = 'SUCCESS' ") ;
				WHERE(" ti.trans_type  = 'PURCHASE' ");
				if (StringUtils.isNotBlank(acqEnname))
					WHERE(" ti.acq_enname  = #{acqEnname} ");
				if (StringUtils.isNotBlank(acqSerNo))
					WHERE(" ti.acq_serial_no = #{acqSerNo} ");
				if (StringUtils.isNotBlank(acqTerNo))
					WHERE(" ti.acq_terminal_no = #{acqTerNo} ");
				if (StringUtils.isNotBlank(acqAccountNo))
					WHERE(" ti.account_no = #{acqAccountNo} ");
				if (!"neweptok".equals(acqEnname)  && !"YS_ZQ".equals(acqEnname) && StringUtils.isNotBlank(acqMerchantNo))
					WHERE(" ti.acq_merchant_no  = #{acqMerchantNo} ");
			}}.toString();
		}

		public String getCollectiveTransDataForT1(final Map<String, Object> parameter) {
			final String acqEnname = StringUtil.filterNull( parameter.get("acqEnname"));
			final String acqSerNo =  StringUtil.filterNull(parameter.get("acqSerNo"));
			final String acqMerchantNo =  StringUtil.filterNull(parameter.get("acqMerchantNo"));
//			final String acqTerNo = StringUtil.filterNull(parameter.get("acqTerNo"));
			final String acqAccountNo = StringUtil.filterNull(parameter.get("acqAccountNo"));
			final String acqTransAmount = StringUtil.filterNull(parameter.get("acqTransAmount"));
			return new SQL(){{
				SELECT(" ti.id  , ti.acq_auth_no,zq.terminal_no,ti.agent_no," +
						"ti.merchant_no,ti.terminal_no,ti.acq_batch_no,ti.acq_serial_no,ti.account_no,ti.batch_no," +
						"ti.serial_no,ti.account_no,ti.trans_amount,ti.acq_reference_no,ti.trans_time," +
						"ti.trans_type,ti.trans_status,ti.acq_merchant_fee,ti.merchant_fee,ti.acq_merchant_rate," +
						"ti.merchant_rate,ti.trans_source,ti.bag_settle,ti.pos_type,ti.id,ti.order_no," +
						"ti.single_share_amount,cto.settle_status,cto.settlement_method,cto.account," +
						"ti.acq_enname,cto.freeze_status, cto.settle_type, zq.unionpay_mer_no, zq.mbp_id "
						+ " from trans_info ti left join collective_trans_order cto on ti.order_no = cto.order_no" +
						"  LEFT JOIN zq_merchant_info zq ON ti.merchant_no=zq.merchant_no  and cto.unionpay_mer_no = zq.unionpay_mer_no ");
				//	WHERE(" ti.trans_status = 'SUCCESS' ") ;
				WHERE(" ti.trans_type  = 'PURCHASE' ");
				if (StringUtils.isNotBlank(acqEnname))
					WHERE(" ti.acq_enname  = #{acqEnname} ");
				if (StringUtils.isNotBlank(acqSerNo))
					WHERE(" ti.acq_serial_no = #{acqSerNo} ");
//				if (StringUtils.isNotBlank(acqTerNo))
//					WHERE(" ti.acq_terminal_no = #{acqTerNo} ");
				if (StringUtils.isNotBlank(acqAccountNo))
					WHERE(" ti.account_no = #{acqAccountNo} ");
				if (StringUtils.isNotBlank(acqTransAmount))
					WHERE(" ti.trans_amount = #{acqTransAmount} ");
				if (("neweptok".equals(acqEnname) || "YS_ZQ".equals(acqEnname)) && StringUtils.isNotBlank(acqMerchantNo))
					WHERE(" cto.unionpay_mer_no  = #{acqMerchantNo} ");
				ORDER_BY( "  ti.id  desc LIMIT 1  ");

			}}.toString();
		}


		public String getAcqTerminalStore(final Map<String, Object> parameter) {

			final String unionMerNo = StringUtil.filterNull( parameter.get("unionMerNo"));

			return new SQL(){{
				SELECT(" ter_no  ");
				FROM(" acq_terminal_store ");
				WHERE(" union_mer_no = #{unionMerNo} ");
				ORDER_BY(" id desc LIMIT 1 ") ;
			}}.toString();
		}
		
	}

	@Select("select  ti.*,cto.account  from  trans_info ti left join collective_trans_order "
			+ "cto on ti.order_no = cto.order_no where ti.trans_status = 'SUCCESS' "
			+ "and ti.trans_type  = 'PURCHASE' and ti.acq_enname  = #{acqEnname} and ti.acq_reference_no=#{orderReferenceNo}")
	@ResultMap("cn.eeepay.framework.dao.nposp.TransInfoMapper.BaseResultMap")
	TransInfo queryNeweptokTransOrder(@Param("orderReferenceNo")String orderReferenceNo, @Param("acqEnname")String acqEnname);



	@Select("select cto.*  from  trans_info ti left join collective_trans_order "
			+ "cto on ti.order_no = cto.order_no where ti.trans_status = 'SUCCESS' "
			+ "and ti.trans_type  = 'PURCHASE' and ti.acq_enname  = #{acqEnname} and ti.acq_reference_no=#{orderReferenceNo}")
	@ResultMap("cn.eeepay.framework.dao.nposp.CollectiveTransOrderMapper.BaseResultMap")
	CollectiveTransOrder findAcqTransInfoByOrderRefNoAndAcqName(@Param("orderReferenceNo")String orderReferenceNo, @Param("acqEnname")String acqEnname);

	@Select("select ti.id,ti.order_no,ti.acq_enname,ti.acq_code,"
			 + " ti.acq_merchant_no,ti.acq_terminal_no, "
			 + " ti.acq_auth_no,ti.acq_reference_no, "
		     + " ti.acq_batch_no,ti.acq_serial_no, "
		     + " ti.acq_response_code,ti.agent_no,ti.merchant_no,"
		     + " ti.terminal_no,ti.batch_no,"
		     + " ti.serial_no,ti.account_no, "
		     + " ti.card_type,ti.currency_type, "
		     + " ti.trans_amount,ti.merchant_fee, "
		     + " ti.merchant_rate,ti.acq_merchant_fee, "
		     + " ti.acq_merchant_rate,ti.trans_type, "
		     + " ti.trans_status,ti.trans_source, "
		     + " ti.ori_acq_batch_no,ti.ori_acq_serial_no,"
		     + " ti.ori_batch_no,ti.ori_serial_no, "
		     + " ti.acq_settle_date,ti.merchant_settle_date, "
		     + " ti.my_settle,ti.review_status, "
		     + " ti.trans_time,ti.last_update_time, "
		     + " ti.create_time,ti.settle_err_code, "
		     + " ti.settle_err_msg,ti.belong_pay, "
		     + " cto.settlement_method,ti.bag_settle, "
		     + " ti.trans_msg,ti.cardholder_phone, "
		     + " ti.trans_id,cto.freeze_status,"
		     + " ti.sign_img,ti.device_sn,ti.sign_check_person,"
		     + " ti.sign_check_time,ti.is_iccard,ti.ic_msg,ti.insurance,ti.expired,ti.pos_type,ti.msg_id,"
		     + " ti.acc_status,ti.issued_status,ti.service_id,ti.single_share_amount,ti.acq_service_id,ti.syn_status,ti.holidays_mark,cto.account,cto.settle_status,cto.settle_type "
		     + " from trans_info ti left join collective_trans_order cto on ti.order_no = cto.order_no where ti.trans_status = 'SUCCESS' and ti.trans_type  = 'PURCHASE' "
		     + " and ti.acq_enname  = #{acqEnname} and ti.acq_reference_no=#{orderReferenceNo} ")
	@ResultMap("cn.eeepay.framework.dao.nposp.TransInfoMapper.BaseResultMap")
	TransInfo getYinshengTranData(@Param("orderReferenceNo")String orderReferenceNo, @Param("acqEnname")String acqEnname);

	@Select("select ti.id,ti.order_no,ti.acq_enname,ti.acq_code,"
			 + " ti.acq_merchant_no,ti.acq_terminal_no, "
			 + " ti.acq_auth_no,ti.acq_reference_no, "
		     + " ti.acq_batch_no,ti.acq_serial_no, "
		     + " ti.acq_response_code,ti.agent_no,ti.merchant_no,"
		     + " ti.terminal_no,ti.batch_no,"
		     + " ti.serial_no,ti.account_no, "
		     + " ti.card_type,ti.currency_type, "
		     + " ti.trans_amount,ti.merchant_fee, "
		     + " ti.merchant_rate,ti.acq_merchant_fee, "
		     + " ti.acq_merchant_rate,ti.trans_type, "
		     + " ti.trans_status,ti.trans_source, "
		     + " ti.ori_acq_batch_no,ti.ori_acq_serial_no,"
		     + " ti.ori_batch_no,ti.ori_serial_no, "
		     + " ti.acq_settle_date,ti.merchant_settle_date, "
		     + " ti.my_settle,ti.review_status, "
		     + " ti.trans_time,ti.last_update_time, "
		     + " ti.create_time,ti.settle_err_code, "
		     + " ti.settle_err_msg,ti.belong_pay, "
		     + " cto.settlement_method,ti.bag_settle, "
		     + " ti.trans_msg,ti.cardholder_phone, "
		     + " ti.trans_id,cto.freeze_status,"
		     + " ti.sign_img,ti.device_sn,ti.sign_check_person,"
		     + " ti.sign_check_time,ti.is_iccard,ti.ic_msg,ti.insurance,ti.expired,ti.pos_type,ti.msg_id,"
		     + " ti.acc_status,ti.issued_status,ti.service_id,ti.single_share_amount,ti.acq_service_id,ti.syn_status,ti.holidays_mark,cto.account,cto.settle_status,cto.settle_type "
		     + " from trans_info ti left join collective_trans_order cto on ti.order_no = cto.order_no where ti.trans_status = 'SUCCESS' and ti.trans_type  = 'PURCHASE' "
		     + " and ti.acq_enname  = 'KQ_ZQ' and ti.acq_reference_no=#{acqReferenceNo} ")
	@ResultMap("cn.eeepay.framework.dao.nposp.TransInfoMapper.BaseResultMap")
	TransInfo getTranDataByAcqReferenceNo(@Param("acqReferenceNo")String acqReferenceNo);
	
	@SelectProvider(type = SqlProvider.class,method = "getTransData")
	@ResultMap("cn.eeepay.framework.dao.nposp.TransInfoMapper.BaseResultMap")
	TransInfo getTranData(@Param("orderReferenceNo")String orderReferenceNo,@Param("acqMerchantNo") String acqMerchantNo, @Param("acqEnname")String acqEnname,@Param("acqSerNo")String acqSerNo,
						  @Param("acqTerNo")String acqTerNo,@Param("acqAccountNo")String acqAccountNo);

	@SelectProvider(type = SqlProvider.class,method = "getColletiveTransData")
	@ResultMap("cn.eeepay.framework.dao.nposp.CollectiveTransOrderMapper.BaseResultMap")
	CollectiveTransOrder getCollectiveTranData(@Param("acqMerchantNo") String acqMerchantNo, @Param("acqEnname")String acqEnname,@Param("acqSerNo")String acqSerNo,
						  @Param("acqTerNo")String acqTerNo,@Param("acqAccountNo")String acqAccountNo);

	@SelectProvider(type = SqlProvider.class,method = "getCollectiveTransDataForT1")
	@ResultMap("cn.eeepay.framework.dao.nposp.CollectiveTransOrderMapper.BaseResultMap")
	CollectiveTransOrder getCollectiveTransDataForT1(@Param("acqMerchantNo") String acqMerchantNo, @Param("acqEnname")String acqEnname,@Param("acqSerNo")String acqSerNo,
											   @Param("acqTerNo")String acqTerNo,@Param("acqAccountNo")String acqAccountNo,@Param("acqTransAmount")BigDecimal acqTransAmount);


	//查询订单状态
	@Select(" select account,freeze_status,settle_status,trans_status,order_no from collective_trans_order  WHERE order_no =#{orderNo} ")
	@ResultMap("cn.eeepay.framework.dao.nposp.CollectiveTransOrderMapper.BaseResultMap")
	CollectiveTransOrder findOrderSomeStatus(@Param("orderNo")String orderNo) ;

	//查询终端号
	@SelectProvider(type = SqlProvider.class,method = "getAcqTerminalStore")
	@Results(value = {
			@Result(property = "terNo", column = "ter_no"),
	})
	Map<String,Object> getAcqTerminalStore(@Param("unionMerNo") String unionMerNo);

	



}
