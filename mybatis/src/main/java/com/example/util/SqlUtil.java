package com.example.util;

import java.util.List;

/**
 * SQL工具
 *
 * @author Qiu Jian
 *
 */
public class SqlUtil {
	private SqlUtil() {
	}

	/**
	 * In语句最多只能包含1000个元素
	 */
	private static final int IN_MAX_NUM = 1000;

	/**
	 * in语句和元素拼接
	 * 
	 * @param columnName in条件的列名称
	 * @param listName   list对象在params中的键名称
	 * @param list       数据列表
	 * @return
	 */
	public static String inJoint(String columnName, String listName, List<?> list) {
		StringBuilder inSb = new StringBuilder();
		// list为空处理
		if (list == null || list.size() < 1) {
			inSb.append(columnName).append(" IN(NULL)");
			return inSb.toString();
		}

		int size = list.size();

		if (size <= SqlUtil.IN_MAX_NUM) {
			inSb.append(columnName).append(" IN(");
			for (int i = 0; i < size; i++) {
				inSb.append("#{").append(listName).append("[").append(i).append("]},");
			}
			inSb.deleteCharAt(inSb.length() - 1);
			inSb.append(")");
		} else {
			inSb.append("(");
			// 列表角标
			int listSize = 0;
			// in的个数
			int inSize = size % SqlUtil.IN_MAX_NUM > 0 ? size / SqlUtil.IN_MAX_NUM + 1 : size / SqlUtil.IN_MAX_NUM;
			for (int i = 1; i <= inSize; i++) {
				inSb.append(columnName).append(" IN(");
				// in里面的元素数
				int subSize = i * SqlUtil.IN_MAX_NUM > size ? size % SqlUtil.IN_MAX_NUM : SqlUtil.IN_MAX_NUM;
				for (int j = 0; j < subSize; j++) {
					inSb.append("#{").append(listName).append("[").append(listSize++).append("]},");
				}
				inSb.deleteCharAt(inSb.length() - 1);
				inSb.append(")");
				inSb.append(" OR ");
			}
			inSb.delete(inSb.length() - 4, inSb.length());
			inSb.append(")");
		}
		return inSb.toString();
	}

	/**
	 * 插入语句values拼接
	 * 
	 * @param sql        原sql
	 * @param listName   列表在params中的键值
	 * @param list       数据列表
	 * @param properties 属性列表
	 * @return
	 */
	public static String valuesJoint(String sql, String listName, List<?> list, String[] properties) {
		if (list == null || list.size() < 1) {
			return "select 1";
		}
		int size = list.size();
		StringBuilder valuesSb = new StringBuilder();
		valuesSb.append(sql);
		valuesSb.append("VALUES");

		for (int i = 0; i < size; i++) {
			valuesSb.append("(");
			for (int j = 0; j < properties.length; j++) {
				String propertyName = properties[j];
				valuesSb.append("#{").append(listName).append("[").append(i).append("].").append(propertyName)
						.append("},");
			}
			valuesSb.deleteCharAt(valuesSb.length() - 1);
			valuesSb.append("),");
		}
		valuesSb.deleteCharAt(valuesSb.length() - 1);
		return valuesSb.toString();
	}

}
