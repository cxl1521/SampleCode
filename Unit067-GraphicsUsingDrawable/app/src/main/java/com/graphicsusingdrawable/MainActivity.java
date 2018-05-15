package com.graphicsusingdrawable;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView mImgView1,
                    mImgView2,
                    mImgView3;
    private Button mBtnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImgView1 = (ImageView)findViewById(R.id.imgView1);
        mImgView2 = (ImageView)findViewById(R.id.imgView2);
        mImgView3 = (ImageView)findViewById(R.id.imgView3);

        mBtnStart = (Button)findViewById(R.id.btnStart);
        mBtnStart.setOnClickListener(btnStartOnClick);
    }

    private View.OnClickListener btnStartOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Drawable drawImg = ContextCompat.getDrawable(
                    MainActivity.this, R.drawable.img01);
            mImgView1.setImageDrawable(drawImg);

            TransitionDrawable drawTran =
                    (TransitionDrawable) ContextCompat.getDrawable(
                            MainActivity.this, R.drawable.trans_drawable);
            mImgView2.setImageDrawable(drawTran);
            drawTran.startTransition(5000);

            GradientDrawable gradShape = new GradientDrawable();
            gradShape.setShape(GradientDrawable.OVAL);
            gradShape.setColor(0xffff0000);
            mImgView3.setImageDrawable(gradShape);
        }
    };
}
