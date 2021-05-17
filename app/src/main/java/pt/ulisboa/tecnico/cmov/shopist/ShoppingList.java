package pt.ulisboa.tecnico.cmov.shopist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import util.db.queryInterfaces.PantryItem;
import util.db.queryInterfaces.ShopItem;
import util.main.SharedClass;

public class ShoppingList extends AppCompatActivity {
    private SharedClass sharedClass;
    private String shopName;
    private void fillList() {
        ListView listView =  findViewById(R.id.ListViewShoppingList);

        //ShopDAO shopDAO = sharedClass.dbShopIst.shopDAO();

       final StableAdapter adapter = new StableAdapter(this, sharedClass.instanceDb().pantryDAO().getAllShopItems(shopName));
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

        });*/
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        sharedClass = (SharedClass)getApplicationContext();

        Intent intent = getIntent();
        shopName = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(shopName);
        AsyncTask.execute(this::fillList);

        ImageButton ib =(ImageButton) findViewById(R.id.imageBasketButton);

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ShoppingList.this, BasketList.class);
                startActivity(i);
            }
        });

    }

    class StableAdapter extends BaseAdapter {



        Context context;
        String[] data;
        ArrayList<ShopItem> shoppingItems = new ArrayList<>();
        private LayoutInflater inflater = null;


        public StableAdapter(Context context, List<util.db.queryInterfaces.PantryItem> data) {
            // TODO Auto-generated constructor stub
            this.context = context;


            for (PantryItem s : data ) {
                shoppingItems.add(new ShopItem(s.name, String.valueOf(s.quantity-s.stock)));
            }

            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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

            ShopItem si = shoppingItems.get(position);

            TextView text = (TextView) vi.findViewById(R.id.textItemName);
            text.setMaxLines(1);
            text.setEllipsize(TextUtils.TruncateAt.END);
            text.setText(si.name);

            ((TextView) vi.findViewById(R.id.textItemQuantities)).setText(String.format("%s un.", si.quantity));

            View finalVi = vi;
            ((ImageButton)vi.findViewById(R.id.shoppingCart)).setOnClickListener((View.OnClickListener) v -> {
/*           pw = new PopupWindow(ShoppingList.this);
             pw.showAtLocation(finalVi, Gravity.CENTER,0,0);
 */
                finalVi.animate().setDuration(1000).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        SharedClass.getBasketList().add(shoppingItems.remove(position));
                        StableAdapter.this.notifyDataSetChanged();
                        finalVi.setAlpha(1);
                    }
                });

            });

            return vi;
        }
    }



}