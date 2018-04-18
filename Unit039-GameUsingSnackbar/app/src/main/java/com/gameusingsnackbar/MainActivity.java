package com.gameusingsnackbar;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView mImgViewComPlay;
    private ImageButton mImgBtnScissors, mImgBtnStone, mImgBtnPaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImgViewComPlay = (ImageView)findViewById(R.id.imgViewComPlay);
        mImgBtnScissors = (ImageButton)findViewById(R.id.imgBtnScissors);
        mImgBtnStone = (ImageButton)findViewById(R.id.imgBtnStone);
        mImgBtnPaper = (ImageButton)findViewById(R.id.imgBtnPaper);

        mImgBtnScissors.setOnClickListener(imgBtnScissorsOnClick);
        mImgBtnStone.setOnClickListener(imgBtnStoneOnClick);
        mImgBtnPaper.setOnClickListener(imgBtnPaperOnClick);
    }

    private View.OnClickListener imgBtnScissorsOnClick = new View.OnClickListener() {
        public void onClick(View view) {
            int iComPlay = (int)(Math.random()*3 + 1);

            if (iComPlay == 1) {
                mImgViewComPlay.setImageResource(R.drawable.scissors);
                Snackbar.make(view, R.string.player_draw, Snackbar.LENGTH_LONG)
                        .show();
            }
            else if (iComPlay == 2) {
                mImgViewComPlay.setImageResource(R.drawable.stone);
                Snackbar.make(view, R.string.player_lose, Snackbar.LENGTH_LONG)
                        .show();
            }
            else {
                mImgViewComPlay.setImageResource(R.drawable.paper);
                Snackbar.make(view, R.string.player_win, Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    };

    private View.OnClickListener imgBtnStoneOnClick = new View.OnClickListener() {
        public void onClick(View view) {
            int iComPlay = (int)(Math.random()*3 + 1);

            if (iComPlay == 1) {
                mImgViewComPlay.setImageResource(R.drawable.scissors);
                Snackbar.make(view, R.string.player_win, Snackbar.LENGTH_LONG)
                        .show();
            }
            else if (iComPlay == 2) {
                mImgViewComPlay.setImageResource(R.drawable.stone);
                Snackbar.make(view, R.string.player_draw, Snackbar.LENGTH_LONG)
                        .show();
            }
            else {
                mImgViewComPlay.setImageResource(R.drawable.paper);
                Snackbar.make(view, R.string.player_lose, Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    };

    private View.OnClickListener imgBtnPaperOnClick = new View.OnClickListener() {
        public void onClick(View view) {
            int iComPlay = (int)(Math.random()*3 + 1);

            if (iComPlay == 1) {
                mImgViewComPlay.setImageResource(R.drawable.scissors);
                Snackbar.make(view, R.string.player_lose, Snackbar.LENGTH_LONG)
                        .show();
            }
            else if (iComPlay == 2) {
                mImgViewComPlay.setImageResource(R.drawable.stone);
                Snackbar.make(view, R.string.player_win, Snackbar.LENGTH_LONG)
                        .show();
            }
            else {
                mImgViewComPlay.setImageResource(R.drawable.paper);
                Snackbar.make(view, R.string.player_draw, Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    };
}
