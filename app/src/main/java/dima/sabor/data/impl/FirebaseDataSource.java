package dima.sabor.data.impl;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import dima.sabor.data.FirebaseInterface;
import dima.sabor.model.User;

public class FirebaseDataSource implements FirebaseInterface {

        private static final String TAG = "EmailPassword";
        private Application application;

        /*private TextView mStatusTextView;
        private TextView mDetailTextView;
        private EditText mEmailField;
        private EditText mPasswordField;*/


        private FirebaseAuth mAuth;
        private DatabaseReference databaseRef;


      /*  @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_emailpassword);

            // Views
            mStatusTextView = findViewById(R.id.status);
            mDetailTextView = findViewById(R.id.detail);
            mEmailField = findViewById(R.id.field_email);
            mPasswordField = findViewById(R.id.field_password);

            // Buttons
            findViewById(R.id.email_sign_in_button).setOnClickListener(this);
            findViewById(R.id.email_create_account_button).setOnClickListener(this);
            findViewById(R.id.sign_out_button).setOnClickListener(this);
            findViewById(R.id.verify_email_button).setOnClickListener(this);

            // [START initialize_auth]
            mAuth = FirebaseAuth.getInstance();
            // [END initialize_auth]
        }

          // [START on_start_check_user]
        @Override
        public void onStart() {
            super.onStart();
            // Check if User is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            //updateUI(currentUser);
        }
        // [END on_start_check_user]



                @Override
        public void onClick(View v) {
           int i = v.getId();
            if (i == R.id.email_create_account_button) {
                createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
            } else if (i == R.id.email_sign_in_button) {
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
            } else if (i == R.id.sign_out_button) {
                signOut();
            } else if (i == R.id.verify_email_button) {
                sendEmailVerification();
            }
        }

        */

    @Inject
    public FirebaseDataSource(/*Application application*/) {
        //this.application = application;
        this.mAuth = FirebaseAuth.getInstance();
        this.databaseRef = FirebaseDatabase.getInstance().getReference();
    }

    //USER FIREBASE

    public void createUser(User user) {
        if(user.getPhoto_url() == null) {
            user.setPhoto_url("NOT");
        }
        databaseRef.child("users").child(user.getUid()).setValue(user);
        databaseRef.child("usernames").child(user.getUsername()).setValue(user);

    }

    public DatabaseReference getUser(String userUid) {
        return databaseRef.child("users").child(userUid);
    }

    public DatabaseReference getUserByUsername(String username) {
        return databaseRef.child("usernames").child(username);
    }

    public void updateUser(User user) {

    }

    public void deleteUser(String key) {

    }



    //SIGN IN AND SIGN UP FIREBASE

    @Override
    public Task<AuthResult> createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        Log.d(TAG, "createAccountpsw:" + password);
        return mAuth.createUserWithEmailAndPassword(email, password);
            /*Log.d(TAG, "createAccount:" + email);
            if (!validateForm()) {
                return;
            }

            showProgressDialog();

            // [START create_user_with_email]
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in User's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser User = mAuth.getCurrentUser();
                                updateUI(User);
                            } else {
                                // If sign in fails, display a message to the User.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(FirebaseDataSource.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // [START_EXCLUDE]
                            hideProgressDialog();
                            // [END_EXCLUDE]
                        }
                    });
            // [END create_user_with_email]*/
        }

    @Override
    public Task<AuthResult> signInWithEmail(String email, String password) {
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    @Override
    public void signOut(/*String provider*/) {
        mAuth.signOut();
        /*if(provider.equals("facebook.com")) {
            FacebookSdk.sdkInitialize(application);
            LoginManager.getInstance().logOut();
        } else if(provider.equals("google.com")) {
            Auth.GoogleSignInApi.signOut(googleApiClient);
        }*/
        //updateUI(null);
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
}
