package web.promise;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.function.Supplier;

@Data
@Builder
@AllArgsConstructor
public class AsyncFunction<T> {
    private Future<T> future;
    private Promise<T> promise;

    public AsyncFunction() {

    }

    public AsyncFunction(Promise<T> promise) {
        this.promise = promise;
    }

    public static <T> AsyncFunction<T> defaultFunction() {
        AsyncFunction<T> async = new AsyncFunction<>();
        async.setPromise(Promise.promise());
        async.setFuture(async.promise.future());
        return async;
    }

    public <NEW_T> AsyncFunction<NEW_T> then(Supplier<NEW_T> supplier) {
        return AsyncFunction.<NEW_T>builder()
                .future(future.compose(handler -> {
                    NEW_T thenValue = supplier.get();
                    return Future.succeededFuture(thenValue);
                }))
                .build();
    }


    @SneakyThrows
    public T await() {
        return future.toCompletionStage().toCompletableFuture().get();
    }
}
