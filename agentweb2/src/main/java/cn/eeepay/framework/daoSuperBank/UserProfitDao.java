package cn.eeepay.framework.daoSuperBank;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.OrderMainSum;
import cn.eeepay.framework.model.UserProfit;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * @author tans
 * @Date 2017-12-4
 */
public interface UserProfitDao {


    @Select("select profit_formula,user_type from user_profit where order_no = #{orderNo}")
    @ResultType(UserProfit.class)
    List<UserProfit> selectByOrderNo(String orderNo);

    @SelectProvider(type = SqlProvider.class, method="selectProfitDetailPage")
    @ResultType(UserProfit.class)
    List<UserProfit> selectProfitDetailPage(@Param("baseInfo")UserProfit baseInfo, @Param("page")Page<UserProfit> page);

    @SelectProvider(type = SqlProvider.class, method="selectMposProfitDetailPage")
    @ResultType(UserProfit.class)
    List<UserProfit> selectMposProfitDetailPage(@Param("baseInfo")UserProfit baseInfo, @Param("page")Page<UserProfit> page);


    @SelectProvider(type = SqlProvider.class, method="selectUserProfitSum")
    @ResultType(OrderMainSum.class)
    OrderMainSum selectUserProfitSum(@Param("baseInfo")UserProfit baseInfo);

    @SelectProvider(type = SqlProvider.class, method="selectTotalProfitSum")
    @ResultType(OrderMainSum.class)
    OrderMainSum selectTotalProfitSum(@Param("baseInfo")UserProfit baseInfo);

