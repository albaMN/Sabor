package dima.sabor.addRecipe;

import java.util.List;

import javax.inject.Inject;

import dima.sabor.data.InternalStorageInterface;
import dima.sabor.data.impl.FirebaseDataSource;
import dima.sabor.model.Recipe;

public class AddRecipePresenter {

    private AddRecipeActivityImpl view;
    private FirebaseDataSource firebaseservice;
    private InternalStorageInterface internalStorage;

    @Inject
    public AddRecipePresenter(AddRecipeActivityImpl view,
                              FirebaseDataSource firebaseservice,
                              InternalStorageInterface internalStorage) {
        this.view = view;
        this.firebaseservice = firebaseservice;
        this.internalStorage = internalStorage;
    }

    public void addRecipe(String title, String time, String people, String difficulty, String place, List<String> ingredients, String description, List<String> images) {
        String userId = internalStorage.getUser().getUid();
        Recipe recipe = new Recipe("",userId,title,time,people,difficulty,place,ingredients,description,images);
        view.showProgress("Creating recipe...");
        String rid = firebaseservice.addRecipe(recipe);
        internalStorage.saveReceiptId(rid);
        view.goToShowProductList();
    }


}
