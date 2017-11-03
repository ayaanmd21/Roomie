package com.grexoft.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.grexoft.roomie.helper.ApplicationContext;
import com.grexoft.roomie.utils.SharedPrefsUtil;
import com.grexoft.roomie.utils.Utils;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_GOOGLE_SIGN_IN = 101;
    private String facebookID = "";
    private String email;
    private String name;
    private String gender;
    private LoginButton btnFacebookLogin;
//    private EditText etUserName, etPassword;
//    private Button btnLogin;
    private CallbackManager callbackManager;
    private Button btnFb;
    private Button btnGmail;
    private GoogleApiClient mGoogleApiClient;
    private String googleID;
    private String socialPhotoUrl;
    private String coverPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);
        btnFacebookLogin = (LoginButton) findViewById(R.id.btn_facebook_login);
        btnFacebookLogin.setReadPermissions("public_profile", "email", "user_friends");
        btnFb = (Button) findViewById(R.id.btn_fb_login);
//        btnLogin = (Button) findViewById(R.id.btn_login);
//        etPassword = (EditText) findViewById(R.id.et_Password);
//        etUserName = (EditText) findViewById(R.id.et_user_name);
        btnGmail = (Button) findViewById(R.id.btn_gmail);
        callbackManager = CallbackManager.Factory.create();


        btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isConnectedToInternet(LoginActivity.this)) {

//                    etUserName.setText("");
//                    etPassword.setText("");
                    btnFacebookLogin.performClick();
                    btnFacebookLogin.setPressed(true);
                    btnFacebookLogin.invalidate();
                    btnFacebookLogin.registerCallback(callbackManager, mCallBack);
                    btnFacebookLogin.setPressed(false);
                    btnFacebookLogin.invalidate();
                } else {
                    Utils.showSnackBar(btnFb, getString(R.string.error_internet));
                }

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        btnGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isConnectedToInternet(LoginActivity.this)) {

//                    etUserName.setText("");
//                    etPassword.setText("");
                    googleSignin();
                } else {
                    Utils.showSnackBar(btnGmail, getString(R.string.error_internet));
                }

            }
        });
//
//        etUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//
//                Utils.setCompoundDrawable(etUserName, LoginActivity.this);
//
//            }
//        });
//
//        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                Utils.setCompoundDrawable(etPassword, LoginActivity.this);
//            }
//        });

    }


    private void googleSignin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);


    }

    private final FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.e("response: ", response + "");
                    try {

                        facebookID = object.getString("id");
                        email = object.getString("email");
                        name = object.getString("name");
                        gender = object.getString("gender");
                        socialPhotoUrl = "https://graph.facebook.com/" + facebookID + "/picture?type=large&width=1080";
//                        coverPhotoUrl="https://graph.facebook.com/" + facebookID + "?fields=cover&access_token=" + AccessToken.getCurrentAccessToken();
//                        coverPhotoUrl = object.getJSONObject("pic_cover").getString("source");

                        ApplicationContext.setUserPhoto(socialPhotoUrl);
                        ApplicationContext.setCoverPhoto(coverPhotoUrl);
                        System.out.println("Photo: url facebook :" + socialPhotoUrl);
                        SharedPrefsUtil.setStringPreference(LoginActivity.this, SharedPrefsUtil.SOCIAL_LOGIN_ID, facebookID);
//                        Intent mIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                        Intent mIntent = new Intent(LoginActivity.this, HomeActivity.class);
                        mIntent.putExtra(SignUpActivity.USER_NAME, email);
                        mIntent.putExtra(SignUpActivity.NAME, name);
                        Toast.makeText(LoginActivity.this, "Welcome " + name, Toast.LENGTH_LONG).show();
                        startActivity(mIntent);
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday,picture");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {
            e.printStackTrace();
            e.getMessage();

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("connection failed: " + connectionResult.toString());
    }


    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();

            if (acct != null) {
                name = acct.getDisplayName();
                email = acct.getEmail();
                googleID = acct.getId();
                socialPhotoUrl = acct.getPhotoUrl() != null ? acct.getPhotoUrl().toString()+"?sz=1080" : "";
                System.out.println("Photo url: "+socialPhotoUrl);
                SharedPrefsUtil.setStringPreference(LoginActivity.this, SharedPrefsUtil.SOCIAL_LOGIN_ID, facebookID);
                Toast.makeText(LoginActivity.this, "Welcome " + name, Toast.LENGTH_LONG).show();
//                Intent mIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                Intent mIntent = new Intent(LoginActivity.this, HomeActivity.class);
                mIntent.putExtra(SignUpActivity.USER_NAME, email);
                mIntent.putExtra(SignUpActivity.NAME, name);
                ApplicationContext.setUserPhoto(socialPhotoUrl);
                startActivity(mIntent);
                finish();
            }

        } else {
            System.out.println("Status:" + result.getStatus());

            Snackbar.make(findViewById(android.R.id.content), "Error in google login", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

}
