package itesm.mx.carpoolingtec.main;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TabLayout.OnTabSelectedListener, Toolbar.OnMenuItemClickListener{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.fab) FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle("Carpooling Tec");
        toolbar.setOnMenuItemClickListener(this);

        fab.setOnClickListener(this);

        TapTargetSequence sequence = new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.fab), "Publica un ride", "Haz click para ofrecer un ride a alumnos del Tec")
                                .outerCircleColor(R.color.colorPrimary)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(R.color.white)
                                .titleTextSize(20)
                                .descriptionTextSize(14)
                                .textColor(R.color.white)
                                .textTypeface(Typeface.SANS_SERIF)
                                .drawShadow(true)
                                .cancelable(false)
                                .tintTarget(false)
                                .transparentTarget(false),
                        TapTarget.forToolbarOverflow(toolbar, "Menu de opciones", "Aqui puedes ver tu perfil y cerrar sesión")
                                .outerCircleColor(R.color.colorPrimary)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(R.color.white)
                                .titleTextSize(20)
                                .descriptionTextSize(14)
                                .textColor(R.color.white)
                                .textTypeface(Typeface.SANS_SERIF)
                                .cancelable(false),
                        TapTarget.forToolbarMenuItem(toolbar, R.id.action_solicitudes, "Solicitudes", "Aquí verás las solicitudes de información de contacto cuando alguien esté interesado en un ride que estes ofreciendo")
                                .outerCircleColor(R.color.colorPrimary)
                                .outerCircleAlpha(0.96f)
                                .targetCircleColor(R.color.white)
                                .titleTextSize(20)
                                .descriptionTextSize(14)
                                .textColor(R.color.white)
                                .textTypeface(Typeface.SANS_SERIF)
                                .cancelable(false)
                        )
                .listener(new TapTargetSequence.Listener() {
                    // This listener will tell us when interesting(tm) events happen in regards
                    // to the sequence
                    @Override
                    public void onSequenceFinish() {
                        // Yay
                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {
                        // Boo
                    }
                });

        sequence.start();

        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(this);
        viewPager.setCurrentItem(1);
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
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
        }
        return false;
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
