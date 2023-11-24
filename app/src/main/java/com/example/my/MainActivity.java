package com.example.my;
import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Date;
import java.util.Locale;
public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private Camera camera;
    private SurfaceView surfaceView;//公共接口，表面保持器
    private MediaRecorder mediaRecorder;//extends object用来录制音频，基于一个简单的控制机
    private boolean isMonitoring = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = findViewById(R.id.surfaceView);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        //向持有显示表面的人提供抽象接口。允许控制曲面大小和格式，编辑曲面中的像素，并监视对曲面的更改。此接口通常可通过Surfaceview类获得。
        surfaceHolder.addCallback(this);//回调，为此保持器添加回调接口。
        Button btnStartMonitoring = findViewById(R.id.btnStartMonitoring);
        Button btnStopMonitoring = findViewById(R.id.btnStopMonitoring);
        btnStartMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "开始监控并录制", Toast.LENGTH_SHORT).show();
                startMonitoring();
            }
        });//按钮事件，响应
        btnStopMonitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "关闭监控并保存", Toast.LENGTH_SHORT).show();
                stopMonitoring();
            }
        });//按钮事件，响应
        };
    private void startMonitoring() {
        if (checkCameraPermissions()) {
            camera = Camera.open();//照相机，Camera类用于设置图像捕获设置、开始/停止预览、捕捉图片和检索视频编码帧。
            Camera.Parameters parameters = camera.getParameters();
            //从open(int)获取Camera的实例。
            //使用getParameters()获取现有的(默认)设置。参数设置
            //可删去
            Camera.Size optimalSize = getOptimalPreviewSize(parameters.getSupportedPreviewSizes(), surfaceView.getWidth(), surfaceView.getHeight());
            //最理想的最佳的大小，相机尺寸大小
            //可删去
            parameters.setPreviewSize(optimalSize.width, optimalSize.height);
            camera.setDisplayOrientation(90);//设置显示方向，以度为单位设置预览显示的顺时针旋转。
            camera.setParameters(parameters);
            mediaRecorder = new MediaRecorder();//mediarecorder=new mediarecorder,媒体记录，初始化，声明并实例化
            camera.unlock();//lock重新锁定摄像头，以防止其他进程对其进行访问。unlock解锁相机以允许另一个进程访问它。
            mediaRecorder.setCamera(camera);//设置摄像头，设置用于录制的相机
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);//定义音频源，摄像机，麦克风音频源
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//定义视频源，视频源
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//媒体录像机，定义输出格式，MPEG_4媒体格式
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//媒体录像机，音频编码器，AMR窄带音频编码
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);//媒体录像机，视频编码器，定义视频编码，MPEG_4_服务商
            //mediaRecorder.setVideoEncodingBitRate(0);//比特率设置为8Mbps，废弃，闪退
            //mediaRecorder.setVideoSize(320,240);//分辨率，废弃，闪退
            mediaRecorder.setVideoFrameRate(100);//帧率设置为100fps，废弃，闪退
            // 设置输出文件路径
            String outputPath = getOutputMediaFilePath();//字符串路径，设置要生成的输出文件的路径。
            mediaRecorder.setOutputFile(outputPath);
            // 设置预览显示
            mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
            //设置预览显示大小。没曲面以显示录制的媒体视频的预览，在准备之前调用它，以确保设置了所需的预览显示。
            try {
                mediaRecorder.prepare();//准备记录器开始捕获和编码数据,必须在设置好所需的音视频源、编码器、文件格式等之后调用此方法。
                mediaRecorder.start();//开始将数据捕获并编码到使用setOutputFile指定的文件
                isMonitoring = true;
                setMonitoringState(true);
            } catch (IOException e) {
                e.printStackTrace();//将打印异常的堆栈跟踪信息，以便调试或错误处理
            }
            //异常处理，try尝试执行以下代码，并捕获可能抛出的异常，如果出现了IOException异常，即输入输出异常，程序跳转到catch
            //首先调用mediaRecorder.prepare()方法来准备mediarecorder对象，这个方法可能会抛出IOException异常
            //接下来调用mediarecorder.start()方法开始录制音频，将isMonitoring变量设置为true，表示正在监视录制状态，通过setMonitoringState(true)方法设置监视状态
        }
    }
    private void stopMonitoring() {
        if (isMonitoring) {
            mediaRecorder.stop();
            mediaRecorder.release();//释放与此MediaRecorder对象关联的资源。
            //camera.lock();
            camera.release();//释放相机资源
            isMonitoring = false;//停止监视录制状态
            setMonitoringState(false);//监视状态设置为false
        }
    }
    //私有方法，用于停止音频录制并释放相关资源
    //检查变量的值，如果为true，则表示正在监视，调用stop，停止录音，release释放资源
    //停止录制音频并释放资源，更新监视状态
    private void setMonitoringState(boolean isMonitoring) {
        Button btnStartMonitoring = findViewById(R.id.btnStartMonitoring);//获取开始按钮的引用，将其存储在变量中
        Button btnStopMonitoring = findViewById(R.id.btnStopMonitoring);
        btnStartMonitoring.setEnabled(!isMonitoring);
        btnStopMonitoring.setEnabled(isMonitoring);
    }
    //设置监视状态，根据监视状态更新两个按钮
    //ismonitoring为true，表示正在监视录制状态，那么开始监视按钮应该设置为不可用，停止监视按钮设置为可用
    private String getOutputMediaFilePath() {
        //File mediaDir = getFilesDir();
        File mediaDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);//提供对环境变量的访问，创建目录
        //调用getExternalFilesDir(Environment.DIRECTORY_MOVIES)方法来获取外部存储器上指定目录，将其存储在mediadir变量中，这个方法返回应该file对象，表示指定目录的路径
        if (!mediaDir.exists()) {//返回一个布尔值，指示是否可以在基础文件系统上找到该文件，如果mediadir不存在
            mediaDir.mkdirs();//创建一个目录
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        //简单日期格式，2000-01-05
        String fileName = "monitoring_" + timeStamp + ".mp4";
        //格式，monitoring+时间戳
        return mediaDir.getAbsolutePath() + File.separator + fileName;
    }
    // return mediaDir.getAbsolutePath() + "/monitoring.mp4";//绝对路径，返回此文件的绝对路径，由于文件名会发生冲突，会把以往视频顶替，因此舍弃，增加上面以时间戳命名
    //返回一个表示输出媒体文件路径的字符串
    //获取输出媒体文件的路径
    //相加权限
    private boolean checkCameraPermissions() {//用于检查应用是否已被授予相加和录音权限，并根据需要请求这些权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            //检查应用是否已被授予相加权限和录音权限，需要传入上下文对象this，要检查的权限和权限返回值，如果相加权限或录音权限中任何一个未被授予，即返回值不是PackageManager.PERMISSION_GRANTED，表示应用尚未获得所需权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 0);
            //请求相机和录音权限，传入上下文对象
            return false;//表示尚未获得权限
        }
        return true;//已获得
    }
    /*surfaceholder.callback接口的实现方法，处理surfaceview的生命周期和状态变化*/
    /*public void surfaceCreated(SurfaceHolder holder){
    }*/
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }
    /*当 SurfaceView 创建成功时调用此方法。在此方法中，执行一些初始化操作，打开摄像头，并将预览显示设置为当前的 SurfaceHolder*/
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //可删去
        if (camera != null) {
            // 停止预览
            camera.stopPreview();
            // 调整预览尺寸
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> supportedSizes = parameters.getSupportedPreviewSizes();
            Camera.Size optimalSize = getOptimalPreviewSize(supportedSizes, width, height);
            parameters.setPreviewSize(optimalSize.width, optimalSize.height);
            camera.setParameters(parameters);
            // 重新开始预览
            camera.startPreview();
        }
    }
    //获取最佳尺寸的预览画面大小函数，预览尺寸是指用于相机预览界面的图像尺寸
    //在给定支持的预览尺寸列表中，找到与指定的宽度和高度最接近的尺寸，并返回最佳的预览尺寸
    //可删去
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int width, int height) {
        final double ASPECT_TOLERANCE = 0.1;//定义一个容差常量用于定义宽高比的容差范围，如果某个预览尺寸的宽高比与目标宽高比的差异超过容差范围，将被忽略
        double targetRatio = (double) width / height;//计算目标宽高比targetRatio，通过将给定的宽度和高度相除得到。
        Camera.Size optimalSize = null;//初始化，表示最佳预览尺寸未找到
        double minDiff = Double.MAX_VALUE;//mindiff为一个很大的值，Double.MAX_VALUE用于记录与目标宽高比最接近的尺寸的差异值
        for (Camera.Size size : sizes) { //使用循环遍历支持的预览尺寸列表中每个尺寸
            double ratio = (double) size.width / size.height;//计算当前尺寸的宽高比ratio，通过将宽度和高度相除得到
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;//如果当前尺寸的宽高比与目标宽高比的差异超过容差范围，忽略尺寸，继续循环
            double diff = Math.abs(size.width - width) + Math.abs(size.height - height);//否则，计算当前尺寸与给定宽度和高度之间的差异值diff，将宽度差和高度差的绝对值相加得到
            if (diff < minDiff) {//如果diff小于mindiff，则将当前尺寸设为最佳预览尺寸
                optimalSize = size;
                minDiff = diff;
                //更新，mindiff=diff
            }
        }
        if (optimalSize == null) {//如果最佳尺寸没有找到，再次便利所有支持的尺寸，找到与给定宽度和高度差异最小的尺寸作为最佳预览尺寸
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                double diff = Math.abs(size.width - width) + Math.abs(size.height - height);
                if (diff < minDiff) {
                    optimalSize = size;
                    minDiff = diff;
                }
            }
        }
        return optimalSize;
    }
    /*public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }*/
    //从给定的支持预览尺寸列表中选择最佳预览尺寸，使其宽高比与目标宽高比接近，并且与给定的宽度和高度差异最小
    /*当 SurfaceView 的大小或格式发生变化时调用此方法。
    在此方法中，根据新的宽度和高度调整摄像头的预览尺寸或其他相关参数*/
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
    /*当 SurfaceView销毁时调用此方法。在此方法中，应该停止摄像头的预览并释放相关资源，关闭摄像头*/
}
