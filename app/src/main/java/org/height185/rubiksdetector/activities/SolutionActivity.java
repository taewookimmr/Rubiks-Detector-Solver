package org.height185.rubiksdetector.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.height185.rubiksdetector.R;

import java.util.ArrayList;

public class SolutionActivity extends AppCompatActivity {

    public ArrayList<String> solution = new ArrayList<>();
    TextView textView_number;
    TextView textView_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        Intent intent = getIntent();
        if(intent != null){
            solution  = intent.getStringArrayListExtra("solution");
        }

        textView_number = (TextView) findViewById(R.id.textView_number);
        textView_result = (TextView) findViewById(R.id.textView_result);

        textView_number.setText(solution.size() + " ");

        String result ="";
        for(int i = 0; i < solution.size(); i++) {
            result += solution.get(i) + " ";
            if((i+1)%5 == 0 && !((i+1)%10 == 0)) {result += "\t";}
            if((i+1)%10 == 0) {result += "\n";}
        }
        textView_result.setText(result);
    }


}
