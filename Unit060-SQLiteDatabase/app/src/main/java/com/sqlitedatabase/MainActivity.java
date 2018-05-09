package com.sqlitedatabase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String DB_FILE = "friends.db",
            DB_TABLE = "friends";
    private SQLiteDatabase mFriendDb;

    private EditText mEdtName,
                    mEdtSex,
                    mEdtAddr,
                    mEdtList;

    private Button mBtnAdd,
                    mBtnQuery,
                    mBtnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FriendDbOpenHelper friendDbOpenHelper =
                new FriendDbOpenHelper(getApplicationContext(), DB_FILE, null, 1);
        mFriendDb = friendDbOpenHelper.getWritableDatabase();

        // 檢查資料表是否已經存在，如果不存在，就建立一個。
        Cursor cursor = mFriendDb.rawQuery(
                "select DISTINCT tbl_name from sqlite_master where tbl_name = '" +
                        DB_TABLE + "'", null);

        if(cursor != null) {
            if(cursor.getCount() == 0)	// 沒有資料表，要建立一個資料表。
                mFriendDb.execSQL("CREATE TABLE " + DB_TABLE + " (" +
                        "_id INTEGER PRIMARY KEY," +
                        "name TEXT NOT NULL," +
                        "sex TEXT," +
                        "address TEXT);");

            cursor.close();
        }

        mEdtName = (EditText)findViewById(R.id.edtName);
        mEdtSex = (EditText)findViewById(R.id.edtSex);
        mEdtAddr = (EditText)findViewById(R.id.edtAddr);
        mEdtList = (EditText)findViewById(R.id.edtList);

        mBtnAdd = (Button)findViewById(R.id.btnAdd);
        mBtnQuery = (Button)findViewById(R.id.btnQuery);
        mBtnList = (Button)findViewById(R.id.btnList);

        mBtnAdd.setOnClickListener(btnAddOnClick);
        mBtnQuery.setOnClickListener(btnQueryOnClick);
        mBtnList.setOnClickListener(btnListOnClick);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFriendDb.close();
    }

    private View.OnClickListener btnAddOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ContentValues newRow = new ContentValues();
            newRow.put("name", mEdtName.getText().toString());
            newRow.put("sex", mEdtSex.getText().toString());
            newRow.put("address", mEdtAddr.getText().toString());
            mFriendDb.insert(DB_TABLE, null, newRow);
        }
    };

    private View.OnClickListener btnQueryOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Cursor c = null;

            if (!mEdtName.getText().toString().equals("")) {
                c = mFriendDb.query(true, DB_TABLE, new String[]{"name", "sex",
                        "address"}, "name=" + "\"" + mEdtName.getText().toString()
                        + "\"", null, null, null, null, null);
            } else if (!mEdtSex.getText().toString().equals("")) {
                c = mFriendDb.query(true, DB_TABLE, new String[]{"name", "sex",
                        "address"}, "sex=" + "\"" + mEdtSex.getText().toString()
                        + "\"", null, null, null, null, null);
            } else if (!mEdtAddr.getText().toString().equals("")) {
                c = mFriendDb.query(true, DB_TABLE, new String[]{"name", "sex",
                        "address"}, "address=" + "\"" + mEdtAddr.getText().toString()
                        + "\"", null, null, null, null, null);
            }

            if (c == null)
                return;

            if (c.getCount() == 0) {
                mEdtList.setText("");
                Toast.makeText(MainActivity.this, "沒有這筆資料", Toast.LENGTH_LONG)
                        .show();
            } else {
                c.moveToFirst();
                mEdtList.setText(c.getString(0) + c.getString(1)  + c.getString(2));

                while (c.moveToNext())
                    mEdtList.append("\n" + c.getString(0) + c.getString(1) +
                            c.getString(2));
            }
        }
    };

    private View.OnClickListener btnListOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Cursor c = mFriendDb.query(true, DB_TABLE, new String[]{"name", "sex",
                    "address"}, 	null, null, null, null, null, null);

            if (c == null)
                return;

            if (c.getCount() == 0) {
                mEdtList.setText("");
                Toast.makeText(MainActivity.this, "沒有資料", Toast.LENGTH_LONG)
                        .show();
            }
            else {
                c.moveToFirst();
                mEdtList.setText(c.getString(0) + c.getString(1)  + c.getString(2));

                while (c.moveToNext())
                    mEdtList.append("\n" + c.getString(0) + c.getString(1)  +
                            c.getString(2));
            }
        }
    };
}
