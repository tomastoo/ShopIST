package util.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import util.db.entities.Item;
import util.db.entities.Pantry;
import util.db.entities.PantryList;
import util.db.queryInterfaces.PantryDAO;

@Database(entities = {Item.class, Pantry.class, PantryList.class}, version = 1)
public abstract class DatabaseShopIst extends RoomDatabase {
    public abstract PantryDAO pantryDAO();
}