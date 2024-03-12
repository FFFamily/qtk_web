package web.parser;

import io.vertx.ext.web.RoutingContext;
import web.exception.ParamParsingException;

import java.lang.reflect.Parameter;

public class DefaultApiMethodParamAnnotationParser extends AbstractApiMethodParamAnnotationParser<Object> {
    @Override
    public Object parser(Parameter parameter, RoutingContext context) {
        try {
            return parameter.getType().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new ParamParsingException("序列化未携带Api注解参数失败："+e.getMessage());
        }
    }
}
