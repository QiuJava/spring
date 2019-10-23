package cn.eeepay.framework.daoPerAgent;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.PaAfterSale;
import cn.eeepay.framework.model.PaCashBackDetail;
import cn.eeepay.framework.model.PaMerInfo;
import cn.eeepay.framework.model.PaSnBack;
import cn.eeepay.framework.model.PaUserInfo;
import cn.eeepay.framework.model.PeragentCard;
import cn.eeepay.framework.util.WriteReadDataSource;

/**
 * @author MXG
 * create 2018/07/11
 */
@WriteReadDataSource
public interface PerAgentDao {

    /**
     * 盟主列表查询
     *
     * @param page
     * @param info
     * @return
     */
    @SelectProvider(type = SqlProvider.class, method = "selectPaUserByParam")
    @ResultType(PaUserInfo.class)
    List<PaUserInfo> selectPaUserByParam(@Param("page") Page<PaUserInfo> page, @Param("info") PaUserInfo info);

    @SelectProvider(type = SqlProvider.class, method = "selectChildPaUser")
    @ResultType(PaUserInfo.class)
    List<PaUserInfo> selectChildPaUser(@Param("info") PaUserInfo info);

    @Select("select * from pa_user_card where user_code = #{userCode} and is_settle = '1' ")
    @ResultType(Map.class)
    Map<String,Object> selectPaUserCardByUserCode(@Param("userCode")String userCode);
    
    @Select("select * from pa_mer_info where merchant_no = #{merchantNo}")
    @ResultType(Map.class)
    Map<String, String> selectByMerchantNo(@Param("merchantNo")String merchantNo);

    @Select("SELECT * FROM pa_agent_user where agent_no = #{agentNo}")
    @ResultType(Map.class)
    Map<String, String> selectByAgentNo(@Param("agentNo") String agentNo);

    @SelectProvider(type = SqlProvider.class, method = "selectPaUserByParam")
    @ResultType(PaUserInfo.class)
    List<PaUserInfo> selectAllList(@Param("info") PaUserInfo info);

    @SelectProvider(type = SqlProvider.class, method = "countUserFromMer")
    @ResultType(Long.class)
    long countUserFromMer(@Param("info") PaUserInfo info);

    @SelectProvider(type = SqlProvider.class, method = "countUser")
    @ResultType(Long.class)
    long countUser(@Param("info") PaUserInfo info);

    @SelectProvider(type = SqlProvider.class, method = "countBigUser")
    @ResultType(Long.class)
    long countBigUser(@Param("info") PaUserInfo info);

    @Update("update pa_user_info pui ,pa_agent_user pau set pui.pwd = #{newPwd} where pui.user_code = pau.user_code and pau.agent_no = #{agentNo} ")
    int updatePassword(@Param("agentNo") String agentNo, @Param("newPwd") String newPwd);

    @Update("update pa_user_card set bank_name=#{agent.bankName}, account=#{agent.accountNo},cnaps=#{agent.cnapsNo} where user_code=#{agent.userCode}")
    @ResultType(Integer.class)
    int updatePaUserCard(@Param("agent") AgentInfo agent);

    @Select("select pui.grade,(SELECT CONCAT('Lv.',ladder.ladder_grade,'(万分之',ladder.val,')')	FROM pa_ladder_setting ladder "
            + "WHERE ladder.type = '1' AND ladder.brand_code = pui.brand_code AND ladder.ladder_grade = pui.share_level) shareLevel "
            + " from pa_user_info pui left join pa_agent_user pau on pui.user_code = pau.user_code where pau.agent_no = #{agentNo}")
    @ResultType(Map.class)
    Map<String, String> selectShareLevel(@Param("agentNo") String agentNo);

    @Select("select count(0) from pa_user_info where id_card_no = #{idCardNo}")
    @ResultType(Integer.class)
    Integer selectByIdCardNo(@Param("idCardNo") String idCardNo);

