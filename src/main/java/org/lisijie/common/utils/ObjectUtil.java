package org.lisijie.common.utils;

import org.lisijie.common.data.Entity;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 对象处理工具
 *
 * @author jesse.li
 */
public class ObjectUtil {

    /**
     * 导出对象的指定属性到HashMap, 每个key转成纯小写加下划线的风格
     *
     * @param obj
     * @param keys
     * @return
     */
    public static Map<String, Object> toMap(Object obj, String[] keys) {
        if (obj == null) {
            return null;
        }
        Class c = obj.getClass();
        Map<String, Object> map = new HashMap<String, Object>();

        for (int i=0; i < keys.length; i++) {
            String methodName = "get" + StringUtil.ucfirst(keys[i]);
            try {
                Method method = c.getDeclaredMethod(methodName);
                Object value = method.invoke(obj);
                if (value instanceof List) {
                    value = toMapList((List<?>) value);
                } else if (value instanceof Entity) {
                    value = toMap(value);
                }
                map.put(StringUtil.toLowerFiled(keys[i]), value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    /**
     * 导出对象所有使用getter方法获取的属性, 每个key转成纯小写加下划线的风格
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> toMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Class c = obj.getClass();
        List<String> keys = new ArrayList<String>();
        for (Method m : c.getDeclaredMethods()) {
            if (m.getName().length() > 3
                    && m.getName().substring(0, 3).equals("get")
                    && Character.isUpperCase(m.getName().charAt(3))
                    && m.getParameterTypes().length == 0) {
                keys.add(m.getName().substring(3));
            }
        }
        return toMap(obj, keys.toArray(new String[keys.size()]));
    }

    public static List<Object> toMapList(List<?> list) {
        if (list == null) {
            return null;
        }
        List<Object> result = new ArrayList<Object>();
        for (Object obj : list) {
            if (obj instanceof Entity) {
                result.add(toMap(obj));
            } else {
                result.add(obj);
            }
        }

        return result;
    }

}

