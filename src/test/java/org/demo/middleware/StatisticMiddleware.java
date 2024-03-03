package org.demo.middleware;

import io.vertx.ext.web.RoutingContext;
import web.annotation.middleware.MiddlewareHandler;

import java.util.HashMap;
import java.util.Map;

public class StatisticMiddleware extends MiddlewareHandler {
    private HashMap<String,Integer> map = new HashMap<>();
    @Override
    public void doInit(HashMap<String, Object> payload) {
        System.out.println("统计初始化");
    }

    @Override
    public void doHandle(HashMap<String, Object> payload, RoutingContext context) {
        map.put(context.currentRoute().getPath(), map.getOrDefault(context.currentRoute().getPath(),0)+1);
        System.out.println("统计接口次数");
        System.out.println("当前接口次数为" + map.get(context.currentRoute().getPath()));
    }
}
