package sk.upjs.ics.android.koncovyprojekt2;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Prvapomoc extends Fragment {
    public Prvapomoc() {
    }
    
    public static Prvapomoc newInstance(String param1, String param2) {
        Prvapomoc fragment = new Prvapomoc();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frameLayout = inflater.inflate(R.layout.fragment_prvapomoc, container, false);
        return frameLayout;
    }

}