package org.fablabsantiago.smartcities.app.lebikee;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


public class OnRouteLocationService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{
    int mStartMode;       // indicates how to behave if the service is killed
    IBinder mBinder;      // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;

    @Override
    public void onCreate()
    {
        Log.i("OnRouteLocationService","onCreate - in");

        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // The service is starting, due to a call to startService()
        Log.i("OnRouteLocationService","onStartCommand - in");
        mGoogleApiClient.connect();
        return mStartMode;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // A client is binding to the service with bindService()
        Log.i("OnRouteLocationService","onBind - in");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        // All clients have unbound with unbindService()
        Log.i("OnRouteLocationService","onUnbind - in");
        return mAllowRebind;
    }
    @Override
    public void onRebind(Intent intent)
    {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
        Log.i("OnRouteLocationService","onRebind - in");
    }
    @Override
    public void onDestroy()
    {
        // The service is no longer used and is being destroyed
        Log.i("OnRouteLocationService","onDestroy - in");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
        mGoogleApiClient.disconnect();
    }

    /* |                                                 */
    /* |  Non service methods, location services related */
    /* \/                                                */
    @Override
    public void onConnected(Bundle bundle)
    {
        Log.i("OnRouteLocationService","onConnected - in");
        createLocationRequest();
        startLocationUpdates();
    }

    protected void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates()
    {
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
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        /*SharedPreferences leBikePrefs = getSharedPreferences("leBikePreferences",MODE_PRIVATE);
        boolean bTrackingRoute = leBikePrefs.getBoolean("BOOL_TRACKING_ROUTE",true); //si es true esta mal. Si no existe tambien esta mal
        if (!bTrackingRoute)
        {  //=false
            bTrackingRoute = true;
            SharedPreferences.Editor editor = leBikePrefs.edit();
            editor.putBoolean("BOOL_TRACKING_ROUTE",bTrackingRoute);
            editor.commit();
            Button trackRouteButton = (Button) findViewById();
        }
        else
        {
            Log.e("OnRouteLocationService","WARNING: Error in boolTrackingRoute value");
            Toast.makeText(this, "WARNING: Error in boolTrackingRoute value", Toast.LENGTH_SHORT).show();
        }*/

    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }

    @Override
    public void onLocationChanged(Location location)
    {
        Log.i("OnRouteLocationService","onLocationChanged, " + location.toString());
        Toast.makeText(this, location.toString(),Toast.LENGTH_SHORT).show();
    }
}
