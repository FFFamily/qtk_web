package org.demo.handler.hello;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
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
    public HelloResponse exec(Object str, @ApiRequestBody() HelloRequest request, @MyParam MyParamEntity obj222){
//        System.out.println(str.toString());
//        System.out.println(request.toString());
//        System.out.println(JsonObject.mapFrom(obj222));
        Vertx vertx = Vertx.currentContext().owner();
        System.out.println("我开始了");
        Future future = vertx.executeBlocking(item -> {
            // 直接异步出去了
            System.out.println("异步开始");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("异步结果");
            System.out.println("异步结束");
        });
        Promise<Object> promise = Promise.promise();
        promise.complete("aa");
        promise.handle(Future.succeededFuture());
        promise.future().onComplete(result -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(result.result());
        });
        System.out.println("我结束了");
        return HelloResponse.builder().id("1213").build();
    }
}
