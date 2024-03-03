package web.annotation.middleware;

import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;

/**
 * 中间件顶层行为接口
 */
public abstract class MiddlewareHandler {

    public abstract void doInit(HashMap<String,Object> payload);
    public abstract void doHandle(HashMap<String, Object> payload,RoutingContext context);
}
