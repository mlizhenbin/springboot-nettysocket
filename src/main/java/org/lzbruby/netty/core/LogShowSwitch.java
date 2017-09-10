package org.lzbruby.netty.core;

/**
 * 功能描述：是否开启打印日志开关
 *
 * @author: lizhenbin
 * @email: lzbruby@163.com
 * @company: lzbruby.org
 * @date: 2017/8/10 time:8:58.
 */
public enum LogShowSwitch {

    /**
     * 打印开启标志
     */
    LOG_ON("ON"),

    /**
     * 打印关闭标志
     */
    LOG_OFF("OFF");

    private String flag;

    LogShowSwitch(String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }
}
