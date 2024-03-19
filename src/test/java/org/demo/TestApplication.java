package org.demo;

import org.demo.client.Client;
import org.demo.global.client.Account;
import org.demo.middleware.SessionMiddleware;
import org.demo.middleware.StatisticMiddleware;
import web.Server;
import web.annotation.EnableWeb;
import web.ops.ServerOptions;

import java.lang.reflect.InvocationTargetException;

@EnableWeb(handlerPackage = "org.demo.handler")
public class TestApplication {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Client.class.getDeclaredConstructor(Class.class).newInstance(Account.class);
        new Server(ServerOptions.builder()
                .handlerPackage("org.demo.handler")
                .before(new Class[]{SessionMiddleware.class})
                .after(new Class[]{StatisticMiddleware.class})
                .host("127.0.0.1")
                .port(8081).build())
                .start();
    }
}
