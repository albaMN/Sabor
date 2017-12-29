package dima.sabor.recipeDetails;


import javax.inject.Inject;

public class RecipeDetailsPresenter {
    private RecipeDetailsActivityImpl view;

    @Inject
    public RecipeDetailsPresenter(RecipeDetailsActivityImpl view) {
        this.view = view;
    }
}
