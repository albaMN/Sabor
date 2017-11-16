package dima.sabor.dependencyinjection.activity;

import dagger.Subcomponent;
import dima.sabor.dependencyinjection.annotation.scope.PerActivity;
import dima.sabor.dependencyinjection.view.ViewModule;
import dima.sabor.base.BaseActivityImpl;
import dima.sabor.login.SignInActivityImpl;
import dima.sabor.profile.ProfileActivityImpl;
import dima.sabor.splash.SplashActivityImpl;


@PerActivity
@Subcomponent(modules = {ActivityModule.class, ViewModule.class})
public interface ActivityComponent {

    void inject(BaseActivityImpl activity);

    void inject(SplashActivityImpl activity);

    void inject(SignInActivityImpl activity);

    void inject(ProfileActivityImpl activity);
}