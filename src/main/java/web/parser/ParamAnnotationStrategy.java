package web.parser;

import lombok.SneakyThrows;
import web.annotation.api.ApiRequestBody;
import web.annotation.api.ApiRequestHeader;
import web.parser.base.AbstractApiMethodParamAnnotationParser;
import web.parser.base.ApiMethodParamAnnotationParser;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 请求参数解析策略管理类
 */
public class ParamAnnotationStrategy {

    private static final HashMap<Class<? extends Annotation>, ApiMethodParamAnnotationParser<?>> paramAnnotationStrategy = new HashMap<>();

    static {
        paramAnnotationStrategy.put(ApiRequestBody.class,new ApiRequestBody.ApiRequestBodyParser());
        paramAnnotationStrategy.put(ApiRequestHeader.class,new ApiRequestHeader.ApiRequestHeaderParser());
    }

    /**
     * 拿到注解修饰的变量解析器
     * @param annotation 修饰变量的注解 为null代表没有注解
     * @return 解析器
     */
    @SneakyThrows
    public static ApiMethodParamAnnotationParser<?> get(Annotation annotation) {
        if (annotation == null){
            // 注解为null走默认解析器流程
            return new DefaultApiMethodParamAnnotationParser();
        }
        ApiMethodParamAnnotationParser<?> p = paramAnnotationStrategy.get(annotation.annotationType());
        if (p == null){
            // 尝试去找用户自定义的解析器
            List<Class<?>> classes = Arrays.stream(annotation.annotationType().getClasses())
                    .filter(item -> item.getSuperclass().isAssignableFrom(AbstractApiMethodParamAnnotationParser.class))
                    .toList();
            if (classes.isEmpty()){
                return new DefaultApiMethodParamAnnotationParser();
            }else if (classes.size() > 1){
                throw new RuntimeException("用户自定义Api方法注解不能具备多个解析器");
            }else {
                return (ApiMethodParamAnnotationParser<?>) classes.get(0).getDeclaredConstructor().newInstance();
            }
        }
        return p;
    }
}
