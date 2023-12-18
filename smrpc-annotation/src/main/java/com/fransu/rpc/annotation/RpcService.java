package com.fransu.rpc.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.fransu.rpc.annotation
 * @Author: franksu
 * @CreateTime: 2023-12-18  09:15
 * @Description: smrpc服务提供者注解
 * @Version: 1.0
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
