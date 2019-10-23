package cn.eeepay.framework.util;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
import org.codehaus.jackson.map.ObjectWriter;

import com.alibaba.fastjson.JSONException;


/**
 * 
 * JSON工具类
 * 
 */
public class JSONUtil {
    private static ObjectMapper objectMapper = null;
    private static ObjectWriter objectWriter = null;
    /**
     * json串中有key为A，但指定转换的mybean中未定义属性A，会抛异常。处理：mapper.configure(org.codehaus.
     * jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
     * false)，加上这一条，就没问题了，正常转换。
     * 默认的json串，如果key或value类型非字符串，需要加上双引号的，但有些json包转换出来的json串却不一定会带上双引号
     * 。使用jackson时
     * ，如果非字符串未加双引号，也会报错。处理：mapper.configure(org.codehaus.jackson.JsonParser
     * .Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)。
     */
    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(org.codehaus.jackson.JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectWriter = objectMapper.writer();
    }

    /**
     * 判断对象是否为合法JSON字符串
     * 
     * @param Object
     *            object
     * @return boolean
     */
    public static boolean mayBeJSON(Object object) {
        if (object == null || !String.class.isAssignableFrom(object.getClass())) {
            return false;
        }
        String string = (String) object;
        if (string.isEmpty()) {
            return false;
        }
        char head = string.charAt(0);
        return head == '[' || head == '{';
    }

    /**
     * 判断对象是否为合法JSON Object的字符串
     * 
     * @param Object
     *            object
     * @return boolean
     */
    public static boolean mayBeJSONObject(Object object) {
        if (object == null || !String.class.isAssignableFrom(object.getClass())) {
            return false;
        }
        String string = (String) object;
        if (string.isEmpty()) {
            return false;
        }
        char head = string.charAt(0);
        return head == '{';
    }

    /**
     * 判断对象是否为合法JSON Array的字符串
     * 
     * @param Object
     *            object
     * @return boolean
     */
    public static boolean mayBeJSONArray(Object object) {
        if (object == null || !String.class.isAssignableFrom(object.getClass())) {
            return false;
        }
        String string = (String) object;
        if (string.isEmpty()) {
            return false;
        }
        char head = string.charAt(0);
        return head == '[';
    }

    /**
     * 将JSON串转换为对象
     * 
     * @param String
     *            json JSON串
     * @param Class
     *            <T> clazz 指定的对象类型
     * @return T
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public static <T> T toObject(String json, Class<T> clazz) throws JSONException {
        if (json == null || json.isEmpty()) {
            return null;
        }
        ObjectReader reader = objectMapper.reader(clazz);
        try {
            return (T) reader.readValue(json);
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    /**
     * 将JSON串转换为List
     * 
     * @param String
     *            json JSON串
     * @return List
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static List toList(String json) throws JSONException {
        if (json == null || json.isEmpty()) {
            return null;
        }
        ObjectReader reader = objectMapper.reader(List.class);
        try {
            return reader.readValue(json);
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    /**
     * 将JSON串转换为Map
     * 
     * @param String
     *            json JSON串
     * @return Map
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public static Map toMap(String json) throws JSONException {
        if (json == null || json.isEmpty()) {
            return null;
        }
        ObjectReader reader = objectMapper.reader(Map.class);
        try {
            return reader.readValue(json);
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    /**
     * 将对象转换为JSON串
     * 
     * @return String
     * @throws Exception
     */
    public static String toJSON(Object object) throws JSONException {
        if (object == null) {
            return null;
        }
        if (String.class.isAssignableFrom(object.getClass())) {
            return (String) object;
        }
        try {
            return objectWriter.writeValueAsString(object);
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    public static void clearAll() {
        objectMapper = null;
        objectWriter = null;
    }
}