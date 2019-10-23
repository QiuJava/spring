package cn.eeepay.framework.daoSuperBank;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.LoanBonusConf;
import cn.eeepay.framework.model.LoanSource;


public interface LoanCompanyDao {

	/**查询机构列表*/
	@Select("select id,company_name from loan_source")
    @ResultType(LoanSource.class)
	List<LoanSource> getLoanCompanies();
	
	
	/**查询贷款奖励配置*/
	@SelectProvider(type = SqlProvider.class, method = "selectLoanConfByPage")
	@ResultType(LoanBonusConf.class)
	List<LoanBonusConf> getLoanBonusConf(@Param("loanConf") LoanBonusConf loanConf,@Param("pager") Page<LoanBonusConf> pager);
	
	 public class SqlProvider{
	     public String selectLoanConfByPage(Map<String, Object> param){
	           StringBuffer sql = new StringBuffer("");
	           final LoanBonusConf loanParam = (LoanBonusConf) param.get("loanConf");
	           sql.append(("select lf.id,oi.org_id,oi.org_name,ls.company_name,"
	           		+ "lf.org_push_loan,"
//	           		+ "ls.loan_bonus,ls.register_bonus,lf.org_cost_reg,"
	           		+ "(ls.loan_bonus-lf.org_cost_loan) loanBonus,(ls.register_bonus-lf.org_cost_reg) registerBonus,"
	           		+ "lf.org_push_reg,lf.source_id"));
	           sql.append((" from loan_bonus_conf lf,org_info oi,loan_source ls "));
	           sql.append((" where lf.org_id=oi.org_id and lf.source_id=ls.id "));
	          
	           if(loanParam != null && loanParam.getSourceId() != null && loanParam.getSourceId() != -1L){//贷款机构ID
	              sql.append(" and lf.source_id=#{loanConf.sourceId}");
	           }
	           sql.append(" and lf.org_id = #{loanConf.entityId} ");
//	           if(loanParam != null && loanParam.getOrgId() != null && loanParam.getOrgId() != -1L){//组织id
//		              sql.append(" and lf.org_id=#{loanConf.orgId}");
//		       }
	           sql.append(" order by lf.id desc ");
	           return sql.toString();
	     }
    }

	 /**修改奖励配置*/
	@Update("update loan_bonus_conf set org_cost_loan=#{orgCostLoan},org_cost_reg=#{orgCostReg},org_push_loan=#{orgPushLoan},org_push_reg=#{orgPushReg},update_by=#{updateBy},update_date=sysdate() where id=#{id}")
	int updLoanConf(LoanBonusConf loanConf);
	
	/**修改贷款机构表*/
	@Update("update loan_source set loan_bonus=#{loanBonus},register_bonus=#{registerBonus} where id=#{sourceId}")
	int updLoanCompany(LoanBonusConf loanCompany);
	
	/**新增贷款机构*/
	@Insert("insert into loan_source (id,company_name,status,loan_bonus,register_bonus) values(#{id},#{companyName},'on',#{loanBonus},#{registerBonus})")
	int saveLoanCompany(@Param("id") long id,@Param("companyName") String companyName,@Param("loanBonus") String loanBonus,@Param("registerBonus") String registerBonus);
	
	@Select("select @@identity")
	long getInsertId(); 
	
	/**新增奖金配置*/
	@Insert("insert into loan_bonus_conf " +
			"(org_id,source_id,org_cost_loan,org_cost_reg,org_push_loan,org_push_reg,update_by,update_date)" +
			" values" +
			"(#{orgId},#{sourceId},#{orgCostLoan},#{orgCostReg},#{orgPushLoan},#{orgPushReg},#{updateBy},sysdate())")
	int saveLoanConf(LoanBonusConf loanConf);
	
	@Select("select key_value from sys_id where key_id='loan_manage'")
	long getSysId();
	@Update("update sys_id set key_value=#{newId} where key_id='loan_manage' and key_value=#{oldId}")
	int updSysId(@Param("newId") long newId,@Param("oldId") long oldId);
	
	@Select("select count(id) from loan_bonus_conf where source_id=#{sourceId}")
	int isExist(@Param("sourceId") long sourceId);
	
	@Select("select lbc.* from loan_bonus_conf lbc left join org_info oi on oi.org_id = lbc.org_id where oi.v2_orgcode = #{v2Code}")
	List<LoanBonusConf> getDirectSalesConf(@Param("v2Code") String v2Code);
	
}
