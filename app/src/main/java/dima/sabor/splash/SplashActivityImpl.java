package dima.sabor.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import dima.sabor.R;
import dima.sabor.base.BaseActivityImpl;
import dima.sabor.dependencyinjection.App;
import dima.sabor.dependencyinjection.activity.ActivityModule;
import dima.sabor.dependencyinjection.view.ViewModule;
import dima.sabor.login.SignInActivityImpl;
import dima.sabor.profile.ProfileActivityImpl;

public class SplashActivityImpl extends BaseActivityImpl {

    @Inject
    SplashPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((App) getApplication())
                .getComponent()
                .plus(new ActivityModule(this),
                        new ViewModule(this))
                .inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    public void showLoginActivity() {
        Intent intent = new Intent(SplashActivityImpl.this, SignInActivityImpl.class);
        startActivity(intent);
        finish();
    }

    public void showMainActivity(/*User user*/) {
        startActivity(new Intent(SplashActivityImpl.this, ProfileActivityImpl.class));
        finish();
    }

}
