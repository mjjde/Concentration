package com.open_source.joker.concentration.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.open_source.joker.concentration.R;
import com.open_source.joker.concentration.app.CONActivity;
import com.open_source.joker.concentration.fragment.HelpFragment;
import com.open_source.joker.concentration.fragment.HomeFragment;

public class MainActivity extends CONActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final int HOME = 1;
    public static final int HELP = 10;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;

    private HomeFragment mHomeFragment;
    private HelpFragment mHelpFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavView = (NavigationView) findViewById(R.id.nav_view);
        setTitle("首页");
        initMannager();
        initFragmet();
        mNavView.setNavigationItemSelectedListener(this);
    }

    private void initFragmet() {
        mHomeFragment = new HomeFragment();
        mHelpFragment = new HelpFragment();
        switchFragment(mHomeFragment, HOME);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        switch (item.getItemId()) {
            case R.id.home:
                switchFragment(mHomeFragment, HOME);
                setTitle("首页");
                break;
            case R.id.help:
                switchFragment(mHelpFragment, HELP);
                setTitle("帮助");
                break;
        }
        item.setChecked(true);
        return false;
    }
}
