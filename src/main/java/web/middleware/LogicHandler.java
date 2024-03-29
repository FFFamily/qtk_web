package web.middleware;


import io.vertx.ext.web.RoutingContext;
import web.annotation.middleware.MiddlewareHandler;

import java.lang.reflect.Parameter;
import java.util.HashMap;

/**
 * 全局逻辑 Handler 请求体
 */
public class LogicHandler extends MiddlewareHandler {
    /**
     * 接口名和接口信息映射
     */
    private HashMap<String,ApiMethInfo> ApiMethInfoMap = new HashMap<>();

    class ApiMethInfo{
        /**
         * api接口名称
         */
        private String apiPathName;
        /**
         * api 接口对应的方法名
         */
        private String methodName;
        /**
         * api 接口方法参数
         */
        private Parameter[] parameters;
        /**
         * api类实例化对象
         */
        private Object obj;
    }

    @Override
    public void doInit(HashMap<String, Object> payload) {

    }

    @Override
    public void doHandle(HashMap<String, Object> payload,RoutingContext context) {

    }
}
