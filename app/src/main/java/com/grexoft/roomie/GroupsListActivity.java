package com.grexoft.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grexoft.roomie.helper.ApplicationContext;
import com.grexoft.roomie.helper.VolleyHelper;
import com.grexoft.roomie.models.Group;
import com.grexoft.roomie.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupsListActivity extends AppCompatActivity {
    private List<Group> groupList;
    private VolleyHelper volleyHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        volleyHelper = new VolleyHelper(GroupsListActivity.this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(GroupsListActivity.this, GroupCreateActivity.class);
                mIntent.putExtra(GroupCreateActivity.CLICK_TYPE, Utils.CLICK_TYPE.NEW.getValue());
                startActivity(mIntent);
            }
        });


        groupList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Group groupBuilder = new Group.GroupBuilder("Room Rent " + i).description("Here add the room rent").photoUrl(ApplicationContext.getUserPhoto()).build();
            groupList.add(groupBuilder);
        }
        GroupListAdapter groupListAdapter = new GroupListAdapter();
        recyclerView.setAdapter(groupListAdapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);

    }

    private class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(
                    parent.getContext());
            View v = inflater.inflate(R.layout.grouplist_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.tvName.setText(groupList.get(position).getGroupName());
            holder.tvGroupDescription.setText(groupList.get(position).getGroupDescription());
            if (!TextUtils.isEmpty(groupList.get(position).getGroupPhotoUrl())) {
               Utils.setUserProfilePic(groupList.get(position).getGroupPhotoUrl(), holder.groupImage,GroupsListActivity.this,volleyHelper);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Group groupBuilder = new Group.GroupBuilder(groupList.get(position).getGroupName()).description(groupList.get(position).getGroupDescription()).photoUrl(groupList.get(position).getGroupPhotoUrl()).build();
                    Intent mIntent = new Intent(GroupsListActivity.this, GroupDetailActivity.class);
                    mIntent.putExtra(GroupCreateActivity.CLICK_TYPE, Utils.CLICK_TYPE.DETAIL.getValue());
                    mIntent.putExtra(GroupCreateActivity.GROUP_DATA, groupBuilder);
                    startActivity(mIntent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return groupList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private CircleImageView groupImage;
            private TextView tvName;
            private TextView tvGroupDescription;
            //            private TextView tvMemberCount;
            private ImageView ivMore;

            ViewHolder(View itemView) {
                super(itemView);
                groupImage = itemView.findViewById(R.id.iv_group_Image);
                tvName = itemView.findViewById(R.id.tv_name);
                tvGroupDescription = itemView.findViewById(R.id.tv_description);
                ivMore = itemView.findViewById(R.id.iv_more_action);

            }
        }


    }
}
