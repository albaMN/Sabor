package dima.sabor.dependencyinjection.application;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dima.sabor.base.executor.JobExecutor;
import dima.sabor.base.executor.PostExecutionThread;
import dima.sabor.base.executor.ThreadExecutor;
import dima.sabor.base.executor.UIThread;
import dima.sabor.base.useCase.RepositoryInterface;
import dima.sabor.data.AppRepository;
import dima.sabor.data.FirebaseInterface;
import dima.sabor.data.InternalStorageInterface;
import dima.sabor.data.impl.FirebaseDataSource;
import dima.sabor.data.impl.InternalStorageDataSource;
import dima.sabor.dependencyinjection.App;
import dima.sabor.dependencyinjection.annotation.qualifier.ForApp;


@Module
public class ApplicationModule {


    private final App app;

    public ApplicationModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    @ForApp
    public Context providesContext(){
        return app;
    }

    @Provides
    @Singleton
    public FirebaseInterface providesFirebaseDataSource(FirebaseDataSource dataSource){
        return dataSource;
    }

    @Provides
    @Singleton
    public InternalStorageInterface providesInternalStorage(InternalStorageDataSource dataSource){
        return dataSource;
    }

    @Provides
    @Singleton
    public ThreadExecutor proviedesThreadExecutor(){
        return new JobExecutor();
    }

    @Provides
    @Singleton
    public PostExecutionThread providesPostExecutionThread(){
        return new UIThread();
    }

    @Provides
    @Singleton
    public RepositoryInterface providesAppRepository(AppRepository repository){
        return repository;
    }


    /*@Provides
    @Singleton
    public ApiInterface providesApiDataSource(ApiDataSource dataSource){
        return dataSource;
    }*/
}
