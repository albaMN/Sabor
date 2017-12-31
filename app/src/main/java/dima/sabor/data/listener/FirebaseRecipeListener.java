package dima.sabor.data.listener;

import java.util.Map;

import dima.sabor.model.Recipe;

public interface FirebaseRecipeListener {

    void onNewRecipe(Map<String,Recipe> map);

    void onError(ErrorBundle bundle);

}
