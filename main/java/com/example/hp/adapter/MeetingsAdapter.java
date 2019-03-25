package com.example.hp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hp.model.MeetingDetails;
import com.example.hp.model.R;

import java.util.Date;
import java.util.List;

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.ViewHolder> {

    private List<MeetingDetails> meetings;

    private Context mContext;

    private OnItemClickListener mListener;

    public void setClickListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public MeetingsAdapter(List<MeetingDetails> meetings, Context mContext) {
        this.meetings = meetings;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_meetings, viewGroup, false);
//        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {
        MeetingDetails meeting = meetings.get(pos);
        viewHolder.setData(meeting, pos);
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

//    @Override
//    public void onClick(View v) {
//        if (mListener != null) {
//            mListener.onItemClick(v, (int) v.getTag());
//        }
//    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_meeting_name;
        private TextView tv_meeting_status;
        private TextView tv_people_number;
        private TextView tv_meeting_master;
        private TextView tv_place;
        private TextView tv_meeting_time_length;
        private TextView tv_meeting_time;
//        private View view;
        private View line;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
//            this.view = itemView;
            tv_meeting_name = itemView.findViewById(R.id.tv_meeting_name);
            tv_meeting_status = itemView.findViewById(R.id.tv_meeting_status);
            tv_people_number = itemView.findViewById(R.id.tv_people_number);
            tv_meeting_master = itemView.findViewById(R.id.tv_meeting_master);
            tv_place = itemView.findViewById(R.id.tv_place);
            tv_meeting_time_length = itemView.findViewById(R.id.tv_meeting_time_length);
            tv_meeting_time = itemView.findViewById(R.id.tv_meeting_time);
            line = itemView.findViewById(R.id.view_line);
        }

        void setData(MeetingDetails meeting, int i) {
//            view.setTag(i);
            MeetingDetails.DataBean data = meeting.getData();
            tv_meeting_name.setText(data.getMeetingName());
            tv_people_number.setText(data.getPeopleNum());
            tv_place.setText(data.getRoomName());
            tv_meeting_time_length.setText("100");
            tv_meeting_master.setText("");
            tv_meeting_time.setText(data.getStartTime());
            if (i == meetings.size() - 1) {
                line.setVisibility(View.GONE);
            } else {
                line.setVisibility(View.VISIBLE);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
