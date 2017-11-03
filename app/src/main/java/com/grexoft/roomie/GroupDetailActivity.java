package com.grexoft.roomie;

import android.content.Intent;
import android.os.Bundle;
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
import com.grexoft.roomie.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupDetailActivity extends AppCompatActivity {
    private EditText etName, etDescription;
    private Button btnExit,btnDelete;
    private RecyclerView mRecyclerView;
    private ImageView ivBackground;
    private CircleImageView circleImageView;
    private List<User> userList;
    private VolleyHelper volleyHelper;
    public static final String CLICK_TYPE = "type";
    private int clickType;
    private TextView tvCount;
    public static final String GROUP_DATA = "group";
    private Group group;
    private FloatingActionButton fabSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        }

        etName = (EditText) findViewById(R.id.et_name);
        etDescription = (EditText) findViewById(R.id.et_description);
        Button btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setVisibility(View.GONE);
        fabSave = (FloatingActionButton) findViewById(R.id.fab_save);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnDelete.setVisibility(View.VISIBLE);
        btnExit.setVisibility(View.VISIBLE);

        tvCount = (TextView) findViewById(R.id.tv_count);
        mRecyclerView = (RecyclerView) findViewById(R.id.membersList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(GroupDetailActivity.this, 2));
        circleImageView = (CircleImageView) findViewById(R.id.civ_group);
        ivBackground = (ImageView) findViewById(R.id.iv_backgound);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        group = (Group) getIntent().getSerializableExtra(GROUP_DATA);
        userList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User userBuilder = new User.UserBuilder("faizan " + i).photoUrl(ApplicationContext.getUserPhoto()).build();
            userList.add(userBuilder);
        }

        clickType = getIntent().getIntExtra(CLICK_TYPE, -1);
        group = (Group) getIntent().getSerializableExtra(GROUP_DATA);


        etName.setFocusable(false);
        etName.setEnabled(false);
        etDescription.setFocusable(false);
        etDescription.setEnabled(false);

        etName.setText(group.getGroupName());
        etDescription.setText(group.getGroupDescription());

        tvCount.setText("member(s): " + userList.size());

        MemberListAdapter memberListAdapter = new MemberListAdapter(userList, this);
        memberListAdapter.setClickType(clickType);
        mRecyclerView.setAdapter(memberListAdapter);


        volleyHelper=new VolleyHelper(this);

        Utils.setUserProfilePic(group.getGroupPhotoUrl(), circleImageView,this,volleyHelper);
        Utils.setUserBackgroundPic(group.getGroupPhotoUrl(), ivBackground,this,volleyHelper);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(GroupDetailActivity.this, GroupCreateActivity.class);
                mIntent.putExtra(GroupCreateActivity.CLICK_TYPE, Utils.CLICK_TYPE.EDIT.getValue());
                mIntent.putExtra(GroupCreateActivity.GROUP_DATA, group);
                startActivity(mIntent);
            }
        });

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

        if (!TextUtils.isEmpty(group.getGroupName())) {
            collapsingToolbarLayout.setTitle(group.getGroupName());
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);

    }

}
