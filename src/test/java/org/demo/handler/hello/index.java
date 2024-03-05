package org.demo.handler.hello;

import io.vertx.core.json.JsonObject;
import org.demo.constant.MyParam;
import org.demo.entity.MyParamEntity;
import org.demo.global.hello.HelloRequest;
import org.demo.global.hello.HelloResponse;
import web.annotation.api.ApiHandler;
import web.annotation.api.ApiRequestBody;
import web.exception.BusinessException;

public class index {
    @ApiHandler
    public HelloResponse exec(Object str, @ApiRequestBody HelloRequest request, @MyParam MyParamEntity obj222){
        System.out.println(str.toString());
        System.out.println(request.toString());
        System.out.println(JsonObject.mapFrom(obj222));
        return HelloResponse.builder().id("1213").build();
    }
}
