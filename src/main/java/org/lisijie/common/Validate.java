package org.lisijie.common;

import com.alibaba.fastjson.JSONObject;
import org.lisijie.exceptions.ServiceException;
import org.springframework.util.StringUtils;

/**
 * 验证类
 */
public class Validate {

    public static void require(JSONObject params, String... fields) throws ServiceException {
        if (fields.length > 0) {
            for (String field : fields) {
                if (!params.containsKey(field)) {
                    throw new ServiceException("缺少参数:" + field);
                }
            }
        }
    }

    /**
     * 参数不能为空
     * @param params
     * @param fields
     * @throws ServiceException
     */
    public static void notEmpty(JSONObject params, String... fields) throws ServiceException {
        if (fields.length > 0) {
            for (String field : fields) {
                if (StringUtils.isEmpty(params.getString(field))) {
                    throw new ServiceException(field + "不能为空");
                }
            }
        }
    }
}
