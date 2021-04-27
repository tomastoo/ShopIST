package pt.ulisboa.tecnico.cmov.shopist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import util.db.entities.Item;
import util.db.entities.PantryItem;
import pt.ulisboa.tecnico.cmov.shopist.PantryList;
import util.db.queryInterfaces.PantryDAO;
import util.main.SharedClass;

public class AddItemToPantry extends AppCompatActivity {
    SharedClass sc;
    PantryDAO pantryDao;
    int quantity;
    int stock;
    float price;
    String name;
    long pantryId;
    private String pantryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_to_pantry);
        Intent intent = getIntent();
        pantryId = intent.getLongExtra("PantryId", 0);
        pantryName = intent.getStringExtra("PantryName");


        sc = (SharedClass)getApplicationContext();
        pantryDao = sc.dbShopIst.pantryDAO();
        handleScannerButton();
        handleConfirmButton();

    }

    private void handleConfirmButton(){
        FloatingActionButton confirmBtn = (FloatingActionButton) findViewById(R.id.floatingActionButton_addItem);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout textInputLayout = findViewById(R.id.textInput_ItemName);
                Editable itemName = textInputLayout.getEditText().getText();
                if(itemName == null)
                    return;

                textInputLayout = findViewById(R.id.textInput_ItemPrice);
                Editable itemPrice = textInputLayout.getEditText().getText();
                if(itemPrice == null)
                    return;

                textInputLayout = findViewById(R.id.textInput_ItemQuantity);
                Editable itemQuantity = textInputLayout.getEditText().getText();
                if(itemQuantity == null)
                    return;

                textInputLayout = findViewById(R.id.textInput_ItemStock);
                Editable itemStock = textInputLayout.getEditText().getText();
                if(itemStock == null)
                    return;

                quantity = Integer.parseInt(itemQuantity.toString());
                stock = Integer.parseInt(itemStock.toString());

                if(stock > quantity)
                    return;

                name = itemName.toString();
                price = Float.parseFloat(itemPrice.toString());
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
        Item item = new Item();
        item.name = name;
        item.price = price;
        //TODO: e se o item ja existir?
        long itemId = pantryDao.insertItem(item);

        PantryItem pantryItem = new PantryItem();
        pantryItem.itemId = (int)itemId;
        pantryItem.pantryId = (int)pantryId;
        pantryItem.quantity = quantity;
        pantryItem.stock = stock;

        pantryDao.insertPantryList(pantryItem);
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
        startActivity(intent);
    }
}