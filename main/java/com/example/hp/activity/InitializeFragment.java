package com.example.hp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hp.constant.AppData;
import com.example.hp.model.R;

public class InitializeFragment extends Fragment {

    private String TAG = "InitFragment";

    private TextView tv_deviceId;
    private EditText et_doorplate;
    private Button btn_sure;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        final View view = inflater.inflate(R.layout.layout_intialize_fragment, container, false);
        tv_deviceId = view.findViewById(R.id.tv_deviceId);
        tv_deviceId.setText("设备号: "+AppData.getDeviceID());
        et_doorplate = view.findViewById(R.id.et_doorplate);
        et_doorplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText)v).setCursorVisible(true);
            }
        });
        btn_sure = view.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppData.setDeviceID(et_doorplate.getText().toString());
                AppData.setHasDeviceId(true);
                Activity activity = getActivity();
                SharedPreferences sp = activity.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("hasDeviceId", true);
                editor.putString("deviceId",AppData.getDeviceID());
                editor.apply();
                FragmentManager manager = ((FragmentActivity)activity).getSupportFragmentManager();
                Fragment fragment = manager.findFragmentById(R.id.fragment_init);
                if(fragment!=null){
                    manager.beginTransaction().remove(fragment).commit();
                }
                activity.findViewById(R.id.fragment_init).setVisibility(View.GONE);
                ((DetailsActivity)activity).initialize();
            }
        });
        return view;
    }
}
