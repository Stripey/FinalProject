package edu.fsu.cs.preppy;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class PreppyProvider extends ContentProvider {
    private static final String authority = "edu.fsu.cs.preppy";
    public static final Uri CONTENT_URI = Uri.parse("content://" + authority);
    private String TAG = PreppyProvider.class.getCanonicalName();

    private PreppyHelper mOpenHelper;
    public static final int DBVERSION = 1;
    public final static String DBNAME = "Preppy";

    // Meal table
    public final static String TABLE_MEAL = "Meal";
    public final static String NAME = "NAME"; // integer
    public final static String LENGTH_IN_DAYS = "LENGTH_IN_DAYS"; // float
    public final static String INGREDIENTS_JSON = "INGREDIENTS_JSON"; // string, json

    private static final String SQL_CREATE_MAIN =
      "CREATE TABLE " + TABLE_MEAL + "( " +
        "_ID INTEGER PRIMARY KEY, " +
        NAME + " INTEGER, " +
        LENGTH_IN_DAYS + " FLOAT, " +
        INGREDIENTS_JSON + " TIME " +
        ")";

    // lesson 10 helper impl
    protected static final class PreppyHelper extends SQLiteOpenHelper {
        PreppyHelper(Context context) {
            super(context, DBNAME, null, DBVERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SQL_CREATE_MAIN);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        }
    }

    public PreppyProvider() {
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new PreppyHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = mOpenHelper
          .getWritableDatabase()
          .insert(TABLE_MEAL, null, values);

        return Uri.withAppendedPath(CONTENT_URI, "" + id);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return mOpenHelper
          .getWritableDatabase()
          .update(TABLE_MEAL, values, selection, selectionArgs);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return mOpenHelper
          .getWritableDatabase()
          .delete(TABLE_MEAL, selection, selectionArgs);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return mOpenHelper
          .getReadableDatabase()
          .query(TABLE_MEAL, projection, selection, selectionArgs,null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
