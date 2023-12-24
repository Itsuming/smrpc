package com.franksu.rpc.protocol;

import com.franksu.rpc.protocol.header.RpcHeader;

import java.io.Serializable;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.protocol
 * @Author: franksu
 * @CreateTime: 2023-12-24  15:13
 * @Description: 协议类
 * @Version: 1.0
 */
public class RpcProtocol<T> implements Serializable {

    private static final long serialVersionUID = 123423432545L;

    /**
     * 消息头
     */
    private RpcHeader rpcHeader;
    /**
     * 消息体
     */
    private T body;
}
