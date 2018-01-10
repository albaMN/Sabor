package dima.sabor.data.impl;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dima.sabor.R;
import dima.sabor.base.BaseActivityImpl;
import dima.sabor.data.FirebaseInterface;
import dima.sabor.data.listener.ErrorBundle;
import dima.sabor.data.listener.FirebaseFavsListener;
import dima.sabor.data.listener.FirebaseRecipeListener;
import dima.sabor.model.Recipe;
import dima.sabor.model.User;

public class FirebaseDataSource implements FirebaseInterface {

    private static final String TAG = "EmailPassword";
    private static final String TAGLOG = "LogListener";

    private Application application;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;

    // for google
    private GoogleApiClient googleApiClient;

    // for facebook
    private CallbackManager callbackManager;

    private ChildEventListener listener;
    private ValueEventListener listener1;
    private ValueEventListener listener2;

    @Inject
    public FirebaseDataSource() {
        //this.application = (App) getApplication();
        this.mAuth = FirebaseAuth.getInstance();
        this.databaseRef = FirebaseDatabase.getInstance().getReference();
    }


    //USER FIREBASE

    @Override
    public String createUser(User user) {
        if(Integer.parseInt(user.getUid()) == -1) {
            DatabaseReference pushedPostRef = databaseRef.child("users").push();
            String userId = pushedPostRef.getKey();
            user.setUid(userId);
            pushedPostRef.setValue(user);
            databaseRef.child("users").child(user.getUid()).setValue(user);
            databaseRef.child("usernames").child(user.getUsername()).setValue(user);
            return userId;
        }
        else {
            databaseRef.child("users").child(user.getUid()).setValue(user);
            databaseRef.child("usernames").child(user.getUsername()).setValue(user);
            return user.getUid();
        }
    }

    @Override
    public DatabaseReference getUser(String userUid) {
        return databaseRef.child("users").child(userUid);
    }

    @Override
    public DatabaseReference getUserByUsername(String username) {
        return databaseRef.child("usernames").child(username);
    }

    @Override
    public void updateImageUser(User user, String imageEncoded) {
        /*databaseRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())*/
        databaseRef.child("users").child(user.getUid()).child("photo_url").setValue(imageEncoded);
        databaseRef.child("usernames").child(user.getUsername()).child("photo_url").setValue(imageEncoded);
    }

    @Override
    public void updateUsernameUser(User user, String username) {
        databaseRef.child("users").child(user.getUid()).child("username").setValue(username);
        databaseRef.child("usernames").child(user.getUsername()).child("username").setValue(username);
    }

    @Override
    public void updatePasswordUser(User user, String password) {
        /*databaseRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())*/
       /* databaseRef.child("users").child(user.getUid()).child("password").setValue(password);
        databaseRef.child("usernames").child(user.getUsername()).child("password").setValue(password); */
    }

    @Override
    public void updateUser(User user) {
        databaseRef.child("users").child(user.getUid()).setValue(user);
        databaseRef.child("usernames").child(user.getUsername()).setValue(user);
    }

    @Override
    public void deleteUser(User user) {
        databaseRef.child("users").child(user.getUid()).removeValue();
        databaseRef.child("usernames").child(user.getUsername()).removeValue();
    }



    //SIGN IN AND SIGN UP FIREBASE

