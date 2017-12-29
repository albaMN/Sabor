package dima.sabor.recipeDetails;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import dima.sabor.R;


public class ImageViewPageAdapter extends PagerAdapter {

    private Context context;
    private List<Bitmap> imageUrls;

    public ImageViewPageAdapter(Context context, List<Bitmap> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.recipe_details_viewpager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.recipe_detail_viewpager_imageview);
        //Picasso.with(context).load(imageUrls.get(position)).into(imageView);
        imageView.setImageBitmap(imageUrls.get(position));

        container.addView(itemView);

        return itemView;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}
