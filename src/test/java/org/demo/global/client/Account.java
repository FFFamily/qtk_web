package org.demo.global.client;

import web.promise.AsyncFunction;

public abstract class Account {
    public abstract AsyncFunction<Object> helloWord(String... args);

    public abstract AsyncFunction<Void> test();
}
