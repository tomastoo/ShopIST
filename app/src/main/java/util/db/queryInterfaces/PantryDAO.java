package util.db.queryInterfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import util.db.entities.Pantry;
import util.db.entities.PantryItem;
import util.db.entities.Shop;

@Dao
public interface PantryDAO {
 @Query("SELECT pantryitem.name as name, pantryitem.quantity as quantity, pantryitem.stock as stock " +
         "FROM pantry INNER JOIN pantryitem ON pantry.id = pantryitem.pantryId " +
         "WHERE pantry.name = :pantryName")
    List<util.db.queryInterfaces.PantryItem> getAllItems(String pantryName);

 @Query("SELECT * FROM pantry WHERE pantry.name = :pantryName")
    Pantry getPantry(String pantryName);

 @Query("SELECT * FROM pantry")
    List<Pantry> getAllPantryLists();

 @Query("SELECT * FROM shop")
    List<Shop> getAllShops();

 @Query("DELETE FROM pantry")
    void nukePantries();

 @Query("DELETE FROM PantryItem")
    void nukePantryItems();

 @Query("DELETE FROM Shop")
    void nukeShops();

 @Insert
    void insertPantryList(PantryItem pantryItem);

 @Insert
    long insertPantry(Pantry pantry);

 @Insert
    long insertShop(Shop shop);
}
