package com.rippleandselector;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1 = (Button) findViewById(R.id.btn1);

        if (Build.VERSION.SDK_INT >= 21)   // Android 5.0 以上。
            btn1.setBackgroundResource(R.drawable.btn_bg_ripple);
        else
            btn1.setBackgroundResource(R.drawable.btn_bg_selector);
    }
}
