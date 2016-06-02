package org.fablabsantiago.smartcities.app.appmobile;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MisAlertasTodas extends Activity
{
    // Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_misalertas_todas);
    }
}
//{
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
//    {
//        return (LinearLayout) inflater.inflate(R.layout.tab_misalertas_pendientes, container, false);
//    }
//
//}