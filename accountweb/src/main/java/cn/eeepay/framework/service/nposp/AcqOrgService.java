package cn.eeepay.framework.service.nposp;

import java.util.List;

import cn.eeepay.framework.model.bill.AcqOrg;
public interface AcqOrgService {
	/**
	 * 查询所有的收单机构
	 * @return
	 */
	public List<AcqOrg> findAllAcqOrg();

	/**
	 * 通过 id 查找收单机构信息
	 * @param id
	 * @return
	 */
	public AcqOrg findAcqOrgByUserId(String userId);
	
	/**
	 * 通过 acqEnname 查找收单机构信息
	 * @param id
	 * @return
	 */
	public AcqOrg findAcqOrgByAcqEnname(String acqEnname);
	
	/**
	 * 查询 收单机构编号  通过 用户名、手机号
	 * @param userName
	 * @param mobilephone
	 * @return
	 */
	public List<String> findAcqOrgListByParams(String userName ,String mobilephone);
}
