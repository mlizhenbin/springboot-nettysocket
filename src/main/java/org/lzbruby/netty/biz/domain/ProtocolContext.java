package org.lzbruby.netty.biz.domain;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;

/**
 * 功能描述：自定义协议处理上下文
 *
 * @author: Zhenbin.Li
 * email： lizhenbin08@sina.com
 * company：org.lzbruby
 * Date: 2017/9/10 Time：11:57
 */
public class ProtocolContext<E, V> implements Serializable {
    private static final long serialVersionUID = 3125712238067587777L;

    /**
     * 交易码
     */
    private String tradeNo;

    /**
     * 请求报文
     */
    private E req;

    /**
     * 响应报文
     */
    private V resp;

    private Map<ProtocolKey, Object> session = Maps.newLinkedHashMap();

    public ProtocolContext(String tradeNo, E req) {
        this.tradeNo = tradeNo;
        this.req = req;
    }

    public <T> T getAttribute(ProtocolKey key) {
        return (T) session.get(key);
    }

    public <T> void addAttribute(ProtocolKey key, T val) {
        this.session.put(key,val);
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public E getReq() {
        return req;
    }

    public V getResp() {
        return resp;
    }

    public void setResp(V resp) {
        this.resp = resp;
    }

    /**
     * 上下文KEY枚举
     */
    public enum ProtocolKey {
        simpleProtocolReq
    }
}
