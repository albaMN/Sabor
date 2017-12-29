package dima.sabor.profile;

import java.util.List;

import javax.inject.Inject;

import dima.sabor.data.InternalStorageInterface;
import dima.sabor.data.impl.FirebaseDataSource;
import dima.sabor.data.listener.ErrorBundle;
import dima.sabor.data.usecase.GetRecipesUserUseCase;
import dima.sabor.model.Recipe;
import dima.sabor.model.User;

public class ProfilePresenter {

    private ProfileActivityImpl view;
    private FirebaseDataSource firebaseservice;
    private InternalStorageInterface internalStorage;
    private GetRecipesUserUseCase getRecipesUserUseCase;

    @Inject
    public ProfilePresenter(ProfileActivityImpl view,
                            FirebaseDataSource firebaseservice,
                            InternalStorageInterface internalStorage,
                            GetRecipesUserUseCase getRecipesUserUseCase) {
        this.view = view;
        this.firebaseservice = firebaseservice;
        this.internalStorage = internalStorage;
        this.getRecipesUserUseCase = getRecipesUserUseCase;
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
    }

    public void getMyRecipes() {
        getRecipesUserUseCase.execute(getUser().getUid(), new GetRecipesUserUseCase.GetMyRecipesListener() {
            @Override
            public void onError(ErrorBundle errorBundle) {
                view.onError(errorBundle.getErrorMessage());
            }

            @Override
            public void onSuccess(Recipe returnParam) {
                view.addRecipe(returnParam);
            }
        });
    }

    public List<Recipe> getMyFavourites() {
        return firebaseservice.getMyFavourites(getUser().getUid());
    }
}