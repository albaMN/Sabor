package dima.sabor.data;

import android.content.Intent;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;

import dima.sabor.base.BaseActivityImpl;
import dima.sabor.data.listener.FirebaseFavsListener;
import dima.sabor.data.listener.FirebaseRecipeListener;
import dima.sabor.model.Recipe;
import dima.sabor.model.User;

public interface FirebaseInterface {
    void createUser(User user);
    DatabaseReference getUser(String userUid);
    DatabaseReference getUserByUsername(String username);
    void updateImageUser(User user, String imageEncoded);
    void updateUsernameUser(User user, String username);
    void updatePasswordUser(User user, String password);
    void updateUser(User user);
    void deleteUser(User user);
    Task<AuthResult> createAccount(String email, String password);
    Task<AuthResult> signInWithEmail(String email, String password);
    CallbackManager getUserWithFacebook();
    Task<AuthResult> getAuthWithFacebook(AccessToken token);
    Intent getUserWithGoogle(BaseActivityImpl activity);
    Task<AuthResult> getAuthWithGoogle(final BaseActivityImpl activity, GoogleSignInAccount acct);
    void signOut(String provider);
    String addRecipe(Recipe recipe);
    void getMyRecipes(String userID, FirebaseFavsListener dataCallback);
    void getRecipes(FirebaseRecipeListener dataCallback);
    void isFav(String userID, FirebaseFavsListener dataCallback);
    void deleteFav(String userID, String recipeID);
    void addFav(String userID,Recipe recipe);
    void deleteRecipe(String userID, String recipeID);
}
