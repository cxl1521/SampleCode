package edu.ntut.user.myhw3;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mMainActivity;
    private Spinner mSpnSex;
    private RadioGroup mRadGrp;
    private RadioButton mRadBtnAgeRange1;
    private RadioButton mRadBtnAgeRange2;
    private RadioButton mRadBtnAgeRange3;
    private TextView mTxtNumFamily;
    private NumberPicker mNumPkrFamily;
    private Button mBtnOK;
    private TextView mTxtSug;


    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        // initialize
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        mMainActivity = (MainActivity) getActivity();
        mSpnSex = (Spinner) mMainActivity.findViewById(R.id.spnSex);
//        mRadGrp = (RadioGroup) mMainActivity.findViewById(R.id.radGrpAge);
        mRadBtnAgeRange1 = (RadioButton) mMainActivity.findViewById(R.id.radBtnAgeRange1);
        mRadBtnAgeRange2 = (RadioButton) mMainActivity.findViewById(R.id.radBtnAgeRange2);
        mRadBtnAgeRange3 = (RadioButton) mMainActivity.findViewById(R.id.radBtnAgeRange3);
//        mTxtNumFamily = (TextView) mMainActivity.findViewById(R.id.txtNumFamily);
        mNumPkrFamily = (NumberPicker) mMainActivity.findViewById(R.id.numPkrFamply);
        mBtnOK = (Button) mMainActivity.findViewById(R.id.btnOK);
        mTxtSug = (TextView) mMainActivity.findViewById(R.id.txtSug);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("edu.ntut.user.myhw3", appContext.getPackageName());
    }

    @Test
    public void maleAgeRange1Family1() {

        //create and execute UI thread (in order to perform GUI interactions)
        mMainActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {                    // provide inputs
                mSpnSex.setSelection(0);
                mRadBtnAgeRange1.setChecked(true);
                mNumPkrFamily.setValue(3);
                mBtnOK.performClick();
            }
        });

        try {
            Thread.sleep(2000);   // wait for 2 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals("建議："+mMainActivity.getString(R.string.sug_not_hurry),
                mTxtSug.getText().toString());
    }

    @Test
    public void maleAgeRange1Family2() {

        //create and execute UI thread (in order to perform GUI interactions)
        mMainActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {                    // provide inputs
                mSpnSex.setSelection(0);
                mRadBtnAgeRange1.setChecked(true);
                mNumPkrFamily.setValue(5);
                mBtnOK.performClick();
            }
        });

        try {
            Thread.sleep(2000);   // wait for 2 second
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals("建議："+mMainActivity.getString(R.string.sug_find_couple),
                mTxtSug.getText().toString());
    }
}
