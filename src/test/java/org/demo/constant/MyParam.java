package org.demo.constant;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.demo.entity.MyParamEntity;
import web.annotation.base.AbstractApiMethodParam;
import web.annotation.parser.AbstractApiMethodParamAnnotationParser;
import web.annotation.parser.ApiMethodParamAnnotationParser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Parameter;
import java.util.HashMap;

/**
 * 自定义解析
 */
@AbstractApiMethodParam()
@Retention(RetentionPolicy.RUNTIME)
public @interface MyParam {
    class MyParamParser extends AbstractApiMethodParamAnnotationParser<MyParamEntity> {
        @Override
        public MyParamEntity parser(Parameter parameter, RoutingContext context) {
            MyParamEntity myParam = new MyParamEntity();
            myParam.setAa("adsas");
            return  myParam;
        }
    }

}
