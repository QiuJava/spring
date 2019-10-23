package cn.eeepay.framework.dao;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.CollectiveTransOrder;
import cn.eeepay.framework.model.PageBean;
import cn.eeepay.framework.util.ReadOnlyDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

@ReadOnlyDataSource
public interface TransInfoDao {

	@SelectProvider(type=SqlProvider.class,method="selectMoneyInfo")
	@ResultType(CollectiveTransOrder.class)
	String queryNumAndMoney(@Param("transInfo") CollectiveTransOrder transInfo,
							@Param("loginAgentNode") String loginAgentNode);
	
	@Select("SELECT cto.*,sis.service_name,"
			+ "hp.type_name,mis.merchant_name,tis.terminal_no,tis.currency_type,tis.settle_err_code,"
			+ "tis.merchant_settle_date,tis.id as tisId,"
			+ "tis.acq_terminal_no,tis.batch_no,tis.acq_serial_no,tis.acq_reference_no,tis.acq_auth_no,tis.serial_no,"
			+"(SELECT sd.sys_name FROM sys_dict sd WHERE sd.sys_value = cto.trans_status AND sd.sys_key = 'TRANS_STATUS' AND sd.STATUS = 1 LIMIT 1 ) AS transStatus1,"
			+"(SELECT sd.sys_name FROM sys_dict sd WHERE sd.sys_value = cto.trans_type AND sd.sys_key = 'TRANS_TYPE' AND sd.STATUS = 1 LIMIT 1 ) AS transType1,"
			+"(SELECT sd1.sys_name FROM sys_dict sd1 WHERE sd1.sys_value = cto.pay_method  AND sd1.sys_key = 'PAY_METHOD_TYPE' AND sd1.STATUS = 1 LIMIT 1) AS payMethod1,"
			+"(SELECT sd3.sys_name FROM sys_dict sd3 WHERE sd3.sys_value = cast(cto.settle_status as char) AND sd3.sys_key = 'SETTLE_STATUS' AND sd3.STATUS = 1 LIMIT 1 ) AS settleStatus1 "
			+ "FROM collective_trans_order cto "
			+ "LEFT JOIN trans_info tis ON tis.order_no = cto.order_no "
			+ "LEFT JOIN merchant_info mis ON mis.merchant_no = tis.merchant_no "
			+ "LEFT JOIN agent_info ais ON ais.agent_no = tis.agent_no "
			+ "LEFT JOIN terminal_info ter ON ter.SN = cto.device_sn "
			+ "LEFT JOIN hardware_product hp on hp.hp_id=ter.type "
			+ "LEFT JOIN service_info sis ON sis.service_id = cto.service_id where cto.order_no=#{id} and cto.trans_status = 'SUCCESS'")
	@ResultType(CollectiveTransOrder.class)
	CollectiveTransOrder queryInfoDetail(@Param("id")String id);

    /**
     * 交易查询导出20170222tgh
     * @param transInfo
     * @param level
     * @param level1
	 * @return
     */
    @SelectProvider(type=SqlProvider.class,method="selectAllInfo")
    @ResultType(CollectiveTransOrder.class)
    List<CollectiveTransOrder> exportAllInfo(@Param("transInfo") CollectiveTransOrder transInfo,
											 @Param("level") int level,
											 @Param("level1") int level1);

    @SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(CollectiveTransOrder.class)
	List<CollectiveTransOrder> queryAllInfo(@Param("page") Page<CollectiveTransOrder> page,
											@Param("transInfo") CollectiveTransOrder transInfo,
											@Param("level") int level, @Param("level1") int level1);

	/**
	 * 交易查询
	 */
	List<CollectiveTransOrder> queryAllInfoByMerchant(@Param("transInfo") CollectiveTransOrder param,
													  @Param("searchAgent") AgentInfo searchAgent,
													  @Param("page")  PageBean page);
	/**
	 * 汇总交易查询
	 */
	int countAllInfoByMerchant(@Param("transInfo") CollectiveTransOrder param,
							   @Param("searchAgent") AgentInfo searchAgent);

