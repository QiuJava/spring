package cn.eeepay.framework.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 代理商操作日志bean
 */
public class AgentOperLogBean implements Serializable{

	private static final long serialVersionUID = 6136557820607183872L;

	//日志编号
	private Integer id;
	
	//代理商编号
	private String agentNo;
	private String agentName;		// 代理商名称
	private String requestMethod; 	// 请求方法
	private String methodDesc;		// 方法描述
	private String requsetParams;	// 请求参数
	private String returnResult;	// 返回结果
	private String operIp;			// 请求ip
	private String operTime;		// 操作时间

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getMethodDesc() {
		return methodDesc;
	}

	public void setMethodDesc(String methodDesc) {
		this.methodDesc = methodDesc;
	}

	public String getRequsetParams() {
		return requsetParams;
	}

	public void setRequsetParams(String requsetParams) {
		this.requsetParams = requsetParams;
	}

	public String getReturnResult() {
		return returnResult;
	}

	public void setReturnResult(String returnResult) {
		this.returnResult = returnResult;
	}

	public String getOperIp() {
		return operIp;
	}

	public void setOperIp(String operIp) {
		this.operIp = operIp;
	}

	public String getOperTime() {
		return operTime;
	}

	public void setOperTime(String operTime) {
		this.operTime = operTime;
	}
}