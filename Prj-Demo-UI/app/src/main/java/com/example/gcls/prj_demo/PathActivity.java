package com.example.gcls.prj_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PathActivity extends AppCompatActivity {
    private TextView tv;
    private Button btnStop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        tv = (TextView) findViewById(R.id.textView2);
        btnStop = (Button) findViewById(R.id.button);

        btnStop.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                MainActivity.blueTooth.sendInformation("5");
            }
        });
    }
}
