package web.annotation;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import web.annotation.base.AbstractApiMethodParam;
import web.annotation.parser.AbstractApiMethodParamAnnotationParser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Parameter;

/**
 * 接口请求体
 */
@AbstractApiMethodParam()
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ApiRequestBody {
    class ApiRequestBodyParser extends AbstractApiMethodParamAnnotationParser<Object> {
        @Override
        public Object parser(Parameter parameter, RoutingContext context) {
            HttpServerRequest request = context.request();
            return  request.body().result().toJson();
//            return null;
        }
    }
}
