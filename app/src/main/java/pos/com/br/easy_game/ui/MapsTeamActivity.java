package pos.com.br.easy_game.ui;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pos.com.br.easy_game.R;
import pos.com.br.easy_game.async.GenericAsyncTask;
import pos.com.br.easy_game.entity.Atualizavel;
import pos.com.br.easy_game.entity.Jogador;
import pos.com.br.easy_game.entity.Usuario;
import pos.com.br.easy_game.enuns.Method;
import pos.com.br.easy_game.listUtil.ListAdapterJogador;

public class MapsTeamActivity extends FragmentActivity implements OnMapReadyCallback, Atualizavel {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private String servico = "usuario";
    private List<Usuario> times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_team);
        setUpMapIfNeeded();
        new GenericAsyncTask(this,this, Method.GET,servico).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void atualizar(JSONObject jsonObject) {
        Gson gson = new Gson();
        times = new ArrayList<>();
        if (jsonObject.has("array")) {
            try {
                JSONArray array = jsonObject.getJSONArray("array");
                for (int i = 0; i < array.length(); i++) {
                    times.add(gson.fromJson(array.getJSONObject(i).toString(), Usuario.class));
                }
                if(times != null && !times.isEmpty()){
                    for (Usuario usuario : times){
                        mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bola_32))
                                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                                .position(new LatLng(usuario.getLatitude(), usuario.getLongitude()))
                                .title(usuario.getLogin()));
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-25.346312, -49.198559), 14.0f));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if(jsonObject.has("erro")) {
            //TODO Toast;
        }
    }

}
