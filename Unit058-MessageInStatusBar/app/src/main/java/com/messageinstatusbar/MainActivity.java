package com.messageinstatusbar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int NOTI_ID = 100;

    private TextView mTxtResult;
    private ImageView mImgViewComPlay;
    private ImageButton mImgBtnScissors, mImgBtnStone, mImgBtnPaper;

    // 新增統計遊戲局數和輸贏的變數
    private int miCountSet = 0,
            miCountPlayerWin = 0,
            miCountComWin = 0,
            miCountDraw = 0;

    private Button mBtnShowResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImgViewComPlay = (ImageView)findViewById(R.id.imgViewComPlay);
        mTxtResult = (TextView)findViewById(R.id.txtResult);
        mImgBtnScissors = (ImageButton)findViewById(R.id.imgBtnScissors);
        mImgBtnStone = (ImageButton)findViewById(R.id.imgBtnStone);
        mImgBtnPaper = (ImageButton)findViewById(R.id.imgBtnPaper);

        mImgBtnScissors.setOnClickListener(imgBtnScissorsOnClick);
        mImgBtnStone.setOnClickListener(imgBtnStoneOnClick);
        mImgBtnPaper.setOnClickListener(imgBtnPaperOnClick);

        mBtnShowResult = (Button)findViewById(R.id.btnShowResult);
        mBtnShowResult.setOnClickListener(btnShowResultOnClick);
    }

    @Override
    protected void onDestroy() {
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                .cancel(NOTI_ID);

        super.onDestroy();
    }

    private View.OnClickListener imgBtnScissorsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            // Decide computer play.
            int iComPlay = (int)(Math.random()*3 + 1);

            miCountSet++;

            // 1 - scissors, 2 - stone, 3 - net.
            if (iComPlay == 1) {
                mImgViewComPlay.setImageResource(R.drawable.scissors);
                mTxtResult.setText(getString(R.string.result) +
                        getString(R.string.player_draw));
                miCountDraw++;
                showNotification("平手" + Integer.toString(miCountDraw) + "局");
            }
            else if (iComPlay == 2) {
                mImgViewComPlay.setImageResource(R.drawable.stone);
                mTxtResult.setText(getString(R.string.result) +
                        getString(R.string.player_lose));
                miCountComWin++;
                showNotification("電腦贏" + Integer.toString(miCountComWin) + "局");
            }
            else {
                mImgViewComPlay.setImageResource(R.drawable.paper);
                mTxtResult.setText(getString(R.string.result) +
                        getString(R.string.player_win));
                miCountPlayerWin++;
                showNotification("玩家贏" + Integer.toString(miCountPlayerWin) + "局");
            }
        }
    };

    private View.OnClickListener imgBtnStoneOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            int iComPlay = (int)(Math.random()*3 + 1);

            miCountSet++;

            // 1 - scissors, 2 - stone, 3 - net.
            if (iComPlay == 1) {
                mImgViewComPlay.setImageResource(R.drawable.scissors);
                mTxtResult.setText(getString(R.string.result) +
                        getString(R.string.player_win));
                miCountPlayerWin++;
                showNotification("玩家贏" + Integer.toString(miCountPlayerWin) + "局");
            }
            else if (iComPlay == 2) {
                mImgViewComPlay.setImageResource(R.drawable.stone);
                mTxtResult.setText(getString(R.string.result) +
                        getString(R.string.player_draw));
                miCountDraw++;
                showNotification("平手" + Integer.toString(miCountDraw) + "局");
            }
            else {
                mImgViewComPlay.setImageResource(R.drawable.paper);
                mTxtResult.setText(getString(R.string.result) +
                        getString(R.string.player_lose));
                miCountComWin++;
                showNotification("電腦贏" + Integer.toString(miCountComWin) + "局");
            }
        }
    };

    private View.OnClickListener imgBtnPaperOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            int iComPlay = (int)(Math.random()*3 + 1);

            miCountSet++;

            // 1 - scissors, 2 - stone, 3 - net.
            if (iComPlay == 1) {
                mImgViewComPlay.setImageResource(R.drawable.scissors);
                mTxtResult.setText(getString(R.string.result) +
                        getString(R.string.player_lose));
                miCountComWin++;
                showNotification("電腦贏" + Integer.toString(miCountComWin) + "局");
            }
            else if (iComPlay == 2) {
                mImgViewComPlay.setImageResource(R.drawable.stone);
                mTxtResult.setText(getString(R.string.result) +
                        getString(R.string.player_win));
                miCountPlayerWin++;
                showNotification("玩家贏" + Integer.toString(miCountPlayerWin) + "局");
            }
            else {
                mImgViewComPlay.setImageResource(R.drawable.paper);
                mTxtResult.setText(getString(R.string.result) +
                        getString(R.string.player_draw));
                miCountDraw++;
                showNotification("平手" + Integer.toString(miCountDraw) + "局");
            }
        }
    };

    private View.OnClickListener btnShowResultOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            Intent it = new Intent();
            it.setClass(MainActivity.this, GameResultActivity.class);

            Bundle bundle = new Bundle();
            bundle.putInt("KEY_COUNT_SET", miCountSet);
            bundle.putInt("KEY_COUNT_PLAYER_WIN", miCountPlayerWin);
            bundle.putInt("KEY_COUNT_COM_WIN", miCountComWin);
            bundle.putInt("KEY_COUNT_DRAW", miCountDraw);
            it.putExtras(bundle);

            startActivity(it);
        }
    };

    private void showNotification(String sMsg) {
        Intent it = new Intent(getApplicationContext(), GameResultActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = new Bundle();
        bundle.putInt("KEY_COUNT_SET", miCountSet);
        bundle.putInt("KEY_COUNT_PLAYER_WIN", miCountPlayerWin);
        bundle.putInt("KEY_COUNT_COM_WIN", miCountComWin);
        bundle.putInt("KEY_COUNT_DRAW", miCountDraw);
        it.putExtras(bundle);

        PendingIntent penIt = PendingIntent.getActivity(getApplicationContext(),
                0, it, PendingIntent. FLAG_CANCEL_CURRENT);

        Notification noti = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.btn_star_big_on)
                .setTicker(sMsg)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(sMsg)
                .setContentIntent(penIt)
                .build();

        NotificationManager notiMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notiMgr.notify(NOTI_ID, noti);
    }
}
