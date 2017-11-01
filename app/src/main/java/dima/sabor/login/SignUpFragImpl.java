package dima.sabor.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import customfonts.MyRegulerText;
import dima.sabor.R;
import dima.sabor.utils.FormatChecker;

public class SignUpFragImpl extends Fragment implements View.OnClickListener {

    SignInActivityImpl activity;

    @BindView(R.id.signup)
    TextView signup;
    @BindView(R.id.signin)
    TextView signin;
    @BindView(R.id.fb)
    TextView fb;
    @BindView(R.id.register_button)
    MyRegulerText account;
    @BindView(R.id.reg_email)
    EditText reg_email;
    @BindView(R.id.reg_password)
    EditText reg_password;
    @BindView(R.id.reg_username)
    EditText reg_username;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_signup, container, false);
        this.activity = (SignInActivityImpl) getActivity();

        ButterKnife.bind(this, view);

        signin.setOnClickListener(this);
        account.setOnClickListener(this);
/*
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SignInActivityImpl) getActivity()).openLoginFragment();
            }
        });*/
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.signin:
                ((SignInActivityImpl) getActivity()).openLoginFragment();
                break;

            case R.id.register_button:
                String email = reg_email.getText().toString();
                String username = reg_username.getText().toString();
                String password = reg_password.getText().toString();
                try {
                    FormatChecker.CheckEmail(email);
                    FormatChecker.CheckPassword(password);
                    FormatChecker.CheckUser(username);;
                    activity.onRegisterPressed(email,password,username);
                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
