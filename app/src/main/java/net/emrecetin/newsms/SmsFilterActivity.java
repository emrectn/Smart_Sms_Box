package net.emrecetin.newsms;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SmsFilterActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdaptor viewPagerAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_filter);

        tabLayout = (TabLayout) findViewById(R.id.filter_tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.filter_viewpager_id);
        viewPagerAdaptor = new ViewPagerAdaptor(getSupportFragmentManager());

        viewPagerAdaptor.addFragment(new FragmentWhite(),"");
        viewPagerAdaptor.addFragment(new FragmentSpam(),"");
        viewPagerAdaptor.addFragment(new FragmentCommercial(),"");
        viewPagerAdaptor.addFragment(new FragmentOTP(),"");

        viewPager.setAdapter(viewPagerAdaptor);
        tabLayout.setupWithViewPager(viewPager);

        // Tab icons
        tabLayout.getTabAt(0).setIcon(R.drawable.img_white);
        tabLayout.getTabAt(1).setIcon(R.drawable.img_spam);
        tabLayout.getTabAt(2).setIcon(R.drawable.img_commercial);
        tabLayout.getTabAt(3).setIcon(R.drawable.img_otp);

    }


}
