package sk.upjs.ics.android.koncovyprojekt2;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ms.square.android.expandabletextview.ExpandableTextView;

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
        ExpandableTextView textView = (ExpandableTextView) frameLayout.findViewById(R.id.BalicekPrvaPomoc);
        textView.setText(getString(R.string.balicek_prvej_pomoci));

        ExpandableTextView textView2 = (ExpandableTextView) frameLayout.findViewById(R.id.expand_tex_view2);
        textView2.setText(getString(R.string.zranenie1));

        ExpandableTextView textView3 = (ExpandableTextView) frameLayout.findViewById(R.id.expand_tex_view3);
        textView3.setText(getString(R.string.zranenie2));

        ExpandableTextView textView4 = (ExpandableTextView) frameLayout.findViewById(R.id.expand_tex_view4);
        textView4.setText(getString(R.string.zranenie3));

        ExpandableTextView textView5 = (ExpandableTextView) frameLayout.findViewById(R.id.expand_tex_view5);
        textView5.setText(getString(R.string.zlomenina1));

        ExpandableTextView textView6 = (ExpandableTextView) frameLayout.findViewById(R.id.expand_tex_view6);
        textView6.setText(getString(R.string.zlomenina2));

        ExpandableTextView textView7 = (ExpandableTextView) frameLayout.findViewById(R.id.expand_tex_view7);
        textView7.setText(getString(R.string.ustipnutie1));

        ExpandableTextView textView8 = (ExpandableTextView) frameLayout.findViewById(R.id.expand_tex_view8);
        textView8.setText(getString(R.string.ustipnutie2));

        return frameLayout;
    }

}