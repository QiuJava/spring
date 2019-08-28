package cn.loan.core.repository.specification;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import cn.loan.core.entity.SystemDictionary;
import cn.loan.core.entity.SystemDictionaryItem;
import cn.loan.core.util.StringUtil;

/**
 * 数据字典明细规格
 * 
 * @author Qiujian
 *
 */
public class SystemDictionaryItemSpecification {
	public static Specification<SystemDictionaryItem> orKeyword(String keyword) {
		return (root, query, cb) -> {
			if (StringUtil.hasLength(keyword)) {
				Predicate likeItemName = cb.like(root.get(StringUtil.ITEM_NAME), keyword.concat(StringUtil.PER_CENT));
				Predicate likeItemKey = cb.like(root.get(StringUtil.ITEM_KEY), keyword.concat(StringUtil.PER_CENT));
				Predicate or = cb.or(likeItemName, likeItemKey);
				return or;
			}
			return null;
		};
	}

	public static Specification<SystemDictionaryItem> equalSystemDictionaryId(Long systemDictionaryId) {
		return (root, query, cb) -> {
			if (systemDictionaryId != null) {
				SystemDictionary systemDictionary = new SystemDictionary();
				systemDictionary.setId(systemDictionaryId);
				Predicate equalSystemDictionaryId = cb.equal(root.get(StringUtil.SYSTEM_DICTIONARY), systemDictionary);
				return equalSystemDictionaryId;
			}
			return null;
		};
	}

}
