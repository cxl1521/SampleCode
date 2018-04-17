package com.seekbarandratingbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private RatingBar mRatingBar;
    private SeekBar mSeekBar;
    private TextView mTxtSeekBarProgress,
            mTxtRatingBarValue,
            mTxtRatingBarProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mTxtSeekBarProgress = (TextView) findViewById(R.id.txtSeekBarProgress);
        mTxtRatingBarValue = (TextView) findViewById(R.id.txtRatingBarValue);
        mTxtRatingBarProgress = (TextView) findViewById(R.id.txtRatingBarProgress);

        mSeekBar.setOnSeekBarChangeListener(seekBarOnChange);
        mRatingBar.setOnRatingBarChangeListener(ratingBarOnChange);
    }

    private SeekBar.OnSeekBarChangeListener seekBarOnChange = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            String s = getString(R.string.seek_bar_progress);
            mTxtSeekBarProgress.setText(s + String.valueOf(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private RatingBar.OnRatingBarChangeListener ratingBarOnChange = new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float  rating,
                                    boolean fromUser) {
            String s = getString(R.string.rating_bar_value);
            mTxtRatingBarValue.setText(s + String.valueOf(rating));
            s = getString(R.string.rating_bar_progress);
            mTxtRatingBarProgress.setText(s + String.valueOf(mRatingBar.getProgress()));
        }
    };
}
