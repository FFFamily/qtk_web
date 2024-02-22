package org.demo.handler.test;

import web.annotation.ApiHandler;

public class index {
    @ApiHandler
    public TestResponse exec(TestRequest request){
        System.out.println(request);
        return TestResponse.builder().id("1213").build();
    }
}
