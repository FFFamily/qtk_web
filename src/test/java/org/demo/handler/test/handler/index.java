package org.demo.handler.test.handler;

import web.annotation.ApiHandler;

public class index {
    @ApiHandler
    public Object exec(){
        System.out.println("你好");
        return null;
    }
}
