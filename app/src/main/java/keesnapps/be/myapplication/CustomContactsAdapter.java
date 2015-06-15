package keesnapps.be.myapplication;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomContactsAdapter extends ArrayAdapter<Contact> {
    public CustomContactsAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_contact, parent, false);
        }
        // Lookup view for data population
        TextView lNaam = (TextView) convertView.findViewById(R.id.lNaam);
        TextView lNummer = (TextView) convertView.findViewById(R.id.lNummer);
        TextView lId = (TextView) convertView.findViewById(R.id.lId);
        TextView lSchuld = (TextView) convertView.findViewById(R.id.lSchuld);
        TextView lDatum = (TextView) convertView.findViewById(R.id.lDatum);
        // Populate the data into the template view using the data object
        lNaam.setText(contact.getNaam());
        lNummer.setText(contact.getNummer());
        lId.setText(contact.getId()+" ");
        lSchuld.setText(contact.getSchuld());
        lDatum.setText(contact.getDatum());

        // Return the completed view to render on screen
        return convertView;
    }
}