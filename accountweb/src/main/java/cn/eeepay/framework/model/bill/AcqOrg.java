package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AcqOrg implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String acqName;//'收单机构名称',
	private String acqEnname; //'收单机构英文简称',
	private String host; //'收单机构主机地址',
	private String port; //'收单机构端口',
	private String LMKOMK; //'LMK下的机构主密钥',
	private String LMKOMKCV; //'LMK下的机构主密钥校验值',
	private String LMKOPK; //'LMK下的Pin密钥',
	private String LMKOPKCV; //'LMK下的Pin密钥校验值',
	private String LMKOAK; //'LMK下的Mac密钥',
	private String LMKOAKCV; //'LMK下的Mac密钥校验值',
	private String WORKKEY; //'收单机构工作秘钥',
	private Integer acqStatus; //'收单机构状态 0.关闭 1.开通',
	private Integer settleType; //'结算类型 1.代付',
	private Date dayAlteredTime; //'日切时间点',
	private Integer settleAccountId; //'分润结算账户ID',
	private Integer acqTransHaveOut; //'本收单机构的交易只能在本机构出款 1.是',
	private Integer realtimeT0greaterT1; //'允许实时T0大于T1 1.否 2.是',
	private BigDecimal acqSuccessAmount; //'收单服务日成功交易总额阀值',
	private String phone; //'负责人手机号',
	private BigDecimal acqDefDayAmount; //'默认收单商户每日限额',
	private Integer dayAmountT0greaterT1; //'允许T0日额大于T1日额 1.否 2.是',
	private BigDecimal t0AdvanceMoney; //'T0垫资额度 单位：元',
	private BigDecimal t0OwnMoney; //'T0自有额度 单位:元',
	private BigDecimal valvesAmount; //'冲量提醒阀值 单位：元',
	private BigDecimal t1TransAmount; //'当日T1交易总额',
	private BigDecimal t0TransAdvanceAmount; //'当日T0垫资交易总额',
	private BigDecimal t0TransOwnAmount; //'当日T0自有交易额度',
	private Integer closeType; //'关闭类型：1.全天 2.指定时段',
	private Date closeStartTime; //'收单机构指定关闭时段起始时间',
	private Date closeEndTime; //'收单机构指定关闭时段截止时间',
	private String acqCloseTips; //'收单机构关闭提示语',
	private Date createTime; //'收单机构创建时间',
	private String createPerson; //'收单机构创建人',
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAcqName() {
		return acqName;
	}
	public void setAcqName(String acqName) {
		this.acqName = acqName;
	}
	public String getAcqEnname() {
		return acqEnname;
	}
	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getLMKOMK() {
		return LMKOMK;
	}
	public void setLMKOMK(String lMKOMK) {
		LMKOMK = lMKOMK;
	}
	public String getLMKOMKCV() {
		return LMKOMKCV;
	}
	public void setLMKOMKCV(String lMKOMKCV) {
		LMKOMKCV = lMKOMKCV;
	}
	public String getLMKOPK() {
		return LMKOPK;
	}
	public void setLMKOPK(String lMKOPK) {
		LMKOPK = lMKOPK;
	}
	public String getLMKOPKCV() {
		return LMKOPKCV;
	}
	public void setLMKOPKCV(String lMKOPKCV) {
		LMKOPKCV = lMKOPKCV;
	}
	public String getLMKOAK() {
		return LMKOAK;
	}
	public void setLMKOAK(String lMKOAK) {
		LMKOAK = lMKOAK;
	}
	public String getLMKOAKCV() {
		return LMKOAKCV;
	}
	public void setLMKOAKCV(String lMKOAKCV) {
		LMKOAKCV = lMKOAKCV;
	}
	public String getWORKKEY() {
		return WORKKEY;
	}
	public void setWORKKEY(String wORKKEY) {
		WORKKEY = wORKKEY;
	}
	public Integer getAcqStatus() {
		return acqStatus;
	}
	public void setAcqStatus(Integer acqStatus) {
		this.acqStatus = acqStatus;
	}
	public Integer getSettleType() {
		return settleType;
	}
	public void setSettleType(Integer settleType) {
		this.settleType = settleType;
	}
	public Date getDayAlteredTime() {
		return dayAlteredTime;
	}
	public void setDayAlteredTime(Date dayAlteredTime) {
		this.dayAlteredTime = dayAlteredTime;
	}
	public Integer getSettleAccountId() {
		return settleAccountId;
	}
	public void setSettleAccountId(Integer settleAccountId) {
		this.settleAccountId = settleAccountId;
	}
	public Integer getAcqTransHaveOut() {
		return acqTransHaveOut;
	}
	public void setAcqTransHaveOut(Integer acqTransHaveOut) {
		this.acqTransHaveOut = acqTransHaveOut;
	}
	public Integer getRealtimeT0greaterT1() {
		return realtimeT0greaterT1;
	}
	public void setRealtimeT0greaterT1(Integer realtimeT0greaterT1) {
		this.realtimeT0greaterT1 = realtimeT0greaterT1;
	}
	public BigDecimal getAcqSuccessAmount() {
		return acqSuccessAmount;
	}
	public void setAcqSuccessAmount(BigDecimal acqSuccessAmount) {
		this.acqSuccessAmount = acqSuccessAmount;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public BigDecimal getAcqDefDayAmount() {
		return acqDefDayAmount;
	}
	public void setAcqDefDayAmount(BigDecimal acqDefDayAmount) {
		this.acqDefDayAmount = acqDefDayAmount;
	}
	public Integer getDayAmountT0greaterT1() {
		return dayAmountT0greaterT1;
	}
	public void setDayAmountT0greaterT1(Integer dayAmountT0greaterT1) {
		this.dayAmountT0greaterT1 = dayAmountT0greaterT1;
	}
	public BigDecimal getT0AdvanceMoney() {
		return t0AdvanceMoney;
	}
	public void setT0AdvanceMoney(BigDecimal t0AdvanceMoney) {
		this.t0AdvanceMoney = t0AdvanceMoney;
	}
	public BigDecimal getT0OwnMoney() {
		return t0OwnMoney;
	}
	public void setT0OwnMoney(BigDecimal t0OwnMoney) {
		this.t0OwnMoney = t0OwnMoney;
	}
	public BigDecimal getValvesAmount() {
		return valvesAmount;
	}
	public void setValvesAmount(BigDecimal valvesAmount) {
		this.valvesAmount = valvesAmount;
	}
	public BigDecimal getT1TransAmount() {
		return t1TransAmount;
	}
	public void setT1TransAmount(BigDecimal t1TransAmount) {
		this.t1TransAmount = t1TransAmount;
	}
	public BigDecimal getT0TransAdvanceAmount() {
		return t0TransAdvanceAmount;
	}
	public void setT0TransAdvanceAmount(BigDecimal t0TransAdvanceAmount) {
		this.t0TransAdvanceAmount = t0TransAdvanceAmount;
	}
	public BigDecimal getT0TransOwnAmount() {
		return t0TransOwnAmount;
	}
	public void setT0TransOwnAmount(BigDecimal t0TransOwnAmount) {
		this.t0TransOwnAmount = t0TransOwnAmount;
	}
	public Integer getCloseType() {
		return closeType;
	}
	public void setCloseType(Integer closeType) {
		this.closeType = closeType;
	}
	public Date getCloseStartTime() {
		return closeStartTime;
	}
	public void setCloseStartTime(Date closeStartTime) {
		this.closeStartTime = closeStartTime;
	}
	public Date getCloseEndTime() {
		return closeEndTime;
	}
	public void setCloseEndTime(Date closeEndTime) {
		this.closeEndTime = closeEndTime;
	}
	public String getAcqCloseTips() {
		return acqCloseTips;
	}
	public void setAcqCloseTips(String acqCloseTips) {
		this.acqCloseTips = acqCloseTips;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreatePerson() {
		return createPerson;
	}
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}
	
	

}
