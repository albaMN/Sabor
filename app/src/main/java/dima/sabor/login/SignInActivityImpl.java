package dima.sabor.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dima.sabor.R;
import dima.sabor.base.BaseActivityImpl;
import dima.sabor.dependencyinjection.App;
import dima.sabor.dependencyinjection.activity.ActivityModule;
import dima.sabor.dependencyinjection.view.ViewModule;
import dima.sabor.model.User;
import dima.sabor.profile.ProfileActivityImpl;

public class SignInActivityImpl extends BaseActivityImpl {

    @Inject
    SignInPresenter presenter;
    SignInFragImpl loginFrag;
    SignUpFragImpl registerFrag;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((App) getApplication())
                .getComponent()
                .plus(new ActivityModule(this),
                        new ViewModule(this))
                .inject(this);

        ButterKnife.bind(this);
        loginFrag = new SignInFragImpl();
        registerFrag = new SignUpFragImpl();

        openLoginFragment();

    }

    public void openRegisterFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_view, registerFrag)
                .commit();
    }

    public void openLoginFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_view, loginFrag)
                .commit();
    }

    public void onLoginPressed(String email, String password) {
        presenter.login(email, password);
    }

    public void onRegisterPressed(String email, String password, String username) {
        user = new User("", username, email, "", "", "");
        presenter.register(email, password);
    }

    public void showExistUsername(User user, String username) {
        Toast.makeText(this, "This username already exists." + username, Toast.LENGTH_LONG).show();
    }

    public void registerSuccess() {
        presenter.createUser(user);
    }

    public void changeActivity(){
        startActivity(new Intent(SignInActivityImpl.this, ProfileActivityImpl.class));
        finish();
    }

    public void showLoginFail() {
        Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show();
    }

   /* public void showLoginSuccess(User user) {
        MainActivity.startWithUser(this, user);
    }*/

}
