package org.height185.rubiksdetector.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import org.height185.rubiksdetector.R;
import org.height185.rubiksdetector.adapters.ViewPagerAdapter;
import org.height185.rubiksdetector.fragment.CubeFragment;

public class ShowActivity extends AppCompatActivity {
    public int requestCode = -1;
    public int[] surfaceColor = new int[6*9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent intent = getIntent();
        if(intent != null){
            requestCode =intent.getIntExtra("requestCode", 0);
            surfaceColor = intent.getIntArrayExtra("surfaceColor");
        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(CubeFragment.newInstance());
        viewPager.setAdapter(adapter);
    }

}
