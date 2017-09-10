package org.lzbruby.netty;

import com.google.common.base.Charsets;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.lzbruby.netty.bean.SimpleProtocolReq;
import org.lzbruby.netty.core.*;
import org.lzbruby.netty.core.heartbeat.HeartbeatHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述：自定义协议业务多线程测试
 *
 * @author: Zhenbin.Li
 * email： lizhenbin08@sina.com
 * company：org.lzbruby
 * Date: 2017/8/20 Time：13:04
 */
public class SimpleProtocolClientJob {

    /**
     * sl4j
     */
    private static final Logger logger = LoggerFactory.getLogger(SimpleProtocolClientJob.class);

    /**
     * 发送心跳检测程序
     *
     * @throws Exception
     */
    protected static void sendSimpleProtocol() throws Exception {
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
                            10000,
                            15,
                            4,
                            0,
                            0,
                            "GBK");
                    pipeline.addLast("decoder", decoder);
                    // encoder
                    ProtocolEncoder encoder = new ProtocolEncoder("GBK");
                    pipeline.addLast("encoder", encoder);
                    pipeline.addLast(new HeartbeatHandler());
                }
            });

            while (true) {

                // 检测本地Socket服务
                ChannelFuture future = bootstrap.connect("127.0.0.1", 8090).sync();

                // 组装报文头
                ProtocolMsg msg = new ProtocolMsg();
                ProtocolHeader header = new ProtocolHeader();
                header.setMagic(SupportType.MAGIC_HEADER.getType());
                header.setMagicType(ProtocolMagicType.XML_SERIAL.getType());
                header.setChannel(0);
                header.setTradeNo(CharByteUtils.getBytes(new char[]{'P', 'R', 'O', 'B', '0', '0', '0', '1'}, Charsets.US_ASCII));
                header.setFlag((byte) 0x01);

                // 组装报文体
                SimpleProtocolReq simpleProtocolReq = new SimpleProtocolReq();
                simpleProtocolReq.setSysId("lzbruby:springboot-nettysocket");
                String xml = JacksonXmlSerializer.toXML(simpleProtocolReq);
                byte[] bodyBytes = xml.getBytes(Charset.forName("GBK"));
                int bodySize = bodyBytes.length;
                header.setBodySize(bodySize);
                msg.setHeader(header);
                String reqBody = new String(bodyBytes, Charset.forName("GBK"));
                msg.setBody(reqBody);
                future.channel().writeAndFlush(msg);
                future.channel().closeFuture().sync();
            }
        } finally {
            workerGroup.shutdownGracefully();
            logger.debug("断开Server连接，关闭NioEventLoopGroup.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int tread = 1;
        ExecutorService executorService = Executors.newFixedThreadPool(tread);
        for (int i = 0; i < tread; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        sendSimpleProtocol();
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            });

            TimeUnit.MILLISECONDS.sleep(100);
        }
    }
}
