package com.activitylifecycle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ActivityLifeCycle";
    final static private int LAUNCH_GAME = 0;
    private TextView mTxtResult;
    private Button mBtnLaunchGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.btnLaunchGame);
        btn.setOnClickListener(btnLaunchGameOnClick);

        mTxtResult = (TextView)findViewById(R.id.txtResult);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != LAUNCH_GAME)
            return;

        switch (resultCode) {
            case RESULT_OK:
                Bundle bundle = data.getExtras();

                int iCountSet = bundle.getInt("KEY_COUNT_SET");
                int iCountPlayerWin = bundle.getInt("KEY_COUNT_PLAYER_WIN");
                int iCountComWin = bundle.getInt("KEY_COUNT_COM_WIN");
                int iCountDraw = bundle.getInt("KEY_COUNT_DRAW");

                String s = "遊戲結果：你總共玩了" + iCountSet +
                        "局, 贏了" + iCountPlayerWin +
                        "局, 輸了" + iCountComWin +
                        "局, 平手" + iCountDraw + "局";
                mTxtResult.setText(s);

                break;
            case RESULT_CANCELED:
                mTxtResult.setText("你選擇取消遊戲。");
        }
    }

    private View.OnClickListener btnLaunchGameOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent it = new Intent();
            it.setClass(MainActivity.this, GameActivity.class);
            startActivityForResult(it, LAUNCH_GAME);
        }
    };

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "MainActivity.onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "MainActivity.onPause()");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.d(LOG_TAG, "MainActivity.onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "MainActivity.onResume()");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.d(LOG_TAG, "MainActivity.onStart()");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "MainActivity.onStop()");
        super.onStop();
    }
}
