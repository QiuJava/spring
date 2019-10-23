package cn.eeepay.framework.daoSuperBank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrderMain;
import cn.eeepay.framework.model.OrderMainSum;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @author tans
 * @Date 2017-12-1
 */
public interface OrderMainDao {

    @SelectProvider(type = SqlProvider.class, method = "selectOrderPage")
    @ResultType(OrderMain.class)
    List<OrderMain> selectOrderPage(@Param("baseInfo") OrderMain baseInfo, @Param("page") Page<OrderMain> page);

    @SelectProvider(type = SqlProvider.class, method = "selectOrderSum")
    @ResultType(OrderMainSum.class)
    OrderMainSum selectOrderSum(@Param("baseInfo") OrderMain baseInfo);

    @Select("select * from order_main where order_no = #{orderNo}")
    @ResultType(OrderMain.class)
    OrderMain selectOrderDetail(@Param("orderNo") String orderNo);
    
    @Select("select user_name from user_info where user_code = #{userCode}")
    @ResultType(String.class)
    String selectUserName(@Param("userCode")String userCode);

    @SelectProvider(type = SqlProvider.class, method = "selectInsuranceOrderDetail")
    @ResultType(OrderMain.class)
    OrderMain selectInsuranceOrderDetail(String orderNo);

    class SqlProvider{
        public String selectOrderPage(Map<String, Object> param){
            final OrderMain baseInfo = (OrderMain)param.get("baseInfo");
            SQL sql = new SQL(){{
            	SELECT("om1.order_no,om1.org_id,om1.org_name,om1.order_type,om1.user_code");
                SELECT("om1.status,om1.create_date,om1.total_bonus,om1.share_user_code");
                SELECT("om1.one_user_code,om1.one_user_type,om1.one_user_profit,om1.org_profit");
                SELECT("om1.plate_profit,om1.account_status,om1.pay_method,om1.pay_date");
                SELECT("om1.pay_channel,om1.pay_order_no,om1.price,om1.refund_status,om1.refund_date,om1.refund_msg");
                SELECT("om1.two_user_code,om1.two_user_type,om1.two_user_profit");
                SELECT("om1.thr_user_code,om1.thr_user_type,om1.thr_user_profit");
                SELECT("om1.fou_user_code,om1.fou_user_type,om1.fou_user_profit");
                SELECT("om1.bank_name,om1.order_name,om1.order_phone,om1.order_id_no,om1.complete_date,om1.repay_transfee,om1.repay_transfee_add");
                SELECT("om1.loan_source_id,om1.loan_name,om1.loan_amount,om1.loan_push_pro,om1.trans_rate");
                SELECT("om1.receive_agent_id,om1.receive_amount,om1.repayment_agent_id,om1.repayment_amount,om1.repay_trans_status");
                SELECT("om1.creditcard_bank_bonus,om1.loan_bank_rate,om1.loan_org_rate,om1.loan_org_bonus,om1.second_user_node");
                SELECT("om1.profit_status2,om1.total_bonus2,om1.pay_date2,om1.one_user_profit2,om1.two_user_profit2");
                SELECT("om1.thr_user_profit2,om1.fou_user_profit2,om1.org_profit2,om1.account_status2");
                SELECT("om1.org_bonus_conf,om1.profit_type,om1.loan_type");
                if("15".equals(baseInfo.getOrderType())){
                    SELECT("om1.insurance_name,om1.insurance_phone,om1.insurance_id_no");
                    SELECT("ipd.product_name,ipd.product_type,ipd.bonus_settle_time,icp.company_nick_name");
                }
                FROM("order_main om1 ");
            }};
            whereSql(sql, baseInfo);
            sql.ORDER_BY(" create_date desc ");
            return sql.toString();
        }

