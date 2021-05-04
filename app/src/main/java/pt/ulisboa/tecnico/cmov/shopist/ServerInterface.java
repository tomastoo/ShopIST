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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.TimeUnit;

import static androidx.work.PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS;


public class ServerInterface {

    Context _context;
    String serverUrl ="https://localhost:8090/";

    public ServerInterface(Context context) {
        _context = context;
        PeriodicWorkRequest updateShopsWorkRequest =
                new PeriodicWorkRequest.Builder(UpdateShopsWorker.class,MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                        .build();
        WorkManager.getInstance(_context).enqueueUniquePeriodicWork(
                "updateShops",
                ExistingPeriodicWorkPolicy.KEEP,
                updateShopsWorkRequest);
    }

    public void updateShops(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(_context);
        String url = serverUrl;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Display the first 500 characters of the response string.
                    Log.println(Log.DEBUG,"Debug","Response is: " + response.substring(0, 500));
                }, error -> {
                    //KABOOOM
                });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


    public void updatePantryLists(){

    }

    public void updatePantryList(){

    }


    public void insertItem(){

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
            updateShops();

            // Indicate whether the work finished successfully with the Result
            return Result.success();
        }
    }

}
