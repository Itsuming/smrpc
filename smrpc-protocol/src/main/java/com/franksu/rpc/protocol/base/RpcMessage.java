package com.franksu.rpc.protocol.base;

import java.io.Serializable;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.protocol.base
 * @Author: franksu
 * @CreateTime: 2023-12-24  14:38
 * @Description: 基础消息类
 * @Version: 1.0
 */
public class RpcMessage implements Serializable {

    /**
     * 是否单向发送
     */
    private boolean oneway;
    /**
     * 是否异步发送
     */
    private boolean async;
}
