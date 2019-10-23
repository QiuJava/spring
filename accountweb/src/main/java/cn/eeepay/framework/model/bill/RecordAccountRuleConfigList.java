package cn.eeepay.framework.model.bill;

import java.util.List;

public class RecordAccountRuleConfigList {
	
	private List<RecordAccountRuleConfig> recordAccountRuleConfig ;

	
	public List<RecordAccountRuleConfig> getRecordAccountRuleConfig() {
		return recordAccountRuleConfig;
	}

	public void setRecordAccountRuleConfig(List<RecordAccountRuleConfig> recordAccountRuleConfig) {
		this.recordAccountRuleConfig = recordAccountRuleConfig;
	}

	public RecordAccountRuleConfigList(List<RecordAccountRuleConfig> recordAccountRuleConfig) {
		super();
		this.recordAccountRuleConfig = recordAccountRuleConfig;
	}

	public RecordAccountRuleConfigList() {
		super();
	}

	@Override
	public String toString() {
		return "RecordAccountRuleConfigList [recordAccountRuleConfig=" + recordAccountRuleConfig + "]";
	}
	
}
