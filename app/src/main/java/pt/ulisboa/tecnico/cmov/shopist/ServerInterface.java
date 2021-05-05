package pt.ulisboa.tecnico.cmov.shopist;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import static androidx.work.PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS;


public class ServerInterface {
    private static ServerInterface instance;
    private RequestQueue requestQueue;
    private static Context _context;
    //https://localhost:8080/api/v1/pantries
    String serverUrl ="http://10.0.2.2:8080/";

    public ServerInterface(Context context) {
        _context = context;
        /*
        PeriodicWorkRequest updateShopsWorkRequest =
                new PeriodicWorkRequest.Builder(UpdateShopsWorker.class,MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                        .build();
        WorkManager.getInstance(_context).enqueueUniquePeriodicWork(
                "updateShops",
                ExistingPeriodicWorkPolicy.KEEP,
                updateShopsWorkRequest);
         */
    }

    public static synchronized ServerInterface getInstance(Context context) {
        if (instance == null) {
            instance = new ServerInterface(context);
        }
        return instance;
    }

    public void getPantries(){
        String url = serverUrl + "api/v1/pantries";
        // Request a string response from the provided URL.
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Debug","Response is: " + response.toString());
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


    public void updatePantryLists(){

    }

    public void updatePantryList(){

    }


    public void insertItem(){

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

    public class UpdateShopsWorker extends Worker {
        public UpdateShopsWorker(
                @NonNull Context context,
                @NonNull WorkerParameters params) {
            super(context, params);
        }

        @Override
        public Result doWork() {

            // Do the work here--in this case, upload the images.
            //updateShops();

            // Indicate whether the work finished successfully with the Result
            return Result.success();
        }
    }

}
