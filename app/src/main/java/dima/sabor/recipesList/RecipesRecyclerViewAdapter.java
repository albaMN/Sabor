package dima.sabor.recipesList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dima.sabor.R;
import dima.sabor.model.Recipe;


public abstract class RecipesRecyclerViewAdapter extends RecyclerView.Adapter<RecipesRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    public List<Recipe> recipe;

    public RecipesRecyclerViewAdapter(Context context, List<Recipe> lr) {
        this.context = context;
        this.recipe = lr;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.recipes_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        Picasso.with(context).load(recipe.get(i).getImages().get(0)).into(viewHolder.itemImage);
      /*  Log.i("images","VIEW product: "+product.get(i).getId()+" images: "+ product.get(i).getImages() );
        if(product.get(i).getImages()!= null) {
            byte[] decodedString = Base64.decode(product.get(i).getImages().get(0), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            viewHolder.itemImage.setImageBitmap(decodedByte);
        }*/

        viewHolder.itemTitle.setText(recipe.get(i).getTitle());
        viewHolder.itemCategory.setText(recipe.get(i).getDifficulty());

       /* String priceRange = product.get(i).getMinPrice() + " - " + product.get(i).getMaxPrice() + "€";
        viewHolder.itemPrice.setText(priceRange);

        viewHolder.itemMatchmaking.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onMatchMakingClick(product.get(viewHolder.getAdapterPosition()).getId());
            }
        });*/

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
    //abstract public void  onMatchMakingClick(int productID);

    class ViewHolder extends RecyclerView.ViewHolder{

        View itemView;

        @BindView(R.id.user_product_list_image)
        public ImageView itemImage;

        @BindView(R.id.user_product_list_title)
        public TextView itemTitle;

        @BindView(R.id.user_product_list_category)
        public TextView itemCategory;

        @BindView(R.id.user_product_list_price)
        public TextView itemPrice;

        @BindView(R.id.user_profile_ratingbar)
        public RatingBar ratingBar;
/*
        @BindView(R.id.user_product_list_matchmaking_launch_button)
        public ImageButton itemMatchmaking;

*/
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

}
