package dima.sabor.profile;

import dima.sabor.base.BaseActivity;
import dima.sabor.model.User;

public interface ProfileActivity extends BaseActivity {
    void onProfileRetrieved(User user);

}
