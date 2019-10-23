package cn.eeepay.framework.dao.bill;
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
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AdjustAccount;
import cn.eeepay.framework.model.bill.ExtUserTypeSubject;
import cn.eeepay.framework.model.bill.Subject;

/**
 * 科目管理表相关
 * @author Administrator
 *
 */
public interface SubjectMapper {
/*	@Select("select * from  bill_subject")
	@Results(
			@Result(column="",javaType=String.class,property="") )
	public List<Subject> getSubject();
	*/
	/**
	 * 新增科目信息
	 * @param subject
	 * @return
	 */
	@Insert("insert into bill_subject(subject_no,subject_name,subject_level,parent_Subject_no,subject_type,balance_from,add_balance_from,"
			+ "debit_credit_flag,is_inner_account,inner_day_bal_flag,inner_sum_flag,creator,create_time,subject_alias)"
			+"values(#{subject.subjectNo},#{subject.subjectName},#{subject.subjectLevel},#{subject.parentSubjectNo},#{subject.subjectType},"
			+ "#{subject.balanceFrom},#{subject.addBalanceFrom},#{subject.debitCreditFlag},#{subject.isInnerAccount},#{subject.innerDayBalFlag},"
			+ "#{subject.innerSumFlag},#{subject.creator},now(),#{subject.subjectAlias})"
			)
	int insertSubject(@Param("subject")Subject subject);
	
	@Select("select * from bill_subject where subject_no = #{subjectNo}")
	@ResultMap("cn.eeepay.framework.dao.bill.SubjectMapper.BaseResultMap")
	Subject findSubjectNoExist(@Param("subjectNo")String subjectNo);
	

	@Select("select * from bill_subject where subject_alias = #{subjectAlias}")
	@ResultMap("cn.eeepay.framework.dao.bill.SubjectMapper.BaseResultMap")
	Subject findSubjectAliasExist(@Param("subjectAlias")String subjectAlias);
	
	
	@Update("update bill_subject set subject_name=#{subject.subjectName}"
			+", subject_level=#{subject.subjectLevel},parent_Subject_no=#{subject.parentSubjectNo},subject_type=#{subject.subjectType}"
			+", balance_from=#{subject.balanceFrom},add_balance_from=#{subject.addBalanceFrom},debit_credit_flag=#{subject.debitCreditFlag}"
			+", is_inner_account=#{subject.isInnerAccount},inner_day_bal_flag=#{subject.innerDayBalFlag},inner_sum_flag=#{subject.innerSumFlag}"
			+ ",updator=#{subject.updator},update_time=now(),subject_alias=#{subject.subjectAlias}"
			+ " where subject_no=#{subject.subjectNo}")
	int updateSubject(@Param("subject")Subject subject);
			
	/**
	 * 删除科目
	 * @param adjustAccount
	 * @return
	 */
	@Delete("delete from bill_subject where id = #{id}")
	int deleteSubjectById(@Param("id")Integer id);
	
	
	@SelectProvider( type=SqlProvider.class,method="findSubjectListInfo")
	@ResultMap("cn.eeepay.framework.dao.bill.SubjectMapper.OneToOneResultMap")
	List<Subject> findSubjectListInfo(@Param("subject")Subject subject,@Param("sort")Sort sort,Page<Subject> page);
	
	@Select("select max(subject_level) as max_subject_level from bill_subject")
	@ResultMap("cn.eeepay.framework.dao.bill.SubjectMapper.BaseResultMaxSubjectLevel")
	int findMaxSubjectLevel();
	
	
	@SelectProvider( type=SqlProvider.class,method="findSelectSubject")
	@ResultMap("cn.eeepay.framework.dao.bill.SubjectMapper.BaseResultMap")
	List<Subject> findSelectSubject(@Param("subject")Subject subject);
	
	@Select("select * from bill_subject")
	@ResultMap("cn.eeepay.framework.dao.bill.SubjectMapper.BaseResultMap")
	List<Subject> findAllSubjectList();
	
	@Select("select * from bill_subject where subject_level = #{subjectLevel}")
	@ResultMap("cn.eeepay.framework.dao.bill.SubjectMapper.BaseResultMap")
	List<Subject> findSubjectListBySubjectLevel(@Param("subjectLevel")Integer subjectLevel);
	
