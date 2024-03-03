package web.utils;

import web.annotation.middleware.MiddlewareHandler;
import web.exception.CouldNotBuildRouteException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MiddlewareUtil {
    public static List<MiddlewareHandler> findAllMiddleWareByClazz(HashMap<String, Object> payload, Class<? extends MiddlewareHandler>[] classes)  {
        List<MiddlewareHandler> middlewareHandlerList = new ArrayList<>();
        if (classes == null){
            return middlewareHandlerList;
        }
        for (Class<? extends MiddlewareHandler> aClass : classes) {
            MiddlewareHandler middlewareHandler = null;
            try {
                middlewareHandler = aClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new CouldNotBuildRouteException("解析中间中出现异常："+e.getMessage());
            }
            middlewareHandler.doInit(payload);
            middlewareHandlerList.add(middlewareHandler);
        }
        return middlewareHandlerList;
    }
}
