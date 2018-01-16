package dima.sabor.profile;

import dima.sabor.base.BaseActivity;
import dima.sabor.model.User;

public interface OptionsProfileActivity extends BaseActivity {
    void deleteUserSuccess();
    void onInfoProfileRetrieved(User user);
}
