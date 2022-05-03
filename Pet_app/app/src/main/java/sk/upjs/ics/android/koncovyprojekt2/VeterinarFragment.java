package sk.upjs.ics.android.koncovyprojekt2;

import android.os.Bundle;

import androidx.annotation.DrawableRes;
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
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.heart) {
            item.setIcon(R.drawable.heart);
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