	@Select("select * from bill_subject where subject_no = #{subjectNo}")
	@ResultMap("cn.eeepay.framework.dao.bill.SubjectMapper.BaseResultMap")
	Subject getSubject(@Param("subjectNo")String subjectNo);
	
	@SelectProvider( type=SqlProvider.class,method="getSubjectAndParentSubject")
	@ResultMap("cn.eeepay.framework.dao.bill.SubjectMapper.OneToOneResultMap")
	Subject getSubjectAndParentSubject(@Param("subjectNo")String subjectNo);
	
	@Select("select * from bill_subject where id = #{id}")
	@ResultMap("cn.eeepay.framework.dao.bill.SubjectMapper.BaseResultMap")
	Subject getSubjectById(@Param("id")Integer id);
	
	
	@Select("select * from bill_subject where parent_subject_no = #{parentSubjectNo}")
	@ResultMap("cn.eeepay.framework.dao.bill.SubjectMapper.BaseResultMap")
	List<Subject> findChildrenSubjectListByParentSubjectNo(@Param("parentSubjectNo")String parentSubjectNo);
	
	@Select("select * from bill_subject where find_in_set(subject_no,get_bill_subject_child_list(#{parentSubjectNo})) ")
	@ResultMap("cn.eeepay.framework.dao.bill.SubjectMapper.BaseResultMap")
	List<Subject> getChildSubjectList(@Param("parentSubjectNo")String parentSubjectNo);
	
	

	
	@SelectProvider(type=SqlProvider.class, method="exsitsSubject")
	@Results(value = {
		@Result(column="subject_no", property="subjectNo", javaType=String.class),
		@Result(column="subject_name", property="subjectName", javaType=String.class),
		//@Result(column="subject_legal_no", property="subjectLegalNo", javaType=String.class),
		@Result(column="subject_level", property="subjectLevel", javaType=Integer.class)
	})
	Subject exsitsSubject(@Param("subjectNo")String subjectNo,@Param("type")String type);
	
	/**
	 * 新增用户类型科目关联
	 * @param subjectExt
	 * @return
	 */
	@Insert("insert into bill_ext_user_type_subject(user_type,subject_no)"
			+"values(#{subjectExt.userType},#{subjectExt.subjectNo})"
			)
	int insertExtUserTypeSubject(@Param("subjectExt")ExtUserTypeSubject subjectExt);
	
	
	
	public class SqlProvider{
		public String getSubjectInfoList(Map<String,Object> map){
			System.out.println(map);
			StringBuffer sbf=new StringBuffer("select * from bill_subject ");
			return sbf.toString();
		}
		public String exsitsSubject(final Map<String,Object> map){
			return new SQL(){{
				SELECT(" subject_no,subject_name,subject_level ");
				FROM(" bill_subject ");
				String type=map.get("type")==null?"inner":map.get("type").toString();
				if(StringUtils.equalsIgnoreCase("legal", type)){
					WHERE(" subject_legal_no=#{subjectNo} ");
				}else{
					WHERE(" subject_no=#{subjectNo} ");
				}
			}}.toString();
		}
		
