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

public class LeBikeActivity extends AppCompatActivity //implements OnMapReadyCallback
{
    protected final Context context = this;

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
        super.onStart();
        final ListView patrimoniosListView = (ListView) findViewById(R.id.destinationsList);
        String[] patrimonios = new String[] {
                "Casa",
                "Beauchef 850",
                "Estación Mapocho"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,android.R.id.text1,patrimonios);
        patrimoniosListView.setAdapter(adapter);
        patrimoniosListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                int itemPosition     = position;
                String  itemValue    = (String) patrimoniosListView.getItemAtPosition(position);
                Log.i("LeBikeActivity","onItemClick, item:" + itemValue);
                Intent onRouteMapIntent = new Intent(context, OnRouteMapActivity.class);
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
