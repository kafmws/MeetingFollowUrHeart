package com.example.hp.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hp.constant.CheckInStatus;
import com.example.hp.model.R;
import com.example.hp.model.UserFaceInfoPack.UserFaceInfo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignInStatusAdapter extends RecyclerView.Adapter<SignInStatusAdapter.ViewHolder> {

    private List<UserFaceInfo> data;
    private Activity activity;

    public SignInStatusAdapter(List<UserFaceInfo> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_sign_status, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        UserFaceInfo info = data.get(i);
        Glide.with(activity).load(info.getAvatarUrl()).into(viewHolder.civ_avatar);
        viewHolder.tv_name.setText(info.getUsername());
        switch (info.getUserStatus()){
            case CheckInStatus.ABSENCE: viewHolder.tv_status.setText("缺勤"); viewHolder.tv_status.setTextColor(Color.RED);break;
            case CheckInStatus.LEAVE: viewHolder.tv_status.setText("请假");viewHolder.tv_status.setTextColor(Color.rgb(51,204,51));break;
            case CheckInStatus.LATE: viewHolder.tv_status.setText("迟到");viewHolder.tv_status.setTextColor(Color.rgb(255,102,0));break;
            case CheckInStatus.NORMAL: viewHolder.tv_status.setText("正常");viewHolder.tv_status.setTextColor(Color.rgb(51,204,51));break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView civ_avatar;
        private TextView tv_name;
        private TextView tv_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            civ_avatar = itemView.findViewById(R.id.civ_avatar);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_status = itemView.findViewById(R.id.tv_status);
        }
    }
}
