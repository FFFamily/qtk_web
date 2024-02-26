package web.annotation.parser;

import web.annotation.ApiRequestBody;
import web.annotation.ApiRequestHeader;

import java.lang.annotation.Annotation;
import java.util.HashMap;

/**
 * 请求参数解析策略管理类
 */
public class ParamAnnotationStrategy {

    private static final HashMap<Class<? extends Annotation>, ApiMethodParamAnnotationParser<?>> paramAnnotationStrategy = new HashMap<>();

    static {
        paramAnnotationStrategy.put(ApiRequestBody.class,new ApiRequestBody.ApiRequestBodyParser());
        paramAnnotationStrategy.put(ApiRequestHeader.class,new ApiRequestHeader.ApiRequestHeaderParser());
    }

    public static ApiMethodParamAnnotationParser<?> get(Annotation annotation) {
        return paramAnnotationStrategy.get(annotation.annotationType());
    }
}
