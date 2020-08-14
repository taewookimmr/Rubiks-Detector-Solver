package org.height185.rubiksdetector.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import org.height185.rubiksdetector.R;
import org.height185.rubiksdetector.activities.ShowActivity;
import org.height185.rubiksdetector.activities.SolutionActivity;
import org.height185.rubiksdetector.algorithm.Algorithm;
import org.height185.rubiksdetector.renderer.CubeRenderer;

/**
 * A simple {@link Fragment} subclass.
 */
public class CubeFragment extends Fragment implements AppCompatSeekBar.OnSeekBarChangeListener {

    private GLSurfaceView glView;
    private CubeRenderer cubesRenderer;

    public AppCompatSeekBar seek_xRotate;
    public AppCompatSeekBar seek_yRotate;
    public AppCompatSeekBar seek_zRotate;

    public Button[] buttons;
    public Button button_solution;

    public CubeFragment() {
        // Required empty public constructor
        cubesRenderer = new CubeRenderer(-50);
    }

    public static CubeFragment newInstance() {
        CubeFragment fragment = new CubeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return initLayout(inflater, container);
    }

    @Override
    public void onResume() {
        super.onResume();
        glView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        glView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        glView = null;
        cubesRenderer = null;
    }

    private View initLayout(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_cube, container, false);

        seek_xRotate = (AppCompatSeekBar) view.findViewById(R.id.seek_xRotate);
        seek_yRotate = (AppCompatSeekBar) view.findViewById(R.id.seek_yRotate);
        seek_zRotate = (AppCompatSeekBar) view.findViewById(R.id.seek_zRotate);
        seek_xRotate.setOnSeekBarChangeListener(this); // 여기서 이걸 걸어뒀구나.
        seek_yRotate.setOnSeekBarChangeListener(this);
        seek_zRotate.setOnSeekBarChangeListener(this);
        seek_xRotate.setTransitionName("x");
        seek_yRotate.setTransitionName("y");
        seek_zRotate.setTransitionName("z");

        seek_xRotate.setProgress((int) cubesRenderer.getxRotate() * 10);
        seek_yRotate.setProgress((int) cubesRenderer.getyRotate() * 10);
        seek_zRotate.setProgress((int) cubesRenderer.getzRotate() * 10);

        glView = (GLSurfaceView) view.findViewById(R.id.glview);
        assert glView != null;
        glView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        glView.setRenderer(cubesRenderer); // Use a custom renderer
        glView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        glView.setZOrderOnTop(true);

        buttons = new Button[9];
        buttons[0] = (Button) view.findViewById(R.id.btn_front);
        buttons[1] = (Button) view.findViewById(R.id.btn_back);
        buttons[2] = (Button) view.findViewById(R.id.btn_right);
        buttons[3] = (Button) view.findViewById(R.id.btn_left);
        buttons[4] = (Button) view.findViewById(R.id.btn_up);
        buttons[5] = (Button) view.findViewById(R.id.btn_down);
        buttons[6] = (Button) view.findViewById(R.id.btn_slide);
        buttons[7] = (Button) view.findViewById(R.id.btn_middle);
        buttons[8] = (Button) view.findViewById(R.id.btn_equator);
        buttonSetting();

        button_solution = (Button) view.findViewById(R.id.button_solution);
        button_solution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SolutionActivity.class);
                cubesRenderer.cube.solution.clear();

                Algorithm.setCoordinate(cubesRenderer.cube);
                Algorithm.cross(cubesRenderer.cube);
                Algorithm.f2l(cubesRenderer.cube);
                Algorithm.oll(cubesRenderer.cube);
                Algorithm.pll(cubesRenderer.cube);
                intent.putStringArrayListExtra("solution", cubesRenderer.cube.solution);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        switch (seekBar.getTransitionName()) {
            case "x":
                if (cubesRenderer != null) {
                    cubesRenderer.setxRotate(progress / 10f);
                }
                break;

            case "y":
                if (cubesRenderer != null) {
                    cubesRenderer.setyRotate(progress / 10f);
                }
                break;

            case "z":

                if (cubesRenderer != null) {
                    cubesRenderer.setzRotate(progress / 10f);
                }
                break;

        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    // ShowActivity에서 넘어오는 surfaceColor 배열을 받는다.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null && getActivity() instanceof ShowActivity) {
            ShowActivity showActivity = (ShowActivity) getActivity();
            switch (showActivity.requestCode) {
                case 0:
                    // MenuActivity의 show simulator 버튼으로부터 여기까지 온 경우
                    break;

                case 1:
                    // TestActivity의 show 3d 버튼으로부터 여기까지 온 경우
                    for (int i = 0; i < showActivity.surfaceColor.length; i++) {
                        cubesRenderer.cube.surfaceColor[i] = showActivity.surfaceColor[i];
                    }
                    cubesRenderer.cube.flag_opencv = true;
                    cubesRenderer.cube.transcriptionProcess();

                    // 여기서 판별할 수 있다. 판별 후, legal state가 아닌 경우에는
                    // showActvity를 닫고 다시 TestActivity로 넘어가게 한다.
                    if (!cubesRenderer.cube.isLegalState()) {
                        // legal state인지 판별하는 판별 메서드로 판별한다.
                        // 판별식이 false라면 TestActivity에 Toast가 뜨도록 한다.
                        showActivity.setResult(R.integer.ILLEGAL);
                        // 이렇게 끝내버린다.
                        showActivity.finish();

                    }

                    break;
            }


        }
    }

    public void buttonSetting() {

        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cubesRenderer.cube.rotatedLayer == -1) cubesRenderer.cube.rotatedLayer = 0;
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cubesRenderer.cube.rotatedLayer == -1) cubesRenderer.cube.rotatedLayer = 1;
            }
        });

        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cubesRenderer.cube.rotatedLayer == -1) cubesRenderer.cube.rotatedLayer = 2;
            }
        });

        buttons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cubesRenderer.cube.rotatedLayer == -1) cubesRenderer.cube.rotatedLayer = 3;
            }
        });

        buttons[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cubesRenderer.cube.rotatedLayer == -1) cubesRenderer.cube.rotatedLayer = 4;
            }
        });

        buttons[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cubesRenderer.cube.rotatedLayer == -1) cubesRenderer.cube.rotatedLayer = 5;
            }
        });

        buttons[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cubesRenderer.cube.rotatedLayer == -1) cubesRenderer.cube.rotatedLayer = 6;
            }
        });

        buttons[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cubesRenderer.cube.rotatedLayer == -1) cubesRenderer.cube.rotatedLayer = 7;
            }
        });

        buttons[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cubesRenderer.cube.rotatedLayer == -1) cubesRenderer.cube.rotatedLayer = 8;
            }
        });

    }
}
