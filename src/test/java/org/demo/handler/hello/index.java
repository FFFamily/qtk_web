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
        System.out.println("准备执行,当前线程为："+Thread.currentThread());
//        System.out.println(str.toString());
//        System.out.println(request.toString());
//        System.out.println(JsonObject.mapFrom(obj222));
//        Vertx vertx = Vertx.currentContext().owner();
        ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
        threadLocal.set(1);
        Future future1 =  Vertx.currentContext().owner().executeBlocking(item -> {
            // 直接异步出去了
            System.out.println("异步开始："+Thread.currentThread()+threadLocal.get());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("异步结果："+Thread.currentThread()+threadLocal.get());
        });
        Future future2 =  Vertx.currentContext().owner().executeBlocking(item -> {
            // 直接异步出去了
            System.out.println("异步开始："+Thread.currentThread()+threadLocal.get());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("异步结果："+Thread.currentThread()+threadLocal.get());
        });
        Promise<Object> promise = Promise.promise();
        promise.complete(future1.result());
        promise.handle(Future.succeededFuture());
        promise.future().onComplete(result -> {
            System.out.println(Thread.currentThread());
            try {
                Thread.sleep(1000);
                System.out.println("处理完成1 当前线程"+Thread.currentThread()+threadLocal.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(result.result());
        });
        promise.future().onComplete(result -> {
            System.out.println(Thread.currentThread());
            try {
                Thread.sleep(1000);
                System.out.println("处理完成2");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(result.result());
        });
        return HelloResponse.builder().id("1213").build();
    }
}
