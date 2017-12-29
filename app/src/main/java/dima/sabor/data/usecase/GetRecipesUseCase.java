package dima.sabor.data.usecase;

import javax.inject.Inject;

import dima.sabor.base.executor.PostExecutionThread;
import dima.sabor.base.executor.ThreadExecutor;
import dima.sabor.base.useCase.BaseUseCase;
import dima.sabor.base.useCase.DefaultCallback;
import dima.sabor.base.useCase.Interactor;
import dima.sabor.base.useCase.RepositoryInterface;
import dima.sabor.data.listener.ErrorBundle;
import dima.sabor.model.Recipe;

public class GetRecipesUseCase extends BaseUseCase<Recipe> implements Interactor<String, Recipe> {

    private final RepositoryInterface repository;
    private final ThreadExecutor executor;
    private GetRecipesUseCase.GetRecipesListener callback;

    RepositoryInterface.GetRecipeCallback dataCallback = new RepositoryInterface.GetRecipeCallback() {
        @Override
        public void onError(ErrorBundle errorBundle) {
            notifyOnError(errorBundle, callback);
        }

        @Override
        public void onSuccess(Recipe returnParam) {
            notifyOnSuccess(returnParam, callback);
        }
    };

    @Inject
    public GetRecipesUseCase(PostExecutionThread postExecutionThread, ThreadExecutor executor, RepositoryInterface repository) {
        super(postExecutionThread);
        this.repository = repository;
        this.executor = executor;
    }

    @Override
    public <R extends DefaultCallback<Recipe>> void execute(String nothing, R defaultCallback) {
        this.callback = ((GetRecipesUseCase.GetRecipesListener) defaultCallback);
        executor.execute(this);
    }

    @Override
    public void run() {
        repository.getRecipes(dataCallback);
    }

    public interface GetRecipesListener extends DefaultCallback<Recipe> {}
}