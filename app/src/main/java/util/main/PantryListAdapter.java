package util.main;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.shopist.R;
import util.db.queryInterfaces.PantryItem;

public class PantryListAdapter extends ArrayAdapter<PantryItem> {
    public PantryListAdapter(Context context, ArrayList<PantryItem> pantryItemArrayList){
        super(context, 0, pantryItemArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PantryItem pantryItem= getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_pantry_list, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.txt_view_name_pantry_item);
        name.setText(pantryItem.name);
        name.setEllipsize(TextUtils.TruncateAt.END);

        TextView stockQt = (TextView) convertView.findViewById(R.id.txt_view_stock_quantity);
        String stockQtString = pantryItem.stock + " / " + pantryItem.quantity;
        stockQt.setText(stockQtString);

        return convertView;
    }
}

