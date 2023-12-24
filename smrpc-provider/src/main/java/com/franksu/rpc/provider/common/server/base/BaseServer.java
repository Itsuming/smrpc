package com.franksu.rpc.provider.common.server.base;

import com.franksu.rpc.provider.common.handler.RpcProviderHandler;
import com.franksu.rpc.provider.common.server.api.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.rpc.provider.common.server.base
 * @Author: franksu
 * @CreateTime: 2023-12-24  13:07
 * @Description: BaseServer
 * @Version: 1.0
 */
public class BaseServer implements Server {
    private final Logger logger = LoggerFactory.getLogger(BaseServer.class);

    /**
     * 主机域名或ip地址
     */
    protected String host = "127.0.0.1";
    /**
     * 端口号
     */
    protected int port = 27110;
    /**
     * 存储实体类关系
     */
    protected Map<String, Object> handlerMap = new HashMap<>();

    public BaseServer(String serverAddress) {
        if (StringUtils.isNotEmpty(serverAddress)) {
            String[] serverArray = serverAddress.split(":");
            this.host = serverArray[0];
            this.port = Integer.parseInt(serverArray[1]);
        }
    }
    @Override
    public void startNettyServer() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                            // TODO: 2023/12/24 预留编解码，需要实现自定义协议
                                    .addLast(new StringDecoder())
                                    .addLast(new StringEncoder())
                                    .addLast(new RpcProviderHandler(handlerMap));
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = serverBootstrap.bind(host, port).sync();
            logger.info("server started on {} : {}", host, port);
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("RPC server start error", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
