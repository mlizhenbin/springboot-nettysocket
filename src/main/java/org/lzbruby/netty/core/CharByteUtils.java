package org.lzbruby.netty.core;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * 功能描述：JAVA char与byte转换工具类
 *
 * @author: lizhenbin
 * @email: lzbruby@163.com
 * @company: lzbruby.org
 * @date: 2017/7/21 time:14:36.
 */
public class CharByteUtils {

    private CharByteUtils() {

    }

    /**
     * 字符串转换为byte数组
     *
     * @param chars
     * @param charset
     * @return
     */
    public static byte[] getBytes(char[] chars, Charset charset) {
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = charset.encode(cb);
        return bb.array();
    }

    /**
     * byte数组转换为字符串
     *
     * @param bytes
     * @param charset
     * @return
     */
    public static char[] getChars(byte[] bytes, Charset charset) {
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        CharBuffer cb = charset.decode(bb);

        return cb.array();
    }

}
