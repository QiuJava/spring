package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AddRequireItem;
import cn.eeepay.framework.model.TerminalInfo;

public interface AddRequireItemService {

	public AddRequireItem selectByMeriId(String meriId);
	
	List<AddRequireItem> selectByName(Page<AddRequireItem> page, String itemName);

	AddRequireItem selectById(String id);

	int insertOrUpDate(AddRequireItem item);

	public AddRequireItem selectRequireName(String requireId);

	public List<AddRequireItem> selectAllRequireName();

	public List<AddRequireItem> queryItemNameList(Page<TerminalInfo> page);
	
	public List<AddRequireItem> getRequireItemByParams(String agent_no, String bp_id);

	public List<AddRequireItem> getRequireItemByType(String type);
	
}
