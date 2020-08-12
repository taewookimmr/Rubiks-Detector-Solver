
package org.height185.rubiksdetector.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.height185.rubiksdetector.R;
import org.height185.rubiksdetector.rubiksDetector.SurfaceDetector;

public class DetectActivity extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "opencv";
    private CameraBridgeViewBase mOpenCvCameraView;
    private Mat matInput;
    private Mat matResult;

    public SurfaceDetector[] surfaceDetector; // surfaceDetector 6개가 필요하다.
    public int sdIndex = 0;     // 0~5;
    public int[] surfaceColor; // 6*9;
    public int[] searchOrder = {0, 2, 1, 3, 4, 5 }; // 탐색 순서

    public TextView textView_targetColor;
    public Button button_save ;
    public AppCompatSeekBar seekBar_ro;
    public AppCompatSeekBar seekBar_gy;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mOpenCvCameraView = (CameraBridgeViewBase)findViewById(R.id.activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setCameraIndex(0); // front-camera(1),  back-camera(0)
        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

        // surfaceDetector 생성자
        surfaceDetector = new SurfaceDetector[6];
        for(int i = 0; i < 6; i++){surfaceDetector[i] = new SurfaceDetector();}

        // 검출 결과가 담길 배열, 제일 중요한 부분
        surfaceColor = new int[6*9];
        for(int i = 0; i < surfaceColor.length; i++){surfaceColor[i] = -1;}

        textView_targetColor = (TextView) findViewById(R.id.textView_targetColor);

        button_save  = (Button) findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 큐브 surfaceDetector가 관심 surface의 색을 안정적으로 검출하고 있을 때
                // 해당 색 데이터를 저장하기 위한 버튼의 기능을 구현하는 부분입니다.
                surfaceDetector[sdIndex].isSaveButtonClicked = true;

//                이 5줄의 코드는 색 데이터를 얻기 위해 임시적으로 사용했던 코드입니다.
//                String data = "";
//                data = surfaceDetector[sdIndex].bgr_temp[0] + ", " +
//                      surfaceDetector[sdIndex].bgr_temp[1] + ", " +
//                        surfaceDetector[sdIndex].bgr_temp[2];
//                saveFile(data);
            }
        });

        // seekBar
        seekBar_ro = (AppCompatSeekBar) findViewById(R.id.seek_ro);
        seekBar_gy = (AppCompatSeekBar) findViewById(R.id.seek_gy);
        seekBar_ro.setProgress(50);
        seekBar_gy.setProgress(50);

        seekBar_ro.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                surfaceDetector[sdIndex].separate_ro = 0.035f + (float)(progress-50) / 1000.0f* 3.0f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar_gy.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                surfaceDetector[sdIndex].separate_gy = 0.0f + (float)(progress-50) / 1000.0f * 3.0f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "onResume :: Internal OpenCV library not found.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "onResume :: OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        matInput = inputFrame.rgba();

        if ( matResult != null ) {
            matResult.release();
        }else{
            matResult = new Mat(matInput.rows(), matInput.cols(), matInput.type());
        }

        matResult = matInput.clone();

        // 이 둘의 순서가 중요하다. drwaLattice 먼저 실행해야 한다.
        surfaceDetector[sdIndex].drawLattice(matResult);
        surfaceDetector[sdIndex].detectColor(matInput);

        if(surfaceDetector[sdIndex].isDetectedAll()){
            for (int j = 0; j < 9; j++) {
                surfaceColor[searchOrder[sdIndex] * 9 + j] = surfaceDetector[sdIndex].surfaceColor[j];
                // you cannot show a toast on Non UI thread
                // consider using fragment in order to check whether the data was saved correctly
                // Log.d("testingtesting", "[" + j + "]번 요소 :  ["+ surfaceDetector[sdIndex].surfaceColor[j] + "]");
            }
            sdIndex++;
        }

        if(sdIndex > 5){
            // 6 surfaces all detected and the corresponding data saved,
            // open? new activity for showing 3d representation of acquired array data of rubiks cube
            // Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
            Intent intent = new Intent(getApplicationContext(), TestActivity.class);
            // transfer the surfaceColor[6*9] to ShowActivity
            intent.putExtra("requestCode", 1);  // 여기서는 1을 날리고, MenuActivity의 show simulator에서는 0을 날린다.
            intent.putExtra("surfaceColor", surfaceColor);

            startActivity(intent);
            finish();
        }else{
            // 어떤 surface를 dectec해야 하는지 알려주는 문구를 출력해주는 부분
            final Handler handler = new Handler(Looper.getMainLooper()) {
                public void handleMessage(Message msg) {
                    String[] detectOrder = {"blue", "red", "green", "orange", "yellow", "white"};
                    String[] detectOrder2 = {"yellow", "yellow", "yellow", "yellow", "green", "blue"};
                    textView_targetColor.setText("Detect " + detectOrder[sdIndex] + " centered face with "
                            + detectOrder2[sdIndex] +" center looking up");
                }
            };

            new Thread() {
                public void run() {
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                }
            }.start();
        }

        return matResult;
    }






}