        public String selectOrderSum(Map<String, Object> param){
            final OrderMain baseInfo = (OrderMain)param.get("baseInfo");
            SQL sql = new SQL(){{
                SELECT("sum(om1.total_bonus) as totalBonusSum");
                SELECT("sum(om1.plate_profit) as plateProfitSum");
                SELECT("sum(om1.org_profit) as orgProfitSum");
                SELECT("sum(om1.org_profit2) as orgProfitSum2");
                SELECT("sum(om1.price) as priceSum");
                SELECT("sum(om1.receive_amount) as receiveAmountSum");
                SELECT("sum(om1.repayment_amount) as repaymentAmountSum");
                FROM("order_main om1");
            }};
            whereSql(sql, baseInfo);
            return sql.toString();
        }
        public String selectInsuranceOrderDetail(String orderNo){
            SQL sql = new SQL(){{
                SELECT("om1.order_no,om1.org_id,om1.org_name,om1.order_type,om1.user_code");
                SELECT("om1.status,om1.create_date,om1.total_bonus,om1.share_user_code");
                SELECT("om1.one_user_code,om1.one_user_type,om1.one_user_profit,om1.org_profit");
                SELECT("om1.plate_profit,om1.account_status,om1.pay_method,om1.pay_date");
                SELECT("om1.pay_channel,om1.pay_order_no,om1.price,om1.refund_status,om1.refund_date,om1.refund_msg");
                SELECT("om1.two_user_code,om1.two_user_type,om1.two_user_profit");
                SELECT("om1.thr_user_code,om1.thr_user_type,om1.thr_user_profit");
                SELECT("om1.fou_user_code,om1.fou_user_type,om1.fou_user_profit");
                SELECT("om1.bank_name,om1.order_name,om1.order_phone,om1.order_id_no,om1.complete_date,om1.repay_transfee,om1.repay_transfee_add");
                SELECT("om1.loan_source_id,om1.loan_name,om1.loan_amount,om1.loan_push_pro,om1.trans_rate");
                SELECT("om1.receive_agent_id,om1.receive_amount,om1.repayment_agent_id,om1.repayment_amount,om1.repay_trans_status");
                SELECT("om1.creditcard_bank_bonus,om1.loan_bank_rate,om1.loan_org_rate,om1.loan_org_bonus,om1.second_user_node");
                SELECT("om1.profit_status2,om1.total_bonus2,om1.pay_date2,om1.one_user_profit2,om1.two_user_profit2");
                SELECT("om1.thr_user_profit2,om1.fou_user_profit2,om1.org_profit2,om1.account_status2");
                SELECT("om1.org_bonus_conf,om1.profit_type,om1.loan_type");
                SELECT("om1.insurance_name,om1.insurance_phone,om1.insurance_id_no");
                SELECT("ipd.product_name,ipd.product_type,ipd.bonus_settle_time,icp.company_nick_name");
                FROM("order_main om1 ");
                LEFT_OUTER_JOIN(" insurance_product ipd on ipd.product_id = om1.upper_product_id");
                LEFT_OUTER_JOIN(" insurance_company icp on icp.company_no = ipd.company_no");
                WHERE("om1.order_no = #{orderNo}");
            }};
            return sql.toString();
        }

