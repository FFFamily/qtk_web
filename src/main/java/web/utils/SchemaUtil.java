package web.utils;

import lombok.SneakyThrows;
import org.reflections.Reflections;
import web.annotation.api.ApiRequestSchema;
import web.annotation.api.ApiResponseSchema;
import web.annotation.api.ApiSchema;
import web.dto.ApiSchemaInfo;
import web.exception.CouldNotBuildRouteException;
import web.schema.obj.Schema;

import java.lang.reflect.Field;
import java.util.Set;

public class SchemaUtil {

    public static ApiSchemaInfo findSchemaByPath(String schemaPackagePath) throws IllegalAccessException {
        Reflections reflections = ReflectionUtil.get(schemaPackagePath);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(ApiSchema.class);
        if (typesAnnotatedWith.size() == 1){
            // 说明有配置 schema
            Class<?> aClass = typesAnnotatedWith.stream().findFirst().get();
            Field[] fields = aClass.getFields();
            // 既然配置了就强制要求有request和response
            Schema request = null;
            Schema response = null;
            for (Field field : fields) {
                if (field.getAnnotation(ApiRequestSchema.class) != null){
                    request = (Schema) field.get(null);
                }
                if (field.getAnnotation(ApiResponseSchema.class) != null){
                    response = (Schema) field.get(null);
                }
            }
            if (request == null || response == null){
                throw new CouldNotBuildRouteException("无法正常构建Schema");
            }
            return ApiSchemaInfo.builder().request(request).response(response).build();
        }
        return null;
    }
}
