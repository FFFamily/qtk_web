package web.annotation.api.base;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 顶层api handler 方法参数注解
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AbstractApiMethodParam {
//   Class<? extends ApiMethodParamAnnotationParser> parser();
}
