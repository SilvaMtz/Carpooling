package itesm.mx.carpoolingtec.main;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import itesm.mx.carpoolingtec.R;
import itesm.mx.carpoolingtec.contacts.ContactsFragment;
import itesm.mx.carpoolingtec.data.AppRepository;
import itesm.mx.carpoolingtec.data.MySharedPreferences;
import itesm.mx.carpoolingtec.login.LoginActivity;
import itesm.mx.carpoolingtec.post.PostActivity;
import itesm.mx.carpoolingtec.profile.ProfileActivity;
import itesm.mx.carpoolingtec.request.RequestActivity;
import itesm.mx.carpoolingtec.rides.RidesFragment;
import itesm.mx.carpoolingtec.schedule.ScheduleFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.fab) FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fab.setOnClickListener(this);

        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);
        viewPager.setCurrentItem(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Set refresh icon color to white.
       /* Drawable refreshIcon = menu.findItem(R.id.action_sort).getIcon();
        if (refreshIcon != null) {
            refreshIcon.mutate();
            refreshIcon.setColorFilter(ContextCompat.getColor(this, R.color.white),
                    PorterDuff.Mode.SRC_ATOP);
        }*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                AppRepository app = AppRepository.getInstance(getSharedPreferences(MySharedPreferences.MY_PREFERENCES, MODE_PRIVATE));
                app.saveMyId(null);
                Intent intent3 = new Intent(this, LoginActivity.class);
                finish();
                startActivity(intent3);
                return true;
            case R.id.action_profile:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_solicitudes:
                Intent intent2 = new Intent(this, RequestActivity.class);
                startActivity(intent2);
                return true;
            /*case R.id.action_sort:
                return true;*/
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra("id",0); // 0 representa main activity
        startActivity(intent);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()){
            case 0:
                fab.setVisibility(View.GONE);
                break;
            case 1:
                fab.setVisibility(View.VISIBLE);
                break;
            case 2:
                fab.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    class MyAdapter extends FragmentPagerAdapter {

        private static final int FRAGMENT_COUNT = 3;

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0:
                    //fab.setVisibility(View.INVISIBLE);
                    return ContactsFragment.newInstance();
                case 1:
                    //fab.setVisibility(View.VISIBLE);
                    return RidesFragment.newInstance(RidesFragment.TO_TEC);
                case 2:
                    return RidesFragment.newInstance(RidesFragment.FROM_TEC);
            }
            return null;
        }

        @Override
        public int getCount() {
            return FRAGMENT_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0:
                    return getResources().getString(R.string.contacts);
                case 1:
                    return getResources().getString(R.string.rides_to_tec);
                case 2:
                    return getResources().getString(R.string.rides_from_tec);
            }
            return null;
        }
    }
}
