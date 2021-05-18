package pt.ulisboa.tecnico.cmov.shopist.Pantry;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.shopist.MainActivity;
import pt.ulisboa.tecnico.cmov.shopist.Pantry.Maps.MapsFragment;
import pt.ulisboa.tecnico.cmov.shopist.R;
import util.db.entities.Pantry;
import util.db.queryInterfaces.PantryDAO;
import util.db.queryInterfaces.PantryItem;
import util.main.PantryListAdapter;
import util.main.SharedClass;

public class PantryList extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private String name;
    PantryDAO pantryDAO;
    //private SharedClass sharedClass
    private ArrayList<PantryItem> pantryItemList;
    private PantryListAdapter pantryListAdapter;
    private SharedClass sc;
    private Pantry pantry;
    //private GMapLocationListener gMapLocationListener;
    LatLng myLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);

        sc = (SharedClass)getApplicationContext();
        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        pantryDAO = sc.instanceDb().pantryDAO();
        //gMapLocationListener = new GMapLocationListener(this);
        //handleMapLocation();
        AsyncTask.execute(this::showItemList);
        AsyncTask.execute(this::setMap);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(name);
        handleNewItemButton();
    }

/*
    private void handleMapLocation(){
        double myLocationLatitude = gMapLocationListener.getLocation().getLatitude();
        double myLocationLongitude = gMapLocationListener.getLocation().getLatitude();
        myLocation = new LatLng(myLocationLatitude, myLocationLongitude);
    }
*/

    private void handleNewItemButton(){
        FloatingActionButton newItemBtn = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        newItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddItemActivity();
            }
        });
    }

    private void openAddItemActivity(){
        Intent intent = new Intent(this, AddItemToPantry.class);
        intent.putExtra("PantryId", pantry.id);
        intent.putExtra("PantryName", pantry.name);
        startActivity(intent);
    }

    private void setMap(){
        pantry = pantryDAO.getPantry(name);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Log.w("PANTRY_LAT_LON", "latitude = " + pantry.latitude + " longitude = " + pantry.longitude);
        MapsFragment mapsFragment = new MapsFragment();

        mapsFragment.setArgs(pantry.latitude, pantry.longitude, this);

        transaction.replace(R.id.mapView, mapsFragment);
        transaction.commit();

    }

    private void showItemList(){
        //pantry = pantryDAO.getPantry(name);
        setPantryItemList();
        setArrayAdapter();
    }

    private void setPantryItemList(){
        pantryItemList = new ArrayList<>(pantryDAO.getAllItems(name));
    }

    private void setArrayAdapter(){
        pantryListAdapter = new PantryListAdapter(this, pantryItemList);
        ListView listView = (ListView) findViewById(R.id.ListViewPantryList);
        //need to run code that updates the UI in a separate thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                listView.setAdapter(pantryListAdapter);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    public Pantry getPantry() {
        return pantry;
    }
}