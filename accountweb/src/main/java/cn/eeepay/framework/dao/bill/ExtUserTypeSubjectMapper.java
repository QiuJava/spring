package cn.eeepay.framework.dao.bill;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.ExtUserTypeSubject;

/**
 * 用户类型科目管理关联表
 * @author Administrator
 *
 */
public interface ExtUserTypeSubjectMapper {
	/**
	 * 新增用户类型科目关联
	 * @param subjectExt
	 * @return
	 */
	@Insert("insert into bill_ext_user_type_subject(user_type,subject_no,creator,create_time,updator,update_time)"
			+"values(#{subjectExt.userType},#{subjectExt.subjectNo},#{subjectExt.creator},#{subjectExt.createTime},#{subjectExt.updator},#{subjectExt.updateTime})"
			)
	int insertExtUserTypeSubject(@Param("subjectExt")ExtUserTypeSubject subjectExt);
	/**
	 * 查询用户类型科目关联是否存在
	 * @param userTypeId
	 * @param subjectNo
	 * @return
	 */
	@Select("select * from bill_ext_user_type_subject where user_type = #{userTypeValue} and subject_no = #{subjectNo}")
	@Results(value = {
		@Result(column="user_type", property="userType", javaType=String.class),
		@Result(column="subject_no", property="subjectNo", javaType=String.class),
		@Result(column="creator", property="creator", javaType=String.class),
		@Result(column="create_time", property="createTime", javaType=Timestamp.class),
		@Result(column="updator", property="updator", javaType=String.class),
		@Result(column="update_time", property="updateTime", javaType=Timestamp.class)
	})
	ExtUserTypeSubject existExtUserTypeSubject(@Param("userTypeValue")String userTypeValue,@Param("subjectNo")String subjectNo);
	/**
	 * 查询用户类型科目关联详细列表
	 * @param page 
	 * @param sort 
	 * @param subjectExt
	 * @return
	 */
	@SelectProvider( type=SqlProvider.class,method="findUserTypeSubjectList")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtUserTypeSubjectMapper.OneToOneResultMap")
	List<ExtUserTypeSubject> findUserTypeSubjectList(@Param("subjectExt")ExtUserTypeSubject subjectExt, @Param("sort")Sort sort, Page<ExtUserTypeSubject> page);

	@Delete("delete from bill_ext_user_type_subject where id = #{id}")
	int deleteUserTypeSubject(@Param("id")String id);
	
	@Select("select * from bill_ext_user_type_subject where user_type = #{userType}")
	@ResultMap("cn.eeepay.framework.dao.bill.ExtUserTypeSubjectMapper.BaseResultMap")
	List<ExtUserTypeSubject> findExtUserTypeSubjectByUserType(@Param("userType")String userType);
	
	public class SqlProvider{
		public String findUserTypeSubjectList(final Map<String, Object> parameter) {
			final ExtUserTypeSubject subjectExt = (ExtUserTypeSubject) parameter.get("subjectExt");
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT("uts.id,uts.subject_no,uts.user_type,bs.subject_name,bs.subject_alias,uts.creator ");
				FROM(" bill_ext_user_type_subject uts, bill_subject bs ");
				WHERE(" uts.subject_no = bs.subject_no ") ;
				if (!StringUtils.isBlank(subjectExt.getUserType()) && !"ALL".equals(subjectExt.getUserType()))
					WHERE(" uts.user_type = #{subjectExt.userType} ");
				if (!StringUtils.isBlank(subjectExt.getSubjectNo()))
					WHERE(" uts.subject_no = #{subjectExt.subjectNo} ");
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}else{
					ORDER_BY(" id desc") ;
				}
			}}.toString();
		}
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","userType","subjectNo","subjectName","subjectAlias","creator"};
		    final String[] columns={"id","user_type","subject_no","subject_name","subject_alias","creator"};
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
