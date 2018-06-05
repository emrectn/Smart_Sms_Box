package net.emrecetin.newsms;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.SettingInjectorService;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FragmentInbox extends Fragment implements LocationListener {

    View v;
    private RecyclerView smsRecyclerView;
    private List<SmsVO> smsVOList;
    private DatabaseOperation dbo;
    private RecyclerViewAdapterSms recyclerViewAdapterSms;
    private static FragmentInbox inst;
    private LocationManager locationManager;
    private double latitude, longtitude;
    private Location location;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public FragmentInbox() {
        smsVOList = new ArrayList<SmsVO>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbo = new DatabaseOperation(getActivity());
        dbo.dbSessionOpen();

        sharedPref = getContext().getSharedPreferences("sensor", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS}, 100);
            Log.i("Tag", "Sms Okuma izni");
        }

        while (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
        }

        if (sharedPref.getString("SmsList", "-").equals("OK")) {
            smsVOList = dbo.getSmsList();
        } else {
            getAllSms();
        }

    }

    public void getLocationPermission(){
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET,}, 10);
            Log.i("Tag", "Lokasyon izni");
            // TODO: Consider calling
        }
        while (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){}

        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        onLocationChanged(location);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.inbox_fragment, container, false);
        setRecyclerAdapter();
        //((MainActivity) getActivity()).getSupportActionBar().setTitle("Inbox");
        return v;
    }

    public List<SmsVO> getAllSms() {
        SmsVO objSms;
        Uri message = Uri.parse("content://sms/");

        ContentResolver cr = getActivity().getContentResolver();
        Cursor c = cr.query(message, null, null, null, null);
        //Context.startManagingCursor(c);
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new SmsVO();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c.getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                objSms.setType(c.getInt(c.getColumnIndexOrThrow("type")));
                objSms.setLatitude(-1);
                objSms.setLongitude(-1);

                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }

                int messageStatus = findStatus(objSms.getAddress(), objSms.getMsg());
                objSms.setStatus(messageStatus);

                // Rehberdeki isimle eşleştirme
                String address_name = dbo.getContactName(objSms.getAddress());
                if (address_name != null) {
                    objSms.setAddress(address_name);
                }

                smsVOList.add(objSms);
                dbo.addSms(objSms);
                c.moveToNext();
            }
            // Contact List Durumu kaydedildi.
            editor.putString("SmsList", "OK");
            editor.commit();
        } else {
            throw new RuntimeException("You have no SMS");
        }
        c.close();
        return dbo.getSmsList();

    }

    public void setRecyclerAdapter() {
        smsRecyclerView = v.findViewById(R.id.sms_recyclerview);
        recyclerViewAdapterSms = new RecyclerViewAdapterSms(getContext(), smsVOList);
        smsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        smsRecyclerView.setAdapter(recyclerViewAdapterSms);
    }

    public void updateList(SmsVO newsms) {
        getLocationPermission();

        int messageStatus = findStatus(newsms.getAddress(), newsms.getMsg());

        if ( locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )){
            onLocationChanged(location);
        }
        else{
            longtitude = 0.0;
            latitude = 0.0;
        }
        newsms.setStatus(messageStatus);
        newsms.setAddress(dbo.getContactName(newsms.getAddress()));
        newsms.setLongitude(longtitude);
        newsms.setLatitude(latitude);
        smsVOList.add(0, newsms);
        recyclerViewAdapterSms.notifyItemInserted(0);
        recyclerViewAdapterSms.notifyDataSetChanged();
        dbo.addSms(newsms);
    }

    @Override
    public void onResume() {
        dbo.dbSessionOpen();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    public static FragmentInbox instance() {
        return inst;
    }

    public int findStatus(String address, String msg) {
        Pattern regexOTP = Pattern.compile("( kod)|( code)|( KOD)|( CODE)|( Kod)|( Code)");
        Pattern regexCOM = Pattern.compile("([B]{1}[0-9]{3})");
        if (dbo.isContact(address)) {
            if (dbo.isBlocked(address)) {
                return 2;
            }
            return 1;
        }
        else{
            Matcher m2 = regexCOM.matcher(msg);
            if (m2.find()) {
                return 3;
            }

            Matcher m = regexOTP.matcher(msg);
            if (m.find()) {
                return 4;
            }

            return 2;
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        if ( locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )){

            latitude = location.getLatitude();
            longtitude = location.getLongitude();
            System.out.println(location.getLatitude()+ "    " + location.getLongitude());
        }else {
            latitude = 0.0;
            longtitude = 0.0;
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}
}
