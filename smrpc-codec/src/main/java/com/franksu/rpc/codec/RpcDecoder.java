package com.franksu.rpc.codec;

import com.franksu.rpc.common.utils.SerializationUtils;
import com.franksu.rpc.constants.RpcConstants;
import com.franksu.rpc.protocol.RpcProtocol;
import com.franksu.rpc.protocol.enumeration.RpcType;
import com.franksu.rpc.protocol.header.RpcHeader;
import com.franksu.rpc.protocol.request.RpcRequest;
import com.franksu.rpc.protocol.response.RpcResponse;
import com.franksu.rpc.serialization.api.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.codec
 * @Author: franksu
 * @CreateTime: 2024-01-06  14:44
 * @Description: RPC解码器
 * @Version: 1.0
 */
public class RpcDecoder extends ByteToMessageDecoder implements RpcCodec {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < RpcConstants.HEADER_TOTAL_LEN) {
            return;
        }

        in.markReaderIndex();

        short magic = in.readShort();
        if (magic != RpcConstants.MAGIC) {
            throw new IllegalArgumentException("magic number is illegal, " + magic);
        }

        byte msgType = in.readByte();
        byte status = in.readByte();
        long requestId = in.readLong();

        ByteBuf serializationTypeByteBuf = in.readBytes(SerializationUtils.MAX_SERIALIZATION_TYPE_COUNT);
        String serializationType = SerializationUtils.removeBlank(serializationTypeByteBuf.toString(CharsetUtil.UTF_8));

        int dataLen = in.readInt();
        if (in.readableBytes() < dataLen) {
            in.resetReaderIndex();
            return;
        }

        byte[] data = new byte[dataLen];
        in.readBytes(data);

        RpcType msgTypeEnum = RpcType.findByType(msgType);
        if (msgTypeEnum == null) {
            return;
        }

        RpcHeader rpcHeader = new RpcHeader();
        rpcHeader.setMagic(magic);
        rpcHeader.setStatus(status);
        rpcHeader.setRequestId(requestId);
        rpcHeader.setMsgType(msgType);
        rpcHeader.setSerializationType(serializationType);
        rpcHeader.setMsgLen(dataLen);

        // Serialization扩展点
        Serialization serialization = getJdkSerialization();
        switch (msgTypeEnum) {
            case REQUEST:
                RpcRequest request = serialization.deserialize(data, RpcRequest.class);
                if (request != null) {
                    RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
                    protocol.setRpcHeader(rpcHeader);
                    protocol.setBody(request);
                    out.add(protocol);
                }
                break;
            case RESPONSE:
                RpcResponse response = serialization.deserialize(data, RpcResponse.class);
                if (response != null) {
                    RpcProtocol<RpcResponse> protocol = new RpcProtocol<>();
                    protocol.setRpcHeader(rpcHeader);
                    protocol.setBody(response);
                    out.add(protocol);
                }
                break;
            case HEARTBEAT:
                break;
        }

    }
}
