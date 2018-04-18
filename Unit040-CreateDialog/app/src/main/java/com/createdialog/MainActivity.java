package com.createdialog;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button mBtnLoginDlg;
    private TextView mTxtResult;
    private Dialog mDlgLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnLoginDlg = (Button) findViewById(R.id.btnLoginDlg);
        mTxtResult = (TextView) findViewById(R.id.txtResult);

        mBtnLoginDlg.setOnClickListener(btnLoginDlgOnClick);
    }

    private View.OnClickListener btnLoginDlgOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            mTxtResult.setText("");

            mDlgLogin = new Dialog(MainActivity.this);
            mDlgLogin.setCancelable(false);
            mDlgLogin.setContentView(R.layout.dlg_log_in);
            Button loginBtnOK = (Button) mDlgLogin.findViewById(R.id.btnOK);
            Button loginBtnCancel = (Button) mDlgLogin.findViewById(R.id.btnCancel);
            loginBtnOK.setOnClickListener(loginDlgBtnOKOnClick);
            loginBtnCancel.setOnClickListener(loginDlgBtnCancelOnClick);
            mDlgLogin.show();
        }
    };

    private View.OnClickListener loginDlgBtnOKOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            EditText edtUserName = (EditText) mDlgLogin.findViewById(R.id.edtUserName);
            EditText edtPassword = (EditText) mDlgLogin.findViewById(R.id.edtPassword);

            mTxtResult.setText("你輸入的使用者名稱：" + edtUserName.getText().toString() +
                    "，密碼：" + edtPassword.getText().toString());
            mDlgLogin.cancel();
        }
    };

    private View.OnClickListener loginDlgBtnCancelOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            mTxtResult.setText("你按下\"取消\"按鈕。");
            mDlgLogin.cancel();
        }
    };

}
