package org.height185.rubiksdetector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class ManualActivity extends AppCompatActivity {

    ImageView imageView_manual;
    Button button_prev;
    Button button_next;
    int imageOrder = 0;
    private AdView adView;         // admob

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        imageView_manual =  (ImageView) findViewById(R.id.imageView_manual);
        button_next = (Button) findViewById(R.id.button_next);
        button_prev = (Button) findViewById(R.id.button_prev);
        button_prev.setClickable(false);

        button_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageOrder--;
                switch (imageOrder){
                    case 0:  imageView_manual.setImageResource(R.drawable.manual_front); break;
                    case 1: imageView_manual.setImageResource(R.drawable.manual_right); break;
                    case 2: imageView_manual.setImageResource(R.drawable.manual_back); break;
                    case 3: imageView_manual.setImageResource(R.drawable.manual_left); break;
                    case 4: imageView_manual.setImageResource(R.drawable.manual_up); break;
                }

                if(imageOrder == 0){
                    button_prev.setClickable(false);
                }else{
                    button_prev.setClickable(true);
                }

                button_next.setClickable(true);

            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageOrder++;
                switch (imageOrder){
                    case 1: imageView_manual.setImageResource(R.drawable.manual_right);  break;
                    case 2: imageView_manual.setImageResource(R.drawable.manual_back); break;
                    case 3: imageView_manual.setImageResource(R.drawable.manual_left); break;
                    case 4: imageView_manual.setImageResource(R.drawable.manual_up); break;
                    case 5: imageView_manual.setImageResource(R.drawable.manual_down);break;
                }

                if(imageOrder == 5){
                    button_next.setClickable(false);
                }else{
                    button_next.setClickable(true);
                }
                button_prev.setClickable(true);
            }
        });

        // Sample AdMob app ID : Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        adView = (AdView)findViewById(R.id.adView_manual);
        Bundle extras = new Bundle();
        extras.putString("max_ad_content_rating", "G"); // 앱이 3세 이상 사용가능이라면 광고레벨을 설정해줘야 한다
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();
        adView.loadAd(adRequest);


    }


}