    @Insert("INSERT INTO pa_user_card (user_code, bank_name, bank_branch_name, account, mobile, "
            + "cnaps, address, card_type, is_settle, create_time)"
            + "VALUES (#{cardInfo.userCode}, #{cardInfo.bankName},#{cardInfo.bankBranchName},#{cardInfo.account},#{cardInfo.mobile},"
            + "#{cardInfo.cnaps},#{cardInfo.address},#{cardInfo.cardType},#{cardInfo.isSettle},now())")
    int insertIdCardNo(@Param("cardInfo") PeragentCard cardInfo);
    
    @Update(" UPDATE pa_user_info SET real_name = #{realName}, id_card_no = #{idCardNo},mobile = #{mobilephone},pwd = #{password} WHERE user_code = #{userCode} ")
    int updatePaUserInfo(@Param("realName") String realName, @Param("idCardNo") String idCardNo,
                         @Param("mobilephone") String mobilephone, @Param("password") String password, @Param("userCode") String userCode);
    
    @Update(" UPDATE pa_user_info SET mobile = #{mobilephone},pwd = #{password} WHERE user_code = #{userCode} ")
    int updatePaUserInfoNoSafePhone(@Param("realName") String realName, @Param("idCardNo") String idCardNo,
    		@Param("mobilephone") String mobilephone, @Param("password") String password, @Param("userCode") String userCode);

    /**
     * 新增的时候更新
     *
     * @param realName
     * @param idCardNo
     * @param userCode
     * @return
     */
    @Update(" UPDATE pa_user_info SET real_name = #{realName}, id_card_no = #{idCardNo} WHERE user_code = #{userCode} ")
    int updatePaUserInfoForInsert(@Param("realName") String realName, @Param("idCardNo") String idCardNo, @Param("userCode") String userCode);

    /**
     * 根据agentNo查询登录代理商对应的人人代理账号信息
     *
     * @param agentNo
     * @return
     */
    @Select("SELECT * FROM pa_user_info pui LEFT JOIN pa_agent_user pau ON pui.user_code=pau.user_code WHERE pau.agent_no=#{agentNo}")
    @ResultType(PaUserInfo.class)
    PaUserInfo selectUserByAgentNo(@Param("agentNo") String agentNo);

    @Select("SELECT * FROM pa_user_info where user_code = #{userCode}")
    @ResultType(PaUserInfo.class)
    PaUserInfo selectByUserCode(@Param("userCode") String userCode);
    
    @Select("SELECT * FROM pa_user_info where user_node like concat(#{userNode},'%')")
    @ResultType(PaUserInfo.class)
    List<PaUserInfo> selectByUserNode(@Param("userNode")String userNode);

    @Select("SELECT count(1) FROM pa_user_info where mobile = #{mobilephone} and user_code != #{userCode}")
    Integer selectByPhone(@Param("mobilephone") String mobilephone, @Param("userCode") String userCode);

    @Select("SELECT ladder_grade as value,CONCAT('万',val) AS text FROM pa_ladder_setting WHERE brand_code=#{brandCode} AND TYPE = '1' AND ladder_grade <= #{agentShareLevel}")
    @ResultType(Map.class)
    List<Map> getShareLevelList(@Param("brandCode") String brandCode, @Param("agentShareLevel") String agentShareLevel);

    @Insert(" INSERT INTO pa_agent_user (user_code, agent_no, agent_node, create_time) VALUES (#{userCode}, #{agentNo}, #{agentNode}, now()) ")
    @ResultType(Integer.class)
    Integer insertPaAgentUser(@Param("userCode")String userCode, @Param("agentNo")String agentNo, @Param("agentNode")String agentNode);
    
    @Insert(" INSERT INTO pa_user_upgrade (user_code, operater, create_time) VALUES (#{userCode}, #{entityUserCode}, now()) ")
    @ResultType(Integer.class)
    Integer insertPaUserUpgrade(@Param("userCode")String userCode, @Param("entityUserCode")String entityUserCode);

    @Update("update pa_mer_info set agent_no = #{agentNo},agent_node = #{agentNode} where user_node like concat(#{userNode},'%')")
    @ResultType(Integer.class)
    Integer updatePaMerInfo(@Param("userNode")String userNode, @Param("agentNo")String agentNo, @Param("agentNode")String agentNode);

