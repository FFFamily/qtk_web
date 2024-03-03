package web.annotation;

/**
 * 开启web服务
 */
public @interface EnableWeb {
    /**
     * handler 的存放位置，目前只支持一个
     */
    String handlerPackage();

    /**
     * 前置中间件
     */
    String[] beforeMiddleware() default {};

    /**
     * 后置中间件
     */
    String[] afterMiddleware() default {};
}
