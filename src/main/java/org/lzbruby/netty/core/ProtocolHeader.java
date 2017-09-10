package org.lzbruby.netty.core;

import com.google.common.base.Charsets;

import java.io.Serializable;

/**
 * 功能描述：自定义报文头对象
 * <p/>
 * 报文头约定格式：魔数(1个字节)+请求类型(1个字节)+渠道(4个字节)+交易码（8个字节）+报文体长度(4个字节)
 *
 * @author: lizhenbin
 * @email: lzbruby@163.com
 * @company: lzbruby.org
 * @date: 2017/7/20 time:14:41.
 */
public class ProtocolHeader implements Serializable {
    private static final long serialVersionUID = 3570420676267282584L;

    /**
     * 魔数，报文头开始字节(1个字节)
     */
    private byte magic;

    /**
     * 报文请求类型,（1个字节） 0x00:心跳检测
     */
    private byte magicType;

    /**
     * 渠道（4个字节）
     */
    private int channel;

    /**
     * 交易码(8个字节)
     */
    private byte[] tradeNo = new byte[8];

    /**
     * 报文体长度（4个字节）
     */
    private int bodySize;

    public ProtocolHeader() {
    }

    public ProtocolHeader(byte magic, byte magicType, int channel, byte[] tradeNo, int bodySize) {
        this.magic = magic;
        this.magicType = magicType;
        this.channel = channel;
        this.tradeNo = tradeNo;
        this.bodySize = bodySize;
    }

    public byte getMagic() {
        return magic;
    }

    public void setMagic(byte magic) {
        this.magic = magic;
    }

    public byte getMagicType() {
        return magicType;
    }

    public void setMagicType(byte magicType) {
        this.magicType = magicType;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public byte[] getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(byte[] tradeNo) {
        this.tradeNo = tradeNo;
    }

    public int getBodySize() {
        return bodySize;
    }

    public void setBodySize(int bodySize) {
        this.bodySize = bodySize;
    }

    @Override
    public String toString() {
        return "[magic=" + magic + ", magicType=" + magicType + ", channel=" + channel
                + ", tradeNo=" + new String(tradeNo, Charsets.US_ASCII) + ", bodySize=" + bodySize + "]";
    }
}
