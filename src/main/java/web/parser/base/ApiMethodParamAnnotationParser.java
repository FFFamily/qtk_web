package web.parser.base;

import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.Parameter;

/**
 * 方法参数注解
 */
public interface ApiMethodParamAnnotationParser<T> {
    public T parser(Parameter parameter, RoutingContext context);
}
