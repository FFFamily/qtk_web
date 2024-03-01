package web;

import com.esotericsoftware.reflectasm.MethodAccess;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import web.annotation.api.ApiHandler;
import web.parser.MethodParamParser;
import web.exception.BusinessException;
import web.ops.ServerOptions;
import web.utils.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 构建 Web 服务的启动类
 */
public class Server {
    private final Vertx vertx;
    private final ServerOptions options;

    public Server(Vertx vertx, ServerOptions options) {
        this.vertx = vertx;
        this.options = options;
    }

    public Server(ServerOptions options) {
        this.vertx = Vertx.vertx();
        this.options = options;
    }

//    private static Function<Object[], Object> getObjectFunction(Method handler, Object o)  {
//        return (Object[] args) -> {
//            handler.setAccessible(true);
//            return handler.invoke(o, args);
//        };
//    }

    /**
     * web服务启动方法
     */
    public void start() {
        // 构建 server
        HttpServer server = vertx.createHttpServer();
        // 路由
        server.requestHandler(initRouter());
        // 监听地址
        server.listen(options.getPort(), options.getHost());
    }

    /**
     * 初始化 路由
     * 接口名称就是 handler 下的文件名称
     * 有两种方式
     * 1，遍历handler目录下的所有文件，直接在server中对每个接口进行路由构建
     * 这个方式的实现逻辑可以是编写一个注解其中配置的是 handler 的实现位置，类似于 MapperScan
     * 2，只配置一个路由 /*，对请求过来的地址进行拆分，再找到对应文件下的 handler接口执行体
     * 这种方式也就意味着只有一个 handler，然后再这个 handler 中进行分发，找到对应的执行方法，然后执行
     * 如何分发呢，进行扫包
     * - 传递一个class路径，告诉 web.Server 要扫哪里
     */

    @SneakyThrows
    private Router initRouter() {
        // 路由
        Router router = Router.router(vertx);
        // 创建请求体处理
        router.route("/*").handler(BodyHandler.create());
        // 扫描 EnableWeb 下的所有 接口
        // TODO 目前用 options 替代
        Reflections reflections = ReflectionUtil.get(options.getHandlerPackage());
        // 由于
        Set<Method> allHandler = reflections.getMethodsAnnotatedWith(ApiHandler.class);
        // 遍历所有的 接口，为接口添加路由
        for (Method handler : allHandler) {
            Class<?> clazz = handler.getDeclaringClass();
            MethodAccess methodInvoker = MethodAccess.get(clazz);
            handler.setAccessible(true);
            // 我可以理解为这里只能支持单例的接口（如果要支持多例呢？）
            Object o = clazz.getDeclaredConstructor().newInstance();
            // 接口名称
            String apiPathName = "/" + clazz.getPackageName().split(options.getHandlerPackage() + "\\.")[1].replaceAll("\\.","_");
            router.route(HttpMethod.POST, apiPathName)
                    .handler(context -> {
                        // 序列化请求方法参数
                        Object[] args = MethodParamParser.parseApiMethodParamAnnotation(apiPathName,handler.getParameters(), context);
                        Object apply = methodInvoker.invoke(o, handler.getName(), args);
                        if (apply != null) {
                            context.response().end(JsonObject.mapFrom(apply).encode());
                        }
                    })
                    .failureHandler(failureRoutingContext -> {
                        Throwable throwable = failureRoutingContext.failure();
                        throwable.printStackTrace();
                        if (throwable instanceof BusinessException exception) {
                            // 如果为业务异常则不需要额外处理
                            failureRoutingContext.response()
                                    .setStatusCode(200)
                                    .putHeader("content-type", "application/json")
//                                    .end(JsonObject.mapFrom(exception).encode());
                                    .end(JsonObject.of("code", exception.getCode(), "data", exception.getErrorMessage()).encode());
                        } else {
                            failureRoutingContext.response()
                                    .setStatusCode(500)
                                    .putHeader("content-type", "application/json")
                                    .end(JsonObject.of("msg", throwable.toString()).encode());
                        }

                    });
        }
        return router;
    }

}
