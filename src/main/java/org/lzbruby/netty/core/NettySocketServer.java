package org.lzbruby.netty.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 功能描述：Netty Socket服务
 *
 * @author: lizhenbin
 * @email: lzbruby@163.com
 * @company: lzbruby.org
 * @date: 2017/7/20 time:15:39.
 */
@Component
public class NettySocketServer {

    private static final Logger logger = LoggerFactory.getLogger(NettySocketServer.class);

    @Autowired
    private SocketServerProperties properties;

    @Autowired
    private ProtocolInitializer initializer;

    public static final String EPOLL_OS = "LINUX";

    /**
     * 加载Netty Server
     *
     * @throws Exception
     */
    public void loadServer() throws Exception {
        // 启动netty
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 启动Netty Socket服务
                    logger.info("spring application context init finish. start netty socket server.");
                    startNettySocketService();
                } catch (Exception e) {
                    logger.error("spring start netty socket server error.", e);
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * 初始化启动Socket服务
     *
     * @throws Exception
     */
    public void startNettySocketService() throws Exception {
        int propertiesBossGroup = properties.getBossGroup();
        int propertiesWorkGroup = properties.getWorkGroup();

        String osName = System.getProperty("os.name");
        logger.info("spring init netty socket server finish. os name: {}", osName);

        EventLoopGroup bossGroup;
        EventLoopGroup workerGroup;
        if (StringUtils.containsIgnoreCase(EPOLL_OS, osName)) {
            bossGroup = propertiesBossGroup > 0 ? new EpollEventLoopGroup(propertiesBossGroup) : new EpollEventLoopGroup();
            workerGroup = propertiesWorkGroup > 0 ? new EpollEventLoopGroup(propertiesWorkGroup) : new EpollEventLoopGroup();
        } else {
            bossGroup = propertiesBossGroup > 0 ? new NioEventLoopGroup(propertiesBossGroup) : new NioEventLoopGroup();
            workerGroup = propertiesWorkGroup > 0 ? new NioEventLoopGroup(propertiesWorkGroup) : new NioEventLoopGroup();
        }

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(
                            StringUtils.containsIgnoreCase(EPOLL_OS, osName) ?
                                    EpollServerSocketChannel.class : NioServerSocketChannel.class
                    )
                    .childHandler(initializer)
                    .option(ChannelOption.SO_BACKLOG, properties.getSoBacklog())
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getConnectTimeoutMillis())
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            int port = properties.getPort();
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            logger.info("spring init netty socket server finish. listen port: {}", port);
            channelFuture.channel().closeFuture().sync();
        } finally {
            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
            }

            if (bossGroup != null) {
                bossGroup.shutdownGracefully();
            }

            logger.info("客户端断开Server连接，关闭NioEventLoopGroup.");
        }
    }

}
