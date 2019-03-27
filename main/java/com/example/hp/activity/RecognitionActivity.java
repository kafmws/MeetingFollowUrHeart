package com.example.hp.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.opengl.EGLConfig;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.example.hp.constant.AppData;
import com.example.hp.constant.CheckInStatus;
import com.example.hp.model.CheckInStatusResponse;
import com.example.hp.model.MeetingCheckInStatus;
import com.example.hp.model.R;
import com.example.hp.model.UserFaceInfo;
import com.example.hp.util.HardwareUtil;
import com.example.hp.util.ImageUtil;
import com.example.hp.util.OkHttpHelper;
import com.example.hp.util.ResponseDataParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class RecognitionActivity extends BaseActivity implements SurfaceHolder.Callback {

    private String TAG = "RecognitionActivity";
    private String data = "AAD6RAAAdENy6+28fiuhuF+MpbyaXPc8GYkPPQKcnb31dBA8g9c1vbfJMb0P9b28arRvvbz6kL31\n" +
            "    7ZO9+VOSPM4mZ73Ft4Y6yei8vD5nar1Qh/E9lJKAvQWK8jy2bZy9F/LEvZb/8bwY8d07nt55vROI\n" +
            "    er1LUOA89ZedvYpO5LkfAgM9p6FhPZUG0rzUyaM9PA2zvdRi5zyVSlC8HoMHPmTAwL2aDHG9MAfD\n" +
            "    PDHApb0Y7DW8vh/MvbWv4zxlk189SwpHPTY06jxxRjG9rTQSvi2HjD3Dyhc82CqDPfWdpLzJzSM9\n" +
            "    SXaOPDQWeb0HLUC88cEPvbfphT2GipC9UrzWPT0w1T0iIj09pgwxvJKxfbx99LG7VeyQvRrC2zzA\n" +
            "    4w28q8uQvJAeoL0gfJu9zx3ovcVUyD0f5l09yxqlPeEP6zzR1G09m2wWvZBwVj0ugCm8ccUkPZ3f\n" +
            "    TTxcfTq8AHHzvF8WwLxEVzk9Za2bvfQPgj1Xjiy9DxMePfUN2D3g976929Xhu9kfILymtRG+H+SB\n" +
            "    vStMuDztfVO9UgV+O3T+Qb31JRA90bSlvLmw0jw0LSc9zowzvaDXVL0APQS9W1kUvuHZ7jwW8Zk8\n" +
            "    T4FmvZFA8T1BqxA75EO/vU9ttLx03gc9c0kjPVg3Ij0uzjk9ExIOvb6zpbyiCDY9FuKOPAuqrbwC\n" +
            "    WSQ87GoOPgNt4jwDNRW8qaciPVhgYr2IxLk8hw/FPbpdFD3w6tO8hXVxPQruCz3IhPe9QQeYPbr5\n" +
            "    Fr3zfbI8HOC0vKKlDL2scYi9A2emPCgIKT0e9zm9jmYRvgVPab3E/oW9+UUQvVWw3D2A3uy9H6Hp\n" +
            "    vMy7hz12LWO97Qdnuzi5gT1SJc87fBlxPNd+9rvxIai9wXRBPSkzMj4E+hq8GrLMPMlh1j3qz4i8\n" +
            "    OwfrPIJctL3q1nW8rnbBvEUIlT2VjAg9BSyavScKurvAq309aPpKvXbkdz2Xmhq9bjoePjB3wL0G\n" +
            "    NRS9o0s1voi/mT2qSm884Q3OPYitJz2El3S83JmXvTI2Szzn5ei90O9mvbE+3zviOpA9cjBcvS6O\n" +
            "    LL2Q2vG8G7zlvAzXh725Lsw95mjPPFFze73Gl648c+laPRbB6Ty4d+C9cKsuu1z/kzxNj8s9i4fy\n" +
            "    Pfw70D09vk89P/M2u0Vpmz1AuzC8RlQIPFpcVLyII6C8btJPPR6XTr3eyTo8XUDQvZGKM75UpEk9\n" +
            "    EDsFPDSxV73ma4895wmqva7fkL1HemK81RJhPK/F7jxU5iQ90hkZPqZ0Rbym5Sc9skRmPId2ib0O\n" +
            "    k8A9PtENvAnauTwxCgm834oKPOQO0byZuaO9fVmwPTUZg718sV+9n+rHPXiXKT1zE0Y9iP6IPLnU\n" +
            "    mr0sJbG9";
    private List<UserFaceInfo> userFaceInfos;//用户信息
    private List<FaceFeature> featureList;//用户人脸信息
    private MeetingCheckInStatus meetingCheckInStatus;//用户到会状况
    private int currentMeetingId;
    private long beginTime;
    private HardwareUtil door;

    private Camera camera;
    private GLSurfaceView preview;
    private SurfaceHolder holder;

    int screenWidth, screenHeight;
    boolean isPreview = false; // 是否在浏览中


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_recognition);
        preview = findViewById(R.id.surface_view);
        preview.setRenderer(new MyGLRenderer());
        holder = preview.getHolder();
        holder.addCallback(this);

        currentMeetingId = getIntent().getIntExtra("currentMeetingId", -1);
        beginTime = getIntent().getLongExtra("beginTime", -1);
        getFaceFeatures();

        door = new HardwareUtil(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera == null) {
            camera = getCamera();
            if (camera != null && holder != null) {
                setStartPreview(camera, holder);
            }
        }
        int errorCode = engine.init(this,
                FaceEngine.ASF_DETECT_MODE_IMAGE,
                FaceEngine.ASF_OP_0_HIGHER_EXT,
                16, 5,
                FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_FACE_RECOGNITION);
        Log.e("com.arcsoft", "AFR_FSDK_InitialEngine = " + errorCode);
    }

    private int getCameraId() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        int count = Camera.getNumberOfCameras();
        for (int i = 0; i < count; i++) {
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.e(TAG, "有前置摄像头ID=" + i);
                return i;
            }
        }
        Log.e(TAG, "没有前置摄像头");
        return 0;
    }

    /*得到摄像头*/
    private Camera getCamera() {

        Camera camera = Camera.open(getCameraId());
        ;
        if (camera == null)
            Log.e(TAG, "fucking failed");
        try {
            if (camera != null) {//&& !isPreview
                try {
                    Log.e(TAG, "coming");
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setPreviewSize(screenWidth, screenHeight); // 设置预览照片的大小
                    parameters.setPreviewFpsRange(20, 30); // 每秒显示20~30帧
                    parameters.setPictureFormat(ImageFormat.NV21); // 设置图片格式
                    parameters.setPictureSize(screenWidth, screenHeight); // 设置照片的大小
                    parameters.setPreviewFrameRate(3);// 每秒3帧 每秒从摄像头里面获得3个画面,
                    // camera.setParameters(parameters); // android2.3.3以后不需要此行代码
                    camera.setPreviewDisplay(holder); // 通过SurfaceView显示取景画面
                    camera.setPreviewCallback(new Camera.PreviewCallback() {
                        @Override
                        public void onPreviewFrame(byte[] data, Camera camera) {
                            Camera.Size size = camera.getParameters().getPreviewSize();
                            try {
                                // 调用image.compressToJpeg（）将YUV格式图像数据data转为jpg格式
                                YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width,
                                        size.height, null);
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, bos);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, stream);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size());
                                if (compare(bitmap, featureList)) {
                                    door.openTheDoor();//给单片机发信号
                                }
                                bos.flush();

                            } catch (Exception ex) {
                                Log.e(TAG, "Error:" + ex.getMessage());
                            }
                        }
                    }); // 设置回调
                    camera.startPreview(); // 开始预览
                    camera.autoFocus(null); // 自动对焦
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isPreview = true;
            }
        } catch (Exception e) {
            camera = null;
            e.printStackTrace();
            Toast.makeText(this, "前置摄像头开启失败", Toast.LENGTH_LONG).show();
        }
        return camera;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onDestroy() {
        if (camera != null) {
            camera.release();
        }
        super.onDestroy();
        door.unregisterReceiver();
        //销毁人脸识别引擎
        int errorCode = engine.unInit();
        Log.e("com.arcsoft", "AFR_FSDK_UninitialEngine : " + errorCode);
    }

    /*
     开始预览相机内容
    */
    private void setStartPreview(Camera camera, SurfaceHolder holder) {
        Log.e(TAG, "setStartPreview");
        try {
            camera.setPreviewDisplay(holder);
//            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
释放相机资源
 */
    private void releaseCamera() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG, "surfaceCreated");
        setStartPreview(camera, holder);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        setStartPreview(camera, holder);
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    private void compare(Bitmap bitmapLeft, Bitmap bitmapRight) {

//        FaceEngine engine = new FaceEngine();

        byte[] NV21FaceLeft = ImageUtil.bitmapToNv21(bitmapLeft,
                bitmapLeft.getWidth(), bitmapLeft.getHeight());

        byte[] NV21FaceRight = ImageUtil.bitmapToNv21(bitmapRight,
                bitmapRight.getWidth(), bitmapRight.getHeight());

        //用来存放提取到的人脸信息, face_1 是注册的人脸，face_2 是要识别的人脸
        List<FaceInfo> faceLeft = new ArrayList<>();
        List<FaceInfo> faceRight = new ArrayList<>();

        //初始化人脸识别引擎，使用时请替换申请的 APPID 和 SDKKEY
        int errorCode = engine.init(this,
                FaceEngine.ASF_DETECT_MODE_IMAGE,
                FaceEngine.ASF_OP_0_HIGHER_EXT,
                16, 5,
                FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_FACE_RECOGNITION);
        Log.e("com.arcsoft", "AFR_FSDK_InitialEngine = " + errorCode);

        errorCode = engine.detectFaces(NV21FaceLeft, bitmapLeft.getWidth(),
                bitmapLeft.getHeight(), FaceEngine.CP_PAF_NV21, faceLeft);

        Log.e("com.arcsoft", "detectLeftFaces = " + errorCode);

        errorCode = engine.detectFaces(NV21FaceRight, bitmapRight.getWidth(),
                bitmapRight.getHeight(), FaceEngine.CP_PAF_NV21, faceRight);

        Log.e("com.arcsoft", "detectRightFaces = " + errorCode);

        if (faceLeft.size() != 1 || faceRight.size() != 1) {
//            Toast.makeText(this,"请上传单人照片",Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "faceLeft = " + faceLeft.size() + "faceRight = " + faceRight.size()
                    , Toast.LENGTH_SHORT).show();
            return;
        }

        FaceFeature featureLeft = new FaceFeature();
        FaceFeature featureRight = new FaceFeature();

        FaceInfo face1 = faceLeft.get(0);
        FaceInfo face2 = faceRight.get(0);

        //输入的 data 数据为 NV21 格式（如 Camera 里 NV21 格式的 preview 数据）；人脸坐标一般使用人脸检测返回的 Rect 传入；人脸角度请按照人脸检测引擎返回的值传入。
        errorCode = engine.extractFaceFeature(NV21FaceLeft, bitmapLeft.getWidth(), bitmapLeft.getHeight(),
                FaceEngine.CP_PAF_NV21, face1, featureLeft);
        Log.e("com.arcsoft", "Face=" + featureLeft.getFeatureData()[0] + ","
                + featureLeft.getFeatureData()[1] + "," + featureLeft.getFeatureData()[2] + "," + errorCode);

        errorCode = engine.extractFaceFeature(NV21FaceRight, bitmapRight.getWidth(), bitmapRight.getHeight(),
                FaceEngine.CP_PAF_NV21, face2, featureRight);
        Log.e("com.arcsoft", "Face=" + featureRight.getFeatureData()[0] + ","
                + featureRight.getFeatureData()[1] + "," + featureRight.getFeatureData()[2] + "," + errorCode);

        //score 用于存放人脸对比的相似度值
        FaceSimilar score = new FaceSimilar();
        errorCode = engine.compareFaceFeature(featureLeft, featureRight, score);
        Toast.makeText(this,
                "相似度 : " + (int) (score.getScore() * 100) + "%", Toast.LENGTH_SHORT).show();
        Log.e("com.arcsoft", "AFR_FSDK_FacePairMatching=" + errorCode);
        Log.e("com.arcsoft", "Score:" + score.getScore());

        Toast.makeText(this, String.valueOf((int) (score.getScore() * 100)), Toast.LENGTH_SHORT).show();

        //销毁人脸识别引擎
        errorCode = engine.unInit();
        Log.e("com.arcsoft", "AFR_FSDK_UninitialEngine : " + errorCode);
    }

    private boolean compare(Bitmap bitmap, List<FaceFeature> data) {

        int personIndex = 0;
        byte[] NV21FaceLeft = ImageUtil.bitmapToNv21(bitmap,
                bitmap.getWidth(), bitmap.getHeight());

        //用来存放提取到的人脸信息, face_1 是注册的人脸，face_2 是要识别的人脸
        List<FaceInfo> faceInfoList = new ArrayList<>();

        //初始化人脸识别引擎，使用时请替换申请的 APPID 和 SDKKEY

        int errorCode = engine.detectFaces(NV21FaceLeft, bitmap.getWidth(),
                bitmap.getHeight(), FaceEngine.CP_PAF_NV21, faceInfoList);

        Log.e("com.arcsoft", "detectFaces = " + errorCode);

        if (faceInfoList.size() != 1) {
//            Toast.makeText(this,"请上传单人照片",Toast.LENGTH_SHORT).show();
            Log.e(TAG, "faceInfoListSize = " + faceInfoList.size());
            return false;
        }

        FaceFeature feature = new FaceFeature();
        FaceInfo faceInfo = faceInfoList.get(0);

        //输入的 data 数据为 NV21 格式（如 Camera 里 NV21 格式的 preview 数据）；人脸坐标一般使用人脸检测返回的 Rect 传入；人脸角度请按照人脸检测引擎返回的值传入。
        errorCode = engine.extractFaceFeature(NV21FaceLeft, bitmap.getWidth(), bitmap.getHeight(),
                FaceEngine.CP_PAF_NV21, faceInfo, feature);
        Log.e("com.arcsoft", "Face=" + feature.getFeatureData()[0] + ","
                + feature.getFeatureData()[1] + "," + feature.getFeatureData()[2] + "," + errorCode);

        //score 用于存放人脸对比的相似度值
        boolean isConfirm = false;
        FaceSimilar score = new FaceSimilar();
        for (int i = 0; i < data.size(); i++) {
            errorCode = engine.compareFaceFeature(feature, data.get(i), score);
            personIndex = i;
            Log.e("com.arcsoft", "AFR_FSDK_FacePairMatching=" + errorCode);
            Log.e("com.arcsoft", "Score:" + score.getScore());
            if (score.getScore() > 0.85) {
                isConfirm = true;
                Toast.makeText(this, "验证成功", Toast.LENGTH_SHORT).show();
                break;
            }
        }
//        Toast.makeText(this,
//                "相似度 : "+(int)(score.getScore()*100)+"%",Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, String.valueOf((int)(score.getScore()*100)),Toast.LENGTH_SHORT).show();

        if (isConfirm) {//比对成功，更新签到状态
            updateCheckInStatus(personIndex);
        }
        return isConfirm;
    }

    private void updateCheckInStatus(int personIndex) {
        String checkInStatus = null;
        long currentTime = new Date().getTime();
        checkInStatus = currentTime <= (beginTime - 10000) ? CheckInStatus.NORMAL : CheckInStatus.LATE;
        OkHttpHelper.PostOkHttpRequest(new Request.Builder()
                .url("http://www.shidongxuan.top/smartMeeting_Web/access/uploadUserMeetingStatus.do")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(new FormBody.Builder()
                        .add("userId", String.valueOf(userFaceInfos.get(personIndex).getUserId()))
                        .add("meetingId", String.valueOf(currentMeetingId))
                        .add("userStatus", checkInStatus)
                        .build())
                .build(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "网络错误,签到失败");
                        Toast.makeText(RecognitionActivity.this, "网络错误,签到失败!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final CheckInStatusResponse status = ResponseDataParser.checkInStatusResponseParser(data);
                if (status.getStatus() != 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAG, "返回状态出错" + status.getMsg());
                            Toast.makeText(RecognitionActivity.this, "网络错误,签到失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAG, "返回状态正常" + status.getMsg());
                            Toast.makeText(RecognitionActivity.this, "签到成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    private void getFaceFeatures() {
        OkHttpHelper.SendOkHttpRequest("http://www.shidongxuan.top/" +
                        "smartMeeting_Web/access/getAllUserStatus.do"
                        + "?meetingId=" + currentMeetingId
                , new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "网络请求失败");
                                Toast.makeText(RecognitionActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        userFaceInfos = ResponseDataParser.faceFeaturesParser(data);
                        featureList = new ArrayList<>();
                        for (UserFaceInfo info : userFaceInfos) {
                            featureList.add(info.getFaceFeature());
                        }
                        FaceFeature featureTest = new FaceFeature(new FaceFeature(Base64.decode(data, 0)));
                        userFaceInfos.add(new UserFaceInfo(-1, "wangshunTest", featureTest));
                        featureList.add(featureTest);
                    }
                });
    }

    private void getMeetingCheckInStatus() {
        OkHttpHelper.SendOkHttpRequest(
                "http://www.shidongxuan.top/smartMeeting_Web/access/getAllUserStatus.do" + currentMeetingId,
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "网络请求失败");
                                Toast.makeText(RecognitionActivity.this, "数据获取失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        MeetingCheckInStatus status = ResponseDataParser
                                .meetingCheckInStatusParser(response.body().string());
                    }
                }
        );

    }
}


class MyGLRenderer implements GLSurfaceView.Renderer {
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {
        gl.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
    }

    public void onSurfaceChanged(GL10 gl, int w, int h) {
        gl.glViewport(0, 0, w, h);
    }

    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    }
}