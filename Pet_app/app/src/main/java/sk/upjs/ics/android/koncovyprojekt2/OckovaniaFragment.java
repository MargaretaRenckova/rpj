package sk.upjs.ics.android.koncovyprojekt2;

import static android.content.ContentValues.TAG;
import static sk.upjs.ics.android.koncovyprojekt2.MainActivity.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class OckovaniaFragment extends Fragment {
    ListView listViewOckovania;
    List<Ockovania> listOckovania = new ArrayList<>();
    ListAdapter_ockovania adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frameLayout = inflater.inflate(R.layout.fragment_ockovania, container, false);
        listViewOckovania = frameLayout.findViewById(R.id.listViewOckovania);
        String ockovania_str = settings.getString("OCKOVANIA", "");
        if (!ockovania_str.equals("")) obnovData();
        downloadData();
        return frameLayout;
    }

    private void downloadData() {
        if (MainActivity.getCisloCipu().equals("")) return;             // stahuju sa ockovania zvierata podla jeho cisla cipu
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://petapp-d0249-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference().child("petid/" + MainActivity.getCisloCipu() + "/vaccination");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listOckovania.clear();
                ArrayList<String> x = new ArrayList();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    x.clear();
                    for (DataSnapshot keyNode1 : keyNode.getChildren()) {
                        x.add(keyNode1.getValue().toString());
                    }
                    String string = x.get(0) + "#" + x.get(1) + "#" + x.get(2);
                    listOckovania.add(new Ockovania(string));
                }
                Collections.reverse(listOckovania);
                ulozData();
                setData();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void ulozData() {                   // ukladanie dat do SharedPreferences
        String ockovania_str = "";              // ukladanie vo formate "data#data#data|data#data#data|data#data#data", podla tohto formatu sa data obnovuju
        for (int i = 0; i < listOckovania.size(); i++) {
            if (i != 0) ockovania_str += "|";
            ockovania_str += listOckovania.get(i).date + "#" + listOckovania.get(i).vaccine + "#" + listOckovania.get(i).next;
        }
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("OCKOVANIA", ockovania_str);
        editor.apply();
    }

    private void obnovData() {                  // obnovovanie dat zo SharedPreferences
        String ockovania_str = settings.getString("OCKOVANIA", "");
        String[] x = ockovania_str.split("\\|");
        for (int i = 0; i < x.length; i++) {
            listOckovania.add(new Ockovania(x[i]));
        }
        setData();
    }

    private void setData() {                    // nastavenie adaptera pre zobrazenie v liste
        try {
            adapter = new ListAdapter_ockovania(getContext(), listOckovania);
            listViewOckovania.setAdapter(adapter);
        }
        catch (NullPointerException e) {
            Log.w(TAG, "NullPointerExeption", e);
        }

    }
}