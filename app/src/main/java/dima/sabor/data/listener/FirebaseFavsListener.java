package dima.sabor.data.listener;

import java.util.List;

import dima.sabor.model.Recipe;

public interface FirebaseFavsListener {
    void onNewFav(List<Recipe> favs);

    void onError(ErrorBundle bundle);
}
