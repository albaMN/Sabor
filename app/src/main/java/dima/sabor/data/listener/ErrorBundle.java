package dima.sabor.data.listener;

/**
 * Created by Alba on 20/12/2017.
 */

public interface ErrorBundle {
    Exception getException();

    String getErrorMessage();
}
