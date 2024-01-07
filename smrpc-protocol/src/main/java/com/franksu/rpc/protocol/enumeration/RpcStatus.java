package com.franksu.rpc.protocol.enumeration;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.protocol.enumeration
 * @Author: franksu
 * @CreateTime: 2024-01-07  13:55
 * @Description: Rpc状态枚举类
 * @Version: 1.0
 */
public enum RpcStatus {

    SUCCESS(0),
    FAIL(1);

    private final int code;

    RpcStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
