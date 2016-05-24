package org.fablabsantiago.smartcities.app.lebikee;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OnRouteMapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private String destinationName;
    private String destinationDisplayName;
    private Destination destino;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;

    private GoogleMap mMap;
    private LatLng destination;
    private LatLng fablabSCL;

    SharedPreferences leBikePrefs;
    boolean bTrackingRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i("OnRouteMapActivity","onCreate - in");
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

        Intent intent = getIntent();
        destino = FakeDataBase.createDestinationObject(intent.getStringExtra("DESTINO"), intent.getStringExtra("DISPLAY"));
        Log.i("OnRouteMapActivity","onCreate - end");

    }

    public void refreshUI(boolean trackingRoute)
    {
        Button trackRouteButton = (Button) findViewById(R.id.trackRouteButton);
        String buttonText = (trackingRoute)? "LLegué":"Ir ->";
        trackRouteButton.setText(buttonText);

        //Button bienButton = (Button) findViewById(R.id.bienButton);
        //bienButton.setText(String.format(destino.getPositiveHospotNumber()));
        //Button malButton = (Button) findViewById(R.id.malButton);
        //bienButton.setText(Integer.toString(destino.getNegativeHospotNumber()));
    }

    @Override
    protected void onStart()
    {
        Log.i("OnRouteMapActivity","onStart - in");
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop()
    {
        Log.i("OnRouteMapActivity","onStop - in");
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
        mMap = googleMap;

        Log.i("MainActivity:onMapReady", "in");
        //Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
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

        isLocationAvailable();

        //Setup initial focus view of the map and route markers
        //     Importante notar que en esta etapa del trabajo no estan incluidas ni se implementarán
        // funciones de detectar la ruta hacia un lugar específico (GMDirectionsApi) si no que se
        // asumiran tres destinos predefinidos y con ellos tres rutas predefinidas a cada uno. Pero
        // para esto debe haber también un punto de partida predefinido que por ahora será el fablab
        // (para la feria se puede definir la estación mapopocho y crear nuevas rutas predefinidas).
        //     Por eso puede que to-do esto no se justifique enteramente en esta etapa pero si es un
        // proceso que debe realizarce de todas maneras y así se hace, aunque en el fondo no estemos
        // usando nuestra localización.
        mCurrentLocation = new Location("");
        mCurrentLocation.setLatitude(-33.449796);
        mCurrentLocation.setLongitude(-70.6277000);

        fablabSCL = new LatLng(-33.449796, -70.6277000);

        if(mCurrentLocation == null)
        {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.458704, -70.643623), 10));
            return;
        }

        //Configure Map Options
        mMap.setMyLocationEnabled(true); //commented because of the explained bellow
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener()
        {
            @Override
            public boolean onMyLocationButtonClick()
            {
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(correctBounds(destino.latLng, fablabSCL),100));
                return true;
            }
        });
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //Load route from storage
        // TODO: to robust the code against failiures in xml parsing.

        List<LatLng> route1 = null;
        List<LatLng> route2 = null;
        try
        {
            //route1 = loadRoute("fablab_" + destinationName + "_2.gpx");
            //route2 = loadRoute("fablab_" + destinationName + "_1.gpx");
            route1 = destino.getRoute(this,1);
            route2 = destino.getRoute(this,2);
        }
        catch(Exception e)
        {
            Log.i("OnRouteMapActivity","onMapReady: in destination names. Verify hashmap values match the gpx file names");
        }

        // GUI elemens for the map
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(correctBounds(destino.latLng, fablabSCL),100));
        mMap.addMarker(new MarkerOptions()
                .position(fablabSCL)
                .title("Fablab Santiago")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.addMarker(new MarkerOptions()
                .position(destino.latLng)
                .title(destino.displayName)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        if(route1 != null && route2 != null)
        {
            mMap.addPolyline(new PolylineOptions().width((float) 5.0).color(Color.DKGRAY).addAll(route1));
            mMap.addPolyline(new PolylineOptions().width((float) 5.0).color(Color.GRAY).addAll(route2));
        }
        else
        {
            Toast.makeText(this,"Error loading route. Please contact: Mati",Toast.LENGTH_SHORT).show();
        }

        for(int i=0; i<destino.getPositiveHospotNumber(); i++)
        {
            mMap.addMarker(new MarkerOptions()
                    .position(destino.posHotspots.get(i))
                    .title(destino.posHotspotsName.get(i))
                    .snippet(destino.posHotspotsDesc.get(i))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }
        for(int i=0; i<destino.getNegativeHospotNumber(); i++)
        {
            mMap.addMarker(new MarkerOptions()
                    .position(destino.negHotspots.get(i))
                    .title(destino.negHotspotsName.get(i))
                    .snippet(destino.negHotspotsDesc.get(i))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }

    protected boolean isLocationAvailable()
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
            Log.i("OnRouteMapAcivity","isLocationAvaiable: no location permissions");
            return false;
        }
        //Check if location enabled       and notify
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                Toast.makeText(this,"Only network location is available. It may not be precise.",Toast.LENGTH_SHORT).show();
                return true;
            }

            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mCurrentLocation == null)
            {
                Toast.makeText(this,"Couldn't retrieve location. Try again later.",Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
        else
        {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mCurrentLocation == null)
            {
                Toast.makeText(this,"Enable location settings, please :)",Toast.LENGTH_SHORT).show();
                // TODO: desplegar menú para activar localización.
                return false;
            }
            else
            {
                Toast.makeText(this,"Location is being retrieved neither from gps nor from network.",Toast.LENGTH_SHORT).show();
                Log.i("OnRouteMapActivity","onMapReady: Location is being retrieved neither from gps nor from network.");
                return true;
            }
        }
    }

    protected LatLngBounds correctBounds(LatLng marker1, LatLng marker2)
    {
        //Works for south east hemisphere (With the ecuator and first meridian as reference).
        double m1Lat = marker1.latitude;
        double m1Lon = marker1.longitude;
        double m2Lat = marker2.latitude;
        double m2Lon = marker2.longitude;
        double swLat = m2Lat;
        double swLon = m2Lon;
        double neLat = m1Lat;
        double neLon = m1Lon;

        if (m1Lat - m2Lat < 0)
        {
            swLat = m1Lat;
            neLat = m2Lat;
        }
        if (m1Lon - m2Lon < 0)
        {
            swLon = m1Lon;
            neLon = m2Lon;
        }

        return new LatLngBounds(new LatLng(swLat,swLon), new LatLng(neLat,neLon));
    }

    protected List<LatLng> loadRoute(String routeName)
    {
        List<LatLng> list = new ArrayList<LatLng>();
        try
        {
            InputStream inStream = this.getAssets().open(routeName);
            // TODO: Idioma
            // Asset: Dicese del sustantivo en íngles que en el español se refiere a un
            //       valor, activo, acciones, recurso, ventaja, herramienta.
            list = parseRoute(inStream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return list;
    }

    public void fetchRoute()
    {
        Log.i("OnRouteMapActivity","fetchGpx - in");

        final List<LatLng> list = new ArrayList<LatLng>();
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                HttpURLConnection conn = null;
                try
                {
                    URL url = new URL("https://raw.githubusercontent.com/stereo92/leBikee/master/RecursosExternos/route1.2");
                    conn = (HttpURLConnection)url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    Log.i("OnRouteMapActivity","fetchGpx: http input stream, connect");

                    InputStream stream = conn.getInputStream();

                    List<LatLng> list = new ArrayList<LatLng>();
                    list = parseRoute(stream);
                    Log.i("OnRouteMapActivity","fetchGpx: route retrieved");
                    //mMap.addPolyline(new PolylineOptions().width((float) 5.0).color(Color.LTGRAY).addAll(list));
                    mMap.addPolyline(new PolylineOptions().add(new LatLng(-33.449796, -70.6277000), new LatLng(-33.432336,-70.653274)));
                    Log.i("OnRouteMapActivity","fetchGpx: polyline drawn");

                    stream.close();
                }
                catch (IOException e)
                {
                    Log.i("OnRouteMapActivity","fetchRoute: Error requesting gpx to server.");
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    protected List<LatLng> parseRoute(InputStream inputStream)
    {
        List<LatLng> routePoints = new ArrayList<LatLng>();
        try
        {
            Log.i("OnRouteMapActivity","onMapReady: parse: Begining xml parsing test");

            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser gpxParser = xmlFactoryObject.newPullParser();

            gpxParser.setInput(inputStream, null);

            /*Log.i("OnRouteMapActivity","onMapReady: parse: load complete:" + Integer.toString(inStream.read()) + " " +
                    Integer.toString(inStream.read()) + " " +
                    Integer.toString(inStream.read()) + " " +
                    Integer.toString(inStream.read()) + " " +
                    Integer.toString(inStream.read()) + "(ASCII values)");*/

            int eventType = gpxParser.getEventType();
            String tag;
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                // TODO: gpx parse can be optimized by documents structure
                // Se puede optimizar el parse del gpx pensando en como esta estructurado el gpx.
                // Por ejemplo una vez que ya estamos en trkpt se asume que el proximo será asi.
                if(eventType == XmlPullParser.START_TAG)
                {
                    tag = gpxParser.getName();
                    //Log.i("OnRouteMapActivity","onMapReady: parse: Start tag " + tag);
                    if (tag.equals("trkpt"))
                    {
                        String lat = gpxParser.getAttributeValue(null,"lat");
                        String lon = gpxParser.getAttributeValue(null,"lon");
                        //Log.i("OnRouteMapActivity","onMapReady: parse: " + lat + ", " + lon);
                        routePoints.add(new LatLng(Float.valueOf(lat), Float.valueOf(lon)));
                    }
                }
                eventType = gpxParser.next();
            }
            Log.i("OnRouteMapActivity","onMapReady: parse: End document");
        }
        catch (XmlPullParserException e)
        {
            Log.i("OnRouteMapActivity","onMapReady: error en xml pull parser");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            Log.i("OnRouteMapActivity","onMapReady: error getting route1.xml");
            e.printStackTrace();
        }

        return routePoints;
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
        // TODO: confirm correct service load from the service.
        // Actual implementation is blind to whats happening in the service.
        Intent locationService = new Intent(this, OnRouteLocationService.class);
        if (!bTrackingRoute)
        {
            if (isLocationAvailable())
            {
                Log.i("OnRouteMapActivity","onClick - comenzando servicio");
                startService(locationService);
                bTrackingRoute = true;
                // TODO: Check if location is available through the background service.
                // If location its shutted down while recording, push a notification warning.
            }
            else
            {
                Log.i("OnRouteMapActivity","onClick: ubicación no disponible");
            }
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

        // TODO: El estado del botón bien debe ser actualizado con memoria.
        // Guardar su estado y establecer su correcto estado al inicar de nuevo la aplicación.
        Button bien = (Button) findViewById(R.id.bienButton);
        bien.setEnabled(!bTrackingRoute);
        refreshUI(bTrackingRoute);

    }

    /* OnClick - Cargar ruta desde servidor, con error */
    public void malButtonOnClick(View view)
    {
        fetchRoute();
    }

    /* OnClick - Cargar ruta desde servidor, con error */
    public void bienButtonOnClick(View view)
    {
        SharedPreferences serviceRetrievedLocations = getSharedPreferences("SERVICE_RETRIEVED_LOCATIONS_SHARED_PREFERENCES",MODE_PRIVATE);
        Set<String> rutaGrabada = serviceRetrievedLocations.getStringSet("RUTA_SERVICE", null);
        for (String loc : rutaGrabada)
        {
            Log.i("OnRouteMapActivity","bienOnClick: ruta grabada: " + loc);
        }

        Toast.makeText(this,rutaGrabada.toString(),Toast.LENGTH_SHORT).show();
    }
}
