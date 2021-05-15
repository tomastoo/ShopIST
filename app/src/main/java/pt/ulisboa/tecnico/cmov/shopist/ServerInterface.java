package pt.ulisboa.tecnico.cmov.shopist;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import util.db.entities.Pantry;
import util.db.queryInterfaces.PantryDAO;
import util.main.SharedClass;


public class ServerInterface {
    private static ServerInterface instance;
    private RequestQueue requestQueue;
    private static Context _context;
    private SharedClass sharedClass;
    PantryDAO pantryDAO;
    //https://localhost:8080/api/v1/pantries
    String serverUrl ="http://85.241.64.96:8080/";

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
        String url = serverUrl + "api/v1/pantries";
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

    /*
    public void populatePantriesOnLocalDb(){
        WorkRequest getPantriesWorkRequest = OneTimeWorkRequest.from(GetAllPantriesWork.class);
        WorkManager.getInstance(_context.getApplicationContext()).enqueue(getPantriesWorkRequest);
    }*/
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

    public class GetAllPantriesWork extends Worker {
        public GetAllPantriesWork(
                @NonNull Context context,
                @NonNull WorkerParameters params) {
            super(context, params);
        }

        @Override
        public Result doWork() {

            // Do the work here--in this case, upload the images.
            //updateShops();
            getPantries();

            // Indicate whether the work finished successfully with the Result
            return Result.success();
        }
    }

}
