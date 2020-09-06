package org.height185.rubiksdetector.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.height185.rubiksdetector.R;

public class TestActivity extends AppCompatActivity {

    public int[] surfaceColor = new int[6 * 9]; // cube's 2d state data
    public Button button_show3d;   // button for going to ShowActivity
    private AdView adView;         // admob

    public Button[] buttons_state = new Button[6 * 9]; // buttons for showing 2d state data as colors
    public int[] bid = {
            // array of ids of buttons_state
            R.id.b0, R.id.b1, R.id.b2, R.id.b3, R.id.b4, R.id.b5, R.id.b6, R.id.b7, R.id.b8,
            R.id.b9, R.id.b10, R.id.b11, R.id.b12, R.id.b13, R.id.b14, R.id.b15, R.id.b16, R.id.b17,
            R.id.b18, R.id.b19, R.id.b20, R.id.b21, R.id.b22, R.id.b23, R.id.b24, R.id.b25, R.id.b26,
            R.id.b27, R.id.b28, R.id.b29, R.id.b30, R.id.b31, R.id.b32, R.id.b33, R.id.b34, R.id.b35,
            R.id.b36, R.id.b37, R.id.b38, R.id.b39, R.id.b40, R.id.b41, R.id.b42, R.id.b43, R.id.b44,
            R.id.b45, R.id.b46, R.id.b47, R.id.b48, R.id.b49, R.id.b50, R.id.b51, R.id.b52, R.id.b53
    };

    public Button[] buttons_update = new Button[6]; // buttons for updating the color of a buttons_state
    public int[] buid = {
            // array of ids of buttons_update
            R.id.bu0, R.id.bu1, R.id.bu2, R.id.bu3, R.id.bu4, R.id.bu5
    };

    public LinearLayout linlay_update; // linearLayout which contain buttons_update

    public int button_index = -1; // to save which buttons_state is clicked


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Sample AdMob app ID : Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        adView = (AdView) findViewById(R.id.adView_test);
        Bundle extras = new Bundle();
        extras.putString("max_ad_content_rating", "G"); // 앱이 3세 이상 사용가능이라면 광고레벨을 설정해줘야 한다
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();
        adView.loadAd(adRequest);

        Intent intent = getIntent();
        if (intent != null) {
            surfaceColor = intent.getIntArrayExtra("surfaceColor");
        }

        button_show3d = (Button) findViewById(R.id.button_show3d);
        button_show3d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
                intent.putExtra("surfaceColor", surfaceColor);
                intent.putExtra("requestCode", 1);
                startActivityForResult(intent, 1);
                // finish();
            }
        });


        for (int i = 0; i < 6 * 9; i++) {
            buttons_state[i] = (Button) findViewById(bid[i]);
            switch (surfaceColor[i]) {
                case 0:
                    buttons_state[i].setBackground(getResources().getDrawable(R.color.BLUE));
                    break;
                case 1:
                    buttons_state[i].setBackground(getResources().getDrawable(R.color.GREEN));
                    break;
                case 2:
                    buttons_state[i].setBackground(getResources().getDrawable(R.color.RED));
                    break;
                case 3:
                    buttons_state[i].setBackground(getResources().getDrawable(R.color.ORANGE));
                    break;
                case 4:
                    buttons_state[i].setBackground(getResources().getDrawable(R.color.YELLOW));
                    break;
                case 5:
                    buttons_state[i].setBackground(getResources().getDrawable(R.color.WHITE));
                    break;
                default:
                    buttons_state[i].setBackground(getResources().getDrawable(R.color.BLACK));
                    break;
            }
            buttons_state[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    linlay_update.setVisibility(View.VISIBLE); // buttons_updata are now visible
                    int id = view.getId();
                    for (int i = 0; i < 6 * 9; i++) {
                        if (id == bid[i]) {
                            button_index = i; // save which buttons_state is clicked
                            break;
                        }
                    }
                    for (int i = 0; i < 6 * 9; i++) {
                        buttons_state[i].setEnabled(false);
                        // until the color of clicked buttons_state is changed,
                        // all the buttons_state are now unabled.
                    }
                }
            });
        }

        linlay_update = (LinearLayout) findViewById(R.id.linlay_update);

        for (int i = 0; i < 6; i++) {
            buttons_update[i] = (Button) findViewById(buid[i]);
            buttons_update[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    linlay_update.setVisibility(View.INVISIBLE);
                    int id = view.getId(); // which buttons_update is clicked

                    if (id == buid[0]) {  // change the color of the buttons_state to blue
                        buttons_state[button_index].setBackground(getResources().getDrawable(R.color.BLUE));
                        surfaceColor[button_index] = 0;
                    } else if (id == buid[1]) { // green
                        buttons_state[button_index].setBackground(getResources().getDrawable(R.color.GREEN));
                        surfaceColor[button_index] = 1;
                    } else if (id == buid[2]) { // red
                        buttons_state[button_index].setBackground(getResources().getDrawable(R.color.RED));
                        surfaceColor[button_index] = 2;
                    } else if (id == buid[3]) { // orange
                        buttons_state[button_index].setBackground(getResources().getDrawable(R.color.ORANGE));
                        surfaceColor[button_index] = 3;
                    } else if (id == buid[4]) { // yellow
                        buttons_state[button_index].setBackground(getResources().getDrawable(R.color.YELLOW));
                        surfaceColor[button_index] = 4;
                    } else if (id == buid[5]) { // white
                        buttons_state[button_index].setBackground(getResources().getDrawable(R.color.WHITE));
                        surfaceColor[button_index] = 5;
                    }

                    for (int i = 0; i < 6 * 9; i++) {
                        buttons_state[i].setEnabled(true);
                    }
                    // now the color of clicked buttons_state have just changed,
                    // all the buttons_state are now abled.

                    button_index = -1;
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == R.integer.ILLEGAL) {
            Toast.makeText(this, "ILLEGAL STATE\nDETECT AGAIN", Toast.LENGTH_LONG).show();

        }
    }
}
