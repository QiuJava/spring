package cn.eeepay.framework.dao.bill;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.Batches;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

public interface BatchesMapper {
	@SelectProvider( type=SqlProvider.class,method="findBatchesList")
	@ResultType(Batches.class)
	List<Batches> findBatchesList(@Param("start")String start,@Param("end")String end, @Param("sort")Sort sort, Page<Batches> page);
	
	
	public class SqlProvider {
		public String findBatchesList(Map<String, Object> parameter) {
			final String start = (String) parameter.get("start");
			final String end = (String) parameter.get("end");
			final Sort sort = (Sort) parameter.get("sort");
			return new SQL(){{
				SELECT("*");
				FROM("batches");
				if (StringUtils.isNotBlank(start)) {
					WHERE(" create_time>=#{start} ");
				}
				if (StringUtils.isNotBlank(end)) {
					WHERE(" create_time<=#{end} ");
				}
				if(sort != null && StringUtils.isNotBlank(sort.getSidx())){
					ORDER_BY(propertyMapping(sort.getSidx(), 0)+" "+sort.getSord());
				} else {
					ORDER_BY("create_time desc");
				}
			}}.toString();
		}
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","status","createTime"};
		    final String[] columns={"id","status","create_time"};
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
