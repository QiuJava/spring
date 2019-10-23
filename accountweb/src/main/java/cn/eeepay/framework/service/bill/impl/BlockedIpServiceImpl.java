package cn.eeepay.framework.service.bill.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.BlockedIpMapper;
import cn.eeepay.framework.model.bill.BlockedIp;
import cn.eeepay.framework.service.bill.BlockedIpService;
@Service("blockedIpService")
@Transactional
public class BlockedIpServiceImpl implements BlockedIpService {
	private static final Logger log = LoggerFactory.getLogger(BlockedIpServiceImpl.class);
	@Resource
	public BlockedIpMapper blockedIpMapper;
	
	@Value("${login.denyip.times:5}")
	private int loginErrorTimes;//登录最大错误次数
	
	@Value("${login.denyip.minutes:30}")
	private int loginErrorMinutes;
	
	@Override
	public int insert(BlockedIp blockedIp)  throws Exception{
		return blockedIpMapper.insert(blockedIp);
	}
	@Override
	public int update(BlockedIp blockedIp)  throws Exception{
		return blockedIpMapper.update(blockedIp);
	}
	@Override
	public BlockedIp findBlockedIpByIpAndDate(String denyDay,String denyIp){
		return blockedIpMapper.findBlockedIpByIpAndDate(denyDay,denyIp);
	}
	@Override
	public int deleteBlockedIp(BlockedIp blockedIp)  throws Exception{
		return blockedIpMapper.deleteBlockedIp(blockedIp);
	}
	@Override
	public List<BlockedIp> findAllBlockedIpList(String denyDay)  throws Exception{
		return blockedIpMapper.findAllBlockedIpList(denyDay);
	}
	@Override
	public List<BlockedIp> findDenyBlockedIpList(String denyDay,Date denyTime)  throws Exception{
		return blockedIpMapper.findDenyBlockedIpList(denyDay,denyTime);
	}
	@Override
	public int BlockedIp(BlockedIp blockedIp) throws Exception {
		int i = 0;
		String denyDay = blockedIp.getDenyDay();
		String denyIp = blockedIp.getDenyIp();
		BlockedIp existBlockedIp= blockedIpMapper.findBlockedIpByIpAndDate(denyDay,denyIp);
	    Integer denyNum = 1;
        Date denyTime = new Date();
		if (existBlockedIp != null) {
			if (existBlockedIp.getDenyTime().compareTo(new Date()) < 0) {
        		log.info("登陆密码错误,禁用时间已经过期");
        		denyNum = existBlockedIp.getDenyNum() + 1;
        		if (denyNum > loginErrorTimes) {
            		denyNum = 1;
				}
            	if (denyNum == loginErrorTimes ) {
            		Calendar nowTime = Calendar.getInstance();
            		nowTime.add(Calendar.MINUTE, loginErrorMinutes);//30分钟后的时间
            		denyTime = nowTime.getTime();
				}
            	blockedIp.setDenyNum(denyNum);
            	blockedIp.setDenyTime(denyTime);
            	i = blockedIpMapper.update(blockedIp);
			}
		}
		else{
			blockedIp.setDenyNum(1);
        	blockedIp.setDenyTime(new Date());
			i = blockedIpMapper.insert(blockedIp);
		}
		return i;
	}
}
