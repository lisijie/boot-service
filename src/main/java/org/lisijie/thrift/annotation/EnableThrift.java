package org.lisijie.thrift.annotation;

import org.lisijie.thrift.ThriftAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 打开thrift注解支持
 * 因为spring boot只会自动扫描应用入口包及其子包下的自动配置类.
 * 如果本包跟应用入口类不在同一个包, 需要通过特殊的方式让spring boot调用ThriftAutoConfiguration.
 *
 * 使用方法:
 *   在入口类加上本注解 @EnableThrift
 *
 * @author jesse.li
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ThriftAutoConfiguration.class)
@Documented
public @interface EnableThrift {
    String[] value() default {};
}