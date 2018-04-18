package com.gameusingtoast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView mTxtResult;
    private ImageView mImgViewComPlay;
    private ImageButton mImgBtnScissors, mImgBtnStone, mImgBtnPaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImgViewComPlay = (ImageView)findViewById(R.id.imgViewComPlay);
//        mTxtResult = (TextView)findViewById(R.id.txtResult);
        mImgBtnScissors = (ImageButton)findViewById(R.id.imgBtnScissors);
        mImgBtnStone = (ImageButton)findViewById(R.id.imgBtnStone);
        mImgBtnPaper = (ImageButton)findViewById(R.id.imgBtnPaper);

        mImgBtnScissors.setOnClickListener(imgBtnScissorsOnClick);
        mImgBtnStone.setOnClickListener(imgBtnStoneOnClick);
        mImgBtnPaper.setOnClickListener(imgBtnPaperOnClick);
    }

    private View.OnClickListener imgBtnScissorsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            // 決定電腦出拳.
            int iComPlay = (int)(Math.random()*3 + 1);

            // 1 – 剪刀, 2 – 石頭, 3 – 布.
            if (iComPlay == 1) {
                mImgViewComPlay.setImageResource(R.drawable.scissors);
//                mTxtResult.setText(getString(R.string.result) +
//                        getString(R.string.player_draw));
                Toast.makeText(MainActivity.this, R.string.player_draw, Toast.LENGTH_LONG)
                        .show();
            }
            else if (iComPlay == 2) {
                mImgViewComPlay.setImageResource(R.drawable.stone);
//                mTxtResult.setText(getString(R.string.result) +
//                        getString(R.string.player_lose));
                Toast.makeText(MainActivity.this, R.string.player_lose, Toast.LENGTH_LONG)
                        .show();
            }
            else {
                mImgViewComPlay.setImageResource(R.drawable.paper);
//                mTxtResult.setText(getString(R.string.result) +
//                        getString(R.string.player_win));
                Toast.makeText(MainActivity.this, R.string.player_win, Toast.LENGTH_LONG)
                        .show();
            }
        }
    };

    private View.OnClickListener imgBtnStoneOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            // 決定電腦出拳.
            int iComPlay = (int)(Math.random()*3 + 1);

            // 1 – 剪刀, 2 – 石頭, 3 – 布.
            if (iComPlay == 1) {
                mImgViewComPlay.setImageResource(R.drawable.scissors);
//                mTxtResult.setText(getString(R.string.result) +
//                        getString(R.string.player_win));
                Toast.makeText(MainActivity.this, R.string.player_win, Toast.LENGTH_LONG)
                        .show();
            }
            else if (iComPlay == 2) {
                mImgViewComPlay.setImageResource(R.drawable.stone);
//                mTxtResult.setText(getString(R.string.result) +
//                        getString(R.string.player_draw));
                Toast.makeText(MainActivity.this, R.string.player_draw, Toast.LENGTH_LONG)
                        .show();
            }
            else {
                mImgViewComPlay.setImageResource(R.drawable.paper);
//                mTxtResult.setText(getString(R.string.result) +
//                        getString(R.string.player_lose));
                Toast.makeText(MainActivity.this, R.string.player_lose, Toast.LENGTH_LONG)
                        .show();
            }
        }
    };

    private View.OnClickListener imgBtnPaperOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            // 決定電腦出拳.
            int iComPlay = (int)(Math.random()*3 + 1);

            // 1 – 剪刀, 2 – 石頭, 3 – 布.
            if (iComPlay == 1) {
                mImgViewComPlay.setImageResource(R.drawable.scissors);
//                mTxtResult.setText(getString(R.string.result) +
//                        getString(R.string.player_lose));
                Toast.makeText(MainActivity.this, R.string.player_lose, Toast.LENGTH_LONG)
                        .show();
            }
            else if (iComPlay == 2) {
                mImgViewComPlay.setImageResource(R.drawable.stone);
//                mTxtResult.setText(getString(R.string.result) +
//                        getString(R.string.player_win));
                Toast.makeText(MainActivity.this, R.string.player_win, Toast.LENGTH_LONG)
                        .show();

            }
            else {
                mImgViewComPlay.setImageResource(R.drawable.paper);
//                mTxtResult.setText(getString(R.string.result) +
//                        getString(R.string.player_draw));
                Toast.makeText(MainActivity.this, R.string.player_draw, Toast.LENGTH_LONG)
                        .show();

            }
        }
    };
}
