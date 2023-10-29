package com.example.nahid.foodbuzzv1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.google.android.gms.common.SignInButton;
import android.content.Intent;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;



public class PhoneLoginActivity extends AppCompatActivity {




    public static final int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;
    public static final String TAG = "PhoneLoginActivity";


    private EditText phoneText;
    private EditText codeText;
    private Button verifyButton;
    private Button sendButton;
    private Button resendButton;

    private TextView statusText;



    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        //mobile text

        phoneText = (EditText) findViewById(R.id.et_Phone);
        codeText = (EditText) findViewById(R.id.et_code);
        verifyButton = (Button) findViewById(R.id.btn_verify);
        sendButton = (Button) findViewById(R.id.btn_send);
        resendButton = (Button) findViewById(R.id.btn_resend);

        statusText = (TextView) findViewById(R.id.tv_status);

        verifyButton.setEnabled(false);
        resendButton.setEnabled(false);

        statusText.setText("Signed Out");



        //google


        mAuth = FirebaseAuth.getInstance();




        // Configure Google Sign In

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();



        // Build a GoogleSignInClient with the options specified by gso.

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(PhoneLoginActivity.this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }







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
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

                // Google Sign In failed, update UI appropriately

                Log.w(TAG, "Google sign in failed", e);

                // ...

            }

        }

    }





    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());



        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information

                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {

                            // If sign in fails, display a message to the user.

                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(PhoneLoginActivity.this,"you are not able to log in to google",Toast.LENGTH_LONG).show();
                            updateUI(null);

                        }


                    }

                });

    }



    private void updateUI(FirebaseUser user) {




        statusText.setText("Signed In");
        sendButton.setEnabled(false);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        if (acct != null) {

            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            Toast.makeText(this, "Name of the user :" + personName + " user id is : " + personId, Toast.LENGTH_SHORT).show();

        }


    }


    //text verification

    public void sendCode(View view) {

        String phoneNumber = phoneText.getText().toString();



        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(

                phoneNumber,        // Phone number to verify

                60,                 // Timeout duration

                TimeUnit.SECONDS,   // Unit of timeout

                this,               // Activity (for callback binding)

                verificationCallbacks);

    }



    private void setUpVerificatonCallbacks() {



        verificationCallbacks =

                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {



                    @Override

                    public void onVerificationCompleted(

                            PhoneAuthCredential credential) {


                        statusText.setText("Signed In");
                        resendButton.setEnabled(false);
                        verifyButton.setEnabled(false);
                        codeText.setText("");
                        signInWithPhoneAuthCredential(credential);

                    }



                    @Override

                    public void onVerificationFailed(FirebaseException e) {



                        if (e instanceof FirebaseAuthInvalidCredentialsException) {

                            // Invalid request

                            Log.d(TAG, "Invalid credential: "

                                    + e.getLocalizedMessage());

                        } else if (e instanceof FirebaseTooManyRequestsException) {

                            // SMS quota exceeded

                            Log.d(TAG, "SMS Quota exceeded.");

                        }

                    }


                    @Override

                    public void onCodeSent(String verificationId,

                                           PhoneAuthProvider.ForceResendingToken token) {

                        phoneVerificationId = verificationId;
                        resendToken = token;

                        verifyButton.setEnabled(true);
                        sendButton.setEnabled(false);
                        resendButton.setEnabled(true);

                    }

                };

    }



    public void verifyCode(View view) {



        String code = codeText.getText().toString();



        PhoneAuthCredential credential =

                PhoneAuthProvider.getCredential(phoneVerificationId, code);

        signInWithPhoneAuthCredential(credential);

    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            codeText.setText("");
                            statusText.setText("Signed In");
                            resendButton.setEnabled(false);
                            verifyButton.setEnabled(false);
                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(PhoneLoginActivity.this,"You are signed in with Phone Number",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(PhoneLoginActivity.this, SettingActivity.class);
                            startActivity(intent);






                        } else {

                            if (task.getException() instanceof

                                    FirebaseAuthInvalidCredentialsException) {

                                // The verification code entered was invalid

                            }

                        }

                    }

                });

    }


    public void resendCode(View view) {

        String phoneNumber = phoneText.getText().toString();

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks,
                resendToken);

    }




}
