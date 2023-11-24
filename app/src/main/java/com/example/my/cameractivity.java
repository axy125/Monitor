package com.example.my;
import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class cameractivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private boolean previewing = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camermain);
        Button btnStart = findViewById(R.id.btnStart);
        Button btnCapture = findViewById(R.id.btnCapture);
        // 检查相机权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }
        // 检查文件存储权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        surfaceView = findViewById(R.id.cameraPreview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera != null) {//检查相机是否存在，即是否成功打开相机，如果相机存在
                    camera.startPreview();//调用相机的startpreview方法开始预览
                    previewing = true;//预览状态变量，相机正在预览状态
 }}});
        btnCapture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (camera != null && previewing) {//相机是否存在且在预览状态，即相机是否成功打开并处于预览状态
                    camera.takePicture(null, null, pictureCallback);//调用相机takepicture方法拍照
                    //穿入三个参数，照片的快门声音回调，照片的原始数据回调，照片的回调接口pictureCallback
                    //pictureCallback是一个camera.picturecallback对象，在拍照完成后回调其onpicturetaken方法，可以在该方法中处理拍摄的照片数据
                }
            }
        });
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
       try {
            camera = Camera.open();
            camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        /*if (previewing) {camera.stopPreview();previewing = false;}
        try {camera.setPreviewDisplay(holder);camera.startPreview();previewing = true;} catch (IOException e) { e.printStackTrace();}*/
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
       /* if (camera != null) {camera.stopPreview();camera.release();camera = null;previewing = false;}*/
    }
   Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
       @Override
       public void onPictureTaken(byte[] data, Camera camera) {
           File pictureFileDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "CameraApp");
           if (!pictureFileDir.exists()) {
               if (!pictureFileDir.mkdirs()) {
                   return;
               }
           }
           //增加时间戳
           String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
           String photoFileName = "photo_" + timeStamp + ".jpg";
           File photoFile = new File(pictureFileDir, photoFileName);
           try {
               FileOutputStream fos = new FileOutputStream(photoFile);
               fos.write(data);
               fos.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
           camera.stopPreview();
           previewing = false;
       }
   };
    public void startCameraPreview(View view) {
        if (camera != null && !previewing) {
            camera.startPreview();
            previewing = true;
        }
    }
    public void captureImage(View view) {
        if (camera != null && previewing) {
            camera.takePicture(null, null, pictureCallback);
        }
    }
}
