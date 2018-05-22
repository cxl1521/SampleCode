package com.unit6_marriagesuggestion;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    // 用來儲存MainActivity物件和它的介面元件
    private MainActivity mMainActivity;
    private EditText mEdtSex, mEdtAge;
    private Button mBtnDoSug;
    private TextView mTxtResult;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        // 完成初始設定。
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        // 利用getActivity()方法取得MainActivity物件
        // 再取得MainActivity物件的介面元件
        mMainActivity = (MainActivity) getActivity();
        mEdtSex = (EditText) mMainActivity.findViewById(R.id.edtSex);
        mEdtAge = (EditText) mMainActivity.findViewById(R.id.edtAge);
        mBtnDoSug = (Button) mMainActivity.findViewById(R.id.btnOK);
        mTxtResult = (TextView) mMainActivity.findViewById(R.id.txtR);
    }

    @Test
    public void maleAge25() {
        // 測試男生，年齡25的情況
        // 對於介面元件的控制必須在UI thread（或稱為main thread）中執行
        mMainActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mEdtSex.setText("男");
                mEdtAge.setText("25");
                mBtnDoSug.performClick();
            }
        });

        // 由於模擬器執行比較慢，等候一點時間讓它執行完畢
        try {
            Thread.sleep(2000);   // 等2秒鐘
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 檢查是否顯示正確的字串
        assertEquals(mMainActivity.getString(R.string.sug_not_hurry),
                mTxtResult.getText().toString());
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();

        // 加入測試完畢後執行清除的程式碼。
    }
}
