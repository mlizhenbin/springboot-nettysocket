package org.lzbruby.netty.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;

import java.nio.ByteOrder;

/**
 * 功能描述：接收请求Decoder
 * 所有ByteBuf的对象都需要手动回收内存
 *
 * @author: lizhenbin
 * @email: lzbruby@163.com
 * @company: lzbruby.org
 * @date: 2017/7/20 time:15:25.
 */
public class ProtocolDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * 交易码占用字节数
     */
    public static final int AUTHORIZE_TRADE_NO_LEN = 8;

    /**
     * 报文头长度：魔数(1个字节)+请求类型(1个字节)+渠道(4个字节)+交易码（8个字节+标志位（1字节）+报文体长度(4个字节)
     */
    public static final int AUTHORIZE_PROTOCOL_HEAD_LEN = 19;


    /**
     * 字符集
     */
    private final String charset;

    public ProtocolDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, String charset) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        this.charset = charset;
    }

    public ProtocolDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, String charset) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
        this.charset = charset;
    }

    public ProtocolDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast, String charset) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
        this.charset = charset;
    }

    public ProtocolDecoder(ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast, String charset) {
        super(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
        this.charset = charset;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in2) throws Exception {
        ByteBuf in = (ByteBuf) super.decode(ctx, in2);
        try {
            if (in == null) {
                return null;
            }

            if (in.readableBytes() < AUTHORIZE_PROTOCOL_HEAD_LEN) {
                return null;
            }

            // 读取报文头
            byte magic = in.readByte();
            byte magicType = in.readByte();
            int channel = in.readInt();

            byte[] tradeNo = new byte[AUTHORIZE_TRADE_NO_LEN];
            for (int i = 0; i < tradeNo.length; i++) {
                tradeNo[i] = in.readByte();
            }

            byte flag = in.readByte();
            int bodySize = in.readInt();

            // 读取报文体，转换为字符串
            ByteBuf buf = in.readBytes(bodySize);
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            String body = new String(req, charset);

            ProtocolHeader header = new ProtocolHeader(magic, magicType, channel, tradeNo, flag, bodySize);
            ProtocolMsg msg = new ProtocolMsg();
            msg.setHeader(header);
            msg.setBody(body);

            // 回收内存
            ReferenceCountUtil.release(buf);

            return msg;
        } finally {
            // 回收内存
            ReferenceCountUtil.release(in);
        }

    }
}
