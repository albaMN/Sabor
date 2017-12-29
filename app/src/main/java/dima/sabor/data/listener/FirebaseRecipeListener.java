package dima.sabor.data.listener;

import dima.sabor.model.Recipe;

public interface FirebaseRecipeListener {

    void onNewRecipe(Recipe recipe);

    void onError(ErrorBundle bundle);

}
