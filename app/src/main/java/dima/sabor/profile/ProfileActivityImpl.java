package dima.sabor.profile;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dima.sabor.R;
import dima.sabor.dependencyinjection.App;
import dima.sabor.dependencyinjection.activity.ActivityModule;
import dima.sabor.dependencyinjection.view.ViewModule;
import dima.sabor.menu.MenuActivityImpl;
import dima.sabor.model.Recipe;
import dima.sabor.model.User;
import dima.sabor.recipeDetails.RecipeDetailsActivityImpl;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class ProfileActivityImpl extends MenuActivityImpl implements ProfileActivity {

    private static final int PICTURE_TAKEN_FROM_GALLERY = 2;
    private static final int PICTURE_TAKEN_FROM_CAMERA = 1;
    private static final int MY_PERMISSIONS = 3;
    private User user;

    private String profileimage;
    private String nPath;

    private List<Recipe> mylistrecipes;
    private List<Recipe> myfavourites;

    @Inject
    ProfilePresenter presenter;

    /*@BindView(R.id.options)
    ImageView options;*/

    @BindView(R.id.user_profile_avatar)
    ImageView userAvatar;

    @BindView(R.id.user_profile_name)
    TextView userName;

    @BindView(R.id.user_profile_num_recipes)
    TextView userNumRec;

    @BindView(R.id.tabHostProfile)
    TabHost tabHost;

    @BindView(R.id.user_recipes_recycler_view2)
    RecyclerView recyclerView;

    @BindView(R.id.user_recipes_recycler_view3)
    RecyclerView recyclerView2;

    RecyclerView.Adapter adapter1;
    RecyclerView.Adapter adapter2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ((App) getApplication())
                .getComponent()
                .plus(new ActivityModule(this),
                        new ViewModule(this))
                .inject(this);

        ButterKnife.bind(this);
        showProfile();

        setupMyRecipes();
        setupMyFavourites();

        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("RECIPES");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("FAVOURITES");

        tab1.setIndicator("My recipes");
        tab1.setContent(R.id.layoutMyRecipes);

        tab2.setIndicator("Favourites");
        tab2.setContent(R.id.layoutMyFavourites);

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);

        if(tabHost.getCurrentTab() == 0) generateRecipes(mylistrecipes);
        else generateFavourites(myfavourites);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                if(s.equals("RECIPES")) {
                    generateRecipes(mylistrecipes);
                }
                else if(s.equals("FAVOURITES")) {
                    generateFavourites(myfavourites);
                }
            }
        });

        //TODO: Eliminar todo lo que tenga que ver con las opciones

    /*
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // addFullScreenFragment(new OptionsProfileFragImpl());
                startActivity(new Intent(ProfileActivityImpl.this, OptionsProfileActivityImpl.class));
            }
        }); */

        userAvatar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                changeProfileImage();
            }
        });

    }

    public void setupMyRecipes() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        mylistrecipes = new ArrayList<Recipe>();
        presenter.getMyRecipes();

        generateRecipes(mylistrecipes);
        Log.i("RECIPES:", "Generating recipes");
    }

    public void setupMyFavourites() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView2.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView2.getContext(),
                layoutManager.getOrientation());
        recyclerView2.addItemDecoration(dividerItemDecoration);

        myfavourites = new ArrayList<Recipe>();
        presenter.getMyFavourites();

        generateFavourites(myfavourites);
    }

    public void generateRecipes(List<Recipe> recipes) {

        adapter1 = new UserRecipesRecyclerViewAdapter(this, recipes) {
            @Override
            public void onItemClick(String gson) {
               // Intent intent = new Intent(ProfileActivityImpl.this, UserProductDetailsActivityImpl.class);
                //intent.putExtra("recipe", gson);
                //startActivity(intent);
            }

            @Override
            public void onDeletedClick(final Recipe r) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivityImpl.this);

                builder.setMessage("Are you sure you want to delete this recipe permanently?")
                        .setTitle("Confirmation")
                        .setPositiveButton("Accept", new DialogInterface.OnClickListener()  {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("Dialogs", "Accepted confirmation.");
                                presenter.deleteRecipe(r);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Log.i("Dialogs", "Denied confirmation.");
                                dialog.cancel();
                            }
                        });

                builder.create().show();

            }
        };

        recyclerView.setAdapter(adapter1);
    }

    public void generateFavourites(List<Recipe> recipes) {
       adapter2 = new UserFavouritesRecyclerViewAdapter(this, recipes) {
            @Override
            public void onItemClick(String gson) {
                Intent intent = new Intent(ProfileActivityImpl.this, RecipeDetailsActivityImpl.class);
                intent.putExtra("recipe", gson);
                startActivity(intent);
            }

            @Override
            public void onFavouriteClick(Recipe recipe) {
                presenter.deleteFav(recipe.getId());
            }
        };
        recyclerView2.setAdapter(adapter2);
    }

    public void addRecipe(List<Recipe> recipes) {
       mylistrecipes.clear();
       mylistrecipes = recipes;
       userNumRec.setText(String.valueOf(recipes.size()));
       //adapter1.notifyDataSetChanged();
       generateRecipes(mylistrecipes);
    }

    public void addFavourites(List<Recipe> fav) {
        myfavourites.clear();
        myfavourites = fav;
        //adapter2.notifyDataSetChanged();
        generateFavourites(myfavourites);
    }


    public void showProfile() {
        presenter.showProfile();
    }

    public void removeFullScreenFragment(){
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.base_container)).commit();
    }

    public void onProfileRetrieved(User user) {
        hideProgress();
        this.user = user;
        userName.setText(user.getUsername());
        //userNumRec.setText(user.getEmail());
        if(user.getPhoto_url() != "") {
            if (!user.getPhoto_url().contains("http")) {
                try {
                    Bitmap imageBitmap = decodeFromFirebaseBase64(user.getPhoto_url());
                    userAvatar.setImageBitmap(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

                Picasso.with(this).load(user.getPhoto_url()).into(userAvatar);
            }
        }
    }

    public User getInfo(){
        return user;
    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }


    private void changeProfileImage() {
        if(mayRequestStoragePermission()) userAvatar.setEnabled(true);
        else userAvatar.setEnabled(false);
        final CharSequence[] options ={"Take a picture", "Open gallery"};
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileActivityImpl.this);
        builder.setTitle("Change profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(options[which] == "Take a picture") {
                    getPictureFromCamera();
                }
                else if (options[which] == "Open gallery") {
                    getPictureFromGallery();
                }
                else dialog.dismiss();
            }
        });
        builder.show();
    }

    private void getPictureFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("error","Error when creating the file");
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile);
                startActivityForResult(takePictureIntent, PICTURE_TAKEN_FROM_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = String.valueOf(System.currentTimeMillis()/1000);
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );
        nPath = image.getAbsolutePath();
        return image;
    }

    private void getPictureFromGallery(){
        Intent gallerypickerIntent = new Intent(Intent.ACTION_PICK);
        gallerypickerIntent.setType("image/*");
        startActivityForResult(gallerypickerIntent, PICTURE_TAKEN_FROM_GALLERY);
    }

    private boolean mayRequestStoragePermission() {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(new RelativeLayout(getApplicationContext()), "The permissions are necessary to be able to use the application",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            });
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "Accepted permits", Toast.LENGTH_SHORT).show();
                userAvatar.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }
    private void showExplanation() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Denied permits");
        builder.setMessage("To use the functions of the application you need to accept the permits");
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICTURE_TAKEN_FROM_CAMERA && resultCode == RESULT_OK ) {
            MediaScannerConnection.scanFile(this, new String[]{nPath}, null,
                    new MediaScannerConnection.OnScanCompletedListener (){
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned" + path + "i");
                            Log.i("ExternalStorage", "-> Uri = " + uri);
                        }
                    });
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            userAvatar.setImageBitmap(imageBitmap);
            profileimage = BitMapToString(imageBitmap);
            user.setPhoto_url(profileimage);
            presenter.updateImageUser(user, profileimage);
        }
        else if (requestCode == PICTURE_TAKEN_FROM_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                nPath = getPathFromURI(selectedImageUri);
                Bitmap bm = BitmapFactory.decodeFile(nPath);
                profileimage = BitMapToString(bm);
                userAvatar.setImageURI(selectedImageUri);
                user.setPhoto_url(profileimage);
                presenter.updateImageUser(user, profileimage);
            }
        }
    }

    private String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        return imageEncoded;
    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }


}