    @Update("update pa_trans_info set agent_no = #{agentNo},agent_node = #{agentNode} where user_node like concat(#{userNode},'%')")
    @ResultType(Integer.class)
    Integer updatePaTransInfo(@Param("userNode")String userNode, @Param("agentNo")String agentNo, @Param("agentNode")String agentNode);

    @Update("update pa_order set is_platform = '2' where poster_code = #{userCode}")
    @ResultType(Integer.class)
	Integer updatePaOrder(@Param("userCode")String userCode);

    @Update("update pa_user_info set agent_no = #{agentNo},agent_node = #{agentNode},two_user_code = #{userCode} where user_node like concat(#{userNode},'%')")
    @ResultType(Integer.class)
    Integer updatePaUserInfoByUserNode(@Param("userNode")String userNode, @Param("agentNo")String agentNo, @Param("agentNode")String agentNode, @Param("userCode")String userCode);
    
    @Update("update pa_user_info set user_type = '2' where user_code = #{userCode}")
    @ResultType(Integer.class)
    Integer updatePaUserInfoByUserCode(@Param("userCode")String userCode);

    @Select("select * from pa_mer_info where user_node like concat(#{userNode},'%')")
    @ResultType(List.class)
	List<PaMerInfo> selectPaMerInfo(@Param("userNode")String userNode);
    
    @Select("select * from pa_brand where brand_code = #{agentOem}")
    @ResultType(Map.class)
    Map<String, Object> selectFromPaBrand(@Param("agentOem")String agentOem);

    @Update("update pa_user_info set can_profit_change = #{canProfitChange} where user_code = #{userCode}")
    @ResultType(Integer.class)
	Integer updateCanProfitChange(@Param("canProfitChange")String canProfitChange, @Param("userCode")String userCode);

    @Select("select one_agent_status from pa_brand where brand_code = #{agentOem}")
    @ResultType(Integer.class)
    Integer selectStatus(@Param("agentOem")String agentOem);
    
    @Select("select count(1) from pa_back_sn where order_no = #{orderNo}")
    @ResultType(Integer.class)
    Integer selectSnBackCount(@Param("orderNo")String orderNo);

    @SelectProvider(type = SqlProvider.class, method = "selectPaAfterSale")
    @ResultType(List.class)
	List<PaAfterSale> selectPaAfterSale(@Param("page")Page<PaAfterSale> page, @Param("info")PaAfterSale info);
    
    @SelectProvider(type = SqlProvider.class, method = "selectPaAfterSale")
    @ResultType(List.class)
    List<PaAfterSale> exportAfterSale(@Param("info")PaAfterSale info);
    
    @SelectProvider(type = SqlProvider.class, method = "countWaitHandleTotal")
    @ResultType(Integer.class)
    Integer countWaitHandleTotal(@Param("info")PaAfterSale day);
    
    @SelectProvider(type = SqlProvider.class, method = "countWaitHandleTotalTreeDays")
    @ResultType(Integer.class)
    Integer countWaitHandleTotalTreeDays(@Param("info")PaAfterSale day);
    
    @SelectProvider(type = SqlProvider.class, method = "countWaitHandleTotalSevenDays")
    @ResultType(Integer.class)
    Integer countWaitHandleTotalSevenDays(@Param("info")PaAfterSale day);
    
    @SelectProvider(type = SqlProvider.class, method = "selectSnBackByParam")
    @ResultType(List.class)
	List<PaSnBack> selectSnBackByParam(@Param("page")Page<PaSnBack> page, @Param("info")PaSnBack info);
    
    @SelectProvider(type = SqlProvider.class, method = "selectSnBackByParam")
    @ResultType(List.class)
    List<PaSnBack> exportSnBack(@Param("info")PaSnBack info);
    
    @Select("select sn from pa_back_sn where order_no = #{orderNo}")
    @ResultType(List.class)
    List<Map<String, Object>> selectSnByOrder(@Param("orderNo")String orderNo);
    
