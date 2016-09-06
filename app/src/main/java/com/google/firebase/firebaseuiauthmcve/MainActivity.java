package com.google.firebase.firebaseuiauthmcve;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int RC_SIGN_IN = 1;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // sign in
                    String username = getString(R.string.signed_in_message, user.getDisplayName());
                    ((TextView) findViewById(R.id.signInTextView)).setText(username);
                } else {
                    // sign out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(AuthUI.GOOGLE_PROVIDER, AuthUI.EMAIL_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        findViewById(R.id.signOutButton).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuthStateListener != null) {
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onClick(View view) {
        AuthUI.getInstance()
                .signOut(this);
    }
}
