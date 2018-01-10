package dima.sabor.recipeDetails;


import javax.inject.Inject;

import dima.sabor.data.InternalStorageInterface;
import dima.sabor.model.Recipe;

public class RecipeDetailsPresenter {
    private RecipeDetailsActivityImpl view;
    private InternalStorageInterface internalStorageInterface;

    @Inject
    public RecipeDetailsPresenter(RecipeDetailsActivityImpl view,
                                  InternalStorageInterface internalStorageInterface) {
        this.view = view;
        this.internalStorageInterface = internalStorageInterface;
    }

    public Recipe getActualRecipe() {
        return internalStorageInterface.getActualRecipe();
    }
}
