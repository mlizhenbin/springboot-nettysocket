package org.lzbruby.netty.biz;

import org.lzbruby.netty.biz.impl.ProtocolFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 功能描述：自定义协议配置
 *
 * @author: Zhenbin.Li
 * email： lizhenbin08@sina.com
 * company：org.lzbruby
 * Date: 2017/9/10 Time：12:15
 */
@SpringBootConfiguration
public class ProtocolBizConfig {

    @Autowired
    private ProtocolBiz simpleProtocolBiz;

    @Bean(name = "protocolFacade")
    public ProtocolFacade protocolFacade() {
        ProtocolFacade facade = new ProtocolFacade();
        facade.setProtocolBizMap("PROB0001", simpleProtocolBiz);

        return facade;
    }
}
