package web.promise;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AsyncFunction<T> {
    private Future<T> future;
    private  Promise<T> promise;

    public AsyncFunction(){

    }
    public AsyncFunction(Promise<T> promise){
        this.promise = promise;
    }

    public static <T> AsyncFunction<T> defaultFunction(){
        AsyncFunction<T> async = new AsyncFunction<>();
        async.setPromise(Promise.promise());
        async.setFuture(async.promise.future());
        return async;
    }

    public T await(){
        return future.result();
    }
}
