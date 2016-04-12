package org.lisijie.thrift.aop;

import org.lisijie.common.ServiceResponse;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;

/**
 * Thrift服务调用拦截器
 * 在这里统计每个方法的调用次数
 */
public class MetricsThriftMethodInterceptor implements MethodInterceptor {

    final GaugeService gaugeService;
    final CounterService counterService;

    public MetricsThriftMethodInterceptor(GaugeService gaugeService, CounterService counterService) {
        this.gaugeService = gaugeService;
        this.counterService = counterService;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return invocation.proceed();
        } catch (NullPointerException e) {
            return ServiceResponse.createFromException(e).setMsg("NullPointerException");
        } catch (Exception e) {
            return ServiceResponse.createFromException(e);
        } finally {
            counterService.increment(invocation.getThis().getClass().getName() + "." + invocation.getMethod().getName());
            gaugeService.submit("timer.thrift." + invocation.getThis().getClass().getCanonicalName() + "." + invocation.getMethod().getName(), System.currentTimeMillis() - startTime);
        }
    }
}

