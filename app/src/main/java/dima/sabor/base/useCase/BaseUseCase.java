package dima.sabor.base.useCase;

import dima.sabor.base.executor.PostExecutionThread;
import dima.sabor.data.listener.ErrorBundle;

public class BaseUseCase<T> {

    private final PostExecutionThread postExecutionThread;

    public BaseUseCase(PostExecutionThread postExecutionThread) {
        this.postExecutionThread = postExecutionThread;
    }

    public void notifyOnError(final ErrorBundle errorBundle, final DefaultCallback<T> callback) {
        postExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(errorBundle);
            }
        });
    }

    public void notifyOnSuccess(final T param, final DefaultCallback<T> callback) {
        postExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(param);
            }
        });
    }

}