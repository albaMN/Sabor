package dima.sabor.data.impl;

import java.util.List;

import javax.inject.Inject;

import dima.sabor.data.FirebaseInterface;
import dima.sabor.data.InternalStorageInterface;
import dima.sabor.data.RepositoryInterface;
import dima.sabor.data.listener.ErrorBundle;
import dima.sabor.data.listener.FirebaseFavsListener;
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
    public void getRecipes(final GetFavouritesCallback dataCallback) {
        firebaseDataSource.getRecipes(/*new FirebaseRecipeListener() {
            @Override
            public void onNewRecipe(Map<String,Recipe> map) {
                //TODO: pasar map entero
                dataCallback.onSuccess(map.get("add"));
            }

            @Override
            public void onError(ErrorBundle bundle) {
                dataCallback.onError(bundle);
            }
        }*/
                new FirebaseFavsListener() {
                    @Override
                    public void onNewFav(List<Recipe> recipes) {
                        dataCallback.onSuccess(recipes);
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

    @Override
    public void getMyRecipes(final GetFavouritesCallback dataCallback) {
        firebaseDataSource.getMyRecipes(internalStorage.getUser().getUid(), new FirebaseFavsListener() {
            @Override
            public void onNewFav(List<Recipe> myrec) {
                dataCallback.onSuccess(myrec);
            }

            @Override
            public void onError(ErrorBundle bundle) {
                dataCallback.onError(bundle);
            }
        });
    }
}
