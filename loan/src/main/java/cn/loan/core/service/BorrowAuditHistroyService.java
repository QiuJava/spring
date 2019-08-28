package cn.loan.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.BorrowAuditHistroy;
import cn.loan.core.entity.SystemDictionaryItem;
import cn.loan.core.repository.BorrowAuditHistroyDao;
import cn.loan.core.util.SystemDictionaryUtil;

/**
 * 借款审核历史服务
 * 
 * @author qiujian
 *
 */
@Service
public class BorrowAuditHistroyService {

	@Autowired
	private BorrowAuditHistroyDao borrowAuditHistroyDao;
	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;

	public List<BorrowAuditHistroy> listByBorrowId(Long id) {
		List<BorrowAuditHistroy> list = borrowAuditHistroyDao.findByBorrowId(id);
		List<SystemDictionaryItem> auditTypeList = SystemDictionaryUtil.getItems(SystemDictionaryUtil.AUDIT_TYPE,
				systemDictionaryHashService);
		list.stream().forEach(borrowAuditHistroy -> {
			auditTypeList.stream().forEach(item -> {
				if (Integer.valueOf(item.getItemValue()).equals(borrowAuditHistroy.getAuditType())) {
					borrowAuditHistroy.setAuditTypeDisplay(item.getItemName());
				}
			});
		});
		return list;
	}

	public void save(BorrowAuditHistroy histroy) {
		borrowAuditHistroyDao.save(histroy);
	}

}
