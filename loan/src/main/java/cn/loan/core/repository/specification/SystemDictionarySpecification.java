package cn.loan.core.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
		return new Specification<SystemDictionary>() {
			@Override
			public Predicate toPredicate(Root<SystemDictionary> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (StringUtil.hasLength(dictKey)) {
					Predicate dictKeyLike = cb.like(root.get(StringUtil.DICT_KEY), dictKey + StringUtil.PER_CENT);
					return dictKeyLike;
				}
				return null;
			}
		};
	}

	public static Specification<SystemDictionary> likeDictName(String dictName) {
		return new Specification<SystemDictionary>() {
			@Override
			public Predicate toPredicate(Root<SystemDictionary> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if (StringUtil.hasLength(dictName)) {
					Predicate dictNameLike = cb.like(root.get(StringUtil.DICT_NAME), dictName + StringUtil.PER_CENT);
					return dictNameLike;
				}
				return null;
			}
		};
	}

}
