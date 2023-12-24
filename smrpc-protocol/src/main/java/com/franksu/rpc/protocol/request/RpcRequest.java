package com.franksu.rpc.protocol.request;

import com.franksu.rpc.protocol.base.RpcMessage;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.protocol.request
 * @Author: franksu
 * @CreateTime: 2023-12-24  14:40
 * @Description: 请求消息类
 * @Version: 1.0
 */
public class RpcRequest extends RpcMessage {
    private static final long serialVersionUID = 23123431245141232L;
    /**
     * 类名称
     */
    private String className;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 参数类型数组
     */
    private Class<?>[] parameterTypes;
    /**
     * 参数数组
     */
    private Object[] parameters;
    /**
     * 版本号
     */
    private String version;
    /**
     * 服务分组
     */
    private String group;
}
