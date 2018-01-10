package dima.sabor.data;

import dima.sabor.model.Recipe;
import dima.sabor.model.User;

public interface InternalStorageInterface {

    boolean isUserLogged();

    void saveToken(String token);

    void saveUser(User user);

    User getUser();

    String getToken();

    void onLogOut();

    void saveReceiptId (String id);

    String getReceiptId();

    void saveActualRecipe(String GsonRecipe);

    Recipe getActualRecipe();

}