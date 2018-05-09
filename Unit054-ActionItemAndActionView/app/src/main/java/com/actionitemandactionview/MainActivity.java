package com.actionitemandactionview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MENU_MUSIC = Menu.FIRST,
            MENU_PLAY_MUSIC = Menu.FIRST + 1,
            MENU_STOP_PLAYING_MUSIC = Menu.FIRST + 2,
            MENU_ABOUT = Menu.FIRST + 3,
            MENU_EXIT = Menu.FIRST + 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actBar = getSupportActionBar();
        actBar.setDisplayShowTitleEnabled(false);
        actBar.setLogo(R.drawable.app_logo);
        actBar.setDisplayUseLogoEnabled(true);
        actBar.setDisplayShowHomeEnabled(true);
        actBar.setBackgroundDrawable(new ColorDrawable(0xFF505050));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // 設定 action views
        Spinner spnRegion = (Spinner)
                menu.findItem(R.id.menuItemRegion).getActionView()
                        .findViewById(R.id.spnRegion);
        ArrayAdapter<CharSequence> adapRegionList = ArrayAdapter.createFromResource(
                this, R.array.spnRegionList, android.R.layout.simple_spinner_item);
        spnRegion.setAdapter(adapRegionList);
        spnRegion.setOnItemSelectedListener(spnRegionOnItemSelected);

        SearchView searchView = (SearchView)
                menu.findItem(R.id.menuItemSearch).getActionView();
        searchView.setOnQueryTextListener(searchViewOnQueryTextLis);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_PLAY_MUSIC:
                Intent it = new Intent(MainActivity.this, MediaPlayService.class);
                startService(it);
                return true;
            case MENU_STOP_PLAYING_MUSIC:
                it = new Intent(MainActivity.this, MediaPlayService.class);
                stopService(it);
                return true;
            case MENU_ABOUT:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("關於這個程式")
                        .setMessage("選單範例程式")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.star_big_on)
                        .setPositiveButton("確定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                        .show();

                return true;
            case MENU_EXIT:
                finish();
                return true;
            case R.id.menuItemRegion:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("選擇地區")
                        .setMessage("這是選擇地區對話盒")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.star_big_on)
                        .setPositiveButton("確定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                        .show();

                return true;
            case R.id.menuItemSearch:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("搜尋")
                        .setMessage("這是搜尋對話盒")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.star_big_on)
                        .setPositiveButton("確定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                        .show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Spinner.OnItemSelectedListener spnRegionOnItemSelected =
            new Spinner.OnItemSelectedListener () {
                public void onItemSelected(AdapterView parent,
                                           View v,
                                           int position,
                                           long id) {
                    Toast.makeText(MainActivity.this, parent.getSelectedItem().toString(),
                            Toast.LENGTH_LONG).show();
                }
                public void onNothingSelected(AdapterView parent) {
                }
            };

    private SearchView.OnQueryTextListener searchViewOnQueryTextLis = new
            SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Toast.makeText(MainActivity.this, query, Toast.LENGTH_LONG).show();

                    return true;
                }
            };

}
