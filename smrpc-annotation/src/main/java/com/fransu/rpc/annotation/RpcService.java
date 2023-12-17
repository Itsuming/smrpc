package com.fransu.rpc.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务提供者注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {

    /**
     * 接口 Class
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 接口 className
     */
    String interfaceClassName() default "";

    /**
     * 版本号
     */
    String version() default "";

    /**
     * 服务分组
     */
    String group() default "";
}
