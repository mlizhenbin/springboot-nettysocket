package org.lzbruby.netty.bean;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 功能描述：请求对象头
 *
 * @author: Zhenbin.Li
 * email： lizhenbin08@sina.com
 * company：org.lzbruby
 * Date: 2017/9/10 Time：11:57
 */
public class ProtocolReqHeader implements Serializable {
    private static final long serialVersionUID = -6038713675913162955L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
