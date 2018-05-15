package com.getscreenwidthheightresolution;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        float horiDpi = dm.xdpi;   // 螢幕的水平解析度
        float vertDpi = dm.ydpi;   // 螢幕的垂直解析度

        Toast.makeText(this, "(width, height, xdpi, ydpi) = (" +
                screenWidth + ", " + screenHeight + ", " + horiDpi + ", " + vertDpi + ")", Toast.LENGTH_LONG)
                .show();

        switch(dm.densityDpi){
            case DisplayMetrics.DENSITY_LOW:
                Toast.makeText(this, "低解析度", Toast.LENGTH_LONG)
                        .show();
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                Toast.makeText(this, "中解析度", Toast.LENGTH_LONG)
                        .show();
                break;
            case DisplayMetrics.DENSITY_HIGH:
                Toast.makeText(this, "高解析度", Toast.LENGTH_LONG)
                        .show();
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                Toast.makeText(this, "超高解析度", Toast.LENGTH_LONG)
                        .show();
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                Toast.makeText(this, "超超高解析度", Toast.LENGTH_LONG)
                        .show();
                break;
        }

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        if (linearLayout != null) {
            linearLayout.post(new Runnable() {
                @Override
                public void run() {
                    // 取得App畫面區域的大小
                    Rect r = new Rect();
                    Window window = getWindow();
                    window.getDecorView().getWindowVisibleDisplayFrame(r);
                    int iStatusBarHeight = r.top;   // 也可以取得status bar的高度
                    int iStatusBarPlusActionBarHeight =
                            getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    int iAppWindowHeight = dm.heightPixels - iStatusBarPlusActionBarHeight;

                    Toast.makeText(MainActivity.this, "Status bar高度：" + iStatusBarHeight, Toast.LENGTH_LONG)
                            .show();
                    Toast.makeText(MainActivity.this, "Status bar加上Action bar高度：" + iStatusBarPlusActionBarHeight, Toast.LENGTH_LONG)
                            .show();
                    Toast.makeText(MainActivity.this, "App顯示區域高度：" + iAppWindowHeight, Toast.LENGTH_LONG)
                            .show();
                }
            });
        }
    }
}
