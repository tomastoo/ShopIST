package pt.ulisboa.tecnico.cmov.shopist;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);

        sc = (SharedClass)getApplicationContext();
        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        pantryDAO = sc.dbShopIst.pantryDAO();

        AsyncTask.execute(this::showItemList);
        //AsyncTask.execute(this::setMap);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(name);
        handleNewItemButton();

    }
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
        startActivity(intent);
    }

    private void setMap(){
        Pantry pantry = pantryDAO.getPantry(name);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();


        MapsFragment mapsFragment = new MapsFragment(pantry.latitude, pantry.longitude);

        transaction.replace(R.id.mapView, mapsFragment);
        transaction.commit();
    }

    private void showItemList(){
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

}