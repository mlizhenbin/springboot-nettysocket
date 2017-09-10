package org.lzbruby.netty.core;

/**
 * 功能描述：自定义协议报文类型
 *
 * @author: Zhenbin.Li
 * email： lizhenbin08@sina.com
 * company：org.lzbruby
 * Date: 2017/8/12 Time：21:48
 */
public enum ProtocolMagicType {

    /**
     * 心跳检测
     */
    HEARTBEAT((byte) 0x01),

    /**
     * XML序列化
     */
    XML_SERIAL((byte) 0x02),;

    private byte type;

    ProtocolMagicType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }
}
