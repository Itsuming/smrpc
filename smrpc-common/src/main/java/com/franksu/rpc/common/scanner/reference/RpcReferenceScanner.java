package com.franksu.rpc.common.scanner.reference;

import com.franksu.rpc.annotation.RpcReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.franksu.rpc.common.scanner.ClassScanner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


/**
 * @BelongsProject: smrpc
 * @BelongsPackage: scanner.reference
 * @Author: franksu
 * @CreateTime: 2023-12-23  14:41
 * @Description: @RpcReference注解扫描器
 * @Version: 1.0
 */
public class RpcReferenceScanner extends ClassScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcReferenceScanner.class);

    public static Map<String, Object> doScannerWithRpcReferenceAnnotationFilter(/*String host, int port,*/ String scanPackage/*, Registry*/) throws IOException {
        Map<String, Object> handlerMap = new HashMap<>();
        List<String> classNameList = getClassNameList(scanPackage);
        if (classNameList == null || classNameList.isEmpty()) {
            return handlerMap;
        }
        classNameList.stream().forEach((className) -> {
            try {
                Class<?> clazz = Class.forName(className);
                Field[] declaredFields = clazz.getDeclaredFields();
                Stream.of(declaredFields).forEach((field -> {
                    RpcReference rpcReference = field.getAnnotation(RpcReference.class);
                    if (rpcReference != null) {
                        // TODO: 2023/12/23 处理后续逻辑，将@RpcReference注解标注的接口引用代理对象，放入全局缓存中
                        LOGGER.info("当前标注了@RpcReference注解的字段名称===>>> " + field.getName());
                        LOGGER.info("@RpcReference注解上标注的属性信息如下：");
                        LOGGER.info("version===>>> " + rpcReference.version());
                        LOGGER.info("group===>>> " + rpcReference.group());
                        LOGGER.info("registryType===>>> " + rpcReference.registryType());
                        LOGGER.info("registryAddress===>>> " + rpcReference.registryAddress());
                    }
                }));
            } catch (Exception e) {
                LOGGER.error("scan classes throws exception: {}", e);
            }
        });

        return handlerMap;
    }
}
