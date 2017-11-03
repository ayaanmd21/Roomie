package com.grexoft.roomie.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grexoft.roomie.R;
import com.grexoft.roomie.helper.VolleyHelper;
import com.grexoft.roomie.models.User;
import com.grexoft.roomie.utils.Utils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sukrit on 20/10/17.
 */

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.ViewHolder> {
    private List<User> userList;
    private VolleyHelper volleyHelper;
    private Context mContext;
    private memberRemoveListener memberRemoveListener;

    public void setClickType(int clickType) {
        this.clickType = clickType;
    }

    private int clickType = -1;

    public MemberListAdapter(List<User> users, Context mContext) {
        this.userList = users;
        volleyHelper = new VolleyHelper(mContext);
        this.mContext = mContext;
    }

    @Override
    public MemberListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_selected_membars_layout, parent, false);

        return new MemberListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MemberListAdapter.ViewHolder holder, final int position) {
        holder.tvName.setText(userList.get(position).getName());
        if (!TextUtils.isEmpty(userList.get(position).getPhotoUrl())) {
            setUserProfilePic(userList.get(position).getPhotoUrl(), holder.groupImage);
        }

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (memberRemoveListener != null) {
                    memberRemoveListener.onRemove(userList.get(position).getUserId());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setMemberRemoveListener(MemberListAdapter.memberRemoveListener memberRemoveListener) {
        this.memberRemoveListener = memberRemoveListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView groupImage;
        private TextView tvName, tvAdmin;
        private ImageView ivDelete;

        ViewHolder(View itemView) {
            super(itemView);
            groupImage = itemView.findViewById(R.id.iv_group_Image);
            tvName = itemView.findViewById(R.id.tv_name);
            ivDelete = itemView.findViewById(R.id.ic_delete);
            tvAdmin = itemView.findViewById(R.id.tv_admin);
            if (clickType == Utils.CLICK_TYPE.DETAIL.getValue()) {
                ivDelete.setVisibility(View.GONE);
            }


        }
    }

    private void setUserProfilePic(String Url, final CircleImageView circleImageView) {


        volleyHelper.callApiGetImage(Url, new VolleyHelper.VolleyBitmapCallback() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onSuccess(Bitmap result) {
                circleImageView.setImageBitmap(result);


            }

            @Override
            public void onError(String error) {
                circleImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.circle_image_view));

            }
        });
    }

    public interface memberRemoveListener {
        public void onRemove(long userId);

    }

}