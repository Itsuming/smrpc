package com.franksu.rpc.test.scanner.consumer.service.impl;

import com.franksu.rpc.annotation.RpcReference;
import com.franksu.rpc.test.scanner.consumer.service.ConsumerBusinessService;
import com.franksu.rpc.test.scanner.service.DemoService;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: scanner.consumer.service.impl
 * @Author: franksu
 * @CreateTime: 2023-12-23  15:16
 * @Description: 服务消费者业务实现类
 * @Version: 1.0
 */
public class ConsumerBusinessServiceImpl implements ConsumerBusinessService {
    @RpcReference(registryType = "zookeeper", registryAddress = "127.0.0.1:2181", version = "1.0.0", group = "franksu")
    private DemoService demoService;
}
