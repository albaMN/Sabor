package dima.sabor.login;

import dima.sabor.base.BaseActivity;
import dima.sabor.model.User;

public interface SignInActivity extends BaseActivity {
    void showLoginFail();
    void changeActivity();
    void showInsertUsername(User user);
    void showExistUsername(User user,String username);
    void showRegisterFail();
}
