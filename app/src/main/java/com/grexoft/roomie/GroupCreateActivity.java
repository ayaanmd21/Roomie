package com.grexoft.roomie;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.grexoft.roomie.adapters.MemberListAdapter;
import com.grexoft.roomie.helper.ApplicationContext;
import com.grexoft.roomie.helper.VolleyHelper;
import com.grexoft.roomie.models.Group;
import com.grexoft.roomie.models.User;
import com.grexoft.roomie.utils.ImageUtil;
import com.grexoft.roomie.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupCreateActivity extends AppCompatActivity {
    private static final int REQ_GALLERY = 101;
    private static final int REQ_PERMISSION = 102;
    private static final int REQ_CAMERA = 103;
    private EditText etName, etDescription;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fabSave;
    private ImageView ivPhoto, ivBackground;
    private CircleImageView circleImageView;
    private List<User> userList;
    private VolleyHelper volleyHelper;
    public static final String CLICK_TYPE = "type";
    private int clickType;
    private TextView tvCount;
    public static final String GROUP_DATA = "group";
    private Group group;
    private Button btnAdd, btnExit, btnDelete;
    private CollapsingToolbarLayout collapsingToolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        }

        etName = (EditText) findViewById(R.id.et_name);
        etDescription = (EditText) findViewById(R.id.et_description);
        btnAdd = (Button) findViewById(R.id.btn_add);
        fabSave = (FloatingActionButton) findViewById(R.id.fab_save);
        ivPhoto = (ImageView) findViewById(R.id.ic_photo);
        tvCount = (TextView) findViewById(R.id.tv_count);
        mRecyclerView = (RecyclerView) findViewById(R.id.membersList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(GroupCreateActivity.this, 2));
        circleImageView = (CircleImageView) findViewById(R.id.civ_group);
        ivBackground = (ImageView) findViewById(R.id.iv_backgound);

        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                Utils.setCompoundDrawable(etName, GroupCreateActivity.this);

            }
        });


        etDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                Utils.setCompoundDrawable(etDescription, GroupCreateActivity.this);

            }
        });

        clickType = getIntent().getIntExtra(CLICK_TYPE, -1);
        group = (Group) getIntent().getSerializableExtra(GROUP_DATA);


        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);


        userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User userBuilder = new User.UserBuilder("faizan " + i).photoUrl(ApplicationContext.getUserPhoto()).build();
            userList.add(userBuilder);
        }

        tvCount.setText("member(s): " + userList.size());

        volleyHelper = new VolleyHelper(GroupCreateActivity.this);

        MemberListAdapter memberListAdapter = new MemberListAdapter(userList, this);
        memberListAdapter.setClickType(clickType);
        mRecyclerView.setAdapter(memberListAdapter);


        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        if (clickType == Utils.CLICK_TYPE.EDIT.getValue()) {
            btnDelete = (Button) findViewById(R.id.btn_delete);
            btnExit = (Button) findViewById(R.id.btn_exit);
            btnDelete.setVisibility(View.VISIBLE);
            btnExit.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(group.getGroupName())) {
                collapsingToolbarLayout.setTitle(group.getGroupName());
            }
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            btnExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            etName.setText(group.getGroupName());
            etDescription.setText(group.getGroupDescription());
            Utils.setUserProfilePic(group.getGroupPhotoUrl(), circleImageView, this, volleyHelper);
            Utils.setUserBackgroundPic(group.getGroupPhotoUrl(), ivBackground, this, volleyHelper);
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ivPhoto.bringToFront();

        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.checkPermission(GroupCreateActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) && Utils.checkPermission(GroupCreateActivity.this, Manifest.permission.CAMERA)) {

                    ImageUtil.selectImage(GroupCreateActivity.this,REQ_CAMERA,REQ_GALLERY);

                } else {
                    Utils.askPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, REQ_PERMISSION, GroupCreateActivity.this);
                }

            }
        });

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
                    ImageUtil.selectImage(GroupCreateActivity.this,REQ_CAMERA,REQ_GALLERY);
                }
                break;
            }

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_GALLERY) {

                Uri source1 = data.getData();
                String picturePathsticker = ImageUtil.getPath(GroupCreateActivity.this, source1);
                if (picturePathsticker != null) {

                    Bitmap bitmap = ImageUtil.decodeFile(new File(picturePathsticker));
//                    Bitmap bitmap = BitmapFactory.decodeFile(picturePathsticker);
                    circleImageView.setImageBitmap(bitmap);
                    ivBackground.setImageBitmap(Utils.scaleAndBlur(this, bitmap));
                }
                System.out.println(picturePathsticker);


            } else if (requestCode == REQ_CAMERA) {
                String picturePath = ImageUtil.getPath(this);
                if (picturePath != null) {

                    Bitmap bitmap = ImageUtil.decodeFile(new File(picturePath));
//                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                    circleImageView.setImageBitmap(bitmap);


                    ivBackground.setImageBitmap(Utils.scaleAndBlur(this, bitmap));
//                    ivBackground.setImageBitmap(Utils.blur(bitmap, this));
                }

            }

        }
    }


}
