package web.annotation;

import web.annotation.base.AbstractApiMethodParam;
import web.annotation.parser.AbstractApiMethodParamAnnotationParser;
import web.annotation.parser.ApiMethodParamAnnotationParser;

import java.lang.annotation.Annotation;

/**
 * 接口请求体
 */
@AbstractApiMethodParam()
public @interface ApiRequestBody {
    class ApiRequestBodyParser extends AbstractApiMethodParamAnnotationParser {
        @Override
        public Object parser(Annotation annotation) {
            return null;
        }
    }
}
