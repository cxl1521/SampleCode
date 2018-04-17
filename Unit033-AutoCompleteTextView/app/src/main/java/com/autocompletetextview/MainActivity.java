package com.autocompletetextview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mBtnAddAutoCompleteText,
            mBtnClearAutoCompleteText;
    private AutoCompleteTextView mAutoCompTextView;

    private ArrayAdapter<String> mAdapAutoCompText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnAddAutoCompleteText = (Button) findViewById(R.id.btnAddAutoCompleteText);
        mBtnClearAutoCompleteText = (Button) findViewById(R.id.btnClearAutoCompleteText);
        mAutoCompTextView = (AutoCompleteTextView) findViewById(R.id.autoCompTextView);

        mAdapAutoCompText = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line);

        mAutoCompTextView.setAdapter(mAdapAutoCompText);

        mBtnAddAutoCompleteText.setOnClickListener(btnAddAutoCompleteTextOnClick);
        mBtnClearAutoCompleteText.setOnClickListener(btnClearAutoCompleteTextOnClick);

    }

    private View.OnClickListener btnAddAutoCompleteTextOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String s = mAutoCompTextView.getText().toString();
            mAdapAutoCompText.add(s);
            mAutoCompTextView.setText("");
        }
    };

    private View.OnClickListener btnClearAutoCompleteTextOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mAdapAutoCompText.clear();
        }
    };
}
