package dima.sabor.base.useCase;

import java.util.List;

import dima.sabor.model.Recipe;

public interface RepositoryInterface {

    void getUserRecipes(String userid, GetRecipeCallback dataCallback);
    void getRecipes(GetRecipeCallback dataCallback);
    void getFavourites(GetFavouritesCallback dataCallback);

    //Callback
    interface DefCallback extends DefaultCallback<String>{}
    interface GetRecipeCallback extends DefaultCallback<Recipe> {}
    interface GetFavouritesCallback extends DefaultCallback<List<Recipe>> {}
}
