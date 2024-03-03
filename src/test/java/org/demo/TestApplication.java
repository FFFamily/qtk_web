package org.demo;

import org.demo.middleware.SessionMiddleware;
import org.demo.middleware.StatisticMiddleware;
import web.Server;
import web.annotation.EnableWeb;
import web.ops.ServerOptions;

@EnableWeb(handlerPackage = "org.demo.handler")
public class TestApplication {
    public static void main(String[] args) {
        new Server(ServerOptions.builder()
                .handlerPackage("org.demo.handler")
                .before(new Class[]{SessionMiddleware.class})
                .after(new Class[]{StatisticMiddleware.class})
                .host("127.0.0.1")
                .port(8081).build())
                .start();
    }
}
