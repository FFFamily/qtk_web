package web.annotation;

/**
 * 开启web服务
 */
public @interface EnableWeb {
    /**
     * handler 的存放位置，目前只支持一个
     */
    String handlerPackage();
}
