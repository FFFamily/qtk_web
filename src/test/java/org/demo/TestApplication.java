package org.demo;

import web.Server;
import web.annotation.EnableWeb;
import web.ops.ServerOptions;

@EnableWeb(handlerPackage = "org.demo.handler")
public class TestApplication {
    public static void main(String[] args) {
        new Server(ServerOptions.builder()
                .handlerPackage("org.demo.handler")
                .host("127.0.0.1")
                .port(8080).build())
                .start();
    }
}
