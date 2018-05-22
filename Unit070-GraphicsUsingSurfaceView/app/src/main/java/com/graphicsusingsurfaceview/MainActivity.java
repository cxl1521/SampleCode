package com.graphicsusingsurfaceview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 用ShapeSurfaceView物件當成App畫面
        ShapeSurfaceView shapeView = new ShapeSurfaceView(this);
        setContentView(shapeView);
    }
}
