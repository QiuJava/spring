package cn.eeepay.framework.dao.bill;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.SysDict;
/**
 * 数据字典
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public interface SysDictMapper {
	@Insert("insert into sys_dict(sys_key,sys_name,html_name,sys_value,order_no,status,remark)"
			+ " values(#{sysDict.sysKey},#{sysDict.sysName},#{sysDict.htmlName},#{sysDict.sysValue},#{sysDict.orderNo},#{sysDict.status},#{sysDict.remark})")
	@ResultMap("cn.eeepay.framework.dao.bill.SysDictMapper.BaseResultMap")
	int insertSysDict(@Param("sysDict")SysDict sysDict);
	
	
	@Update("update sys_dict set sys_key=#{sysDict.sysKey},sys_name=#{sysDict.sysName},html_name=#{sysDict.htmlName},sys_value=#{sysDict.sysValue},order_no=#{sysDict.orderNo},status=#{sysDict.status},remark=#{sysDict.remark}"
			+"   where id=#{sysDict.id}")
	int updateSysDict(@Param("sysDict")SysDict sysDict);
	
	@Delete("delete from sys_dict where id = #{id}")
	int deleteSysDict(@Param("id")Integer id);
	
	@Delete("delete from sys_dict where sysKey = #{sysKey} and sysValue = #{sysValue}")
	int deleteSysDictByParams(@Param("sysKey")String sysKey,@Param("sysValue")String sysValue);
	
	
	@Select("select id,sys_key,sys_name,html_name,sys_value,order_no,status,remark from sys_dict where sys_key = #{sysKey} and sys_value = #{sysValue} ")
	@ResultMap("cn.eeepay.framework.dao.bill.SysDictMapper.BaseResultMap")
	SysDict findSysDict(@Param("sysKey")String sysKey,@Param("sysValue")String sysValue);
	
	
	@Select("select id,sys_key,sys_name,html_name,sys_value,order_no,status,remark from sys_dict where sys_key = #{sysKey} and status=1 ORDER BY sys_value ")
	@ResultMap("cn.eeepay.framework.dao.bill.SysDictMapper.BaseResultMap")
	List<SysDict> findSysDictGroup(@Param("sysKey")String sysKey);
	
	@Select("select id,sys_key,sys_name,html_name,sys_value,order_no,status,remark from sys_dict ")
	@ResultMap("cn.eeepay.framework.dao.bill.SysDictMapper.BaseResultMap")
	List<SysDict> findAllSysDict();
	
	@SelectProvider(type=SqlProvider.class,method="findSysDictList")
	@ResultMap("cn.eeepay.framework.dao.bill.SysDictMapper.BaseResultMap")
	List<SysDict> findSysDictList(@Param("sysDict")SysDict sysDict,@Param("sort")Sort sort,Page<SysDict> page);
	
	@Select("select * from sys_dict where sys_key = #{sysDict.sysKey} and sys_value = #{sysDict.sysValue} and sys_name = #{sysDict.sysName} ")
	@ResultMap("cn.eeepay.framework.dao.bill.SysDictMapper.BaseResultMap")
	SysDict findSysDictExist(@Param("sysDict")SysDict sysDict) ;
	
	@Select("select * from sys_dict where id = #{id} ")
	@ResultMap("cn.eeepay.framework.dao.bill.SysDictMapper.BaseResultMap")
	SysDict findSysDictById(@Param("id")Integer id) ;
	
	
	@Select("select id,sys_key,sys_name,html_name,sys_value,order_no,status,remark from sys_dict where sys_key = #{sysKey} and status=1 ORDER BY sys_value ")
	@ResultMap("cn.eeepay.framework.dao.bill.SysDictMapper.BaseResultMap")
	SysDict findSysDuiAccountGroup(@Param("sysKey")String sysKey);
	
	public class SqlProvider{
		public String findSysDictList(Map<String,Object> param){
			final SysDict sysDict=(SysDict)param.get("sysDict");
			final Sort sord=(Sort)param.get("sort");
			return new SQL(){{
				SELECT("id,sys_key,sys_name,html_name,sys_value,status,order_no");
				FROM("sys_dict");
				if(StringUtils.isNotBlank(sysDict.getSysKey())){
					WHERE("sys_key like \"%\" #{sysDict.sysKey} \"%\" ");
				}
				if(StringUtils.isNotBlank(sysDict.getSysName())){
					WHERE("sys_name like \"%\" #{sysDict.sysName} \"%\" ");
				}
				if(StringUtils.isNotBlank(sysDict.getStatus()) && !"ALL".equals(sysDict.getStatus())){
					WHERE("status like \"%\" #{sysDict.status} \"%\" ");
				}
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				} else {
					ORDER_BY("sys_key,order_no ");
				}
			}}.toString();
		}
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","sysKey","sysName","htmlName","sysValue","status","orderNo"};
		    final String[] columns={"id","sys_key","sys_name","html_name","sys_value","status","order_no"};
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
