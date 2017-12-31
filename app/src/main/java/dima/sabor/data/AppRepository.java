package dima.sabor.data;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dima.sabor.base.useCase.RepositoryInterface;
import dima.sabor.data.listener.ErrorBundle;
import dima.sabor.data.listener.FirebaseFavsListener;
import dima.sabor.data.listener.FirebaseRecipeListener;
import dima.sabor.model.Recipe;

public class AppRepository implements RepositoryInterface {
    private FirebaseInterface firebaseDataSource;
    private InternalStorageInterface internalStorage;

    @Inject
    public AppRepository(InternalStorageInterface internalStorage, FirebaseInterface firebaseDataSource) {
        this.internalStorage = internalStorage;
        this.firebaseDataSource = firebaseDataSource;
    }

    @Override
    public void getUserRecipes(String userid, final GetRecipeCallback dataCallback) {
        firebaseDataSource.getMyRecipes(userid, new FirebaseRecipeListener() {
            @Override
            public void onNewRecipe(Map<String,Recipe> map) {
                //TODO: pasar map entero
                dataCallback.onSuccess(map.get("add"));
            }

            @Override
            public void onError(ErrorBundle bundle) {
                dataCallback.onError(bundle);
            }

        });
    }


    @Override
    public void getRecipes(final GetRecipeCallback dataCallback) {
        firebaseDataSource.getRecipes(new FirebaseRecipeListener() {
            @Override
            public void onNewRecipe(Map<String,Recipe> map) {
                //TODO: pasar map entero
                dataCallback.onSuccess(map.get("add"));
            }

            @Override
            public void onError(ErrorBundle bundle) {
                dataCallback.onError(bundle);
            }
        });
    }

    @Override
    public void getFavourites(final GetFavouritesCallback dataCallback) {
        firebaseDataSource.isFav(internalStorage.getUser().getUid(), new FirebaseFavsListener() {
            @Override
            public void onNewFav(List<Recipe> favs) {
                dataCallback.onSuccess(favs);
            }

            @Override
            public void onError(ErrorBundle bundle) {
                dataCallback.onError(bundle);
            }
        });
    }


}
