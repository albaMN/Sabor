package dima.sabor.data.impl;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import javax.inject.Inject;

import dima.sabor.data.InternalStorageInterface;
import dima.sabor.dependencyinjection.annotation.qualifier.ForApp;
import dima.sabor.model.User;

public class InternalStorageDataSource implements InternalStorageInterface {

    private static final String USER_TOKEN = "user_token";
    private static final String USER = "user";
    private static final String RECEIPT = "recipe";

    SharedPreferences internalStorage;

    @Inject
    public InternalStorageDataSource(@ForApp Context context) {
        internalStorage = context.getSharedPreferences("Sabor", Context.MODE_PRIVATE);
    }


    @Override
    public boolean isUserLogged() {
        String token = internalStorage.getString(USER_TOKEN, null);
        return token != null;
    }

    @Override
    public void saveToken(String token) {
        internalStorage.edit().putString(USER_TOKEN, token).apply();
    }

    @Override
    public void saveUser(User user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        internalStorage.edit().putString(USER, userJson).commit();
    }

    @Override
    public User getUser() {
        Gson gson = new Gson();
        String userJson = internalStorage.getString(USER, null);
        if (userJson != null) {
            User user = gson.fromJson(userJson, User.class);
            return user;
        }
        return null;
    }

    @Override
    public String getToken() {
        String token = internalStorage.getString(USER_TOKEN, null);
        return token;
    }

    @Override
    public void onLogOut() {
        internalStorage.edit().remove(USER_TOKEN).apply();
    }

    @Override
    public void saveReceiptId(String id) {
        internalStorage.edit().putString(RECEIPT, id).commit();
    }

    @Override
    public String getReceiptId() {
        String receiptId = internalStorage.getString(RECEIPT, "0");
        return receiptId;
    }
}