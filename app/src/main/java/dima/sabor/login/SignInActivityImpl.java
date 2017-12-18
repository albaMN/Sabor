package dima.sabor.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.internal.CallbackManagerImpl;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dima.sabor.R;
import dima.sabor.base.BaseActivityImpl;
import dima.sabor.dependencyinjection.App;
import dima.sabor.dependencyinjection.activity.ActivityModule;
import dima.sabor.dependencyinjection.view.ViewModule;
import dima.sabor.model.User;
import dima.sabor.receiptsList.ReceiptsListActivityImpl;

public class SignInActivityImpl extends BaseActivityImpl implements SignInActivity {

    public static final int REQUEST_SIGN_GOOGLE = 9001;
    private GoogleApiClient mGoogleApiClient;

    @Inject
    SignInPresenter presenter;
    SignInFragImpl loginFrag;
    SignUpFragImpl registerFrag;

    // just for facebook login
    private CallbackManager callbackManager;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_main);

        ((App) getApplication())
                .getComponent()
                .plus(new ActivityModule(this),
                        new ViewModule(this))
                .inject(this);

        ButterKnife.bind(this);
        loginFrag = new SignInFragImpl();
        registerFrag = new SignUpFragImpl();

        openLoginFragment();

    }

    public void openRegisterFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_view, registerFrag)
                .commit();
    }

    public void openLoginFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_view, loginFrag)
                .commit();
    }

    public void onLoginPressed(String email, String password) {
        presenter.loginWithEmail(email, password);
    }

    public void onFacebookPressed() {
        callbackManager = presenter.loginWithFacebook();
    }

    public void onGooglePressed() {
        Intent intent = presenter.loginWithGoogle();
        startActivityForResult(intent, REQUEST_SIGN_GOOGLE);
    }

    public void onRegisterPressed(String email, String password, String username) {
        user = new User("", username, email, "", "", "");
        presenter.register(email, password, user);
    }

    public void showExistUsername(User user, String username) {
        Toast.makeText(this, "This username already exists: " + username, Toast.LENGTH_LONG).show();
    }

    public void registerSuccess() {
        openLoginFragment();
    }

    public void changeActivity(){
        startActivity(new Intent(SignInActivityImpl.this, ReceiptsListActivityImpl.class));
        finish();
    }

    public void showRegisterFail() {
        Toast.makeText(this, "Authentication failed. There is already an account with this email.", Toast.LENGTH_LONG).show();
    }

    public void showLoginFail() {
        Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // google
        if(requestCode == REQUEST_SIGN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            presenter.getAuthWithGoogle(result);
        }
        // facebook
        else if(requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

   /* public void showLoginSuccess(User user) {
        MainActivity.startWithUser(this, user);
    }*/

    public void showInsertUsername(final User user) {

        AlertDialog.Builder addAlertDialog = new AlertDialog.Builder(this);

        addAlertDialog.setTitle("Insert your username");
        addAlertDialog.setMessage("Be sure to enter");

        final EditText etUsername = new EditText(this);
        etUsername.setSingleLine();
        addAlertDialog.setView(etUsername);

        addAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String username = etUsername.getText().toString();
                dialog.dismiss();
                user.setUsername(username);
                presenter.createUser(user);
            }
        });

        addAlertDialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

}
