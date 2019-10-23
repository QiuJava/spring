package cn.eeepay.framework.util;


import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 666666 on 2017/9/5.
 */
public class ToolUtils {

    /**
     * 将collections转化为map
     * @param collections   集合
     * @param transformer   转换接口
     * @param <T>           集合泛型
     * @return  map
     */
    public static <T> Map<String, T> collection2Map(Collection<T> collections, Transformer<T> transformer){
        Map<String, T> map = new HashMap<>();
        if (CollectionUtils.isEmpty(collections)){
            return map;
        }
        for (T value : collections) {
            map.put(transformer.transform(value), value);
        }
        return map;
    }


    public interface Transformer<T>{
        String transform(T value);
    }
}

