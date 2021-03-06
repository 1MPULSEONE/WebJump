package com.lovejazz.webjump;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseFirestore fstore;
    String userID;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // GoogleAuth
        createGoogleRequest();
        Button googleBtn = findViewById(R.id.button_google);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
                Log.d("Google","`Google acc is OK!`");

            }
        });
        // EmailAuth
        TextView registerText = findViewById(R.id.register);
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this,MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);}
        });
        Button regBtn = findViewById(R.id.buttonEntry);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText loginEntry = findViewById(R.id.login_entry);
                final String nickname = loginEntry.getText().toString();
                EditText emailEntry = findViewById(R.id.email_entry);
                final String email = emailEntry.getText().toString();
                EditText passwordEntry = findViewById(R.id.password_entry);
                String password = passwordEntry.getText().toString();
                if (nickname.equals("") || email.equals("") || password.equals("")) {
                    Snackbar
                            .make(
                                    findViewById(R.id.activity_registration),
                                    getString(R.string.error_empty_fields),
                                    Snackbar.LENGTH_LONG
                            )
                            .setTextColor(getResources().getColor(R.color.bg_grey))
                            .setBackgroundTint(getResources().getColor(R.color.orange))
                            .show();
                } else {
                    Log.d("Registration", "Fields are filled correctly!");
                    if (!isValid(email)) {

                        Snackbar
                                .make(
                                        findViewById(R.id.activity_registration),
                                        getString(R.string.not_valid_email),
                                        Snackbar.LENGTH_LONG
                                )
                                .setTextColor(getResources().getColor(R.color.bg_grey))
                                .setBackgroundTint(getResources().getColor(R.color.orange))
                                .show();
                    } else {
                        Log.d("Registration", "The Email is valid!");
                        if (password.length() > 12 || password.length() < 6) {
                            Snackbar
                                    .make(
                                            findViewById(R.id.activity_registration),
                                            getString(R.string.passwords_length),
                                            Snackbar.LENGTH_LONG
                                    )
                                    .setTextColor(getResources().getColor(R.color.bg_grey))
                                    .setBackgroundTint(getResources().getColor(R.color.orange))
                                    .show();
                        } else {
                            Log.d("Registration", "Length of password is right!");
                            if (nickname.length() > 12 || nickname.length() < 5) {
                                Snackbar
                                        .make(
                                                findViewById(R.id.activity_registration),
                                                getString(R.string.login_length),
                                                Snackbar.LENGTH_LONG
                                        )
                                        .setTextColor(getResources().getColor(R.color.bg_grey))
                                        .setBackgroundTint(getResources().getColor(R.color.orange))
                                        .show();
                            }
                                else{
                                    mAuth.createUserWithEmailAndPassword(email,password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()){
                                                        Log.d("Registration","Register is successful");
                                                        userID = mAuth.getCurrentUser().getUid();
                                                        DocumentReference documentReference = fstore.collection("users").document(userID);
                                                        Map<String,Object> user = new HashMap<>();
                                                        user.put("nickname",nickname);
                                                        user.put("email",email);
                                                        user.put("id",userID);
                                                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d("Registration","Document is created");
                                                            }
                                                        })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.d("Registration", "Document is created");
                                                                    }
                                                                });
                                                        Intent intent = new Intent(Registration.this, MainPage.class);
                                                        startActivity(intent);
                                                        finish();
                                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                    }
                                                    else{
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
                                                }
                                            });

                                }
                        }
                    }
                }
            }

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
                            String nickname = user.getDisplayName();
                            String email = user.getEmail();
                            userID = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("users").document(userID);
                            Map<String,Object> userGoogle = new HashMap<>();
                            userGoogle.put("nickname",nickname);
                            userGoogle.put("email",email);
                            userGoogle.put("id",userID);
                            documentReference.set(userGoogle).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Registration","Document is created");
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("Registration", "Document is created");
                                        }
                                    });
                            documentReference.set(userGoogle).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Registration","Document is created");
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("Registration", "Document is created");
                                        }
                                    });
                            Intent intToHome = new Intent(Registration.this, MainPage.class);
                            startActivity(intToHome);
                            finish();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
