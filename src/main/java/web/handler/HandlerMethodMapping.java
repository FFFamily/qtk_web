package web.handler;

/**
 * handler method 解析方案
 */
public interface HandlerMethodMapping {
    /**
     * 拿到 handler 对应的 Method
     * @param packagePath handler 对应的方法地址
     */
    public Object getMethod(String packagePath);
}
