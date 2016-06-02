package org.fablabsantiago.smartcities.app.appmobile;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MisAlertasActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misalertas);

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Destination casa = FakeDataBase.createDestinationObject("Casa");

        ArrayList<String> alertas = new ArrayList<>(casa.posHotspotsName);

        final ListView alertasListView = (ListView) findViewById(R.id.misalertas_listview);
        MisAlertasAdapter adapter = new MisAlertasAdapter(this, alertas);
        alertasListView.setAdapter(adapter);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Toast.makeText(this, "Vista de Ejemplo", Toast.LENGTH_LONG).show();

        Toast.makeText(this, "Vista de Ejemplo", Toast.LENGTH_LONG).show();
    }

    public void nuevaAlertazi(View view)
    {
        String infoText = "Pronto serás capaz de agregar tus alertas para que todo sepan que esta ocurriendo.";
        Toast.makeText(this,infoText,Toast.LENGTH_LONG).show();

        //editDestination();
    }

    /*public void editarMiDestino(View view)
    {
        Toast.makeText(this, "Editando ... .-.", Toast.LENGTH_SHORT).show();

        editDestination();
    }

    protected void editDestination()
    {
        LinearLayout subwindow = (LinearLayout) findViewById(R.id.edit_destination_subwindow);
        ListView destinosListx = (ListView) findViewById(R.id.misdestinos_listview);

        subwindow.setVisibility(View.VISIBLE);
        destinosListx.setClickable(false);
    }

    public void closeNewDestination(View view)
    {
        LinearLayout subwindow = (LinearLayout) findViewById(R.id.edit_destination_subwindow);
        ListView destinosListx = (ListView) findViewById(R.id.misdestinos_listview);

        subwindow.setVisibility(View.GONE);
        destinosListx.setClickable(true);
    }*/
}


//public class MisAlertasActivity extends AppCompatActivity
//import android.os.Bundle;
//        import android.app.ActionBar;
//        import android.app.FragmentTransaction;
//        import android.support.v4.app.FragmentActivity;
//        import android.support.v4.view.ViewPager;
//
//public class MainActivity extends FragmentActivity
//{
//    ViewPager Tab;
//    TabPagerAdapter TabAdapter;
//    ActionBar actionBar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
//
//        Tab = (ViewPager)findViewById(R.id.pager);
//        Tab.setOnPageChangeListener(
//                new ViewPager.SimpleOnPageChangeListener() {
//                    @Override
//                    public void onPageSelected(int position) {
//
//                        actionBar = getActionBar();
//                        actionBar.setSelectedNavigationItem(position);                    }
//                });
//        Tab.setAdapter(TabAdapter);
//
//        actionBar = getActionBar();
//        //Enable Tabs on Action Bar
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//        ActionBar.TabListener tabListener = new ActionBar.TabListener(){
//
//            @Override
//            public void onTabReselected(android.app.ActionBar.Tab tab,
//                                        FragmentTransaction ft) {
//                // TODO Auto-generated method stub
//
//            }
//
//            @Override
//            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//
//                Tab.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(android.app.ActionBar.Tab tab,
//                                        FragmentTransaction ft) {
//                // TODO Auto-generated method stub
//
//            }};
//        //Add New Tab
//        actionBar.addTab(actionBar.newTab().setText("Android").setTabListener(tabListener));
//        actionBar.addTab(actionBar.newTab().setText("iOS").setTabListener(tabListener));
//        actionBar.addTab(actionBar.newTab().setText("Windows").setTabListener(tabListener));
//
//    }
//
//}


//public class MisAlertasActivity extends TabActivity implements TabHost.OnTabChangeListener
//{
//
//    /** Called when the activity is first created. */
//    TabHost tabHost;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState)
//    {
//        Log.i("MisAlertasActivity","onCreate - in");
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_misalertas);
//
//
//        Log.i("MisAlertasActivity","onCreate: content view configured");
//
//        // Get TabHost Refference
//        tabHost = getTabHost();
//
//        // Set TabChangeListener called when tab changed
//        tabHost.setOnTabChangedListener(this);
//
//        TabHost.TabSpec spec;
//        Intent intent;
//
//        /************* TAB1 ************/
//        // Create  Intents to launch an Activity for the tab (to be reused)
//        intent = new Intent().setClass(this, MisAlertasCompletasTab.class);
//        spec = tabHost.newTabSpec("First").setIndicator("")
//                .setContent(intent);
//
//        //Add intent to tab
//        tabHost.addTab(spec);
//
//        /************* TAB2 ************/
//        intent = new Intent().setClass(this, MisAlertasPendientesTab.class);
//        spec = tabHost.newTabSpec("Second").setIndicator("")
//                .setContent(intent);
//        tabHost.addTab(spec);
//
//        /************* TAB3 ************/
//        intent = new Intent().setClass(this, MisAlertasTodas.class);
//        spec = tabHost.newTabSpec("Third").setIndicator("")
//                .setContent(intent);
//        tabHost.addTab(spec);
//
//        // Set drawable images to tab
//        tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.ic_camara_gris);
//        tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.ic_ciclovia_grey);
//
//        // Set Tab1 as Default tab and change image
//        tabHost.getTabWidget().setCurrentTab(0);
//        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.ic_peatones_black);
//
//
//    }
//
//    @Override
//    public void onTabChanged(String tabId) {
//
//        /************ Called when tab changed *************/
//
//        //********* Check current selected tab and change according images *******/
//
//        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
//        {
//            if(i==0)
//                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.ic_camara_gris);
//            else if(i==1)
//                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.ic_ciclovia_grey);
//            else if(i==2)
//                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.ic_peatones_grey);
//        }
//
//
//        Log.i("tabs", "CurrentTab: "+tabHost.getCurrentTab());
//
//        if(tabHost.getCurrentTab()==0)
//            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.ic_camara_gris);
//        else if(tabHost.getCurrentTab()==1)
//            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.ic_ciclovia_grey);
//        else if(tabHost.getCurrentTab()==2)
//            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.ic_peatones_black);
//
//    }
//
//}
