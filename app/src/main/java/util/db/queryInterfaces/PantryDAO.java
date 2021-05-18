package util.db.queryInterfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import util.db.entities.Pantry;
import util.db.entities.PantryItem;
import util.db.entities.Shop;

@Dao
public interface PantryDAO {
 @Query("SELECT pantryItem.id id,pantryitem.name as name, pantryitem.quantity as quantity, pantryitem.stock as stock, pantryItem.barcode as barcode " +
         "FROM pantry INNER JOIN pantryitem ON pantry.id = pantryitem.pantryId " +
         "WHERE pantry.name = :pantryName")
    List<util.db.queryInterfaces.PantryItem> getAllItems(String pantryName);

 @Query("SELECT * FROM pantry WHERE pantry.name = :pantryName")
    Pantry getPantry(String pantryName);

 @Query("SELECT * FROM pantryItem WHERE pantryitem.id = :id")
    PantryItem getPantryItem(long id);

 @Query("SELECT * FROM pantryItem WHERE pantryitem.barcode = :barcode")
    PantryItem getPantryItem(String barcode);

 @Query("SELECT * FROM pantry")
    List<Pantry> getAllPantryLists();

 @Query("SELECT * FROM shop")
    List<Shop> getAllShops();

 @Query("SELECT pi.id id, pi.name as name, pi.quantity as quantity, pi.stock as stock, pi.barcode as barcode FROM shop AS s INNER JOIN pantryItem AS pi ON pi.shopId = s.id " +
         " WHERE s.name = :shopName" +
         " AND pi.stock < pi.quantity ")
    List<util.db.queryInterfaces.PantryItem> getAllShopItems(String shopName);

 @Query("DELETE FROM pantry")
    void nukePantries();

 @Query("DELETE FROM PantryItem")
    void nukePantryItems();

 @Query("DELETE FROM Shop")
    void nukeShops();

 @Insert
    void insertPantryItem(PantryItem pantryItem);

 @Insert
    long insertPantry(Pantry pantry);

 @Insert
    long insertShop(Shop shop);

 @Update
 void updatePantryItem(PantryItem pantryItem);




}
