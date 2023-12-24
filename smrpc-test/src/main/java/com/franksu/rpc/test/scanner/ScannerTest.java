package com.franksu.rpc.test.scanner;

import com.franksu.rpc.common.scanner.ClassScanner;
import com.franksu.rpc.common.scanner.reference.RpcReferenceScanner;
import com.franksu.rpc.common.scanner.server.RpcServiceScanner;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: scanner
 * @Author: franksu
 * @CreateTime: 2023-12-23  15:19
 * @Description: 扫描scanner包下所有的类
 * @Version: 1.0
 */
public class ScannerTest {

    /**
     * 扫描scanner下所有类
     * @throws IOException
     */
    @Test
    public void testScannerClassNameList() throws IOException {
        List<String> classNameList = ClassScanner.getClassNameList("com.franksu.rpc.test.scanner");
        classNameList.forEach(System.out::println);
    }

    /**
     * 扫描scanner包下所有标注了@RpcService的注解类
     * @throws IOException
     */
    @Test
    public void testScannerClassNameListByRpcService() throws IOException {
        RpcServiceScanner.doScannerWithRpcServiceAnnotationFilterAndRegistryService("com.franksu.rpc.test.scanner");
    }
    /**
     * 扫描scanner包下所有标注了@RpcReference的注解类
     * @throws IOException
     */
    @Test
    public void testScannerClassNameListByRpcReference() throws IOException {
        RpcReferenceScanner.doScannerWithRpcReferenceAnnotationFilter("com.franksu.rpc.test.scanner");
    }
}
