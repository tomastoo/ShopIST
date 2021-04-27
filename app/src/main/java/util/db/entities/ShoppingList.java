package util.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "shopping_list",
        /*foreignKeys = {
        @ForeignKey(entity = Pantry.class, parentColumns = "id" , childColumns = "pantryId"),
        @ForeignKey(entity = Item.class, parentColumns = "id", childColumns = "itemId")
},*/ indices = {@Index(value = "name", unique = true)})
public class ShoppingList {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    /*@ColumnInfo(index = true)
    public int pantryId;

    @ColumnInfo(index = true)
    public int itemId;*/

    @ColumnInfo(name = "quantity")
    public int quantity;

    @ColumnInfo(name = "stock")
    public int stock;

    public ShoppingList(long id, String name, int quantity, int stock) {
        this.id = id;
        this.name = name;
        /*this.pantryId = pantryId;
        this.itemId = itemId;*/
        this.quantity = quantity;
        this.stock = stock;
    }

    @Ignore
    public ShoppingList(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Ignore
    public ShoppingList(String name) {
        this.name = name;
    }
}
