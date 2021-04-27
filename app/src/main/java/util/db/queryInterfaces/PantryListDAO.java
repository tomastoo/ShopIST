package util.db.queryInterfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import util.db.entities.PantryList;

@Dao
public interface PantryListDAO {
    @Query("SELECT * FROM pantry_list")
    List<PantryList> getAllPantryLists();

    @Query("SELECT * FROM pantry_list WHERE pantry_list.name = :listName")
    PantryList getPantryList(String listName);

    @Insert
    void insertPantryList(PantryList pantryList);

    @Update
    void updatePantryList(PantryList pantryList);

    @Delete
    void deletePantryList(PantryList pantryList);
}
