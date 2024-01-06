package com.franksu.rpc.serialization.api;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.serialization.api
 * @Author: franksu
 * @CreateTime: 2024-01-06  13:58
 * @Description: Serialization接口
 * @Version: 1.0
 */
public interface Serialization {

    /**
     * 序列化
     */
    <T> byte[] serialize(T obj);

    /**
     * 反序列化
     */
    <T> T deserialize(byte[] data, Class<T> cls);
}
