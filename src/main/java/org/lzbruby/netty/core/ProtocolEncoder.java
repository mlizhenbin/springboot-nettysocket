package org.lzbruby.netty.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 功能描述：接收请求Encoder
 *
 * @author: lizhenbin
 * @email: lzbruby@163.com
 * @company: lzbruby.org
 * @date: 2017/7/20 time:15:23.
 */
public class ProtocolEncoder extends MessageToByteEncoder<ProtocolMsg> {

    /**
     * 字符集
     */
    private final String charset;

    public ProtocolEncoder(String charset) {
        this.charset = charset;
    }

    public ProtocolEncoder(Class<? extends ProtocolMsg> outboundMessageType, String charset) {
        super(outboundMessageType);
        this.charset = charset;
    }

    public ProtocolEncoder(boolean preferDirect, String charset) {
        super(preferDirect);
        this.charset = charset;
    }

    public ProtocolEncoder(Class<? extends ProtocolMsg> outboundMessageType, boolean preferDirect, String charset) {
        super(outboundMessageType, preferDirect);
        this.charset = charset;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ProtocolMsg msg, ByteBuf out) throws Exception {
        if (msg == null || msg.getHeader() == null) {
            throw new Exception("The encode message is null");
        }

        ProtocolHeader header = msg.getHeader();
        String body = msg.getBody();
        byte[] bodyBytes = body.getBytes(charset);
        int bodySize = bodyBytes.length;

        // 报文头
        out.writeByte(header.getMagic());
        out.writeByte(header.getMagicType());
        out.writeInt(header.getChannel());
        out.writeBytes(header.getTradeNo());
        out.writeByte(header.getFlag());
        out.writeInt(bodySize);

        // 报文体
        out.writeBytes(bodyBytes);
    }
}
