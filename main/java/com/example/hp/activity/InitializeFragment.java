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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.constant.AppData;
import com.example.hp.model.R;
import com.example.hp.util.OkHttpHelper;
import com.example.hp.util.ResponseDataParser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

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
                final Activity activity = getActivity();
                final String doorplate = et_doorplate.getText().toString();
                OkHttpHelper.PostOkHttpRequest(new Request.Builder()
                                .url("http://www.shidongxuan.top/smartMeeting_Web/access/checkMapping.do")
                                .header("Content-Type", "application/x-www-form-urlencoded")
                                .post(new FormBody.Builder()
                                        .add("roomNumber", doorplate)
                                        .add("machineNumber",AppData.getDeviceID()).build())
                                .build()
                        , new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "网络请求失败"+e.getMessage());
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity,"网络超时", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(!ResponseDataParser.validateDoorplateParser(response.body().string())){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity,"绑定有误！请核实后重试！", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Log.e(TAG, "绑定失败");
                            Log.e(TAG,AppData.getDeviceID());
                        }else{
                            Log.e(TAG, "绑定成功");
                            AppData.setDoorplate(doorplate);
                            AppData.setHasDoorplate(true);
                            SharedPreferences sp = activity.getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("hasDoorplate", true);
                            editor.putString("doorplate",AppData.getDoorplate());
                            editor.apply();
                            FragmentManager manager = ((FragmentActivity)activity).getSupportFragmentManager();
                            Fragment fragment = manager.findFragmentById(R.id.fragment_init);
                            if(fragment!=null){
                                manager.beginTransaction().remove(fragment).commit();
                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    activity.findViewById(R.id.fragment_init).setVisibility(View.GONE);
                                    ((DetailsActivity)activity).initialize();
                                }
                            });
                        }
                    }
                });
            }
        });
        return view;
    }
}
