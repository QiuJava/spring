package cn.eeepay.framework.dao.bill;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.BusinessAccount;

public interface BusinessAccountMapper {
	@SelectProvider(type=SqlProvider.class,method="findBusinessAccount")
	@ResultMap("cn.eeepay.framework.dao.bill.BusinessAccountMapper.BaseResultMap")
	List<BusinessAccount> findBusinessAccount(@Param("account")BusinessAccount account,@Param("params")Map<String, String> params, @Param("sort")Sort sort, Page<BusinessAccount> page);
	
	/**
	 * 调账表(这里还没有提交，所以提交时间这个时候补处理)
	 * @param adjustAccount
	 * @return
	 */
	@Insert("insert into business_account(applicant,applicant_time,approver,approve_time,status,remark,file_path)"
			+ " values(#{adjustAccount.applicant},#{adjustAccount.applicantTime},#{adjustAccount.approver},#{adjustAccount.approveTime},#{adjustAccount.status},"
			+ "#{adjustAccount.remark},#{adjustAccount.filePath})")			
	@SelectKey(statement=" SELECT LAST_INSERT_ID() AS id", keyProperty="adjustAccount.id", before=false, resultType=int.class)  
	int insertBusinessAccount(@Param("adjustAccount")BusinessAccount adjustAccount);
	
	@Select("select * from business_account where id = #{id}")
	@ResultMap("cn.eeepay.framework.dao.bill.BusinessAccountMapper.BaseResultMap")
	BusinessAccount getBusinessAccount(@Param("id")Integer id);
	
	@Update("update business_account set approver = #{adjustAccount.approver},remark= #{adjustAccount.remark},file_path= #{adjustAccount.filePath} where id = #{adjustAccount.id}")
	int updateBusinessAccount(@Param("adjustAccount")BusinessAccount adjustAccount);
	
	/**
	 * (只更新调账状态为  2  待审批,及提交时间 为 当前时间)
	 * @param adjustAccount
	 * @return
	 */
	@Update("update business_account set status= #{adjustAccount.status},applicant_time = now() where id = #{adjustAccount.id}")
	int updateBusinessExamineDate(@Param("adjustAccount")BusinessAccount adjustAccount);
	
	public class SqlProvider {
		public String findBusinessAccount(final Map<String, Object> parameter) {
			final BusinessAccount account = (BusinessAccount) parameter.get("account");
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final Sort sord=(Sort)parameter.get("sort");
			
			final String beginDate = params.get("beginDate");
			final String endDate = params.get("endDate");
			final String id = params.get("id");
			
			return new SQL(){{
				SELECT("*");
				FROM("business_account");
				if (StringUtils.isNotBlank(id))
					WHERE(" id like \"%\"#{params.id}\"%\" ");
				if (!StringUtils.isBlank(account.getApplicant()))
					WHERE(" applicant like \"%\"#{account.applicant}\"%\" ");
				if (beginDate != null && StringUtils.isNotBlank(beginDate))
					WHERE(" applicant_time >= #{params.beginDate} ");
				if (endDate != null && StringUtils.isNotBlank(endDate))
					WHERE(" applicant_time <= #{params.endDate} ");
				if(StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				}else{
					ORDER_BY(" applicant_time desc") ;
				}
			}}.toString();
		}
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","status","applicant","applicantTime","approver","approveTime"};
		    final String[] columns={"id","status","applicant","applicant_time","approver","approve_time"};
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
