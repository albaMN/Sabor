package dima.sabor.dependencyinjection.activity;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dima.sabor.dependencyinjection.annotation.qualifier.ForActivity;
import dima.sabor.dependencyinjection.annotation.scope.PerActivity;
import dima.sabor.base.BaseActivityImpl;

@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(BaseActivityImpl activity){
        this.activity = activity;
    }

    @Provides
    @PerActivity
    @ForActivity
    Context providesContext(){
        return activity;
    }

}