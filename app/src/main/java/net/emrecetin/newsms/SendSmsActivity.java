package net.emrecetin.newsms;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class SendSmsActivity extends AppCompatActivity {
    private EditText mobile_no, message;
    private Button send_sms;
    private DatabaseOperation dbo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_fragment);

        getSupportActionBar().setTitle("Send Sms");

        dbo = new DatabaseOperation(this);
        dbo.dbSessionOpen();

        mobile_no = findViewById(R.id.edt_tel_no);
        message = findViewById(R.id.edt_message);
        send_sms = findViewById(R.id.send_sms);

        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            mobile_no.setText(extras.getString("Tel"));
        }


        send_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsVO smsVO = new SmsVO();

                String no = mobile_no.getText().toString();
                String msg = message.getText().toString();

                //Getting intent and PendingIntent instance
                Intent intent = new Intent(getApplicationContext(), SendSmsActivity.class);
                PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                //Get the SmsManager instance and call the sendTextMessage method to send message
                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage(no, null, msg, pi,null);

                Date date = new Date();

                smsVO.setAddress(no);
                smsVO.setMsg(msg);
                smsVO.setType(2);
                smsVO.setStatus(-1);
                smsVO.setTime(String.valueOf(date.getTime()));

                dbo.addSms(smsVO);


                Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                        Toast.LENGTH_LONG).show();
            }
        });
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

