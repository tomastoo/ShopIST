package util.db.queryInterfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import util.db.entities.PantryList;
import util.db.entities.ShoppingList;

@Dao
public interface ShoppingListDAO {
    @Query("SELECT * FROM shopping_list")
    List<ShoppingList> getAllShoppingLists();

    @Query("SELECT * FROM shopping_list WHERE shopping_list.name = :listName")
    PantryList getShoppingList(String listName);

    @Insert
    void insertShoppingList(ShoppingList shoppingList);

    @Update
    void updateShoppingList(ShoppingList shoppingList);

    @Delete
    void deleteShoppingList(ShoppingList shoppingList);
}
