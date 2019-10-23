package cn.eeepay.framework.daoSuperBank;

import java.util.List;

import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.LoanSource;

/**
 *  superbank.loan_source
 * 贷款机构表
 * @author tans
 * @date 2017-12-5
 */
public interface LoanSourceDao {

    @Select("select id, company_name from loan_source order by convert(company_name using gbk)")
    @ResultType(LoanSource.class)
    List<LoanSource> getLoanList();
}
