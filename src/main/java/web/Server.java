package web;

import com.esotericsoftware.reflectasm.MethodAccess;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import web.annotation.api.ApiHandler;
import web.annotation.middleware.MiddlewareHandler;
import web.dto.ApiSchemaInfo;
import web.exception.BusinessException;
import web.factory.ClassFactory;
import web.factory.ObjFactory;
import web.ops.ServerOptions;
import web.parser.MethodParamMapping;
import web.schema.verification.SchemaVerification;
import web.utils.MiddlewareUtil;
import web.utils.ReflectionUtil;
import web.utils.SchemaUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

    /**
     * todo 幂等性
     * web服务启动方法
     */
    public void start() {
        HttpServerOptions httpServerOptions = new HttpServerOptions();
        httpServerOptions.setIdleTimeout(2);
        httpServerOptions.setIdleTimeoutUnit(TimeUnit.SECONDS);
        httpServerOptions.setHost(options.getHost());
        httpServerOptions.setPort(options.getPort());
        // 构建 server
        HttpServer server = vertx.createHttpServer(httpServerOptions);
        // 路由
        server.requestHandler(initRouter());

        // 监听地址
        server.listen();
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
        router.route("/*")
                .order(0)
                .handler(BodyHandler.create())
                .handler(res -> {
                    vertx.setTimer(5000, id -> {
                        if (!res.response().ended()) {
                            res.response()
                                    .setStatusCode(504)
                                    .end("Request Timeout");
                        }
                    });
                });
        // 扫描 EnableWeb 下的所有 接口
        Reflections reflections = ReflectionUtil.get(options.getHandlerPackage());
        Set<Method> allHandler = reflections.getMethodsAnnotatedWith(ApiHandler.class);
        HashMap<String, Object> payload = new HashMap<>();
        List<MiddlewareHandler> beforeMiddleWares = MiddlewareUtil.findAllMiddleWareByClazz(payload, options.getBefore());
        List<MiddlewareHandler> afterMiddleWares = MiddlewareUtil.findAllMiddleWareByClazz(payload, options.getAfter());
        ObjFactory factory = new ClassFactory();
        // 遍历所有的 接口，为接口添加路由
        for (Method handler : allHandler) {
            Class<?> clazz = handler.getDeclaringClass();
            handler.setAccessible(true);
            Object o = factory.createObj(clazz);
            // 接口位置
            String packageName = clazz.getPackageName();
            String schemaPackageName = packageName + ".schema";
            ApiSchemaInfo schemaInfo = SchemaUtil.findSchemaByPath(schemaPackageName);
            // 接口名称
            String apiPathName = "/" + packageName.split(options.getHandlerPackage() + "\\.")[1].replaceAll("\\.", "_");
            router.route(HttpMethod.POST, apiPathName)
                    .handler(context -> {
                        beforeMiddleWares.forEach(item -> item.doHandle(payload, context));
                        Object[] args = MethodParamMapping.parseApiMethodParamAnnotation(apiPathName, handler.getParameters(), context);
                        Object apply = MethodAccess.get(clazz).invoke(o, handler.getName(), args);
                        if (schemaInfo != null) {
                            SchemaVerification.check(schemaInfo.getRequest(), context.body().asJsonObject().getMap());
                        }
                        afterMiddleWares.forEach(item -> item.doHandle(payload, context));
                        if (apply != null) {
                            context.response().end(JsonObject.mapFrom(apply).encode());
                        }
                    })
                    .failureHandler(failureRoutingContext -> {
                        Throwable throwable = failureRoutingContext.failure();
                        if (throwable != null) {
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
                        }
                    });
        }
        return router;
    }


}
