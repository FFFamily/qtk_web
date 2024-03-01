package web.annotation.api;

import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import web.annotation.api.base.AbstractApiMethodParam;
import web.parser.base.AbstractApiMethodParamAnnotationParser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Parameter;

/**
 * 请求头
 */
@AbstractApiMethodParam()
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ApiRequestHeader {
    class ApiRequestHeaderParser extends AbstractApiMethodParamAnnotationParser<MultiMap> {
        @Override
        public MultiMap parser(Parameter parameter, RoutingContext context) {
            return context.request().headers();
        }
    }
}
