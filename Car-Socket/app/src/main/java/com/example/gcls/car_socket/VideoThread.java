package com.example.gcls.car_socket;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by shiyu on 15/12/18.
 */
public class VideoThread extends Thread {

    private MainActivity main;
    private Button ButtonBegin;
    public TextView TestText;
    public SurfaceHolder surfaceHolder;
    private boolean isPreview;
    private final static int CAMERA_PORT = 8686;
    private final static int PREVIEW_WIDTH = 200, PREVIEW_HEIGHT = 200;
    private static int counter = 0;
    private static final int PERIOD = 5;
    private android.hardware.Camera camera;
    private static final int MAX_FACES = 1;
    //private FaceRecognizer faceRecognizer;
    private Bitmap bitmap;
    private Bitmap tmpBmp;
    private byte[] byteArray;
    private  Bitmap BitmapForCompress;
    private byte[] byteForCompress;
    boolean detected;
    RectF faceRects[] = new RectF[MAX_FACES];
    int detectedFaces = 0;
    int options = 60;
    int jishu = 0;
    int cishu = 0;
    YuvImage image = null;
    //private String clientIP;

    public VideoThread(MainActivity main) {
        this.main = main;
    }

    @Override
    public void run() {
        if(Looper.myLooper() == null) {
            Looper.prepare();
        }
        TestText = (TextView)main.findViewById(R.id.test);
        ButtonBegin = (Button)main.findViewById(R.id.Begin);

        surfaceHolder = ((SurfaceView) main.findViewById(R.id.surfaceView)).getHolder();

        //faceRecognizer = new FaceRecognizer(null, main);
        //TestText.setText("SKDJ");
        surfaceHolder.setKeepScreenOn(true);
        isPreview = false;
        String an = "safj";
//        byte b []  = an.getBytes();
//        ByteArrayOutputStream outstream = new ByteArrayOutputStream(85);
//        outstream.write(5);
//        Thread th = new SendVideoThread(outstream, "sdf");
//        th.start();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                //initCamera();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initCamera();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @SuppressLint("NewApi")
    private void initCamera() {
        if (!isPreview) {
            //TestText.setText("ss");
            camera = android.hardware.Camera.open(1);
        }
        if (camera != null && !isPreview) {
            try {
                Camera.Parameters params = camera.getParameters();
                android.hardware.Camera.Parameters parameters = camera.getParameters();
                parameters.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
                parameters.setPreviewFpsRange(20, 30);
                parameters.setPictureFormat(ImageFormat.NV21);
                parameters.setPictureSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
                camera.setDisplayOrientation(90);
                camera.setPreviewDisplay(surfaceHolder);
                camera.setParameters(params);
                camera.setPreviewCallback(new StreamIt(main.clientIP));
                camera.startPreview();
                camera.autoFocus(null);

            } catch (Exception e) {
                e.printStackTrace();
            }
            isPreview = true;
        }
    }

    class StreamIt implements android.hardware.Camera.PreviewCallback {
        private String ipname = "ipname";

        public StreamIt(String ipname) {
            this.ipname = ipname;
        }

        @Override
        public void onPreviewFrame(byte[] data, android.hardware.Camera camera) {
            android.hardware.Camera.Size size = camera.getParameters().getPreviewSize();
            try {
                jishu++;
                cishu++;
                System.out.println("次数："+cishu);
                if(jishu >= 2) {
                    image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);

                    if (image != null) {
                        ByteArrayOutputStream outstream = new ByteArrayOutputStream();

                        image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, outstream);
                        byteForCompress = outstream.toByteArray();
                        BitmapForCompress = BitmapFactory.decodeByteArray(byteForCompress, 0, byteForCompress.length);
                        while (outstream.toByteArray().length / 1024 > 100) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                            outstream.reset();//重置baos即清空baos
                            BitmapForCompress.compress(Bitmap.CompressFormat.JPEG, options, outstream);//这里压缩options%，把压缩后的数据存放到baos中
                            if(options > 10)
                            options -= 10;//每次都减少10
                        }
                        options = 10;
                        Thread th = new SendVideoThread(outstream, ipname);
                        th.start();
                        outstream.flush();
                    }
                    jishu = 0;
                }
            } catch (Exception ex) {
                Log.e("Sys", "Error:" + ex.getMessage());
            }
        }
    }

    class SendVideoThread extends Thread {
        private byte byteBuffer[] = new byte[1024];
        private OutputStream outsocket;
        private ByteArrayOutputStream myoutputstream;
        private String ipname;

        public SendVideoThread(ByteArrayOutputStream myoutputstream, String ipname) {
            this.myoutputstream = myoutputstream;
            this.ipname = ipname;

        }

        public void run() {
            //TestText.setText("dsdsafa");
            Socket socket = null;
            try {
                InetAddress serverAddr = InetAddress.getByName("172.20.10.8" );
                Log.d("TCP", "C: Connecting...");

                // 应用Server的IP和端口建立Socket对象
                socket = new Socket(serverAddr, CAMERA_PORT);
                byteArray = myoutputstream.toByteArray();
                ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
                outsocket = socket.getOutputStream();
                int amount, num = 0;
                while ((amount = inputStream.read(byteBuffer)) != -1) {
                    outsocket.write(byteBuffer, 0, amount);
                }
                myoutputstream.flush();
                myoutputstream.close();
                socket.close();

            }
            catch(UnknownHostException e)
            {
                Log.e("unknown", "172.20.10.8 is unkown server!");
            }            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {

                }
            }
        }

    }

    private Bitmap compressImage(Bitmap image,int Compress,int length) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, Compress, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>length) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}


