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
 * @Description: smrpc服务消费者注解
 * @Version: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Component
public @interface RpcReference {

    /**
     * 版本号
     */
    String version() default "1.0.0";

    /**
     * 注册中心类型 zookeeper、nacos、etcd、consul
     */
    String registryType() default "zookeeper";

    /**
     * 注册地址
     */
    String registryAddress() default "10.10.10.1:5556";

    /**
     * 负载均衡类型
     */
    String loadBalanceType() default "zkconsistenthash";

    /**
     * 序列化类型 protostuff、kryo、json、jdk、hessian2、fst
     */
    String serializationType() default "protostuff";
    /**
     * 超时时间,(默认5s)
     */
    long timeout() default 5000;
    /**
     * 是否异步执行
     */
    boolean async() default false;
    /**
     * 是否单向调用
     */
    boolean anoeway() default false;

    /**
     * 代理的类型 jdk、javassist、cglib
     */
    String proxy() default "jdk";
    /**
     * 服务分组
     */
    String group() default "";

}
