package app.com.appsamples;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import app.com.appsamples.frag.BluetoothPager.BluetoothTest;
import app.com.appsamples.frag.BluetoothPager.Home;
import app.com.appsamples.frag.RecordAudio;
import app.com.appsamples.frag.RecyclerViewTest;
import app.com.appsamples.frag.RingerMode;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putInt("no", 1);
        RecordAudio recordAudio = RecordAudio.getInstant(bundle);
        fragment = recordAudio;
        changeFrag(fragment);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void changeFrag(Fragment fragment, String title) {
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(title)
                .commit();
    }


    void changeFrag(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void clearBack() {
        //For remove specific fragment by its name
        fragmentManager.popBackStack("2", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        //for remove all backstack
        //fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Bundle bundle = new Bundle();
        String title = "";

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            RecordAudio recordAudio = RecordAudio.getInstant(bundle);
            fragment = recordAudio;
            title = "1";
        } else if (id == R.id.nav_gallery) {
            bundle.putInt("no", 2);
            title = "2";
            fragment = new RecyclerViewTest();
        } else if (id == R.id.nav_slideshow) {
            bundle.putInt("no", 3);
            title = "3";
            fragment = new RingerMode();
        } else if (id == R.id.nav_manage) {
            bundle.putInt("no", 4);
            title = "4";
            fragment = new Home();
        /*} else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_send) {*/
        }

        changeFrag(fragment, title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
