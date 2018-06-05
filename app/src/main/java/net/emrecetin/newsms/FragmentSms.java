package net.emrecetin.newsms;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class FragmentSms extends Fragment implements LocationListener{

    View v;
    private DatabaseOperation dbo;
    private EditText mobile_no, message;
    private Button send_sms;
    private LocationManager locationManager;
    private double latitude, longtitude;
    private Location location;

    public FragmentSms() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbo = new DatabaseOperation(getActivity());

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, 100);
        }

        while (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){

        }

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET,}, 10);
            Log.i("Tag", "Lokasyon izni");
            // TODO: Consider calling
            return;
        }

        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.sms_fragment,container,false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Send Sms");

        mobile_no = v.findViewById(R.id.edt_tel_no);
        message = v.findViewById(R.id.edt_message);
        send_sms = v.findViewById(R.id.send_sms);

        send_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String no = mobile_no.getText().toString();
                String msg = message.getText().toString();


                if ( locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )){
                    onLocationChanged(location);
                }
                else{
                    longtitude = 0.0;
                    latitude = 0.0;
                }

                if (no.equals("") || msg.equals("")){
                    Toast.makeText(getActivity().getApplicationContext(), "Number or Message Empty!",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    SmsVO smsVO = new SmsVO();

                    //Getting intent and PendingIntent instance
                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(getActivity().getApplicationContext(), 0, intent, 0);

                    Date date = new Date();
                    smsVO.setAddress(no);
                    smsVO.setMsg(msg);
                    smsVO.setType(2);
                    smsVO.setStatus(-1);
                    smsVO.setTime(String.valueOf(date.getTime()));
                    smsVO.setLongitude(longtitude);
                    smsVO.setLatitude(latitude);
                    dbo.addSms(smsVO);

                    //Get the SmsManager instance and call the sendTextMessage method to send message
                    SmsManager sms=SmsManager.getDefault();
                    sms.sendTextMessage(no, null, msg, pi,null);
                    Toast.makeText(getActivity().getApplicationContext(), "Message Sent successfully!",
                            Toast.LENGTH_LONG).show();

                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        dbo.dbSessionOpen();
        super.onResume();
    }

    @Override
    public void onPause() {
        dbo.dbSessionClose();
        super.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longtitude = location.getLongitude();
        System.out.println(latitude+"  " +longtitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
