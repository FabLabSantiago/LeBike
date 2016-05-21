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
                "Mi Casa",
                "Estación Mapocho",
                "Casa Rosada",
                "Fablab",
                "Lomito Alemán"};
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
    }

    /* Otro+ - OnClick */
    public void otroRoute(View view)
    {
        int id = view.getId();
        Log.i("LeBikeActivity","startRoute, id:" + Integer.toString(id));
        Intent onRouteMapIntent = new Intent(this, OnRouteMapActivity.class);
        startActivity(onRouteMapIntent);
    }
}
