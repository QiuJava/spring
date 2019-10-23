package cn.eeepay.framework.model;

/**
 * 功能控制开关
 * 
 * @author Administrator
 *
 */
public class FunctionManager {

	private int id;

	private String functionNumber;

	private String functionName;

	private Integer functionSwitch;

	private Integer agentControl;

	//liuks 进件开关需求
	private Integer agentIsControl;//0:默认 不显示代理商控制 1,显示代理商控制
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFunctionNumber() {
		return functionNumber;
	}

	public void setFunctionNum(String functionNumber) {
		this.functionNumber = functionNumber;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public Integer getFunctionSwitch() {
		return functionSwitch;
	}

	public void setFunctionSwitch(Integer functionSwitch) {
		this.functionSwitch = functionSwitch;
	}

	public Integer getAgentControl() {
		return agentControl;
	}

	public void setAgentControl(Integer agentControl) {
		this.agentControl = agentControl;
	}

	public Integer getAgentIsControl() {
		return agentIsControl;
	}

	public void setAgentIsControl(Integer agentIsControl) {
		this.agentIsControl = agentIsControl;
	}
}
