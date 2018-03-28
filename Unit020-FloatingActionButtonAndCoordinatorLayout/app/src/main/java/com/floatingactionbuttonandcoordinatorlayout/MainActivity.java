package com.floatingactionbuttonandcoordinatorlayout;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mFab;
    private TextView mTxtMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTxtMsg = (TextView) findViewById(R.id.txtMsg);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(mFabOnClick);
    }

    private View.OnClickListener mFabOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mTxtMsg.setText("你按了FAB！");
        }
    };
}
