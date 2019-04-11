package cn.qj.core.config.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Jpa 查询工具
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
@Component
@SuppressWarnings("unchecked")
public class QueryUtil {

	@Autowired
	private EntityManager entityManager;

	public <T> T findOneResult(String sql, Class<T> clz, List<Object> params) {
		return findListResult(sql, clz, params).get(0);
	}

	public <T> List<T> findListResult(String sql, Class<T> clz, List<Object> params) {
		Query nativeQuery = entityManager.createNativeQuery(sql);
		for (int i = 0; i < params.size(); i++) {
			nativeQuery.setParameter(i + 1, params.get(i));
		}
		SQLQuery sqlQuery = nativeQuery.unwrap(SQLQuery.class);
		org.hibernate.Query query = sqlQuery.setResultTransformer(Transformers.aliasToBean(clz));
		return query.list();
	}
}
