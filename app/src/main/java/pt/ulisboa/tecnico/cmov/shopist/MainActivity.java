package pt.ulisboa.tecnico.cmov.shopist;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import util.db.Database;
import util.db.DatabaseShopIst;
import util.db.entities.Pantry;
import util.db.entities.PantryItem;
import util.main.MainAdapter;
import util.main.SharedClass;

public class MainActivity extends AppCompatActivity implements DialogAdd.DialogAddListener, ExpandableListView.OnChildClickListener {

    public static final String EXTRA_MESSAGE = "pt.ulisboa.tecnico.cmov.shopist.MESSAGE";

    private GpsTracker gpsTracker;

    private static MainActivity instance;
    // Main Activity Options
    ExpandableListView expandableListView;
    List<String> listGroup;
    HashMap<String, List<String>> listItem;
    MainAdapter adapter;
    List<PantryItem> pantryItems;
    private static boolean runOnce = false;
    //db stuff
    SharedClass sc;
    DatabaseShopIst db;
    boolean empty = false;

    // Global Variables
    String new_list_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        sc = (SharedClass) getApplicationContext();
        db = sc.instanceDb();


        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        expandableListView = findViewById(R.id.expand_activities_button);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        adapter = new MainAdapter(this, listGroup, listItem);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener(this);
        //AsyncTask.execute(this::updateLocalDB);
        sc.updateLocalDB();

        /*AsyncTask.execute(() -> {
            List<util.db.entities.Pantry> lists_pantry = db.pantryDAO().getAllPantryLists();
            if (lists_pantry.size() < 1){
                empty = true;
            }
        });
        if (empty){
            AsyncTask.execute(this::initListData);
        }*/
        if(!runOnce) {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Database.clearDatabase(sc);
                    Database.fillDatabase(sc);
                    initListData();
                }
            });
            runOnce = true;
        }
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public void initListData() {
        //update PantryList before making queries to de local DB

        /*listGroup = new ArrayList<>();
        listItem = new HashMap<>();*/

        listGroup.add(getString(R.string.group1));
        listGroup.add(getString(R.string.group2));
        listGroup.add(getString(R.string.group3));

        /*String[] array;
        /*List<String> list1 = new ArrayList<>();
        array = getResources().getStringArray(R.array.group1);
        for(String item : array){
            list1.add(item);
        }*/
        AsyncTask.execute(() -> {
            String[] array;
            List<String> list1 = new ArrayList<>();
            array = getResources().getStringArray(R.array.group1);
            for (String item : array) {
                list1.add(item);
            }
            listItem.put(listGroup.get(0), list1);
        });

        AsyncTask.execute(() -> {
            List<util.db.entities.Pantry> lists_pantry = db.pantryDAO().getAllPantryLists();
            List<String> list2 = new ArrayList<>();
            for (util.db.entities.Pantry item : lists_pantry) {
                list2.add(item.name);
            }

            list2.add("+");
            listItem.put(listGroup.get(1), list2);
        });

        AsyncTask.execute(() -> {
            List<String> list3 = new ArrayList<>();

            List<util.db.entities.Shop> lists_shopping = db.pantryDAO().getAllShops();

            for(util.db.entities.Shop item : lists_shopping){
                //TODO Fill time
                list3.add(item.name);
            }
            listItem.put(listGroup.get(2), list3);
        });

        /*listItem.put(listGroup.get(0), list1);
        listItem.put(listGroup.get(1), list2);
        listItem.put(listGroup.get(2), list3);*/
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Stuff that updates the UI
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        String item = (String) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
        /*Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();*/

        String group = (String) parent.getExpandableListAdapter().getGroup(groupPosition);
        //Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();

        switch (group) {
            case "Menu":
                Toast.makeText(getApplicationContext(), "A1", Toast.LENGTH_SHORT).show();
                break;
            case "Pantry Lists":
                if (item.equals("+")) {
                    new_list_type = "Pantry";
                    openDialog();
                } else {
                    Intent intent = new Intent(MainActivity.this, PantryList.class);
                    intent.putExtra(EXTRA_MESSAGE, item);
                    startActivity(intent);
                }
                break;
            case "Shopping Lists":
                if (item.equals("+")) {
                    new_list_type = "Shopping";
                    openDialog();
                } else {
                    Intent intent = new Intent(MainActivity.this, ShoppingList.class);
                    intent.putExtra(EXTRA_MESSAGE, item);
                    startActivity(intent);
                }
                break;
        }
        return false;
    }

    public void openDialog() {
        DialogAdd dialogAdd = new DialogAdd();
        dialogAdd.show(getSupportFragmentManager(), "Dialog Add");
    }

    @Override
    public void applyName(String name) {
        List<String> list;
        switch (new_list_type) {
            case "Shopping":
/*
                util.db.entities.ShoppingList shoppingList = new util.db.entities.ShoppingList(name);
                db.shoppingListDAO().insertShoppingList(shoppingList);
*/

                list = listItem.get(listGroup.get(2));
                list.set((list.size() - 1), name);
                list.add("+");

                listItem.put(listGroup.get(2), list);
                adapter.notifyDataSetChanged();
                break;
            case "Pantry":
                Pantry pantry = new Pantry(name);
                createPantry(name);
                AsyncTask.execute(() -> db.pantryDAO().insertPantry(pantry));
                //  PantryList pantryList = new PantryList(name);
                // db.pantryListDAO().insertPantryList(pantryList);

                list = listItem.get(listGroup.get(1));
                list.set((list.size() - 1), name);
                list.add("+");

                listItem.put(listGroup.get(1), list);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    public void Share(View view) {
        //Toast.makeText(getApplicationContext(), "Share", Toast.LENGTH_SHORT).show();

        LinearLayout row = (LinearLayout) view.getParent();
        TextView list = (TextView) row.findViewById(R.id.list_child);

        //Toast.makeText(getApplicationContext(), list.getText().toString(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, Share.class);
        intent.putExtra(EXTRA_MESSAGE, list.getText().toString());
        startActivity(intent);
    }

    public void createPantry(String name) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://85.241.64.96:8080/api/v1/pantries";
            JSONObject jsonBody = new JSONObject();
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            jsonBody.put("timestamp", timestamp.toString());
            jsonBody.put("name", name);

            gpsTracker = new GpsTracker(MainActivity.this);
            if(gpsTracker.canGetLocation()){
                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();
                jsonBody.put("longitude", longitude);
                jsonBody.put("latitude", latitude);
            }else{
                gpsTracker.showSettingsAlert();
            }

            String androidId = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            List<String> guids = new ArrayList<>();
            guids.add(androidId);

            jsonBody.put("guids", guids);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}

