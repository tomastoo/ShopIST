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

 @Query("SELECT * FROM pantry WHERE pantry.server_id = :serverId")
    Pantry getPantryServerId(long serverId);

 @Query("SELECT * FROM pantryItem WHERE pantryitem.id = :id")
    PantryItem getPantryItem(long id);

 @Query("SELECT MAX(p.name) name FROM pantry p JOIN pantryitem pi ON p.id = pi.pantryId WHERE pi.id = :id")
    String getPantryNameByItem(long id);

 @Query("SELECT * FROM pantryItem WHERE pantryitem.barcode = :barcode")
    PantryItem getPantryItem(String barcode);

 @Query("SELECT * FROM pantry")
    List<Pantry> getAllPantryLists();

 @Query("SELECT * FROM shop")
    List<Shop> getAllShops();


 @Query("SELECT * FROM shop s WHERE s.id IN (SELECT sh.id FROM shop AS sh INNER JOIN pantryItem AS pi ON pi.shopId = s.id " +
         " WHERE 1 = 1" +
         " AND pi.stock < pi.quantity )")
    List<Shop> getShopsWithItems();

 @Query("SELECT * FROM shop WHERE server_id = :serverid")
    Shop getShopByServerId(long serverid);

 @Query("SELECT * FROM shop WHERE shop.name = :serverName")
    Shop getShop(String serverName);

 @Query("SELECT pi.id id, pi.name as name, pi.quantity as quantity, pi.stock as stock, pi.barcode as barcode FROM shop AS s INNER JOIN pantryItem AS pi ON pi.shopId = s.id " +
         " WHERE s.name = :shopName" +
         " AND pi.stock < pi.quantity ")
    List<util.db.queryInterfaces.PantryItem> getAllShopItems(String shopName);

 @Query("DELETE FROM pantry")
    void nukePantries();
 @Query("DELETE FROM pantry WHERE pantry.server_id = 0")
    void nukeInvalidPantries();

 @Query("DELETE FROM PantryItem")
    void nukePantryItems();

 @Query("DELETE FROM PantryItem WHERE pantryId = :pantryId")
    void nukePantryItemsFromPantryId(long pantryId);

 @Query("DELETE FROM Shop")
    void nukeShops();

 @Query("DELETE FROM pantryitem WHERE pantryitem.id = :id")
    void removePantryItem(long id);

 @Insert
    void insertPantryItem(PantryItem pantryItem);

 @Insert
    long insertPantry(Pantry pantry);

 @Insert
    long insertShop(Shop shop);

 @Update
 void updatePantryItem(PantryItem pantryItem);

 @Update
 void updatePantry(Pantry pantry);

 @Update
 void updateShop(Shop shop);





}
