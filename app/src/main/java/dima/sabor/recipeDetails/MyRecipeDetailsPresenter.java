package dima.sabor.recipeDetails;


import javax.inject.Inject;

import dima.sabor.data.InternalStorageInterface;
import dima.sabor.data.impl.FirebaseDataSource;
import dima.sabor.model.Recipe;

public class MyRecipeDetailsPresenter {
    private MyRecipeDetailsActivityImpl view;
    private InternalStorageInterface internalStorageInterface;
    private FirebaseDataSource firebaseservice;

    @Inject
    public MyRecipeDetailsPresenter(MyRecipeDetailsActivityImpl view,
                                    InternalStorageInterface internalStorageInterface,
                                    FirebaseDataSource firebaseservice) {
        this.view = view;
        this.internalStorageInterface = internalStorageInterface;
        this.firebaseservice = firebaseservice;
    }

    public void deleteRecipe(Recipe recipe, String act) {
        firebaseservice.deleteRecipe(internalStorageInterface.getUser().getUid(), recipe.getId());
        if (act.equals("all")) view.goToShowRecipesList();
        else if (act.equals("mine")) view.goToShowMyRecipesList();
    }

    public Recipe getMyActualRecipe() {
        return internalStorageInterface.getActualRecipe();
    }
}
