package com.franksu.rpc.test.provider.service.Impl;

import com.franksu.rpc.annotation.RpcService;
import com.franksu.rpc.test.provider.service.DemoService;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.test.provider.service.Impl
 * @Author: franksu
 * @CreateTime: 2023-12-24  13:48
 * @Description: provider DemoService实现
 * @Version: 1.0
 */
@RpcService(interfaceClass = DemoService.class, interfaceClassName = "com.franksu.rpc.test.scanner.service.DemoService",
version = "1.0.0", group = "franksu")
public class ProviderDemoServiceImpl implements DemoService {
}
