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
import dima.sabor.recipeDetails.RecipeDetailsActivityImpl;

public class RecipesListActivityImpl extends MenuActivityImpl implements RecipesListActivity {

    private List<Recipe> listrecipes;

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
        presenter.getRecipes();

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
        Log.i("images","VIEW recipes: "+ recipes);

        adapter = new RecipesRecyclerViewAdapter(this, recipes) {
            @Override
            public void onItemClick(String gson) {
                Intent intent = new Intent(RecipesListActivityImpl.this, RecipeDetailsActivityImpl.class);
                intent.putExtra("recipe", gson);
                startActivity(intent);
            }

        };

        recyclerViewRecipes.setAdapter(adapter);
    }

    public void addRecipe(Recipe recipe) {
        listrecipes.add(recipe);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        finish();
    }


}

