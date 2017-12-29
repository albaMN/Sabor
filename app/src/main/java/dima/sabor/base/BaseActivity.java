package dima.sabor.base;

public interface BaseActivity {

    void showProgress(String message);

    void hideProgress();

    void onError(String error);

}
