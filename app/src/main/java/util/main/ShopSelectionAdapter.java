package util.main;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.shopist.R;
import util.db.entities.Shop;
import util.db.queryInterfaces.PantryItem;

public class ShopSelectionAdapter extends ArrayAdapter<Shop> {
    public ShopSelectionAdapter(Context context, ArrayList<Shop> shopArrayList) {
        super(context, 0, shopArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView,
                          ViewGroup parent) {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_shop, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.txt_view_shop_name);
        Shop shop = getItem(position);

        // It is used the name to the TextView when the
        // current item is not null.
        if (shop != null) {
            textViewName.setText(shop.name);
        }
        return convertView;
    }
}