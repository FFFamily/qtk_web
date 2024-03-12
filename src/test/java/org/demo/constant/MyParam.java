package org.demo.constant;

import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import org.demo.entity.MyParamEntity;
import web.annotation.api.base.AbstractApiMethodParam;
import web.parser.AbstractApiMethodParamAnnotationParser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Parameter;

/**
 * 自定义解析
 */
@AbstractApiMethodParam()
@Retention(RetentionPolicy.RUNTIME)
public @interface MyParam {
    class MyParamParser extends AbstractApiMethodParamAnnotationParser<MyParamEntity> {
        @Override
        public MyParamEntity parser(Parameter parameter, RoutingContext context) {
            System.out.println(Vertx.currentContext().config().getMap());
            MyParamEntity myParam = new MyParamEntity();
            myParam.setAa("adsas");
            return  myParam;
        }
    }

}
