package dima.sabor.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dima.sabor.R;
import dima.sabor.profile.ProfileActivity;

public class SignInActivityImpl extends AppCompatActivity {
    @Inject
    SignInPresenter presenter;
    SignInFragImpl loginFrag;
    SignUpFragImpl registerFrag;

    protected MaterialDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

   /* public void goToVerificationView(String usuari) {
        final Integer code = (int) (Math.random() * 100000);
        String output = String.format("%05d", code);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.pop_verification, null);
        builder.setView(dialogView);

        final EditText codeEditText = (EditText) dialogView.findViewById(R.id.verification_code);

        builder.setPositiveButton("Enviar", null);

        final AlertDialog mDialog = builder.create();

        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                Button b = mDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String code1 = codeEditText.getText().toString();
                        if (code.toString().equals(code1) || code1.equals("1234567")) {
                            mDialog.dismiss();
                            startActivity(new Intent(SignInActivity.this, ProfileActivity.class));
                        } else {
                            codeEditText.setText("");
                            Toast.makeText(SignInActivity.this, "Wrong Code", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        mDialog.show();
    }*/

    public void onLoginPressed(String email, String password) {
        presenter.login(email, password);
    }

    public void onRegisterPressed(String email, String password, String username) {
        presenter.register(email, password, username);
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        openLoginFragment();
        return true;
    }*/

/*
    @Override
    public void onRegisterRetrieved(Boolean returnParam) {
        SignUpFragImpl registerFrag = (SignUpFragImpl)
                getSupportFragmentManager().findFragmentById(R.id.fragment_view);
        registerFrag.onRegisterRetrieved(returnParam);
    }*/

    public void showProgress(String message) {
        if(progressDialog == null || !progressDialog.isShowing()) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
            builder.content(message);
            builder.progress(true, 0);
            progressDialog = builder.build();

            progressDialog.show();
        }
    }

    public void changeActivity(){
        startActivity(new Intent(SignInActivityImpl.this, ProfileActivity.class));
    }
}
