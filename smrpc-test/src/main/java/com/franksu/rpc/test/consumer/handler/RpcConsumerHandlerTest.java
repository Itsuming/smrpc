package com.franksu.rpc.test.consumer.handler;

import com.franksu.rpc.consumer.common.RpcConsumer;
import com.franksu.rpc.protocol.RpcProtocol;
import com.franksu.rpc.protocol.header.RpcHeaderFactory;
import com.franksu.rpc.protocol.request.RpcRequest;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.test.consumer.handler
 * @Author: franksu
 * @CreateTime: 2024-01-27  14:37
 * @Description: 测试类
 * @Version: 1.0
 */
public class RpcConsumerHandlerTest {

    public static void main(String[] args) throws Exception {
        RpcConsumer consumer = RpcConsumer.getInstance();
        consumer.sendRequest(getRpcRequestProtocol());
        Thread.sleep(2000);
        consumer.close();
    }

    public static RpcProtocol<RpcRequest> getRpcRequestProtocol() {
        // 模拟发送数据
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
        protocol.setRpcHeader(RpcHeaderFactory.getRequestHeader("jdk"));
        RpcRequest request = new RpcRequest();
        request.setClassName("com.franksu.rpc.test.api.DemoService");
        request.setGroup("franksu");
        request.setMethodName("hello");
        request.setParameters(new Object[]{"franksu"});
        request.setParameterTypes(new Class[]{String.class});
        request.setVersion("1.0.0");
        request.setOneway(false);
        request.setAsync(false);
        protocol.setBody(request);
        return protocol;
    }
}