package com.grexoft.roomie;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.grexoft.roomie.helper.ApplicationContext;
import com.grexoft.roomie.helper.VolleyHelper;
import com.grexoft.roomie.utils.Utils;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private CircleImageView imageView;
    private ImageView ivBg;
    private CardView cvGroup, cvTask, cvLogout, cvPreference, cvPassbook, cvFriends, cvExpense, cvProfile;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        String name = getIntent().getStringExtra("name");
        if (!TextUtils.isEmpty(name)) {
            collapsingToolbarLayout.setTitle(name);
        }

        imageView = (CircleImageView) findViewById(R.id.civ_me);
        cvGroup = (CardView) findViewById(R.id.cardview_group);
        cvTask = (CardView) findViewById(R.id.cardview_task);
        cvLogout = (CardView) findViewById(R.id.cardview_logout);
        cvExpense = (CardView) findViewById(R.id.cardview_expense);
        cvPassbook = (CardView) findViewById(R.id.cardview_passbook);
        cvPreference = (CardView) findViewById(R.id.cardview_setting);
        ivBg = (ImageView) findViewById(R.id.iv_backgound);
        cvFriends = (CardView) findViewById(R.id.cardview_freinds);
        cvProfile = (CardView) findViewById(R.id.cardview_profile);

//        defaultBackround = BitmapFactory.decodeResource(getResources(), R.drawable.material);
        if (ApplicationContext.getUserPhoto() != null) {
            setUserProfilePic(ApplicationContext.getUserPhoto());
        } else {
            imageView.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, R.drawable.circle_image_view));
            ivBg.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, R.drawable.material));
        }
        cvGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, GroupsListActivity.class));
            }
        });
        cvTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showSnackBar(cvTask, "task");
            }
        });
        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(HomeActivity.this,ProfileActivity.class);
                mIntent.putExtra("clickType",Utils.CLICK_TYPE.DETAIL.getValue());
                startActivity(mIntent);
            }
        });
        cvFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cvPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cvExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utils.showAlertDialog(HomeActivity.this, "Logout", "Are you sure,you want to logout?", true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (logout()) {

                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Utils.showSnackBar(cvLogout, "failed to logout");
                        }
                    }
                });


            }
        });
        cvPassbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private boolean logout() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
            return true;
        }
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            return true;
        }
        return false;


    }


    private void setUserProfilePic(String Url) {
        VolleyHelper volleyHelper = new VolleyHelper(HomeActivity.this);
        volleyHelper.callApiGetImage(Url, new VolleyHelper.VolleyBitmapCallback() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onSuccess(Bitmap result) {
                imageView.setImageBitmap(result);
                ivBg.setImageBitmap(Utils.blur(result, HomeActivity.this));
            }

            @Override
            public void onError(String error) {
                imageView.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, R.drawable.circle_image_view));
                ivBg.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this, R.drawable.material));
                System.out.println("error in bitmap: " + error);
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println(connectionResult.toString());
    }
}
