package com.franksu.rpc.consumer.common;

import com.franksu.rpc.consumer.common.future.RpcFuture;
import com.franksu.rpc.consumer.common.handler.RpcConsumerHandler;
import com.franksu.rpc.consumer.common.initializer.RpcConsumerInitializer;
import com.franksu.rpc.protocol.RpcProtocol;
import com.franksu.rpc.protocol.request.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.consumer.common
 * @Author: franksu
 * @CreateTime: 2024-01-27  14:11
 * @Description: 消费者
 * @Version: 1.0
 */
public class RpcConsumer {

    private final Logger logger = LoggerFactory.getLogger(RpcConsumer.class);
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    private static volatile RpcConsumer instance;

    private static Map<String, RpcConsumerHandler> handlerMap = new ConcurrentHashMap<>();

    private RpcConsumer() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(4);
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new RpcConsumerInitializer());
    }

    public static RpcConsumer getInstance() {
        if (instance == null) {
            synchronized (RpcConsumer.class) {
                if (instance == null) {
                    instance = new RpcConsumer();
                }
            }
        }
        return  instance;
    }

    public void close() {
        eventLoopGroup.shutdownGracefully();
    }

    public RpcFuture sendRequest(RpcProtocol<RpcRequest> protocol) throws Exception {
        // TODO: 2024/1/27 暂时写死，后续在引入注册中心时，从注册中心获取
        String serviceAddress = "127.0.0.1";
        int port = 27880;
        String key = serviceAddress.concat("_").concat(String.valueOf(port));
        RpcConsumerHandler handler = handlerMap.get(key);
        // 缓存中无RpcClientHandler
        if (handler == null) {
            handler = getRpcConsumerHandler(serviceAddress, port);
            handlerMap.put(key, handler);
        } else if (!handler.getChannel().isActive()) {
            // 缓存中存在clien，但不活跃
            handler.close();
            handler = getRpcConsumerHandler(serviceAddress, port);
            handlerMap.put(key, handler);
        }

        return handler.sendRequest(protocol);

    }

    /**
     * 创建连接并返回RpcClientHandler
     */
    private RpcConsumerHandler getRpcConsumerHandler(String serviceAddress, int port) throws Exception {
        ChannelFuture channelFuture = bootstrap.connect(serviceAddress, port).sync();
        channelFuture.addListener((ChannelFutureListener) -> {
            if (channelFuture.isSuccess()) {
                logger.info("connect rpc server {} on port {} success.", serviceAddress, port);
            } else {
                logger.error("connect rpc server {} on port {} failed.", serviceAddress, port);
            }
            channelFuture.cause().printStackTrace();
            eventLoopGroup.shutdownGracefully();
        });

        return channelFuture.channel().pipeline().get(RpcConsumerHandler.class);
    }
}