    class SqlProvider{
        public String selectProfitDetailPage(Map<String, Object> param){
            UserProfit baseInfo = (UserProfit) param.get("baseInfo");
            SQL sql = new SQL();
            sql.SELECT("up.id,up.order_type,up.status,up.order_no");
            sql.SELECT("up.org_id,up.share_user_code,up.user_code,up.user_type");
            sql.SELECT("up.total_profit,up.user_profit,up.create_date,up.account_status");
            sql.SELECT("share.nick_name shareNickName,share.user_name shareUserName,share.phone shareUserPhone");
            sql.SELECT("user.user_name,om.second_user_node,profit_level");
            sql.FROM("user_profit up left join order_main om on up.order_no = om.order_no ");
            whereSql(baseInfo, sql);
            return sql.toString();
        }
        public String selectMposProfitDetailPage(Map<String, Object> param){
            UserProfit baseInfo = (UserProfit) param.get("baseInfo");
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("SELECT");
            stringBuffer.append(" up.id,up.order_type,up.`status`,up.order_no, ");
            stringBuffer.append(" up.org_id,up.share_user_code,up.user_code,up.user_type, ");
            stringBuffer.append(" up.total_profit,up.user_profit,up.create_date,up.account_status, ");
            stringBuffer.append(" up.profit_level,up.remark, ");
            stringBuffer.append(" share.nick_name shareNickName,share.user_name shareUserName,share.phone shareUserPhone, ");
            stringBuffer.append(" share.open_province,share.open_city,share.open_region,share.remark as shareUserRemark, ");
            stringBuffer.append(" user.user_name");
            stringBuffer.append(" FROM user_profit up");
            stringBuffer.append(" LEFT OUTER JOIN user_info share on share.user_code = up.share_user_code ");
            stringBuffer.append(" LEFT OUTER JOIN user_info user on user.user_code = up.user_code ");
            stringBuffer.append(" WHERE 1=1");
            if(StringUtils.isNotBlank(baseInfo.getOrderNo())){
                stringBuffer.append(" AND up.order_no = #{baseInfo.orderNo}");
            }
            if(StringUtils.isNotBlank(baseInfo.getStatus())){
                stringBuffer.append(" AND up.status = #{baseInfo.status}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOrderType())){
                stringBuffer.append(" AND up.order_type = #{baseInfo.orderType}");
            }else{
//                101采购订单 102激活订单(奖金) 103激活订单(返现) 104交易订单
                stringBuffer.append(" AND up.order_type in( 101 , 102 , 103 ,104 )");
            }
            if(StringUtils.isNotBlank(baseInfo.getCreateDateStart())){
                stringBuffer.append(" AND up.create_date >= #{baseInfo.createDateStart}");
            }
            if(StringUtils.isNotBlank(baseInfo.getCreateDateEnd())){
                stringBuffer.append(" AND up.create_date <= #{baseInfo.createDateEnd}");
            }
            if(StringUtils.isNotBlank(baseInfo.getShareUserCode())){
                stringBuffer.append(" AND up.share_user_code = #{baseInfo.shareUserCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getShareUserPhone())){
                stringBuffer.append(" AND share.phone = #{baseInfo.shareUserPhone}");
            }
            if(StringUtils.isNotBlank(baseInfo.getUserCode())){
                stringBuffer.append(" AND up.user_code = #{baseInfo.userCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getUserPhone())){
                stringBuffer.append(" AND user.phone = #{baseInfo.userPhone}");
            }
            if(StringUtils.isNotBlank(baseInfo.getAccountStatus())){
                stringBuffer.append(" AND up.account_status = #{baseInfo.accountStatus}");
            }
            if(baseInfo.getOrgId() != null && -1 != baseInfo.getOrgId()){
                stringBuffer.append(" AND up.org_id = #{baseInfo.orgId}");
            }
            if(StringUtils.isNotBlank(baseInfo.getUserType())){
                stringBuffer.append(" AND up.user_type = #{baseInfo.userType}");
            }
            if(StringUtils.isNotBlank(baseInfo.getUserName())){
                baseInfo.setUserName(baseInfo.getUserName() + "%");
                stringBuffer.append(" AND user.user_name like #{baseInfo.userName}");
            }
            if(StringUtils.isNotBlank(baseInfo.getShareUserName())){
                baseInfo.setShareUserName(baseInfo.getShareUserName() + "%");
                stringBuffer.append(" AND share.user_name like #{baseInfo.shareUserName}");
            }
            if(StringUtils.isNotBlank(baseInfo.getRemark())){
                baseInfo.setRemark(baseInfo.getRemark() + "%");
                stringBuffer.append(" AND up.remark like #{baseInfo.remark}");
            }
            if(StringUtils.isNotBlank(baseInfo.getShareUserRemark())){
                baseInfo.setShareUserRemark(baseInfo.getShareUserRemark() + "%");
                stringBuffer.append(" AND share.remark like #{baseInfo.shareUserRemark}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOpenProvince()) && !"全部".equals(baseInfo.getOpenProvince())){
                stringBuffer.append(" AND share.open_province = #{baseInfo.openProvince}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOpenCity()) && !"全部".equals(baseInfo.getOpenCity())){
                stringBuffer.append(" AND share.open_city = #{baseInfo.openCity}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOpenRegion()) && !"全部".equals(baseInfo.getOpenRegion())){
                stringBuffer.append(" AND share.open_region = #{baseInfo.openRegion}");
            }
            stringBuffer.append(" AND up.org_id = #{baseInfo.entityId}");
            stringBuffer.append(" ORDER BY up.create_date desc");
            System.out.println("mpos===="+stringBuffer.toString());
            return stringBuffer.toString();
        }

        public String selectUserProfitSum(Map<String, Object> param){
            UserProfit baseInfo = (UserProfit) param.get("baseInfo");
            SQL sql = new SQL();
            sql.SELECT("sum(up.user_profit) as profitSum");
            sql.FROM("user_profit up left join order_main om on up.order_no = om.order_no ");
            whereSql(baseInfo, sql);
//            sql.WHERE("up.status = '5'");
            sql.WHERE("up.org_id = #{baseInfo.entityId}");
            return sql.toString();
        }

        public String selectTotalProfitSum(Map<String, Object> param){
            UserProfit baseInfo = (UserProfit) param.get("baseInfo");
            SQL sql = new SQL();
            sql.SELECT_DISTINCT("up.order_no, up.total_profit");
            sql.FROM("user_profit up left join order_main om on up.order_no = om.order_no ");
            whereSql(baseInfo, sql);
//            sql.WHERE("up.status = '5'");
            StringBuilder sb = new StringBuilder();
            sb.append("select sum(t.total_profit) as totalBonusSum");
            sb.append(" from (").append(sql.toString()).append(") as t");
            return sb.toString();
        }

        private void whereSql(UserProfit baseInfo, SQL sql) {
//            sql.FROM("user_profit up");
            sql.LEFT_OUTER_JOIN("user_info share on share.user_code = up.share_user_code");
            sql.LEFT_OUTER_JOIN("user_info user on user.user_code = up.user_code");
            sql.WHERE("up.user_type <> '60'");//去掉平台的分润
            if (StringUtils.isNotBlank(baseInfo.getRemark())) {
    			sql.WHERE("user.remark = #{baseInfo.remark}");
    		}
    		if (StringUtils.isNotBlank(baseInfo.getOpenProvince())) {
    			sql.WHERE("user.open_province = #{baseInfo.openProvince}");
    		}
    		if (StringUtils.isNotBlank(baseInfo.getOpenCity())) {
    			sql.WHERE("user.open_city = #{baseInfo.openCity}");
    		}
    		if (StringUtils.isNotBlank(baseInfo.getOpenRegion())) {
    			sql.WHERE("user.open_region = #{baseInfo.openRegion}");
    		}
            if(StringUtils.isNotBlank(baseInfo.getOrderNo())){
                sql.WHERE("up.order_no = #{baseInfo.orderNo}");
            }
            if(StringUtils.isNotBlank(baseInfo.getStatus())){
                sql.WHERE("up.status = #{baseInfo.status}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOrderType())){
                sql.WHERE("up.order_type = #{baseInfo.orderType}");
            }else{
                sql.WHERE("up.order_type not in( 101 , 102 , 103 ,104 )");
            }
            if(StringUtils.isNotBlank(baseInfo.getCreateDateStart())){
                sql.WHERE("up.create_date >= #{baseInfo.createDateStart}");
            }
            if(StringUtils.isNotBlank(baseInfo.getCreateDateEnd())){
                sql.WHERE("up.create_date <= #{baseInfo.createDateEnd}");
            }
            if(StringUtils.isNotBlank(baseInfo.getShareUserCode())){
                sql.WHERE("up.share_user_code = #{baseInfo.shareUserCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getShareUserPhone())){
                sql.WHERE("share.phone = #{baseInfo.shareUserPhone}");
            }
            if(StringUtils.isNotBlank(baseInfo.getUserCode())){
                sql.WHERE("up.user_code = #{baseInfo.userCode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getUserPhone())){
                sql.WHERE("user.phone = #{baseInfo.userPhone}");
            }
            if(StringUtils.isNotBlank(baseInfo.getAccountStatus())){
                sql.WHERE("up.account_status = #{baseInfo.accountStatus}");
            }
            if(baseInfo.getOrgId() != null && -1 != baseInfo.getOrgId()){
                sql.WHERE("up.org_id = #{baseInfo.orgId}");
            }
            if(StringUtils.isNotBlank(baseInfo.getSecondUserNode())){
                sql.WHERE("om.second_user_node = #{baseInfo.secondUserNode}");
            }
            if(StringUtils.isNotBlank(baseInfo.getOEM())){
            	sql.WHERE("up.user_type != '50'");
            }
            if(StringUtils.isNotBlank(baseInfo.getUserType())){
            	sql.WHERE("up.user_type = #{baseInfo.userType}");
            }
            if(StringUtils.isNotBlank(baseInfo.getShareUserName())){
            	sql.WHERE("share.user_name = #{baseInfo.shareUserName}");
            }
            if(StringUtils.isNotBlank(baseInfo.getUserName())){
            	sql.WHERE("user.user_name = #{baseInfo.userName}");
            }
            sql.WHERE("up.org_id = #{baseInfo.entityId}");
            sql.ORDER_BY("up.create_date desc");
        }

    }

}
