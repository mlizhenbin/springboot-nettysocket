package org.lzbruby.netty.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * 功能描述：采用Jackson序列化XML
 *
 * @author: Zhenbin.Li
 * email： lizhenbin08@sina.com
 * company：org.lzbruby
 * Date: 2017/8/21 Time：22:45
 */
public class JacksonXmlSerializer {

    /**
     * XML MAPPER
     */
    private static XmlMapper xmlMapper = null;

    /**
     * 私有构造
     */
    private JacksonXmlSerializer() {

    }

    /**
     * XML序列化JAVA BEAN
     *
     * @param xml
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toBean(String xml, Class<T> clazz) {
        if (StringUtils.isBlank(xml) || clazz == null) {
            return null;
        }

        XmlMapper xmlMapper = getXmlMapper();
        try {
            T bean = xmlMapper.readValue(xml, clazz);
            return bean;
        } catch (IOException e) {
            throw new RuntimeException("XML反序列化JAVA BEAN异常.", e);
        }
    }

    /**
     * JAVA BEAN反序列化XML
     *
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> String toXML(T bean) {
        if (bean == null) {
            return null;
        }

        XmlMapper xmlMapper = getXmlMapper();
        xmlMapper.enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION);
//        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            String xml = xmlMapper.writeValueAsString(bean);
            return xml;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JAVA BEAN序列化XML异常.", e);
        }
    }

    public static XmlMapper getXmlMapper() {
        if (xmlMapper == null) {
            xmlMapper = new XmlMapper();
            xmlMapper.getSerializerProvider().setNullValueSerializer(new ProtocolNullSerializer());
        }

        return xmlMapper;
    }

    /**
     * JAVABEAN NULL值输出节点
     */
    private static class ProtocolNullSerializer extends StdSerializer<Object> {

        private ProtocolNullSerializer() {
            super(Object.class);
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString("");
        }
    }
}
