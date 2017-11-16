package dima.sabor.login;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import dima.sabor.data.impl.FirebaseDataSource;
import dima.sabor.model.User;

import static com.googlecode.eyesfree.utils.LogUtils.TAG;

public class SignInPresenter {

    private SignInActivityImpl view;
    private FirebaseDataSource firebaseservice;

    @Inject
    public SignInPresenter(SignInActivityImpl view, FirebaseDataSource firebaseservice) {
        this.view = view;
        this.firebaseservice = firebaseservice;
    }

    public void login(final String usuari, String password) {
        view.showProgress("Signing in...");
        firebaseservice.signInWithEmail(usuari, password).addOnCompleteListener(view, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in User's information
                    Log.d(TAG, "signInWithEmail:success");
                    view.hideProgress();
                    view.changeActivity();
                    // FirebaseUser User = mAuth.getCurrentUser();
                    //updateUI(User);
                } else {
                    // If sign in fails, display a message to the User.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    view.hideProgress();
                    Toast.makeText(view, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    // updateUI(null);
                }
            /*
                // [START_EXCLUDE]
                if (!task.isSuccessful()) {
                    //mStatusTextView.setText(R.string.auth_failed);
                }
                hideProgressDialog();
                // [END_EXCLUDE]*/

            }
        });
    }

    public void register(String email, String password) {
        view.showProgress("Creating new User...");
        firebaseservice.createAccount(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");
                    view.hideProgress();
                    //view.changeActivity();
                    view.registerSuccess();
                    processLogin(task.getResult().getUser(), task.getResult().getUser().getProviderData().get(1));
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    view.hideProgress();
                    Toast.makeText(view, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createUser(final User user) {
        //view.showLoading(true);
        final String username = user.getUsername();
        firebaseservice.getUserByUsername(username).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean exists = dataSnapshot.exists();
                        if(!exists) {
                            //activity.showLoading(false);
                            user.setUsername(username);
                            firebaseservice.createUser(user);
                            //view.showLoginSuccess(user);
                            view.changeActivity();
                        } else {
                            //activity.showLoading(false);
                            view.showExistUsername(user, username);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //activity.showLoading(false);
                        //activity.showInsertUsername(user);
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
                            //view.showInsertUsername(user);
                        } else {
                            view.changeActivity();
                            // view.showLoginSuccess(remoteUser);
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
