package cn.eeepay.framework.daoSuperBank;

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
import cn.eeepay.framework.model.RedAccountInfo;
import cn.eeepay.framework.model.RedEnvelopesDetails;
import cn.eeepay.framework.model.UserInfoSuperBank;

/**
 * 银行家红包
 * @author Administrator
 *
 */
public interface RedEnvelopesDao {
	
	/**
	 * 红包提现成功,插入红包提现明细
	 * @param details
	 * @return
	 */
	@Insert(" insert into red_account_detail(id,red_account_id,account_code,create_date,type,trans_amount,red_order_id,remark,withd_on) "
			+ "values(#{details.id},#{details.redAccountId},#{details.accountCode},now(),#{details.type},#{details.transAmount},#{details.redOrderId},null,#{details.withdOn})")
	Integer insertRedEnvelopesDetail(@Param("details")RedEnvelopesDetails details);
	
	/**
	 * 查询到当前登录代理商的红包账户余额信息
	 * @param entityId
	 * @return
	 */
	@Select("select * from red_account_info where relation_id = #{entityId} and type = '1'")
    @ResultType(RedAccountInfo.class)
	RedAccountInfo selectRedAccountInfo(@Param("entityId")String entityId);

	/**
	 * 超级银行家红包明细查询  用户
	 * @param info
	 * @param entityId
	 * @return
	 */
	@SelectProvider(type = SqlProvider.class, method="selectRedEnvelopesDetailsByUser")
	@ResultType(RedEnvelopesDetails.class)
	List<RedEnvelopesDetails> selectRedEnvelopesDetailsByUser(@Param("info")RedEnvelopesDetails info,
			@Param("entityId")String entityId,@Param("page")Page<RedEnvelopesDetails> page);
	/**
	 * 超级银行家红包明细查询 代理商
	 * @param info
	 * @param entityId
	 * @return
	 */
	@SelectProvider(type = SqlProvider.class, method="selectRedEnvelopesDetailsByAgent")
	@ResultType(RedEnvelopesDetails.class)
	List<RedEnvelopesDetails> selectRedEnvelopesDetailsByAgent(@Param("info")RedEnvelopesDetails info, 
			@Param("entityId")String entityId,@Param("page")Page<RedEnvelopesDetails> page);
	
	@SelectProvider(type = SqlProvider.class, method="selectRedEnvelopesDetailsByUser")
	@ResultType(RedEnvelopesDetails.class)
	List<RedEnvelopesDetails> exportSelectRedEnvelopesDetailsByUser(@Param("info")RedEnvelopesDetails info,@Param("entityId")String entityId);

	@SelectProvider(type = SqlProvider.class, method="selectRedEnvelopesDetailsByAgent")
	@ResultType(RedEnvelopesDetails.class)
	List<RedEnvelopesDetails> exportSelectRedEnvelopesDetailsByAgent(@Param("info")RedEnvelopesDetails info,@Param("entityId")String entityId);

    /**
     * 查询登录代理商红包余额
     * @param entityId
     * @return
     */
    @Select("select rai.total_amount from red_account_info rai left join org_info oi on rai.relation_id = oi.org_id where oi.org_id = #{entityId}")
    @ResultType(String.class)
	String selectTotalAmountByAgent(@Param("entityId")String entityId);

	/**
	 * 查询登录用户红包余额
	 * @param entityId
	 * @return
	 */
    @Select("select rai.total_amount from red_account_info rai left join user_info ui on rai.relation_id = ui.user_id where ui.org_id = #{entityId}")
    @ResultType(String.class)
	String selectTotalAmountByUser(@Param("entityId")String entityId);

    /**
     * 更新代理商红包账户余额
     * @return
     */
    @Update(" update red_account_info set total_amount = '0' where relation_id = #{entityId} and type = '1'")
    Integer updateRedBalanceByAgent(@Param("entityId")String entityId);

    /**
     * 更新用户红包账户余额(用户不会有这个页面)
     * @return
     */
    @Update(" update red_account_info set total_amount = '0' where relation_id = #{entityId} and type = '2'")
	Integer updateRedBalanceByUser(@Param("entityId")String entityId);
    
    /**
     * 统计总金额
     * @param info
     * @param entityId
     * @return
     */
    @SelectProvider(type = SqlProvider.class, method="selectTotalAmount")
	@ResultType(Integer.class)
    Integer selectTotalAmount(@Param("info")RedEnvelopesDetails info, @Param("entityId")String entityId);

    @Select(" select max(id) from red_account_detail ")
    @ResultType(Integer.class)
    Integer selectId();
    
    public class SqlProvider{
        public String selectRedEnvelopesDetailsByUser(Map<String, Object> param){
        	RedEnvelopesDetails info = (RedEnvelopesDetails) param.get("info");
            SQL sql = new SQL(){{
            	SELECT(" rai.type,rad.red_order_id,rad.create_date,rad.type transType,if(rad.trans_amount>0,'增加','减少') operationType,rad.trans_amount ");
            	FROM(" red_account_detail rad "
            			+ "left join red_account_info rai on rad.red_account_id = rai.id "
            			+ "left join user_info ui on rai.relation_id = ui.user_id ");
            	WHERE("ui.org_id = #{entityId}");
            }};
            whereSql(info,sql);
            sql.ORDER_BY(" rad.create_date desc ");
            return sql.toString();
        }
        public String selectRedEnvelopesDetailsByAgent(Map<String, Object> param){
        	RedEnvelopesDetails info = (RedEnvelopesDetails) param.get("info");
        	SQL sql = new SQL(){{
        		SELECT(" rai.type,rad.red_order_id,rad.create_date,rad.type transType,if(rad.trans_amount>0,'增加','减少') operationType,rad.trans_amount ");
        		FROM(" red_account_detail rad "
        				+ "left join red_account_info rai on rad.red_account_id = rai.id "
        				+ "left join org_info oi on rai.relation_id = oi.org_id ");
        		WHERE("oi.org_id = #{entityId}");
        	}};
        	whereSql(info,sql);
        	sql.ORDER_BY(" rad.create_date desc ");
        	return sql.toString();
        }
        public String selectTotalAmount(Map<String, Object> param){
        	RedEnvelopesDetails info = (RedEnvelopesDetails) param.get("info");
        	SQL sql = new SQL(){{
        		SELECT(" sum(rad.trans_amount) totalAmount ");
        		FROM(" red_account_detail rad left "
        				+ "join red_account_info rai on rad.red_account_id = rai.id "
        				+ "left join org_info oi on rai.relation_id = oi.org_id ");
        		WHERE("oi.org_id = #{entityId}");
        	}};
        	whereSql(info,sql);
        	return sql.toString();
        }

        private void whereSql(RedEnvelopesDetails info, SQL sql) {
            if(StringUtils.isNotBlank(info.getStartCreateDate())){
                sql.WHERE("rad.create_date >= #{info.startCreateDate}");
            }
            if(StringUtils.isNotBlank(info.getEndCreateDate())){
            	sql.WHERE("rad.create_date <= #{info.endCreateDate}");
            }
            if(StringUtils.isNotBlank(info.getTransType())){
            	sql.WHERE("rad.type = #{info.transType}");
            }
            if(StringUtils.isNotBlank(info.getOperationType())){
            	if ("增加".equals(info.getOperationType())) {
            		sql.WHERE("rad.trans_amount > 0");
	            }else if ("减少".equals(info.getOperationType())){
	            	sql.WHERE("rad.trans_amount <= 0");
	            }
            }
        }
    }

}
