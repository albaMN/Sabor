package dima.sabor.login;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import javax.inject.Inject;

import dima.sabor.data.InternalStorageInterface;
import dima.sabor.data.impl.FirebaseDataSource;
import dima.sabor.model.User;

import static com.googlecode.eyesfree.utils.LogUtils.TAG;

public class SignInPresenter {

    private SignInActivityImpl view;
    private FirebaseDataSource firebaseservice;
    private InternalStorageInterface internalStorage;

    @Inject
    public SignInPresenter(SignInActivityImpl view, FirebaseDataSource firebaseservice,
                           InternalStorageInterface internalStorage) {
        this.view = view;
        this.firebaseservice = firebaseservice;
        this.internalStorage = internalStorage;
    }

    public void loginWithEmail(final String usuari, String password) {
        view.showProgress("Signing in...");
        firebaseservice.signInWithEmail(usuari, password).addOnCompleteListener(view, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");
                    view.hideProgress();
                    view.changeActivity();
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    view.hideProgress();
                    Toast.makeText(view, "Authentication failed",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    protected CallbackManager loginWithFacebook() {
        CallbackManager callbackManager = firebaseservice.getUserWithFacebook();
        LoginManager.getInstance().logInWithReadPermissions(view, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        getAuthWithFacebook(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        view.showLoginFail();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        view.showLoginFail();
                    }
                });
        return callbackManager;
    }

    protected void getAuthWithFacebook(final AccessToken accessToken) {
        firebaseservice.getAuthWithFacebook(accessToken)
                .addOnCompleteListener(view, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            view.hideProgress();
                            User us = new User();
                            for(UserInfo profile : task.getResult().getUser().getProviderData()) {
                                String providerId = profile.getProviderId();
                                String uid = profile.getUid();
                                String name = profile.getDisplayName();
                                String email = profile.getEmail();
                                Uri photoUri = profile.getPhotoUrl();
                                Log.d("fisache", providerId + " " + uid + " " + name + " " + email + " " + photoUri);
                                us.setUid(uid);
                                us.setName(name);
                                us.setEmail(email);
                                us.setPhoto_url(photoUri.toString());
                                us.setProvider("facebook");
                            }
                            processLogin(task.getResult().getUser(), task.getResult().getUser().getProviderData().get(1));
                            //view.showInsertUsername(us);
                        } else {
                            view.hideProgress();
                            view.showLoginFail();
                        }
                    }
                });
    }

    protected Intent loginWithGoogle() {
        return firebaseservice.getUserWithGoogle(view);
    }

    protected void getAuthWithGoogle(GoogleSignInResult result) {
        if(result.isSuccess()) {
            final GoogleSignInAccount acct = result.getSignInAccount();
            firebaseservice.getAuthWithGoogle(view, acct)
                    .addOnCompleteListener(view, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                view.hideProgress();
                                User us = new User();
                                for(UserInfo profile : task.getResult().getUser().getProviderData()) {
                                    String providerId = profile.getProviderId();
                                    String uid = profile.getUid();
                                    String name = profile.getDisplayName();
                                    String email = profile.getEmail();
                                    Uri photoUri = profile.getPhotoUrl();
                                    Log.d("fisache", providerId + " " + uid + " " + name + " " + email + " " + photoUri);
                                    us.setUid(uid);
                                    us.setName(name);
                                    us.setEmail(email);
                                    us.setPhoto_url(photoUri.toString());
                                    us.setProvider("google");
                                }
                                processLogin(task.getResult().getUser(), task.getResult().getUser().getProviderData().get(1));
                                //view.showInsertUsername(us);
                            } else {
                                view.hideProgress();
                                view.showLoginFail();
                            }
                        }
                    });
        } else {
            view.showLoginFail();
        }
    }


    public void register(String email, String password, final User user) {
        view.showProgress("Creating new User...");
        firebaseservice.createAccount(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");
                    view.hideProgress();
                    processLogin(task.getResult().getUser(), task.getResult().getUser().getProviderData().get(1));
                    createUser(user);
                    view.registerSuccess();
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    view.hideProgress();
                    view.showRegisterFail();
                    Toast.makeText(view, "Authentication failed. This ",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createUser(final User user) {
        //internalStorage.saveUser(user);
        final String username = user.getUsername();
        firebaseservice.getUserByUsername(username).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean exists = dataSnapshot.exists();
                        if(!exists) {
                            view.hideProgress();
                            user.setUsername(username);
                            firebaseservice.createUser(user);
                            internalStorage.saveUser(user);
                            view.changeActivity();
                        } else {
                            view.hideProgress();
                            view.showExistUsername(user, username);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                       // view.hideProgress();
                        //TODO: hacer que si se logea con google o facebook pida username
                        view.showInsertUsername(user);
                    }
                }
        );
    }

    private void processLogin(FirebaseUser firebaseUser, UserInfo userInfo) {
        final User user = User.newInstance(firebaseUser, userInfo);
        firebaseservice.getUser(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User remoteUser = dataSnapshot.getValue(User.class);
                        if(remoteUser == null || remoteUser.getUsername() == null) {
                            view.showInsertUsername(user);
                        } else {
                            internalStorage.saveUser(remoteUser);
                            view.changeActivity();
                            //view.showLoginSuccess(remoteUser);
                            //TODO: seguro que es esto??

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        view.showLoginFail();
                    }
                }
        );
    }

}
