package keesnapps.be.myapplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ContactsDataSource {
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAAM,MySQLiteHelper.COLUMN_NUMMER,MySQLiteHelper.COLUMN_SCHULD,MySQLiteHelper.COLUMN_DATUM };

    public ContactsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Contact createContact(String naam,String schuld,String nummer, String datum) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAAM, naam);
        values.put(MySQLiteHelper.COLUMN_SCHULD, schuld);
        values.put(MySQLiteHelper.COLUMN_NUMMER,nummer);
        values.put(MySQLiteHelper.COLUMN_DATUM,datum);
        long insertId = database.insert(MySQLiteHelper.TABLE_SCHULDEN, null,
                values);

        Cursor cursor = database.query(MySQLiteHelper.TABLE_SCHULDEN,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Contact newContact = cursorToContact(cursor);
        cursor.close();

        return newContact;
    }

    public void deleteContact(Contact contact) {
        long id = contact.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_SCHULDEN, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void updateContact(Contact contact){
        long id = contact.getId();
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_SCHULD,contact.getSchuld());
        values.put(MySQLiteHelper.COLUMN_NUMMER,contact.getNummer());
        values.put(MySQLiteHelper.COLUMN_NAAM,contact.getNaam());
        database.update(MySQLiteHelper.TABLE_SCHULDEN,values,MySQLiteHelper.COLUMN_ID+ " = " + id, null);
    };

    public List<Contact> getAllContacts() {
        List<Contact> contacten = new ArrayList<Contact>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_SCHULDEN,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Contact contact = cursorToContact(cursor);
            contacten.add(contact);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return contacten;
    }

    private Contact cursorToContact(Cursor cursor) {
        Contact contact = new Contact();
        contact.setId(cursor.getInt(0));
        contact.setNaam(cursor.getString(1));
        contact.setNummer(cursor.getString(2));
        contact.setSchuld(cursor.getString(3));
        contact.setDatum(cursor.getString(4));
        return contact;
    }
}
