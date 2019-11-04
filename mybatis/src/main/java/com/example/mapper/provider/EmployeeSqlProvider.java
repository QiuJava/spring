package com.example.mapper.provider;

import com.example.entity.Employee;
import org.apache.ibatis.jdbc.SQL;

public class EmployeeSqlProvider {

    public String insertSelective(Employee record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("employee");
        
        if (record.getId() != null) {
            sql.VALUES("id", "#{id,jdbcType=BIGINT}");
        }
        
        if (record.getUsername() != null) {
            sql.VALUES("username", "#{username,jdbcType=VARCHAR}");
        }
        
        if (record.getPassword() != null) {
            sql.VALUES("password", "#{password,jdbcType=VARCHAR}");
        }
        
        if (record.getEmail() != null) {
            sql.VALUES("email", "#{email,jdbcType=VARCHAR}");
        }
        
        if (record.getNickname() != null) {
            sql.VALUES("nickname", "#{nickname,jdbcType=VARCHAR}");
        }
        
        if (record.getPasswordErrors() != null) {
            sql.VALUES("password_errors", "#{passwordErrors,jdbcType=INTEGER}");
        }
        
        if (record.getStatus() != null) {
            sql.VALUES("status", "#{status,jdbcType=INTEGER}");
        }
        
        if (record.getSuperAdmin() != null) {
            sql.VALUES("super_admin", "#{superAdmin,jdbcType=INTEGER}");
        }
        
        if (record.getEmployeeType() != null) {
            sql.VALUES("employee_type", "#{employeeType,jdbcType=INTEGER}");
        }
        
        if (record.getEmployeeNumber() != null) {
            sql.VALUES("employee_number", "#{employeeNumber,jdbcType=VARCHAR}");
        }
        
        if (record.getIntro() != null) {
            sql.VALUES("intro", "#{intro,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateTime() != null) {
            sql.VALUES("create_time", "#{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            sql.VALUES("update_time", "#{updateTime,jdbcType=TIMESTAMP}");
        }
        
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(Employee record) {
        SQL sql = new SQL();
        sql.UPDATE("employee");
        
        if (record.getUsername() != null) {
            sql.SET("username = #{username,jdbcType=VARCHAR}");
        }
        
        if (record.getPassword() != null) {
            sql.SET("password = #{password,jdbcType=VARCHAR}");
        }
        
        if (record.getEmail() != null) {
            sql.SET("email = #{email,jdbcType=VARCHAR}");
        }
        
        if (record.getNickname() != null) {
            sql.SET("nickname = #{nickname,jdbcType=VARCHAR}");
        }
        
        if (record.getPasswordErrors() != null) {
            sql.SET("password_errors = #{passwordErrors,jdbcType=INTEGER}");
        }
        
        if (record.getStatus() != null) {
            sql.SET("status = #{status,jdbcType=INTEGER}");
        }
        
        if (record.getSuperAdmin() != null) {
            sql.SET("super_admin = #{superAdmin,jdbcType=INTEGER}");
        }
        
        if (record.getEmployeeType() != null) {
            sql.SET("employee_type = #{employeeType,jdbcType=INTEGER}");
        }
        
        if (record.getEmployeeNumber() != null) {
            sql.SET("employee_number = #{employeeNumber,jdbcType=VARCHAR}");
        }
        
        if (record.getIntro() != null) {
            sql.SET("intro = #{intro,jdbcType=VARCHAR}");
        }
        
        if (record.getCreateTime() != null) {
            sql.SET("create_time = #{createTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdateTime() != null) {
            sql.SET("update_time = #{updateTime,jdbcType=TIMESTAMP}");
        }
        
        sql.WHERE("id = #{id,jdbcType=BIGINT}");
        
        return sql.toString();
    }
}