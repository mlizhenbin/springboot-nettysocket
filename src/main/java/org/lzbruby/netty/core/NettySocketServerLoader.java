package org.lzbruby.netty.core;

import org.lzbruby.netty.core.heartbeat.HeartbeatListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 功能描述：Spring容器加载完成，启动Netty Server
 *
 * @author: lizhenbin
 * @email: lzbruby@163.com
 * @company: lzbruby.org
 * @date: 2017/7/17 time:11:16.
 */
@Component
public class NettySocketServerLoader implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(NettySocketServerLoader.class);

    @Autowired
    private NettySocketServer nettySocketServer;

    @Autowired
    private HeartbeatListener heartbeatListener;

    @Override
    public void afterPropertiesSet() throws Exception {

        // 启动netty
        nettySocketServer.loadServer();

        // 启动Netty心跳检测服务
        logger.info("spring application context init finish. start netty socket server listener.");
        heartbeatListener.listen();
    }
}