	/**
	 * 导出交易查询
	 */
	List<CollectiveTransOrder> exportAllInfoByMerchant(@Param("transInfo")  CollectiveTransOrder transInfo,
													   @Param("searchAgent") AgentInfo searchAgent);

	/**
	 * 风控调单查询订单信息（不限制交易状态）
	 * @param id
	 * @return
	 */
	@Select("SELECT cto.*,sis.service_name,"
			+ "hp.type_name,mis.merchant_name,tis.terminal_no,tis.currency_type,tis.settle_err_code,"
			+ "tis.merchant_settle_date,tis.id as tisId,"
			+ "tis.acq_terminal_no,tis.batch_no,tis.acq_serial_no,tis.acq_reference_no,tis.acq_auth_no,tis.serial_no,"
			+"(SELECT sd.sys_name FROM sys_dict sd WHERE sd.sys_value = cto.trans_status AND sd.sys_key = 'TRANS_STATUS' AND sd.STATUS = 1 LIMIT 1 ) AS transStatus1,"
			+"(SELECT sd.sys_name FROM sys_dict sd WHERE sd.sys_value = cto.trans_type AND sd.sys_key = 'TRANS_TYPE' AND sd.STATUS = 1 LIMIT 1 ) AS transType1,"
			+"(SELECT sd1.sys_name FROM sys_dict sd1 WHERE sd1.sys_value = cto.pay_method  AND sd1.sys_key = 'PAY_METHOD_TYPE' AND sd1.STATUS = 1 LIMIT 1) AS payMethod1,"
			+"(SELECT sd3.sys_name FROM sys_dict sd3 WHERE sd3.sys_value = cast(cto.settle_status as char) AND sd3.sys_key = 'SETTLE_STATUS' AND sd3.STATUS = 1 LIMIT 1 ) AS settleStatus1 "
			+ "FROM collective_trans_order cto "
			+ "LEFT JOIN trans_info tis ON tis.order_no = cto.order_no "
			+ "LEFT JOIN merchant_info mis ON mis.merchant_no = tis.merchant_no "
			+ "LEFT JOIN agent_info ais ON ais.agent_no = tis.agent_no "
			+ "LEFT JOIN terminal_info ter ON ter.SN = cto.device_sn "
			+ "LEFT JOIN hardware_product hp on hp.hp_id=ter.type "
			+ "LEFT JOIN service_info sis ON sis.service_id = cto.service_id where cto.order_no=#{id}")
	@ResultType(CollectiveTransOrder.class)
	CollectiveTransOrder queryInfoDetailForSurveyOrder(@Param("id") String id);


	@Select("select * from collective_trans_order where order_no=#{id}")
	@ResultType(CollectiveTransOrder.class)
	CollectiveTransOrder queryInfoByOrderNo(@Param("id") String orderId);

	@Select("select * from collective_trans_order where order_no=#{orderNo}")
	@ResultType(CollectiveTransOrder.class)
	CollectiveTransOrder selectByOrderNo(@Param("orderNo") String orderNo);

	@Select("select * from collective_trans_order where order_no=#{orderNo} and agent_node like concat(#{agentNode},'%')")
	CollectiveTransOrder selectByOrderNoAndAgentNode(@Param("orderNo") String orderNo, @Param("agentNode") String agentNode);

	Integer queryMaxAgentLevel();

	List<Map<String, String>> summaryTransAmount(@Param("startTransTime") String startTransTime,
												 @Param("endTransTime") String endTransTime,
												 @Param("agentLevel") int agentLevel,
												 @Param("start") int start,
												 @Param("offset") int offset);


