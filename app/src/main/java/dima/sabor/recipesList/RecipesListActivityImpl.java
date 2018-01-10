package dima.sabor.recipesList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dima.sabor.R;
import dima.sabor.addRecipe.AddRecipeActivityImpl;
import dima.sabor.dependencyinjection.App;
import dima.sabor.dependencyinjection.activity.ActivityModule;
import dima.sabor.dependencyinjection.view.ViewModule;
import dima.sabor.menu.MenuActivityImpl;
import dima.sabor.model.Recipe;
import dima.sabor.recipeDetails.MyRecipeDetailsActivityImpl;
import dima.sabor.recipeDetails.RecipeDetailsActivityImpl;

public class RecipesListActivityImpl extends MenuActivityImpl implements RecipesListActivity {

    private List<Recipe> listrecipes;
    private List<String> listfav;

    @Inject
    ShowRecipesPresenter presenter;

    @BindView(R.id.recipes_recycler_view)
    RecyclerView recyclerViewRecipes;

    @BindView(R.id.recipes_add_recipe_fab)
    FloatingActionButton addProductButton;

    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        ((App) getApplication())
                .getComponent()
                .plus(new ActivityModule(this),
                        new ViewModule(this))
                .inject(this);

        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerViewRecipes.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewRecipes.getContext(),
                layoutManager.getOrientation());
        recyclerViewRecipes.addItemDecoration(dividerItemDecoration);

        listrecipes = new ArrayList<Recipe>();
        listfav = new ArrayList<String>();
        presenter.getRecipes();
        presenter.getFav();

        generateRecipes(listrecipes);

        addProductButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(RecipesListActivityImpl.this, AddRecipeActivityImpl.class));
            }
        });

    }

    public void generateRecipes(List<Recipe> recipes) {
       // Log.i("images","VIEW recipes: "+ recipes);

        adapter = new RecipesRecyclerViewAdapter(this, recipes) {
            @Override
            public void onItemClick(String gson, String userId) {
                if(userId.equals(presenter.getUserID())) {
                    presenter.saveRecInternalSt(gson);
                    Intent intent = new Intent(RecipesListActivityImpl.this, MyRecipeDetailsActivityImpl.class);
                    intent.putExtra("activity","all");
                    startActivity(intent);
                }
                else {
                    presenter.saveRecInternalSt(gson);
                    Intent intent = new Intent(RecipesListActivityImpl.this, RecipeDetailsActivityImpl.class);
                    startActivity(intent);
               }
            }

            @Override
            public void onFavouriteClick(Recipe recipe, boolean add) {
                if (add) {
                    Log.i("ADDFAV","list: "+listfav + "   " + recipe.getId());
                    presenter.addFav(recipe);
                    //listfav.add(recipe.getId());
                }
                else {
                    Log.i("DELETEFAV","list: "+listfav + "   " + recipe.getId());
                    presenter.deleteFav(recipe.getId());
                }
            }

            @Override
            public boolean isFav(String RecipeID) {
                //Log.i("DUDA","La lista contiene la receta1?: " + listfav.contains(RecipeID) +"    "+RecipeID);
                if(listfav.contains(RecipeID)) return true;
                else return false;
            }

        };

        recyclerViewRecipes.setAdapter(adapter);
    }

    public void addRecipe(Recipe recipe) {
        listrecipes.add(recipe);
        adapter.notifyDataSetChanged();

    }

    public void addFavourites(List<String> favs) {
        Log.i("ALLFAVS", "list: " +favs);
        listfav.clear();
        listfav = favs;
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        finish();
    }


}

