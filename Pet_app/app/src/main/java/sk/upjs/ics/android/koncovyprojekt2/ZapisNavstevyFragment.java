package sk.upjs.ics.android.koncovyprojekt2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class ZapisNavstevyFragment extends Fragment {
    EditText navstevyZapis_datum;
    EditText navstevyZapis_dovod;
    EditText navstevyZapis_nasledujucaKontrola;
    EditText navstevyZapis_id;
    EditText navstevyZapis_heslo;
    private Button navstevyZapis_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frameLayout = inflater.inflate(R.layout.fragment_zapis_navstevy, container, false);
        navstevyZapis_datum = frameLayout.findViewById(R.id.navstevyZapis_datum);
        navstevyZapis_dovod = frameLayout.findViewById(R.id.navstevyZapis_dovod);
        navstevyZapis_nasledujucaKontrola = frameLayout.findViewById(R.id.navstevyZapis_nasledujucaKontrola);
        navstevyZapis_id = frameLayout.findViewById(R.id.navstevyZapis_id);
        navstevyZapis_heslo = frameLayout.findViewById(R.id.navstevyZapis_heslo);
        navstevyZapis_button = frameLayout.findViewById(R.id.navstevyZapis_button);
        navstevyZapis_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odosli();
            }
        });
        return frameLayout;
    }

    private void odosli() {                                             // pred samotnym odoslanim dat do databazy sa kontroluje ich format a takisto aj heslo
        String datum = navstevyZapis_datum.getText().toString();
        String x[];
        String dovod = navstevyZapis_dovod.getText().toString();
        String id = navstevyZapis_id.getText().toString();
        String heslo = navstevyZapis_heslo.getText().toString();
        String nasledujucaKontrola = navstevyZapis_nasledujucaKontrola.getText().toString();
        if (!(datum.matches("([0-9]{2}).([0-9]{2}).([0-9]{4})") || datum.matches("([0-9]).([0-9]).([0-9]{4})") || datum.matches("([0-9]{2}).([0-9]).([0-9]{4})") || datum.matches("([0-9]).([0-9]{2}).([0-9]{4})"))) {
            Toast.makeText(getActivity(), "Nesprávny formát dátumu", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dovod.equals("")) {
            Toast.makeText(getActivity(), "Nezadal si dôvod", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!id.matches("([0-9]{9})")) {
            Toast.makeText(getActivity(), "Nesprávne číslo čipu", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!heslo.equals(getString(R.string.heslo))) {
            Toast.makeText(getActivity(), "Nesprávne heslo", Toast.LENGTH_SHORT).show();
            return;
        }

        x = datum.split("\\.");
        if (x[0].length() == 1) x[0] = "0" + x[0];
        if (x[1].length() == 1) x[1] = "0" + x[1];

        if (isOnline()) {                   // ak sme online data sa zapisu
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://petapp-d0249-default-rtdb.europe-west1.firebasedatabase.app");
            String reference = "petid/" + id + "/visitation/" + x[2] + x[1] + x[0];
            DatabaseReference myRef = database.getReference(reference + "/date");
            myRef.setValue(datum);

            myRef = database.getReference(reference + "/reason");
            myRef.setValue(dovod);

            if (!nasledujucaKontrola.equals("")) {
                myRef = database.getReference(reference + "/next");
                myRef.setValue(nasledujucaKontrola);
            }
            navstevyZapis_datum.setText("");
            navstevyZapis_dovod.setText("");
            navstevyZapis_nasledujucaKontrola.setText("");
            navstevyZapis_id.setText("");
            navstevyZapis_heslo.setText("");
        }
        else Toast.makeText(getActivity(), "Nemáš pripojenie k internetu", Toast.LENGTH_SHORT).show();
    }

    public boolean isOnline(){                      // pripojenie k internetu zabezpecovanie pomocou pingu, ak je uspesny vrati true inak false
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }
}