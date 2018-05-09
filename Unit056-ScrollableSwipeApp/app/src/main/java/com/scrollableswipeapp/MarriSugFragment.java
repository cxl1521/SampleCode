package com.scrollableswipeapp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MarriSugFragment extends Fragment {

    private Button mBtnOK;
    private TextView mTxtR;

    private RadioGroup mRadGrpSex, mRadGrpAge;
    private RadioButton mRadBtnAgeRange1, mRadBtnAgeRange2, mRadBtnAgeRange3;

    public MarriSugFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_marri_sug, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBtnOK = (Button) getView().findViewById(R.id.btnOK);
        mTxtR = (TextView) getView().findViewById(R.id.txtR);

        mBtnOK.setOnClickListener(btnOKOnClick);

        mRadGrpSex = (RadioGroup) getView().findViewById(R.id.radGrpSex);
        mRadGrpAge = (RadioGroup) getView().findViewById(R.id.radGrpAge);
        mRadBtnAgeRange1 = (RadioButton) getView().findViewById(R.id.radBtnAgeRange1);
        mRadBtnAgeRange2 = (RadioButton) getView().findViewById(R.id.radBtnAgeRange2);
        mRadBtnAgeRange3 = (RadioButton) getView().findViewById(R.id.radBtnAgeRange3);
        mRadGrpSex.setOnCheckedChangeListener(radGrpSexOnCheckedChange);
    }

    private View.OnClickListener btnOKOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String strSug = getString(R.string.result);

            // 不需要判斷男女生，直接依照選擇的年齡區間顯示結果
            switch (mRadGrpAge.getCheckedRadioButtonId()) {
                case R.id.radBtnAgeRange1:
                    strSug += getString(R.string.sug_not_hurry);
                    break;
                case R.id.radBtnAgeRange2:
                    strSug += getString(R.string.sug_find_couple);
                    break;
                case R.id.radBtnAgeRange3:
                    strSug += getString(R.string.sug_get_married);
                    break;
            }

            mTxtR.setText(strSug);
        }
    };

    private RadioGroup.OnCheckedChangeListener radGrpSexOnCheckedChange = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            if (i == R.id.radBtnMale) {
                mRadBtnAgeRange1.setText(getString(R.string.male_age_range1));
                mRadBtnAgeRange2.setText(getString(R.string.male_age_range2));
                mRadBtnAgeRange3.setText(getString(R.string.male_age_range3));
            } else {
                mRadBtnAgeRange1.setText(getString(R.string.female_age_range1));
                mRadBtnAgeRange2.setText(getString(R.string.female_age_range2));
                mRadBtnAgeRange3.setText(getString(R.string.female_age_range3));
            }
        }
    };
}
