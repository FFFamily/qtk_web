package web.handler;

import com.esotericsoftware.reflectasm.MethodAccess;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.springframework.lang.Nullable;
import web.annotation.api.ApiHandler;
import web.exception.DefaultWebException;
import web.utils.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractHandlerMethodMapping implements HandlerMethodMapping {
    private final Map<String, Object> handlerMap = new LinkedHashMap<>();
    @Nullable
    protected abstract Object getHandlerMethodInternal(Class<?> clazz);

    /**
     * 找到所有的 Handler
     * Handler 规则为固定为某个路径下的 handler 目录
     * @param handlerPackage
     */
    @SneakyThrows
    protected void detectHandlers(String handlerPackage)  {
        Reflections reflections = ReflectionUtil.get(handlerPackage);
        Set<Method> handlers = reflections.getMethodsAnnotatedWith(ApiHandler.class);
        for (Method handler : handlers) {
            // 遍历handler
            Class<?> clazz = handler.getDeclaringClass();
            MethodAccess methodInvoker = MethodAccess.get(clazz);
            String packageName = clazz.getPackageName();
            String apiPathName = "/" + packageName.split(handlerPackage + "\\.")[1].replaceAll("\\.","_");
            handler.setAccessible(true);
            Object handlerObj = clazz.getDeclaredConstructor().newInstance();
            handlerMap.put(apiPathName,handlerObj);
        }
    }

}
