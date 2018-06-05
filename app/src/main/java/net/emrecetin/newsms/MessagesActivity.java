package net.emrecetin.newsms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MessagesActivity extends AppCompatActivity {

    private TextView name, msg;
    private Intent myintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        name = findViewById(R.id.sms_namee);
        msg = findViewById(R.id.sms_msgg);

        myintent = getIntent();
        name.setText(myintent.getStringExtra("Tel"));
        msg.setText(myintent.getStringExtra("Msg"));


    }
}
