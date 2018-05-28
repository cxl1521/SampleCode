package com.recordvideo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION_CAMERA = 100;
    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90,
                            SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
    private static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray(),
                                        INVERSE_ORIENTATIONS = new SparseIntArray();

    private boolean mbFaceDetAvailable;
    private int miMaxFaceCount = 0;
    private int miFaceDetMode;

    private TextureView mTextureView = null;

    private Size mPreviewSize = null;
    private CameraDevice mCameraDevice = null;
    private CaptureRequest.Builder mPreviewBuilder = null;
    private CameraCaptureSession mCameraPreviewCaptureSession = null,
                                mCameraTakePicCaptureSession = null,
                                mCameraRecordVideoCaptureSession = null;

    private MediaRecorder mMediaRecorder = null;
    private boolean mbRecordingVideo = false;
    private Button mBtnRecordVideo;

    // 當UI的TextureView建立時，會執行onSurfaceTextureAvailable()
    private TextureView.SurfaceTextureListener mSurfaceTextureListener =
            new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture,
                                                      int width, int height) {
                    // 檢查是否取得使用camera的權限
                    if (askForPermissions())
                        openCamera();   // 開啟camera.
                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture,
                                                        int width, int height) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextureView = (TextureView) findViewById(R.id.textureView);

        Button btnTakePicture = (Button) findViewById(R.id.btnTakePicture);
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForPermissions();
                takePicture();
            }
        });

        // 設定手機轉動角度和影片方向的對照表
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_0, 90);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_90, 0);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_180, 270);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_270, 180);

        INVERSE_ORIENTATIONS.append(Surface.ROTATION_0, 270);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_90, 180);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_180, 90);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_270, 0);

        mBtnRecordVideo = (Button) findViewById(R.id.btnRecordingVideo);
        mBtnRecordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mbRecordingVideo) {
                    stopRecordingVideo();
                    mBtnRecordVideo.setText("錄影");
                } else {
                    startRecordingVideo();
                    mBtnRecordVideo.setText("停止錄影");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mCameraDevice != null)
        {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    openCamera();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private  boolean askForPermissions() {
        // App需要用的功能權限清單
        String[] permissions= new String[] {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO};

        // 檢查是否已經取得權限
        final List<String> listPermissionsNeeded = new ArrayList<>();
        boolean bShowPermissionRationale = false;

        for (String p: permissions) {
            int result = ContextCompat.checkSelfPermission(MainActivity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);

                // 檢查是否需要顯示說明
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        MainActivity.this, p))
                    bShowPermissionRationale = true;
            }
        }

        // 向使用者徵詢還沒有許可的權限
        if (!listPermissionsNeeded.isEmpty()) {
            if (bShowPermissionRationale) {
                AlertDialog.Builder altDlgBuilder =
                        new AlertDialog.Builder(MainActivity.this);
                altDlgBuilder.setTitle("提示");
                altDlgBuilder.setMessage("App需要您的許可才能執行。");
                altDlgBuilder.setIcon(android.R.drawable.ic_dialog_info);
                altDlgBuilder.setCancelable(false);
                altDlgBuilder.setPositiveButton("確定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                                        REQUEST_PERMISSION_CAMERA);
                            }
                        });
                altDlgBuilder.show();
            } else
                ActivityCompat.requestPermissions(MainActivity.this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                        REQUEST_PERMISSION_CAMERA);

            return false;
        }

        return true;
    }

    private void openCamera() {
        // 取得 CameraManager
        CameraManager camMgr = (CameraManager) getSystemService(CAMERA_SERVICE);

        try{
            // 取得相機背後的 camera
            String cameraId = camMgr.getCameraIdList()[0];
            CameraCharacteristics camChar = camMgr.getCameraCharacteristics(cameraId);

            // 取得解析度
            StreamConfigurationMap map = camChar.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            mPreviewSize = map.getOutputSizes(SurfaceTexture.class)[0];

            // 檢查是否有人臉偵測功能
            int[] iFaceDetModes = camChar.get(
                    CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES);
            if (iFaceDetModes == null) {
                mbFaceDetAvailable = false;
                Toast.makeText(MainActivity.this, "不支援人臉偵測", Toast.LENGTH_LONG)
                        .show();
            } else {
                mbFaceDetAvailable = false;
                for (int mode : iFaceDetModes) {
                    if (mode == CameraMetadata.STATISTICS_FACE_DETECT_MODE_SIMPLE) {
                        mbFaceDetAvailable = true;
                        miFaceDetMode = CameraMetadata.STATISTICS_FACE_DETECT_MODE_SIMPLE;
                        break;   // Find the desired mode, so stop searching.
                    } else if (mode == CameraMetadata.STATISTICS_FACE_DETECT_MODE_FULL) {
                        // This is a candidate mode, keep searching.
                        mbFaceDetAvailable = true;
                        miFaceDetMode = CameraMetadata.STATISTICS_FACE_DETECT_MODE_FULL;
                    }
                }
            }

            if (mbFaceDetAvailable) {
                miMaxFaceCount = camChar.get(
                        CameraCharacteristics.STATISTICS_INFO_MAX_FACE_COUNT);

                Toast.makeText(MainActivity.this, "人臉偵測功能: " + String.valueOf(miFaceDetMode) +
                        "\n人臉樹最大值: " + String.valueOf(miMaxFaceCount), Toast.LENGTH_LONG)
                        .show();
            }

            // 啟動 camera
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                camMgr.openCamera(cameraId, mCameraStateCallback, null);
        }
        catch(CameraAccessException e)
        {
            e.printStackTrace();
        }
    }

    private CameraDevice.StateCallback mCameraStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            startPreview();
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            Toast.makeText(MainActivity.this, "無法使用camera", Toast.LENGTH_LONG)
                    .show();
        }

        @Override
        public void onError(CameraDevice cameraDevice, int error) {
            Toast.makeText(MainActivity.this, "Camera開啟錯誤", Toast.LENGTH_LONG)
                    .show();
        }
    };

    // Camera的CaptureSession狀態改變時執行
    private CameraCaptureSession.StateCallback mCameraCaptureSessionCallback =
            new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    closeAllCameraCaptureSession();

                    // 記下這個capture session，使用完畢要刪除
                    mCameraPreviewCaptureSession = cameraCaptureSession;

                    mPreviewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
                    mPreviewBuilder.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, miFaceDetMode);

                    HandlerThread backgroundThread = new HandlerThread("CameraPreview");
                    backgroundThread.start();
                    Handler backgroundHandler = new Handler(backgroundThread.getLooper());

                    try {
                        mCameraPreviewCaptureSession.setRepeatingRequest(mPreviewBuilder.build(), null, backgroundHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(MainActivity.this, "Camera預覽錯誤", Toast.LENGTH_LONG)
                            .show();
                }
            };

    private void startPreview() {
        // 從UI元件的TextureView取得SurfaceTexture
        // 依照 camera的解析度，設定TextureView的解析度
        SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

        // 依照TextureView的解析度建立一個 surface 給camera使用
        Surface surface = new Surface(surfaceTexture);

        // 設定camera的CaptureRequest和CaptureSession
        try {
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        } catch (CameraAccessException e){
            e.printStackTrace();
        }

        mPreviewBuilder.addTarget(surface);

        try {
            mCameraDevice.createCaptureSession(Arrays.asList(surface), mCameraCaptureSessionCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // 建立新的Camera Capture Session之前
    // 呼叫這個方法，清除舊的Camera Capture Session
    private void closeAllCameraCaptureSession() {
        if (mCameraPreviewCaptureSession != null) {
            mCameraPreviewCaptureSession.close();
            mCameraPreviewCaptureSession = null;
        }

        if (mCameraTakePicCaptureSession != null) {
            mCameraTakePicCaptureSession.close();
            mCameraTakePicCaptureSession = null;
        }

        if (mCameraRecordVideoCaptureSession != null) {
            mCameraRecordVideoCaptureSession.close();
            mCameraRecordVideoCaptureSession = null;
        }
    }

    private void takePicture() {
        if(mCameraDevice == null) {
            Toast.makeText(MainActivity.this, "Camera錯誤", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // 準備影像檔
        final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath(), "photo.jpg");

        // 準備OnImageAvailableListener
        ImageReader.OnImageAvailableListener imgReaderOnImageAvailable =
                new ImageReader.OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader imageReader) {
                        // 把影像資料寫入檔案
                        Image image = null;
                        try {
                            image = imageReader.acquireLatestImage();
                            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                            byte[] bytes = new byte[buffer.capacity()];
                            buffer.get(bytes);

                            OutputStream output = null;
                            try {
                                output = new FileOutputStream(file);
                                output.write(bytes);
                            } finally {
                                if (null != output)
                                    output.close();
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (image != null)
                                image.close();
                        }
                    }
                };

        // 取得 CameraManager
        CameraManager camMgr = (CameraManager) getSystemService(CAMERA_SERVICE);

        try {
            CameraCharacteristics camChar = camMgr.getCameraCharacteristics(mCameraDevice.getId());

            // 設定拍照的解析度
            Size[] jpegSizes = null;
            if (camChar != null)
                jpegSizes = camChar.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        .getOutputSizes(ImageFormat.JPEG);

            int picWidth = 640;
            int picHeight = 480;
            if (jpegSizes != null && jpegSizes.length > 0) {
                picWidth = jpegSizes[0].getWidth();
                picHeight = jpegSizes[0].getHeight();
            }

            // 設定照片要輸出給誰
            // 1. 儲存為影像檔； 2. 輸出給UI的TextureView顯示
            ImageReader imgReader = ImageReader.newInstance(picWidth, picHeight, ImageFormat.JPEG, 1);

            // 準備拍照用的thread
            HandlerThread thread = new HandlerThread("CameraTakePicture");
            thread.start();
            final Handler backgroudHandler = new Handler(thread.getLooper());

            // 把OnImageAvailableListener和thread設定給ImageReader
            imgReader.setOnImageAvailableListener(imgReaderOnImageAvailable, backgroudHandler);

            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(imgReader.getSurface());
            outputSurfaces.add(new Surface(mTextureView.getSurfaceTexture()));

            final CaptureRequest.Builder captureBuilder =
                    mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(imgReader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            // 決定照片的方向（直的或橫的）
            SparseIntArray PICTURE_ORIENTATIONS = new SparseIntArray();
            PICTURE_ORIENTATIONS.append(Surface.ROTATION_0, 90);
            PICTURE_ORIENTATIONS.append(Surface.ROTATION_90, 0);
            PICTURE_ORIENTATIONS.append(Surface.ROTATION_180, 270);
            PICTURE_ORIENTATIONS.append(Surface.ROTATION_270, 180);

            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, PICTURE_ORIENTATIONS.get(rotation));

            // 準備拍照的callback
            final CameraCaptureSession.CaptureCallback camCaptureCallback =
                    new CameraCaptureSession.CaptureCallback() {
                        @Override
                        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                            super.onCaptureCompleted(session, request, result);

                            Integer mode = result.get(CaptureResult.STATISTICS_FACE_DETECT_MODE);
                            Face[] faces = result.get(CaptureResult.STATISTICS_FACES);
                            if(faces != null && mode != null)
                                Toast.makeText(MainActivity.this, "人臉: " + faces.length, Toast.LENGTH_SHORT).show();

                            // 播放快門音效檔
                            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sound_camera_shutter);
                            MediaPlayer mp = MediaPlayer.create(MainActivity.this, uri);
                            mp.start();

                            Toast.makeText(MainActivity.this, "拍照完成\n影像檔: " + file, Toast.LENGTH_SHORT).show();
                            startPreview();
                        }

                        @Override
                        public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {
                        }
                    };

            // 最後一步就是建立Capture Session
            // 然後啟動拍照
            mCameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                            try {
                                closeAllCameraCaptureSession();

                                // 記下這個capture session，使用完畢要刪除
                                mCameraTakePicCaptureSession = cameraCaptureSession;

                                cameraCaptureSession.capture(captureBuilder.build(), camCaptureCallback, backgroudHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
                            Toast.makeText(MainActivity.this, "拍照起始錯誤", Toast.LENGTH_LONG)
                                    .show();
                        }
                    },
                    backgroudHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void startRecordingVideo() {
        if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
            return;
        }

        try {
            setUpMediaRecorder();

            SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
            assert surfaceTexture != null;
            surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            List<Surface> surfaces = new ArrayList<>();

            // 設定預覽輸出的surface
            Surface previewSurface = new Surface(surfaceTexture);
            surfaces.add(previewSurface);
            mPreviewBuilder.addTarget(previewSurface);

            // 設定錄影的surface，也就是MediaRecorder的surface
            Surface recorderSurface = mMediaRecorder.getSurface();
            surfaces.add(recorderSurface);
            mPreviewBuilder.addTarget(recorderSurface);

            // 準備錄影用的thread
            HandlerThread thread = new HandlerThread("CameraRecordVideo");
            thread.start();
            final Handler backgroudHandler = new Handler(thread.getLooper());

            // 建立錄影的capture session
            mCameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    closeAllCameraCaptureSession();

                    mCameraRecordVideoCaptureSession = cameraCaptureSession;

                    mPreviewBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

                    try {
                        mCameraRecordVideoCaptureSession.setRepeatingRequest(mPreviewBuilder.build(), null, backgroudHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }

                    // 開始錄影
                    mbRecordingVideo = true;
                    mMediaRecorder.start();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(MainActivity.this, "錄影起始錯誤", Toast.LENGTH_LONG)
                            .show();
                }
            }, backgroudHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecordingVideo() {
        mbRecordingVideo = false;
        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;
        startPreview();
    }

    private void setUpMediaRecorder() throws IOException {
        // 影片檔路徑和名稱
        String sFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getPath() + "/video.mp4";

        mMediaRecorder = new MediaRecorder();

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setOutputFile(sFile);
        mMediaRecorder.setVideoEncodingBitRate(10000000);
        mMediaRecorder.setVideoFrameRate(30);

        CameraManager camMgr = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            // 決定影片的解析度
            CameraCharacteristics camChar = camMgr.getCameraCharacteristics(mCameraDevice.getId());
            StreamConfigurationMap map = camChar
                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            Size videoSize = map.getOutputSizes(MediaRecorder.class)[0];
            mMediaRecorder.setVideoSize(videoSize.getWidth(), videoSize.getHeight());
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            int rotation = getWindowManager().getDefaultDisplay().getRotation();

            int sensorOrientation = camChar.get(CameraCharacteristics.SENSOR_ORIENTATION);
            switch (sensorOrientation) {
                case SENSOR_ORIENTATION_DEFAULT_DEGREES:
                    mMediaRecorder.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation));
                    break;
                case SENSOR_ORIENTATION_INVERSE_DEGREES:
                    mMediaRecorder.setOrientationHint(INVERSE_ORIENTATIONS.get(rotation));
                    break;
            }
            mMediaRecorder.prepare();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
