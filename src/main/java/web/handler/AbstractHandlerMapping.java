package web.handler;

import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import web.annotation.api.ApiHandler;
import web.utils.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractHandlerMapping implements HandlerMapping {
    private final Map<String, Object> handlerMap = new LinkedHashMap<>();
    protected void detectHandlers(String handlerPackage)  {
        Reflections reflections = ReflectionUtil.get(handlerPackage);
        Set<Method> handlers = reflections.getMethodsAnnotatedWith(ApiHandler.class);
    }
}
