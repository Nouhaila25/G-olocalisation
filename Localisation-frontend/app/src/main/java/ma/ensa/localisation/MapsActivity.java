package ma.ensa.localisation;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    String showUrl = "http://192.168.1.199/localisation/showPositions.php";
    RequestQueue requestQueue;
    private GoogleMap mMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        String apiKey = getResources().getString(R.string.google_maps_api_key);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        requestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
    }

    private void setUpMap() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                showUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray positions = response.getJSONArray("positions");
                    for (int i = 0; i < positions.length(); i++) {
                        JSONObject position = positions.getJSONObject(i);
                        double latitude = position.getDouble("latitude");
                        double longitude = position.getDouble("longitude");

                        // Créer un marqueur avec la position
                        LatLng latLng = new LatLng(latitude, longitude);
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng);

                        // Ajouter le marqueur à la carte
                        mMap.addMarker(markerOptions);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Gérer les erreurs ici
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}