package cn.eeepay.framework.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {
	/**
	 * 分批list
	 * 
	 * @param sourceList
	 *            要分批的list
	 * @param batchCount
	 *            每批list的个数
	 * @return List<List<Object>>
	 */
	public static List<List<?>> batchList(List<?> sourceList, int batchCount) {
	    List<List<?>> returnList = new ArrayList<>();
	    int startIndex = 0; // 从第0个下标开始
	    while (startIndex < sourceList.size()) {
	        int endIndex = 0;
	        if (sourceList.size() - batchCount < startIndex) {
	            endIndex = sourceList.size();
	        } else {
	            endIndex = startIndex + batchCount;
	        }
	        returnList.add(sourceList.subList(startIndex, endIndex));
	        startIndex = startIndex + batchCount; // 下一批
	    }
	    return returnList;
	}
}
