package sk.upjs.ics.android.koncovyprojekt2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static String cisloCipu;
    private static MojVeterinar oblubenyVeterinar;
    private DrawerLayout drawer;
    public static SharedPreferences settings;
    public NavigationView navigationView;
    public BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        cisloCipu = settings.getString("CISLOCIPU", "");
        settings.getString("IMG", "");
        setContentView(R.layout.activity_main);
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (cisloCipu.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View customLayout = getLayoutInflater().inflate(R.layout.vstup, null);
            builder.setView(customLayout);
            builder.create();
            final EditText cislo = customLayout.findViewById(R.id.vstupCisloCipu);
            builder.setPositiveButton("OK", (dialog, which) -> {
                if (cislo.getText().toString().equals("")) {
                    Toast.makeText(this, "Nesprávne číslo čipu", Toast.LENGTH_SHORT).show();
                } else {
                    cisloCipu = cislo.getText().toString();
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("CISLOCIPU", cislo.getText().toString());
                    editor.apply();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                }
            });
            builder.setNegativeButton("Zatvor", (dialog, which) -> {
                Toast.makeText(this, "Používaš aplikáciu bez čísla čipu, informácie o zvierati sa nebudú zobrazovať", Toast.LENGTH_LONG).show();
            });
            builder.show();
        }
        if (settings.getString("OBLUBENY_VETERINAR", null)!=null) obnovOblubenehoVeterinara();
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
                    if (!(navigationView.getCheckedItem() == null))navigationView.getCheckedItem().setChecked(false);
                    break;
                case R.id.nav_favorites:
                    selectedFragment = new OckovaniaFragment();
                    if (!(navigationView.getCheckedItem() == null))navigationView.getCheckedItem().setChecked(false);
                    break;
                case R.id.nav_search:
                    selectedFragment = new PoznamkyFragment();
                    if (!(navigationView.getCheckedItem() == null))navigationView.getCheckedItem().setChecked(false);
                    break;
                case R.id.nav_card:
                    selectedFragment = new NavstevyFragment();
                    if (!(navigationView.getCheckedItem() == null))navigationView.getCheckedItem().setChecked(false);
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
                selectedFragment = new MojveterinarFragment();
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
        item.setChecked(false);
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
        if (id == R.id.doctor) {
            if (oblubenyVeterinar==null) Toast.makeText(this, "Nezvolil si si obľúbeného veterinára", Toast.LENGTH_SHORT).show();
            else zobrazDoktora();
        }
        return super.onOptionsItemSelected(item);
    }

    private void zobrazDoktora() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setTitle("Obľúbený veterinár");
        final View customLayout = getLayoutInflater().inflate(R.layout.veterinar_dialog, null);
        builder.setView(customLayout);
        builder.create();

        TextView meno = customLayout.findViewById(R.id.menoapriezviskoDialog);
        ImageView image = customLayout.findViewById(R.id.veterinarImageDialog);
        TextView adresa = customLayout.findViewById(R.id.adresaDialog);
        TextView telefon = customLayout.findViewById(R.id.telefonDialog);
        TextView email = customLayout.findViewById(R.id.emailDialog);
        meno.setText("MVDr. " + oblubenyVeterinar.firstname + " " + oblubenyVeterinar.lastname);
        adresa.setText(oblubenyVeterinar.address);
        telefon.setText(oblubenyVeterinar.phone);
        email.setText(oblubenyVeterinar.email);
        Picasso.get().load(getOblubenyVeterinar().imageUrl).into(image);
        if (image.getDrawable()!=null) {
            image.setBackgroundResource(R.drawable.layoutborder);
            image.setPadding(8,8,8,8);
        }
        image.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                image.setBackgroundResource(R.drawable.layoutborder);
                image.setPadding(8,8,8,8);
            }
        });

        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private void obnovOblubenehoVeterinara() {
        String veterinar_str = settings.getString("OBLUBENY_VETERINAR", null);
        String []x = veterinar_str.split("#");
        MojVeterinar mojVeterinar = new MojVeterinar(x[2], x[4], x[0], x[5], x[1], x[3]);
        setOblubenyVeterinar(mojVeterinar);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected static String getCisloCipu() {
        return cisloCipu;
    }

    public static MojVeterinar getOblubenyVeterinar() {
        return oblubenyVeterinar;
    }

    public static void setOblubenyVeterinar(MojVeterinar oblubenyVeterinar) {
        MainActivity.oblubenyVeterinar = oblubenyVeterinar;
    }
}