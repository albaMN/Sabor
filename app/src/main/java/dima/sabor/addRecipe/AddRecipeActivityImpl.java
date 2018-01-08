package dima.sabor.addRecipe;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dima.sabor.NonScrollListView;
import dima.sabor.R;
import dima.sabor.base.BaseActivityImpl;
import dima.sabor.dependencyinjection.App;
import dima.sabor.dependencyinjection.activity.ActivityModule;
import dima.sabor.dependencyinjection.view.ViewModule;
import dima.sabor.profile.ProfileActivityImpl;
import dima.sabor.utils.AddRecipeSquareImageView;
import dima.sabor.utils.RecipeChecker;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AddRecipeActivityImpl extends BaseActivityImpl implements AddRecipeActivity {

        private final int MY_PERMISSIONS = 3;
        private final int PICTURE_TAKEN_FROM_CAMERA = 1;
        private final int PICTURE_TAKEN_FROM_GALLERY = 2;
        private String  photo1, photo2, photo3, photo4, nPath;
        private int num_photo;

        private final int PLACE_PICKER_REQUEST = 4;
        String[] perms = {WRITE_EXTERNAL_STORAGE, "android.permission.CAMERA", "android.permission.ACCESS_FINE_LOCATION"};
        private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5;

        private static final String TAG = "AddRecipeActivityImpl";
        private String category;
        private RelativeLayout mRlView;

        private ArrayAdapter<String> arrayAdapter;
        private ArrayList<String> ingredients;

        private String place;

        AddRecipeDifficultyFragImpl addDifficultyFrag;

        @BindView(R.id.recipeName)
        EditText e_title;

        @BindView(R.id.description)
        EditText e_description;

        @BindView(R.id.cookingTime)
        EditText e_time;

        @BindView(R.id.numberOfPeople)
        EditText e_people;

        @BindView(R.id.recipeDifficultyLevel)
        EditText e_difficulty;

        @BindView(R.id.ingredient)
        EditText e_ingredient;

        @BindView(R.id.addIngredient)
        Button addIngredient;

        @BindView(R.id.listIngredients)
        NonScrollListView e_listIngredients;

        @BindView(R.id.place)
        EditText e_place;

        @BindView(R.id.add_product_close_button)
        ImageButton closeButton;

        @BindView(R.id.add_product_send_button)
        ImageButton addButton;

        @BindView(R.id.image1)
        AddRecipeSquareImageView e_photo1;

        @BindView(R.id.image2)
        AddRecipeSquareImageView e_photo2;

        @BindView(R.id.image3)
        AddRecipeSquareImageView e_photo3;

        @BindView(R.id.image4)
        AddRecipeSquareImageView e_photo4;


        @Inject
        AddRecipePresenter presenter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_recipe);
            ((App) getApplication())
                    .getComponent()
                    .plus(new ActivityModule(this),
                            new ViewModule(this))
                    .inject(this);
            ButterKnife.bind(this);

            category = photo1 = photo2 = photo3 = photo4="";
            ingredients = new ArrayList<>();

            addDifficultyFrag = new AddRecipeDifficultyFragImpl();
            e_difficulty.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick (View v)
                {
                    addFullScreenFragment(new AddRecipeDifficultyFragImpl());
                }
            });

            //Pedir permisos camara + localizacion
            requestPermissions(perms, MY_PERMISSIONS);
            enableornot(perms);

            e_place.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    placeIntent();
                } });

            addIngredient.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick (View v)
                {
                    if(e_ingredient.getText().toString().length() > 0) {
                        ingredients.add(e_ingredient.getText().toString());
                        arrayAdapter.notifyDataSetChanged();
                        e_ingredient.setText("");
                    }
                    else {
                        Toast.makeText(getApplicationContext(),
                                "Write an ingredient", Toast.LENGTH_LONG).show();
                    }
                }
            });

            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ingredients);
            e_listIngredients.setAdapter(arrayAdapter);

            e_listIngredients.setTextFilterEnabled(true);

            e_listIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String pos = arrayAdapter.getItem(position);
                    //onIngredientPressed(pos);
                }
            });

            closeButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onBackPressed();
                    finish();
                }
            });

            addButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onAddProductPressed();
                }
            });

            addEvents();
        }


        private void placeIntent() {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }

        private void enableornot(String [] per) {
            if ((checkSelfPermission(per[0]) == PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(per[1]) == PackageManager.PERMISSION_GRANTED)) {
                e_photo1.setEnabled(true);
                e_photo2.setEnabled(true);
                e_photo3.setEnabled(true);
                e_photo4.setEnabled(true);
            }
            else {
                e_photo1.setEnabled(false);
                e_photo2.setEnabled(false);
                e_photo3.setEnabled(false);
                e_photo4.setEnabled(false);
            }

            if (checkSelfPermission(per[2]) == PackageManager.PERMISSION_GRANTED) e_place.setEnabled(true);
            else e_place.setEnabled(false);
        }


        private void addEvents(){
            e_photo1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    num_photo = 1;
                    showOptions();
                }
            });

            e_photo2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    num_photo = 2;
                    showOptions();
                }
            });

            e_photo3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    num_photo = 3;
                    showOptions();
                }
            });

            e_photo4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    num_photo = 4;
                    showOptions();
                }
            });
        }


        private void showOptions() {
            final CharSequence[] options ={"Take a picture", "Open gallery", "Cancel"};
            //final AlertDialog.Builder builder = new AlertDialog.Builder(AddRecipeActivityImpl.this);
            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AddRecipeActivityImpl.this);
            builder.setTitle("Choose an option");
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
                    Log.e("error","error while creating the File");
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

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if(requestCode == MY_PERMISSIONS){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    e_photo1.setEnabled(true);
                    e_photo2.setEnabled(true);
                    e_photo3.setEnabled(true);
                    e_photo4.setEnabled(true);
                }
                else if (grantResults[2] == PackageManager.PERMISSION_GRANTED) e_place.setEnabled(true);
                boolean allPermissionsGranted = true;
                if(grantResults.length>0){
                    for(int grantResult: grantResults){
                        if(grantResult != PackageManager.PERMISSION_GRANTED){
                            allPermissionsGranted = false;
                            break;
                        }
                    }
                }
                if(!allPermissionsGranted) showExplanation();
            }
            //else showExplanation();

        }

        private void showExplanation() {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddRecipeActivityImpl.this);
            builder.setTitle("Denied permits");
            builder.setMessage("To use the functions of the application you need to accept the permits");
            builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri  uri = Uri.fromParts("package", getPackageName(), null);
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

        // Get the real path from the URI
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

        //path to base64
        public String BitMapToString(Bitmap bitmap) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            return imageEncoded;
        }

        public void onAddProductPressed() {
            String title = e_title.getText().toString();
            String description = e_description.getText().toString();
            String people = e_people.getText().toString();
            String time = e_time.getText().toString();
            //String place = e_place.getText().toString();
            String difficulty = e_difficulty.getText().toString();
            try {
                RecipeChecker.checkTitle(title);
                RecipeChecker.checkTime(time);
                RecipeChecker.checkPeople(people);
                RecipeChecker.checkDifficulty(difficulty);
                RecipeChecker.checkPlace(place);
                RecipeChecker.checkIngredients(ingredients);
                RecipeChecker.checkDescription(description);
                RecipeChecker.checkImages(photo1,photo2,photo3,photo4);
                List<String> images = new ArrayList<String>();
                if (!photo1.isEmpty()) images.add(photo1);
                if (!photo2.isEmpty()) images.add(photo2);
                if (!photo3.isEmpty()) images.add(photo3);
                if (!photo4.isEmpty()) images.add(photo4);
                presenter.addRecipe(title, time, people, difficulty, place, ingredients, description, images);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        public void goToShowProductList(){
            hideProgress();
            /*Toast.makeText(getApplicationContext(),
                "Producto añadido correctamente", Toast.LENGTH_LONG).show();*/
            /*TODO: te rediccionara al perfil del usuario que alli es donde se carga, o se puede ir al general
            y que haga como instagram que se vea como carga el producto?? */
            startActivity(new Intent(this, ProfileActivityImpl.class));
            finish();
        }

        public void onDifficultyPressed(String cat){
            category = cat.toLowerCase();
            e_difficulty.setText(cat);
        }




        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
                Place pl = PlacePicker.getPlace(data, this);
                //String toastMsg = String.format("Place: %s", place.getName());
                //Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                if(pl.getName() != null) {
                    e_place.setText(pl.getName());
                    place = pl.getName()+"#"+pl.getLatLng().toString();
                    Log.i("PLACE", "LatLong: "+pl.getLatLng());
                }
            }

            else if (requestCode == PICTURE_TAKEN_FROM_CAMERA && resultCode == RESULT_OK ) {
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
                String encoded_image = BitMapToString (imageBitmap);

                if (num_photo == 1) {
                    e_photo1.setImageBitmap(imageBitmap);
                    photo1 = encoded_image;
                }
                else if (num_photo == 2) {
                    e_photo2.setImageBitmap(imageBitmap);
                    photo2 = encoded_image;
                }
                else if (num_photo == 3) {
                    e_photo3.setImageBitmap(imageBitmap);
                    photo3 = encoded_image;
                }
                else {
                    e_photo4.setImageBitmap(imageBitmap);
                    photo4 = encoded_image;
                }

            }
            else if (requestCode == PICTURE_TAKEN_FROM_GALLERY && resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    nPath = getPathFromURI(selectedImageUri);
                    Bitmap bm = BitmapFactory.decodeFile(nPath);
                    String encoded_image = BitMapToString (bm);
                    if (num_photo == 1) {
                        e_photo1.setImageURI(selectedImageUri);
                        photo1 = encoded_image;
                    }
                    else if (num_photo == 2) {
                        e_photo2.setImageURI(selectedImageUri);
                        photo2 =  encoded_image;
                    }
                    else if (num_photo == 3) {
                        e_photo3.setImageURI(selectedImageUri);
                        photo3 =  encoded_image;
                    }
                    else {
                        e_photo4.setImageURI(selectedImageUri);
                        photo4 =  encoded_image;
                    }
                }
            }
            /*Toast.makeText(getApplicationContext(),
                    nPath, Toast.LENGTH_LONG).show();*/
        }

        @Override
        public void onBackPressed() {
            finish();
        }

}
