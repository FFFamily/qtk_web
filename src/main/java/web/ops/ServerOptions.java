package web.ops;

import lombok.Builder;
import lombok.Data;
import web.annotation.middleware.MiddlewareHandler;

/**
 * web 服务启动参数配置
 */
@Data
@Builder
public class ServerOptions {
    // 端口
    private Integer port;
    // 地址
    private String host;
    // handler 地址
    private String handlerPackage;
    // 前置中间件
    private Class<? extends MiddlewareHandler>[] before;
    // 后置中间件
    private Class<? extends MiddlewareHandler>[] after;
}
