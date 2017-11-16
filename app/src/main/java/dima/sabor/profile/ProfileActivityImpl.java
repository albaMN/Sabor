package dima.sabor.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dima.sabor.R;
import dima.sabor.base.BaseActivityImpl;
import dima.sabor.dependencyinjection.App;
import dima.sabor.dependencyinjection.activity.ActivityModule;
import dima.sabor.dependencyinjection.view.ViewModule;
import dima.sabor.login.SignInActivityImpl;

public class ProfileActivityImpl extends BaseActivityImpl {

    @Inject
    ProfilePresenter presenter;

    @BindView(R.id.options)
    ImageView options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ((App) getApplication())
                .getComponent()
                .plus(new ActivityModule(this),
                        new ViewModule(this))
                .inject(this);

        ButterKnife.bind(this);

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.logOut();
            }
        });
    }

    public void signOutSuccess() {
        startActivity(new Intent(ProfileActivityImpl.this, SignInActivityImpl.class));
        finish();
    }

}
