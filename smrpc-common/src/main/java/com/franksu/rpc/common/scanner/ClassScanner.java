package com.franksu.rpc.common.scanner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: scanner
 * @Author: franksu
 * @CreateTime: 2023-12-23  13:22
 * @Description: 通用类扫描器
 * @Version: 1.0
 */
public class ClassScanner {
    /**
     * 文件
     */
    private static final String PROTOCOL_FILE = "file";
    /**
     * jar包
     */
    private static final String PROTOCOL_JAR = "jar";
    /**
     * class文件后缀
     */
    private static final String CLASS_FILE_SUFFIX = ".class";

    /**
     * 扫描当前工程中指定目录下所有类信息
     * @param packageName 包名
     * @param packagePath 包的绝对路径
     * @param recursive 是否递归调用
     * @param classNameList 类名集合
     */
    private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<String> classNameList) {
        // 获取此包的目录，创建一个File
        File dir = new File(packagePath);
        // 若目录不存在或不是一个目录则直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // 若存在则获取包下所有目录和文件
        File[] files = dir.listFiles(new FileFilter() {
            // 自定义过滤规则：可以继续遍历或以.class结尾
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) ||
                        (file.getName().endsWith(".class"));
            }
        });
        // 循环遍历所有文件
        for (File file : files) {
            // 若是目录则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, classNameList);
            } else {
                // 如果是.class文件，则获取文件名
                String className = file.getName().substring(0, file.getName().length() - 6);
                // 将类名添加到集合中
                classNameList.add(packageName + "." + className);
            }
        }
    }

    /**
     * 扫描指定jar包下所有类信息
     * @param packageName 包名
     * @param classNameList 类信息列表
     * @param recursive 是否递归
     * @param packageDirName 当前包名的前部分名称
     * @param url 包的url
     * @return 处理后的包名，供下次调用使用
     * @throws IOException IO异常
     */
    private static String findAndAddClassesInPackageByJar(String packageName, List<String> classNameList, boolean recursive, String packageDirName, URL url) throws IOException {
        // 如果是jar文件，则定义一个jarFile
        JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
        // 从jar包获取枚举类
        Enumeration<JarEntry> entries = jarFile.entries();
        // 循环遍历
        while (entries.hasMoreElements()) {
            // 获取jar包中的文件
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            // 若是以“/”开头，获取后面字符串
            if (name.charAt(0) == '/') {
                name = name.substring(1);
            }
            // 若前半部分和定义包名相同
            if (name.startsWith(packageName)) {
                int idx = name.lastIndexOf('/');
                // 若以“/”结尾则是一个包
                if (idx != -1) {
                    // 获取包名，将“/”替换为“.”
                    packageName = name.substring(0, idx).replace(".", "/");
                }
                // 若可以继续迭代并且是一个包
                if ((idx != -1) || recursive) {
                    // 若是class文件且不是目录
                    if (name.endsWith(CLASS_FILE_SUFFIX) && !jarEntry.isDirectory()) {
                        // 加入类名列表中
                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                        classNameList.add(packageName + "." + className);
                    }
                }
            }

        }
        return packageName;
    }

    /**
     * 扫描指定包下所有类信息
     * @param packageName 指定包名
     * @return 类名集合
     * @throws IOException IO异常
     */
    public static List<String> getClassNameList(String packageName) throws IOException {
        // 定义class类名集合
        List<String> classNameList = new ArrayList<>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包名并进行替换
        String packagDirName = packageName.replace(".", "/");
        // 获取当前上下文资源信息，循环遍历
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packagDirName);
        while (dirs.hasMoreElements()) {
            // 获取元素
            URL element = dirs.nextElement();
            // 获取协议名称
            String protocol = element.getProtocol();
            // 文件形式
            if (PROTOCOL_FILE.equals(protocol)) {
                // 获取包的物理路径
                String filePath = URLDecoder.decode(element.getFile(), "utf-8");
                // 扫描加入集合
                findAndAddClassesInPackageByFile(packageName, filePath, recursive, classNameList);
            } else if (PROTOCOL_JAR.equals(protocol)) {
                // jar包形式
                findAndAddClassesInPackageByJar(packageName, classNameList, recursive, packagDirName, element);
            }
        }

        return classNameList;
    }
}
