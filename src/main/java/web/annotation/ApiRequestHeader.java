package web.annotation;

import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import web.annotation.base.AbstractApiMethodParam;
import web.annotation.parser.ApiMethodParamAnnotationParser;

import java.lang.reflect.Parameter;

/**
 * 请求头
 */
@AbstractApiMethodParam()
public @interface ApiRequestHeader {
    class ApiRequestHeaderParser implements ApiMethodParamAnnotationParser<MultiMap> {
        @Override
        public MultiMap parser(Parameter parameter, RoutingContext context) {
            return context.request().headers();
        }
    }
}
