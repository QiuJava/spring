package cn.eeepay.framework.dao;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.commons.lang3.StringUtils;
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
import cn.eeepay.framework.model.SysDict;
/**
 * 数据字典
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
@WriteReadDataSource
public interface SysDictDao {
	
	@Select("SELECT * from sys_dict where sys_key = #{sysKey} and status = '1'")
	@ResultType(SysDict.class)
	SysDict selectDictByKey(@Param("sysKey") String sysKey);
	
	@Select("select sd.id,sd.sys_value,sd.sys_name from sys_dict sd "
			+ " where sd.status=1 and sd.parent_id=#{key} and sd.sys_key=#{key}")
	@ResultType(SysDict.class)
	List<SysDict> selectListByKey(@Param("key")String key);
	
	//查询数据字典tgh502
	@Select("SELECT * FROM sys_dict WHERE sys_key = #{sysKey}")
	@ResultType(List.class)
	List<Map<String, Object>> selectFromSysDict(@Param("sysKey")String sysKey);
	
	@Select("SELECT sys_value FROM sys_dict WHERE sys_key = #{sysKey}")
	@ResultType(String.class)
	String SelectServiceId(@Param("sysKey")String sysKey);
	
	@Insert("insert into sys_dict(sys_key,sys_name,sys_value,order_no,status,remark)"
			+ " values(#{sysDict.sysKey},#{sysDict.sysName},#{sysDict.sysValue},#{sysDict.orderNo},#{sysDict.status},#{sysDict.remark})")
	@ResultMap("cn.eeepay.framework.dao.SysDictMapper.BaseResultMap")
	int insertSysDict(@Param("sysDict")SysDict sysDict);
	
	@Update("update sys_dict set sys_key=#{info.sysKey},sys_name=#{info.sysName},sys_value=#{info.sysValue},"
			+ "parent_id=#{info.parentId},order_no=#{info.orderNo},status=#{info.status},remark=#{info.remark} "
			+ "where id=#{info.id}")
	int update(@Param("info")SysDict info);
	
	@Select("select * from sys_dict where status=1 and sys_key='INITIAL_PWD'")
	@ResultType(SysDict.class)
	SysDict selectRestPwd();
//	
//	
//	@Update("update sys_dict set sys_key=#{sysDict.sysKey},sys_name=#{sysDict.sysName},sys_value=#{sysDict.sysValue},order_no=#{sysDict.orderNo},status=#{sysDict.status},remark=#{sysDict.remark}"
//			+"  where id=#{sysDict.id}")
//	int updateSysDict(@Param("sysDict")SysDict sysDict);
//	
//	@Delete("delete from sys_dict where id = #{id}")
//	int deleteSysDict(@Param("id")Integer id);
//	
//	@Delete("delete from sys_dict where sysKey = #{sysKey} and sysValue = #{sysValue}")
//	int deleteSysDictByParams(@Param("sysKey")String sysKey,@Param("sysValue")String sysValue);
//	
//	
	@Select("select id,sys_key,sys_name,sys_value,order_no,status,remark from sys_dict where sys_key = #{sysKey} and sys_value = #{sysValue} ")
	@ResultMap("cn.eeepay.framework.dao.SysDictMapper.BaseResultMap")
	SysDict findSysDict(@Param("sysKey")String sysKey,@Param("sysValue")String sysValue);
	
	
	@Select("select id,sys_key,sys_name,sys_value,order_no,status,remark from sys_dict where sys_key = #{sysKey}")
	@ResultMap("cn.eeepay.framework.dao.SysDictMapper.BaseResultMap")
	List<SysDict> findSysDictGroup(@Param("sysKey")String sysKey);
	
	
	@Select("select id,sys_key,sys_name,sys_value,order_no,status,remark from sys_dict ")
	@ResultMap("cn.eeepay.framework.dao.SysDictMapper.BaseResultMap")
	List<SysDict> findAllSysDict();
	
	@SelectProvider(type=SqlProvider.class,method="findSysDictList")
	@ResultMap("cn.eeepay.framework.dao.SysDictMapper.BaseResultMap")
	List<SysDict> findSysDictList(@Param("sysDict")SysDict sysDict,@Param("sort")Sort sort,Page<SysDict> page);
	
	@Select("select * from sys_dict where sys_key = #{sysDict.sysKey} and sys_value = #{sysDict.sysValue} and sys_name = #{sysDict.sysName} ")
	@ResultMap("cn.eeepay.framework.dao.SysDictMapper.BaseResultMap")
	SysDict findSysDictExist(@Param("sysDict")SysDict sysDict) ;
	
	@Select("select * from sys_dict where id = #{id} ")
	@ResultMap("cn.eeepay.framework.dao.SysDictMapper.BaseResultMap")
	SysDict findSysDictById(@Param("id")Integer id) ;
	
	
	@Select("SELECT * from sys_dict where sys_key='TRANS_STATUS' and sys_value !='STRING'")
	@ResultType(SysDict.class)
	List<SysDict> selectTranStatusAllDict();
	
	@Select("SELECT * from sys_dict where sys_key='TRANS_TYPE' and sys_value !='STRING'")
	@ResultType(SysDict.class)
	List<SysDict> selectTransTypeAllDict();
	
	@Select("SELECT * from sys_dict where sys_key='SERVICE_TYPE' and sys_value !='INT'")
	@ResultType(SysDict.class)
	List<SysDict> selectServiceTypeAllDict();
	
	@Select("SELECT * from sys_dict where sys_key='PAY_METHOD_TYPE' and sys_value !='INT'")
	@ResultType(SysDict.class)
	List<SysDict> selectPayMethodTypeAllDict();
	
	@Select("SELECT * from sys_dict where sys_key='CARD_TYPE' and sys_value !='INT'")
	@ResultType(SysDict.class)
	List<SysDict> selectCardTypeAllDict();
	
	@Select("SELECT s.*,p.sys_value `type` FROM sys_dict p LEFT JOIN sys_dict s  ON p.sys_key=s.parent_id WHERE s.status=1 and p.parent_id='BOSS_INIT' ORDER BY s.sys_key,s.order_no")
	@ResultType(SysDict.class)
	List<SysDict> selectAllDict();

	@Select("SELECT * from sys_dict where sys_key='AGENT_WEB_SHARE_SWITCH' limit 1")
	@ResultType(SysDict.class)
    SysDict findAgentWebShareSwitch();

	@Select("SELECT * from sys_dict where sys_key='SUPER_PUSH_AGENT_SWITCH' limit 1")
	@ResultType(SysDict.class)
	SysDict findAgentWebPromotionSwitch();

	@Select("SELECT * from sys_dict where sys_key='SUPER_PUSH_CASH_BACK_SWITCH' limit 1")
	@ResultType(SysDict.class)
	SysDict findAgentWebCashBackSwitch();

	@Select("SELECT * from sys_dict where sys_key=#{keyName} and sys_value !='INT'")
	@ResultType(SysDict.class)
	List<SysDict> listSysDictGroup(@Param("keyName") String keyName);

	/**
	 * 获取微创业无卡支付的业务产品id
	 * @return
	 */
	@Select("SELECT sys_value from sys_dict where sys_key='SUPER_PUSH_BP_ID' limit 1")
    String getSuperPushBpId();

	@Select("SELECT sys_value from sys_dict where sys_key='ACCOUNTANT_SHARE_ACCOUNTING' limit 1")
    String getAccountantShareAccounting();

	@Select("SELECT sys_value from sys_dict where sys_key='AGENT_OEM_ID' and sys_name=#{sysName} and status='1'")
	String getAgentOem(@Param("sysName") String sysName);

	@Select("select count(1) from sys_dict where sys_key = 'AGENT_OEM_ID' and sys_name=#{sysName} and sys_value=#{sysValue} and status='1'")
	Long checkExistsAgentOemId(@Param("sysName") String sysName,@Param("sysValue") String sysValue);

	@Select("SELECT sys_value from sys_dict where sys_key=#{sysKey} limit 1")
	SysDict getByKey(@Param("sysKey") String sysKey);

	@SelectProvider(type=SqlProvider.class,method="findActivityTypeBy")
	@ResultType(String.class)
	List<String> findActivityTypeBy(@Param("types") String[] types);

	@Select("select * from sys_dict where sys_key=#{sysKey}")
	@ResultType(SysDict.class)
    List<SysDict> selectByKey(@Param("sysKey") String sysKey);

    public class SqlProvider{
		public String findActivityTypeBy(Map<String,Object> param){
			final String[] types =(String[])param.get("types");
			StringBuilder sbSql = new StringBuilder("SELECT sys_name FROM sys_dict WHERE sys_key='ACTIVITY_TYPE' AND sys_value IN (");
			for (int i = 0; i < types.length; i++) {
				sbSql.append(i==types.length-1 ? "'"+types[i]+"'" : "'"+types[i]+"',");
			}
			sbSql.append(")");
			return sbSql.toString();
		}

		public String findSysDictList(Map<String,Object> param){
			final SysDict sysDict=(SysDict)param.get("sysDict");
			return new SQL(){{
				SELECT("id,sys_key,sys_name,sys_value,status,order_no");
				FROM("sys_dict");
				ORDER_BY("order_no") ;
				if(StringUtils.isNotBlank(sysDict.getSysKey())){
					WHERE("sys_key like \"%\" #{sysDict.sysKey} \"%\" ");
				}
				if(StringUtils.isNotBlank(sysDict.getSysName())){
					WHERE("sys_name like \"%\" #{sysDict.sysName} \"%\" ");
				}
				if(StringUtils.isNotBlank(sysDict.getStatus()) && !"ALL".equals(sysDict.getStatus())){
					WHERE("status like \"%\" #{sysDict.status} \"%\" ");
				}
			}}.toString();
		}
		
		
	}

    @Select("SELECT\r\n" + 
    		"	sys_key\r\n" + 
    		"FROM\r\n" + 
    		"	sys_dict\r\n" + 
    		"WHERE\r\n" + 
    		"	sys_key LIKE \"TRADE_GROUP%\"\r\n" + 
    		"AND sys_name = #{agentType}\r\n" + 
    		"AND sys_value like concat('%',#{agentOem},'%')")
    @ResultType(String.class)
	String findTradeGroupByAgentInfo(@Param("agentType")String agentType, @Param("agentOem")String agentOem);
	
}
