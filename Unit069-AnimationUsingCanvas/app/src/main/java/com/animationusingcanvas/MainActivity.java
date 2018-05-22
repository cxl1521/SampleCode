package com.animationusingcanvas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 用ShapeView物件當成App畫面
        ShapeView shapeView = new ShapeView(this);
        setContentView(shapeView);
    }
}
