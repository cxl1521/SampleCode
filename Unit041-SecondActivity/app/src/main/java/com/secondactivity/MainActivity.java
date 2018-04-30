package com.secondactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.btnLaunchGame);
        btn.setOnClickListener(btnLaunchGameOnClick);
    }

    private View.OnClickListener btnLaunchGameOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent it = new Intent();
            it.setClass(MainActivity.this, GameActivity.class);
            startActivity(it);
        }
    };
}
