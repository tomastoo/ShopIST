package util.db.queryInterfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import util.db.entities.Item;
import util.db.entities.Pantry;
import util.db.entities.PantryItem;

@Dao
public interface PantryDAO {
 @Query("SELECT item.name as name, PantryItem.quantity as quantity, PantryItem.stock as stock " +
         "FROM item INNER JOIN PantryItem ON item.id = PantryItem.itemId " +
         "INNER JOIN pantry ON pantry.id = PantryItem.pantryId " +
         "WHERE pantry.name = :pantryName")
    List<util.db.queryInterfaces.PantryItem> getAllItems(String pantryName);

 @Query("SELECT * FROM pantry WHERE pantry.name = :pantryName")
    Pantry getPantry(String pantryName);

 @Query("SELECT * FROM pantry")
    List<Pantry> getAllPantryLists();


 @Query("DELETE FROM item")
    void nukeItems();
 @Query("DELETE FROM pantry")
    void nukePantries();
 @Query("DELETE FROM PantryItem")
    void nukePantryItems();

 @Insert
    long insertItem(Item item);

 @Insert
    void insertPantryList(PantryItem pantryItem);

 @Insert
    long insertPantry(Pantry pantry);
}
