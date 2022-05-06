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
import java.util.List;


public class MojveterinarFragment extends Fragment {
    ListView listVeterinariView;
    List<MojVeterinar> listVeterinari = new ArrayList<>();
    ListAdapter_mojveterinar adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frameLayout = inflater.inflate(R.layout.fragment_mojveterinar, container, false);
        listVeterinariView=frameLayout.findViewById(R.id.listVeterinari);
        String veterinari_str = settings.getString("VETERINARI", "");
        if (!veterinari_str.equals("")) obnovData();
        downloadData();
        return frameLayout;
    }

    private void downloadData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://petapp-d0249-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference().child("veterinarians");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listVeterinari.clear();
                ArrayList<String> x = new ArrayList();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    x.clear();
                    for (DataSnapshot keyNode1 : keyNode.getChildren()) {
                        x.add(keyNode1.getValue().toString());
                    }
                    listVeterinari.add(new MojVeterinar(x.get(0), x.get(1), x.get(2), x.get(3), x.get(4), x.get(5)));
                }
                ulozData();
                setData();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void ulozData() {
        String veterinari_str = "";
        for (int i = 0; i < listVeterinari.size(); i++) {
            if (i != 0) veterinari_str += "|";
            veterinari_str += listVeterinari.get(i).firstname + "#" + listVeterinari.get(i).lastname + "#" + listVeterinari.get(i).address + "#" + listVeterinari.get(i).phone+ "#" + listVeterinari.get(i).email+ "#" + listVeterinari.get(i).imageUrl;
        }
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("VETERINARI", veterinari_str);
        editor.apply();
    }

    private void obnovData() {
        String veterinari_str = settings.getString("VETERINARI", "");
        String[] veterinar = veterinari_str.split("\\|");
        for (int i = 0; i < veterinar.length; i++) {
            String []x = veterinar[i].split("#");
            listVeterinari.add(new MojVeterinar(x[2], x[4], x[0], x[5], x[1], x[3]));
        }
        setData();
    }

    private void setData() {
        try {
            adapter = new ListAdapter_mojveterinar(getContext(), listVeterinari, this.getActivity());
            listVeterinariView.setAdapter(adapter);
        }
        catch (NullPointerException e) {
            Log.w(TAG, "NullPointerExeption", e);
        }
    }
}