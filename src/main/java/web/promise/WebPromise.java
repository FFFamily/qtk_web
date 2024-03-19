package web.promise;

import io.vertx.core.Promise;

/**
 * 应该是 promise 的装饰类
 * @param <T>
 */
public class WebPromise<T> {

    /**
     * 默认的异步方法
     */
    public static AsyncFunction<Void> defaultResolve(){
        return AsyncFunction.defaultFunction();
    }
}
