package pt.ulisboa.tecnico.cmov.shopist.Pantry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import util.ServerSync.ServerInterface;
import pt.ulisboa.tecnico.cmov.shopist.MainActivity;
import pt.ulisboa.tecnico.cmov.shopist.R;
import util.db.entities.PantryItem;
import util.db.entities.Shop;
import util.db.queryInterfaces.PantryDAO;
import util.main.SharedClass;
import util.main.ShopSelectionAdapter;

public class AddItemToPantry extends AppCompatActivity {
    private static int REQUEST_CODE = 1;
    private SharedClass sc;
    private PantryDAO pantryDao;
    private int quantity;
    private int stock;
    private Shop selectedShop;
    private String name;
    private int pantryId;
    private String pantryName;
    private ShopSelectionAdapter adapterShop;
    private String barcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_to_pantry);
        Intent intent = getIntent();
        pantryId = intent.getIntExtra("PantryId", 0);
        pantryName = intent.getStringExtra("PantryName");
        sc = (SharedClass)getApplicationContext();
        pantryDao = sc.instanceDb().pantryDAO();
        AsyncTask.execute(this::handleSpinnerShops);
        handleScannerButton();
        handleConfirmButton();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE && Activity.RESULT_OK == resultCode) {
            barcode = intent.getStringExtra("Barcode");
            Log.w("BARCODE", String.valueOf(barcode));
        }
    }
    private void handleSpinnerShops(){
        ArrayList<Shop> shopList = (ArrayList<Shop>) pantryDao.getAllShops();
        //get the spinner from the xml.
        Spinner spinner = findViewById(R.id.spinner_shops);
        adapterShop = new ShopSelectionAdapter(this, shopList);
        spinner.setAdapter(adapterShop);

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id)
                    {
                        selectedShop = (Shop) parent.getItemAtPosition(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }
                });
    }

    private void handleConfirmButton(){
        FloatingActionButton confirmBtn = (FloatingActionButton) findViewById(R.id.floatingActionButton_addItem);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout textInputLayout = findViewById(R.id.textInput_ItemName);
                Editable itemName = textInputLayout.getEditText().getText();
                if(itemName.length() <= 0)
                    return;

                textInputLayout = findViewById(R.id.textInput_ItemQuantity);
                Editable itemQuantity = textInputLayout.getEditText().getText();
                if(itemQuantity.length() <= 0)
                    return;

                textInputLayout = findViewById(R.id.textInput_ItemStock);
                Editable itemStock = textInputLayout.getEditText().getText();
                if(itemStock.length() <= 0)
                    return;

                if (selectedShop == null)
                    return;


                quantity = Integer.parseInt(itemQuantity.toString());
                stock = Integer.parseInt(itemStock.toString());

                if(stock > quantity)
                    return;

                name = itemName.toString();
                //price = Float.parseFloat(itemPrice.toString());
                asyncInsert();

            }
        });
    }

    private void asyncInsert(){
        AsyncTask.execute(this::insertItem);
        Intent intent = new Intent(this, PantryList.class);
        intent.putExtra(MainActivity.EXTRA_MESSAGE, pantryName);
        startActivity(intent);
    }

    private void insertItem(){
        PantryItem pantryItem = new PantryItem(pantryId, selectedShop.id, quantity, stock, name, barcode);
        pantryDao.insertPantryItem(pantryItem);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                adapterShop.notifyDataSetChanged();
            }
        });
    }

    private void handleScannerButton(){
        FloatingActionButton scannerBtn = (FloatingActionButton) findViewById(R.id.floatingActionButton_scan);
        scannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openItemScanActivity();
            }
        });
    }

    private void openItemScanActivity(){
        Intent intent = new Intent(this, ItemScan.class);
        startActivityForResult(intent, REQUEST_CODE);
    }
}