package org.demo.handler.test;

import io.vertx.core.json.JsonObject;
import org.demo.client.Client;
import org.demo.constant.MyParam;
import org.demo.entity.MyParamEntity;
import web.annotation.api.ApiHandler;
import web.annotation.api.ApiRequestBody;
import web.exception.BusinessException;

public class index {
    @ApiHandler
    public TestResponse exec(Object str,@ApiRequestBody TestRequest request,@MyParam MyParamEntity obj222) throws InterruptedException {
        System.out.println("====测试异步调用========");
//        Object res = Client.account.helloWord("你好").await();
//        System.out.println("测试结果："+res);
//        Client.account.test().await();
        Thread.sleep(3000L);
        System.out.println("====测试异步调用========结束==========");
        return TestResponse.builder().id("1213").build();
    }
}
