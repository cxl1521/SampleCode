package com.progressdialogandmultithread;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private Button mBtnShowProgressDlg;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);setContentView(R.layout.activity_main);

        mBtnShowProgressDlg = (Button) findViewById(R.id.btnShowProgressDlg);
        mBtnShowProgressDlg.setOnClickListener(btnShowProgressDlgOnClick);
    }

    private View.OnClickListener btnShowProgressDlgOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            final ProgressDialog progressDlg = new ProgressDialog(MainActivity.this);
            progressDlg.setTitle("執行中");
            progressDlg.setMessage("請稍等...");
            progressDlg.setIcon(android.R.drawable.ic_dialog_info);
            progressDlg.setCancelable(false);
            progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDlg.setMax(100);
            progressDlg.show();

            new Thread(new Runnable() {
                public void run() {
                    Calendar begin = Calendar.getInstance();
                    do {
                        Calendar now = Calendar.getInstance();
                        final int iDiffSec = 60 * (now.get(Calendar.MINUTE) - begin.get(Calendar.MINUTE)) +
                                now.get(Calendar.SECOND) - begin.get(Calendar.SECOND);

                        if (iDiffSec * 2 > 100) {
                            mHandler.post(new Runnable() {
                                public void run() {
                                    progressDlg.setProgress(100);
                                }
                            });

                            break;
                        }

                        mHandler.post(new Runnable() {
                            public void run() {
                                progressDlg.setProgress(iDiffSec * 2);
                            }
                        });

                        if (iDiffSec * 4 < 100)
                            mHandler.post(new Runnable() {
                                public void run() {
                                    progressDlg.setSecondaryProgress(iDiffSec * 4);
                                }
                            });
                        else
                            mHandler.post(new Runnable() {
                                public void run() {
                                    progressDlg.setSecondaryProgress(100);
                                }
                            });
                    } while (true);

                    progressDlg.dismiss();
                }
            }).start();
        }
    };

}
