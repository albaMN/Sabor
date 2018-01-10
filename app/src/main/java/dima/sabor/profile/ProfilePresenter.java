package dima.sabor.profile;

import java.util.List;

import javax.inject.Inject;

import dima.sabor.data.InternalStorageInterface;
import dima.sabor.data.impl.FirebaseDataSource;
import dima.sabor.data.listener.ErrorBundle;
import dima.sabor.data.usecase.GetFavouritesUseCase;
import dima.sabor.data.usecase.GetMyRecipesUseCase;
import dima.sabor.model.Recipe;
import dima.sabor.model.User;

public class ProfilePresenter {

    private ProfileActivityImpl view;
    private FirebaseDataSource firebaseservice;
    private InternalStorageInterface internalStorage;
    private GetMyRecipesUseCase getMyRecipesUseCase;
    private GetFavouritesUseCase getFavouritesUseCase;

    @Inject
    public ProfilePresenter(ProfileActivityImpl view,
                            FirebaseDataSource firebaseservice,
                            InternalStorageInterface internalStorage,
                            GetMyRecipesUseCase getMyRecipesUseCase,
                            GetFavouritesUseCase getFavouritesUseCase) {
        this.view = view;
        this.firebaseservice = firebaseservice;
        this.internalStorage = internalStorage;
        this.getMyRecipesUseCase = getMyRecipesUseCase;
        this.getFavouritesUseCase = getFavouritesUseCase;
    }


    public User getUser() {
        return internalStorage.getUser();
    }

    public void showProfile() {
        view.showProgress("Loading profile...");
        view.onProfileRetrieved(getUser());
    }

    public void updateUser(User user) {
        firebaseservice.updateUser(user);
    }

    public void updateImageUser(User user, String imageEncoded) {
        firebaseservice.updateImageUser(user, imageEncoded);
        user.setPhoto_url(imageEncoded);
        internalStorage.saveUser(user);
    }

    public void getMyRecipes() {
        getMyRecipesUseCase.execute("", new GetMyRecipesUseCase.GetMyRecipesListener() {
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

    public void getMyFavourites() {
        getFavouritesUseCase.execute("",new GetFavouritesUseCase.GetFavouritesListener() {
            @Override
            public void onError(ErrorBundle errorBundle) {
                view.onError(errorBundle.getErrorMessage());
            }

            @Override
            public void onSuccess(List<Recipe> returnParam) {
                view.addFavourites(returnParam);
            }
        });
    }

    public void deleteFav(String RecipeID) {
        firebaseservice.deleteFav(internalStorage.getUser().getUid(), RecipeID);
    }

    public void deleteRecipe(Recipe recipe) {
        firebaseservice.deleteRecipe(internalStorage.getUser().getUid(), recipe.getId());
    }

    public void saveRecInternalSt(String gsonRecipe) {
        internalStorage.saveActualRecipe(gsonRecipe);
    }

}