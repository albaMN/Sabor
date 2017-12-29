package dima.sabor.base.useCase;

public interface Interactor<InputType, ReturnType> extends Runnable {
    void run();

    <R extends DefaultCallback<ReturnType>> void execute(InputType param, R defaultCallback);
}
