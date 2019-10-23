package cn.eeepay.framework.dao.bill;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import cn.eeepay.framework.model.bill.BlockedIp;

public interface BlockedIpMapper{
	
	@Insert("insert into blocked_ip(deny_ip,deny_day,deny_num,deny_time)"
			+ " values(#{blockedIp.denyIp},#{blockedIp.denyDay},#{blockedIp.denyNum},#{blockedIp.denyTime})")
	int insert(@Param("blockedIp")BlockedIp blockedIp);
	
	@Delete("delete from blocked_ip where deny_ip = #{blockedIp.denyIp} and deny_day=#{blockedIp.denyDay} ")
	int deleteBlockedIp(@Param("blockedIp")BlockedIp blockedIp);
	
	@Update("update blocked_ip set deny_num = #{blockedIp.denyNum},deny_time= #{blockedIp.denyTime} "
			+ " where deny_day = #{blockedIp.denyDay} and deny_ip = #{blockedIp.denyIp}")
	int update(@Param("blockedIp")BlockedIp blockedIp);
	
	@Select("select * from blocked_ip where deny_day = #{denyDay} and deny_ip =#{denyIp}")
	@ResultMap("cn.eeepay.framework.dao.bill.BlockedIpMapper.BaseResultMap")
	BlockedIp findBlockedIpByIpAndDate(@Param("denyDay")String denyDay,@Param("denyIp")String denyIp);
	
	@Select("select * from blocked_ip where deny_day = #{denyDay}")
	@ResultMap("cn.eeepay.framework.dao.bill.BlockedIpMapper.BaseResultMap")
	List<BlockedIp> findAllBlockedIpList(@Param("denyDay")String denyDay);
	
	@Select("select * from blocked_ip where deny_day = #{denyDay} and deny_time > #{denyTime}")
	@ResultMap("cn.eeepay.framework.dao.bill.BlockedIpMapper.BaseResultMap")
	List<BlockedIp> findDenyBlockedIpList(@Param("denyDay")String denyDay,@Param("denyTime")Date denyTime);
	
}
