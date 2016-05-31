package org.fablabsantiago.smartcities.app.lebikee;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class FakeDataBase
{
    public static Destination createDestinationObject(String name, String displayName)
    {
        Destination destination = null;

        switch(name)
        {
            case "casa":
                Log.i("FakeDataBase","switch case 'casa'");
                destination = new Destination(name, displayName, new LatLng(-33.4126792,-70.5986989));
                Log.i("FakeDataBase","switch case 'casa': adding hotspots");
                destination.addPositiveHotspot(new LatLng(-33.436836, -70.610898), true, "Ciclovía", "Ciclovía en  buen estado");
                destination.addPositiveHotspot(new LatLng(-33.415570, -70.599686), true, "Bien", "Todo tranquilo :).");
                destination.addNegativeHotspot(new LatLng(-33.439271, -70.621557), false, "Cruce Peligroso", "Se cruzan autos por al frente.");
                destination.addNegativeHotspot(new LatLng(-33.432028, -70.602107), false, "Autos Asesinos", "Pasan muy rápido y la calle es angosta.");
                break;
            case "beauchef850":
                Log.i("FakeDataBase","switch case 'beauchef850'");
                destination = new Destination(name, displayName, new LatLng(-33.4577491, -70.6634021));
                destination.addPositiveHotspot(new LatLng(-33.450757, -70.645864), true, "Ciclovía", "Buena ciclovía, rápida.");
                destination.addNegativeHotspot(new LatLng(-33.452959, -70.657371), false, "Cruce Peligroso", "Mucho auto y poca señalización.");
                destination.addNegativeHotspot(new LatLng(-33.448725, -70.637259), false, "Vía en mal estado", "La ciclovía esta rota con grandes baches en ciertos lugares.");
                break;
            case "estacionmapocho":
                Log.i("FakeDataBase","switch case 'estacionmapocho'");
                destination = new Destination(name, displayName, new LatLng(-33.432336,-70.653274));
                break;
            default:
                Log.i("FakeDataBase","destionationLatLng switch Error - invalid 'name'");
        }



        /*
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
        */

        return destination;

    }

}
