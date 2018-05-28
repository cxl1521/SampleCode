package com.androidwearandaction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GameResultActivity extends AppCompatActivity {

    private EditText mEdtCountSet,
            mEdtCountPlayerWin,
            mEdtCountComWin,
            mEdtCountDraw;
    private Button mBtnBackToGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);

        mEdtCountSet = (EditText)findViewById(R.id.edtCountSet);
        mEdtCountPlayerWin = (EditText)findViewById(R.id.edtCountPlayerWin);
        mEdtCountComWin = (EditText)findViewById(R.id.edtCountComWin);
        mEdtCountDraw = (EditText)findViewById(R.id.edtCountDraw);
        mBtnBackToGame = (Button)findViewById(R.id.btnBackToGame);

        mBtnBackToGame.setOnClickListener(btnBackToGameOnClick);

        showResult();
    }

    private View.OnClickListener btnBackToGameOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };

    private void showResult() {
        // 從 Bundle 物件中取出資料
        Bundle bundle = getIntent().getExtras();

        int iCountSet = bundle.getInt("KEY_COUNT_SET");
        int iCountPlayerWin = bundle.getInt("KEY_COUNT_PLAYER_WIN");
        int iCountComWin = bundle.getInt("KEY_COUNT_COM_WIN");
        int iCountDraw = bundle.getInt("KEY_COUNT_DRAW");

        mEdtCountSet.setText(Integer.toString(iCountSet));
        mEdtCountPlayerWin.setText(Integer.toString(iCountPlayerWin));
        mEdtCountComWin.setText(Integer.toString(iCountComWin));
        mEdtCountDraw.setText(Integer.toString(iCountDraw));
    }
}
