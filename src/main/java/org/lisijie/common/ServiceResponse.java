package org.lisijie.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.lisijie.common.data.Entity;
import org.lisijie.common.utils.ObjectUtil;
import org.lisijie.exceptions.ServiceException;
import org.lisijie.thrift.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 输出处理类
 *
 * @author jesse.li
 */
public class ServiceResponse {
    private Logger logger;
    private Response response;
    private Map<String, Object> data;

    public ServiceResponse(String loggerName) {
        logger = LoggerFactory.getLogger(loggerName);
        response = new Response();
        response.code = 0;
        response.msg  = "";
        response.data = "";
        data = new HashMap<String, Object>();
    }

    public void setError(String msg) {
        this.setError(-1, msg);
    }

    public void setError(int code, String msg) {
        response.code = code;
        response.msg  = msg;
    }

    public void setError(Exception e) {
        e.printStackTrace();
        this.setError(-1, e.getMessage());
    }

    /**
     * 输出变量
     * @param name
     * @param value
     */
    public void assign(String name, Object value) {
        if (value instanceof List) {
            value = ObjectUtil.toMapList((List<Object>)value);
        } else if (value instanceof Entity) {
            value = ObjectUtil.toMap(value);
        }
        this.data.put(name, value);
    }

    /**
     * 输出整个实体对象, key自动转换成小写+下划线格式, 如果参数不是实体对象, 将抛出异常
     * @param value
     */
    public void assign(Entity value) {
        assign(ObjectUtil.toMap(value));
    }

    /**
     * 输出已组织好的map
     * @param data
     */
    public void assign(Map<String,Object> data) {
        this.data = data;
    }

    public void reset() {
        this.response.code = 0;
        this.response.msg = "";
        this.data.clear();
    }

    public Response getResponse() {
        if (this.data != null && this.data.size() > 0) {
            this.response.data = JSON.toJSONStringWithDateFormat(this.data, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty);
        }
        return response;
    }

    /**
     * 根据异常创建Response对象
     * @param e
     * @return
     */
    public static Response createFromException(Exception e) {
        Response response = new Response();
        if (e instanceof ServiceException) {
            response.setCode(((ServiceException) e).getCode());
        } else {
            response.setCode(-1);
        }
        response.setMsg(e.getMessage());
        return response;
    }

}
