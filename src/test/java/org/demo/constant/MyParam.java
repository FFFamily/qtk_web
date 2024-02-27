package org.demo.constant;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
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
    class MyParamParser extends AbstractApiMethodParamAnnotationParser<HashMap<String,String>> {
        @Override
        public HashMap<String,String> parser(Parameter parameter, RoutingContext context) {
            HashMap<String,String> map = new HashMap<>();
            map.put("aa","11");
            return  map;
        }
    }

}
