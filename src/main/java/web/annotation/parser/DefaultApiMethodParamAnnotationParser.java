package web.annotation.parser;

import io.vertx.ext.web.RoutingContext;

import java.lang.reflect.Parameter;

public class DefaultApiMethodParamAnnotationParser extends AbstractApiMethodParamAnnotationParser<Object>{

    @Override
    public Object parser(Parameter parameter, RoutingContext context) {
        return null;
    }
}
