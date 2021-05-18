package pt.ulisboa.tecnico.cmov.shopist;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import util.db.entities.PantryItem;
import util.db.queryInterfaces.ShopItem;
import util.main.SharedClass;

public class BasketList extends AppCompatActivity {
    private SharedClass sharedClass;
    private String shopName;
    private void fillList() {
        final StableAdapter adapter = new StableAdapter(this);
        ListView listView =  findViewById(R.id.ListViewBasketList);

        listView.setAdapter(adapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_basketlist);
        sharedClass = (SharedClass)getApplicationContext();
        AsyncTask.execute(this::fillList);
        FloatingActionButton checkoutBasket = (FloatingActionButton)findViewById(R.id.floatingCheckOutBasketButton);

        checkoutBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        for (ShopItem si: SharedClass.getBasketList()) {
                                PantryItem pi = sharedClass.instanceDb().pantryDAO().getPantryItem(si.id);
                                pi.stock = pi.quantity;
                                sharedClass.instanceDb().pantryDAO().updatePantryItem(pi);
                            }
                        SharedClass.getBasketList().clear();
                        BasketList.this.finish();
                    }
                });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AsyncTask.execute(this::fillList);
    }

    class StableAdapter extends BaseAdapter {
        Context context;
        String[] data;
        ArrayList<ShopItem> shoppingItems = new ArrayList<>();
        private LayoutInflater inflater = null;


        public StableAdapter(Context context) {
            shoppingItems.addAll(SharedClass.getBasketList());

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
                vi = inflater.inflate(R.layout.item_basket_list, null);

            ShopItem si = shoppingItems.get(position);

            TextView text = (TextView) vi.findViewById(R.id.textItemName);
            text.setMaxLines(1);
            text.setEllipsize(TextUtils.TruncateAt.END);
            text.setText(si.name);

            ((TextView) vi.findViewById(R.id.textItemQuantities)).setText(String.format("%s un.", si.quantity));

            View finalVi = vi;
            ((ImageButton)vi.findViewById(R.id.removeBasketItem)).setOnClickListener((View.OnClickListener) v -> {
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

            ((ImageButton)vi.findViewById(R.id.editBasketItem)).setOnClickListener((View.OnClickListener) v -> {
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