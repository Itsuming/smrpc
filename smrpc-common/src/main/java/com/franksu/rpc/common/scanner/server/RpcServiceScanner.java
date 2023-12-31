package com.franksu.rpc.common.scanner.server;

import com.franksu.rpc.annotation.RpcService;
import com.franksu.rpc.common.helper.RpcServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.franksu.rpc.common.scanner.ClassScanner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: scanner.server
 * @Author: franksu
 * @CreateTime: 2023-12-23  14:20
 * @Description: RpcService注解扫描器
 * @Version: 1.0
 */
public class RpcServiceScanner extends ClassScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServiceScanner.class);

    public static Map<String, Object> doScannerWithRpcServiceAnnotationFilterAndRegistryService (/*String host, int port,*/ String scanPackage/*, RegistryService registryService*/) throws IOException {
        Map<String, Object> handlerMap = new HashMap<>();
        List<String> classNameList = getClassNameList(scanPackage);
        if (classNameList == null || classNameList.isEmpty()) {
            return handlerMap;
        }
        classNameList.stream().forEach((className) -> {
            try {
                Class<?> clazz = Class.forName(className);
                RpcService rpcService = clazz.getAnnotation(RpcService.class);
                if (rpcService != null) {
                    // 优先使用interfaceClass，若interfaceClass的name为空则使用interfaceClassName
                    // TODO: 2023/12/23 后续逻辑向注册中心注册服务元数据，同时向handlermap中记录标注了RpcService注解的类实例
                    // LOGGER.info("当前标注了@RpcSerivce注解的类实例名称===>>> " + clazz.getName());
                    // LOGGER.info("@RpcSerivce注解上标注的属性信息如下：");
                    // LOGGER.info("interfaceClass===>>> " + rpcService.interfaceClass().getName());
                    // LOGGER.info("interfaceClassName===>>> " + rpcService.interfaceClassName());
                    // LOGGER.info("version===>>> " + rpcService.version());
                    // LOGGER.info("group===>>> " + rpcService.group());
                    String serviceName = getServiceName(rpcService);
                    // String key = serviceName.concat(rpcService.version()).concat(rpcService.group());
                    String key = RpcServiceHelper.buildServiceKey(serviceName, rpcService.version(), rpcService.group());
                    handlerMap.put(key, clazz.newInstance());
                }
            } catch (Exception e) {
                LOGGER.error("scan classes throws exception {}", e);
            }
        });

        return handlerMap;
    }

    /**
     * 获取服务名称
     * @return 服务名称
     */
    /**
     * 获取serviceName
     */
    private static String getServiceName(RpcService rpcService){
        //优先使用interfaceClass
        Class clazz = rpcService.interfaceClass();
        if (clazz == void.class){
            return rpcService.interfaceClassName();
        }
        String serviceName = clazz.getName();
        if (serviceName == null || serviceName.trim().isEmpty()){
            serviceName = rpcService.interfaceClassName();
        }
        return serviceName;
    }

}
