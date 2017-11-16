package dima.sabor.splash;


import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dima.sabor.data.impl.FirebaseDataSource;

public class SplashPresenter {
    private SplashActivityImpl view;
    private FirebaseDataSource firebaseservice;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authListener;


    @Inject
    public SplashPresenter(SplashActivityImpl view, FirebaseDataSource firebaseservice) {
        this.view = view;
        this.firebaseservice = firebaseservice;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void unsubscribe(){
        if(authListener != null) {
            firebaseAuth.removeAuthStateListener(authListener);
        }
    }

    public void subscribe() {
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user == null) {
                    view.showLoginActivity();
                } else {
                    processLogin(user);
                }
            }
        };

        firebaseAuth.addAuthStateListener(authListener);
    }

    private void processLogin(FirebaseUser user) {
      /*  firebaseservice.getUser(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if(user == null || user.getUsername() == null) {
                            activity.showLoginActivity();
                        } else {
                            activity.showMainActivity(user);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        activity.showLoginActivity();
                    }
                }
        );*/
        view.showMainActivity();
    }
}
