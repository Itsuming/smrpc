package com.franksu.rpc.provider.common.handler;

import com.alibaba.fastjson.JSONObject;
import com.franksu.rpc.common.helper.RpcServiceHelper;
import com.franksu.rpc.common.threadPool.ServerThreadPool;
import com.franksu.rpc.constants.RpcConstants;
import com.franksu.rpc.protocol.RpcProtocol;
import com.franksu.rpc.protocol.enumeration.RpcStatus;
import com.franksu.rpc.protocol.enumeration.RpcType;
import com.franksu.rpc.protocol.header.RpcHeader;
import com.franksu.rpc.protocol.request.RpcRequest;
import com.franksu.rpc.protocol.response.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.rpc.provider.common.handler
 * @Author: franksu
 * @CreateTime: 2023-12-24  12:55
 * @Description: 服务提供者消息处理
 * @Version: 1.0
 */
public class RpcProviderHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
    private final Logger logger = LoggerFactory.getLogger(RpcProviderHandler.class);

    private final Map<String, Object> handlerMap;
    // 调用真实方法所采用类型
    private final String reflectType;

    public RpcProviderHandler(Map<String, Object> handlerMap, String reflectType) {
        this.handlerMap = handlerMap;
        this.reflectType = reflectType;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> protocol) throws Exception {
        // logger.info("RPC提供者收到的数据为===>>> {}", JSONObject.toJSONString(protocol));
        // logger.info("handlerMap中存放的数据如下所示：");
        // for (Map.Entry<String, Object> entry : handlerMap.entrySet()) {
        //     logger.info(entry.getKey() + " === " + entry.getValue());
        // }

        ServerThreadPool.submit(() -> {
            RpcHeader rpcHeader = protocol.getRpcHeader();
            rpcHeader.setMsgType((byte) RpcType.RESPONSE.getType());
            RpcRequest request = protocol.getBody();
            logger.debug("Receive request: " + rpcHeader.getRequestId());
            // 创建RpcResponse
            RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<>();
            RpcResponse rpcResponse = new RpcResponse();
            try {
                Object result = handle(request);
                rpcResponse.setResult(result);
                rpcResponse.setAsync(request.isAsync());
                rpcResponse.setOneway(request.isOneway());
                rpcHeader.setStatus((byte) RpcStatus.SUCCESS.getCode());
            } catch (Throwable throwable) {
                rpcResponse.setError(throwable.toString());
                rpcHeader.setStatus((byte) RpcStatus.FAIL.getCode());
                logger.error("RPC Server handle request error", throwable);
            }
            responseRpcProtocol.setRpcHeader(rpcHeader);
            responseRpcProtocol.setBody(rpcResponse);
            ctx.writeAndFlush(responseRpcProtocol).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    logger.debug("Send response for request " + rpcHeader.getRequestId());
                }
            });
        });
    }

    /**
     * 将request中的参数拼接处理，从而从map中获取类实例
     *
     * @param request RpcRequest
     * @return
     */
    private Object handle(RpcRequest request) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getVersion()
                , request.getGroup());
        Object serviceBean = handlerMap.get(serviceKey);
        if (serviceBean == null) {
            throw new RuntimeException(String.format("service not exist: %s:%s", request.getClassName(), request.getMethodName()));
        }

        // 通过反射获取bean
        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        logger.debug(serviceClass.getName());
        logger.debug(methodName);

        if (parameterTypes != null && parameterTypes.length > 0) {
            for (int i = 0; i < parameterTypes.length; ++i) {
                logger.debug(parameterTypes[i].getName());
            }
        }
        if (parameters != null && parameters.length > 0) {
            for (int i = 0; i < parameters.length; ++i) {
                logger.debug(parameters[i].toString());
            }
        }

        return invokeMethod(serviceBean, serviceClass, methodName, parameterTypes, parameters);
    }

    public Object invokeMethod(Object serviceBean, Class<?> serviceClass, String methodName,
                               Class<?>[] parameterTypes, Object[] parameters) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        switch (this.reflectType) {
            case RpcConstants.REFLECT_TYPE_JDK:
                return this.invokeJDKMethod(serviceBean, serviceClass, methodName, parameterTypes, parameters);
            case RpcConstants.REFLECT_TYPE_CGLIB:
                return this.invokeCGLibMethod(serviceBean, serviceClass, methodName, parameterTypes, parameters);
            default:
                throw new IllegalArgumentException("not support reflect type");

        }

    }


    private Object invokeJDKMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // JDK reflect
        logger.info("use jdk reflect type invoke method...");
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);
    }

    private Object invokeCGLibMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws InvocationTargetException {
        logger.info("use CGLib reflect type invoke method...");
        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastClassMethod = serviceFastClass.getMethod(methodName, parameterTypes);

        return serviceFastClassMethod.invoke(serviceBean, parameters);
    }

}
