package web.parser;

import io.vertx.ext.web.RoutingContext;
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
public class MethodParamParser {
    // 保留每个路径下的参数解析器
    private static final HashMap<String, ApiMethodParamAnnotationParser<?>[]> parserCache = new HashMap<>();
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
                    annotationParser = ParamAnnotationStrategy.get(null);
                    parsers[i] = annotationParser;
                    arg[i] =  annotationParser.parser(param,context);
                    continue;
                }
                List<Annotation> annotationList = Arrays.stream(annotations)
                        .filter(annotation -> annotation.annotationType().getAnnotation(AbstractApiMethodParam.class) != null)
                        .toList();
                if (annotationList.isEmpty()){
                    // 有被注解修饰，但没有 Api 注解
                    annotationParser = ParamAnnotationStrategy.get(null);
                }else if (annotationList.size() > 1){
                    // 多个 Api 注解共同修饰同一个
                    throw new RuntimeException(param.getName()+"存在多个Api注解");
                }else {
                    annotationParser = ParamAnnotationStrategy.get(annotationList.get(0));
                }
                parsers[i] = annotationParser;
                arg[i] = annotationParser.parser(param,context);
            }
        }
        parserCache.put(apiPathName,parsers);
        return arg;
    }

}
