package cn.eeepay.framework.daoPerAgent;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.PaMerInfo;
import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * @author RPC
 * create 2018/09/11
 */
@WriteReadDataSource
public interface PerMerDao {

    /**
     * 盟主商户查询
     * @param page
     * @param info
     * @return
     */
    @SelectProvider(type = SqlProvider.class, method = "selectPaMerByParam")
    @ResultType(PaMerInfo.class)
    List<PaMerInfo> selectPaMerByParam(@Param("page") Page<PaMerInfo> page, @Param("info") PaMerInfo info);

    @Select("SELECT real_name FROM pa_user_info where user_code = #{userCode}")
    @ResultType(Map.class)
    Map<String, Object> selectRealNameByUserCode(@Param("userCode") String userCode);

    @SelectProvider(type = SqlProvider.class, method = "countMer")
    @ResultType(Long.class)
    long countMer(@Param("info") PaMerInfo info);
    
    @SelectProvider(type = SqlProvider.class, method = "selectPaMerByParam")
    @ResultType(PaMerInfo.class)
    List<PaMerInfo> exportMerInfo(@Param("info")PaMerInfo info);


    public class SqlProvider{

        public String selectPaMerByParam(Map<String, Object> param){
            final PaMerInfo info = (PaMerInfo) param.get("info");
            SQL sql = new SQL(){
                {
                	 SELECT("pmi.merchant_no,pmi.mobile_phone,pmi.user_code,pui.mobile,pui.real_name,pmi.create_time,"
                     		+ "pmi.act_time,pui.nick_name,pmi.status,pmi.bind_ter,pmi.is_act,pmi.is_mer_user"
                    );
                    FROM("pa_mer_info pmi");
                    LEFT_OUTER_JOIN("pa_user_info pui ON pui.user_code=pmi.user_code");
                }
            };
            sqlWhere(sql, param);
            sql.ORDER_BY("create_time DESC");
            return sql.toString();
        }



        public String countMer(Map<String, Object> param){
            SQL sql = new SQL(){
                {
                    SELECT("COUNT(pmi.id)");
                    FROM("pa_mer_info pmi");
                    LEFT_OUTER_JOIN("pa_user_info pui ON pui.user_code=pmi.user_code");
                }
            };
            sqlWhere(sql, param);
            return sql.toString();
        }




        public void sqlWhere(SQL sql, Map<String, Object> param){
            final PaMerInfo info = (PaMerInfo) param.get("info");
            String userType=info.getLoginUserType();
            if("1".equals(userType)){
                sql.WHERE("pmi.user_node LIKE CONCAT(#{info.loginUserNode},'%')");
            }else if("2".equals(userType)){
                sql.WHERE("(pmi.user_code=#{info.loginUserCode} or pui.parent_id=#{info.loginUserCode})");
            }

            if(StringUtils.isNotBlank(info.getRealName())){
                sql.WHERE("pui.real_name LIKE CONCAT('%',#{info.realName},'%')");
            }
            if(StringUtils.isNotBlank(info.getMobile())){
                sql.WHERE("pui.mobile=#{info.mobile}");
            }
            if(StringUtils.isNotBlank(info.getUserCode())){
                sql.WHERE("pui.user_code=#{info.userCode}");
            }
            if(StringUtils.isNotBlank(info.getMerchantNo())){
                sql.WHERE("pmi.merchant_no=#{info.merchantNo}");
            }
            if(StringUtils.isNotBlank(info.getNickName())){
            	sql.WHERE("pui.nick_name like concat('%',#{info.nickName},'%')");
            }
            if(StringUtils.isNotBlank(info.getStartCreateTime())){
            	sql.WHERE("pmi.create_time >= #{info.startCreateTime}");
            }
            if(StringUtils.isNotBlank(info.getEndCreateTime())){
            	sql.WHERE("pmi.create_time <= #{info.endCreateTime}");
            }
            if(StringUtils.isNotBlank(info.getStartActTime())){
            	sql.WHERE("pmi.act_time >= #{info.startActTime}");
            }
            if(StringUtils.isNotBlank(info.getEndActTime())){
            	sql.WHERE("pmi.act_time <= #{info.endActTime}");
            }
            String merType = info.getMerType();
            if(StringUtils.isNotBlank(merType)){
            	/**
            	 * {text:"已注册未认证商户",value:'0'},{text:"已认证未绑定机具商户",value:'1'},{text:"已绑机具未激活商户",value:'2'},
                          {text:"已激活商户",value:'3'},{text:"由商户成为盟主的商户",value:'4'}
            	 */
            	switch (merType) {
				case "0":
					sql.WHERE("pmi.status = '0'");//未注册,没数据,所以已注册不用标识,直接条件未认证就行
					break;
				case "1":
					sql.WHERE("(pmi.status = '1' and pmi.bind_ter = '0')");
					break;
				case "2":
					sql.WHERE("(pmi.bind_ter = '1' and pmi.is_act = '0')");
					break;
				case "3":
					sql.WHERE("pmi.is_act = '1'");
					break;
				case "4":
					sql.WHERE("pmi.is_mer_user = '1'");
					break;
				default:
					break;
				}
            }
        }
    }
}
