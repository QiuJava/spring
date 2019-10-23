package cn.eeepay.framework.model;

import java.util.List;

public class TradeSumInfoQo {

	private String startTime;
	private String endTime;
	private List<String> agentNoList;
	private String teamId;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<String> getAgentNoList() {
		return agentNoList;
	}

	public void setAgentNoList(List<String> agentNoList) {
		this.agentNoList = agentNoList;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
}
