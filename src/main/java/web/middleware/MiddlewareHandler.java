package web.middleware;

/**
 * 中间件顶层行为接口
 */
public interface MiddlewareHandler {
    public void init();
    public void handle();
}
