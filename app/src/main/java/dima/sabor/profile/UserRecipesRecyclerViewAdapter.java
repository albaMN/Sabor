package dima.sabor.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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
        //TODO: Cambiar el layout
        View v = LayoutInflater.from(context).inflate(R.layout.my_recipes_list_item, viewGroup, false);
        return new ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        //TODO: Hacerlo bien

        //Picasso.with(context).load(product.get(i).getImages().get(0)).into(viewHolder.itemImage);
        Log.i("images","VIEW product: "+recipe.get(i).getId()+" images: "+ recipe.get(i).getImages() );
        if(recipe.get(i).getImages()!= null) {
           /* byte[] decodedString = Base64.decode(product.get(i).getImages().get(0), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            viewHolder.itemImage.setImageBitmap(decodedByte);*/
            Picasso.with(context).load(recipe.get(i).getImages().get(0)).into(viewHolder.itemImage);
        }

        viewHolder.itemTitle.setText(recipe.get(i).getTitle());
        viewHolder.itemCategory.setText("funciona2");
        viewHolder.itemPrice.setText("funciona1");

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

    class ViewHolder extends RecyclerView.ViewHolder{

        View itemView;

        @BindView(R.id.recipe_list_image2)
        public ImageView itemImage;

        @BindView(R.id.user_product_list_title2)
        public TextView itemTitle;

        @BindView(R.id.user_product_list_category2)
        public TextView itemCategory;

        @BindView(R.id.user_product_list_price2)
        public TextView itemPrice;

        /*@BindView(R.id.user_product_list_matchmaking_launch_button)
        public ImageButton itemMatchmaking;*/


        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

}
