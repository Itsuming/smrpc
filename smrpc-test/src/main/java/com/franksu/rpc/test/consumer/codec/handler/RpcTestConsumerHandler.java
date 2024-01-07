package com.franksu.rpc.test.consumer.codec.handler;

import com.alibaba.fastjson.JSONObject;
import com.franksu.rpc.protocol.RpcProtocol;
import com.franksu.rpc.protocol.header.RpcHeaderFactory;
import com.franksu.rpc.protocol.request.RpcRequest;
import com.franksu.rpc.protocol.response.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.test.consumer.codec.handler
 * @Author: franksu
 * @CreateTime: 2024-01-07  09:59
 * @Description: 消费者数据处理器
 * @Version: 1.0
 */
public class RpcTestConsumerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {

    private final Logger logger = LoggerFactory.getLogger(RpcTestConsumerHandler.class);


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("发送数据开始......");
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
        request.setAsync(false);
        request.setOneway(false);

        protocol.setBody(request);

        logger.info("服务消费者发送的消息===>>> {}", JSONObject.toJSONString(protocol));
        ctx.writeAndFlush(protocol);

        logger.info("数据发送完毕......");

    }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> rpcResponseRpcProtocol) throws Exception {
        logger.info("服务消费者接收到的消息：{}", JSONObject.toJSONString(rpcResponseRpcProtocol));
    }
}
