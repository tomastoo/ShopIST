package pt.ulisboa.tecnico.cmov.shopist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.db.queryInterfaces.ShopDAO;
import util.db.queryInterfaces.ShopItem;
import util.main.SharedClass;

public class ShoppingList extends AppCompatActivity {
    private SharedClass sharedClass;
    private String shopName;
    private void fillList() {
        ListView listView =  findViewById(R.id.ListViewShoppingList);

        ShopDAO shopDAO = sharedClass.dbShopIst.shopDAO();

        final StableAdapter adapter = new StableAdapter(this, shopDAO);
        listView.setAdapter(adapter);

       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }

        }); */
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        sharedClass = (SharedClass)getApplicationContext();
        shopName = "Shop1"; //TODO : Buscar por intent da main activity
        fillList();
    }

    class StableAdapter extends BaseAdapter {

        class ShoppingItem {
            String name;
            String quantity;
            String price;
            Boolean inManyPantryLists;

            public ShoppingItem(String name,String quantity , String price, Boolean inManyPantryLists) {
                this.name = name;
                this.quantity = quantity;
                this.price = price;
                this.inManyPantryLists = inManyPantryLists;

            }
            public ShoppingItem(String name,String quantity , String price) {
                this.name = name;
                this.quantity = quantity;
                this.price = price;
                inManyPantryLists = false;
            }
        }

        Context context;
        String[] data;
        ArrayList<ShoppingItem> shoppingItems = new ArrayList<>();
        private LayoutInflater inflater = null;

        public StableAdapter(Context context, ShopDAO data) {
            // TODO Auto-generated constructor stub
            this.context = context;

            /*for (ShopItem s : data.getAllItems() ) {
                shoppingItems.add(new ShoppingItem(s.name, String.valueOf(s.quantity), String.valueOf(s.price)));
            }
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return shoppingItems.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return shoppingItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View vi = convertView;
            if (vi == null)
                vi = inflater.inflate(R.layout.item_shopping_list, null);

            ShoppingItem si = shoppingItems.get(position);

            TextView text = (TextView) vi.findViewById(R.id.textItemName);
            text.setMaxLines(1);
            text.setEllipsize(TextUtils.TruncateAt.END);
            text.setText(si.name);

            ((TextView) vi.findViewById(R.id.textItemQuantities)).setText(String.format("%s un. / %s €", si.quantity, si.price));

            View finalVi = vi;
            ((ImageButton)vi.findViewById(R.id.shoppingCart)).setOnClickListener((View.OnClickListener) v -> {
/*           pw = new PopupWindow(ShoppingList.this);
             pw.showAtLocation(finalVi, Gravity.CENTER,0,0);
 */
                finalVi.animate().setDuration(1000).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        shoppingItems.remove(position);
                        StableAdapter.this.notifyDataSetChanged();
                        finalVi.setAlpha(1);
                    }
                });

            });

            return vi;
        }
    }



}