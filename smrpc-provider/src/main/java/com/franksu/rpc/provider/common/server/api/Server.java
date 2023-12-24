package com.franksu.rpc.provider.common.server.api;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.rpc.provider.common.server.api
 * @Author: franksu
 * @CreateTime: 2023-12-24  13:04
 * @Description: 服务提供方启动核心接口
 * @Version: 1.0
 */
public interface Server {
    /**
     * 启动Netty服务
     */
    void startNettyServer();
}
