package com.franksu.rpc.provider.common.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.rpc.provider.common.handler
 * @Author: franksu
 * @CreateTime: 2023-12-24  12:55
 * @Description: 服务提供者消息处理
 * @Version: 1.0
 */
public class RpcProviderHandler extends SimpleChannelInboundHandler {
    private final Logger logger = LoggerFactory.getLogger(RpcProviderHandler.class);

    private final Map<String, Object> handlerMap;

    public RpcProviderHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        logger.info("RPC提供者收到的数据为===>>> {}", o.toString());
        logger.info("handlerMap中存放的数据如下所示：");
        for (Map.Entry<String, Object> entry : handlerMap.entrySet()) {
            logger.info(entry.getKey() + " === " + entry.getValue());
        }
        //直接返回数据
        channelHandlerContext.writeAndFlush(o);
    }
}