		public String findSubjectListInfo(final Map<String, Object> parameter) {
			final Subject subject = (Subject) parameter.get("subject");
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT(" s.id,s.subject_no,s.subject_name,s.subject_level,s.parent_subject_no,s.creator, "+
						" s.subject_type,s.balance_from,s.add_balance_from,s.debit_credit_flag,s.is_inner_account, "+
						" s.inner_day_bal_flag,s.inner_sum_flag,s.subject_alias, "+
						" ps.subject_no as p_subject_no,ps.subject_name as p_subject_name,ps.subject_level as p_subject_level,ps.parent_subject_no as p_parent_subject_no, "+
						" ps.subject_type as p_subject_type,ps.balance_from as p_balance_from,ps.add_balance_from as p_add_balance_from,ps.debit_credit_flag as p_debit_credit_flag,ps.is_inner_account as p_is_inner_account, "+
						" ps.inner_day_bal_flag as p_inner_day_bal_flag,ps.inner_sum_flag as p_inner_sum_flag,ps.subject_alias as p_subject_alias ");
				FROM("bill_subject as s left join bill_subject ps on s.parent_subject_no = ps.subject_no");
				if (!StringUtils.isBlank(subject.getSubjectNo()))
					WHERE(" s.subject_no like  \"%\"#{subject.subjectNo}\"%\" ");
				if (subject.getSubjectLevel() != null && !subject.getSubjectLevel().equals(9999))
					WHERE(" s.subject_level = #{subject.subjectLevel} ");
				if (!StringUtils.isBlank(subject.getSubjectName()))
					WHERE(" s.subject_name like  \"%\"#{subject.subjectName}\"%\" ");
				if (!StringUtils.isBlank(subject.getParentSubjectNo()) && !subject.getParentSubjectNo().equals("ALL") )
					WHERE(" s.parent_subject_no =  #{subject.parentSubjectNo} ");
				if (!StringUtils.isBlank(subject.getSubjectType()) && !subject.getSubjectType().equals("ALL"))
					WHERE(" s.subject_type = #{subject.subjectType} ");
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}
			}}.toString();
		}
		
		public String getSubjectAndParentSubject(final Map<String, Object> parameter) {
			final String subjectNo = (String) parameter.get("subjectNo");
			return new SQL(){{
				SELECT("   	a.subject_no, "+
						" 	a.subject_name, "+
						" 	a.subject_level, "+
						" 	a.subject_type, "+
						" 	a.parent_subject_no, "+
						" 	a.balance_from, "+
						" 	a.add_balance_from, "+
						" 	a.debit_credit_flag, "+
						" 	a.is_inner_account, "+
						" 	a.inner_sum_flag, "+
						" 	a.inner_day_bal_flag, "+
						" 	a.creator, "+
						" 	a.create_time, "+
						" 	a.updator, "+
						" 	a.update_time,a.subject_alias, "+
						" 	b.subject_no as p_subject_no, "+
						" 	b.subject_name as p_subject_name, "+
						" 	b.subject_level as p_subject_level, "+
						" 	b.subject_type as p_subject_type, "+
						" 	b.parent_subject_no as p_parent_subject_no, "+
						" 	b.balance_from as p_balance_from, "+
						" 	b.add_balance_from as p_add_balance_from, "+
						" 	b.debit_credit_flag as p_debit_credit_flag, "+
						" 	b.is_inner_account as p_is_inner_account, "+
						" 	b.inner_sum_flag as p_inner_sum_flag, "+
						" 	b.inner_day_bal_flag as p_inner_day_bal_flag, "+
						" 	b.creator as p_creator, "+
						" 	b.create_time as p_create_time, "+
						" 	b.updator as p_updator, "+
						" 	b.update_time as p_update_time,b.subject_alias ");
				FROM(" bill_subject AS a left join bill_subject AS b on a.parent_subject_no = b.subject_no");
				WHERE(" a.subject_no = #{subjectNo} ");
			}}.toString();
		}
		
		public String findSelectSubject(final Map<String, Object> parameter) {
			final Subject subject = (Subject) parameter.get("subject");
			return new SQL(){{
				SELECT("subject_no,"
						+ "subject_name ");
				FROM("bill_subject");
				if (!StringUtils.isBlank(subject.getSubjectNo()))
					WHERE(" subject_no like  \"%\"#{subject.subjectNo}\"%\" or subject_name like  \"%\"#{subject.subjectNo}\"%\" ");
			}}.toString();
		}
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"subjectNo","subjectName","subjectAlias","subjectLevel","parentSubjectNo","parentSubjectName","subjectType","balanceFrom"};
		    final String[] columns={"subject_no","subject_name","subject_alias","subject_level","parent_subject_no","subject_name","subject_type","balance_from"};
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
		

