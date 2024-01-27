package com.franksu.rpc.consumer.common.handler;

import com.alibaba.fastjson.JSONObject;
import com.franksu.rpc.protocol.RpcProtocol;
import com.franksu.rpc.protocol.request.RpcRequest;
import com.franksu.rpc.protocol.response.RpcResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;


/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.consumer.common.handler
 * @Author: franksu
 * @CreateTime: 2024-01-27  13:50
 * @Description: RPC消费者处理器
 * @Version: 1.0
 */
public class RpcConsumerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {

    private final Logger logger = LoggerFactory.getLogger(RpcConsumerHandler.class);

    private volatile Channel channel;

    private SocketAddress remotePeer;

    public Channel getChannel() {
        return channel;
    }

    public SocketAddress getRemotePeer() {
        return remotePeer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> rpcResponseRpcProtocol) throws Exception {
        logger.info("服务消费者接收到的数据===>>> {}", JSONObject.toJSONString(rpcResponseRpcProtocol));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.remotePeer = this.channel.remoteAddress();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    /**
     * 服务消费者向发送者发送消息
     */
    public void sendRequest(RpcProtocol<RpcRequest> protocol) {
        logger.info("服务消费者发送的数据===>>> {}", JSONObject.toJSONString(protocol));
        channel.writeAndFlush(protocol);
    }

    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
}
