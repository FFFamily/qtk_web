package web.annotation.parser;

import java.lang.annotation.Annotation;

/**
 * 方法参数注解
 */
public interface  ApiMethodParamAnnotationParser {
    public Object parser(Annotation annotation);
}
