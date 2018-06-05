package net.emrecetin.newsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";
    private SmsVO newsms = new SmsVO();

    public void onReceive(Context context, Intent intent) {
        Bundle intentExtras = intent.getExtras();
        if (intentExtras != null) {
            Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
            for (int i = 0; i < sms.length; ++i) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                newsms.setMsg(smsMessage.getMessageBody().toString());
                newsms.setAddress(smsMessage.getOriginatingAddress());
                newsms.setTime(String.valueOf(smsMessage.getTimestampMillis()));
                newsms.setType(1);
                newsms.setLongitude(-1);
                newsms.setLatitude(-1);


                //Loglar
                Log.i(getClass().getSimpleName(), "DispMesgBody : " + newsms.getMsg());
                Log.i(getClass().getSimpleName(), "OriAddre : "+ newsms.getAddress());


                Long millis = smsMessage.getTimestampMillis();
                Date date=new Date(millis);

                System.out.println(DateFormat.getDateInstance().format(date)+" "+(millis / (1000 * 60 * 60)) % 24+":"+ (millis / (1000 * 60)) % 60+":"+(millis / 1000) % 60);


            }
            Toast.makeText(context, "Yeni Mesajınız Var", Toast.LENGTH_SHORT).show();

            //this will update the UI with message
            FragmentInbox inst = FragmentInbox.instance();
            if (inst !=null){
                inst.updateList(newsms);
            }
        }
    }

}