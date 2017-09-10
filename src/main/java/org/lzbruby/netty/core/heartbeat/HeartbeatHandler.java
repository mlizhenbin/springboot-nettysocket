package org.lzbruby.netty.core.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.lzbruby.netty.core.ProtocolMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能描述：心跳检测处理器
 *
 * @author: lizhenbin
 * @email: lzbruby@163.com
 * @company: lzbruby.org
 * @date: 2017/7/10 time:15:11.
 */
public class HeartbeatHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class);

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, Object obj) throws Exception {
        try {
            if (obj instanceof ProtocolMsg) {
                ProtocolMsg msg = (ProtocolMsg) obj;
                logger.info("Client Receive Server Heartbeat Msg = {}", msg.getBody());
            }
        } finally {
            ReferenceCountUtil.release(obj);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常就关闭连接
        logger.warn("Client断开Socket通讯异常，关闭Client！", cause);
        ctx.close();
    }
}
