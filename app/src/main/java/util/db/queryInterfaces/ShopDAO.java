package util.db.queryInterfaces;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import util.db.entities.Pantry;

@Dao
public interface ShopDAO {
 /*@Query("SELECT item.name as name, pantry_list.quantity as quantity, pantry_list.stock as stock " +
         "FROM item INNER JOIN pantry_list ON item.id = pantry_list.itemId " +
         "INNER JOIN pantry ON pantry.id = pantry_list.pantryId " +
         "WHERE pantry.name = :shopName")
    List<ShopItem> getAllItems(String shopName);

 @Query("SELECT * FROM Shop WHERE shop.name = :shopName")
    Pantry getShop(String shopName);*/
}
