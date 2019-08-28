package cn.loan.core.repository.specification;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import cn.loan.core.entity.SystemDictionary;
import cn.loan.core.util.StringUtil;

/**
 * 数据字典规格
 * 
 * @author Qiujian
 *
 */
public class SystemDictionarySpecification {
	public static Specification<SystemDictionary> likeDictKey(String dictKey) {
		return (root, query, cb) -> {
			if (StringUtil.hasLength(dictKey)) {
				Predicate dictKeyLike = cb.like(root.get(StringUtil.DICT_KEY), dictKey.concat(StringUtil.PER_CENT));
				return dictKeyLike;
			}
			return null;
		};
	}

	public static Specification<SystemDictionary> likeDictName(String dictName) {
		return (root, query, cb) -> {
			if (StringUtil.hasLength(dictName)) {
				Predicate dictNameLike = cb.like(root.get(StringUtil.DICT_NAME), dictName.concat(StringUtil.PER_CENT));
				return dictNameLike;
			}
			return null;
		};
	}

}
