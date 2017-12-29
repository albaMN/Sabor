package dima.sabor.addRecipe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dima.sabor.R;

public abstract class AddDifficultyRecipeAdapter extends RecyclerView.Adapter<AddDifficultyRecipeAdapter.ViewHolder>{

    private Context context;
    private List<String> categories;

    public AddDifficultyRecipeAdapter(Context context, List<String> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_recipe_difficulty_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.categoryName.setText(categories.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCategoryClick(categories.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public abstract void onCategoryClick(String category);

    class ViewHolder extends RecyclerView.ViewHolder{

        View itemView;

        @BindView(R.id.add_recipe_difficulty_name)
        TextView categoryName;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            ButterKnife.bind(this, itemView);
        }
    }
}