        public void whereSql(SQL sql, OrderMain baseInfo){
        	if (StringUtils.isNotBlank(baseInfo.getSharePhone())) {
        		sql.LEFT_OUTER_JOIN(" user_info ui0 on ui0.user_code = om1.user_code ");
			}
        	if (StringUtils.isNotBlank(baseInfo.getUserName())) {
        		sql.LEFT_OUTER_JOIN(" user_info ui5 on ui5.user_code = om1.user_code ");
        	}
        	if (StringUtils.isNotBlank(baseInfo.getOneUserName())) {
        		sql.LEFT_OUTER_JOIN(" user_info ui1 on ui1.user_code = om1.one_user_code ");
        	}
        	if (StringUtils.isNotBlank(baseInfo.getTwoUserName())) {
        		sql.LEFT_OUTER_JOIN(" user_info ui2 on ui2.user_code = om1.two_user_code ");
        	}
        	if (StringUtils.isNotBlank(baseInfo.getThrUserName())) {
        		sql.LEFT_OUTER_JOIN(" user_info ui3 on ui3.user_code = om1.thr_user_code ");
        	}
        	if (StringUtils.isNotBlank(baseInfo.getFouUserName())) {
        		sql.LEFT_OUTER_JOIN(" user_info ui4 on ui4.user_code = om1.fou_user_code ");
        	}
        	if (StringUtils.isNotBlank(baseInfo.getIncomeType())) {
        		sql.LEFT_OUTER_JOIN(" user_income uic on uic.order_no = om1.order_no ");
        	}

        	if (StringUtils.isNotBlank(baseInfo.getRemark()) || StringUtils.isNotBlank(baseInfo.getOpenProvince())
        			|| StringUtils.isNotBlank(baseInfo.getOpenCity()) || StringUtils.isNotBlank(baseInfo.getOpenRegion())) {
        		sql.LEFT_OUTER_JOIN(" user_info ui on ui.user_code = om1.user_code ");
        		if (StringUtils.isNotBlank(baseInfo.getRemark())) {
        			sql.WHERE("ui.remark = #{baseInfo.remark}");
        		}
        		if (StringUtils.isNotBlank(baseInfo.getOpenProvince())) {
        			sql.WHERE("ui.open_province = #{baseInfo.openProvince}");
        		}
        		if (StringUtils.isNotBlank(baseInfo.getOpenCity())) {
        			sql.WHERE("ui.open_city = #{baseInfo.openCity}");
        		}
        		if (StringUtils.isNotBlank(baseInfo.getOpenRegion())) {
        			sql.WHERE("ui.open_region = #{baseInfo.openRegion}");
        		}
			}
        	//查询当前登录代理商
        	sql.WHERE("om1.org_id = #{baseInfo.entityId}");
        	if(StringUtils.isNotBlank(baseInfo.getOrderType())){
            	baseInfo.setOrderTypeList(baseInfo.getOrderType().split(","));
            	if(baseInfo.getOrderTypeList().length > 1){
            		StringBuilder sb=new StringBuilder();
        			MessageFormat messageFormat = new MessageFormat("#'{'baseInfo.orderTypeList[{0}]}");
    				for (int i = 0; i < baseInfo.getOrderTypeList().length; i++) {
    					sb.append(messageFormat.format(new Integer[] { i }));
    					sb.append(",");
    				}
    				sb.setLength(sb.length() - 1);
                    sql.WHERE("om1.order_type in (" + sb.toString() + ")");
            	} else {
            		sql.WHERE("om1.order_type = #{baseInfo.orderType}");
            	}
            }
        	if(StringUtils.isNotBlank(baseInfo.getProfitStatus2())){
        		sql.WHERE("om1.profit_status2 = #{baseInfo.profitStatus2}");
        	}
        	if(StringUtils.isNotBlank(baseInfo.getPayDate2Start())){
                sql.WHERE("om1.pay_date2 >= #{baseInfo.payDate2Start}");
            }
            if(StringUtils.isNotBlank(baseInfo.getPayDate2End())){
                sql.WHERE("om1.pay_date2 <= #{baseInfo.payDate2End}");
            }
            if(StringUtils.isNotBlank(baseInfo.getAccountStatus2())){
            	sql.WHERE("om1.account_status2 = #{baseInfo.accountStatus2}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOrderType()) && "15".equals(baseInfo.getOrderType())) {
                sql.LEFT_OUTER_JOIN(" insurance_product ipd on ipd.upper_product_id = om1.upper_product_id");
                sql.LEFT_OUTER_JOIN(" insurance_company icp on icp.company_no = ipd.company_no");
                if (baseInfo.getCompanyId() != null && baseInfo.getCompanyId() != -1) {
                    sql.WHERE("ipd.company_no = #{baseInfo.companyId}");
                }
                if (StringUtils.isNotBlank(baseInfo.getProductType())) {
                    sql.WHERE("ipd.product_type = #{baseInfo.productType}");
                }
            }
            
            if(StringUtils.isNotBlank(baseInfo.getOrderNo())){
                sql.WHERE("om1.order_no like concat(#{baseInfo.orderNo}, '%')");
            }
            if(StringUtils.isNotBlank(baseInfo.getOrderName())){
            	sql.WHERE("om1.order_name = #{baseInfo.orderName}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOrderPhone())) {
                sql.WHERE("om1.order_phone = #{baseInfo.orderPhone}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOrderIdNo())){
        	    sql.WHERE("om1.order_id_no = #{baseInfo.orderIdNo}");
            }
            if(StringUtils.isNotBlank(baseInfo.getRepayTransStatus())){
            	sql.WHERE("om1.repay_trans_status = #{baseInfo.repayTransStatus}");
            }
            if(StringUtils.isNotBlank(baseInfo.getStatus())){
                sql.WHERE("om1.status = #{baseInfo.status}");
            }
            if(StringUtils.isNotBlank(baseInfo.getCreateDateStart())){
                sql.WHERE("om1.create_date >= #{baseInfo.createDateStart}");
            }
            if(StringUtils.isNotBlank(baseInfo.getCreateDateEnd())){
                sql.WHERE("om1.create_date <= #{baseInfo.createDateEnd}");
            }
            if(StringUtils.isNotBlank(baseInfo.getPayDateStart())){
                sql.WHERE("om1.pay_date >= #{baseInfo.payDateStart}");
            }
            if(StringUtils.isNotBlank(baseInfo.getPayDateEnd())){
                sql.WHERE("om1.pay_date <= #{baseInfo.payDateEnd}");
            }
            if(StringUtils.isNotBlank(baseInfo.getCompleteDateStart())){
                sql.WHERE("om1.complete_date >= #{baseInfo.completeDateStart}");
            }
            if(StringUtils.isNotBlank(baseInfo.getCompleteDateEnd())){
                sql.WHERE("om1.complete_date <= #{baseInfo.completeDateEnd}");
            }
            if(StringUtils.isNotBlank(baseInfo.getShareUserCode())){
                sql.WHERE("om1.share_user_code = #{baseInfo.shareUserCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getAccountStatus())){
                sql.WHERE("om1.account_status = #{baseInfo.accountStatus}");
            }
            if(baseInfo.getOrgId() != null && -1 != baseInfo.getOrgId()){
                sql.WHERE("om1.org_id = #{baseInfo.orgId}");
            }
            if(StringUtils.isNotBlank(baseInfo.getPayOrderNo())){
                sql.WHERE("om1.pay_order_no like concat( #{baseInfo.payOrderNo} , '%')");
            }
            if(StringUtils.isNotBlank(baseInfo.getOrderPhone())){
                sql.WHERE("om1.order_phone = #{baseInfo.orderPhone}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOrderIdNo())){
                sql.WHERE("om1.order_id_no = #{baseInfo.orderIdNo}");
            }
            if(baseInfo.getBankSourceId() != null && -1 != baseInfo.getBankSourceId()){
                sql.WHERE("om1.bank_source_id = #{baseInfo.bankSourceId}");
            }
            if(StringUtils.isNotBlank(baseInfo.getReceiveAgentId())){
                sql.WHERE("om1.receive_agent_id = #{baseInfo.receiveAgentId}");
            }
            if(StringUtils.isNotBlank(baseInfo.getRepaymentAgentId())){
                sql.WHERE("om1.repayment_agent_id = #{baseInfo.repaymentAgentId}");
            }
            if(baseInfo.getLoanSourceId() != null && baseInfo.getLoanSourceId() != -1){
                sql.WHERE("om1.loan_source_id = #{baseInfo.loanSourceId}");
            }
            if(StringUtils.isNotBlank(baseInfo.getUserCode())){
                sql.WHERE("om1.user_code = #{baseInfo.userCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getSecondUserNode())){
            	sql.WHERE("om1.second_user_node = #{baseInfo.secondUserNode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getStartRefundDate())){
                sql.WHERE("om1.refund_date >= #{baseInfo.startRefundDate}");
            }
            if(StringUtils.isNotBlank(baseInfo.getEndRefundDate())){
                sql.WHERE("om1.refund_date <= #{baseInfo.endRefundDate}");
            }
            if(StringUtils.isNotBlank(baseInfo.getRefundStatus())){
            	sql.WHERE("om1.refund_status = #{baseInfo.refundStatus}");
            }
            if(StringUtils.isNotBlank(baseInfo.getRefundMsg())){
            	sql.WHERE("om1.refund_msg = #{baseInfo.refundMsg}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOneUserCode())){
                sql.WHERE("om1.one_user_code = #{baseInfo.oneUserCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOneUserType())){
            	sql.WHERE("om1.one_user_type = #{baseInfo.oneUserType}");
            }
            if(StringUtils.isNotBlank(baseInfo.getTwoUserCode())){
            	sql.WHERE("om1.two_user_code = #{baseInfo.twoUserCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getTwoUserType())){
            	sql.WHERE("om1.two_user_type = #{baseInfo.twoUserType}");
            }
            if(StringUtils.isNotBlank(baseInfo.getThrUserCode())){
            	sql.WHERE("om1.thr_user_code = #{baseInfo.thrUserCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getThrUserType())){
            	sql.WHERE("om1.thr_user_type = #{baseInfo.thrUserType}");
            }
            if(StringUtils.isNotBlank(baseInfo.getFouUserCode())){
            	sql.WHERE("om1.fou_user_code = #{baseInfo.fouUserCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getFouUserType())){
            	sql.WHERE("om1.fou_user_type = #{baseInfo.fouUserType}");
            }
            if (StringUtils.isNotBlank(baseInfo.getBankName())) {
        		sql.WHERE("om1.bank_name = #{baseInfo.bankName}");
        	}
            if (StringUtils.isNotBlank(baseInfo.getLoanType())) {
        		sql.WHERE("om1.loan_type = #{baseInfo.loanType}");
        	}
        	if (StringUtils.isNotBlank(baseInfo.getProfitType())) {
        		sql.WHERE("om1.profit_type = #{baseInfo.profitType}");
        	}
            if (StringUtils.isNotBlank(baseInfo.getOneUserName())) {
        		sql.WHERE(" ui1.user_name = #{baseInfo.oneUserName}");
			}
            if (StringUtils.isNotBlank(baseInfo.getSharePhone())) {
            	sql.WHERE(" ui0.phone = #{baseInfo.sharePhone}");
            }
            if (StringUtils.isNotBlank(baseInfo.getUserName())) {
            	sql.WHERE(" ui5.user_name = #{baseInfo.userName}");
            }
        	if (StringUtils.isNotBlank(baseInfo.getTwoUserName())) {
        		sql.WHERE(" ui2.user_name = #{baseInfo.twoUserName}");
        	}
        	if (StringUtils.isNotBlank(baseInfo.getThrUserName())) {
        		sql.WHERE(" ui3.user_name = #{baseInfo.thrUserName}");
        	}
        	if (StringUtils.isNotBlank(baseInfo.getFouUserName())) {
        		sql.WHERE(" ui4.user_name = #{baseInfo.fouUserName}");
        	}
        	if (StringUtils.isNotBlank(baseInfo.getIncomeType())) {
        		sql.WHERE("uic.income_type = #{baseInfo.incomeType}");
        	}

            if(StringUtils.isNotBlank(baseInfo.getInsuranceName())){
                sql.WHERE("om1.insurance_name = #{baseInfo.insuranceName}");
            }
            if(StringUtils.isNotBlank(baseInfo.getInsurancePhone())) {
                sql.WHERE("om1.insurance_phone = #{baseInfo.insurancePhone}");
            }
            if(StringUtils.isNotBlank(baseInfo.getInsuranceIdNo())){
                sql.WHERE("om1.insurance_id_no = #{baseInfo.insuranceIdNo}");
            }
        }
    }
}
