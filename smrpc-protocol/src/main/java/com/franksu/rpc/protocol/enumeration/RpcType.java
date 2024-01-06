package com.franksu.rpc.protocol.enumeration;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.protocol.enumeration
 * @Author: franksu
 * @CreateTime: 2023-12-24  14:34
 * @Description: 协议枚举类
 * @Version: 1.0
 */
public enum RpcType {
    /**
     * 请求消息
     */
    REQUEST(1),
    /**
     * 响应消息
     */
    RESPONSE(2),
    /**
     * 心跳数据
     */
    HEARTBEAT(3);

    private final int type;

    RpcType(int value) {
        this.type = value;
    }

    public int getType() {
        return type;
    }

    public static RpcType findByType(int type) {
        for (RpcType rpcType : RpcType.values()) {
            if (rpcType.getType() == type) {
                return rpcType;
            }
        }
        return null;
    }
}
