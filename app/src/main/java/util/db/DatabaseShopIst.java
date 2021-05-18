package util.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import util.db.entities.Pantry;
import util.db.entities.PantryItem;
import util.db.entities.Shop;
import util.db.queryInterfaces.PantryDAO;

@Database(entities = { Pantry.class, PantryItem.class, Shop.class}, version = 4)
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

    public abstract PantryDAO pantryDAO();
}
