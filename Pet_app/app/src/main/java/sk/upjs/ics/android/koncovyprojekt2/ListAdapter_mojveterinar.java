package sk.upjs.ics.android.koncovyprojekt2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.List;

public class ListAdapter_mojveterinar extends ArrayAdapter<MojVeterinar> {
    private Context activityContext;
    private List<MojVeterinar> list;
    private FragmentActivity activity;

    public ListAdapter_mojveterinar(Context context, List<MojVeterinar> list, FragmentActivity activity) {
        super(context, R.layout.list_item_button, list);
        this.activityContext = context;
        this.list = list;
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        final ListAdapter_mojveterinar.ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(activityContext).inflate(R.layout.list_item_button, viewGroup, false);
            viewHolder = new ListAdapter_mojveterinar.ViewHolder();
            viewHolder.button = view.findViewById(R.id.button);
            viewHolder.button.setText("MVDr. " + list.get(position).firstname + " " + list.get(position).lastname);

            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VeterinarFragment nextFrag= new VeterinarFragment(list.get(position));
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, nextFrag)
                            .commit();
                }
            });
            view.setTag(viewHolder);
        } else {
            viewHolder = (ListAdapter_mojveterinar.ViewHolder) view.getTag();
        }

        return view;
    }

    private static class ViewHolder {
        Button button;
    }
}
