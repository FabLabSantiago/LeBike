package org.fablabsantiago.smartcities.app.lebikee;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class LeBikeActivity extends AppCompatActivity //implements OnMapReadyCallback
{
    protected final Context context = this;

    HashMap<String, String> hmap = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i("LeBikeActivity","onCreate in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lebike);

        // Inicializamos los destinos
        hmap.put("Casa","casa");
        hmap.put("Beauchef 850","beauchef850");
        hmap.put("Estación Mapocho","estacionmapocho");
    }

    @Override
    protected void onStart()
    {
        Log.i("LeBikeActivity","onStart - in");
        super.onStart();
        final ListView patrimoniosListView = (ListView) findViewById(R.id.destinationsList);
        Set<String> destinationsSet = hmap.keySet();
        String[] patrimonios = new String[3];
        patrimonios = destinationsSet.toArray(patrimonios);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,android.R.id.text1,patrimonios);
        patrimoniosListView.setAdapter(adapter);
        patrimoniosListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String  itemValue    = (String) patrimoniosListView.getItemAtPosition(position);
                Log.i("LeBikeActivity","onItemClick, item:" + itemValue);
                Intent onRouteMapIntent = new Intent(context, OnRouteMapActivity.class);
                onRouteMapIntent.putExtra("DESTINO",hmap.get(itemValue));
                onRouteMapIntent.putExtra("DISPLAY",itemValue);
                startActivity(onRouteMapIntent);
            }
        });

        // TODO: Crear clase ruta o destino.
        // Aquí se agrupan to-do lo realacionado a cada ruta. Tiempo de viaje, puntaje, hotspots,
        // nombre, etc...
    }

    /* Otro+ - OnClick */
    public void otroRoute(View view)
    {
        String infoText = "Option not available for the moment.\n" +
                        "Later you will be able to add new destinations and we will recomend the" +
                        " best route to get there. ";
        // TODO: add new personalized destinations
        // We will use google map apis:
        //     - Google Places API for Android
        //     - Google Maps Road API (web)
        //     - Google Maps Directions API (web)
        // For now we will just have 3 destinations with its routes predefined. We will be able to
        // add event in the route and the notifications have to be enabled.
        Toast.makeText(this,infoText,Toast.LENGTH_LONG).show();
    }
}
