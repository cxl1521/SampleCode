package com.hobbyselectionusingcheckbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private CheckBox mChkBoxMusic, mChkBoxSing, mChkBoxDance,
            mChkBoxTravel, mChkBoxReading, mChkBoxWriting,
            mChkBoxClimbing, mChkBoxSwim, mChkBoxExercise,
            mChkBoxFitness, mChkBoxPhoto, mChkBoxEating,
            mChkBoxPainting;
    private Button mBtnOK;
    private TextView mTxtHobby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 從介面佈局檔中取得介面元件
        mChkBoxMusic = (CheckBox)findViewById(R.id.chkBoxMusic);
        mChkBoxSing = (CheckBox)findViewById(R.id.chkBoxSing);
        mChkBoxDance = (CheckBox)findViewById(R.id.chkBoxDance);
        mChkBoxTravel = (CheckBox)findViewById(R.id.chkBoxTravel);
        mChkBoxReading = (CheckBox)findViewById(R.id.chkBoxReading);
        mChkBoxWriting = (CheckBox)findViewById(R.id.chkBoxWriting);
        mChkBoxClimbing = (CheckBox)findViewById(R.id.chkBoxClimbing);
        mChkBoxSwim = (CheckBox)findViewById(R.id.chkBoxSwim);
        mChkBoxExercise = (CheckBox)findViewById(R.id.chkBoxExercise);
        mChkBoxFitness = (CheckBox)findViewById(R.id.chkBoxFitness);
        mChkBoxPhoto = (CheckBox)findViewById(R.id.chkBoxPhoto);
        mChkBoxEating = (CheckBox)findViewById(R.id.chkBoxEating);
        mChkBoxPainting = (CheckBox)findViewById(R.id.chkBoxPainting);
        mBtnOK = (Button)findViewById(R.id.btnOK);
        mTxtHobby = (TextView)findViewById(R.id.txtHobby);

        // 設定button元件的事件listener
        mBtnOK.setOnClickListener(btnOKOnClick);
    }

    private View.OnClickListener btnOKOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String s = getString(R.string.your_hobby);

            if (mChkBoxMusic.isChecked())
                s += mChkBoxMusic.getText().toString();

            if (mChkBoxSing.isChecked())
                s += mChkBoxSing.getText().toString();

            if (mChkBoxDance.isChecked())
                s += mChkBoxDance.getText().toString();

            if (mChkBoxTravel.isChecked())
                s += mChkBoxTravel.getText().toString();

            if (mChkBoxReading.isChecked())
                s += mChkBoxReading.getText().toString();

            if (mChkBoxWriting.isChecked())
                s += mChkBoxWriting.getText().toString();

            if (mChkBoxClimbing.isChecked())
                s += mChkBoxClimbing.getText().toString();

            if (mChkBoxSwim.isChecked())
                s += mChkBoxSwim.getText().toString();

            if (mChkBoxExercise.isChecked())
                s += mChkBoxExercise.getText().toString();

            if (mChkBoxFitness.isChecked())
                s += mChkBoxFitness.getText().toString();

            if (mChkBoxPhoto.isChecked())
                s += mChkBoxPhoto.getText().toString();

            if (mChkBoxEating.isChecked())
                s += mChkBoxEating.getText().toString();

            if (mChkBoxPainting.isChecked())
                s += mChkBoxPainting.getText().toString();

            mTxtHobby.setText(s);
        }
    };
}
