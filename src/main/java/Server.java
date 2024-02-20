import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import ops.ServerOptions;

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

    /**
     * web服务启动方法
     */
    public void start(){
        // 构建 server
        HttpServer server = vertx.createHttpServer();
        // 监听地址
        server.listen(options.getPort(),options.getHost());
    }

    /**
     * 初始化 路由
     * 接口名称就是 handler 下的文件名称
     * 有两种方式
     * 1，遍历handler目录下的所有文件，直接在server中对每个接口进行路由构建
     * 2，只配置一个路由 /*，对请求过来的地址进行拆分，再找到对应文件下的 handler接口执行体
     */
    private Router initRouter(){
        // 路由
        Router router = Router.router(vertx);
        router
            .route("/*")
                // 创建请求体处理
            .handler(BodyHandler.create());
        return router;
    }
    public static void main(String[] args) {

    }
}
