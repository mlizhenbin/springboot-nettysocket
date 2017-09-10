package org.lzbruby.netty.main;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * 功能描述：SpringBoot启动类
 *
 * @author: lizhenbin
 * @email: lzbruby@163.com
 * @company: lzbruby.org
 * @date: 2016/12/16 time:11:14.
 */
@Configuration
@ComponentScan(value = "org.lzbruby.netty")
@SpringBootApplication
public class Application {

    /**
     * sl4j
     */
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    /**
     * Application启动入口
     *
     * @param args
     */
    public static void main(String[] args) {
        if (ArrayUtils.isEmpty(args)) {
            String projectRootPath = System.getProperty("user.dir");
            String configPath = projectRootPath + File.separator + "src" + File.separator + "main" + File.separator
                    + "resources" + File.separator;

            // 本地开发测试，默认使用dev的配置文件
            String springConfigLocation = "--spring.config.location=" + configPath;
            String springProfilesActive = "--spring.profiles.active=dev";
            String loggingConfig = "--logging.config=" + configPath + "log4j2-dev.xml";

            // 本地配置设置到启动参数
            String[] localArgs = new String[]{springConfigLocation, springProfilesActive, loggingConfig};
            String[] mergeConfigArgs = ArrayUtils.addAll(localArgs, args);
            logger.debug("本地工程启动Application，启动配置参数，mergeConfigArgs={}", ArrayUtils.toString(mergeConfigArgs));
            SpringApplication.run(new Object[]{Application.class}, mergeConfigArgs);
        } else {
            logger.debug("本地工程启动Application，启动配置参数，localBootArgs={}", ArrayUtils.toString(args));
            SpringApplication.run(new Object[]{Application.class}, args);
        }
    }
}
