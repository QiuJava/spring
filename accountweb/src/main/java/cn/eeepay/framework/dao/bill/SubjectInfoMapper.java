package cn.eeepay.framework.dao.bill;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import cn.eeepay.framework.model.bill.Subject;
import cn.eeepay.framework.model.bill.SubjectInfo;

/**
 * 科目动态信息表
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月27日11:42:47
 *
 */
public interface SubjectInfoMapper {
	/**
	 * 新增科目动态信息
	 * @param subject
	 * @return
	 */
	@Insert("insert into bill_subject_info(subject_no,org_no,currency_no,create_date,subject_level,balance_from,today_balance,today_debit_amount,today_credit_amount,yesterday_amount)"
			+"values(#{subjectInfo.subjectNo},#{subjectInfo.orgNo},#{subjectInfo.currencyNo},#{subjectInfo.createDate},#{subjectInfo.subjectLevel},#{subjectInfo.balanceFrom},#{subjectInfo.todayBalance},#{subjectInfo.todayDebitAmount},#{subjectInfo.todayCreditAmount},#{subjectInfo.yesterdayAmount})"
			)
	int insertSubjectInfo(@Param("subjectInfo")SubjectInfo subjectInfo);
	
	@Update("update bill_subject_info set org_no=#{subjectInfo.orgNo},currency_no=#{subjectInfo.currencyNo}"
			+", create_date=#{subjectInfo.createDate},subject_level=#{subjectInfo.subjectLevel},balance_from=#{subjectInfo.balanceFrom}"
			+", today_balance=#{subjectInfo.todayBalance},today_debit_amount=#{subjectInfo.todayDebitAmount},today_credit_amount=#{subjectInfo.todayCreditAmount}"
			+", yesterday_amount=#{subjectInfo.yesterdayAmount}"
			+ " where subject_no=#{subject.subjectNo}")
	int updateSubjectInfo(@Param("subjectInfo")SubjectInfo subjectInfo);
	
	@Select("select * from bill_subject_info where subject_no = #{subjectNo} and org_no = #{orgNo} and currency_no = #{currencyNo} and create_date = #{createDate}")
	@ResultMap("cn.eeepay.framework.dao.bill.SubjectInfoMapper.BaseResultMap")
	SubjectInfo findSubjectInfoByParams(@Param("subjectNo")String subjectNo,@Param("orgNo")String orgNo,@Param("currencyNo")String currencyNo,@Param("createDate")Date createDate);
	
	
	@Select("select * from bill_subject_info where create_date = #{createDate}")
	@ResultMap("cn.eeepay.framework.dao.bill.SubjectInfoMapper.BaseResultMap")
	List<SubjectInfo> findSubjectInfoByDate(@Param("createDate")Date createDate);
}
