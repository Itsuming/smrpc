package com.franksu.rpc.codec;

import com.franksu.rpc.common.utils.SerializationUtils;
import com.franksu.rpc.protocol.RpcProtocol;
import com.franksu.rpc.protocol.header.RpcHeader;
import com.franksu.rpc.serialization.api.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.codec
 * @Author: franksu
 * @CreateTime: 2024-01-06  14:30
 * @Description: RPC编码器
 * @Version: 1.0
 */
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol<Object>> implements RpcCodec{

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocol<Object> msg, ByteBuf byteBuf) throws Exception {

        RpcHeader header = msg.getRpcHeader();
        byteBuf.writeShort(header.getMagic());
        byteBuf.writeByte(header.getMsgType());
        byteBuf.writeByte(header.getStatus());
        byteBuf.writeLong(header.getRequestId());

        String serializationType = header.getSerializationType();
        // serialization是扩展点
        Serialization serialization = getJdkSerialization();
        byteBuf.writeBytes(SerializationUtils.paddingString(serializationType).getBytes("UTF-8"));

        byte[] data = serialization.serialize(msg.getBody());
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);
    }
}
