package dima.sabor.dependencyinjection.view;

import dagger.Module;
import dagger.Provides;
import dima.sabor.addRecipe.AddRecipeActivityImpl;
import dima.sabor.base.BaseActivity;
import dima.sabor.login.SignInActivityImpl;
import dima.sabor.menu.MenuActivityImpl;
import dima.sabor.profile.OptionsProfileActivityImpl;
import dima.sabor.profile.ProfileActivityImpl;
import dima.sabor.recipeDetails.RecipeDetailsActivityImpl;
import dima.sabor.recipesList.RecipesListActivityImpl;
import dima.sabor.splash.SplashActivityImpl;


@Module
public class ViewModule {

    private BaseActivity view;

    public ViewModule(BaseActivity view) {
        this.view = view;
    }

    @Provides
    BaseActivity providesBaseView(){
        return view;
    }

    @Provides
    SplashActivityImpl providesSplashView(){
        return (SplashActivityImpl) view;
    }

    @Provides
    SignInActivityImpl providesLoginView(){
        return (SignInActivityImpl) view;
    }

    @Provides
    MenuActivityImpl providesMenuView() {
        return (MenuActivityImpl) view;
    }

    @Provides
    OptionsProfileActivityImpl providesOptionsProfileView() {
        return (OptionsProfileActivityImpl) view;
    }

    @Provides
    AddRecipeActivityImpl providesAddRecipeView() {
        return (AddRecipeActivityImpl) view;
    }

    @Provides
    RecipesListActivityImpl providesShowRecipesView() {
        return (RecipesListActivityImpl) view;
    }

    @Provides
    ProfileActivityImpl providesProfileView(){
        return (ProfileActivityImpl) view;
    }

    @Provides
    RecipeDetailsActivityImpl providesRecipeDetailsView(){
        return (RecipeDetailsActivityImpl) view;
    }
}