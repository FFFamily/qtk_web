package web.annotation;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import web.annotation.base.AbstractApiMethodParam;
import web.annotation.parser.ApiMethodParamAnnotationParser;

import java.lang.reflect.Parameter;

/**
 * 接口请求体
 */
@AbstractApiMethodParam()
public @interface ApiRequestBody {
    class ApiRequestBodyParser implements ApiMethodParamAnnotationParser<JsonObject> {
        @Override
        public JsonObject parser(Parameter parameter, RoutingContext context) {
            HttpServerRequest request = context.request();
            return  request.body().result().toJsonObject();
        }
    }
}
