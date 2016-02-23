package com.open_source.joker.concentration.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.open_source.joker.concentration.R;
import com.open_source.joker.concentration.app.CONActivity;
import com.open_source.joker.concentration.fragment.HomeFragment;
import com.open_source.joker.concentration.fragment.OtherFragment;

public class MainActivity extends CONActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int HOME = 1;
    private static final int OTHER = 10;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;
    private HomeFragment mHomeFragment;
    private OtherFragment mOtherFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("首页");
        initManager();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavView = (NavigationView) findViewById(R.id.nav_view);
        mNavView.setNavigationItemSelectedListener(this);

        mHomeFragment = new HomeFragment();
        mOtherFragment = new OtherFragment();

        switchFragment(mHomeFragment, HOME);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.home:
                switchFragment(mHomeFragment, HOME);
                break;
            case R.id.other:
                switchFragment(mOtherFragment, OTHER);
                break;
        }
        item.setChecked(true);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}
