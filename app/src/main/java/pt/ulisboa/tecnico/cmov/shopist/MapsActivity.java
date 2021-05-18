package pt.ulisboa.tecnico.cmov.shopist;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    LatLng lastLatLng = null;
    Marker lastMarker = null;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // initializing our search view.
        searchView = findViewById(R.id.idSearchView);

        // Obtain the SupportMapFragment and get notified
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // adding on query listener for our search view.
        handleConfirmButton();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || location.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);

                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    lastLatLng = latLng;

                    if(lastMarker != null)
                        lastMarker.remove();

                    lastMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(location));

                    // below line is to animate camera to that position.
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        // at last we calling our map fragment to update.

        mapFragment.getMapAsync(this);
    }


    public void handleConfirmButton(){
        FloatingActionButton confirmBtn = (FloatingActionButton) findViewById(R.id.floatingButtonConfirmLocation);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastLatLng != null){
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("latitude", lastLatLng.latitude);
                    returnIntent.putExtra("longitude", lastLatLng.longitude);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}