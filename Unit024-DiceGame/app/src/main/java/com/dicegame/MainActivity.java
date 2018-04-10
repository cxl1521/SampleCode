package com.dicegame;

import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private ImageView mImgRollingDice;
    private TextView mTxtDiceResult;
    private Button mBtnRollDice;

    // Use static Handler to avoid memory leaks.
    private static class StaticHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public StaticHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity == null) return;

            int iRand = (int)(Math.random()*6 + 1);

            String s = activity.getString(R.string.dice_result);
            activity.mTxtDiceResult.setText(s + iRand);
            switch (iRand) {
                case 1:
                    activity.mImgRollingDice.setImageResource(R.drawable.dice01);
                    break;
                case 2:
                    activity.mImgRollingDice.setImageResource(R.drawable.dice02);
                    break;
                case 3:
                    activity.mImgRollingDice.setImageResource(R.drawable.dice03);
                    break;
                case 4:
                    activity.mImgRollingDice.setImageResource(R.drawable.dice04);
                    break;
                case 5:
                    activity.mImgRollingDice.setImageResource(R.drawable.dice05);
                    break;
                case 6:
                    activity.mImgRollingDice.setImageResource(R.drawable.dice06);
                    break;
            }

        }
    }

    public final StaticHandler mHandler = new StaticHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImgRollingDice = (ImageView)findViewById(R.id.imgRollingDice);
        mTxtDiceResult = (TextView)findViewById(R.id.txtDiceResult);
        mBtnRollDice = (Button)findViewById(R.id.btnRollDice);

        mBtnRollDice.setOnClickListener(btnRollDiceOnClick);
    }

    private View.OnClickListener btnRollDiceOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String s = getString(R.string.dice_result);
            mTxtDiceResult.setText(s);

            // 從程式資源中取得動畫檔，設定給ImageView物件，然後開始播放。
            Resources res = getResources();
            final AnimationDrawable animDraw =
                    (AnimationDrawable) res.getDrawable(R.drawable.anim_roll_dice);
            mImgRollingDice.setImageDrawable(animDraw);
            animDraw.start();

            // 啟動background thread進行計時。
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    animDraw.stop();
                    mHandler.sendMessage(mHandler.obtainMessage());
                }
            }).start();

            // 計時的另一種作法。
//            mHandler.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    animDraw.stop();
//                    mHandler.sendMessage(mHandler.obtainMessage());
//                }
//            }, 5000);
        }
    };
}
