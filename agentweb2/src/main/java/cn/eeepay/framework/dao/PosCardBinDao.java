package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.util.ReadOnlyDataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.PosCardBin;

@ReadOnlyDataSource
public interface PosCardBinDao {

	@Select("select * from pos_card_bin c where c.card_length = length(#{accountNo}) AND c.verify_code = left(#{accountNo},c.verify_length) limit 1")
	@ResultType(PosCardBin.class)
	PosCardBin queryInfo(@Param("accountNo")String accountNo);
	
	@Select("select bank_no from pos_card_bin c  where  c.card_length = length(#{accountNo}) AND c.verify_code = left(#{accountNo},  c.verify_length)")
	@ResultType(PosCardBin.class)
	String queryBankNo(@Param("accountNo")String accountNo);
	
	@Select("select * from pos_card_bin c where c.card_length = length(#{accountNo}) AND c.verify_code = left(#{accountNo},c.verify_length)")
	@ResultType(PosCardBin.class)
	List<PosCardBin> queryAllInfo(@Param("accountNo")String accountNo);

	@Select("select bank_name from acp_bear_bank  where  type = #{type} ")
	@ResultType(String.class)
	List<String> queryAcpBearBankNameByType(@Param("type")Integer type);

	@Select("select * from acp_bear_bank  where  type = #{type} and bank_name like CONCAT('%',#{bankName},'%') ")
	@ResultType(Map.class)
	Map queryAcpBearBankByBankName(@Param("bankName")String bankName,@Param("type")Integer type);
}
