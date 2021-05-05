package util.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import util.db.entities.Item;
import util.db.entities.Pantry;
import util.db.entities.PantryItem;
import util.db.entities.Shop;
import util.db.entities.ShopItems;
import util.db.entities.ShoppingList;
import util.db.queryInterfaces.PantryDAO;
import util.db.queryInterfaces.PantryListDAO;
import util.db.queryInterfaces.ShopDAO;
import util.db.queryInterfaces.ShoppingListDAO;

@Database(entities = {Item.class, Pantry.class, PantryItem.class, ShoppingList.class, Shop.class, ShopItems.class}, version = 8)
public abstract class DatabaseShopIst extends RoomDatabase {
    private static final String DB_NAME = "shop_ist";
    private static DatabaseShopIst instance;
    private static final int DATABASE_VERSION = 2;

    public static synchronized DatabaseShopIst getInstance(Context context){
        context.deleteDatabase(DB_NAME);
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), DatabaseShopIst.class, DB_NAME).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
    public abstract ShopDAO shopDAO();
    public abstract PantryDAO pantryDAO();
    public abstract PantryListDAO pantryListDAO();
    public abstract ShoppingListDAO shoppingListDAO();
}
