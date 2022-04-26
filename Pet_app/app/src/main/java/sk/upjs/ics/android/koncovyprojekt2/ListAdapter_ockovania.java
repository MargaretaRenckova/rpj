package sk.upjs.ics.android.koncovyprojekt2;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class ListAdapter_ockovania extends ArrayAdapter<Ockovania> {
    private Context activityContext;
    private List<Ockovania> list;
    public static final String TAG = "ListView";

    public ListAdapter_ockovania(Context context, List<Ockovania> list){
        super(context, R.layout.list_item, list);
        this.activityContext = context;
        this.list = list;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup){

        final ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(activityContext).inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.date = view.findViewById(R.id.date);
            viewHolder.vaccine = view.findViewById(R.id.vaccine);
            viewHolder.next = view.findViewById(R.id.next);

            viewHolder.date.setText("Dátum očkovania: " + list.get(position).date);
            viewHolder.vaccine.setText("Nasledujúce očkovanie: " + list.get(position).vaccine);
            viewHolder.next.setText("Vakcína: " + list.get(position).next);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        return view;
    }

    private static class ViewHolder {
        TextView date;
        TextView vaccine;
        TextView next;
    }
}
