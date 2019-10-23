package cn.eeepay.framework.service.bill;

import java.util.Date;
import java.util.List;

import cn.eeepay.framework.model.bill.BlockedIp;

public interface BlockedIpService {
	int insert(BlockedIp blockedIp) throws Exception;
	int BlockedIp(BlockedIp blockedIp) throws Exception;
	int update(BlockedIp blockedIp) throws Exception;
	int deleteBlockedIp(BlockedIp blockedIp) throws Exception;
	BlockedIp findBlockedIpByIpAndDate(String denyDay,String denyIp);
	List<BlockedIp> findAllBlockedIpList(String denyDay) throws Exception;
	List<BlockedIp> findDenyBlockedIpList(String denyDay,Date denyTime) throws Exception;
}
