package cn.eeepay.framework.daoSuperBank;

import cn.eeepay.framework.model.InsuranceCompany;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 保险公司表dao
 * @author panfuhao
 * @date 2018/7/23
 */
public interface InsuranceCompanyDao {

    @Select("select company_no, company_nick_name from insurance_company order by convert(company_nick_name using gbk)")
    @ResultType(InsuranceCompany.class)
    List<InsuranceCompany> getCompanyNickNameList();
}
