package com.satyajit.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;


import android.widget.Button;

import android.graphics.Color;

import android.graphics.drawable.ColorDrawable;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {



    Typeface Cav;

    SignInButton signIn;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    FirebaseUser user;

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();



            setContentView(R.layout.activity_login);


            initUI();

            setGooglePlusButtonText();



    }


    private void checkAuthenticatedUser() {

        //Step 1 : Log the user info to DB
        //Step 2 : Read from DB and match UID
        //Step 3 : If anything fucks up Invoke the fucked up process


        Toasty.info(this, "Starting Authentication...",Toasty.LENGTH_SHORT).show();

         user = mAuth.getCurrentUser();

        addLoginToDB();






    }


    void matchUID(){

        Toasty.info(this, "Checking Permissions...",Toasty.LENGTH_SHORT).show();

        DocumentReference docRef = db.collection("authenticated_users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        if (document.getData()!=null&&document.getData().get("isAdmin")!=null&&(Boolean) document.getData().get("isAdmin"))
                            SuccessLogin();

                    } else {
                        fuckLog();
                    }
                } else {
                    fuckLog();
                }
            }
        });


    }

    void SuccessLogin(){


        Toasty.success(LoginActivity.this, "Welcome" + " " + user.getDisplayName(), Toast.LENGTH_LONG, true).show();
        LoginActivity.this.finish();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }


    void fuckLog(){

        //Got an intruder / Network / Auth Fail


        showDialog();

        mGoogleSignInClient.signOut();

        mAuth.signOut();


    }



    void addLoginToDB(){

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm");
        Date date = new Date(System.currentTimeMillis());


// Add a new document with a generated ID
        DocumentReference washingtonRef = db.collection("logs_marker").document("XXXXXX");

        final Map<String, Object> userData2 = new HashMap<>();

        userData2.put(   System.currentTimeMillis()+" "+android.os.Build.MODEL, FieldValue.serverTimestamp() );

        final Map<String, Object> userData = new HashMap<>();

        userData.put(user.getUid(), userData2);




        washingtonRef
                .set(userData,SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        matchUID();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        fuckLog();
                    }
                });


    }


    void showDialog(){


        final Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.error_pop);

        Button callBtnPop = dialog.findViewById(R.id.btnOk);

        callBtnPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });

        dialog.show();


    }

    private void setGooglePlusButtonText() {






        for (int i = 0; i < signIn.getChildCount(); i++) {
            View v = signIn.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(getString(R.string.login_with_google));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,17);
                tv.setTypeface(Cav);


                ViewGroup.LayoutParams params = tv.getLayoutParams();

                params.height = 170;
                tv.setLayoutParams(params);

                return;
            }
        }


    }

    void initUI(){


        signIn = findViewById(R.id.sign_in_google);



        Cav = Typeface.createFromAsset(getAssets(),"font/cav.ttf");

        Toasty.Config.getInstance().setToastTypeface(Cav).apply();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

                .requestIdToken("YOUR_TOKEN")
                .requestEmail()
                .requestProfile()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 808);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 808) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

                Toasty.error(this, "FAILED", Toast.LENGTH_SHORT, true).show();

                e.printStackTrace();
                // ...
            }
        }

      
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d("DONE", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                           checkAuthenticatedUser();


                        } else {
                            // If sign in fails, display a message to the user.
                            Toasty.error(LoginActivity.this, "FAILED", Toast.LENGTH_SHORT, true).show();
                          //  LoginActivity.this.finish();

                        }

                        // ...
                    }
                });

    }
    
    
    
    
    
    
    
}
    
    