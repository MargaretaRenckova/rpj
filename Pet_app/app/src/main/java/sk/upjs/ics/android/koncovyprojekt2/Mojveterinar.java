package sk.upjs.ics.android.koncovyprojekt2;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Mojveterinar extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Button vet1;


    public Mojveterinar() {
        // Required empty public constructor
    }

    public static Mojveterinar newInstance(String param1, String param2) {
        Mojveterinar fragment = new Mojveterinar();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View frameLayout = inflater.inflate(R.layout.fragment_mojveterinar, container, false);
        vet1=frameLayout.findViewById(R.id.vet1);
        vet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // LayoutInflater inflanter = getLayoutInflater();
                //View x = inflanter.inflate(R.layout.veterinar, null);


            }
        });
        return frameLayout;
    }

   


}