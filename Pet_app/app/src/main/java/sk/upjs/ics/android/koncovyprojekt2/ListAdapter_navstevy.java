package sk.upjs.ics.android.koncovyprojekt2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter_navstevy extends ArrayAdapter<Navstevy> {
    private Context activityContext;
    private List<Navstevy> list;

    public ListAdapter_navstevy(Context context, List<Navstevy> list) {
        super(context, R.layout.list_item, list);
        this.activityContext = context;
        this.list = list;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        final ListAdapter_navstevy.ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(activityContext).inflate(R.layout.list_item, null);
            viewHolder = new ListAdapter_navstevy.ViewHolder();
            viewHolder.date = view.findViewById(R.id.item1);
            viewHolder.reason = view.findViewById(R.id.item2);
            viewHolder.next = view.findViewById(R.id.item3);
            viewHolder.date.setText("Dátum: " + list.get(position).date);
            viewHolder.reason.setText("Dôvod: " + list.get(position).reason);
            if (!list.get(position).next.equals("null")) viewHolder.next.setText("Najbližšia kontrola: " + list.get(position).next);
            else viewHolder.next.setVisibility(View.GONE);          // nie vzdy je uvedena aj dalsia kontrola zvierata, ak teda nie je uvedena, tak sa nezobrazi ani pole na tuto informaciu urcene
            view.setTag(viewHolder);
        } else {
            viewHolder = (ListAdapter_navstevy.ViewHolder) view.getTag();
        }

        return view;
    }

    private static class ViewHolder {
        TextView date;
        TextView reason;
        TextView next;
    }
}
