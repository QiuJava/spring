package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ProblemType;
import cn.eeepay.framework.model.UserFeedbackProblem;

public interface UserFeedbackProblemDao {

	@SelectProvider(type=SqlProvider.class,method="selectAllInfo")
	@ResultType(UserFeedbackProblem.class)
	List<UserFeedbackProblem> selectAllInfo(Page<UserFeedbackProblem> page,@Param("ufp")UserFeedbackProblem ufp);
	
	@Select("SELECT ufp.*,ui.user_name,ui.mobilephone,pt.type_name from user_feedback_problem ufp "
			+ "LEFT JOIN user_info ui on ui.user_id=ufp.user_id "
			+ "LEFT JOIN problem_type pt on pt.problem_type=ufp.problem_type where ufp.id=#{id}")
	@ResultType(UserFeedbackProblem.class)
	UserFeedbackProblem selectDetailById(@Param("id")int id);
	
	@Select("SELECT * from problem_type")
	@ResultType(ProblemType.class)
	List<ProblemType> selectAllProblemInfo();
	
	public class SqlProvider{
			public String selectAllInfo(Map<String,Object> param){
				final UserFeedbackProblem ufp=(UserFeedbackProblem)param.get("ufp");
				return new SQL(){{
					SELECT("ufp.*,ui.user_name,ui.mobilephone,pt.type_name");
					FROM("user_feedback_problem ufp "
							+ "LEFT JOIN user_info ui on ui.user_id=ufp.user_id "
							+ "LEFT JOIN problem_type pt on pt.problem_type=ufp.problem_type");
					if(StringUtils.isNotBlank(ufp.getTitle())){
						WHERE("ufp.title= #{ufp.title}");
					}
					if(StringUtils.isNotBlank(ufp.getUserName())){
						WHERE("ui.user_name= #{ufp.userName}");
					}
					if(StringUtils.isNotBlank(ufp.getProblemType())){
						WHERE("ufp.problem_type = #{ufp.problemType}");
					}
					if(StringUtils.isNotBlank(ufp.getUserType())){
						WHERE("ufp.user_type =#{ufp.userType}");
					}
					if(StringUtils.isNotBlank(ufp.getMobilephone())){
						WHERE("ui.mobilephone = #{ufp.mobilephone}");
					}
					ORDER_BY("submit_time desc");
				}}.toString();
			}
	  }
	
}
