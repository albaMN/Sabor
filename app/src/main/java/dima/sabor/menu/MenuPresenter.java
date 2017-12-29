package dima.sabor.menu;

import javax.inject.Inject;

import dima.sabor.data.InternalStorageInterface;
import dima.sabor.data.impl.FirebaseDataSource;

public class MenuPresenter {
    private MenuActivityImpl view;
    private FirebaseDataSource firebaseservice;
    private InternalStorageInterface internalStorage;

    @Inject
    public MenuPresenter(MenuActivityImpl view,
                         FirebaseDataSource firebaseservice,
                         InternalStorageInterface internalStorage) {
        this.view = view;
        this.firebaseservice = firebaseservice;
        this.internalStorage = internalStorage;
    }

    public void logOut(){
        firebaseservice.signOut(internalStorage.getUser().getProvider());
        view.signOutSuccess();
    }
}
