package com.example.hk185080.allinonelogin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private Button signout;
    FirebaseAuth firebaseAuth;
    TextView userName,userEmail,userPhone;
    ImageView profilepic;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signout=findViewById(R.id.button2);
        userName=findViewById(R.id.username);
        userEmail=findViewById(R.id.useremail);
        profilepic=findViewById(R.id.profilepic);
        userPhone=findViewById(R.id.userPhone);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signout();
            }
        });


            firebaseUI();

    }
    private void firebaseUI()
    {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                //new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                //new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build());

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);


    }
    private void signout()
    {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...

                        firebaseUI();
                    }
                });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
                Toast.makeText(this,"success",Toast.LENGTH_SHORT).show();
                updateUI(user);
            } else {
                // Sign in failed, check response for error code
                // ...
                Log.e("error",response.toString());
            }
        }
    }

    private void updateUI(FirebaseUser user) {
        userName.setText(user.getDisplayName());
        userEmail.setText(user.getEmail());
        userPhone.setText(user.getPhoneNumber());
        Glide.with(this).load(user.getPhotoUrl()).into(profilepic);
    }
}
