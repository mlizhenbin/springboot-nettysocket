package org.lzbruby.netty.biz.impl;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.lzbruby.netty.bean.SimpleProtocolReq;
import org.lzbruby.netty.bean.SimpleProtocolResp;
import org.lzbruby.netty.biz.ProtocolBiz;
import org.lzbruby.netty.biz.domain.ProtocolContext;
import org.lzbruby.netty.core.JacksonXmlSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 功能描述：简单的自定义业务实现
 *
 * @author: Zhenbin.Li
 * email： lizhenbin08@sina.com
 * company：org.lzbruby
 * Date: 2017/9/10 Time：11:56
 */
@Service("simpleProtocolBiz")
public class SimpleProtocolBiz implements ProtocolBiz<String, String> {

    /**
     * sl4j
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleProtocolBiz.class);

    @Override
    public void handle(ProtocolContext context) {
        preHandle(context);
        postHandle(context);
        completeHandle(context);
    }

    @Override
    public void preHandle(ProtocolContext<String, String> context) {
        String req = context.getReq();
        LOGGER.info("接受到请求报文, req={}", req);
        SimpleProtocolReq simpleProtocolReq = JacksonXmlSerializer.toBean(req, SimpleProtocolReq.class);
        LOGGER.info("接受到请求报文, simpleProtocolReq={}", ToStringBuilder.reflectionToString(simpleProtocolReq));

        context.addAttribute(ProtocolContext.ProtocolKey.simpleProtocolReq, simpleProtocolReq);
    }

    @Override
    public void postHandle(ProtocolContext<String, String> context) {
    }

    @Override
    public void completeHandle(ProtocolContext<String, String> context) {
        SimpleProtocolReq simpleProtocolReq = (SimpleProtocolReq) context.getAttribute(ProtocolContext.ProtocolKey.simpleProtocolReq);
        SimpleProtocolResp resp = simpleProtocolResp(simpleProtocolReq);
        String xml = JacksonXmlSerializer.toXML(resp);
        LOGGER.info("返回请求报文, resp={}", xml);

        context.setResp(xml);
    }

    private SimpleProtocolResp simpleProtocolResp(SimpleProtocolReq simpleProtocolReq) {
        SimpleProtocolResp resp = new SimpleProtocolResp();
        BeanUtils.copyProperties(simpleProtocolReq, resp);
        return resp;
    }
}