    @Select("select * from pa_mer_info where merchant_no = #{merchantNo}")
    @ResultType(Map.class)
    Map<String, Object> selectFromPaMerInfoByMerchantNo(@Param("merchantNo")String merchantNo);
    
    @Update("update pa_terminal_back set status = #{status},last_update_time = now() where order_no = #{orderNo}")
    @ResultType(Integer.class)
    Integer updateStatus(@Param("orderNo")String orderNo,@Param("status")String status);
    
    @Update("update pa_after_sale set handler=1,deal_desc = #{dealDesc},deal_img = #{dealImg},deal_time = now(),status = '2' WHERE order_no = #{orderNo}")
    @ResultType(Integer.class)
    Integer updateNowAfterSale(@Param("orderNo")String orderNo, @Param("dealDesc")String dealDesc,@Param("dealImg") String dealImg);
    
    @Select("select sn from pa_back_sn where order_no = #{orderNo}")
    @ResultType(List.class)
	List<String> selectSnFromPaBackSN(@Param("orderNo")String orderNo);
    
    @Select("select * from pa_trans_info where trans_no = #{orderNo}")
    @ResultType(Map.class)
    Map<String, Object> selectFromPaTransInfo(@Param("orderNo")String orderNo);
    
//    @Select("select count(*) num from pa_mer_info pmi,pa_activity_detail pad where pmi.merchant_no=pad.merchant_no and pmi.user_node like concat(#{userNode},'%')")
    @Select("select count(*) from pa_mer_info pai where pai.is_act=1 and pai.user_node like concat(#{userNode},'%') and pai.user_node != #{userNode}")
    @ResultType(Integer.class)
    Integer selectAllMerTotal(@Param("userNode")String userNode);
    
    @Select("select * from pa_user_account where user_code = #{userCode}")
    @ResultType(Integer.class)
    Map<String, String> selectFromPaUserAccount(@Param("userCode")String userCode);
    
    @Select(" SELECT COUNT(*) FROM pa_mer_info WHERE user_code = #{userCode}")
    @ResultType(Long.class)
    Long selectMerTotal(@Param("userCode")String userCode);

    @Select(" SELECT COUNT(*) FROM pa_user_info WHERE parent_id= #{userCode}")
    @ResultType(Long.class)
	Long selectAllyTotal(@Param("userCode")String userCode);
    
    @Select(" SELECT PARAM_VALUE AS value FROM pa_sys_config  WHERE PARAM_KEY = #{key}")
    @ResultType(String.class)
    String selectMd5Key(@Param("key")String key);

    @Select("SELECT ladder_grade FROM pa_ladder_setting " +
            "WHERE brand_code=#{brandCode} AND TYPE = '1' AND ladder_grade <= #{agentShareLevel}")
    @ResultType(Integer.class)
    List<Integer> selectShareLevelList(@Param("brandCode") String brandCode, @Param("agentShareLevel") String agentShareLevel);

    @Select("SELECT ptb.id " +
            "FROM pa_terminal_back ptb " +
            "LEFT OUTER JOIN  pa_user_info pui1 ON ptb.user_code = pui1.user_code " +
            "WHERE pui1.user_node LIKE CONCAT(#{userNode},'%') AND ptb.order_no=#{orderNo}")
    @ResultType(String.class)
    String selectPaTerminalBackByOrderNo(@Param("userNode")String userNode, @Param("orderNo")String orderNo);
    
    @SelectProvider(type = SqlProvider.class, method = "selectPaCashBackDetail")
    @ResultType(List.class)
    List<PaCashBackDetail> selectPaCashBackDetail(@Param("page")Page<PaCashBackDetail> page, @Param("info")PaCashBackDetail info);
    
    @SelectProvider(type = SqlProvider.class, method = "selectPaCashBackDetail")
    @ResultType(List.class)
    List<PaCashBackDetail> exportCashBackDetail(@Param("info")PaCashBackDetail info);
    
    @SelectProvider(type = SqlProvider.class, method = "sumCashBackAmount")
    @ResultType(BigDecimal.class)
    BigDecimal sumCashBackAmount(@Param("info")PaCashBackDetail info,@Param("entryStatus")String entryStatus);

