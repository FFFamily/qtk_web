package web.annotation.parser;

import io.vertx.ext.web.RoutingContext;
import web.annotation.base.AbstractApiMethodParam;
import web.middleware.ParamParsingException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.HashMap;

/**
 * 注解解析器
 */
public class MethodParamParser {
    // 保留每个路径下的参数解析器
    private static final HashMap<String,ApiMethodParamAnnotationParser<?>[]> parserCache = new HashMap<>();
    /**
     * 解析api方法参数
     * 除了web框架中已有的，同时也要支持用户自定义的注解
     * 解析方法参数注解 若存在对应注解就进行解析,没有就不赋值
     * TODO 需要考虑到突然性的并发问题，防止解析器解析错误
     * @param apiPathName 请求路径 路径是唯一的
     * @param parameters  方法参数
     */
    public synchronized static Object[] parseApiMethodParamAnnotation(String apiPathName, Parameter[] parameters, RoutingContext context){
        ApiMethodParamAnnotationParser<?>[] cache = parserCache.get(apiPathName);
        if (cache != null){
            // 已被解析过
        }
        Object[] arg = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];
            Annotation[] annotations = param.getAnnotations();
            if (annotations.length == 0){
                // 没有被注解修饰
                arg[i] =  newInstanceParam(param);
                continue;
            }
            // 有注解修饰
            for (Annotation paramAnnotation :annotations) {
                // 只有携带了 AbstractApiMethodParam 注解才会解析,才会对方法参数赋值
                AbstractApiMethodParam apiMethodParam = paramAnnotation.annotationType().getAnnotation(AbstractApiMethodParam.class);
                if (apiMethodParam != null){
                    arg[i] =  ParamAnnotationStrategy.get(paramAnnotation).parser(param,context);
                }else {
                    // 如果没有携带只序列化
                    arg[i] =  newInstanceParam(param);
                }
            }
        }
        return arg;
    }

    private static Object newInstanceParam(Parameter param){
        try {
            return param.getType().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new ParamParsingException("序列化未携带Api注解参数失败："+e.getMessage());
        }
    }
}
