package cn.eeepay.framework.dao;

import cn.eeepay.framework.model.TeamInfoEntry;
import org.apache.ibatis.jdbc.SQL;

public class TeamInfoEntrySqlProvider {

    public String insertSelective(TeamInfoEntry record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("team_info_entry");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        
        if (record.getTeamId() != null) {
            sql.VALUES("team_id", "#{teamId,jdbcType=BIGINT}");
        }
        
        if (record.getTeamEntryId() != null) {
            sql.VALUES("team_entry_id", "#{teamEntryId,jdbcType=VARCHAR}");
        }
        
        if (record.getTeamEntryName() != null) {
            sql.VALUES("team_entry_name", "#{teamEntryName,jdbcType=VARCHAR}");
        }
        
        if (record.getRemark() != null) {
            sql.VALUES("remark", "#{remark,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateTime() != null) {
            sql.VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getLastUpdateTime() != null) {
            sql.VALUES("last_update_time", "#{lastUpdateTime,jdbcType=TIMESTAMP}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(TeamInfoEntry record) {
        SQL sql = new SQL();
        sql.UPDATE("team_info_entry");
        
        if (record.getTeamId() != null) {
            sql.SET("team_id = #{teamId,jdbcType=BIGINT}");
        }
        
        if (record.getTeamEntryId() != null) {
            sql.SET("team_entry_id = #{teamEntryId,jdbcType=VARCHAR}");
        }
        
        if (record.getTeamEntryName() != null) {
            sql.SET("team_entry_name = #{teamEntryName,jdbcType=VARCHAR}");
        }
        
        if (record.getRemark() != null) {
            sql.SET("remark = #{remark,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getLastUpdateTime() != null) {
            sql.SET("last_update_time = #{lastUpdateTime,jdbcType=TIMESTAMP}");
        }
        
        sql.WHERE("id = #{id,jdbcType=BIGINT}");
        
        return sql.toString();
    }
}