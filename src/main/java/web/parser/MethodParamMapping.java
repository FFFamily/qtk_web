package web.parser;

import io.vertx.ext.web.RoutingContext;
import lombok.SneakyThrows;
import web.annotation.api.ApiRequestBody;
import web.annotation.api.ApiRequestHeader;
import web.annotation.api.base.AbstractApiMethodParam;
import web.parser.base.ApiMethodParamAnnotationParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 注解解析器
 */
public class MethodParamMapping {
    // 保留每个路径下的参数解析器
    private static final HashMap<String, ApiMethodParamAnnotationParser<?>[]> parserCache = new HashMap<>();
    private static final HashMap<Class<? extends Annotation>, ApiMethodParamAnnotationParser<?>> paramAnnotationStrategy = new HashMap<>();

    static {
        paramAnnotationStrategy.put(ApiRequestBody.class,new ApiRequestBody.ApiRequestBodyParser());
        paramAnnotationStrategy.put(ApiRequestHeader.class,new ApiRequestHeader.ApiRequestHeaderParser());
    }

    /**
     * 解析api方法参数
     * 除了web框架中已有的，同时也要支持用户自定义的注解
     * 解析方法参数注解 若存在对应注解就进行解析,没有就不赋值
     * TODO 需要考虑到突然性的并发问题，防止解析器解析错误
     * TODO 可以在编译的时候就爆出相关的解析问题，而不是在掉接口的时
     * @param apiPathName 请求路径 路径是唯一的
     * @param parameters  方法参数
     */
    public static Object[] parseApiMethodParamAnnotation(String apiPathName, Parameter[] parameters, RoutingContext context){
        ApiMethodParamAnnotationParser<?>[] parsers = parserCache.get(apiPathName);
        Object[] arg = new Object[parameters.length];
        if (parsers != null){
            // 已被解析过
            for (int i = 0; i < parameters.length; i++) {
                arg[i] = parsers[i].parser(parameters[i],context);
            }
        }else {
            parsers = new ApiMethodParamAnnotationParser<?>[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Parameter param = parameters[i];
                Annotation[] annotations = param.getAnnotations();
                ApiMethodParamAnnotationParser<?> annotationParser;
                if (annotations.length == 0){
                    // 没有被注解修饰
                    annotationParser = get(null);
                    parsers[i] = annotationParser;
                    arg[i] =  annotationParser.parser(param,context);
                    continue;
                }
                List<Annotation> annotationList = Arrays.stream(annotations)
                        .filter(annotation -> annotation.annotationType().getAnnotation(AbstractApiMethodParam.class) != null)
                        .toList();
                if (annotationList.isEmpty()){
                    // 有被注解修饰，但没有 Api 注解
                    annotationParser = get(null);
                }else if (annotationList.size() > 1){
                    // 多个 Api 注解共同修饰同一个
                    throw new RuntimeException(param.getName()+"存在多个Api注解");
                }else {
                    annotationParser = get(annotationList.get(0));
                }
                parsers[i] = annotationParser;
                arg[i] = annotationParser.parser(param,context);
            }
        }
        parserCache.put(apiPathName,parsers);
        return arg;
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
