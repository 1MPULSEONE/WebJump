package com.lovejazz.webjump;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            Intent intToHome = new Intent(MainActivity.this,Profession.class);
            startActivity(intToHome);
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        // GoogleAuth
        createGoogleRequest();
        Button googleBtn = findViewById(R.id.button_google);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        FirebaseUser currentUser = mAuth.getCurrentUser();
        TextView registerText = findViewById(R.id.register);
        Button buttonLogin  = findViewById(R.id.buttonEntry);
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser mFireBaseUser = mAuth.getCurrentUser();
            if(mFireBaseUser != null){
                Log.d("Login","User is logged in!");
                Intent intent = new Intent(MainActivity.this,Profession.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            else{
                Log.d("Login","User isn`t logged in");
            }

            }
        };
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText emailEntry = findViewById(R.id.email_entry);
                final String email = emailEntry.getText().toString();
                EditText passwordEntry = findViewById(R.id.password_entry);
                String password = passwordEntry.getText().toString();
                if (email.equals("") || password.equals("")) {
                    Snackbar
                            .make(
                                    findViewById(R.id.activity_main),
                                    getString(R.string.error_empty_fields),
                                    Snackbar.LENGTH_LONG
                            )
                            .setTextColor(getResources().getColor(R.color.bg_grey))
                            .setBackgroundTint(getResources().getColor(R.color.orange))
                            .show();
                }
                else{
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Snackbar
                                        .make(
                                                findViewById(R.id.activity_main),
                                                getString(R.string.some_problem),
                                                Snackbar.LENGTH_LONG
                                        )
                                        .setTextColor(getResources().getColor(R.color.bg_grey))
                                        .setBackgroundTint(getResources().getColor(R.color.orange))
                                        .show();
                            }
                            else{
                                 Intent intToHome = new Intent(MainActivity.this,Profession.class);
                                startActivity(intToHome);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        }
                    });
                }
            }
        });

        ImageButton arrow = findViewById(R.id.arrow_left);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Registration.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Registration.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);}
        });

    }
    private void createGoogleRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    // Google Sing In
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("googleSingIn", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("googleSingIn", "Google sign in failed", e);
                Snackbar
                        .make(
                                findViewById(R.id.activity_registration),
                                getString(R.string.some_problem),
                                Snackbar.LENGTH_LONG
                        )
                        .setTextColor(getResources().getColor(R.color.bg_grey))
                        .setBackgroundTint(getResources().getColor(R.color.orange))
                        .show();
            }
            // ...
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar
                                    .make(
                                            findViewById(R.id.activity_registration),
                                            getString(R.string.some_problem),
                                            Snackbar.LENGTH_LONG
                                    )
                                    .setTextColor(getResources().getColor(R.color.bg_grey))
                                    .setBackgroundTint(getResources().getColor(R.color.orange))
                                    .show();
                        }

                        // ...
                    }
                });
    }
}