package org.fablabsantiago.smartcities.app.lebikee;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class HotspotInfoAdapter implements GoogleMap.InfoWindowAdapter
{
    LayoutInflater inflater = null;
    Context context;
    private TextView textViewTitle;

    public HotspotInfoAdapter(Context cont, LayoutInflater inflater) {
        this.inflater = inflater;
        this.context = cont;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        Integer source = R.layout.infowindow_map_hotspot_positive;
        View v = new View(context);
        if (marker != null)
        {
            String snip = marker.getSnippet();
            if (snip.equals("negative"))
            {
                v = inflater.inflate(R.layout.infowindow_map_hotspot_negative, null);
            }
            else if (snip.equals("positive"))
            {
                v = inflater.inflate(R.layout.infowindow_map_hotspot_positive, null);
            }
        }

        return (v);
    }

    @Override
    public View getInfoContents(Marker marker) {
        return (null);
    }
}
