package sk.upjs.ics.android.koncovyprojekt2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.List;

public class ListAdapter_mojveterinar extends ArrayAdapter<MojVeterinar> {
    private Context activityContext;
    private List<MojVeterinar> list;

    public ListAdapter_mojveterinar(Context context, List<MojVeterinar> list) {
        super(context, R.layout.list_item_button, list);
        this.activityContext = context;
        this.list = list;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        final ListAdapter_mojveterinar.ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(activityContext).inflate(R.layout.list_item_button, null);
            viewHolder = new ListAdapter_mojveterinar.ViewHolder();
            viewHolder.button = view.findViewById(R.id.button);
            viewHolder.button.setText("MVDr. " + list.get(position).firstname + " " + list.get(position).lastname);
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
