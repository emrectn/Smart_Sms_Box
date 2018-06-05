 package net.emrecetin.newsms;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

 public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdaptor viewPagerAdaptor;
    private DatabaseOperation dbo = new DatabaseOperation(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 100);
            Log.i("Tag","Contact Okuma izni");
        }

        while (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){}

        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        viewPagerAdaptor = new ViewPagerAdaptor(getSupportFragmentManager());

        viewPagerAdaptor.addFragment(new FragmentInbox(),"");
        viewPagerAdaptor.addFragment(new FragmentContact(),"");
        viewPagerAdaptor.addFragment(new FragmentSms(),"");

        viewPager.setAdapter(viewPagerAdaptor);
        tabLayout.setupWithViewPager(viewPager);

        // Tab icons
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_move_to_inbox);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_group_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_message_black_24dp);

        //remove shadow
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
    }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         MenuInflater menuInflater = getMenuInflater();
         menuInflater.inflate(R.menu.example_menu, menu);
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent smsfilter_intent = new Intent(MainActivity.this, SmsFilterActivity.class);
                startActivity(smsfilter_intent);
                return true;

            case R.id.item2:
                Intent blacklist_intent = new Intent(MainActivity.this, BlackListActivity.class);
                startActivity(blacklist_intent);
                return true;

            case R.id.item3:
                Intent map_intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(map_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
     }


 }
