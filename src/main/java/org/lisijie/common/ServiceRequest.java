package org.lisijie.common;

import com.alibaba.fastjson.JSONObject;
import org.lisijie.thrift.Request;
import org.apache.thrift.TException;

/**
 * 服务接口的请求对象
 *
 * 负责解析thrift定义的request对象参数,并提供获取参数方法.
 *
 * @author jesse.li (lsj86@qq.com)
 */
public class ServiceRequest {

    /**
     * Thrift定义的请求对象
     */
    private Request request;

    /**
     * 请求参数的Map
     */
    private JSONObject params;

    public ServiceRequest(Request request) throws TException {
        this.request = request;
        try {
            this.params = JSONObject.parseObject(request.data);
        } catch (Exception e) {
            throw new TException("请求体解析失败:" + e.getMessage());
        }
    }

    /**
     * 返回params对象
     * @return
     */
    public JSONObject params() {
        return params;
    }

    /**
     * 获取客户端IP
     * @return
     */
    public long getClientIp() {
        return request.clientIp;
    }

    /**
     * 获取业务AppID
     * @return
     */
    public int getAppId() {
        return request.appId;
    }

    /**
     * 获取业务AppKey
     * @return
     */
    public String getAppKey() {
        return request.appKey;
    }

    /**
     * 获取请求ID
     * @return
     */
    public String getRequestId() {
        return request.requestId;
    }

    /**
     * 获取请求时间戳
     * @return
     */
    public long getRequestTime() {
        return request.requestTime;
    }
}
