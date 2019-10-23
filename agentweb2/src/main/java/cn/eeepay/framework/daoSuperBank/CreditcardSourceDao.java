package cn.eeepay.framework.daoSuperBank;

import cn.eeepay.framework.model.CreditcardSource;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CreditcardSourceDao {

    @Select("select id,bank_name,show_order,bank_bonus from creditcard_source order by convert(bank_name using gbk)")
    @ResultType(CreditcardSource.class)
    List<CreditcardSource> allBanksList();
}
