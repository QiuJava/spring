package cn.eeepay.framework.service.bill;

public interface CommonService {
	/**
	 * 得到内部账号
	 * 内部账号  = 6位机构号+14位科目号+12位顺序号
	 * @param orgNo 机构号
	 * @param subjectNo 科目号
	 * @return
	 * @throws Exception
	 */
	
	public String getInsAccountNo(String orgNo,String subjectNo) throws Exception;
	/**
	 * 得到外部账号
	 * 外部账号  = 6位机构号+14位科目号+12位顺序号
	 * @param orgNo 机构号
	 * @param subjectNo 科目号
	 * @return
	 * @throws Exception
	 */
	public String getExtAccountNo(String orgNo,String subjectNo) throws Exception;
}
