package org.fablabsantiago.smartcities.app.lebikee;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class StartActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void startLeBike(View view)
    {
        Log.i("MainActivity","startLeBike entered");
        Intent leBikeIntent = new Intent(this, LeBikeActivity.class);
        startActivity(leBikeIntent);
    }

    public void startLePatrimonios(View view)
    {
        Log.i("MainActivity","startLePatrimonios entered");
        Intent lePatrimoniosIntent = new Intent(this, LePatrimoniosActivity.class);
        startActivity(lePatrimoniosIntent);
    }
}
