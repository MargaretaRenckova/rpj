package sk.upjs.ics.android.koncovyprojekt2;

import static android.content.ContentValues.TAG;
import static sk.upjs.ics.android.koncovyprojekt2.MainActivity.settings;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    TextView home, breed, color, gender, owner, id, birthdate, name;
    ArrayList<String> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View frameLayout = inflater.inflate(R.layout.fragment_home, container, false);
        birthdate = frameLayout.findViewById(R.id.birthdate);
        breed = frameLayout.findViewById(R.id.breed);
        color = frameLayout.findViewById(R.id.color);
        gender = frameLayout.findViewById(R.id.gender);
        home = frameLayout.findViewById(R.id.home);
        name = frameLayout.findViewById(R.id.name);
        owner = frameLayout.findViewById(R.id.owner);
        id = frameLayout.findViewById(R.id.id);
        downloadData();
        setData();
        return frameLayout;
    }

    private void setData(){
        birthdate.setText(settings.getString("BIRTHDATE", ""));
        breed.setText(settings.getString("BREED", ""));
        color.setText(settings.getString("COLOR", ""));
        gender.setText(settings.getString("GENDER", ""));
        home.setText(settings.getString("HOME", ""));
        name.setText(settings.getString("NAME", ""));
        owner.setText(settings.getString("OWNER", ""));
        id.setText(MainActivity.getCisloCipu());
    }

    private void downloadData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://petapp-d0249-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference().child("petid/" +MainActivity.getCisloCipu());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot keyNode: dataSnapshot.getChildren()) {
                    list.add(keyNode.getValue().toString());
                }
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("BIRTHDATE", list.get(0));
                editor.putString("BREED", list.get(1));
                editor.putString("COLOR", list.get(2));
                editor.putString("GENDER", list.get(3));
                editor.putString("HOME", list.get(4));
                editor.putString("NAME", list.get(5));
                editor.putString("OWNER", list.get(6));
                editor.apply();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
