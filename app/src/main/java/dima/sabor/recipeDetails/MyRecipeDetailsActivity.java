package dima.sabor.recipeDetails;

import java.util.List;

import dima.sabor.base.BaseActivity;
import dima.sabor.model.Recipe;

public interface MyRecipeDetailsActivity extends BaseActivity{
    void onDetailsRetrieved(Recipe returnParam);
    void goToShowRecipesList();
    void setUpViewPager(List<String> images);
}
