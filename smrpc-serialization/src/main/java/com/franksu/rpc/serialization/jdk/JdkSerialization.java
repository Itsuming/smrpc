package com.franksu.rpc.serialization.jdk;

import com.franksu.rpc.common.exception.SerializerException;
import com.franksu.rpc.serialization.api.Serialization;

import java.io.*;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.serialization.jdk
 * @Author: franksu
 * @CreateTime: 2024-01-06  14:10
 * @Description: jdkSerialization实现类
 * @Version: 1.0
 */
public class JdkSerialization implements Serialization {
    @Override
    public <T> byte[] serialize(T obj) {
        if (obj == null) {
            throw new SerializerException("Serialize object is null");
        }
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(os);
            out.writeObject(obj);
            return os.toByteArray();
        } catch (IOException e) {
            throw new SerializerException(e.getMessage(), e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        if (data == null) {
            throw new SerializerException("deserialize data is null");
        }
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(is);
            return (T) in.readObject();
        } catch (Exception e) {
            throw new SerializerException(e.getMessage(), e);
        }
    }
}
