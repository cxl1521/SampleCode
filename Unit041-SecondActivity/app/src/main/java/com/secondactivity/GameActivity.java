package com.secondactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    private TextView mTxtResult;
    private ImageView mImgViewComPlay;
    private ImageButton mImgBtnScissors, mImgBtnStone, mImgBtnPaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mImgViewComPlay = (ImageView)findViewById(R.id.imgViewComPlay);
        mTxtResult = (TextView)findViewById(R.id.txtResult);
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
                mTxtResult.setText(getString(R.string.result) +
                        getString(R.string.player_draw));
            }
            else if (iComPlay == 2) {
                mImgViewComPlay.setImageResource(R.drawable.stone);
                mTxtResult.setText(getString(R.string.result) +
                        getString(R.string.player_lose));
            }
            else {
                mImgViewComPlay.setImageResource(R.drawable.paper);
                mTxtResult.setText(getString(R.string.result) +
                        getString(R.string.player_win));
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
                mTxtResult.setText(getString(R.string.result) +
                        getString(R.string.player_win));
            }
            else if (iComPlay == 2) {
                mImgViewComPlay.setImageResource(R.drawable.stone);
                mTxtResult.setText(getString(R.string.result) +
                        getString(R.string.player_draw));
            }
            else {
                mImgViewComPlay.setImageResource(R.drawable.paper);
                mTxtResult.setText(getString(R.string.result) +
                        getString(R.string.player_lose));
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
                mTxtResult.setText(getString(R.string.result) +
                        getString(R.string.player_lose));
            }
            else if (iComPlay == 2) {
                mImgViewComPlay.setImageResource(R.drawable.stone);
                mTxtResult.setText(getString(R.string.result) +
                        getString(R.string.player_win));
            }
            else {
                mImgViewComPlay.setImageResource(R.drawable.paper);
                mTxtResult.setText(getString(R.string.result) +
                        getString(R.string.player_draw));
            }
        }
    };
}
