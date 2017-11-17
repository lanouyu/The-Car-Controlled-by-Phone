package com.example.gcls.prj_demo;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gcls.prj_demo.util.myBluetoothList;

import java.io.Serializable;
import java.net.ServerSocket;


public class MainActivity extends AppCompatActivity implements Serializable {
    public Button ButtonBtConnect;
    public Intent IntentBtConnect;
    public static BluetoothSocket CarSocket;
    static public BlueTooth blueTooth;
    public Button forwardBt;
    public Button backwardBt;
    public Button leftBt;
    public Button rightBt;
    public Button videoBt;
    public Button gvtBt;
    public Button speechBt;
    public Button gestureBt;
    public Button faceBt;
    public Button pswBt;
    public Button pathBt;
    public MainActivity main;
    public TextView tV;
    public static final int CAMERA_PORT = 8686;
    private ServerSocket cameraSocket;
    public static  Handler handler;
    private Bitmap bitmap;
    private myBluetoothList.Builder getBluetoothList;
    static boolean myflag = false;
//    private ServiceConnection conn = new ServiceConnection() {
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            System.out.println("------Service DisConnected-------");
//        }
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            System.out.println("------Service Connected-------");
//        }
//    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        blueTooth = new BlueTooth(this);
        blueTooth.start();
        ButtonBtConnect = (Button) findViewById(R.id.btnBtConnect);
        pswBt = (Button)findViewById(R.id.pswBtn);
        WifiInfo infowifi=  ((WifiManager)getSystemService(WIFI_SERVICE)).getConnectionInfo();
        main = this;
        tV = (TextView)findViewById(R.id.info);
        int Ip = infowifi.getIpAddress();
        String strIp = "" + (Ip & 0xFF) + "." + ((Ip >> 8) & 0xFF) + "." + ((Ip >> 16) & 0xFF) + "." + ((Ip >> 24) & 0xFF);
        tV.setText(strIp.toString());




        ButtonBtConnect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public  void onClick(View v)
            {
                Button[] taskList;
                taskList = new Button[100];
                String newOne ="搜索到的设备";
                int length = blueTooth.refreshBluetoothDevice();
                int num = 0;
                if(length==0)newOne="未搜索到设备";
                for(;num < length;++num) {
                    taskList[num] = blueTooth.getBluetoothButton[num];
                }
                System.out.println("长度："+length);
                getBluetoothList = new myBluetoothList.Builder(MainActivity.this,taskList,length,newOne);
               getBluetoothList.create().show();
            }
        }
        );
        forwardBt = (Button)findViewById(R.id.forward);
        leftBt = (Button)findViewById(R.id.Left);
        rightBt = (Button)findViewById(R.id.Right);
        backwardBt = (Button)findViewById(R.id.Backward);
        videoBt = (Button)findViewById(R.id.btnVideo);
        gvtBt = (Button)findViewById(R.id.btngravity);
        speechBt = (Button)findViewById(R.id.btnSpeech);
        gestureBt = (Button)findViewById(R.id.btnGesture);
        faceBt = (Button)findViewById(R.id.btnFace);
        pathBt = (Button)findViewById(R.id.btnPath);

        forwardBt.setOnClickListener(new Button.OnClickListener(){ // 点击forward按钮，蓝牙传输信息
            public  void onClick(View v){
                blueTooth.Forward();
            }
        });

        leftBt.setOnClickListener(new Button.OnClickListener(){ // 点击forward按钮，蓝牙传输信息
            public  void onClick(View v){
                blueTooth.TurnLeft();

            }});

        rightBt.setOnClickListener(new Button.OnClickListener(){ // 点击forward按钮，蓝牙传输信息
            public  void onClick(View v){
                blueTooth.TurnRight();

            }});

        backwardBt.setOnClickListener(new Button.OnClickListener(){ // 点击forward按钮，蓝牙传输信息
            public  void onClick(View v){
                blueTooth.Backward();

    }});

        videoBt.setOnClickListener(new Button.OnClickListener(){
            public  void onClick(View v){
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,CameraActivity.class);
                startActivity(intent);
            }
        });

        pswBt.setOnClickListener(new Button.OnClickListener(){
            public  void onClick(View v){
                myflag = true;
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,pwdActivity.class);
                startActivity(intent);
            }
        });


        gvtBt.setOnClickListener(new Button.OnClickListener(){
            public  void onClick(View v){
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,GravityActivity.class);
                //intent.putExtra("main", main);
                startActivity(intent);
            }
        });

        speechBt.setOnClickListener(new Button.OnClickListener(){
            public  void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,SpeechActivity.class);
                //intent.putExtra("blueTooth", blueTooth);
                startActivity(intent);
            }
        });

        pathBt.setOnClickListener(new Button.OnClickListener(){
            public  void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,PathActivity.class);
                //intent.putExtra("blueTooth", blueTooth);
                startActivity(intent);
            }
        });

        gestureBt.setOnClickListener(new Button.OnClickListener(){
            public  void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,GestureActivity.class);
                //intent.putExtra("blueTooth", blueTooth);
                startActivity(intent);
            }
        });

        faceBt.setOnClickListener(new Button.OnClickListener(){
            public  void onClick(View v) {
//                ComponentName componetName = new ComponentName(
//                        //这个是另外一个应用程序的包名
//                        "com.megvii.awesomedemo.facepp",
//                        //这个参数是要启动的Activity
//                        "com.megvii.landmarkproject.LoadingActivity");
//
//                try {
//                    Intent intent = new Intent();
//                    intent.setComponent(componetName);
//                    startActivity(intent);
//                } catch (Exception e) {
////				Toast.makeText(getApplicationContext(), "可以在这里提示用户没有找到应用程序，或者是做其他的操作！", 0).show();
//
//                }

                Intent intent = new Intent();
                intent.setClass(MainActivity.this,FaceActivity.class);
                //intent.putExtra("blueTooth", blueTooth);
                startActivity(intent);
            }
        });




    }
}


