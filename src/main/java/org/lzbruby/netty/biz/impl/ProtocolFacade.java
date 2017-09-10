package org.lzbruby.netty.biz.impl;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.lzbruby.netty.biz.ProtocolBiz;
import org.lzbruby.netty.biz.domain.ProtocolContext;

import java.util.Map;

/**
 * 功能描述：自定义协议业务转发器
 *
 * @author: Zhenbin.Li
 * email： lizhenbin08@sina.com
 * company：org.lzbruby
 * Date: 2017/9/10 Time：12:04
 */
public class ProtocolFacade implements ProtocolBiz {

    private Map<String, ProtocolBiz> protocolBizMap = Maps.newHashMap();

    public ProtocolBiz getProtocolBizMap(String tradeNo) {
        if (StringUtils.isBlank(tradeNo)) {
            throw new RuntimeException("Protocol trade no null.");
        }

        ProtocolBiz protocolBiz = protocolBizMap.get(tradeNo);
        if (protocolBiz == null) {
            throw new RuntimeException("Protocol biz config trade no null.");
        }

        return protocolBiz;
    }

    public void setProtocolBizMap(String tradeNo, ProtocolBiz protocolBiz) {
        this.protocolBizMap.put(tradeNo, protocolBiz);
    }

    @Override
    public void handle(ProtocolContext context) {
        getProtocolBizMap(context.getTradeNo()).handle(context);
    }

    @Override
    public void preHandle(ProtocolContext context) {
        getProtocolBizMap(context.getTradeNo()).preHandle(context);
    }

    @Override
    public void postHandle(ProtocolContext context) {
        getProtocolBizMap(context.getTradeNo()).postHandle(context);
    }

    @Override
    public void completeHandle(ProtocolContext context) {
        getProtocolBizMap(context.getTradeNo()).completeHandle(context);
    }
}
