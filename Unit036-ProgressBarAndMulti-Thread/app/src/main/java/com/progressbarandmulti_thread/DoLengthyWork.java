package com.progressbarandmulti_thread;

import android.os.Handler;
import android.widget.ProgressBar;

import java.util.Calendar;

public class DoLengthyWork extends Thread {

    private Handler mHandler;
    private ProgressBar mProgressBar;

    @Override
    public void run() {
        Calendar begin = Calendar.getInstance();
        do {
            Calendar now = Calendar.getInstance();
            final int iDiffSec = 60 * (now.get(Calendar.MINUTE) -
                    begin.get(Calendar.MINUTE)) +
                    now.get(Calendar.SECOND) - begin.get(Calendar.SECOND);

            if (iDiffSec * 2 > 100) {
                mHandler.post(new Runnable() {
                    public void run() {
                        mProgressBar.setProgress(100);
                    }
                });

                break;
            }

            mHandler.post(new Runnable() {
                public void run() {
                    mProgressBar.setProgress(iDiffSec * 2);
                }
            });

            if (iDiffSec * 4 < 100)
                mHandler.post(new Runnable() {
                    public void run() {
                        mProgressBar.setSecondaryProgress(iDiffSec * 4);
                    }
                });
            else
                mHandler.post(new Runnable() {
                    public void run() {
                        mProgressBar.setSecondaryProgress(100);
                    }
                });
        } while (true);
    }

    void setProgressBar(ProgressBar proBar) {
        mProgressBar = proBar;
    }

    void setHandler(Handler h) {
        mHandler = h;
    }

}
