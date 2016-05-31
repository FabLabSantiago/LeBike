package org.fablabsantiago.smartcities.app.lebikee;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class LePatrimoniosActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i("LePatrimoniosActivity","onCreate in");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lepatrimonios);

        //Configuramos la lista de patrimonios simple
        /* //Rara esta wea, si activo esta lista simple, el mapa de OnRouteMapActivity se cae ://.
        final ListView patrimoniosListView = (ListView) findViewById(R.id.patrimoniosListView);
        String[] patrimonios = new String[] {"Mi Casa",
                                             "Estación Mapocho",
                                             "Palacio La Moneda",
                                             "Iglesia San Fransisco",
                                             "Casa Rosada"};
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
                Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();
            }
        });
        */

    }

    public void activatePatrimoniosAlarm(View view)
    {
        Toast.makeText(this, "Se tiene que configurar la geofensa :)", Toast.LENGTH_LONG).show();
    }


}
