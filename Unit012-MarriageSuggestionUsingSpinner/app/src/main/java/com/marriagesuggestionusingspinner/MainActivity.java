package com.marriagesuggestionusingspinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText mEdtAge;
    private Button mBtnOK;
    private TextView mTxtR;
    private Spinner mSpnSex;
    private String msSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEdtAge = (EditText) findViewById(R.id.edtAge);
        mBtnOK = (Button) findViewById(R.id.btnOK);
        mTxtR = (TextView) findViewById(R.id.txtR);

        mBtnOK.setOnClickListener(btnOKOnClick);

        mSpnSex = (Spinner) findViewById(R.id.spnSex);
        mSpnSex.setOnItemSelectedListener(spnSexOnItemSelected);
    }

    private View.OnClickListener btnOKOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int iAge = Integer.parseInt(mEdtAge.getText().toString());

            String strSug;
            if (msSex.equals(getString(R.string.sex_male)))
                if (iAge < 28)
                    strSug = getString(R.string.sug_not_hurry);
                else if (iAge > 33)
                    strSug = getString(R.string.sug_get_married);
                else
                    strSug = getString(R.string.sug_find_couple);
            else
                if (iAge < 25)
                    strSug = getString(R.string.sug_not_hurry);
                else if (iAge > 30)
                    strSug = getString(R.string.sug_get_married);
                else
                    strSug = getString(R.string.sug_find_couple);

            mTxtR.setText(strSug);
        }
    };

    private AdapterView.OnItemSelectedListener spnSexOnItemSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            msSex = adapterView.getSelectedItem().toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };
}
