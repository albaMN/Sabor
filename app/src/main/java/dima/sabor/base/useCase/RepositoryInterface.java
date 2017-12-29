package dima.sabor.base.useCase;

import dima.sabor.model.Recipe;

public interface RepositoryInterface {

    void getUserRecipes(String chatId, GetRecipeCallback dataCallback);
    void getRecipes(GetRecipeCallback dataCallback);

    //Callback
    interface GetRecipeCallback extends DefaultCallback<Recipe> {}
}
