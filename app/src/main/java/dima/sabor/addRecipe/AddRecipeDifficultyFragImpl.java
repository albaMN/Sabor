package dima.sabor.addRecipe;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import dima.sabor.R;
import dima.sabor.utils.RecipeDifficulties;

public class AddRecipeDifficultyFragImpl extends Fragment {
    @BindView(R.id.add_difficulty_recipe_recyclerview)
    RecyclerView difficultyRecyclerView;

    @BindView(R.id.add_difficulty_recipe_close_button)
    ImageButton backButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_difficulty_recipe, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        difficultyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AddRecipeActivityImpl) getActivity()).removeFullScreenFragment();
            }
        });

        AddDifficultyRecipeAdapter adapter = new AddDifficultyRecipeAdapter(getActivity(), RecipeDifficulties.getAllDifficulties()) {
            @Override
            public void onCategoryClick(String category) {
                ((AddRecipeActivityImpl) getActivity()).onDifficultyPressed(category);
                ((AddRecipeActivityImpl) getActivity()).removeFullScreenFragment();
            }
        };

        difficultyRecyclerView.setAdapter(adapter);
    }
}
