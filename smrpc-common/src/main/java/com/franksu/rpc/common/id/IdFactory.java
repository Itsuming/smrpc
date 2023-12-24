package com.franksu.rpc.common.id;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.common.id
 * @Author: franksu
 * @CreateTime: 2023-12-24  14:58
 * @Description: ID工厂
 * @Version: 1.0
 */
public class IdFactory {
    private static final AtomicLong REQUEST_ID_GEN = new AtomicLong(0);

    public static Long getId() {
        return REQUEST_ID_GEN.incrementAndGet();
    }
}
