package web.annotation.parser;

import lombok.SneakyThrows;
import web.annotation.ApiRequestBody;
import web.annotation.ApiRequestHeader;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 请求参数解析策略管理类
 */
public class ParamAnnotationStrategy {

    private static final HashMap<Class<? extends Annotation>, ApiMethodParamAnnotationParser<?>> paramAnnotationStrategy = new HashMap<>();

    static {
        paramAnnotationStrategy.put(ApiRequestBody.class,new ApiRequestBody.ApiRequestBodyParser());
        paramAnnotationStrategy.put(ApiRequestHeader.class,new ApiRequestHeader.ApiRequestHeaderParser());
    }

    @SneakyThrows
    public static ApiMethodParamAnnotationParser<?> get(Annotation annotation) {
        ApiMethodParamAnnotationParser<?> p = paramAnnotationStrategy.get(annotation.annotationType());
        if (p == null){
            // 尝试去找用户自定义的解析器
            List<Class<?>> classes = Arrays.stream(annotation.annotationType().getClasses()).filter(item -> item.getSuperclass().isAssignableFrom(AbstractApiMethodParamAnnotationParser.class)).toList();
            if (classes.isEmpty() ){
                throw new RuntimeException("用户自定义Api方法注解但没定义解析器");
            }else if (classes.size() > 1){
                throw new RuntimeException("用户自定义Api方法注解不能具备多个解析器");
            }else {
                return (ApiMethodParamAnnotationParser<?>) classes.get(0).getDeclaredConstructor().newInstance();
            }
        }
        return paramAnnotationStrategy.get(annotation.annotationType());
    }
}
