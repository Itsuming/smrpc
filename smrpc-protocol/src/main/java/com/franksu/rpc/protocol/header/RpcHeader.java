package com.franksu.rpc.protocol.header;

import java.io.Serializable;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.protocol.header
 * @Author: franksu
 * @CreateTime: 2023-12-24  14:47
 * @Description: 消息头类
 * @Version: 1.0
 */
public class RpcHeader implements Serializable {
    private static final long serialVersionUID = 12312412412L;
    /*
    +---------------------------------------------------------------+
    | 魔数 2byte | 报文类型 1byte | 状态 1byte |     消息 ID 8byte      |
    +---------------------------------------------------------------+
    |           序列化类型 16byte      |        数据长度 4byte          |
    +---------------------------------------------------------------+
    */

    /**
     * 魔数 2字节
     */
    private short magic;
    /**
     * 报文类型 1字节
     */
    private byte msgType;
    /**
     * 状态 1字节
     */
    private byte status;
    /**
     * 消息ID 8字节
     */
    private long requestId;
    /**
     * 序列化类型 16字节，不足补零，约定不超过16字节
     */
    private String serializationType;
    /**
     * 消息长度 4字节
     */
    private int msgLen;

    public short getMagic() {
        return magic;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getSerializationType() {
        return serializationType;
    }

    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
    }

    public int getMsgLen() {
        return msgLen;
    }

    public void setMsgLen(int msgLen) {
        this.msgLen = msgLen;
    }
}
