package org.demo.handler.error.hello;

import io.vertx.core.json.JsonObject;
import org.demo.constant.MyParam;
import org.demo.entity.MyParamEntity;
import web.annotation.ApiHandler;
import web.annotation.ApiRequestBody;
import web.exception.BusinessException;

public class index {
    @ApiHandler
    public TestResponse exec(Object str, @ApiRequestBody TestRequest request, @MyParam MyParamEntity obj222){
        System.out.println(str.toString());
        System.out.println(request.toString());
        System.out.println(JsonObject.mapFrom(obj222));
        return TestResponse.builder().id("1213").build();
    }
}
