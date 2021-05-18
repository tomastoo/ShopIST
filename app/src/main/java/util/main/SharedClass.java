package util.main;

import android.app.Application;
import android.provider.Settings;

import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import util.ServerSync.ServerInterface;
import util.db.DatabaseShopIst;
import util.db.queryInterfaces.PantryItem;
import util.db.queryInterfaces.ShopItem;


public class SharedClass extends Application {

    private static DatabaseShopIst dbShopIst = null;
    private static List<ShopItem> basketList = null;
    public DatabaseShopIst instanceDb() {
        if(dbShopIst == null) {
            dbShopIst = Room.databaseBuilder(this, DatabaseShopIst.class, "shopIstDb").fallbackToDestructiveMigration().build();
            //this.updateLocalDB();
        }
        return dbShopIst;
    }

    public void updateLocalDB() {
        String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        ServerInterface.getInstance(this).getPantries();
        ServerInterface.getInstance(this).getShops();
    }

    public static List<ShopItem> getBasketList() {
        if(basketList == null) {
            basketList = new ArrayList<ShopItem>();
        }
        return basketList;
    }
}