    public class SqlProvider {

    	public String sumCashBackAmount(Map<String, Object> param) {
            SQL sql = new SQL() {
                {
                	SELECT(" sum(pcb.cash_back_amount) ");
                    FROM(" pa_cash_back_detail pcb ");
                    LEFT_OUTER_JOIN(" pa_activity_detail pad on pcb.active_order = pad.active_order ");
                    LEFT_OUTER_JOIN(" pa_user_info pui on pcb.user_code = pui.user_code ");
              }
          };
          cashBackDetailSqlWhere(sql,param);
          sql.WHERE(" pcb.entry_status = #{entryStatus}");
          return sql.toString();
      }
    	public String selectPaCashBackDetail(Map<String, Object> param) {
        	SQL sql = new SQL() {
        		{
        			SELECT(" pcb.active_order,pcb.cash_back_amount,pcb.user_code,pad.activity_code,pad.activity_type_no,pad.trans_amount,"
        					+ "pcb.cash_back_amount,pcb.entry_status,pcb.create_time,pcb.entry_time,pui.user_node,pui.real_name ");
        			FROM(" pa_cash_back_detail pcb ");
        			LEFT_OUTER_JOIN(" pa_activity_detail pad on pcb.active_order = pad.active_order ");
        			LEFT_OUTER_JOIN(" pa_user_info pui on pcb.user_code = pui.user_code ");
        		}
        	};
        	cashBackDetailSqlWhere(sql,param);
        	sql.ORDER_BY("pcb.create_time DESC");
        	return sql.toString();
        }
        private void cashBackDetailSqlWhere(SQL sql, Map<String, Object> param) {
        	final PaCashBackDetail info = (PaCashBackDetail) param.get("info");
			if (info.getAgentLevel() == 1) {
				sql.WHERE(" pui.user_node like concat(#{info.loginUserNode},'%') ");
			}else{
//				sql.WHERE(" pcb.user_code = #{info.loginUserCode} ");
				sql.WHERE(" (pui.user_code = #{info.loginUserCode} or pui.parent_id = #{info.loginUserCode})");
			}
            if(StringUtils.isNotBlank(info.getActiveOrder())){
            	sql.WHERE("pcb.active_order = #{info.activeOrder}");
			}
            if(StringUtils.isNotBlank(info.getEntryStatus())){
            	sql.WHERE("pcb.entry_status = #{info.entryStatus}");
            }
            if(StringUtils.isNotBlank(info.getRealName())){
            	sql.WHERE("pui.real_name like concat('%',#{info.realName},'%')");
            }
            if(StringUtils.isNotBlank(info.getUserCode())){
            	sql.WHERE("pcb.user_code = #{info.userCode}");
            }
            if(StringUtils.isNotBlank(info.getEntryTimeStart())){
            	sql.WHERE("pcb.entry_time >= #{info.entryTimeStart}");
            }
            if(StringUtils.isNotBlank(info.getEntryTimeEnd())){
            	sql.WHERE("pcb.entry_time <= #{info.entryTimeEnd}");
            }
		}
      public String selectSnBackByParam(Map<String, Object> param) {
      	final PaSnBack info = (PaSnBack) param.get("info");
      	SQL sql = new SQL() {
      		{
      			SELECT(" ptb.order_no,ptb.user_code,ptb.receive_user_code,pui.user_type receiveUserType,pui.one_user_code beLongUserCode,ptb.status,ptb.create_time,ptb.last_update_time ");
      			FROM(" pa_terminal_back ptb  ");
      			LEFT_OUTER_JOIN(" pa_user_info pui on ptb.receive_user_code = pui.user_code ");
      			LEFT_OUTER_JOIN(" pa_user_info pui1 on ptb.user_code = pui1.user_code ");
      			if (info.getEntityLevel() == 1) {
      				WHERE("pui1.user_node like concat(#{info.userNode},'%')");
      			}else{
      				WHERE(" (pui1.user_node = #{info.userNode} or pui1.parent_id = #{info.parentId})");
      			}
        			if(StringUtils.isNotBlank(info.getOrderNo())){
        				WHERE("ptb.order_no = #{info.orderNo}");
        			}
        			if(StringUtils.isNotBlank(info.getUserCode())){
        				WHERE("ptb.user_code = #{info.userCode}");
        			}
        			if(StringUtils.isNotBlank(info.getReceiveUserCode())){
        				WHERE("ptb.receive_user_code = #{info.receiveUserCode}");
        			}
        			if(StringUtils.isNotBlank(info.getStatus())){
        				WHERE("ptb.status = #{info.status}");
        			}
        			if(StringUtils.isNotBlank(info.getBeLongUserCode())){
        				WHERE("pui.one_user_code = #{info.beLongUserCode}");
        			}
        			if(StringUtils.isNotBlank(info.getReceiveUserType())){
        				WHERE("pui.user_type = #{info.receiveUserType}");
        			}
        			if(StringUtils.isNotBlank(info.getCreateTimeStart())){
        				WHERE("ptb.create_time >= #{info.createTimeStart}");
        			}
        			if (StringUtils.isNotBlank(info.getCreateTimeEnd())) {
        				WHERE("ptb.create_time <= #{info.createTimeEnd}");
        			}
        			if(StringUtils.isNotBlank(info.getLastUpdateTimeStart())){
        				WHERE("ptb.last_update_time >= #{info.lastUpdateTimeStart}");
        			}
        			if (StringUtils.isNotBlank(info.getLastUpdateTimeEnd())) {
        				WHERE("ptb.last_update_time <= #{info.lastUpdateTimeEnd}");
        			}
        			ORDER_BY("ptb.create_time DESC");
        		}
        	};
        	return sql.toString();
        }
        public String countWaitHandleTotal(Map<String, Object> param) {
        	SQL sql = new SQL() {
        		{
        			SELECT(" count(*) ");
        			FROM(" pa_after_sale pas left join pa_user_info pui on pas.user_code = pui.user_code ");
        			WHERE(" pas.status = '0' and pui.user_node like concat(#{info.userNode},'%') and pas.handler = 1 ");
        		}
        	};
        	afterSaleWhere(sql,param);
        	return sql.toString();
        }
        public String countWaitHandleTotalTreeDays(Map<String, Object> param) {
        	SQL sql = new SQL() {
        		{
        			SELECT(" count(*) ");
        			FROM(" pa_after_sale pas left join pa_user_info pui on pas.user_code = pui.user_code ");
        			WHERE(" pas.status = '0' and pui.user_node like concat(#{info.userNode},'%') and "
        					+ "(date(pas.apply_time) <= DATE_SUB(CURDATE(), INTERVAL 3 DAY) and date(pas.apply_time) >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)) "
        					+ " and pas.handler = 1 ");
        		}
        	};
        	afterSaleWhere(sql,param);
        	return sql.toString();
        }
        public String countWaitHandleTotalSevenDays(Map<String, Object> param) {
        	SQL sql = new SQL() {
        		{
        			SELECT(" count(*) ");
        			FROM(" pa_after_sale pas left join pa_user_info pui on pas.user_code = pui.user_code ");
        			WHERE(" pas.status = '0' and pui.user_node like concat(#{info.userNode},'%')  and date(pas.apply_time) < DATE_SUB(CURDATE(), INTERVAL 7 DAY)"
        					+ " and pas.handler = 1  ");
        		}
        	};
        	afterSaleWhere(sql,param);
        	return sql.toString();
        }
        public String selectPaAfterSale(Map<String, Object> param) {
        	SQL sql = new SQL() {
        		{
        			SELECT(" pas.*");
        			FROM("pa_after_sale pas left join pa_user_info pui on pas.user_code = pui.user_code ");
        			WHERE("pui.user_node like concat(#{info.userNode},'%') and pas.status != 1 and pas.ascription<>2");
        		}
        	};
        	afterSaleWhere(sql,param);
        	sql.ORDER_BY("pas.apply_time DESC");
        	return sql.toString();
        }
        private void afterSaleWhere(SQL sql, Map<String, Object> param) {
            final PaAfterSale info = (PaAfterSale) param.get("info");
			if(StringUtils.isNotBlank(info.getOrderNo())){
				sql.WHERE("pas.order_no = #{info.orderNo}");
			}
            if(StringUtils.isNotBlank(info.getPayOrder())){
            	sql.WHERE("pas.pay_order = #{info.payOrder}");
            }
            if(StringUtils.isNotBlank(info.getSaleType())){
            	sql.WHERE("pas.sale_type = #{info.saleType}");
            }
            if(StringUtils.isNotBlank(info.getStatus())){
            	sql.WHERE("pas.status = #{info.status}");
            }
            if (StringUtils.isNotBlank(info.getApplyTimeBegin())) {
            	sql.WHERE("pas.apply_time >= #{info.applyTimeBegin}");
            }
            if (StringUtils.isNotBlank(info.getApplyTimeEnd())) {
            	sql.WHERE("pas.apply_time <= #{info.applyTimeEnd}");
            }
            if (StringUtils.isNotBlank(info.getDealTimeBegin())) {
            	sql.WHERE("pas.deal_time >= #{info.dealTimeBegin}");
            }
            if (StringUtils.isNotBlank(info.getDealTimeEnd())) {
            	sql.WHERE("pas.deal_time <= #{info.dealTimeEnd}");
            }
		}
        public String selectPaUserByParam(Map<String, Object> param) {
        	final PaUserInfo info = (PaUserInfo) param.get("info");
        	SQL sql = new SQL() {
        		{
        			SELECT("u.user_code,u.real_name,u.mobile,u.status,u.agent_no,u.grade,u.share_level,u.create_time,u.user_type," +
        					"u.user_code IN (SELECT user_code FROM pa_user_card) AS isBindCard,u.can_profit_change,u.user_node,u.nick_name," +
        					"u2.user_type AS parentType,u.parent_id,u2.real_name as parentName," +
        					"u.parent_id='" + info.getLoginUserCode() + "' as isDirect," +
        					"CONCAT('万',pls.val) AS levelShow," + 
        					" concat('万',pls2.val) as vipLevelShow "
//        					"(SELECT COUNT(id) FROM pa_mer_info WHERE user_code=u.user_code) AS merTotal," +
//        					"(SELECT COUNT(id) FROM pa_user_info WHERE parent_id=u.user_code) AS allyTotal, " +
//        					"(select count(0) from pa_mer_info pmi inner join pa_activity_detail pad on pmi.merchant_no = pad.merchant_no " +
//        					"where pmi.user_node like concat(u.user_node,'%')) as allMerTotal"
        					);
        			FROM("pa_user_info u");
        			LEFT_OUTER_JOIN("pa_user_info u2 ON u.parent_id=u2.user_code");
        			LEFT_OUTER_JOIN("pa_ladder_setting pls ON pls.brand_code=u.brand_code AND pls.type='1' AND pls.ladder_grade=u.share_level");
        			LEFT_OUTER_JOIN("pa_ladder_setting pls2 ON pls2.brand_code=u.brand_code AND pls2.type='3' AND pls2.ladder_grade=u.vip_share_level");
        		}
        	};
        	sqlWhere(sql, param);
        	sql.ORDER_BY("create_time DESC");
        	return sql.toString();
        }

