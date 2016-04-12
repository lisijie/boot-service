package org.lisijie.common;

import org.apache.thrift.TException;
import org.lisijie.thrift.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务处理器基类
 *
 * @author jesse.li (lsj86@qq.com)
 */
public class BaseServiceHandler {

    protected Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * ping接口,用于检查服务是否可用
     *
     * @param seq 请求序号
     * @return int
     * @throws TException
     */
    public int ping(int seq) throws TException {
        return seq;
    }

    /**
     * 解析请求
     *
     * @param req
     * @throws TException
     */
    protected ServiceRequest resolveRequest(Request req) throws TException {
        return new ServiceRequest(req);
    }

    /**
     * 创建输出对象
     *
     * @return
     */
    protected ServiceResponse createResponse() {
        return new ServiceResponse(this.getClass().getName());
    }
}
