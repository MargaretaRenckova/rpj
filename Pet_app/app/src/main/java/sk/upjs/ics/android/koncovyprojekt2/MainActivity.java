package sk.upjs.ics.android.koncovyprojekt2;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

import static sk.upjs.ics.android.koncovyprojekt2.Defaults.DISMISS_ACTION;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    public static SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = settings.edit();
        editor.apply();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_favorites:
                    selectedFragment = new FavoritesFragment();
                    break;
                case R.id.nav_search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.nav_card:
                    selectedFragment = new CardFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment;
        switch (item.getItemId()) {
            case R.id.mojveterinar:
                selectedFragment = new Mojveterinar();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                break;
            case R.id.upozornenia:
                selectedFragment = new Upozornenia();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                break;
            case R.id.choroby:
                selectedFragment = new Choroby();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                break;
            case R.id.prvapomoc:
                selectedFragment = new Prvapomoc();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                break;
            case R.id.info:
                selectedFragment = new InfoONas();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item) {
            upravProfil();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void upravProfil() {
    }
}