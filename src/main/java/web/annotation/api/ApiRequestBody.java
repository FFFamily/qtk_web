package web.annotation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.ext.web.RoutingContext;
import lombok.SneakyThrows;
import web.annotation.api.base.AbstractApiMethodParam;
import web.parser.AbstractApiMethodParamAnnotationParser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Parameter;

/**
 * 接口请求体
 */
@AbstractApiMethodParam()
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ApiRequestBody {

    class ApiRequestBodyParser extends AbstractApiMethodParamAnnotationParser<Object> {
        @SneakyThrows // 移出 readValue 可能产生的IO异常
        @Override
        public Object parser(Parameter parameter, RoutingContext context) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(context.body().buffer().getBytes(),parameter.getType());
        }
    }
}
