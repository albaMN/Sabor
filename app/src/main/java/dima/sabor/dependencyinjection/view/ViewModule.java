package dima.sabor.dependencyinjection.view;

import dagger.Module;
import dagger.Provides;
import dima.sabor.base.BaseActivity;
import dima.sabor.login.SignInActivityImpl;
import dima.sabor.profile.OptionsProfileActivityImpl;
import dima.sabor.profile.ProfileActivityImpl;
import dima.sabor.splash.SplashActivityImpl;


@Module
public class ViewModule {

    private BaseActivity view;

    public ViewModule(BaseActivity view) {
        this.view = view;
    }

    @Provides
    BaseActivity providesBaseView(){
        return view;
    }

    @Provides
    SplashActivityImpl providesSplashView(){
        return (SplashActivityImpl) view;
    }

    @Provides
    SignInActivityImpl providesLoginView(){
        return (SignInActivityImpl) view;
    }

    @Provides
    ProfileActivityImpl providesProfileView(){
        return (ProfileActivityImpl) view;
    }

    @Provides
    OptionsProfileActivityImpl providesOptionsProfileView() {
        return (OptionsProfileActivityImpl) view;
    }

}