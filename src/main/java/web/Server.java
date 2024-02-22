package web;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import web.annotation.ApiHandler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import web.ops.ServerOptions;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;
import java.util.function.Function;

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
     * web服务启动方法
     */
    public void start(){
        // 构建 server
        HttpServer server = vertx.createHttpServer();
        // 路由
        server.requestHandler(initRouter());
        // 监听地址
        server.listen(options.getPort(),options.getHost());
    }

    /**
     * 初始化 路由
     * 接口名称就是 handler 下的文件名称
     * 有两种方式
     * 1，遍历handler目录下的所有文件，直接在server中对每个接口进行路由构建
     *      这个方式的实现逻辑可以是编写一个注解其中配置的是 handler 的实现位置，类似于 MapperScan
     * 2，只配置一个路由 /*，对请求过来的地址进行拆分，再找到对应文件下的 handler接口执行体
     *      这种方式也就意味着只有一个 handler，然后再这个 handler 中进行分发，找到对应的执行方法，然后执行
     *      如何分发呢，进行扫包
     *          - 传递一个class路径，告诉 web.Server 要扫哪里
     *
     */
    private Router initRouter(){
        // 路由
        Router router = Router.router(vertx);
        router
            .route("/*")
            // 创建请求体处理
            .handler(BodyHandler.create());
        // 扫描 EnableWeb 下的所有 handler
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .addUrls(ClasspathHelper.forPackage(options.getHandlerPackage()))
                        .setScanners(
                                new MethodAnnotationsScanner(),
                                new TypeAnnotationsScanner(),
                                new SubTypesScanner(false)
                        )
        );
        Set<Method> allHandler = reflections.getMethodsAnnotatedWith(ApiHandler.class);
        // 遍历所有的请求
        allHandler.forEach(handler -> {
            Class<?> clazz = handler.getDeclaringClass();
            Object o;
            try {
                o = clazz.getDeclaredConstructors()[0].newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            Object finalO = o;
            Function<Object[],Object> func = (Object[] args) -> {
                handler.setAccessible(true);
                try {
                    // 拿到方法参数
                    Parameter[] parameters = handler.getParameters();
                    // 可以写一个解析器，什么参数就生成什么解析器，然后去解析
                    for (Parameter parameter : parameters) {
                        // 解析方法参数注解
                        // 主要的核心注解为：ApiRequestBody、
                    }
                    return handler.invoke(finalO, (Object) null);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };
            String[] paths = clazz.getName().split("\\.");
            String name = "/"+paths[paths.length-2];
            router.route(HttpMethod.GET,name).handler(context -> {
                Object apply = func.apply(null);
                if (apply != null){
                    context.response().end(apply.toString());
                }

            });
        });
        return router;
    }

}