package cn.loan.core.util;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

/**
 * 本地查询工具
 *
 * @author Qiujian
 *
 */
public class NativeQueryUtil {

	private NativeQueryUtil() {
	}

	public static List<?> findListResult(EntityManager entityManager, String sql, Class<?> clz,
			Map<String, Object> params) {
		Query nativeQuery = entityManager.createNativeQuery(sql);
		params.forEach((key, value) -> {
			nativeQuery.setParameter(key, value);
		});
		SQLQuery sqlQuery = nativeQuery.unwrap(SQLQuery.class);
		org.hibernate.Query query = sqlQuery.setResultTransformer(Transformers.aliasToBean(clz));
		return query.list();
	}

}