        public String countUserFromMer(Map<String, Object> param) {
            SQL sql = new SQL() {
                {
                    SELECT("COUNT(u.id)");
                    FROM("pa_user_info u");
                    LEFT_OUTER_JOIN("pa_user_info u2 ON u.parent_id=u2.user_code");
                    WHERE("u.user_code IN(SELECT user_code FROM pa_mer_user_info)");
                }
            };
            sqlWhere(sql, param);
            return sql.toString();
        }

        public String countUser(Map<String, Object> param) {
            SQL sql = new SQL() {
                {
                    SELECT("SUM(CASE WHEN u.user_type=3 THEN 1 ELSE 0 END)");
                    FROM("pa_user_info u");
                    LEFT_OUTER_JOIN("pa_user_info u2 ON u.parent_id=u2.user_code");
                }
            };
            sqlWhere(sql, param);
            return sql.toString();
        }

        public String countBigUser(Map<String, Object> param) {
            SQL sql = new SQL() {
                {
                    SELECT("SUM(CASE WHEN u.user_type=2 THEN 1 ELSE 0 END)");
                    FROM("pa_user_info u");
                    LEFT_OUTER_JOIN("pa_user_info u2 ON u.parent_id=u2.user_code");
                }
            };
            sqlWhere(sql, param);
            return sql.toString();
        }

