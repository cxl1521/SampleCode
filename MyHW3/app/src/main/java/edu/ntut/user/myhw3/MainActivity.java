package edu.ntut.user.myhw3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Spinner mSpnSex;
    private RadioGroup mRadGrp;
    private RadioButton mRadBtnAgeRange1;
    private RadioButton mRadBtnAgeRange2;
    private RadioButton mRadBtnAgeRange3;
    private TextView mTxtNumFamily;
    private NumberPicker mNumPkrFamily;
    private Button mBtnOK;
    private TextView mTxtSug;

    private String selectedSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpnSex = (Spinner) findViewById(R.id.spnSex);
        mRadGrp = (RadioGroup) findViewById(R.id.radGrpAge);
        mRadBtnAgeRange1 = (RadioButton) findViewById(R.id.radBtnAgeRange1);
        mRadBtnAgeRange2 = (RadioButton) findViewById(R.id.radBtnAgeRange2);
        mRadBtnAgeRange3 = (RadioButton) findViewById(R.id.radBtnAgeRange3);
        mTxtNumFamily = (TextView) findViewById(R.id.txtNumFamily);
        mNumPkrFamily = (NumberPicker) findViewById(R.id.numPkrFamply);
        mNumPkrFamily.setMinValue(0);
        mNumPkrFamily.setMaxValue(20);
        mNumPkrFamily.setValue(3);
        mBtnOK = (Button) findViewById(R.id.btnOK);
        mTxtSug = (TextView) findViewById(R.id.txtSug);

        mSpnSex.setOnItemSelectedListener(spnOnItemSelect);
        mNumPkrFamily.setOnValueChangedListener(numPkrFamilyOnValueChange);
        mBtnOK.setOnClickListener(btnOKOnClick);
    }

    private AdapterView.OnItemSelectedListener spnOnItemSelect = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedSex = parent.getSelectedItem().toString();

            switch (selectedSex) {
                case "male": mRadBtnAgeRange1.setText(getString(R.string.maleAgeRange1));
                    mRadBtnAgeRange2.setText(getString(R.string.maleAgeRange2));
                    mRadBtnAgeRange3.setText(getString(R.string.maleAgeRange3));
                    break;
                case "female":
                    mRadBtnAgeRange1.setText(getString(R.string.femaleAgeRange1));
                    mRadBtnAgeRange2.setText(getString(R.string.femaleAgeRange2));
                    mRadBtnAgeRange3.setText(getString(R.string.femaleAgeRange3));
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private NumberPicker.OnValueChangeListener numPkrFamilyOnValueChange = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            mTxtNumFamily.setText(String.valueOf(newVal));
        }
    };

    private View.OnClickListener btnOKOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            MarriageSuggestion ms = new MarriageSuggestion();

            String strSex = mSpnSex.getSelectedItem().toString();
            int iAgeRange = 0;

                switch (mRadGrp.getCheckedRadioButtonId()) {
                    case R.id.radBtnAgeRange1:
                        iAgeRange = 1;
                        break;
                    case R.id.radBtnAgeRange2:
                        iAgeRange = 2;
                        break;
                    case R.id.radBtnAgeRange3:
                        iAgeRange = 3;
                        break;
                }

            mTxtSug.setText(ms.getSuggestion(strSex, iAgeRange, mNumPkrFamily.getValue()));
        }
    };
}
