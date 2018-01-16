package dima.sabor.recipesList;


import java.util.List;

import dima.sabor.menu.MenuActivity;

public interface RecipesListActivity extends MenuActivity {
    void addFavourites(List<String> res);

}
