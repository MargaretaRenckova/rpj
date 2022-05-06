package sk.upjs.ics.android.koncovyprojekt2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import static sk.upjs.ics.android.koncovyprojekt2.MainActivity.settings;


public class PoznamkyFragment extends Fragment {
    EditText poznamky;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View frameLayout = inflater.inflate(R.layout.fragment_poznamky, container, false);
        poznamky = frameLayout.findViewById(R.id.poznamky);
        poznamky.setText(settings.getString("POZNAMKY", ""));       // nacitanie obsahu poznamok
        return frameLayout;
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("POZNAMKY", poznamky.getText().toString());            // ukladanie obsahu poznamok
        editor.apply();

    }
}