package dima.sabor.recipeDetails;

import dima.sabor.base.BaseActivity;
import dima.sabor.model.Recipe;

public interface RecipeDetailsActivity extends BaseActivity{
    void onDetailsRetrieved(Recipe returnParam);
    //void setUpViewPager(List<String> images);
}
