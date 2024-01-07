package com.franksu.rpc.test.provider.service.Impl;

import com.franksu.rpc.annotation.RpcService;
import com.franksu.rpc.test.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.test.provider.service.Impl
 * @Author: franksu
 * @CreateTime: 2023-12-24  13:48
 * @Description: provider DemoService实现
 * @Version: 1.0
 */
@RpcService(interfaceClass = DemoService.class, interfaceClassName = "com.franksu.rpc.test.api.DemoService",
version = "1.0.0", group = "franksu")
public class ProviderDemoServiceImpl implements DemoService {
    private final Logger logger = LoggerFactory.getLogger(ProviderDemoServiceImpl.class);

    @Override
    public String hello(String name) {
        logger.info("调用hello方法传入的参数为===>>> {}", name);
        return "hello " + name;
    }
}
