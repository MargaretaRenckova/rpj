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
    EditText ockovaniaZapis_datum;
    EditText ockovaniaZapis_dovod;
    EditText ockovaniaZapis_nasledujucaKontrola;
    EditText ockovaniaZapis_id;
    EditText ockovaniaZapis_heslo;
    private Button ockovaniaZapis_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frameLayout = inflater.inflate(R.layout.fragment_zapis_navstevy, container, false);
        ockovaniaZapis_datum = frameLayout.findViewById(R.id.ockovaniaZapis_datum);
        ockovaniaZapis_dovod = frameLayout.findViewById(R.id.ockovaniaZapis_dovod);
        ockovaniaZapis_nasledujucaKontrola = frameLayout.findViewById(R.id.ockovaniaZapis_nasledujucaKontrola);
        ockovaniaZapis_id = frameLayout.findViewById(R.id.ockovaniaZapis_id);
        ockovaniaZapis_heslo = frameLayout.findViewById(R.id.ockovaniaZapis_heslo);
        ockovaniaZapis_button = frameLayout.findViewById(R.id.ockovaniaZapis_button);
        ockovaniaZapis_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                odosli();
            }
        });
        return frameLayout;
    }

    private void odosli() {
        String datum = ockovaniaZapis_datum.getText().toString();
        String x[];
        String dovod = ockovaniaZapis_dovod.getText().toString();
        String id = ockovaniaZapis_id.getText().toString();
        String heslo = ockovaniaZapis_heslo.getText().toString();
        String nasledujucaKontrola = ockovaniaZapis_nasledujucaKontrola.getText().toString();
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

        if (isOnline()) {
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
            ockovaniaZapis_datum.setText("");
            ockovaniaZapis_dovod.setText("");
            ockovaniaZapis_nasledujucaKontrola.setText("");
            ockovaniaZapis_id.setText("");
            ockovaniaZapis_heslo.setText("");
        }
        else Toast.makeText(getActivity(), "Nemáš pripojenie k internetu", Toast.LENGTH_SHORT).show();
    }

    public boolean isOnline(){
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