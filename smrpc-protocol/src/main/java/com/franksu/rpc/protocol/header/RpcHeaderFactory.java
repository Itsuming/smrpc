package com.franksu.rpc.protocol.header;

import com.franksu.rpc.common.id.IdFactory;
import com.franksu.rpc.constants.RpcConstants;
import com.franksu.rpc.protocol.enumeration.RpcType;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.protocol.header
 * @Author: franksu
 * @CreateTime: 2023-12-24  14:53
 * @Description: 消息头工厂类
 * @Version: 1.0
 */
public class RpcHeaderFactory {
    public static RpcHeader getRequestHeader(String serializationType) {
        RpcHeader rpcHeader = new RpcHeader();
        Long requestId = IdFactory.getId();
        rpcHeader.setRequestId(requestId);
        rpcHeader.setMagic(RpcConstants.MAGIC);
        rpcHeader.setMsgType((byte) RpcType.REQUEST.getValue());
        rpcHeader.setStatus((byte) 0x1);
        rpcHeader.setSerializationType(serializationType);

        return rpcHeader;
    }
}
