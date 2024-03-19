package org.demo.global.client;

import web.promise.AsyncFunction;

public abstract class Account {
    public abstract AsyncFunction<Object> helloWord();

    public abstract AsyncFunction<Void> test();
}
