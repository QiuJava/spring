package cn.eeepay.framework.model;

import java.util.List;

public class UpdateActivityBatchResult {
	private Integer errorCount;
	private Integer successCount;
	private List<SnVo> errorlist;

	public Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}

	public Integer getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(Integer successCount) {
		this.successCount = successCount;
	}

	public List<SnVo> getErrorlist() {
		return errorlist;
	}

	public void setErrorlist(List<SnVo> errorlist) {
		this.errorlist = errorlist;
	}

}
