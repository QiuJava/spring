package cn.eeepay.framework.model.nposp;

import java.io.Serializable;
import java.math.BigDecimal;

public class OutAccountService implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id; // '出款服务管理ID',
	private Integer	acqOrgId; //'收单机构ID',
	private Integer serviceType; //'出款服务类型 1：单笔代付-自有资金 2：单笔代付-垫资  3：批量代付',
	private String serviceName; //'出款服务名称',
	private BigDecimal outAccountMinAmount; //'单笔出款最小限额',
	private BigDecimal  outAccountMaxAmount; //'单笔出款最大限额',
	private BigDecimal dayOutAccountAmount; //'单日出款限额',
	private BigDecimal outAmountWarning; //'出款预警额度',
	private BigDecimal transformationAmount; //'跳转服务预警额度',
	private String level; //'优先等级',
	private String antoCloseMsg; //'提现自动关闭提示',
	private Integer outAccountStatus; //'状态 1.打开 2.关闭',
	private String acqEnname; //'收单机构英文简称',
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAcqOrgId() {
		return acqOrgId;
	}
	public void setAcqOrgId(Integer acqOrgId) {
		this.acqOrgId = acqOrgId;
	}
	public Integer getServiceType() {
		return serviceType;
	}
	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public BigDecimal getOutAccountMinAmount() {
		return outAccountMinAmount;
	}
	public void setOutAccountMinAmount(BigDecimal outAccountMinAmount) {
		this.outAccountMinAmount = outAccountMinAmount;
	}
	public BigDecimal getOutAccountMaxAmount() {
		return outAccountMaxAmount;
	}
	public void setOutAccountMaxAmount(BigDecimal outAccountMaxAmount) {
		this.outAccountMaxAmount = outAccountMaxAmount;
	}
	public BigDecimal getDayOutAccountAmount() {
		return dayOutAccountAmount;
	}
	public void setDayOutAccountAmount(BigDecimal dayOutAccountAmount) {
		this.dayOutAccountAmount = dayOutAccountAmount;
	}
	public BigDecimal getOutAmountWarning() {
		return outAmountWarning;
	}
	public void setOutAmountWarning(BigDecimal outAmountWarning) {
		this.outAmountWarning = outAmountWarning;
	}
	public BigDecimal getTransformationAmount() {
		return transformationAmount;
	}
	public void setTransformationAmount(BigDecimal transformationAmount) {
		this.transformationAmount = transformationAmount;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getAntoCloseMsg() {
		return antoCloseMsg;
	}
	public void setAntoCloseMsg(String antoCloseMsg) {
		this.antoCloseMsg = antoCloseMsg;
	}
	public Integer getOutAccountStatus() {
		return outAccountStatus;
	}
	public void setOutAccountStatus(Integer outAccountStatus) {
		this.outAccountStatus = outAccountStatus;
	}
	public String getAcqEnname() {
		return acqEnname;
	}
	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
	}
	
	
}
