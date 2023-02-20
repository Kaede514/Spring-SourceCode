package com.kaede.a05;

import org.springframework.context.support.GenericApplicationContext;

public class MyGenericApplicationContext extends GenericApplicationContext {
    private Thread shutdownHook = null;

    @Override
    public void registerShutdownHook() {
        if (this.shutdownHook == null) {
            this.shutdownHook = new Thread(SHUTDOWN_HOOK_THREAD_NAME) {
                @Override
                public void run() {
                    System.out.println("容器关闭...");
                }
            };
            Runtime.getRuntime().addShutdownHook(this.shutdownHook);
        }
    }
}