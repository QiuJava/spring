package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.TeamInfoEntry;
import cn.eeepay.framework.util.WriteReadDataSource;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
@WriteReadDataSource
public interface TeamInfoEntryMapper {
    @Delete({
        "delete from team_info_entry",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long id);

    @Insert({
        "insert into team_info_entry (id, team_id, ",
        "team_entry_id, team_entry_name, ",
        "remark, create_time, ",
        "last_update_time)",
        "values (#{id,jdbcType=BIGINT}, #{teamId,jdbcType=BIGINT}, ",
        "#{teamEntryId,jdbcType=VARCHAR}, #{teamEntryName,jdbcType=VARCHAR}, ",
        "#{remark,jdbcType=VARCHAR}, #{createTime,jdbcType=TIMESTAMP}, ",
        "#{lastUpdateTime,jdbcType=TIMESTAMP})"
    })
    int insert(TeamInfoEntry record);

    @InsertProvider(type=TeamInfoEntrySqlProvider.class, method="insertSelective")
    int insertSelective(TeamInfoEntry record);

    @Select({
        "select",
        "id, team_id, team_entry_id, team_entry_name, remark, create_time, last_update_time",
        "from team_info_entry",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="team_id", property="teamId", jdbcType=JdbcType.BIGINT),
        @Result(column="team_entry_id", property="teamEntryId", jdbcType=JdbcType.VARCHAR),
        @Result(column="team_entry_name", property="teamEntryName", jdbcType=JdbcType.VARCHAR),
        @Result(column="remark", property="remark", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="last_update_time", property="lastUpdateTime", jdbcType=JdbcType.TIMESTAMP)
    })
    TeamInfoEntry selectByPrimaryKey(Long id);

    @UpdateProvider(type=TeamInfoEntrySqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(TeamInfoEntry record);

    @Update({
        "update team_info_entry",
        "set team_id = #{teamId,jdbcType=BIGINT},",
          "team_entry_id = #{teamEntryId,jdbcType=VARCHAR},",
          "team_entry_name = #{teamEntryName,jdbcType=VARCHAR},",
          "remark = #{remark,jdbcType=VARCHAR},",
          "create_time = #{createTime,jdbcType=TIMESTAMP},",
          "last_update_time = #{lastUpdateTime,jdbcType=TIMESTAMP}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(TeamInfoEntry record);

    @Select({"SELECT ", 
    		"	*  ", 
    		"FROM ", 
    		"	`team_info_entry`  ", 
    		"WHERE ", 
    		"	team_id = #{merTeamId}"})
	List<TeamInfoEntry> findTeamInfoEntryByTeamId(@Param("merTeamId")String merTeamId);
}