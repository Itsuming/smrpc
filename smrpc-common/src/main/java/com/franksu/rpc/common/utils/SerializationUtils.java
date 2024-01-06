package com.franksu.rpc.common.utils;

import java.util.stream.IntStream;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.common.utils
 * @Author: franksu
 * @CreateTime: 2024-01-06  13:40
 * @Description: 序列化工具类
 * @Version: 1.0
 */
public class SerializationUtils {

    private static final String PADDING_STRING = "0";
    /**
     * 约定序列化类型最大长度为16
     */
    public static final int MAX_SERIALIZATION_TYPE_COUNT = 16;

    /**
     * 长度不足16字符串后面补0
     * @param str 原始字符串
     * @return 补零后字符串
     */
    public static String paddingString(String str) {
        str = transNullToEmpty(str);
        if (str.length() >= MAX_SERIALIZATION_TYPE_COUNT) {
            return str;
        }
        StringBuilder paddingStr = new StringBuilder(str);
        int paddingCount = str.length() - MAX_SERIALIZATION_TYPE_COUNT;
        IntStream.range(0, paddingCount).forEach(i ->{
            paddingStr.append(PADDING_STRING);
        });

        return paddingStr.toString();
    }

    /**
     * 字符串去0操作
     * @param str 原始字符串
     * @return 去0后字符串
     */
    public static String removeBlank(String str) {
        str = transNullToEmpty(str);
        return str.replace(PADDING_STRING, "");
    }

    /**
     * null转化为空串
     * @param str 原始串
     * @return 转化后串
     */
    public static String transNullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
