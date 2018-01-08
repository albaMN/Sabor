package dima.sabor.data.usecase;

import java.util.List;

import javax.inject.Inject;

import dima.sabor.base.executor.PostExecutionThread;
import dima.sabor.base.executor.ThreadExecutor;
import dima.sabor.base.useCase.BaseUseCase;
import dima.sabor.base.useCase.DefaultCallback;
import dima.sabor.base.useCase.Interactor;
import dima.sabor.data.RepositoryInterface;
import dima.sabor.data.listener.ErrorBundle;
import dima.sabor.model.Recipe;


public class GetMyRecipesUseCase  extends BaseUseCase< List<Recipe>> implements Interactor<String, List<Recipe>> {

    private final RepositoryInterface repository;
    private final ThreadExecutor executor;
    private GetMyRecipesUseCase.GetMyRecipesListener callback;

    RepositoryInterface.GetFavouritesCallback dataCallback = new RepositoryInterface.GetFavouritesCallback() {
        @Override
        public void onError(ErrorBundle errorBundle) {
            notifyOnError(errorBundle, callback);
        }

        @Override
        public void onSuccess( List<Recipe> returnParam) {
            notifyOnSuccess(returnParam, callback);
        }

    };

    @Inject
    public GetMyRecipesUseCase(PostExecutionThread postExecutionThread, ThreadExecutor executor, RepositoryInterface repository) {
        super(postExecutionThread);
        this.repository = repository;
        this.executor = executor;
    }

    @Override
    public <R extends DefaultCallback< List<Recipe> >> void execute(String nothing, R defaultCallback) {
        this.callback = ((GetMyRecipesUseCase.GetMyRecipesListener) defaultCallback);
        executor.execute(this);
    }

    @Override
    public void run() {
        repository.getMyRecipes(dataCallback);
    }


    public interface GetMyRecipesListener extends DefaultCallback< List<Recipe>> {}
}