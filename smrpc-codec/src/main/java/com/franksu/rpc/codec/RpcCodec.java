package com.franksu.rpc.codec;

import com.franksu.rpc.serialization.api.Serialization;
import com.franksu.rpc.serialization.jdk.JdkSerialization;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.codec
 * @Author: franksu
 * @CreateTime: 2024-01-06  14:23
 * @Description: codec
 * @Version: 1.0
 */
public interface RpcCodec {
    default Serialization getJdkSerialization() {
        return new JdkSerialization();
    }
}
