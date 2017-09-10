package org.lzbruby.netty.core;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能描述：Socket服务初始化组件
 *
 * @author: lizhenbin
 * @email: lzbruby@163.com
 * @company: lzbruby.org
 * @date: 2017/7/11 time:16:31.
 */
@Component
public class ProtocolInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private SocketServerProperties properties;

    @Autowired
    private ProtocolHandler protocolHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        String socketFlag = properties.getSocketLog();
        if (StringUtils.equals(socketFlag, LogShowSwitch.LOG_ON.getFlag())) {
            pipeline.addLast("logging", new LoggingHandler(io.netty.handler.logging.LogLevel.INFO));
        }

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
        ProtocolEncoder encoder = new ProtocolEncoder(
                properties.getCharset());
        pipeline.addLast("encoder", encoder);
        pipeline.addLast(protocolHandler);
    }
}
