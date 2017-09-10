package org.lzbruby.netty.core;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 功能描述：自定义通讯报文对象
 *
 * @author: lizhenbin
 * @email: lzbruby@163.com
 * @company: lzbruby.org
 * @date: 2017/7/20 time:14:39.
 */
public class ProtocolMsg implements Serializable {
    private static final long serialVersionUID = -3979815844186162407L;

    /**
     * 报文头
     */
    private ProtocolHeader header;

    /**
     * 报文体
     */
    private String body;

    public ProtocolHeader getHeader() {
        return header;
    }

    public void setHeader(ProtocolHeader header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
