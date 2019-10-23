package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.util.Date;

public class BatchesDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer stepId;
	private Integer status; //'运行结果  0:未运行  1:成功 2:失败',
	private String executeResult; //'执行日志',
	private Integer executeMode; //'执行方式  0：自动执行   1：人工执行',
	private String executePerson; //'人工执行跑批的人',
	private Date executeTime; //'人工执行跑批时间',
	private Integer batchesId; //'跑批批次id',
	
	//前台使用字段
	private String stepName;
	
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStepId() {
		return stepId;
	}
	public void setStepId(Integer stepId) {
		this.stepId = stepId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getExecuteResult() {
		return executeResult;
	}
	public void setExecuteResult(String executeResult) {
		this.executeResult = executeResult;
	}
	public Integer getExecuteMode() {
		return executeMode;
	}
	public void setExecuteMode(Integer executeMode) {
		this.executeMode = executeMode;
	}
	public String getExecutePerson() {
		return executePerson;
	}
	public void setExecutePerson(String executePerson) {
		this.executePerson = executePerson;
	}
	public Date getExecuteTime() {
		return executeTime;
	}
	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}
	public Integer getBatchesId() {
		return batchesId;
	}
	public void setBatchesId(Integer batchesId) {
		this.batchesId = batchesId;
	}
	
}
