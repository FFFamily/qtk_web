package org.demo.handler.test;

import web.annotation.ApiHandler;
import web.annotation.ApiRequestBody;

public class index {
    @ApiHandler
    public TestResponse exec(Object str,@ApiRequestBody TestRequest request,Object obj222){
        System.out.println(str.toString());
        System.out.println(request.toString());
        System.out.println(obj222.toString());
        return TestResponse.builder().id("1213").build();
    }
}
