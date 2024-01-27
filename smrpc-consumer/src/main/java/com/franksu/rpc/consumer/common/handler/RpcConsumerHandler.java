package com.franksu.rpc.consumer.common.handler;

import com.alibaba.fastjson.JSONObject;
import com.franksu.rpc.protocol.RpcProtocol;
import com.franksu.rpc.protocol.header.RpcHeader;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


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

    // 存储请求ID与RpcResponse协议的对应关系
    private Map<Long, RpcProtocol<RpcResponse>> pendingResponse = new ConcurrentHashMap<>();

    public Channel getChannel() {
        return channel;
    }

    public SocketAddress getRemotePeer() {
        return remotePeer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> rpcResponseRpcProtocol) throws Exception {
        if (rpcResponseRpcProtocol == null) {
            return;
        }
        logger.info("服务消费者接收到的数据===>>> {}", JSONObject.toJSONString(rpcResponseRpcProtocol));
        RpcHeader header = rpcResponseRpcProtocol.getRpcHeader();
        long requestId = header.getRequestId();
        pendingResponse.put(requestId, rpcResponseRpcProtocol);
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
    public Object sendRequest(RpcProtocol<RpcRequest> protocol) {
        logger.info("服务消费者发送的数据===>>> {}", JSONObject.toJSONString(protocol));
        channel.writeAndFlush(protocol);
        RpcHeader header = protocol.getRpcHeader();
        long requestId = header.getRequestId();
        // 异步转同步
        while (true) {
            RpcProtocol<RpcResponse> responseRpcProtocol = pendingResponse.remove(requestId);
            if (responseRpcProtocol != null) {
                return responseRpcProtocol.getBody().getResult();
            }
        }
    }

    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
}
