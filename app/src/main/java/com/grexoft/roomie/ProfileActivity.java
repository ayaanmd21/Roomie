package com.grexoft.roomie;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.grexoft.roomie.utils.ImageUtil;
import com.grexoft.roomie.utils.Utils;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private static final int REQ_PERMISSION = 105;
    private static final int REQ_CAMERA = 106;
    private static final int REQ_GALLERY = 107;
    private EditText etName, etLoginId, etAdd, etMobile;
    private FloatingActionButton fabEditUpdate, fabPhoto;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale, rbOther;
    private CircleImageView circleImageView;
    private int clickType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        etAdd = (EditText) findViewById(R.id.et_address);
        etName = (EditText) findViewById(R.id.et_name);
        etMobile = (EditText) findViewById(R.id.et_mobile);
        etLoginId = (EditText) findViewById(R.id.et_user_id);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        rbMale = (RadioButton) findViewById(R.id.male);
        rbFemale = (RadioButton) findViewById(R.id.female);
        rbOther = (RadioButton) findViewById(R.id.other);
        fabEditUpdate = (FloatingActionButton) findViewById(R.id.fab_save);
        fabPhoto = (FloatingActionButton) findViewById(R.id.ic_photo);

        FloatingActionButton fabBack = (FloatingActionButton) findViewById(R.id.fab_cancel);

        clickType=getIntent().getIntExtra("clickType",-1);

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Utils.hideSoftKeyboard(ProfileActivity.this);
        circleImageView = (CircleImageView) findViewById(R.id.civ_me);

        etAdd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                Utils.setCompoundDrawable(etAdd, ProfileActivity.this);

            }
        });

        etLoginId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Utils.setCompoundDrawable(etLoginId, ProfileActivity.this);
            }
        });

        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                Utils.setCompoundDrawable(etName, ProfileActivity.this);

            }
        });

        etMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Utils.setCompoundDrawable(etMobile, ProfileActivity.this);
            }
        });

        fabPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.checkPermission(ProfileActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) && Utils.checkPermission(ProfileActivity.this, android.Manifest.permission.CAMERA)) {

                    ImageUtil.selectImage(ProfileActivity.this, REQ_CAMERA, REQ_GALLERY);

                } else {
                    Utils.askPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, REQ_PERMISSION, ProfileActivity.this);
                }
            }
        });

        fabEditUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(clickType==Utils.CLICK_TYPE.DETAIL.getValue()){
                    clickType=Utils.CLICK_TYPE.EDIT.getValue();
                }else {
                    clickType=Utils.CLICK_TYPE.DETAIL.getValue();
                }
                changeUi();
            }
        });
        changeUi();
    }

    private void changeUi(){
        if(clickType==Utils.CLICK_TYPE.DETAIL.getValue()){
            etLoginId.setEnabled(false);
            etAdd.setEnabled(false);
            etMobile.setEnabled(false);
            etName.setEnabled(false);
            rgGender.setEnabled(false);
            rbMale.setEnabled(false);
            rbFemale.setEnabled(false);
            rbOther.setEnabled(false);
            fabPhoto.setVisibility(View.GONE);
            fabEditUpdate.setImageDrawable(ContextCompat.getDrawable(ProfileActivity.this,R.drawable.ic_edit_white24dp));
        }else {
            etLoginId.setEnabled(true);
            etAdd.setEnabled(true);
            etMobile.setEnabled(true);
            etName.setEnabled(true);
            rgGender.setEnabled(true);
            rbMale.setEnabled(true);
            rbFemale.setEnabled(true);
            rbOther.setEnabled(true);
            fabPhoto.setVisibility(View.VISIBLE);
            fabEditUpdate.setImageDrawable(ContextCompat.getDrawable(ProfileActivity.this,R.drawable.ic_done_white));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION: {
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                } else {
                    ImageUtil.selectImage(ProfileActivity.this, REQ_CAMERA, REQ_GALLERY);
                }
                break;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_GALLERY) {

                Uri source1 = data.getData();
                String picturePathsticker = ImageUtil.getPath(ProfileActivity.this, source1);
                if (picturePathsticker != null) {

                    Bitmap bitmap = ImageUtil.decodeFile(new File(picturePathsticker));
                    circleImageView.setImageBitmap(bitmap);
                }


            } else if (requestCode == REQ_CAMERA) {
                String picturePath = ImageUtil.getPath(this);
                if (picturePath != null) {

                    Bitmap bitmap = ImageUtil.decodeFile(new File(picturePath));
                    circleImageView.setImageBitmap(bitmap);


                }

            }

        }
    }

}
