package org.lzbruby.netty.core;

import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import io.netty.channel.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.lzbruby.netty.biz.ProtocolBiz;
import org.lzbruby.netty.biz.domain.ProtocolContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

/**
 * 功能描述：自定义通讯处理器
 *
 * @author: lizhenbin
 * @email: lzbruby@163.com
 * @company: lzbruby.org
 * @date: 2017/7/10 time:15:11.
 */
@Component
@ChannelHandler.Sharable
public class ProtocolHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(ProtocolHandler.class);

    @Autowired
    private SocketServerProperties properties;

    @Autowired
    private ProtocolBiz protocolFacade;

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, Object obj) throws Exception {
        Channel channel = ctx.channel();
        if (!(obj instanceof ProtocolMsg)) {
            logger.debug("客户端请求非协议报文ProtocolMsg类型，服务端不做请求处理！obj={}", ToStringBuilder.reflectionToString(obj));
            return;
        }

        ProtocolMsg msg = (ProtocolMsg) obj;
        ProtocolHeader header = msg.getHeader();
        byte magicType = header.getMagicType();
        String flag = properties.getBodyFlag();
        if (StringUtils.equals(flag, LogShowSwitch.LOG_ON.getFlag()) && magicType != ProtocolMagicType.HEARTBEAT.getType()) {
            logger.info("服务端接收到Client请求head={}, remoteAddress={}", header, channel.remoteAddress());
            logger.info("服务端接收到Client请求body={}", msg.getBody());
        }

        // 校验报文头是否0x01
        Predicate<ProtocolHeader> predicate = new Predicate<ProtocolHeader>() {
            @Override
            public boolean apply(@Nullable ProtocolHeader input) {
                return input.getMagic() == SupportType.MAGIC_HEADER.getType();
            }
        };
        if (!predicate.apply(header)) {
            logger.error("Client request Magic type error! ProtocolHeader={}", header);
            channel.writeAndFlush("client request Magic type error!");
            return;
        }

        // 处理业务逻辑
        if (magicType != ProtocolMagicType.HEARTBEAT.getType()) {
            String tradeNo = new String(header.getTradeNo(), Charsets.US_ASCII);
            ProtocolContext<String, String> protocolContext
                    = new ProtocolContext<String, String>(tradeNo, msg.getBody());
            protocolFacade.handle(protocolContext);

            String resp = protocolContext.getResp();
            msg.setBody(resp);
        }

        if (StringUtils.equals(flag, LogShowSwitch.LOG_ON.getFlag()) && magicType != ProtocolMagicType.HEARTBEAT.getType()) {
            logger.info("服务端响应Client报文head={}", header);
            logger.info("服务端响应Client报文body={}", msg);
        }

        channel.writeAndFlush(msg).addListener(ChannelFutureListener.CLOSE);
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.warn("Client断开Socket通讯异常，关闭Server！", cause);
        ctx.close();
    }
}
