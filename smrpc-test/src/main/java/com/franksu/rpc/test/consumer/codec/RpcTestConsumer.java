package com.franksu.rpc.test.consumer.codec;

import com.franksu.rpc.test.consumer.codec.init.RpcTestConsumerInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.test.consumer.codec
 * @Author: franksu
 * @CreateTime: 2024-01-07  10:30
 * @Description: RpcTestConsumer
 * @Version: 1.0
 */
public class RpcTestConsumer {

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);

        try {
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new RpcTestConsumerInitializer());
            bootstrap.connect("127.0.0.1", 27880).sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Thread.sleep(2000);
            eventLoopGroup.shutdownGracefully();
        }
    }
}
