package org.demo.handler.test;

import io.vertx.core.json.JsonObject;
import org.demo.constant.MyParam;
import web.annotation.ApiHandler;
import web.annotation.ApiRequestBody;

public class index {
    @ApiHandler
    public TestResponse exec(Object str,@ApiRequestBody TestRequest request,@MyParam Object obj222){
        System.out.println(str.toString());
        System.out.println(request.toString());
        System.out.println(JsonObject.mapFrom(obj222));
        if (1 == 1){
            throw new RuntimeException("111");
        }
        return TestResponse.builder().id("1213").build();
    }
}
