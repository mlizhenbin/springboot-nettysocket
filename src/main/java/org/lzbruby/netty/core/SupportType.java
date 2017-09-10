package org.lzbruby.netty.core;

/**
 * 功能描述：Socket请求报文属性类型
 *
 * @author: lizhenbin
 * @email: lzbruby@163.com
 * @company: lzbruby.org
 * @date: 2017/8/10 time:8:53.
 */
public enum SupportType {

    /**
     * 报文头开始字节
     */
    MAGIC_HEADER((byte) 0x01),
    ;

    /**
     * 类型
     */
    private byte type;

    SupportType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return type;
    }
}
