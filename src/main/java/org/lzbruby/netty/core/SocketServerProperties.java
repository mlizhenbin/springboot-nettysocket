package org.lzbruby.netty.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 功能描述：Netty Server配置
 *
 * @author: Zhenbin.Li
 * email： lizhenbin08@sina.com
 * company：org.lzbruby
 * Date: 2017/7/22 Time：14:48
 */
@Configuration
@ConfigurationProperties(prefix = "socket.server")
public class SocketServerProperties {

    /**
     * 字符集
     */
    private String charset;

    /**
     * 自定义协议端口号
     */
    private int port;

    /**
     * 最大报文长度
     */
    private int maxFrameLength;

    /**
     * bodySize之前占用字节数
     */
    private int lengthFieldOffset;

    /**
     * bodySize占用字节数
     */
    private int lengthFieldLength;

    /**
     * 偏移量
     */
    private int lengthAdjustment;

    /**
     * 跳过读取字节数
     */
    private int initialBytesToStrip;

    /**
     * 建立了3次TCP握手等待处理连接池
     */
    private int soBacklog;

    /**
     * 超时时间
     */
    private int connectTimeoutMillis;

    /**
     * 开启Socket日志标志
     */
    private String socketLog;

    /**
     * 开启报文打印标志
     */
    private String bodyFlag;

    /**
     * 监控时间
     */
    private int monitorTime;

    /**
     * boss线程梳理
     */
    private int bossGroup;

    /**
     * 工作线程梳理
     */
    private int workGroup;

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxFrameLength() {
        return maxFrameLength;
    }

    public void setMaxFrameLength(int maxFrameLength) {
        this.maxFrameLength = maxFrameLength;
    }

    public int getLengthFieldOffset() {
        return lengthFieldOffset;
    }

    public void setLengthFieldOffset(int lengthFieldOffset) {
        this.lengthFieldOffset = lengthFieldOffset;
    }

    public int getLengthFieldLength() {
        return lengthFieldLength;
    }

    public void setLengthFieldLength(int lengthFieldLength) {
        this.lengthFieldLength = lengthFieldLength;
    }

    public int getLengthAdjustment() {
        return lengthAdjustment;
    }

    public void setLengthAdjustment(int lengthAdjustment) {
        this.lengthAdjustment = lengthAdjustment;
    }

    public int getInitialBytesToStrip() {
        return initialBytesToStrip;
    }

    public void setInitialBytesToStrip(int initialBytesToStrip) {
        this.initialBytesToStrip = initialBytesToStrip;
    }

    public int getSoBacklog() {
        return soBacklog;
    }

    public void setSoBacklog(int soBacklog) {
        this.soBacklog = soBacklog;
    }

    public int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public void setConnectTimeoutMillis(int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    public String getSocketLog() {
        return socketLog;
    }

    public void setSocketLog(String socketLog) {
        this.socketLog = socketLog;
    }

    public String getBodyFlag() {
        return bodyFlag;
    }

    public void setBodyFlag(String bodyFlag) {
        this.bodyFlag = bodyFlag;
    }

    public int getMonitorTime() {
        return monitorTime;
    }

    public void setMonitorTime(int monitorTime) {
        this.monitorTime = monitorTime;
    }

    public int getBossGroup() {
        return bossGroup;
    }

    public void setBossGroup(int bossGroup) {
        this.bossGroup = bossGroup;
    }

    public int getWorkGroup() {
        return workGroup;
    }

    public void setWorkGroup(int workGroup) {
        this.workGroup = workGroup;
    }
}
