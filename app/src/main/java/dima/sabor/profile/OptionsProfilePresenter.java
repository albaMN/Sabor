package dima.sabor.profile;


import javax.inject.Inject;

import dima.sabor.data.InternalStorageInterface;
import dima.sabor.data.impl.FirebaseDataSource;
import dima.sabor.model.User;

public class OptionsProfilePresenter {

    private OptionsProfileActivityImpl view;
    private FirebaseDataSource firebaseservice;
    private InternalStorageInterface internalStorage;

    @Inject
    public OptionsProfilePresenter(OptionsProfileActivityImpl view,
                            FirebaseDataSource firebaseservice,
                            InternalStorageInterface internalStorage) {
        this.view = view;
        this.firebaseservice = firebaseservice;
        this.internalStorage = internalStorage;
    }


    public User getUser() {
        return internalStorage.getUser();
    }

    public void showInfo() {
        view.showProgress("Loading info...");
        view.onInfoProfileRetrieved(getUser());
    }

    public void deleteUser(){
        firebaseservice.deleteUser(internalStorage.getUser());
        view.deleteUserSuccess();
    }

    public void updatePasswordUser(User user, String password) {
        firebaseservice.updatePasswordUser(user, password);
    }

    public void updateUsernameUser(User user, String username) {
        firebaseservice.updateUsernameUser(user, username);
    }}
