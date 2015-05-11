package keesnapps.be.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_SCHULDEN = "schulden";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAAM = "naam";
    public static final String COLUMN_SCHULD = "schuld";
    public static final String COLUMN_NUMMER = "nummer";
    public static final String COLUMN_DATUM = "datum";
    private static final String DATABASE_NAME = "schulden.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table"
            + TABLE_SCHULDEN + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAAM
            + " text, " + COLUMN_SCHULD
            + " text, " + COLUMN_NUMMER
            + " text, " + COLUMN_DATUM
            + " text " +
            ");";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCHULDEN_TABEL = "CREATE TABLE " +
                TABLE_SCHULDEN + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAAM
                + " TEXT," + COLUMN_SCHULD + " TEXT," + COLUMN_NUMMER
                + " TEXT, " +COLUMN_DATUM + " TEXT"
                + ")";
        db.execSQL(CREATE_SCHULDEN_TABEL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHULDEN);
        onCreate(db);
    }

}