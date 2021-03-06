package dima.sabor.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dima.sabor.R;
import dima.sabor.model.Recipe;


public abstract class UserRecipesRecyclerViewAdapter extends RecyclerView.Adapter<UserRecipesRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    public List<Recipe> recipe;

    public UserRecipesRecyclerViewAdapter(Context context, List<Recipe> lr) {
        this.context = context;
        this.recipe = lr;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_recipes_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        if(recipe.get(i).getImages()!= null) {
            byte[] decodedString = Base64.decode(recipe.get(i).getImages().get(0), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            viewHolder.itemImage.setImageBitmap(decodedByte);
        }

        viewHolder.itemTitle.setText(recipe.get(i).getTitle());
        viewHolder.itemDifficulty.setText("Difficulty: " + recipe.get(i).getDifficulty());
        viewHolder.itemTime.setText("Time: "+ recipe.get(i).getTime()+"h");


        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeletedClick(recipe.get(viewHolder.getAdapterPosition()));
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Gson gson= new Gson();
                String json = gson.toJson(recipe.get(viewHolder.getAdapterPosition()));
                onItemClick(json);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipe.size();
    }

    abstract public void onItemClick(String gson);
    abstract public void  onDeletedClick(Recipe r);

    class ViewHolder extends RecyclerView.ViewHolder{

        View itemView;

        @BindView(R.id.user_recipe_list_image2)
        public ImageView itemImage;

        @BindView(R.id.user_recipe_list_title2)
        public TextView itemTitle;

        @BindView(R.id.user_recipe_list_difficulty2)
        public TextView itemDifficulty;

        @BindView(R.id.user_recipe_list_time2)
        public TextView itemTime;

        @BindView(R.id.user_recipe_list_delete2)
        public ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

}
