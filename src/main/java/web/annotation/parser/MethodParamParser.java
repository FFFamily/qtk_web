package web.annotation.parser;

import web.annotation.base.AbstractApiMethodParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

/**
 * 注解解析器
 */
public class MethodParamParser {
    /**
     * 解析api方法参数
     * @param param 方法参数
     */
    public static Object parseApiMethodParamAnnotation(Parameter param){
        for (Annotation paramAnnotation : param.getAnnotations()) {
            // 只有携带了 AbstractApiMethodParam 注解才会解析,才会对方法参数赋值
            AbstractApiMethodParam apiMethodParam = paramAnnotation.annotationType().getAnnotation(AbstractApiMethodParam.class);
            if (apiMethodParam != null){
                AbstractApiMethodParamAnnotationParser parser;

            }else {
                // 如果没有携带只序列化
            }
        }
    }
}
