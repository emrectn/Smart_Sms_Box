package net.emrecetin.newsms;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ZoomControls;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseOperation dbo;
    private List<SmsVO> smsVOList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dbo = new DatabaseOperation(this);
        dbo.dbSessionOpen();

        smsVOList = new ArrayList<SmsVO>();

        ZoomControls zoom=(ZoomControls)findViewById(R.id.zoom);

        zoom.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });

        zoom.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        final Button btn_MapType=(Button) findViewById(R.id.btn_Sat);
        btn_MapType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMap.getMapType()==GoogleMap.MAP_TYPE_NORMAL){
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    btn_MapType.setText("NORMAL");
                }else{
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    btn_MapType.setText("UYDU");
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        smsVOList = dbo.getSmsList();
        if (mMap != null){
            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(41.015137, 28.979530);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in İstanbul"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }

        for(int i=0; i<smsVOList.size(); i++){
            if(smsVOList.get(i).getLatitude() != -1.0){
                if (smsVOList.get(i).getLatitude() != 0.0){
                    System.out.println("Pin Eklendi : "+i+"  " + smsVOList.get(i).getLatitude()+ "--" + smsVOList.get(i).getLongitude());
                    LatLng latLng=new LatLng(smsVOList.get(i).getLatitude(),smsVOList.get(i).getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(smsVOList.get(i).getId()));
                }
            }
        }
    }
    @Override
    protected void onResume() {
        dbo.dbSessionOpen();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dbo.dbSessionClose();
        super.onPause();
    }
}
