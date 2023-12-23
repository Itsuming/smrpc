package com.franksu.rpc.test.scanner.provider;

import com.franksu.rpc.annotation.RpcService;
import com.franksu.rpc.test.scanner.service.DemoService;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: scanner.provider
 * @Author: franksu
 * @CreateTime: 2023-12-23  15:09
 * @Description: DemoService实现类
 * @Version: 1.0
 */
@RpcService(interfaceClass = DemoService.class, interfaceClassName = "com.fransu.rpc.test.scanner.service.DemoService", version = "1.0", group = "franksu")
public class ProvicerDemoServiceImpl implements DemoService {
}
