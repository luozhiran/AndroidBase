package com.plugin.threadtool;

import com.itg.lib_log.L;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadClient {

    private ThreadPoolExecutor executors;



    public ThreadClient(ThreadPoolExecutor executors) {
        this.executors = executors;
    }

    public ThreadClient() {
//        Executors.newCachedThreadPool()
//        executors = new ThreadPoolExecutor(1, 4, 60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        executors = new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
                Executors.defaultThreadFactory(),
                new MyRejected());
    }


    public void addTask(Runnable runnable) {
        if (executors.isShutdown()) {
            throw new IllegalStateException("线程池已经停止");
        }
        executors.execute(runnable);
        logThreadInfo();


    }




    public void shutdown() {
        executors.shutdown();
    }

    public void shutdownNow() {
        executors.shutdownNow();
    }

    private void logThreadInfo(){
        L.e("活跃的线程数："+executors.getActiveCount()+" 核心线程数:"+executors.getCorePoolSize()+"  线程池大小："+executors.getPoolSize()+"  队列的大小:"+executors.getQueue().size());
    }

    private static class MyRejected implements RejectedExecutionHandler{

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("报警信息： 被线程池拒绝，没有被执行");
        }
    }
}
