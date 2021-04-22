package util.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import util.db.entities.Item;
import util.db.entities.Pantry;
import util.db.entities.PantryList;
import util.db.queryInterfaces.PantryDAO;
import util.db.queryInterfaces.ShopDAO;

@Database(entities = {Item.class, Pantry.class, PantryList.class}, version = 2)
public abstract class DatabaseShopIst extends RoomDatabase {
    public abstract PantryDAO pantryDAO();
    public abstract ShopDAO shopDAO();
}
