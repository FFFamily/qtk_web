package web.utils;

import lombok.SneakyThrows;
import org.reflections.Reflections;
import web.annotation.api.ApiHandler;
import web.annotation.api.ApiRequestSchema;
import web.annotation.api.ApiResponseSchema;
import web.annotation.api.ApiSchema;
import web.exception.CouldNotBuildRouteException;
import web.schema.Schema;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

public class SchemaUtil {
    @SneakyThrows
    public static Schema findSchemaByPath(String schemaPackagePath){
        Reflections reflections = ReflectionUtil.get(schemaPackagePath);
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(ApiSchema.class);
        if (typesAnnotatedWith.size() == 1){
            // 说明有配置 schema
            Class<?> aClass = typesAnnotatedWith.stream().findFirst().get();
            Field[] fields = aClass.getFields();
            // 既然配置了就强制要求有request和response
            Schema request;
            Schema response;
            for (Field field : fields) {
                if (field.getAnnotation(ApiRequestSchema.class) != null){
                    request = (Schema) field.get(null);
                }
                if (field.getAnnotation(ApiResponseSchema.class) != null){
                    response = (Schema) field.get(null);
                }
            }

            return (Schema) aClass.getDeclaredConstructor().newInstance();
        }
        return null;
    }
}