	public class SqlProvider{
    	public String selectAllInfo(Map<String,Object> param){
    		final CollectiveTransOrder transInfo=(CollectiveTransOrder)param.get("transInfo");
    		final int level=(int)param.get("level");
    		 String sql=new SQL(){{

				SELECT("cto.agent_node,cto.order_no,cto.settlement_method,mis.merchant_name,cto.merchant_no,bpd.bp_name,\n" +
						"          cto.trans_type transType1,cto.card_type cardType1,cto.account_no,ais.agent_name,\n" +
						"          mis.mobilephone,mis.recommended_source,cto.trans_amount,cto.trans_status transStatus1,cto.settle_status settleStatus1,\n" +
						"          cto.trans_time,ter.activity_type,cto.order_type,mis.create_time ct,si.service_type,ter.SN ,ti.team_name as merGroup"
						);
    			FROM("collective_trans_order cto "
    					//20170113以下
    					+ "JOIN merchant_info mis on mis.merchant_no=cto.merchant_no "
    					+ "JOIN agent_info ais on ais.agent_no=mis.agent_no "
    					+ "LEFT JOIN terminal_info ter on ter.SN=cto.device_sn "
    					+ "LEFT JOIN business_product_define bpd on bpd.bp_id=cto.business_product_id "
						+ "LEFT JOIN service_info si on si.service_id = cto.service_id "
						+ "LEFT JOIN team_info ti on ti.team_id = mis.team_id "
    					//xy20170628
//    					+"LEFT JOIN (SELECT * FROM agent_info ai WHERE ai.parent_id = #{transInfo.agentNo}) temp ON POSITION(temp.agent_node IN ais.agent_node) > 0 "
//						+"LEFT JOIN  agent_info temp on temp.parent_id = #{transInfo.agentNo}  and POSITION(temp.agent_node IN ais.agent_node) > 0 "
    					);
    			
    			
    			if(StringUtils.isNotBlank(transInfo.getInitAgentNo())){
    				WHERE("cto.agent_node LIKE CONCAT(#{transInfo.initAgentNo},'%')");
    			}
    			if(StringUtils.isNotBlank(transInfo.getTeamEntryId())){
    				WHERE("mis.team_entry_id = #{transInfo.teamEntryId}");
    			}
    			//20170113以上
    			if(StringUtils.isNotBlank(transInfo.getMerchantNo())){
    				WHERE(" (cto.merchant_no=#{transInfo.merchantNo})");
    			}
    			
    			if(StringUtils.isNotBlank(transInfo.getAgentNode())){
    			    if (StringUtils.isBlank(transInfo.getBool()) || StringUtils.equals("1", transInfo.getBool())){
                        if (!StringUtils.equals(transInfo.getAgentNode(), transInfo.getInitAgentNo())){
                            WHERE(" cto.agent_node LIKE CONCAT(#{transInfo.agentNode},'%')");
                        }
                    }else{
                        WHERE(" cto.agent_node = #{transInfo.agentNode}");
                    }
    			}
    			if(StringUtils.isNotBlank(transInfo.getOrderNo())){
    				WHERE(" cto.order_no=#{transInfo.orderNo}");
    			}
    			if (StringUtils.isNotBlank(transInfo.getServiceType())){
    				WHERE("si.service_type = #{transInfo.serviceType}");
				}
    			if(StringUtils.isNotBlank(transInfo.getTransStatus())){
    				WHERE(" cto.trans_status=#{transInfo.transStatus}");
    			}else{
					WHERE (" cto.trans_status != 'INIT' ");
				}
    			if(StringUtils.isNotBlank(transInfo.getMobilephone())){
    				WHERE(" mis.mobilephone=#{transInfo.mobilephone}");
    			}
    			if(StringUtils.isNotBlank(transInfo.getAccountNo())){
    				WHERE(" cto.account_no=#{transInfo.accountNo}");
    			}
    			if (StringUtils.isNotBlank(transInfo.getTerminalNo())) {
    				WHERE(" ter.sn=#{transInfo.terminalNo}");
				}
				 //商户创建时间
				if(StringUtils.isNotBlank(transInfo.getStartCreateTime())){
					WHERE("mis.create_time >= #{transInfo.startCreateTime}");
				}
				if(StringUtils.isNotBlank(transInfo.getEndCreateTime())){
					WHERE("mis.create_time <= #{transInfo.endCreateTime}");
				}
    			//商户号和手机号输入，不限时间查询条件
				if(StringUtils.isBlank(transInfo.getMerchantNo()) || (StringUtils.isBlank(transInfo.getMobilephone()))){
					if(StringUtils.isNotBlank(transInfo.getSdate())){
						WHERE("cto.trans_time>=#{transInfo.sdate}");
					}
					if(StringUtils.isNotBlank(transInfo.getEdate())){
						WHERE("cto.trans_time<=#{transInfo.edate}");
					}
				}
    			if (!"-1".equals(transInfo.getMerTeamId())) {
					 WHERE(" mis.team_id = #{transInfo.merTeamId} ");
				}
   			
				if(StringUtils.isNotBlank(transInfo.getPayMethod())){
					WHERE("cto.pay_method=#{transInfo.payMethod}");
				}
				//添加活动类型查询20170216
				if(StringUtils.isNotBlank(transInfo.getActivityType())){
					WHERE("FIND_IN_SET(#{transInfo.activityType}, ter.activity_type)");
				}

				//gw
				if(StringUtils.isNotBlank(transInfo.getProductType()) && !StringUtils.equals("-1", transInfo.getProductType())){
					WHERE("cto.business_product_id=#{transInfo.productType}");
				}
				//20170221tgh添加结算周期
				if(StringUtils.isNotBlank(transInfo.getSettlementMethod())){
					WHERE("cto.settlement_method=#{transInfo.settlementMethod}");
				}
				//20170317tgh添加结算状态
				if(StringUtils.isNotBlank(transInfo.getSettleStatus())){
					WHERE("cto.settle_status=#{transInfo.settleStatus}");
				}
				 // 交易活动类型
				 if (StringUtils.isNotBlank(transInfo.getOrderType())) {
					 WHERE("cto.order_type = #{transInfo.orderType}");
				 }
				//交易金额
				if(transInfo.getMinTransAmount() != null){
					WHERE("cto.trans_amount >= #{transInfo.minTransAmount}");
				}
				if(transInfo.getMaxTransAmount() != null){
					WHERE("cto.trans_amount <= #{transInfo.maxTransAmount}");
				}
				// 是否是超级推商户
			  	if(StringUtils.isNotBlank(transInfo.getRecommendedSource())){
					if("1".equals(transInfo.getRecommendedSource())){
						WHERE("mis.recommended_source='1'");
					}
					if(!"1".equals(transInfo.getRecommendedSource())){
						WHERE("mis.recommended_source<>'1'");
					}
			  	}
				ORDER_BY("cto.trans_time desc,cto.agent_node");
    		}}.toString();
    		return sql;
    	}
    	

