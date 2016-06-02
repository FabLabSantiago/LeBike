package org.fablabsantiago.smartcities.app.appmobile;

import android.app.Fragment;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

//public class MisAlertasCompletasTab extends Fragment
//{
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
//    {
//        return (LinearLayout) inflater.inflate(R.layout.tab_misalertas_completas, container, false);
//    }
//
//}

public class MisAlertasCompletasTab extends Activity
{
    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_misalertas_completas);
    }
}
