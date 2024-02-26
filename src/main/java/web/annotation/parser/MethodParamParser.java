package web.annotation.parser;

import io.vertx.ext.web.RoutingContext;
import web.annotation.base.AbstractApiMethodParam;
import web.middleware.ParamParsingException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

/**
 * 注解解析器
 */
public class MethodParamParser {
    /**
     * 解析api方法参数
     *
     * @param param   方法参数
     * @param context
     */
    public static Object parseApiMethodParamAnnotation(Parameter param, RoutingContext context){
        for (Annotation paramAnnotation : param.getAnnotations()) {
            // 只有携带了 AbstractApiMethodParam 注解才会解析,才会对方法参数赋值
            AbstractApiMethodParam apiMethodParam = paramAnnotation.annotationType().getAnnotation(AbstractApiMethodParam.class);
            if (apiMethodParam != null){
                return ParamAnnotationStrategy.get(paramAnnotation).parser(param,context);
            }
        }
        // 如果没有携带只序列化
        try {
            return param.getType().getDeclaredConstructor().newInstance();
        }catch (Exception e){
            throw new ParamParsingException("序列化未携带Api注解参数失败："+e.getMessage());
        }
    }
}
