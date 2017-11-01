package dima.sabor.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import customfonts.MyRegulerText;
import dima.sabor.R;

public class SignInFragImpl extends Fragment implements View.OnClickListener {

    @BindView(R.id.signup)
    TextView signup;
    @BindView(R.id.signin)
    TextView signin;
    @BindView(R.id.fbButt)
    ImageView fb;
    @BindView(R.id.googleButt)
    ImageView googl;
    @BindView(R.id.account)
    TextView account;
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

        buttonsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();

                ((SignInActivityImpl) getActivity()).onLoginPressed(emailString, passwordString);

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SignInActivityImpl) getActivity()).openRegisterFragment();
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        /*switch(v.getId()){
            case R.id.register_button:
                String nombre = e_nombre.getText().toString();
                String apellidos = e_apellidos.getText().toString();
                String contraseña = e_contraseña.getText().toString();
                String repcontraseña = e_repcontraseña.getText().toString();
                String teléfono = e_telefono.getText().toString();
                String mail = e_mail.getText().toString();
                String año = e_año.getText().toString();
                String mes = e_mes.getText().toString();
                String dia = e_dia.getText().toString();
                String fecha = año + "-" + mes + "-" + dia;
                try {
                    FormatChecker.CheckName(nombre);
                    FormatChecker.CheckUser(nombre+" "+apellidos);
                    FormatChecker.CheckPassword(contraseña,repcontraseña);
                    FormatChecker.CheckPhone(teléfono);
                    FormatChecker.CheckEmail(mail);
                    FormatChecker.CheckDate(fecha);
                    activity.onRegisterPressed(nombre,apellidos,contraseña,teléfono,mail,fecha);

                } catch (Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            e.getMessage(), Toast.LENGTH_LONG).show();
                }
                break;
        }*/
    }
}
