package com.franksu.rpc.test.consumer.codec.init;

import com.franksu.rpc.codec.RpcDecoder;
import com.franksu.rpc.codec.RpcEncoder;
import com.franksu.rpc.test.consumer.codec.handler.RpcTestConsumerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.test.consumer.codec.init
 * @Author: franksu
 * @CreateTime: 2024-01-07  10:28
 * @Description: RpcTestConsumerInitializer
 * @Version: 1.0
 */
public class RpcTestConsumerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline cp = socketChannel.pipeline();
        cp.addLast(new RpcDecoder());
        cp.addLast(new RpcEncoder());
        cp.addLast(new RpcTestConsumerHandler());
    }
}
