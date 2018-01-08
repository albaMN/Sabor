package dima.sabor.data;

import java.util.List;

import dima.sabor.base.useCase.DefaultCallback;
import dima.sabor.model.Recipe;

public interface RepositoryInterface {

    void getRecipes(GetRecipeCallback dataCallback);
    void getFavourites(GetFavouritesCallback dataCallback);
    void getMyRecipes(GetFavouritesCallback dataCallback);

    //Callback
    interface DefCallback extends DefaultCallback<String> {}
    interface GetRecipeCallback extends DefaultCallback<Recipe> {}
    interface GetFavouritesCallback extends DefaultCallback<List<Recipe>> {}
}
