package org.fablabsantiago.smartcities.app.lebikee;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class OnRouteMapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;

    SharedPreferences leBikePrefs;
    boolean bTrackingRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onroutemap);

        // leBikePrefs has:
        //     - trackingRoute: true , si se esta grabando una ruta
        //                      falso, si no
        leBikePrefs = getSharedPreferences("leBikePreferences",MODE_PRIVATE);
        bTrackingRoute = leBikePrefs.getBoolean("BOOL_TRACKING_ROOT",false);
        refreshUI(bTrackingRoute);

        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void refreshUI(boolean trackingRoute)
    {
        Button trackRouteButton = (Button) findViewById(R.id.trackRouteButton);
        String buttonText = (trackingRoute)? "LLegué":"Ir ->";
        trackRouteButton.setText(buttonText);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        Log.i("OnRouteMapActivity", "onConnected - in");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /*                                                        */
    /*    onMapReady, here go all map configurations          */
    /*                                                        */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        Log.i("MainActivity:onMapReady", "in");
        //Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //Check if location enabled       and notify
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                Toast.makeText(this,"Only network location is available. It may not be precise.",Toast.LENGTH_SHORT).show();
            }

            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mCurrentLocation == null)
            {
                Toast.makeText(this,"Couldn't retrieve location. Try again later.",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mCurrentLocation == null)
            {
                Toast.makeText(this,"Enable location settings, please :)",Toast.LENGTH_SHORT).show();
                //TODO: desplegar menú para activar localización.
            }
            else
            {
                Toast.makeText(this,"Location is being retrieved neither from gps nor from network.",Toast.LENGTH_SHORT).show();
                Log.i("OnRouteMapActivity","onMapReady: Location is being retrieved neither from gps nor from network.");

            }
        }

        //Configure Map Options
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        //Setup initial focus view of the map
        if(mCurrentLocation == null)
        {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.458704, -70.643623), 10));
        }
        else
        {
            LatLng fabLabSCL = new LatLng(-33.449796, -70.6277000);
            LatLng initialFocusLocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(fabLabSCL).title("Fablab Santiago"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(fabLabSCL,initialFocusLocation),100));
        }
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.i("OnRouteMapActivity","onConnectionSuspended - in");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
        Log.i("OnRouteMapActivity","onConnectionFailed - in");
    }

    /* OnClick - Comenzar recorrido */
    public void readLocationInBackground(View view)
    {
        Intent locationService = new Intent(this, OnRouteLocationService.class);
        if (!bTrackingRoute)
        {
            Log.i("OnRouteMapActivity","onClick - comenzando servicio");
            startService(locationService);
            bTrackingRoute = true;

        }
        else
        {
            Log.i("OnRouteMapActivity","onClick - cerrando servicio");
            stopService(locationService);
            bTrackingRoute = false;
        }
        SharedPreferences.Editor editor = leBikePrefs.edit();
        editor.putBoolean("BOOL_TRACKING_ROOT",bTrackingRoute);
        editor.commit();
        refreshUI(bTrackingRoute);

    }
}