//		public String findUserTypeSubjectList(final Map<String, Object> parameter) {
//			final ExtUserTypeSubject subjectExt = (ExtUserTypeSubject) parameter.get("subjectExt");
//			final Sort sord=(Sort)parameter.get("sort");
//			return new SQL(){{
//				SELECT("uts.id,uts.subject_no,uts.user_type,bs.subject_name,bs.subject_alias ");
//				FROM(" bill_ext_user_type_subject uts, bill_subject bs ");
//				WHERE(" uts.subject_no = bs.subject_no ") ;
//				if (!StringUtils.isBlank(subjectExt.getUserType()) && !"ALL".equals(subjectExt.getUserType()))
//					WHERE(" uts.user_type = #{subjectExt.userType} ");
//				if (!StringUtils.isBlank(subjectExt.getSubjectNo()))
//					WHERE(" uts.subject_no = #{subjectExt.subjectNo} ");
//				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
//					ORDER_BY(userTypePropertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
//				}else{
//					ORDER_BY(" id desc") ;
//				}
//			}}.toString();
//		}
//		
//		public String userTypePropertyMapping(String name,int type){
//			final String[] propertys={"id","userType","subjectNo","subjectName","subjectAlias"};
//		    final String[] columns={"id","user_type","subject_no","subject_name","subject_alias"};
//		    if(StringUtils.isNotBlank(name)){
//		    	if(type==0){//属性查出字段名
//		    		for(int i=0;i<propertys.length;i++){
//		    			if(name.equalsIgnoreCase(propertys[i])){
//		    				return columns[i];
//		    			}
//		    		}
//		    	}else if(type==1){//字段名查出属性
//		    		for(int i=0;i<propertys.length;i++){
//		    			if(name.equalsIgnoreCase(columns[i])){
//		    				return propertys[i];
//		    			}
//		    		}
//		    	}
//		    }
//			return null;
//		}
		
//		public String findExtUserTypeSubjectByUserType(final Map<String, Object> parameter) {
//			final String userType = (String) parameter.get("userType");
//			return new SQL(){{
//				SELECT(" 	euts.id, "+
//						" 	euts.user_type, "+
//						" 	euts.subject_no, "+
//						" 	s.subject_no, "+
//						" 	s.subject_name, "+
//						" 	s.subject_level, "+
//						" 	s.parent_subject_no, "+
//						" 	s.subject_type, "+
//						" 	s.balance_from, "+
//						" 	s.add_balance_from, "+
//						" 	s.debit_credit_flag, "+
//						" 	s.is_inner_account, "+
//						" 	s.inner_day_bal_flag, "+
//						" 	s.inner_sum_flag, "+
//						" 	s.creator, "+
//						" 	s.create_time, "+
//						" 	s.updator, "+
//						" 	s.update_time, "+
//						" 	c.sys_key, "+
//						" 	c.sys_name, "+
//						" 	c.sys_value ");
//				FROM(" bill_ext_user_type_subject AS euts,bill_subject AS s,sys_dict AS c ");
//				WHERE(" euts.subject_no = s.subject_no AND euts.user_type = c.sys_value AND c.sys_key = 'sys_account_type' ");
//				WHERE(" user_type=#{userType} ");
//			}}.toString();
//		}
		
	}
	
	

//	/**
//	 * 查询用户类型科目关联是否存在
//	 * @param userTypeId
//	 * @param subjectNo
//	 * @return
//	 */
//	@Select("select * from bill_ext_user_type_subject where user_type = #{userTypeValue} and subject_no = #{subjectNo}")
//	@Results(value = {
//		@Result(column="user_type", property="userType", javaType=String.class),
//		@Result(column="subject_no", property="subjectNo", javaType=String.class)
//	})
//	ExtUserTypeSubject existExtUserTypeSubject(@Param("userTypeValue")String userTypeValue,@Param("subjectNo")String subjectNo);
	
	
//	/**
//	 * 查询用户类型科目关联详细列表
//	 * @param page 
//	 * @param sort 
//	 * @param subjectExt
//	 * @return
//	 */
//	@SelectProvider( type=SqlProvider.class,method="findUserTypeSubjectList")
//	@ResultMap("cn.eeepay.framework.dao.bill.ExtUserTypeSubjectMapper.OneToOneResultMap")
//	List<ExtUserTypeSubject> findUserTypeSubjectList(@Param("subjectExt")ExtUserTypeSubject subjectExt, @Param("sort")Sort sort, Page<ExtUserTypeSubject> page);
//
//	@Delete("delete from bill_ext_user_type_subject where id = #{id}")
//	int deleteUserTypeSubject(@Param("id")String id);
//	
//	@Select("select * from bill_ext_user_type_subject where user_type = #{userType}")
//	@ResultMap("cn.eeepay.framework.dao.bill.ExtUserTypeSubjectMapper.BaseResultMap")
//	List<ExtUserTypeSubject> findExtUserTypeSubjectByUserType(@Param("userType")String userType);
	
	
}
