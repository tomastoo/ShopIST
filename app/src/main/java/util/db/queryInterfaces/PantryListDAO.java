package util.db.queryInterfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import util.db.entities.PantryItem;

@Dao
public interface PantryListDAO {
    @Query("SELECT * FROM PantryItem")
    List<PantryItem> getAllPantryLists();
/*
    @Query("SELECT * FROM pantry_list WHERE pantry_list.name = :listName")
    PantryList getPantryList(String listName);
*/
    @Insert
    void insertPantryList(PantryItem pantryItem);

    @Update
    void updatePantryList(PantryItem pantryItem);

    @Delete
    void deletePantryList(PantryItem pantryItem);
}
