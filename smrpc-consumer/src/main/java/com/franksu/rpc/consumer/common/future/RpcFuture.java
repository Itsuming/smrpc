package com.franksu.rpc.consumer.common.future;

import com.franksu.rpc.protocol.RpcProtocol;
import com.franksu.rpc.protocol.request.RpcRequest;
import com.franksu.rpc.protocol.response.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * @BelongsProject: smrpc
 * @BelongsPackage: com.franksu.rpc.consumer.common.future
 * @Author: franksu
 * @CreateTime: 2024-01-28  14:17
 * @Description: 自定义RpcFuture
 * @Version: 1.0
 */
public class RpcFuture extends CompletableFuture<Object> {

    private static final Logger logger = LoggerFactory.getLogger(RpcFuture.class);

    private Sync sync;

    private RpcProtocol<RpcRequest> requestRpcProtocol;

    private RpcProtocol<RpcResponse> responseRpcProtocol;

    private long startTime;

    private long responseTimeThreshold = 5000;

    public RpcFuture(RpcProtocol<RpcRequest> requestRpcProtocol) {
        this.sync = new Sync();
        this.requestRpcProtocol = requestRpcProtocol;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        sync.acquire(-1);
        if (this.responseRpcProtocol != null) {
            return responseRpcProtocol.getBody().getResult();
        } else {
            return null;
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
        if (success) {
            if (this.responseRpcProtocol != null) {
                return this.responseRpcProtocol.getBody().getResult();
            } else {
                return null;
            }
        } else {
            throw new RuntimeException("Timeout exception. Request id：" +
                    this.requestRpcProtocol.getRpcHeader().getRequestId()
                    + ". Request class name：" +
                    this.requestRpcProtocol.getBody().getClassName() +
                    ". Request method：" +
                    this.requestRpcProtocol.getBody().getMethodName());
        }
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    public void done(RpcProtocol<RpcResponse> responseRpcProtocol) {
        this.responseRpcProtocol = responseRpcProtocol;
        sync.release(1);
        // Threshold
        long responseTime = System.currentTimeMillis() - startTime;
        if (responseTime > this.responseTimeThreshold) {
            logger.warn("Service response time is too slow. RequestId = " + this.requestRpcProtocol.getRpcHeader().getRequestId()
             + ". responseTime = " + responseTime + " ms");
        }
    }

    static class Sync extends AbstractQueuedSynchronizer {
        private static final long serialVersionUID = 1L;

        // future status
        private final int done = 1;
        private final int pending = 0;

        protected boolean tryAcquire(int acquires) {
            return getState() == done;
        }

        protected boolean tryRelease(int releases) {
            if (getState() == pending) {
                if (compareAndSetState(pending, done)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isDone() {
            getState();
            return getState() == done;
        }
    }
}
