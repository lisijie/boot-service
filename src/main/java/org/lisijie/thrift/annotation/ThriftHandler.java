package org.lisijie.thrift.annotation;

import org.apache.thrift.protocol.TProtocolFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Thrift处理器注解
 *
 * 要把一个thrift服务注册到某个URL上, 可直接使用注解: @ThriftHandler("/path")
 */
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface ThriftHandler {
    String[] value() default {};
    Class<? extends TProtocolFactory> factory() default TProtocolFactory.class;
}
