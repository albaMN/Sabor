package dima.sabor.dependencyinjection.application;

import javax.inject.Singleton;

import dagger.Component;
import dima.sabor.dependencyinjection.view.ViewModule;
import dima.sabor.dependencyinjection.App;
import dima.sabor.dependencyinjection.activity.ActivityComponent;
import dima.sabor.dependencyinjection.activity.ActivityModule;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(App application);

    ActivityComponent plus(ActivityModule activityModule, ViewModule viewModule);
}
