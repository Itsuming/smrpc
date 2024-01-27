package com.franksu.rpc.consumer.common.initializer;

import com.franksu.rpc.codec.RpcDecoder;
import com.franksu.rpc.codec.RpcEncoder;
import com.franksu.rpc.consumer.common.handler.RpcConsumerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.consumer.common.initializer
 * @Author: franksu
 * @CreateTime: 2024-01-27  14:08
 * @Description: RPC消费者初始化管道
 * @Version: 1.0
 */
public class RpcConsumerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new RpcEncoder());
        pipeline.addLast(new RpcDecoder());
        pipeline.addLast(new RpcConsumerHandler());
    }
}
