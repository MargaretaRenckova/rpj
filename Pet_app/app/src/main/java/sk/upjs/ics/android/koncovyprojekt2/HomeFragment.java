package sk.upjs.ics.android.koncovyprojekt2;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static sk.upjs.ics.android.koncovyprojekt2.MainActivity.settings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    TextView home, breed, color, gender, owner, id, birthdate, name;
    ImageView imageDog;
    private static final int IMAGE_PICK_CODE = 1000;
    ArrayList<String> list = new ArrayList<>();

    @SuppressLint("IntentReset")
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
        imageDog = frameLayout.findViewById(R.id.imageDog);
        String img_str = settings.getString("IMG", "");
        if (!img_str.equals("")) {
            byte[] imageAsBytes = Base64.decode(img_str.getBytes(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            Drawable d = new BitmapDrawable(getResources(), bitmap);
            imageDog.setImageDrawable(d);
        }
        imageDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        downloadData();
        setData();
        return frameLayout;
    }

    private void setData() {
        birthdate.setText(settings.getString("BIRTHDATE", ""));
        breed.setText(settings.getString("BREED", ""));
        color.setText(settings.getString("COLOR", ""));
        gender.setText(settings.getString("GENDER", ""));
        home.setText(settings.getString("HOME", ""));
        name.setText(settings.getString("NAME", ""));
        owner.setText(settings.getString("OWNER", ""));
        id.setText(MainActivity.getCisloCipu());
    }

    private void downloadData() {
        if (MainActivity.getCisloCipu().equals("")) return;
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://petapp-d0249-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference().child("petid/" + MainActivity.getCisloCipu());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
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
                setData();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = {"Vybrať obrázok z galérie", "Vymazať fotku", "Zrušiť"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Pridaj si fotku miláčika!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Vybrať obrázok z galérie")) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, IMAGE_PICK_CODE);
                } else if (options[item].equals("Vymazať fotku")){
                    imageDog.setImageResource(R.drawable.dog);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("IMG", "");
                    editor.apply();
                } else if (options[item].equals("Zrušiť")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageDog.setImageURI(data.getData());
            imageDog.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageDog.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] image = stream.toByteArray();
            String img_str = Base64.encodeToString(image, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("IMG", img_str);
            editor.apply();
        }
    }
}