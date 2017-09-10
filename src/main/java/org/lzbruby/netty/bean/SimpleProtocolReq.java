package org.lzbruby.netty.bean;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 功能描述：请求报文体
 *
 * @author: lizhenbin
 * @email: lzbruby@163.com
 * @company: lzbruby.org
 * @date: 2017/7/31 time:11:41.
 */
@JacksonXmlRootElement(localName = "ROOT")
public class SimpleProtocolReq extends ProtocolReqHeader implements Serializable {
    private static final long serialVersionUID = -3946116138751006027L;

    /**
     * 系统ID
     */
    @JacksonXmlProperty(localName = "SYSTEM-ID")
    private String sysId;

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
