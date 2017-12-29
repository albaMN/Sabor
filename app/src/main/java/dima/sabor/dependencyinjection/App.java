package dima.sabor.dependencyinjection;

import android.app.Application;

import dima.sabor.dependencyinjection.application.ApplicationComponent;
import dima.sabor.dependencyinjection.application.ApplicationModule;
import dima.sabor.dependencyinjection.application.DaggerApplicationComponent;

public class App extends Application {

    ApplicationComponent component = null;

    public ApplicationComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();


        component.inject(this);
    }
}