package web.client;

import lombok.SneakyThrows;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import web.promise.WebPromise;

import java.lang.reflect.Method;

public class WebClient {

    @SneakyThrows
    public WebClient(Class<?> target) {
        // 通过CGLIB动态代理获取代理对象的过程
        Enhancer enhancer = new Enhancer();
        // 设置enhancer对象的父类
        enhancer.setSuperclass(target);
        // 设置enhancer的回调对象
        enhancer.setCallback(new WebClientInterceptor());
        // 创建代理对象
        Object client = enhancer.create();
        this.getClass().getField(target.getSimpleName().toLowerCase()).set(this,client);
    }

    static class WebClientInterceptor implements MethodInterceptor {

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
//            Long time = 0L;
//            while(time != 1000L){
//                time+=100L;
//                Thread.sleep(100L);
//            }
            Thread.sleep(2000L);
            System.out.println("当前线程："+Thread.currentThread()+"执行方法为："+method.getName());
            return WebPromise.defaultResolve();
        }
    }
}
