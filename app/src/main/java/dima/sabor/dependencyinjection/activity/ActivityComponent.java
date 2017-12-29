package dima.sabor.dependencyinjection.activity;

import dagger.Subcomponent;
import dima.sabor.addRecipe.AddRecipeActivityImpl;
import dima.sabor.base.BaseActivityImpl;
import dima.sabor.dependencyinjection.annotation.scope.PerActivity;
import dima.sabor.dependencyinjection.view.ViewModule;
import dima.sabor.login.SignInActivityImpl;
import dima.sabor.menu.MenuActivityImpl;
import dima.sabor.profile.OptionsProfileActivityImpl;
import dima.sabor.profile.ProfileActivityImpl;
import dima.sabor.recipeDetails.RecipeDetailsActivityImpl;
import dima.sabor.recipesList.RecipesListActivityImpl;
import dima.sabor.splash.SplashActivityImpl;


@PerActivity
@Subcomponent(modules = {ActivityModule.class, ViewModule.class})
public interface ActivityComponent {

    void inject(BaseActivityImpl activity);

    void inject(SplashActivityImpl activity);

    void inject(SignInActivityImpl activity);

    void inject(MenuActivityImpl activity);

    void inject(OptionsProfileActivityImpl activity);

    void inject(AddRecipeActivityImpl activity);

    void inject(RecipesListActivityImpl activity);

    void inject(ProfileActivityImpl activity);

    void inject(RecipeDetailsActivityImpl activity);
}