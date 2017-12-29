package dima.sabor.base;

import javax.inject.Inject;

public class BasePresenter {

    private BaseActivity view;

    @Inject
    public BasePresenter(BaseActivity view) {
        this.view = view;
    }
}
