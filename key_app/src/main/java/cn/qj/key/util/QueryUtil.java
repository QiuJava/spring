package cn.qj.key.util;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;

/**
 * 查询工具
 * 
 * @author Qiujian
 * @date 2018/11/27
 */
public class QueryUtil {
	private QueryUtil() {
	}

	public static String handelPageSql(String sql, int page, int size) {
		StringBuilder sb = new StringBuilder(sql);
		sb.append(" LIMIT ");
		int record = (page - 1) * size;
		if (record == 0) {
			sb.append(size);
		} else {
			sb.append(record).append(",").append(size);
		}
		return sb.toString();
	}

	public static <T> T findOneResult(EntityManager entityManager, String sql, Class<T> clz, List<Object> params) {
		return findListResult(entityManager, sql, clz, params).get(0);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> findListResult(EntityManager entityManager, String sql, Class<T> clz,
			List<Object> params) {
		Query nativeQuery = entityManager.createNativeQuery(sql);
		for (int i = 0; i < params.size(); i++) {
			nativeQuery.setParameter(i + 1, params.get(i));
		}
		SQLQuery sqlQuery = nativeQuery.unwrap(SQLQuery.class);
		org.hibernate.Query query = sqlQuery.setResultTransformer(Transformers.aliasToBean(clz));
		return query.list();
	}

}
