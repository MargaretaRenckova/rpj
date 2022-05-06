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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NavstevyFragment extends Fragment {
    ListView listViewNavstevy;
    List<Navstevy> listNavstevy = new ArrayList<>();
    ListAdapter_navstevy adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frameLayout = inflater.inflate(R.layout.fragment_navstevy, container, false);
        listViewNavstevy = frameLayout.findViewById(R.id.listViewNavstevy);
        String navstevy_str = settings.getString("NAVSTEVY", "");
        if (!navstevy_str.equals("")) obnovData();
        downloadData();
        return frameLayout;
    }

    private void downloadData() {
        if (MainActivity.getCisloCipu().equals("")) return;             // stahuju sa navstevy zvierata podla jeho cisla cipu
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://petapp-d0249-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference().child("petid/" + MainActivity.getCisloCipu() + "/visitation");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listNavstevy.clear();
                ArrayList<String> x = new ArrayList();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    x.clear();
                    for (DataSnapshot keyNode1 : keyNode.getChildren()) {
                       x.add(keyNode1.getValue().toString());
                    }
                    if (x.size()==2) {
                        listNavstevy.add(new Navstevy(x.get(0), x.get(1), "null"));
                    }
                    else {
                        if (x.size()!=1) listNavstevy.add(new Navstevy(x.get(0), x.get(2), x.get(1)));
                    }
                }
                Collections.reverse(listNavstevy);
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
        String navstevy_str = "";               // ukladanie vo formate "data#data#data|data#data#data|data#data#data", podla tohto formatu sa data obnovuju
        for (int i = 0; i < listNavstevy.size(); i++) {
            if (i != 0) navstevy_str += "|";
            navstevy_str += listNavstevy.get(i).date + "#" + listNavstevy.get(i).reason + "#" + listNavstevy.get(i).next;
        }
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("NAVSTEVY", navstevy_str);
        editor.apply();
    }

    private void obnovData() {                  // obnovovanie dat zo SharedPreferences
        String navstevy_str = settings.getString("NAVSTEVY", "");
        String[] navsteva = navstevy_str.split("\\|");
        for (int i = 0; i < navsteva.length; i++) {
            String []x = navsteva[i].split("#");
            listNavstevy.add(new Navstevy(x[0], x[1], x[2]));
        }
        setData();
    }

    private void setData(){                    // nastavenie adaptera pre zobrazenie v liste
        try {
            adapter = new ListAdapter_navstevy(getContext(), listNavstevy);
            listViewNavstevy.setAdapter(adapter);
        }
        catch (NullPointerException e) {
            Log.w(TAG, "NullPointerExeption", e);
        }
    }
}