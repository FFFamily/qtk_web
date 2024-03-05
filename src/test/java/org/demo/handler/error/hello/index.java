package org.demo.handler.error.hello;

import io.vertx.core.json.JsonObject;
import org.demo.constant.MyParam;
import org.demo.entity.MyParamEntity;
import web.annotation.api.ApiHandler;
import web.annotation.api.ApiRequestBody;

public class index {
    @ApiHandler
    public TestResponse exec(Object str, @ApiRequestBody TestRequest request, @MyParam MyParamEntity obj222){
        System.out.println(Thread.currentThread().getName());
//        System.out.println(str.toString());
//        System.out.println(request.toString());
//        System.out.println(JsonObject.mapFrom(obj222));
        return TestResponse.builder().id("1213").build();
    }
}
