package com.contentprovider.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class FriendsContentProvider extends ContentProvider {

    private static final String AUTHORITY =
            "com.contentprovider.providers.FriendsContentProvider";
    private static final String DB_FILE = "friends.db",
            DB_TABLE = "friends";
    private static final int URI_ROOT = 0,
            DB_TABLE_FRIENDS = 1;
    public static final Uri CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + DB_TABLE);
    private static final UriMatcher sUriMatcher = new UriMatcher(URI_ROOT);
    static {
        sUriMatcher.addURI(AUTHORITY, DB_TABLE, DB_TABLE_FRIENDS);
    }
    private SQLiteDatabase mFriendDb;

    @Override
    public boolean onCreate() {
        FriendDbOpenHelper friendDbOpenHelper = new FriendDbOpenHelper(
                getContext(), DB_FILE,
                null, 1);

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

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        if (sUriMatcher.match(uri) != DB_TABLE_FRIENDS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        Cursor c = mFriendDb.query(true, DB_TABLE, projection,
                selection, null, null, null, null, null);
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
       if (sUriMatcher.match(uri) != DB_TABLE_FRIENDS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        long rowId = mFriendDb.insert(DB_TABLE, null, contentValues);
        Uri insertedRowUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
        getContext().getContentResolver().notifyChange(insertedRowUri, null);

        return insertedRowUri;
    }

    @Override
    public int delete(Uri uri, String  selection, String[]  selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        return 0;
    }
}
