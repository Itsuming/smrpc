package com.franksu.rpc.common.exception;

import java.io.Serializable;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.common.exception
 * @Author: franksu
 * @CreateTime: 2024-01-06  13:03
 * @Description: 序列化异常类
 * @Version: 1.0
 */
public class SerializerException extends RuntimeException{
    private static final long serialVersionUID = -5257910967813160917L;

    public SerializerException(final Throwable e) {
        super(e);
    }

    public SerializerException(final String message) {
        super(message);
    }
    public SerializerException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
