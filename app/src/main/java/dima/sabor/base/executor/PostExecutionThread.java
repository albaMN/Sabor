package dima.sabor.base.executor;

public interface PostExecutionThread {

    void post(Runnable runnable);
}