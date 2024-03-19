package org.demo.middleware;

import io.vertx.ext.web.RoutingContext;
import web.annotation.middleware.MiddlewareHandler;

import java.util.HashMap;

public class SessionMiddleware extends MiddlewareHandler {
    @Override
    public void doInit(HashMap<String, Object> payload) {
        payload.put("session","123456789");
//        System.out.println("session 初始化");
    }

    @Override
    public void doHandle(HashMap<String, Object> payload, RoutingContext context) {
//        System.out.println("请求拦截处理，当前线程为："+Thread.currentThread());
//        System.out.println(payload.toString());
    }
}
