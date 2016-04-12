package org.lisijie.thrift.aop;

import org.apache.thrift.TApplicationException;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Method;

/**
 * 异常拦截器
 * 当服务方法抛出异常时,会经过这里进行处理.
 */
public class ExceptionsThriftMethodInterceptor implements ThrowsAdvice {

    private static Logger log = LoggerFactory.getLogger(ExceptionsThriftMethodInterceptor.class);

    @SuppressWarnings("unused")
    public void afterThrowing(Method method, Object[] args, Object target, Exception e) throws Throwable {
        // 直接抛出原异常,不处理
        throw e;
    }
}
