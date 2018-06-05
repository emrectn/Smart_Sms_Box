package net.emrecetin.newsms;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdaptor extends FragmentPagerAdapter {

    private final List<Fragment> lstFragment = new ArrayList<>();
    private final List<String> lstTitles = new ArrayList<>();

    public ViewPagerAdaptor(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return lstFragment.get(position);
    }

    @Override
    public int getCount() {
        return lstTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return lstTitles.get(position);
    }

    public void addFragment (Fragment fragment, String title) {
        lstFragment.add(fragment);
        lstTitles.add(title);
    }
}
