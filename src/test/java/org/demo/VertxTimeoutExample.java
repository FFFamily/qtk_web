package org.demo;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

public class VertxTimeoutExample {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        HttpServer server = vertx.createHttpServer();

        server.requestHandler(req -> {
            long timerId = vertx.setTimer(2000, id -> {
                if (!req.response().ended()) {
                    req.response()
                            .setStatusCode(504) // 设置状态码为504表示超时
                            .end("Request Timeout");
                }
            });

            // 模拟一个耗时操作，超过2秒
            vertx.executeBlocking(future -> {
                try {
                    Thread.sleep(3000);
                    future.complete();
                } catch (InterruptedException e) {
                    future.fail(e);
                }
            }, res -> {
                vertx.cancelTimer(timerId); // 取消定时器，确保超时处理不会执行
                if (req.response().ended()) {
                    return;
                }
                if (res.succeeded()) {
                    req.response()
                            .putHeader("content-type", "text/plain")
                            .end("Hello from Vert.x!");
                } else {
                    req.response()
                            .setStatusCode(500) // 设置状态码为500表示内部错误
                            .end("Internal Server Error");
                }
            });
        });

        server.listen(8080, result -> {
            if (result.succeeded()) {
                System.out.println("Server is now listening!");
            } else {
                System.err.println("Failed to start server: " + result.cause());
            }
        });
    }
}
