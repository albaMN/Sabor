package dima.sabor.recipesList;

import android.util.Log;

import javax.inject.Inject;

import dima.sabor.data.listener.ErrorBundle;
import dima.sabor.data.usecase.GetRecipesUseCase;
import dima.sabor.model.Recipe;

public class ShowRecipesPresenter {

    private RecipesListActivityImpl view;
    private GetRecipesUseCase getRecipesUseCase;

    @Inject
    public ShowRecipesPresenter(RecipesListActivityImpl view,
                                GetRecipesUseCase getRecipesUseCase) {

        this.view = view;
        this.getRecipesUseCase = getRecipesUseCase;
    }

    public void getRecipes() {
        getRecipesUseCase.execute("",new GetRecipesUseCase.GetRecipesListener() {
            @Override
            public void onError(ErrorBundle errorBundle) {
                view.onError(errorBundle.getErrorMessage());
            }

            @Override
            public void onSuccess(Recipe returnParam) {
                Log.i("ALL RECEIPTS: ", "Title: " + returnParam.getTitle());
                view.addRecipe(returnParam);
            }
        });
    }
}
