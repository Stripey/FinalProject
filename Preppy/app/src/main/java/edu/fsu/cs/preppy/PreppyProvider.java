package edu.fsu.cs.preppy;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

public class PreppyProvider extends ContentProvider {

    private static final String AUTHORITY = "edu.fsu.cs.preppy";
    private static final String TAG = PreppyProvider.class.getCanonicalName();
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private PreppyHelper mOpenHelper;
    public static final int DBVERSION = 1;
    public final static String DBNAME = "Preppy";

    // Meal table
    public final static String TABLE_MEAL = "Meal";
    public final static String NAME = "NAME"; // integer
    public final static String LENGTH_IN_DAYS = "LENGTH_IN_DAYS"; // float
    public final static String INGREDIENTS = "INGREDIENTS"; // string
    private final static String CSV_SCHEMA = NAME + "," + LENGTH_IN_DAYS + "," + INGREDIENTS + "\n";

    private static final String SQL_CREATE_MAIN = "CREATE TABLE " + TABLE_MEAL + "( " + "_ID INTEGER PRIMARY KEY, " + NAME + " TEXT, " + LENGTH_IN_DAYS + " FLOAT, " + INGREDIENTS + " TIME " + ")";

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

    // https://stackoverflow.com/a/326440
    // idiomatic function for reading a file before java 11
    @RequiresApi(api = Build.VERSION_CODES.O)
    static String readFile(String path, Charset encoding) throws IOException {

        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        // See here: https://stackoverflow.com/questions/16733232/how-do-i-call-custom-method-in-contentprovider-through-contentresolver-and-acces
        // "How do I call custom method in ContentProvider through ContentResolver and access Bundle afterwards?"
        // This allows me to delegate function calls to the corresponding member functions when
        // the .call() API (added in Api 11, I think) is used
        Bundle mealDataBundle = new Bundle();

        switch (method) {
            case "dump":
                assert arg != null;
                boolean dumpSucceeded = false;
                // arg is file path to dump contents of current database into file
                try {
                    dump(new File(arg));
                    dumpSucceeded = true;
                }
                catch  (NoSuchFileException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                mealDataBundle.putBoolean("succeeded", dumpSucceeded);
                break;
            case "dumps":
                // no arg required here, just writing database contents to a string
                mealDataBundle.putString("csvString", dumps());
                break;
            case "load":
                assert arg != null;
                boolean loadSucceeded = false;
                // arg should be a path to the file load into the database
                // file should contain a valid csv string
                try {
                    loads(readFile(arg, StandardCharsets.US_ASCII));
                    loadSucceeded = true;
                }
                catch  (NoSuchFileException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                mealDataBundle.putBoolean("succeeded", loadSucceeded);
                break;
            case "loads":
                assert arg != null;
                // arg here should be a valid csv string to insert into the database
                loads(arg);
                break;
        }

        return mealDataBundle;
    }

    // Write/"dump" the database to a CSV file
    public void dump(File dest) throws IOException {

        if (dest.exists()) {
            Log.i(TAG, "Destination file already exists");
            return;
        }

        FileWriter fw = new FileWriter(dest);
        fw.write(dumps());
        fw.flush();
        fw.close();
    }

    // Load from a CSV file and store the rows as rows in the Meal table
    public void load(File src) {

        if (!src.exists()) {
            Log.i(TAG, "Source file does not exist");
            return;
        }

        try {
            BufferedReader fr = new BufferedReader(new FileReader(src));
            StringBuilder csv_sb = new StringBuilder();
            String buffer;

            buffer = fr.readLine();

            // ensure source file is not empty
            if (buffer == null) {
                Log.i(TAG, "Source file is empty");
                fr.close();
                return;
            }
            // verify first row of non-empty file contains the proper row schema
            else if (!buffer.equals(CSV_SCHEMA)) {
                Log.i(TAG, "Source file's row schema is incorrect");
                fr.close();
                return;
            }
            // save csv schema since `loads` function expects it
            csv_sb.append(buffer);

            // read the remaining rows of csv files as data to insert
            while ((buffer = fr.readLine()) != null) {
                csv_sb.append(buffer);
            }

            // store into database
            loads(csv_sb.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Serialize/"dump" the rows of the database as a CSV-valid String
    public String dumps() {

        StringBuilder csv_sb = new StringBuilder(CSV_SCHEMA);

        // SELECT * FROM Meal;
        Cursor c = query(Uri.parse(""), null, null, null, null);

        // Add each row from the Meal table as a row to the csv StringBuilder
        if (c.moveToFirst()) {
            do {
                String row_name = c.getString(1);
                String row_length_in_days = c.getString(2);
                String row_ingredients = c.getString(3);

                csv_sb.append(row_name).append(",").append(row_length_in_days).append(",").append('"').append(row_ingredients).append('"').append("\n");
            } while (c.moveToNext());
        }
        c.close();

        // Return string/csv reprensentation of database
        return csv_sb.toString();
    }

    // Given a CSV-valid string, store each row as rows in the Meal table
    public void loads(String csv_input) {

        String[] rows = csv_input.split("\n", 1);

        if (!rows[0].equals(CSV_SCHEMA)) {
            Log.i(TAG, "Failed to load csv data, row schema incorrect");
            return;
        }

        for (String row : rows[1].split("\n")) {
            String[] values = row.split(",");

            ContentValues cv = new ContentValues();
            cv.put(NAME, values[0]);
            cv.put(LENGTH_IN_DAYS, Float.valueOf(values[1]));
            cv.put(INGREDIENTS, values[2]);

            insert(CONTENT_URI, cv);
            Log.i(TAG, "Inserted meal \"" + NAME + " \" into database.");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        long id = mOpenHelper.getWritableDatabase().insert(TABLE_MEAL, null, values);

        return Uri.withAppendedPath(CONTENT_URI, "" + id);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        return mOpenHelper.getWritableDatabase().update(TABLE_MEAL, values, selection, selectionArgs);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        return mOpenHelper.getWritableDatabase().delete(TABLE_MEAL, selection, selectionArgs);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        return mOpenHelper.getReadableDatabase().query(TABLE_MEAL, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        return null;
    }
}
