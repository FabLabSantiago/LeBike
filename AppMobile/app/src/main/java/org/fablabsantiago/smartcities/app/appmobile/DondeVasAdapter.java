package org.fablabsantiago.smartcities.app.appmobile;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DondeVasAdapter extends ArrayAdapter<String>
{

    public DondeVasAdapter(Context context, ArrayList<String> destinos)
    {
        super(context, 0, destinos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String destino = getItem(position);

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_dondevas, parent, false);
        }

        TextView dondeVoy = (TextView) convertView.findViewById(R.id.dondevas_item_text);
        dondeVoy.setText(destino);
        dondeVoy.setTypeface(null, Typeface.BOLD);

        return convertView;
    }
}
