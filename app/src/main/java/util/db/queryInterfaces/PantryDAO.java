package util.db.queryInterfaces;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

import util.db.entities.Item;
import util.db.entities.Pantry;
import util.db.entities.PantryList;

@Dao
public interface PantryDAO {
    /*@Query("SELECT item.name as name, pantry_list.quantity as quantity, pantry_list.stock as stock " +
         "FROM item INNER JOIN pantry_list ON item.id = pantry_list.itemId " +
         "INNER JOIN pantry ON pantry.id = pantry_list.pantryId " +
         "WHERE pantry.name = :pantryName")
    List<PantryItem> getAllItems(String pantryName);*/

    @Query("SELECT * FROM pantry WHERE pantry.name = :pantryName")
    Pantry getPantry(String pantryName);
}
