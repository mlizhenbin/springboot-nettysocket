package org.lzbruby.netty.biz;

import org.lzbruby.netty.biz.domain.ProtocolContext;

/**
 * 功能描述：自定义协议业务处理接口
 *
 * @author: Zhenbin.Li
 * email： lizhenbin08@sina.com
 * company：org.lzbruby
 * Date: 2017/9/10 Time：11:55
 */
public interface ProtocolBiz<E, V> {

    /**
     * 执行处理业务
     *
     * @param context
     */
    public void handle(ProtocolContext<E, V> context);

    /**
     * 预处理业务
     *
     * @param context
     */
    public void preHandle(ProtocolContext<E, V> context);

    /**
     * 提交执行处理业务
     *
     * @param context
     */
    public void postHandle(ProtocolContext<E, V> context);

    /**
     * 完成处理业务
     *
     * @param context
     */
    public void completeHandle(ProtocolContext<E, V> context);

}