        public String selectChildPaUser(Map<String, Object> param) {
            final PaUserInfo info = (PaUserInfo) param.get("info");
            if (!"1".equals(info.getUserType()) && "!2".equals(info.getUserType())) {
                return null;
            }
            info.setUserNode(info.getUserNode() + "M");//不包括自身
            //final String realName = (String)param.get("realName");
            //final String userNode = (String)param.get("userNode");
            //final String userCode = (String)param.get("userCode");
            return new SQL() {{
                SELECT("user_code,real_name,agent_no,agent_node");
                FROM("pa_user_info");
                //if(StringUtils.isNotBlank(info.getRealName())){
                //   WHERE("real_name like concat(#{info.realName},'%')");
                //}
                if ("1".equals(info.getUserType())) {
                    WHERE("user_node like concat(#{info.userNode},'%')");
                } else if ("2".equals(info.getUserType())) {
                    WHERE("parent_id=#{info.userCode}");
                }

                WHERE("real_name is not null");
            }}.toString();
        }

        public void sqlWhere(SQL sql, Map<String, Object> param) {
            final PaUserInfo info = (PaUserInfo) param.get("info");
            sql.WHERE("u.agent_node like concat(#{info.agentNode}, '%')");
            if (StringUtils.isNotBlank(info.getNickName())) {
            	sql.WHERE("u.nick_name like concat('%',#{info.nickName},'%')");
            }
            if (StringUtils.isNotBlank(info.getUserCode())) {
                sql.WHERE("u.user_code=#{info.userCode}");
            }
            if (StringUtils.isNotBlank(info.getCanProfitChange())) {
            	sql.WHERE("u.can_profit_change=#{info.canProfitChange}");
            }
            if (StringUtils.isNotBlank(info.getUserType())) {
                sql.WHERE("u.user_type=#{info.userType}");
            } else {
                sql.WHERE("u.user_type!=1");
            }
            if (StringUtils.isNotBlank(info.getParentId())) {
                sql.WHERE("u.parent_id=#{info.parentId}");
            }
            if (StringUtils.isNotBlank(info.getRealName())) {
                sql.WHERE("u.real_name LIKE CONCAT('%',#{info.realName},'%')");
            }
            if (StringUtils.isNotBlank(info.getMobile())) {
                sql.WHERE("u.mobile=#{info.mobile}");
            }
            if (StringUtils.isNotBlank(info.getAgentNo())) {
                sql.WHERE("u.agent_no=#{info.agentNo}");
            }
            if (StringUtils.isNotBlank(info.getParentType())) {
                sql.WHERE("u2.user_type=#{info.parentType}");
            }
            if (StringUtils.isNotBlank(info.getGrade())) {
                sql.WHERE("u.grade=#{info.grade}");
            }
            if (StringUtils.isNotBlank(info.getIsBindCard())) {
                if ("0".equals(info.getIsBindCard())) {
                    sql.WHERE("u.user_code NOT IN (SELECT user_code FROM pa_user_card)");
                }
                if ("1".equals(info.getIsBindCard())) {
                    sql.WHERE("u.user_code IN (SELECT user_code FROM pa_user_card)");
                }
            }
            if (StringUtils.isNotBlank(info.getCreateTimeBegin())) {
                sql.WHERE("u.create_time>=#{info.createTimeBegin}");
            }
            if (StringUtils.isNotBlank(info.getCreateTimeEnd())) {
                sql.WHERE("u.create_time<=#{info.createTimeEnd}");
            }
        }

    }

}
