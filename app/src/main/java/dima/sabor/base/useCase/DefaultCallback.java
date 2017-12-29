package dima.sabor.base.useCase;

import dima.sabor.data.listener.ErrorBundle;

public interface DefaultCallback<T> {

    void onError(ErrorBundle errorBundle);

    void onSuccess(T returnParam);
}
