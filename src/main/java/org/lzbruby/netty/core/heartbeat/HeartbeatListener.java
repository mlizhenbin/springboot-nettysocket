package org.lzbruby.netty.core.heartbeat;

import com.google.common.base.Charsets;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.lang.time.DateFormatUtils;
import org.lzbruby.netty.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述：Netty Server心跳检测服务，
 * 监听服务端口是否正常，不正常则重新启动服务
 *
 * @author: lizhenbin
 * @email: lzbruby@163.com
 * @company: lzbruby.org
 * @date: 2017/8/11 time:9:01.
 */
@Component
public class HeartbeatListener {

    private static final Logger logger = LoggerFactory.getLogger(HeartbeatListener.class);

    @Autowired
    private SocketServerProperties properties;

    @Autowired
    private NettySocketServer nettySocketServer;

    @Autowired
    private RetryTemplate retryTemplate;

    /**
     * 执行监听Netty Server
     */
    public void listen() {
        // 开启线程监听
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(properties.getMonitorTime());
                        sendHeartbeat();
                    } catch (InterruptedException e) {
                        logger.error("Heartbeat Listener监听Socket服务异常!", e);
                    }
                }
            }
        });
    }

    /**
     * 发送心跳检测程序
     *
     * @throws InterruptedException
     */
    protected void sendHeartbeat() throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
                    // decoder
                    ProtocolDecoder decoder = new ProtocolDecoder(
                            properties.getMaxFrameLength(),
                            properties.getLengthFieldOffset(),
                            properties.getLengthFieldLength(),
                            properties.getLengthAdjustment(),
                            properties.getInitialBytesToStrip(),
                            properties.getCharset());
                    pipeline.addLast("decoder", decoder);
                    // encoder
                    ProtocolEncoder encoder = new ProtocolEncoder(properties.getCharset());
                    pipeline.addLast("encoder", encoder);
                    pipeline.addLast(new HeartbeatHandler());
                }
            });

            // 启动客户端
            int port = properties.getPort();
            // 检测本地Socket服务
            ChannelFuture future = bootstrap.connect("127.0.0.1", port).sync();

            // 组装报文头
            ProtocolMsg msg = new ProtocolMsg();
            ProtocolHeader header = new ProtocolHeader();
            header.setMagic(SupportType.MAGIC_HEADER.getType());
            header.setMagicType(ProtocolMagicType.HEARTBEAT.getType());
            header.setChannel(0);
            header.setTradeNo(CharByteUtils.getBytes(new char[]{'E', 'C', 'H', 'O', '0', '0', '0', '1'}, Charsets.US_ASCII));

            // 组装报文体
            String body = "Netty Socket Server Hello World." + DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
            byte[] bodyBytes = body.getBytes(Charset.forName(properties.getCharset()));
            int bodySize = bodyBytes.length;
            header.setBodySize(bodySize);
            msg.setHeader(header);
            String reqBody = new String(bodyBytes, Charset.forName(properties.getCharset()));
            msg.setBody(reqBody);
            future.channel().writeAndFlush(msg);
            future.channel().closeFuture().sync();
        } catch (Exception ex) {
            // 连接出现异常时，尝试自动启动
            if (ex instanceof ConnectException) {
                logger.error("heartbeat listener try to start netty socket server.", ex);
                try {
                    retryTemplate.execute(new RetryCallback<Object, Throwable>() {
                        @Override
                        public Object doWithRetry(RetryContext retryContext) throws Throwable {
                            nettySocketServer.loadServer();
                            return null;
                        }
                    });

                    logger.info("heartbeat listener start netty socket server success.");
                } catch (Exception e) {
                    logger.error("heartbeat listener try to start netty socket server failure.", e);
                } catch (Throwable throwable) {
                    logger.error("heartbeat listener try to start netty socket server error.", throwable);
                }
            }
        } finally {
            workerGroup.shutdownGracefully();
            logger.debug("断开Server连接，关闭NioEventLoopGroup.");
        }
    }
}
