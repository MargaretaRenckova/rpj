package sk.upjs.ics.android.koncovyprojekt2;

import static sk.upjs.ics.android.koncovyprojekt2.MainActivity.settings;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class VeterinarFragment extends Fragment {
    private MojVeterinar veterinar;
    private TextView meno;
    private TextView adresa;
    private TextView telefon;
    private TextView email;
    private ImageView image;

    public VeterinarFragment(MojVeterinar veterinar) {
        this.veterinar = veterinar;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frameLayout = inflater.inflate(R.layout.fragment_veterinar, container, false);
        image = frameLayout.findViewById(R.id.veterinarImage);
        meno = frameLayout.findViewById(R.id.menoapriezvisko);
        adresa = frameLayout.findViewById(R.id.adresa);
        telefon = frameLayout.findViewById(R.id.telefon);
        email = frameLayout.findViewById(R.id.email);
        meno.setText("MVDr. " + veterinar.firstname + " " + veterinar.lastname);
        adresa.setText(veterinar.address);
        telefon.setText(veterinar.phone);
        email.setText(veterinar.email);
        Picasso.get().load(veterinar.imageUrl).into(image);
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
        return frameLayout;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.doctor).setVisible(false);
        menu.findItem(R.id.heart).setVisible(true);
        menu.findItem(R.id.arrow).setVisible(true);

        try {
            if (MainActivity.getOblubenyVeterinar().toString().equals(veterinar.toString())) menu.findItem(R.id.heart).setIcon(R.drawable.heart);
            else menu.findItem(R.id.heart).setIcon(R.drawable.heart_line);
        }
        catch (NullPointerException e) {
            menu.findItem(R.id.heart).setIcon(R.drawable.heart_line);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.heart) {
            if (String.valueOf(item.getTitle()).equals("heart")) {
                item.setIcon(R.drawable.heart_line);
                item.setTitle("heart_line");
                MainActivity.setOblubenyVeterinar(null);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("OBLUBENY_VETERINAR", null);
                editor.apply();
            }
            else {
                item.setIcon(R.drawable.heart);
                item.setTitle("heart");
                MainActivity.setOblubenyVeterinar(veterinar);
                String veterinar_str = veterinar.firstname + "#" + veterinar.lastname + "#" + veterinar.address + "#" + veterinar.phone + "#" + veterinar.email + "#" + veterinar.imageUrl;
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("OBLUBENY_VETERINAR", veterinar_str);
                editor.apply();
            }
        }
        if (id == R.id.arrow) {
            MojveterinarFragment nextFrag= new MojveterinarFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, nextFrag)
                    .commit();
        }
        return super.onOptionsItemSelected(item);
    }
}