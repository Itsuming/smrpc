package com.franksu.rpc.protocol.response;

import com.franksu.rpc.protocol.base.RpcMessage;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.protocol.response
 * @Author: franksu
 * @CreateTime: 2023-12-24  14:45
 * @Description: 响应类消息
 * @Version: 1.0
 */
public class RpcResponse extends RpcMessage {
    private static final long serialVersionUID = 13212343327372L;
    private String error;
    private Object result;
}
