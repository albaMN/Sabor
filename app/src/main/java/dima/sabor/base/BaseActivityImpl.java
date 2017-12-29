package dima.sabor.base;

import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import javax.inject.Inject;

import dima.sabor.R;
import dima.sabor.dependencyinjection.App;
import dima.sabor.dependencyinjection.activity.ActivityModule;
import dima.sabor.dependencyinjection.view.ViewModule;

public class BaseActivityImpl extends AppCompatActivity implements BaseActivity {

    @Inject
    BasePresenter presenter;

    @LayoutRes
    private int layoutRes;

    protected MaterialDialog progressDialog;
    private FrameLayout baseContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) getApplication())
                .getComponent()
                .plus(new ActivityModule(this),
                        new ViewModule(this))
                .inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*if(!isChatServiceRunning()){
            Intent serviceIntent = new Intent(this, ChatService.class);
            startService(serviceIntent);
        }*/
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.activity_base);

        this.layoutRes = layoutResID;

        this.baseContainer = (FrameLayout) findViewById(R.id.base_content_layout);
        LayoutInflater.from(this).inflate(layoutResID, baseContainer);
    }

    public void setUpBackActionBar(){
        FrameLayout frameToolbar = (FrameLayout) findViewById(R.id.base_toolbar_layout);
        LayoutInflater.from(this).inflate(R.layout.toolbar_core, frameToolbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setUpBackActionBar(toolbar);
    }

    public void setUpBackActionBar(Toolbar toolbar){
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void addFullScreenFragment(Fragment fragment){

        getSupportFragmentManager().beginTransaction().replace(R.id.base_container, fragment).commit();

    }

    public void addFullScreenFragmentWithTransition(Fragment fragment, @AnimRes int enterTrans, @AnimRes  int exitTrans){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(enterTrans, exitTrans);
        transaction.replace(R.id.base_container, fragment);
        transaction.commit();

    }

    public void removeFullScreenFragment(){
        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.base_container)).commit();
    }

    /*private boolean isChatServiceRunning(){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ChatService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }*/

    @Override
    public void showProgress(String message) {
        if(progressDialog == null || !progressDialog.isShowing()) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
            builder.content(message);
            builder.progress(true, 0);
            progressDialog = builder.build();

            progressDialog.show();
        }

    }

    @Override
    public void hideProgress() {

        if(progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }

    }

    @Override
    public void onError(String error) {

        hideProgress();
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();

    }
}
