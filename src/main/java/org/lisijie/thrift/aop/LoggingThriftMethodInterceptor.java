package org.lisijie.thrift.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import java.lang.reflect.Method;

/**
 * 日志处理拦截器
 * 对每个方法的调用前, 调用后做日志记录.
 */
public class LoggingThriftMethodInterceptor implements MethodBeforeAdvice, AfterReturningAdvice {

    private static Logger log = LoggerFactory.getLogger(LoggingThriftMethodInterceptor.class);

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        log.info("{}.{}() 接收请求 : {}",
                target.getClass().getSimpleName(),
                method.getName(),
                args[0]
        );
    }

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        log.info("{}.{}() 返回结果 : {}",
                target.getClass().getSimpleName(),
                method.getName(),
                returnValue);
    }

}