    	public String selectMoneyInfo(Map<String,Object> param){
    		final CollectiveTransOrder transInfo=(CollectiveTransOrder)param.get("transInfo");
    		final String loginAgentNode=(String)param.get("loginAgentNode");
    		String sql= new SQL(){{
    			SELECT("SUM(cto.trans_amount) AS totalMoney ");
    			FROM("collective_trans_order cto ");
                JOIN("merchant_info mis on mis.merchant_no=cto.merchant_no");
                LEFT_OUTER_JOIN("terminal_info ter on ter.SN=cto.device_sn ");
    			if(StringUtils.isNotBlank(loginAgentNode)){
    				WHERE(" cto.agent_node LIKE CONCAT(#{loginAgentNode}, '%')");
    			}
    			if(StringUtils.isNotBlank(transInfo.getMerchantNo())){
    				WHERE(" cto.merchant_no=#{transInfo.merchantNo}");
    			}
    			if (StringUtils.isNotBlank(transInfo.getServiceType())){
					LEFT_OUTER_JOIN("service_info si on si.service_id = cto.service_id ");
					WHERE("si.service_type = #{transInfo.serviceType}");
				}
    			if(StringUtils.isNotBlank(transInfo.getAgentNode())){
                    if (StringUtils.isBlank(transInfo.getBool()) || StringUtils.equals("1", transInfo.getBool())){
                        if (! StringUtils.equals(transInfo.getAgentNode(), loginAgentNode)){
                            WHERE(" cto.agent_node LIKE CONCAT(#{transInfo.agentNode}, '%')");
                        }
                    }else{
                        WHERE(" cto.agent_node = #{transInfo.agentNode}");
                    }
    			}
    			if(StringUtils.isNotBlank(transInfo.getOrderNo())){
    				WHERE(" cto.order_no=#{transInfo.orderNo}");
    			}
    			if(StringUtils.isNotBlank(transInfo.getTransStatus())){
    				WHERE(" cto.trans_status=#{transInfo.transStatus}");
    			}else {
                    WHERE (" cto.trans_status != 'INIT' ");
                }
    			if(StringUtils.isNotBlank(transInfo.getAccountNo())){
    				WHERE(" cto.account_no=#{transInfo.accountNo}");
    			}
				if(StringUtils.isNotBlank(transInfo.getPayMethod())){
					WHERE("cto.pay_method=#{transInfo.payMethod}");
				}
				//添加活动类型查询20170216
				if(StringUtils.isNotBlank(transInfo.getActivityType())){
				  //  LEFT_OUTER_JOIN("terminal_info ter on ter.SN = cto.device_sn");
					WHERE("FIND_IN_SET(#{transInfo.activityType}, ter.activity_type)");
				}
				//20170221tgh添加结算周期
				if(StringUtils.isNotBlank(transInfo.getSettlementMethod())){
					WHERE("cto.settlement_method=#{transInfo.settlementMethod}");
				}
				//20170317tgh添加结算状态
				if(StringUtils.isNotBlank(transInfo.getSettleStatus())){
					WHERE("cto.settle_status=#{transInfo.settleStatus}");
				}
				//gw增加业务产品查询条件
				if(StringUtils.isNotBlank(transInfo.getProductType()) && !StringUtils.equals("-1", transInfo.getProductType())){
					WHERE("cto.business_product_id=#{transInfo.productType}");
				}
				// 交易活动类型
				if (StringUtils.isNotBlank(transInfo.getOrderType())) {
					WHERE("cto.order_type = #{transInfo.orderType}");
				}
                //交易金额
                if(transInfo.getMinTransAmount() != null){
                    WHERE("cto.trans_amount >= #{transInfo.minTransAmount}");
                }
                if(transInfo.getMaxTransAmount() != null){
                    WHERE("cto.trans_amount <= #{transInfo.maxTransAmount}");
                }
                
                if (StringUtils.isNotBlank(transInfo.getTerminalNo())) {
    				WHERE(" ter.sn=#{transInfo.terminalNo}");
				}
				if(StringUtils.isNotBlank(transInfo.getSdate())){
					WHERE("cto.trans_time>=#{transInfo.sdate}");
				}
				if(StringUtils.isNotBlank(transInfo.getEdate())){
					WHERE("cto.trans_time<=#{transInfo.edate}");
				}
				//商户创建时间
				if(StringUtils.isNotBlank(transInfo.getStartCreateTime())){
					WHERE("mis.create_time >= #{transInfo.startCreateTime}");
				}
				if(StringUtils.isNotBlank(transInfo.getEndCreateTime())){
					WHERE("mis.create_time <= #{transInfo.endCreateTime}");
				}
                
                if (!"-1".equals(transInfo.getMerTeamId())) {
					 WHERE(" mis.team_id = #{transInfo.merTeamId} ");
				} 

                if(StringUtils.isNotBlank(transInfo.getMobilephone())){
                    WHERE(" mis.mobilephone=#{transInfo.mobilephone}");
                }
				// 是否是超级推商户
				if(StringUtils.isNotBlank(transInfo.getRecommendedSource())){
					if("1".equals(transInfo.getRecommendedSource())){
						WHERE("mis.recommended_source='1'");
					}
					if(!"1".equals(transInfo.getRecommendedSource())){
						WHERE("mis.recommended_source<>'1'");
					}
				}
				//商户创建时间
    		}}.toString();
    		return sql;
    	}

  }

}
