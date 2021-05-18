package pt.ulisboa.tecnico.cmov.shopist.Pantry;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.shopist.DialogAdd;
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

        mapsFragment.setArgs(pantry.latitude, pantry.longitude);
        mapsFragment.setMapsContext(this);

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
        listView.setAdapter(pantryListAdapter);
        listView.setItemsCanFocus(true);
        listView.setOnItemClickListener(new AdapterView .OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PantryItem selectedItem = (PantryItem) parent.getItemAtPosition(position);
                if(selectedItem.stock > 0){
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            consumeItem(selectedItem);
                        }
                    });
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                PantryItem selectedItem = (PantryItem) parent.getItemAtPosition(position);
                openDialog(selectedItem);
                return false;
            }
        });

    }

    public void openDialog(PantryItem pantryItem) {
        DialogRemoveItem dialogRemoveItem = new DialogRemoveItem();
        dialogRemoveItem.setPantryItem(pantryItem);
        dialogRemoveItem.show(getSupportFragmentManager(), "Dialog Remove");
    }

    public void deleteItemDialog(PantryItem pantryItemDel){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                  deleteItem(pantryItemDel);
            }
        });
    }

    private void deleteItem(PantryItem pantryItem){
        pantryDAO.removePantryItem(pantryItem.id);
        pantryItemList.remove(pantryItem);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pantryListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void consumeItem(PantryItem pantryItem){
        pantryItem.stock -= 1;
        util.db.entities.PantryItem pantryItemEntity = pantryDAO.getPantryItem(pantryItem.id);
        pantryItemEntity.stock -= 1;
        pantryDAO.updatePantryItem(pantryItemEntity);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pantryListAdapter.notifyDataSetChanged();
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