package com.franksu.rpc.test.provider.single;

import com.franksu.rpc.provider.RpcSingleServer;
import org.junit.Test;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.test.provider.single
 * @Author: franksu
 * @CreateTime: 2023-12-24  13:51
 * @Description: 原生java提供服务测试类
 * @Version: 1.0
 */
public class RpcSingleServerTest {
    @Test
    public void startRpcSingleServer() {
        RpcSingleServer rpcSingleServer = new RpcSingleServer("127.0.0.1:27880", "com.franksu.rpc.test");
        rpcSingleServer.startNettyServer();
    }
}
