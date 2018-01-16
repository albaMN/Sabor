package dima.sabor.profile;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import customfonts.MyRegulerText;
import dima.sabor.R;
import dima.sabor.base.BaseActivityImpl;
import dima.sabor.dependencyinjection.App;
import dima.sabor.dependencyinjection.activity.ActivityModule;
import dima.sabor.dependencyinjection.view.ViewModule;
import dima.sabor.login.SignInActivityImpl;
import dima.sabor.model.User;
import dima.sabor.utils.FormatChecker;

public class OptionsProfileActivityImpl extends BaseActivityImpl implements OptionsProfileActivity {

    private User user;

    @Inject
    OptionsProfilePresenter presenter;

    @BindView(R.id.optionsProf_close_button)
    ImageButton closeBut;

    @BindView(R.id.user_profile_name_opt)
    TextView user_name;

    @BindView(R.id.user_profile_email_opt)
    TextView user_email;

    @BindView(R.id.user_profile_password_opt)
    TextView user_passw;

    @BindView(R.id.delete_user_but)
    MyRegulerText delete_user_but;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_profile);

        ((App) getApplication())
                .getComponent()
                .plus(new ActivityModule(this),
                        new ViewModule(this))
                .inject(this);

        ButterKnife.bind(this);

        showInfo();

        closeBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OptionsProfileActivityImpl.this, ProfileActivityImpl.class));
                //onBackPressed();
                finish();
            }
        });

        //TODO: no poder cambiar passw, pero si nombre de entero (no el username). Se puede cambiar email? etc
        user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUsername();
            }
        });
        user_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //changeEmail();
            }
        });
        user_passw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
        delete_user_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });

    }

    public void showInfo() {
        presenter.showInfo();
    }

    public void onInfoProfileRetrieved(User user) {
        hideProgress();
        this.user = user;
        user_name.setText(user.getUsername());
        user_email.setText(user.getEmail());
    }

    private void deleteUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Delete user");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_change_username, null);

        final EditText usernameEditText = view.findViewById(R.id.dialog_change_username_new);

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newUsername = usernameEditText.getText().toString();
                try {
                    //FormatChecker.CheckUser(newUsername);
                    presenter.deleteUser();
                    dialog.dismiss();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogBuilder.create().show();
    }

    public void deleteUserSuccess() {
        startActivity(new Intent(OptionsProfileActivityImpl.this, SignInActivityImpl.class));
        finish();
    }

    private void changeUsername() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Change username");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_change_username, null);

        final EditText usernameEditText = view.findViewById(R.id.dialog_change_username_new);

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newUsername = usernameEditText.getText().toString();
                try {
                    FormatChecker.CheckUser(newUsername);
                    changeUsername(newUsername);
                    dialog.dismiss();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialogBuilder.create().show();
    }


    private void changePassword() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Change password");

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_change_password, null);

        final EditText newEditText = (EditText) view.findViewById(R.id.dialog_change_password_new);
        final EditText repeatEditText = (EditText) view.findViewById(R.id.dialog_change_password_repeat);

        alertDialogBuilder.setView(view);

        alertDialogBuilder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String newPassword = newEditText.getText().toString();
                String repeatPassword = repeatEditText.getText().toString();
                try {
                    FormatChecker.CheckPasswordChange(newPassword,repeatPassword);
                    changePassword(newPassword);
                    dialog.dismiss();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialogBuilder.create().show();
    }


    public void changePassword(String password) {
        presenter.updatePasswordUser(user, password);
        //user.setPassword(password);
    }

    public void changeUsername(String username) {
        presenter.updateUsernameUser(user, username);
        user.setUsername(username);
    }


}
