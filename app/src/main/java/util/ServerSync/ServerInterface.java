package util.ServerSync;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayObjectRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pt.ulisboa.tecnico.cmov.shopist.GpsTracker;
import pt.ulisboa.tecnico.cmov.shopist.MainActivity;
import util.db.entities.Pantry;
import util.db.entities.PantryItem;
import util.db.entities.Shop;
import util.db.queryInterfaces.PantryDAO;
import util.main.SharedClass;


public class ServerInterface {
    private static ServerInterface instance;
    private RequestQueue requestQueue;
    private static Context _context;
    private SharedClass sharedClass;
    PantryDAO pantryDAO;
    //https://localhost:8080/api/v1/pantries
    String serverUrl ="http://10.0.2.2:8080/";
    GpsTracker gpsTracker;
    long minDistance = 10000;

    public ServerInterface(Context context) {
        _context = context;
        sharedClass = (SharedClass)_context.getApplicationContext();
        pantryDAO = sharedClass.instanceDb().pantryDAO();
    }

    public static synchronized ServerInterface getInstance(Context context) {
        if (instance == null) {
            instance = new ServerInterface(context);
        }
        return instance;
    }



    //gets all pantries and then they are inserted in our local DB
    public void getPantries(){
        String url = serverUrl + "api/v1/pantries_guid/";
        String androidId = Settings.Secure.getString(_context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        url += androidId;
        Log.d("Debug","ID is: " + androidId);
        // Request a string response from the provided URL.

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Debug","Response is: " + response.toString());

                        if(response.length() > 0)
                        {
                            for(int i = 0; i < response.length(); i++){
                                try {
                                    JSONObject pantryJson = response.getJSONObject(i);
                                    long server_id = pantryJson.getLong("id");
                                    double latitude = pantryJson.getDouble("latitude");
                                    double longitude = pantryJson.getDouble("longitude");
                                    String name = pantryJson.getString("name");
                                    String date = pantryJson.getString("timestamp");
                                    Pantry pantry = new Pantry(latitude, longitude, name, server_id);

                                    AsyncTask.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            Pantry p = pantryDAO.getPantry(name);
                                            if (p != null) {
                                                long serverDate = parseStringDateToLong(date);
                                                if (p.time_stamp<serverDate) {
                                                    p.time_stamp = serverDate;
                                                    p.name = name;
                                                    p.longitude = longitude;
                                                    p.latitude = latitude;
                                                    pantryDAO.updatePantry(p);
                                                    updatePantry(p);
                                                }
                                            } else {
                                                pantryDAO.insertPantry(pantry);
                                            }
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse errorRes = error.networkResponse;
                        String stringData = "";
                        if(errorRes != null && errorRes.data != null){
                            try {
                                stringData = new String(errorRes.data,"UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("Error",error.toString());

                    }
                });

// Access the RequestQueue through your singleton class.
        ServerInterface.getInstance(_context).addToRequestQueue(jsonObjectRequest);

    }

     private void updatePantry(Pantry p) {
        String url = serverUrl + "api/v1/pantryItems/pantry/";
        url += p.server_id;
        Log.d("Debug","PantryUpdate ID is: " + p.server_id);
        // Request a string response from the provided URL.

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Debug","Response is: " + response.toString());
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                pantryDAO.nukePantryItemsFromPantryId((int)p.id);
                            }
                        });
                        if(response.length() > 0) {

                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject pantryItemsJson = response.getJSONObject(i);
                                    String name = pantryItemsJson.getString("name");
                                    long shop = pantryItemsJson.getLong("shop");
                                    int stock = pantryItemsJson.getInt("stock");
                                    int quantity = pantryItemsJson.getInt("quantity");
                                    String barcode = pantryItemsJson.getString("barcode");

                                    PantryItem pi = new PantryItem(p.id,(int)shop, quantity,stock,name, barcode);
                                    AsyncTask.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            pantryDAO.insertPantryItem(pi);
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse errorRes = error.networkResponse;
                        String stringData = "";
                        if(errorRes != null && errorRes.data != null){
                            try {
                                stringData = new String(errorRes.data,"UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("Error",error.toString());

                    }
                });

// Access the RequestQueue through your singleton class.
        ServerInterface.getInstance(_context).addToRequestQueue(jsonObjectRequest);

    }


    private long parseStringDateToLong(String date)  {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date parsedDate = new Date();
        try {
            parsedDate = dateFormat.parse(date);
        } catch (Exception e){};
        return parsedDate.getTime()/1000;
    }

    public void getShops() {
        String url = serverUrl + "api/v1/shops_location/";

        gpsTracker = new GpsTracker(_context);
        String location = "";

        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            location = "\"" + latitude + "@" + longitude +"\"";

        }else{
            gpsTracker.showSettingsAlert();
        }


        JSONObject jsonBody  = null;

        try {
            jsonBody = new JSONObject("{\"location\":"+ location +", \"minDistance\":" + 10000000 + "}]");
        } catch (Exception e) {};
        // Request a string response from the provided URL.

        JsonArrayObjectRequest jsonObjectRequest = new JsonArrayObjectRequest
                (Request.Method.POST, url,jsonBody, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Debug","Response is: " + response.toString());
                        if(response.length() > 0)
                        {
                            for(int i = 0; i < response.length(); i++){
                                try {
                                    JSONObject shopsJson = response.getJSONObject(i);
                                    long server_id = shopsJson.getLong("id");
                                    double latitude = shopsJson.getDouble("latitude");
                                    double longitude = shopsJson.getDouble("longitude");
                                    String name = shopsJson.getString("name");
                                    String date = shopsJson.getString("timestamp");
                                    Shop shop = new Shop(latitude, longitude, name, server_id);

                                    AsyncTask.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            Shop s = pantryDAO.getShopByServerId(server_id);
                                            if(s != null) {
                                                long serverDate = parseStringDateToLong(date);
                                                if (s.time_stamp < serverDate) {
                                                    s.time_stamp = serverDate;
                                                    s.latitude = latitude;
                                                    s.longitude = longitude;
                                                    s.name = name;
                                                    pantryDAO.updateShop(s);
                                                }
                                            } else pantryDAO.insertShop(shop);
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse errorRes = error.networkResponse;
                        String stringData = "";
                        if(errorRes != null && errorRes.data != null){
                            try {
                                stringData = new String(errorRes.data,"UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("Error",error.toString());

                    }
                });

// Access the RequestQueue through your singleton class.
        ServerInterface.getInstance(_context).addToRequestQueue(jsonObjectRequest);

    }

    //gets all pantries and then they are inserted in our local DB
    public void getPantries(String androidId){
        String url = serverUrl + "api/v1/pantries_guid/";
        //serverUrl += androidId;
        // Request a string response from the provided URL.

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Debug","Response is: " + response.toString());
                        if(response.length() > 0)
                        {

                            for(int i = 0; i < response.length(); i++){
                                try {
                                    JSONObject pantryJson = response.getJSONObject(i);
                                    long server_id = pantryJson.getLong("id");
                                    double latitude = pantryJson.getDouble("latitude");
                                    double longitude = pantryJson.getDouble("longitude");
                                    String name = pantryJson.getString("name");
                                    Pantry pantry = new Pantry(latitude, longitude, name, server_id);

                                    AsyncTask.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            pantryDAO.insertPantry(pantry);
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            MainActivity.getInstance().initListData();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse errorRes = error.networkResponse;
                        String stringData = "";
                        if(errorRes != null && errorRes.data != null){
                            try {
                                stringData = new String(errorRes.data,"UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.e("Error",error.toString());

                    }
                });

// Access the RequestQueue through your singleton class.
        ServerInterface.getInstance(_context).addToRequestQueue(jsonObjectRequest);

    }


    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(_context.getApplicationContext());
        }
        return requestQueue;
    }


    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


    public void updatePantryItem(PantryItem pi, String pantryName){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(_context);
            String androidId = Settings.Secure.getString(_context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            String URL = serverUrl + "api/v1/pantryItems/"+ pantryName + "/" + androidId;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", pi.name);
            jsonBody.put("barcode", pi.barcode);
            jsonBody.put("stock", pi.stock);
            jsonBody.put("quantity", pi.quantity);
            jsonBody.put("shop", pi.shopId);

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



    public void createShop(Shop shop){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(_context);

            String URL = serverUrl + "api/v1/shops";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", shop.name);
            jsonBody.put("latitude", shop.latitude);
            jsonBody.put("longitude", shop.longitude);
            jsonBody.put("timestamp", shop.time_stamp);

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
