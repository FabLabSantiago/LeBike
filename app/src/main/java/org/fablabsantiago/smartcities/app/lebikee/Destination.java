package org.fablabsantiago.smartcities.app.lebikee;


import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Destination
{
    /* Fields
	 *
	 */
    protected Context context;
    public String name;
    public String displayName;
    public LatLng latLng;
    public List<LatLng> posHotspots;
    public List<LatLng> negHotspots;
    public List<String> posHotspotsName;
    public List<String> negHotspotsName;
    public List<String> posHotspotsDesc;
    public List<String> negHotspotsDesc;
    public List<String> posHotspotsDateTime;
    public List<String> negHotspotsDateTime;


    /* Constructor
	 *
	 */
    public Destination(String nm, String dNm, LatLng latlng)
    {
        Log.i("Destination","constructor");
        name                 = nm;
        displayName          = dNm;
        latLng               = latlng;
        posHotspots          = new ArrayList<LatLng>();
        negHotspots          = new ArrayList<LatLng>();
        posHotspotsName      = new ArrayList<String>();
        negHotspotsName      = new ArrayList<String>();
        posHotspotsDesc      = new ArrayList<String>();
        negHotspotsDesc      = new ArrayList<String>();
        posHotspotsDateTime  = new ArrayList<String>();
        negHotspotsDateTime  = new ArrayList<String>();
    }


    /* Methods
	 *
	 */
    protected List<LatLng> getRoute(Context context, int routeNum)
    {
        Log.i("Destination","getRoute - in");
        List<LatLng> list = new ArrayList<LatLng>();
        try
        {
            String routeName = "fablab_" + name + "_" + Integer.toString(routeNum) + ".gpx";
            InputStream inStream = context.getAssets().open(routeName);
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

    public List<LatLng> getPositiveHotspots()
    {

        return null;
    }

    public List<LatLng> getNegativeHotspots()
    {

        return null;
    }

    public void addPositiveHotspot(LatLng hotspot, String name, String description)
    {
        Log.i("Destination","addPositiveHotspot - in");

        DateFormat date = new SimpleDateFormat("yyyyMMdd-HHmmss");
        addPositiveHotspot(hotspot, name, description, date.format(new Date()));

        Log.i("Destination","addPositiveHotspot - end");
    }

    public void addPositiveHotspot(LatLng hotspot, String name, String description, String date)
    {
        Log.i("Destination","addPositiveHotspot(+date) - in" + hotspot.toString());

        posHotspots.add(hotspot);
        posHotspotsName.add(name);
        posHotspotsDesc.add(description);
        posHotspotsDateTime.add(date);

        Log.i("Destination","addPositiveHotspot(+date) - end");
    }

    public void addNegativeHotspot(LatLng hotspot, String name, String description)
    {
        Log.i("Destination","addNegativeHotspot - in");

        DateFormat date = new SimpleDateFormat("yyyyMMdd-HHmmss");
        addNegativeHotspot(hotspot, name, description, date.format(new Date()));
    }

    public void addNegativeHotspot(LatLng hotspot, String name, String description, String date)
    {
        negHotspots.add(hotspot);
        negHotspotsName.add(name);
        negHotspotsDesc.add(description);
        negHotspotsDateTime.add(date);
    }

    public int getPositiveHospotNumber()
    {
        Log.i("Destination","getPositiveHotspotNumber - in");
        return posHotspots.size();
    }

    public int getNegativeHospotNumber()
    {
        Log.i("Destination","getNegativeHotspotNumber - in");
        return negHotspots.size();
    }





    /* Auxiliar Functions
     *
     */
    protected List<LatLng> parseRoute(InputStream inputStream)
    {
        List<LatLng> routePoints = new ArrayList<LatLng>();
        try
        {
            Log.i("Destination","parseRoute: parse: Begining xml parsing test");

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
            Log.i("Destination","parseRoute: parse: End document");
        }
        catch (XmlPullParserException e)
        {
            Log.i("Destination","parseRoute: error en xml pull parser");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            Log.i("Destination","parseRoute: error getting route1.xml");
            e.printStackTrace();
        }

        return routePoints;
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
                    //mMap.addPolyline(new PolylineOptions().add(new LatLng(-33.449796, -70.6277000), new LatLng(-33.432336,-70.653274)));
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
}
