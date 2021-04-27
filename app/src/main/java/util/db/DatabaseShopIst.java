package util.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import util.db.entities.Item;
import util.db.entities.Pantry;
import util.db.entities.PantryList;
import util.db.entities.ShoppingList;
import util.db.queryInterfaces.PantryDAO;
import util.db.queryInterfaces.PantryListDAO;
import util.db.queryInterfaces.ShopDAO;
import util.db.queryInterfaces.ShoppingListDAO;

@Database(entities = {Item.class, Pantry.class, PantryList.class, ShoppingList.class}, version = 1)
public abstract class DatabaseShopIst extends RoomDatabase {
    /*private static final String DB_NAME = "shop_ist";
    private static DatabaseShopIst instance;

    public static synchronized DatabaseShopIst getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), DatabaseShopIst.class, DB_NAME).fallbackToDestructiveMigration().build();
        }

        return instance;
    }*/

    public abstract PantryDAO pantryDAO();
    public abstract PantryListDAO pantryListDAO();
    public abstract ShoppingListDAO shoppingListDAO();
    public abstract ShopDAO shopDAO();
}
