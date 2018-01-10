package dima.sabor.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import customfonts.MyRegulerText;
import dima.sabor.R;
import dima.sabor.utils.FormatChecker;

public class SignInFragImpl extends Fragment implements View.OnClickListener {

    @BindView(R.id.signup)
    TextView signup;
    @BindView(R.id.signin)
    TextView signin;
    @BindView(R.id.fbButt)
    ImageView fb;
    /*@BindView(R.id.googleButt)
    ImageView googl;*/
    /*@BindView(R.id.account)
    TextView account;*/
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.buttonsignin)
    MyRegulerText buttonsignin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.activity_signin, container, false);

        ButterKnife.bind(this, rootView);

        buttonsignin.setOnClickListener(this);
        fb.setOnClickListener(this);
        signup.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonsignin:
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();
                try {
                    FormatChecker.CheckEmail(emailString);
                    FormatChecker.CheckPassword(passwordString);
                    ((SignInActivityImpl) getActivity()).onLoginPressed(emailString, passwordString);
                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.signup:
                ((SignInActivityImpl) getActivity()).openRegisterFragment();
                break;
            case R.id.fbButt:
                ((SignInActivityImpl) getActivity()).onFacebookPressed();
                break;
          /*  case R.id.googleButt:
                ((SignInActivityImpl) getActivity()).onGooglePressed();
                break;*/

        }
    }

}
