package com.franksu.rpc.provider;

import com.franksu.rpc.common.scanner.server.RpcServiceScanner;
import com.franksu.rpc.provider.common.server.base.BaseServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.rpc.provider.common
 * @Author: franksu
 * @CreateTime: 2023-12-24  13:29
 * @Description: java原生方式启动服务
 * @Version: 1.0
 */
public class RpcSingleServer extends BaseServer {
    private final Logger logger = LoggerFactory.getLogger(RpcSingleServer.class);
    public RpcSingleServer(String serverAddress, String scanPackage, String reflectType) {
        super(serverAddress, reflectType);
        try {
            this.handlerMap = RpcServiceScanner.doScannerWithRpcServiceAnnotationFilterAndRegistryService(scanPackage);
        } catch (Exception e) {
            logger.error("RPC server init error", e);
        }
    }
}
