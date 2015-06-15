package keesnapps.be.myapplication.mainActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import keesnapps.be.myapplication.Contact;
import keesnapps.be.myapplication.ContactsDataSource;
import keesnapps.be.myapplication.CustomContactsAdapter;
import keesnapps.be.myapplication.R;


public class MainActivity extends Activity {

    Button buttonZoek;
    Button buttonAdd;
    Button buttonDelete;
    TextView naam;
    TextView nummer;
    TextView toonNaam;
    EditText bedrag;
    SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    ListView list;
    CustomContactsAdapter adapter;
    static final int PICK_CONTACT = 1;
    private ContactsDataSource datasource;
    String _id;
    String _naam;
    String _nummer;
    String _bedrag;
    String _datum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        naam = (TextView) findViewById(R.id.textNaam);
        nummer = (TextView) findViewById(R.id.textNummer);
        toonNaam = (TextView) findViewById(R.id.toonNaam);
        bedrag = (EditText) findViewById(R.id.bedrag);
        buttonZoek = (Button) findViewById(R.id.buttonZoek);
        final Context context = this;
        list = (ListView) findViewById(R.id.list);
        datasource = new ContactsDataSource(this);
        datasource.open();
        initGui();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                showOurDialog(parent,view,position,id);
            }


        });


// Construct the data source

    }

    private void showOurDialog(AdapterView<?> parent, final View view,
                          final int position, long id){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.click_contact_dialog);
        dialog.setTitle("");

        Button editButton = (Button) dialog.findViewById(R.id.dialogButtonEdit);
        Button removeButton = (Button) dialog.findViewById(R.id.dialogButtonDelete);
        // if button is clicked, close the custom dialog
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editContact(list,view,position);
                dialog.dismiss();
            }
        });
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeContact(list, view, position);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showEditDialog(final Contact contact, final View view, final int position){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.edit_contact_dialog);
        dialog.setTitle("make changes to edit contact");
        final EditText editNaam = (EditText) dialog.findViewById(R.id.edit_naam);
        editNaam.setText(contact.getNaam());
        final EditText editSchuld = (EditText) dialog.findViewById(R.id.edit_bedrag);
        editSchuld.setText(contact.getSchuld());
        final EditText editNummer = (EditText) dialog.findViewById(R.id.edit_nummer);
        editNummer.setText(contact.getNummer());
        Button buttonOk = (Button) dialog.findViewById(R.id.buttonOk);
        // if button is clicked, close the custom dialog
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact.setNummer(editNummer.getText().toString());
                contact.setNaam(editNaam.getText().toString());
                contact.setSchuld(editSchuld.getText().toString());
                datasource.updateContact(contact);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void editContact(ListView l, View v, int position) {
        CustomContactsAdapter adapter = (CustomContactsAdapter) list.getAdapter();
        Contact contact  = adapter.getItem(position);
        showEditDialog(contact,v,position);
        datasource.updateContact(contact);
        adapter.notifyDataSetChanged();
    }
    public void removeContact(ListView l, View v, int position) {
        CustomContactsAdapter adapter = (CustomContactsAdapter) list.getAdapter();
        Contact contact;
        contact = adapter.getItem(position);
        datasource.deleteContact(contact);
        adapter.remove(contact);
    }


    private void initGui() {
        ArrayList<Contact> arrayOfContacts = new ArrayList<Contact>(datasource.getAllContacts());
        Date date = new Date();
        Date now = new Date();
        for(Contact c: arrayOfContacts){
            try {
                date =  dateFormat.parse(c.getDatum());
                date.setMinutes(date.getMinutes()+10);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            System.out.println((date.compareTo(now)>=0 )? "juist": "fout") ;



            _datum = new String(dateFormat.format(date));

        }
// Create the adapter to convert the array to views
        adapter = new CustomContactsAdapter(this, arrayOfContacts);
// Attach the adapter to a ListView
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void addAction(View view) {

        Contact contact = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        _datum = new String(dateFormat.format(date));
        _bedrag = bedrag.getText().toString();
        contact = datasource.createContact(_naam, _bedrag, _nummer, _datum);
        adapter.add(contact);
        // SmsManager smsManager = SmsManager.getDefault();
        //smsManager.sendTextMessage(_nummer, null, "ge weet t goe genoeg je", null, null);
        adapter.notifyDataSetChanged();
        resetParams();
    }

    public void zoekAction(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }

    public void resetParams() {
        _datum = null;
        _naam = null;
        _datum = null;
        _bedrag = null;
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        _id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        _naam = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                        toonNaam.setText(_naam);
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + _id,
                                    null, null);
                            phones.moveToFirst();
                            _nummer = phones.getString(phones.getColumnIndex("data1"));
                        }
                        c.close();
                    }
                }

                break;
        }
    }

    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }


}

