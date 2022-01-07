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
import java.util.HashSet;
import java.util.Set;

import static sk.upjs.ics.android.koncovyprojekt2.Defaults.DISMISS_ACTION;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    public static SharedPreferences settings;
    public static String meno;
    public static String priezvisko;
    public static boolean isPrvadavka;
    public static String datumPrvadavka;
    public static boolean isDruhadavka;
    public static String datumDruhadavka;
    public static String firma;
    public static ArrayList<String> testy;
    public static ArrayList<String> testyDetail;
    Set<String> testySet;
    Set<String> testyDetailSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences("DATA", Context.MODE_PRIVATE);
        meno = settings.getString("MENO", "");
        priezvisko = settings.getString("PRIEZVISKO", "");
        isPrvadavka = settings.getBoolean("ISPRVADAVKA", false);
        datumPrvadavka = settings.getString("DATUMPRVADAVKA", "");
        isDruhadavka = settings.getBoolean("ISDRUHADAVKA", false);
        datumDruhadavka = settings.getString("DATUMDRUHADAVKA", "");
        firma = settings.getString("FIRMA", "");
        testySet = settings.getStringSet("TESTY", new HashSet<>());
        testyDetailSet = settings.getStringSet("TESTYDETAIL", new HashSet<>());
        testy = new ArrayList<>(testySet);
        testyDetail = new ArrayList<>(testyDetailSet);
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
        editor.putString("MENO", meno);
        editor.putString("PRIEZVISKO", priezvisko);
        editor.putBoolean("ISPRVADAVKA", isPrvadavka);
        editor.putString("DATUMPRVADAVKA", datumPrvadavka);
        editor.putBoolean("ISDRUHADAVKA", isDruhadavka);
        editor.putString("DATUMDRUHADAVKA", datumDruhadavka);
        editor.putString("FIRMA", firma);
        testySet = new HashSet<>();
        testySet.addAll(testy);
        testyDetailSet = new HashSet<>();
        testyDetailSet.addAll(testyDetail);
        editor.putStringSet("TESTY", testySet);
        editor.putStringSet("TESTYDETAIL", testyDetailSet);
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
            case R.id.testy:
                selectedFragment = new InfoFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                break;
            case R.id.karantena:
                selectedFragment = new Karantena();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                break;
            case R.id.telefon:
                selectedFragment = new Kontakty();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                break;
            case R.id.qaa:
                selectedFragment = new Otazky();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setTitle("Pridaj svoje informácie");
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog, null);
        builder.setView(customLayout);
        builder.create();
        final RadioGroup typTestu = customLayout.findViewById(R.id.typTestu);
        final RadioButton anti_test = customLayout.findViewById(R.id.Anti_test);
        final RadioButton PCR_test = customLayout.findViewById(R.id.PCR_test);
        final RadioGroup pozitnegat = customLayout.findViewById(R.id.pozitnegat);
        final RadioButton negativita = customLayout.findViewById(R.id.negativita);
        final RadioButton pozitivita = customLayout.findViewById(R.id.pozitivita);
        typTestu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                pozitnegat.setVisibility(View.VISIBLE);
            }
        });
        final CalendarView datumvykonaniatestu = customLayout.findViewById(R.id.datumvykonanietestu);
        final Calendar cal1 = Calendar.getInstance();
        final String[] Date1 = new String[3];
        Date1[0] = String.valueOf(cal1.get(Calendar.DAY_OF_MONTH));
        Date1[1] = String.valueOf(cal1.get(Calendar.MONTH) + 1);
        Date1[2] = String.valueOf(cal1.get(Calendar.YEAR));
        datumvykonaniatestu.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Date1[0] = String.valueOf(dayOfMonth);
                Date1[1] = String.valueOf(month + 1);
                Date1[2] = String.valueOf(year);
            }
        });

        final TextView nadpisockovania = customLayout.findViewById(R.id.nadpisockovania);
        final RadioButton first_dose = customLayout.findViewById(R.id.first_dose);
        final RadioButton second_dose = customLayout.findViewById(R.id.second_dose);
        final RadioGroup firmy = customLayout.findViewById(R.id.firmy);
        final TextView nadpisdatumockovania = customLayout.findViewById(R.id.nadpisdatumockovania);
        final CalendarView datumockovania = customLayout.findViewById(R.id.datumockovania);
        final Calendar cal2 = Calendar.getInstance();
        final String[] Date2 = new String[3];
        Date2[0] = String.valueOf(cal2.get(Calendar.DAY_OF_MONTH));
        Date2[1] = String.valueOf(cal2.get(Calendar.MONTH) + 1);
        Date2[2] = String.valueOf(cal2.get(Calendar.YEAR));
        datumockovania.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Date2[0] = String.valueOf(dayOfMonth);
                Date2[1] = String.valueOf(month + 1);
                Date2[2] = String.valueOf(year);
            }
        });
        if (!isPrvadavka) {
            second_dose.setClickable(false);
        }
        if (isPrvadavka) {
            first_dose.setClickable(false);
        }
        if (isPrvadavka && isDruhadavka) {
            nadpisockovania.setVisibility(View.GONE);
            first_dose.setVisibility(View.GONE);
            second_dose.setVisibility(View.GONE);
            datumockovania.setVisibility(View.GONE);

        }
        nadpisdatumockovania.setVisibility(View.GONE);
        datumockovania.setVisibility(View.GONE);
        first_dose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    firmy.setVisibility(View.VISIBLE);
                    nadpisdatumockovania.setVisibility(View.VISIBLE);
                    datumockovania.setVisibility(View.VISIBLE);
                }
            }
        });

        second_dose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    firmy.setVisibility(View.GONE);
                    nadpisdatumockovania.setVisibility(View.VISIBLE);
                    datumockovania.setVisibility(View.VISIBLE);
                }
            }
        });

        builder.setPositiveButton("ULOŽ", (dialog, which) -> {
            if (anti_test.isChecked()) {
                if (pozitivita.isChecked()) {
                    testy.add("Antigénový test");
                    testyDetail.add("pozitívny," + Date1[0] + ". " + Date1[1] + ". " + Date1[2]);
                }
                if (negativita.isChecked()) {
                    testy.add("Antigénový test");
                    testyDetail.add("negatívny," + Date1[0] + ". " + Date1[1] + ". " + Date1[2]);
                }
            }
            if (PCR_test.isChecked()) {
                if (pozitivita.isChecked()) {
                    testy.add("PCR test");
                    testyDetail.add("pozitívny," + Date1[0] + ". " + Date1[1] + ". " + Date1[2]);
                }
                if (negativita.isChecked()) {
                    testy.add("PCR test");
                    testyDetail.add("negatívny," + Date1[0] + ". " + Date1[1] + ". " + Date1[2]);
                }
            }

            if (first_dose.isChecked()) {
                switch (firmy.getCheckedRadioButtonId()) {
                    case R.id.pfizer: {
                        isPrvadavka = true;
                        firma = "Pzifer/BioNTech";
                        break;
                    }
                    case R.id.moderna: {
                        isPrvadavka = true;
                        firma = "Moderna";
                        break;
                    }
                    case R.id.SputnikV: {
                        isPrvadavka = true;
                        firma = "Sputnik V";
                        break;
                    }
                    case R.id.AstraZeneca: {
                        isPrvadavka = true;
                        firma = "AstraZeneca";
                        break;
                    }
                    case R.id.JJ: {
                        isPrvadavka = true;
                        firma = "J&J";
                        break;
                    }
                }
                datumPrvadavka = Date2[0] + ". " + Date2[1] + ". " + Date2[2];
            }
            if (second_dose.isChecked()) {
                isDruhadavka = true;
                datumDruhadavka = Date2[0] + " " + Date2[1] + " " + Date2[2];
            }
        });
        builder.setNegativeButton("Zrušiť", DISMISS_ACTION);
        builder.show();
    }
}