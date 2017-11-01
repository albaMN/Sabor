package dima.sabor.login;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import javax.inject.Inject;

import dima.sabor.firebase.auth.SignInUpService;

import static com.googlecode.eyesfree.utils.LogUtils.TAG;

public class SignInPresenter {
    private SignInActivityImpl view;
    private SignInUpService firebaseservice;

    @Inject
    public SignInPresenter(SignInActivityImpl view, SignInUpService firebaseservice) {
        this.view = view;
        this.firebaseservice = firebaseservice;
    }

    public void login(final String usuari, String password) {
        view.showProgress("Signing in...");
        firebaseservice.signInWithEmail(usuari, password).addOnCompleteListener(view, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");
                    view.changeActivity();
                    // FirebaseUser user = mAuth.getCurrentUser();
                    //updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
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

    public void register(String email, String username, String password) {
        view.showProgress("Creating new user...");
        firebaseservice.createAccount(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");
                    view.changeActivity();
                    //processLogin(task.getResult().getUser(), task.getResult().getUser().getProviderData().get(1));
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(view, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
