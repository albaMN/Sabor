package dima.sabor.recipesList;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dima.sabor.data.InternalStorageInterface;
import dima.sabor.data.impl.FirebaseDataSource;
import dima.sabor.data.listener.ErrorBundle;
import dima.sabor.data.usecase.GetFavouritesUseCase;
import dima.sabor.data.usecase.GetRecipesUseCase;
import dima.sabor.model.Recipe;

public class ShowRecipesPresenter {

    private RecipesListActivityImpl view;
    private FirebaseDataSource firebaseservice;
    private GetRecipesUseCase getRecipesUseCase;
    private GetFavouritesUseCase getFavouritesUseCase;
    private InternalStorageInterface internalstorage;

    @Inject
    public ShowRecipesPresenter(RecipesListActivityImpl view,
                                FirebaseDataSource firebaseservice,
                                InternalStorageInterface internalstorage,
                                GetRecipesUseCase getRecipesUseCase,
                                GetFavouritesUseCase getFavouriesUseCase) {

        this.view = view;
        this.firebaseservice = firebaseservice;
        this.internalstorage = internalstorage;
        this.getRecipesUseCase = getRecipesUseCase;
        this.getFavouritesUseCase = getFavouriesUseCase;
    }

    public void getRecipes() {
        getRecipesUseCase.execute("",new GetRecipesUseCase.GetRecipesListener() {
            @Override
            public void onError(ErrorBundle errorBundle) {
                view.onError(errorBundle.getErrorMessage());
            }

            @Override
            public void onSuccess(List<Recipe> returnParam) {
                view.addRecipe(returnParam);
            }
        });
    }

    public void getFav() {
        getFavouritesUseCase.execute("",new GetFavouritesUseCase.GetFavouritesListener() {
            @Override
            public void onError(ErrorBundle errorBundle) {
                view.onError(errorBundle.getErrorMessage());
            }

            @Override
            public void onSuccess(List<Recipe> returnParam) {
                List<String> res = new ArrayList<String>();
                for(Recipe r: returnParam){
                    res.add(r.getId());
                }
                view.addFavourites(res);
            }
        });
    }



    public void deleteFav(String RecipeID) {
        firebaseservice.deleteFav(internalstorage.getUser().getUid(), RecipeID);
    }

    public void addFav(Recipe recipe) {
        firebaseservice.addFav(internalstorage.getUser().getUid(), recipe);
    }

    public String getUserID() {
        return internalstorage.getUser().getUid();
    }

    public void saveRecInternalSt(String gsonRecipe) {
        internalstorage.saveActualRecipe(gsonRecipe);
    }
}
