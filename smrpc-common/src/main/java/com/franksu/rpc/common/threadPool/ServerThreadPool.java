package com.franksu.rpc.common.threadPool;

import sun.nio.ch.ThreadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.common.threadPool
 * @Author: franksu
 * @CreateTime: 2024-01-07  13:58
 * @Description: 线程池工具类
 * @Version: 1.0
 */
public class ServerThreadPool {

    private static ThreadPoolExecutor threadPoolExecutor;

    static {
        threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(65536));
    }

    public static void submit(Runnable task) {
        threadPoolExecutor.submit(task);
    }

    public static void shutdown() {
        threadPoolExecutor.shutdown();
    }
}
