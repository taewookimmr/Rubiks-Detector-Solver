package org.height185.rubiksdetector.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.height185.rubiksdetector.activities.TestActivity;
import org.height185.rubiksdetector.renderer.CubeView2D;

public class TestFragment extends Fragment {

    public static final String NAME = "TEST";
    public int surfaceColor[] = new int[6*9];


    public TestFragment(){
    }

    public static TestFragment newInstance() {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    // 이게 먼저 실행됨. onCreatView보다 먼저.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(getActivity() != null && getActivity() instanceof TestActivity){
            TestActivity testActivity = (TestActivity) getActivity();
             for(int i = 0; i < testActivity.surfaceColor.length; i++){
                 this.surfaceColor[i] = testActivity.surfaceColor[i];
             }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 커스텀 뷰를 생성해서 반환한다.
        CubeView2D view = new CubeView2D(this.getContext(), surfaceColor);
        return view;
    }






}
