package dima.sabor.recipeDetails;

import dima.sabor.base.BaseActivity;
import dima.sabor.model.Recipe;

public interface MyRecipeDetailsActivity extends BaseActivity{
    void onDetailsRetrieved(Recipe returnParam);
    void goToShowRecipesList();
    void goToShowMyRecipesList();
    //void setUpViewPager(List<String> images);
}