    //@Override
    public Task<AuthResult> createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        Log.d(TAG, "createAccountpsw:" + password);
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    @Override
    public Task<AuthResult> signInWithEmail(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    @Override
    public CallbackManager getUserWithFacebook() {
        FacebookSdk.sdkInitialize(application);
        callbackManager = CallbackManager.Factory.create();
        return callbackManager;
    }

    @Override
    public Task<AuthResult> getAuthWithFacebook(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        return mAuth.signInWithCredential(credential);
    }

    @Override
    public Intent getUserWithGoogle(BaseActivityImpl activity) {
        /*GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.web_client_google))
                .requestEmail()
                .build();*/
        if(googleApiClient != null) {
            googleApiClient.disconnect();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        return Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
    }

    @Override
    public Task<AuthResult> getAuthWithGoogle(final BaseActivityImpl activity, GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        return mAuth.signInWithCredential(credential);
    }

    @Override
    public void signOut(String provider) {
        mAuth.signOut();
        if(provider.equals("facebook.com")) {
            FacebookSdk.sdkInitialize(application);
            LoginManager.getInstance().logOut();
        } else if(provider.equals("google.com")) {
            Auth.GoogleSignInApi.signOut(googleApiClient);
        }
    }

    /* private void sendEmailVerification() {
        // Disable button
        findViewById(R.id.verify_email_button).setEnabled(false);

        // Send verification email
        // [START send_email_verification]
        final FirebaseUser User = mAuth.getCurrentUser();
        User.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        findViewById(R.id.verify_email_button).setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(FirebaseDataSource.this,
                                    "Verification email sent to " + User.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(FirebaseDataSource.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser User) {
        hideProgressDialog();
        if (User != null) {
            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    User.getEmail(), User.isEmailVerified()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, User.getUid()));

            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);

            findViewById(R.id.verify_email_button).setEnabled(!User.isEmailVerified());
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }
    }
    */

    //ADD RECIPE
    @Override
    public String addRecipe(Recipe recipe) {
        DatabaseReference pushedPostRef = databaseRef.child("recipes").push();
        String recipeId = pushedPostRef.getKey();
        recipe.setId(recipeId);
        pushedPostRef.setValue(recipe);
        databaseRef.child("users").child(recipe.getUserId()).child("recipes").child(recipe.getId()).setValue(recipe);
        return recipeId;
    }

    //GET LIST RECIPES
    @Override
    public void getMyRecipes(String UserID,final FirebaseFavsListener dataCallback) {

            this.listener1 = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            List<Recipe> favs = new ArrayList<>();
                            for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                Recipe recipe = postSnapshot.getValue(Recipe.class);
                                favs.add(recipe);
                            }
                            dataCallback.onNewFav(favs);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("ISfav", "onCancelled", databaseError.toException());
                        }
                    };

            databaseRef.child("users").child(UserID).child("recipes").addValueEventListener(listener1);

    }

    @Override
    public void getRecipes(final FirebaseRecipeListener dataCallback) {
        this.listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAGLOG, "onChildAdded: {" + dataSnapshot.getKey() + ": " + dataSnapshot.getValue() + "}");
                //dataCallback.onNewRecipe(dataSnapshot.getValue(Recipe.class));
                Map<String, Recipe> hm = new HashMap<String, Recipe>();
                hm.put("add", dataSnapshot.getValue(Recipe.class));
                dataCallback.onNewRecipe(hm);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAGLOG, "onChildChanged: {" + dataSnapshot.getKey() + ": " + dataSnapshot.getValue() + "}");
                //dataCallback.onNewRecipe(dataSnapshot.getValue(Recipe.class));
                Map<String, Recipe> hm = new HashMap<String, Recipe>();
                hm.put("changed", dataSnapshot.getValue(Recipe.class));
                dataCallback.onNewRecipe(hm);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAGLOG, "onChildRemoved: {" + dataSnapshot.getKey() + ": " + dataSnapshot.getValue() + "}");
                Map<String, Recipe> hm = new HashMap<String, Recipe>();
                hm.put("removed", dataSnapshot.getValue(Recipe.class));
                dataCallback.onNewRecipe(hm);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAGLOG, "onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {
                Log.e(TAGLOG, "Error!", databaseError.toException());
                dataCallback.onError(new ErrorBundle() {
                    @Override
                    public Exception getException() {
                        return databaseError.toException();
                    }

                    @Override
                    public String getErrorMessage() {
                        return databaseError.getMessage();
                    }
                });
            }
        };

        databaseRef.child("recipes").addChildEventListener(listener);
    }

    @Override
    public void isFav(String UserID,final FirebaseFavsListener dataCallback) {

        this.listener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Recipe> favs = new ArrayList<>();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Recipe recipe = postSnapshot.getValue(Recipe.class);
                    favs.add(recipe);
                }
                dataCallback.onNewFav(favs);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ISfav", "onCancelled", databaseError.toException());
            }
        };

        databaseRef.child("users").child(UserID).child("favourites").addValueEventListener(listener2);

    }

    @Override
    public void addFav(String userID,Recipe recipe) {
        databaseRef.child("users").child(userID).child("favourites").child(recipe.getId()).setValue(recipe);
    }

    @Override
    public void deleteFav(String userID, String RecipeID) {
        databaseRef.child("users").child(userID).child("favourites").child(RecipeID).removeValue();
    }

    @Override
    public void deleteRecipe(String userID, String RecipeID) {
        databaseRef.child("users").child(userID).child("recipes").child(RecipeID).removeValue();
        databaseRef.child("recipes").child(RecipeID).removeValue();
    }
}
