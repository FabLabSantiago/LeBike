package org.fablabsantiago.smartcities.app.appmobile;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    }

    @Override
    protected void onStart()
    {
        Log.i("LeBikeActivity","onStart - in");
        super.onStart();

        final ListView destinosListView = (ListView) findViewById(R.id.destinationsList);
        DondeVasAdapter adapter = new DondeVasAdapter(this, FakeDataBase.getDestinos());
        destinosListView.setAdapter(adapter);
        destinosListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String  itemValue    = (String) destinosListView.getItemAtPosition(position);
                Log.i("LeBikeActivity","onItemClick, item: " + itemValue + "(end)");
                if (itemValue.equals("Casa") || itemValue.equals("Beauchef 850") || itemValue.equals("Estacion Mapocho"))
                {
                    Intent onRouteMapIntent = new Intent(context, OnRouteMapActivity.class);
                    onRouteMapIntent.putExtra("DESTINO", itemValue);
                    startActivity(onRouteMapIntent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Destino de demostración. Presione alguno de los primeros 3.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // TODO: Crear clase ruta o destino.
        // Aquí se agrupan to-do lo realacionado a cada ruta. Tiempo de viaje, puntaje, hotspots,
        // nombre, etc...
    }

    /* Otro+ - OnClick */
    public void otroRoute(View view)
    {
        String infoText = "Later you will be able to add new destinations and we will recomend the" +
                        " best route to get there. ";
        // TODO: add new personalized destinations
        // We will use google map apis:
        //     - Google Places API for Android
        //     - Google Maps Road API (web)
        //     - Google Maps Directions API (web)
        // For now we will just have 3 destinations with its routes predefined. We will be able to
        // add event in the route and the notifications have to be enabled.
        Toast.makeText(this,infoText,Toast.LENGTH_LONG).show();

        Intent misDestinosIntent = new Intent(this, MisDestinosActivity.class);
        misDestinosIntent.putExtra("REQUESTING_NEW_DESTINATION",true);
        startActivity(misDestinosIntent);
    }

    public void misDestinos(View view)
    {
        Log.i("LeBikeActivity","onMisDestinosOnClick entered");
        Intent misDestinosIntent = new Intent(this, MisDestinosActivity.class);
        startActivity(misDestinosIntent);
    }

    public void misAlertas(View view)
    {
        Log.i("LeBikeActivity","onMisAlertas entered");
        Intent misAlertasIntent = new Intent(this, MisAlertasActivity.class);
        startActivity(misAlertasIntent);
    }
}
