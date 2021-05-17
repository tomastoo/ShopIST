package pt.ulisboa.tecnico.cmov.shopist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class ItemScan extends AppCompatActivity {
    private int pantryId;
    private String pantryName;
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_scan);
        Intent intent = getIntent();
        pantryId = intent.getIntExtra("PantryId", 0);
        pantryName = intent.getStringExtra("PantryName");

        CodeScannerView scannerView = findViewById(R.id.scanner_view);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            mCodeScanner = new CodeScanner(this, scannerView);
        }
        else{
            openAddItem();
        }

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // link how to display the pop up layout https://developer.android.com/guide/topics/ui/dialogs#CustomLayout
                        String scanNumText = result.getText();
                        Toast.makeText(ItemScan.this, scanNumText, Toast.LENGTH_SHORT).show();
                        Intent intent = getIntent();
                        intent.putExtra("Barcode", scanNumText);
                        setResult(Activity.RESULT_OK, getIntent());
                        finish();
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    public void openAddItem(){
        Intent intent = new Intent(this, AddItemToPantry.class);
        intent.putExtra("PantryId", pantryId);
        intent.putExtra("PantryName", pantryName);
        startActivity(intent);
    }

    public void openAddItemWithScan(String barCode){
        Intent intent = new Intent(this, AddItemToPantry.class);
        intent.putExtra("PantryId", pantryId);
        intent.putExtra("PantryName", pantryName);
        int barCodeNum = Integer.parseInt(barCode);
        intent.putExtra("BarCode", barCode);
        startActivity(intent);
    }
}